package com.busticket.user_service.service;

import com.busticket.user_service.exception.EmailServiceException;
import jakarta.annotation.PostConstruct;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URI;


@Service
@RequiredArgsConstructor
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;

    @Value("${app.base-url}")
    private String baseUrl;

    @PostConstruct
    public void validateBaseUrl() {
        if (baseUrl == null || baseUrl.trim().isEmpty()) {
            throw new IllegalStateException("app.base-url property is not set");
        }
        try {
            @SuppressWarnings("unused")
            var url = URI.create(baseUrl).toURL(); // Validation only
        } catch (MalformedURLException | IllegalArgumentException e) {
            throw new IllegalStateException("Invalid app.base-url: " + baseUrl, e);
        }
    }

    public void sendVerificationEmail(String to, String token) {
        String subject = "Verify your email";
        String verificationLink = baseUrl + "/api/auth/verify-email/" + token;
        String content = "<p>Please click the link below to verify your email for NextStop:</p>" +
                "<a href=\"" + verificationLink + "\">Verify Now</a>";

        sendEmail(to, subject, content);
    }

    public void sendPasswordResetEmail(String to, String token) {
        String subject = "Reset your password";
        String resetLink = baseUrl + "/reset-password?token=" + token;
        String content = "<p>You requested a password reset. Click the link below to reset your password:</p>" +
                "<a href=\"" + resetLink + "\">Reset Password</a>";

        sendEmail(to, subject, content);
    }

    private void sendEmail(String to, String subject, String htmlContent) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            logger.error("Failed to send email to {}: {}", to, e.getMessage());
            throw new EmailServiceException("Failed to send email to " + to, e);
        }
    }
}
