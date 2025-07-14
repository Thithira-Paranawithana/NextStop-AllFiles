package com.example.booking.booking_service.dto;

import lombok.Data;
import java.util.List;

@Data
public class SeatMappingRequest {
    private Integer scheduleId;
    private List<String> seatNumbers;
}