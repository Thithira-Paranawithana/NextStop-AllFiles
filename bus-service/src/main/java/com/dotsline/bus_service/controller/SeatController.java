package com.dotsline.bus_service.controller;

import com.dotsline.bus_service.model.Seat;
import com.dotsline.bus_service.service.SeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("seats")
public class SeatController {

    @Autowired
    private SeatService seatService;

    @GetMapping("/bus/{busId}")
    public ResponseEntity<List<Seat>> getSeatsByBusId(@PathVariable Integer busId) {
        List<Seat> seats = seatService.getSeatsByBusId(busId);
        return ResponseEntity.ok(seats);
    }


    @GetMapping("/seats/{id}")
    public ResponseEntity<Seat> getSeatById(@PathVariable Integer id) {
        return seatService.getSeatById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/bus/{busId}/seat/{seatNumber}")
    public ResponseEntity<Seat> getSeatByBusIdAndSeatNumber(
            @PathVariable Integer busId,
            @PathVariable String seatNumber) {

        Optional<Seat> seat = seatService.findByBusIdAndSeatNumber(busId, seatNumber);
        if (seat.isPresent()) {
            return ResponseEntity.ok(seat.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
