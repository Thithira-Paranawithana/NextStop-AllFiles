
package com.busticket.notificationservice.service;

import com.busticket.notificationservice.dto.BookingConfirmationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailSenderService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.from:quickbus.busservices@gmail.com}")
    private String from;

    public void sendSimpleEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }

    public void sendBookingConfirmationEmail(BookingConfirmationRequest request) {
        String subject = "Booking Confirmation - NextStop Bus Services";
        String body = buildBookingConfirmationEmailBody(request);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(request.getEmail());
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }

    private String buildBookingConfirmationEmailBody(BookingConfirmationRequest request) {
        StringBuilder body = new StringBuilder();

        body.append("Dear ").append(request.getPassengerName()).append(",\n\n");
        body.append("Your bus booking has been confirmed! Here are your booking details:\n\n");

        body.append("=== BOOKING DETAILS ===\n");
        body.append("Booking ID: #").append(request.getBookingId()).append("\n");
        body.append("Route: ").append(request.getRoute()).append("\n");
        body.append("Travel Date: ").append(request.getTravelDate()).append("\n");
        body.append("Departure Time: ").append(request.getDepartureTime()).append("\n\n");

        body.append("=== BUS INFORMATION ===\n");
        body.append("Bus Number: ").append(request.getBusNumber()).append("\n");
        body.append("Operator: ").append(request.getOperatorName()).append("\n\n");

        body.append("=== SEAT & PAYMENT DETAILS ===\n");
        body.append("Number of Seats: ").append(request.getNumberOfSeats()).append("\n");
        body.append("Seat Numbers: ").append(String.join(", ", request.getSeatNumbers())).append("\n");
        body.append("Price per Seat: LKR ").append(String.format("%.2f", request.getPricePerSeat())).append("\n");
        body.append("Total Amount: LKR ").append(String.format("%.2f", request.getTotalAmount())).append("\n\n");

        body.append("=== IMPORTANT NOTES ===\n");
        body.append("• Please arrive 15 minutes before departure time\n");
        body.append("• Carry a valid ID and softcopy of the ticket for verification\n");
        body.append("• Keep this email or download your ticket for the journey\n");
        body.append("• This ticket is non-transferable\n\n");

        body.append("Thank you for choosing NextStop Bus Services!\n");
        body.append("Have a safe journey!\n\n");
        body.append("Best regards,\n");
        body.append("NextStop Team\n");
        body.append("Email: quickbus.busservices@gmail.com");

        return body.toString();
    }
}
