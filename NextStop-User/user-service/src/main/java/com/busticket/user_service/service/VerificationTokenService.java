package com.busticket.user_service.service;

import com.busticket.user_service.exception.UserServiceException;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class VerificationTokenService {

    private final RedisTemplate<String, String> redisTemplate;
    private final MeterRegistry meterRegistry;

    private static final Logger logger = LoggerFactory.getLogger(VerificationTokenService.class);
    private static final String VERIFICATION_PREFIX = "verify:";
    private static final String PASSWORD_RESET_PREFIX = "reset:";

    @Value("${token.verification.expiration.minutes}")
    private long verificationExpirationMinutes;

    @Value("${token.password-reset.expiration.minutes}")
    private long passwordResetExpirationMinutes;

    @PostConstruct
    public void validateExpirations() {
        if (verificationExpirationMinutes <= 0 || passwordResetExpirationMinutes <= 0) {
            throw new IllegalStateException("Token expiration times must be positive");
        }
    }

    public String createVerificationToken(String email) {
        String rateLimitKey = "ratelimit:verify:" + email;
        Long attempts = redisTemplate.opsForValue().increment(rateLimitKey);
        if (attempts == null || attempts > 5) {
            logger.warn("Rate limit exceeded for email: {}", email);
            throw new UserServiceException("Too many verification attempts");
        }
        redisTemplate.expire(rateLimitKey, 1, TimeUnit.HOURS);

        String token = UUID.randomUUID().toString();
        String key = VERIFICATION_PREFIX + token;
        redisTemplate.opsForValue().set(key, email, verificationExpirationMinutes, TimeUnit.MINUTES);
        meterRegistry.counter("token.created", "type", "verification").increment();
        logger.info("Created verification token for email: {}, key: {}", email, key);
        return token;
    }

    public String createPasswordResetToken(String email) {
        String token = UUID.randomUUID().toString();
        String key = PASSWORD_RESET_PREFIX + token;
        redisTemplate.opsForValue().set(key, email, passwordResetExpirationMinutes, TimeUnit.MINUTES);
        meterRegistry.counter("token.created", "type", "password-reset").increment();
        logger.info("Created password reset token for email: {}, key: {}", email, key);
        return token;
    }

    public String getEmailFromToken(String token, boolean isPasswordReset) {
        String prefix = isPasswordReset ? PASSWORD_RESET_PREFIX : VERIFICATION_PREFIX;
        String key = prefix + token;
        String email = redisTemplate.opsForValue().get(key);
        if (email == null) {
            meterRegistry.counter("token.validation.failed", "type", isPasswordReset ? "password-reset" : "verification").increment();
            logger.warn("Invalid or expired token: {}", key);
            throw new UserServiceException("Invalid or expired token");
        }

        redisTemplate.delete(key);
        meterRegistry.counter("token.validated", "type", isPasswordReset ? "password-reset" : "verification").increment();
        logger.info("Retrieved and deleted token: {} for email: {}", key, email);
        return email;

    }
}
