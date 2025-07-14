package com.busticket.user_service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class NotificationRestClient {

    private static final Logger logger = LoggerFactory.getLogger(NotificationRestClient.class);
    private static final String NOTIFICATION_QUEUE = "notification:queue";

    private final RestTemplate restTemplate;
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    @Value("${notification.service.url}")
    private String notificationServiceUrl;

    public NotificationRestClient(RestTemplate restTemplate, RedisTemplate<String, String> redisTemplate,
                                  ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    @Retryable(
            retryFor = {HttpClientErrorException.class, HttpServerErrorException.class},
            backoff = @Backoff(delay = 1000, multiplier = 2)
    )
    public void sendUserRegisteredNotification(String email) {
        String url = notificationServiceUrl + "/api/notifications/welcome";
        sendEmailPayload(url, email);
    }

    @Recover
    public void recoverUserRegisteredNotification(Throwable t, String email) {
        logger.error("Failed to send user registered notification for {}: {}", email, t.getMessage());
        queueNotification("welcome", email);
    }

    @Retryable(
            retryFor = {HttpClientErrorException.class, HttpServerErrorException.class},
            backoff = @Backoff(delay = 1000, multiplier = 2)
    )
    public void sendPasswordResetNotification(String email) {
        String url = notificationServiceUrl + "/api/notifications/password-reset";
        sendEmailPayload(url, email);
    }

    @Recover
    public void recoverPasswordResetNotification(Throwable t, String email) {
        logger.error("Failed to send password reset notification for {}: {}", email, t.getMessage());
        queueNotification("password-reset", email);
    }

    private void sendEmailPayload(String url, String email) {
        Map<String, String> payload = new HashMap<>();
        payload.put("email", email);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(payload, headers);
        restTemplate.postForEntity(url, request, Void.class);
    }

    private void queueNotification(String type, String email) {
        try {
            Map<String, String> notification = new HashMap<>();
            notification.put("type", type);
            notification.put("email", email);
            String notificationJson = objectMapper.writeValueAsString(notification);
            redisTemplate.opsForList().rightPush(NOTIFICATION_QUEUE, notificationJson);
            redisTemplate.expire(NOTIFICATION_QUEUE, 7, TimeUnit.DAYS);
            logger.info("Queued notification of type {} for {}", type, email);
        } catch (JsonProcessingException e) {
            logger.error("Failed to queue notification for {}: {}", email, e.getMessage());
        }
    }

}
