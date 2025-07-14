package com.example.booking.booking_service.dto;

import lombok.Data;

@Data
public class Bus {
    private Integer id;
    private String busNumber;
    private String type;
    private Integer totalSeats;
    private String operatorName;
    private String status;

}