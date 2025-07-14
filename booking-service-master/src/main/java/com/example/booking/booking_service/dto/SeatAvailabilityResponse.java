package com.example.booking.booking_service.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeatAvailabilityResponse {
    private Integer seatId;
    private String seatNumber;
    private boolean available;
}
