package com.busticket.user_service.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
public class NotificationRetryTask {

    private static final Logger logger = LoggerFactory.getLogger(NotificationRetryTask.class);
    private static final String NOTIFICATION_QUEUE = "notification:queue";

    private final RestTemplate restTemplate;
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;
    private final String notificationServiceUrl;

    public NotificationRetryTask(RestTemplate restTemplate, RedisTemplate<String, String> redisTemplate,
                                 ObjectMapper objectMapper, @Value("${notification.service.url}") String notificationServiceUrl) {
        this.restTemplate = restTemplate;
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
        this.notificationServiceUrl = notificationServiceUrl;
    }

    @Scheduled(fixedRate = 60000)
    public void retryNotifications() {
        Long queueSize = redisTemplate.opsForList().size(NOTIFICATION_QUEUE);
        if (queueSize == null || queueSize == 0) {
            return;
        }

        // Process notifications while queue is not empty
        while (queueSize > 0) {
            String notificationJson = redisTemplate.opsForList().leftPop(NOTIFICATION_QUEUE);
            if (notificationJson == null) {
                break;
            }

            try {
                // Fix: Use TypeReference for proper generic type handling
                Map<String, String> notification = objectMapper.readValue(notificationJson,
                        new TypeReference<>() {
                        });

                if (sendNotification(notification)) {
                    logger.info("Successfully sent queued notification of type {} for {}",
                            notification.get("type"), notification.get("email"));
                }
            } catch (Exception e) {
                logger.error("Failed to retry notification: {}", e.getMessage());
                // Put failed notification back to the end of queue
                redisTemplate.opsForList().rightPush(NOTIFICATION_QUEUE, notificationJson);
            }

            // Fix: Update queue size after each iteration
            queueSize = redisTemplate.opsForList().size(NOTIFICATION_QUEUE);
            if (queueSize == null) {
                queueSize = 0L;
            }
        }
    }

    // Extract method to eliminate code duplication
    private boolean sendNotification(Map<String, String> notification) {
        String type = notification.get("type");
        String email = notification.get("email");

        if (type == null || email == null) {
            logger.warn("Invalid notification data: missing type or email");
            return false;
        }

        String url = notificationServiceUrl + "/api/notifications/" + type;

        Map<String, String> payload = createPayload(email);
        HttpHeaders headers = createHeaders();
        HttpEntity<Map<String, String>> request = new HttpEntity<>(payload, headers);

        try {
            restTemplate.postForEntity(url, request, Void.class);
            return true;
        } catch (Exception e) {
            logger.error("Failed to send notification to {}: {}", url, e.getMessage());
            throw e; // Re-throw to trigger retry logic
        }
    }

    private Map<String, String> createPayload(String email) {
        Map<String, String> payload = new HashMap<>();
        payload.put("email", email);
        return payload;
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}