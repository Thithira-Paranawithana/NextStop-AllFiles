
package com.busticket.notificationservice.controller;

import com.busticket.notificationservice.dto.BookingConfirmationRequest;
import com.busticket.notificationservice.service.EmailSenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

        import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final EmailSenderService emailSenderService;

    @PostMapping("/welcome")
    public ResponseEntity<String> sendWelcomeEmail(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        emailSenderService.sendSimpleEmail(email, "Welcome to NextStop!", "Thanks for registering.");
        return ResponseEntity.ok("Welcome email sent to: " + email);
    }

    @PostMapping("/password-reset")
    public ResponseEntity<String> sendPasswordReset(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        emailSenderService.sendSimpleEmail(email, "Reset Your Password", "Click the link to reset your password.");
        return ResponseEntity.ok("Password reset email sent to: " + email);
    }

    @PostMapping("/booking-confirmation")
    public ResponseEntity<String> sendBookingConfirmation(@RequestBody BookingConfirmationRequest request) {
        emailSenderService.sendBookingConfirmationEmail(request);
        return ResponseEntity.ok("Booking confirmation email sent to: " + request.getEmail());
    }
}