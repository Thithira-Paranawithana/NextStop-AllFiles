package com.busticket.notificationservice.dto;

import lombok.Data;

import java.util.List;

@Data
public class BookingConfirmationRequest {
    private String email;
    private String passengerName;
    private Integer bookingId;
    private String route;
    private String travelDate;
    private String departureTime;
    private String busNumber;
    private String operatorName;
    private List<String> seatNumbers;
    private Integer numberOfSeats;
    private Double pricePerSeat;
    private Double totalAmount;
}
