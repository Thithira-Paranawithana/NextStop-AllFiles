package com.example.booking.booking_service.model;

import lombok.Data;

@Data
public class Seat {
    private Integer id;
    private String seatNumber;
    private Boolean isReserved;
}