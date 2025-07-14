
package com.example.booking.booking_service.model;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Entity
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer userId;
    private Integer scheduleId;
    private String seatNumbers;
    private Integer numberOfSeats;
    private Double pricePerSeat;
    private Double totalPrice;
    private String source;
    private String destination;
    private LocalDateTime bookingDateTime;
    private LocalDate travelDate;
    private LocalTime departureTime;
}
