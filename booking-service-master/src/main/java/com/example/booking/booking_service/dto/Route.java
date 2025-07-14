package com.example.booking.booking_service.dto;

import lombok.Data;

@Data
public class Route {
    private Integer id;
    private String sourceCity;
    private String destinationCity;
    private Double distanceKm;
    private String duration;
    private String status;
}