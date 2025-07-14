package com.example.booking.booking_service.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
public class BookingSeat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer seatId;      // references seat in bus service
    private Integer scheduleId;  // references schedule in bus service
    private LocalDate travelDate;

    @ManyToOne
    @JoinColumn(name = "booking_id")
    private Booking booking;
}
