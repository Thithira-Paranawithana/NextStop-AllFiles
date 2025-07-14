package com.example.booking.booking_service.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class BusRouteSchedule {
    private Integer id;
    private Bus bus;
    private Route route;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private Double fare;
    private String status;
}