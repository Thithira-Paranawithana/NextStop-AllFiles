package com.dotsline.bus_service.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String seatNumber; // S1, S2

//    private Boolean isReserved = false;

    @ManyToOne
    @JoinColumn(name = "bus_id")
    private Bus bus;
}
