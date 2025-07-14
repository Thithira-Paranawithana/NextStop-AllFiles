package com.dotsline.bus_service.dao;

import com.dotsline.bus_service.model.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SeatRepository extends JpaRepository<Seat, Integer> {
    List<Seat> findByBusId(Integer busId);

    Optional<Seat> findByBusIdAndSeatNumber(Integer busId, String seatNumber);
}
