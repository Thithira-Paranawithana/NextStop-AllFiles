package com.example.booking.booking_service.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Data
public class BookingResponseDTO {
    private Integer bookingId;
    private Integer userId;
    private List<String> seatNumbers;
    private Integer numberOfSeats;
    private double pricePerSeat;
    private double totalPrice;
    private String source;
    private String destination;
    private LocalTime departureTime;
    private LocalDate travelDate;
    private LocalDateTime bookingDateTime;

    // Bus details
    private String busNumber;
    private String busType;
    private String operatorName;
}
