
package com.example.booking.booking_service.dto;
import lombok.Data;

import java.util.List;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class BookingRequest {
    public Integer userId;
    private Integer scheduleId;
    private List<Integer> seatIds;
    private LocalDate travelDate;
    private LocalTime departureTime;
}

