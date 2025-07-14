package com.dotsline.bus_service.service;

import com.dotsline.bus_service.dao.SeatRepository;
import com.dotsline.bus_service.model.Bus;
import com.dotsline.bus_service.model.Seat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SeatService {

    @Autowired
    private SeatRepository seatRepository;

    public void createSeatsForBus(Bus bus) {
        int totalSeats = bus.getTotalSeats();
        List<Seat> seats = new ArrayList<>();
        for (int i = 1; i <= totalSeats; i++) {
            Seat seat = new Seat();
            seat.setSeatNumber("S" + i);
//            seat.setIsReserved(false);
            seat.setBus(bus);
            seats.add(seat);
        }
        seatRepository.saveAll(seats);
    }

    public List<Seat> getSeatsByBusId(Integer busId) {
        return seatRepository.findByBusId(busId);
    }

    public Optional<Seat> updateSeat(Integer id, Seat updatedSeat) {
        return seatRepository.findById(id).map(existingSeat -> {
            existingSeat.setSeatNumber(updatedSeat.getSeatNumber());
//            existingSeat.setIsReserved(updatedSeat.getIsReserved());
            return seatRepository.save(existingSeat);
        });
    }

    public Optional<Seat> getSeatById(Integer id) {
        return seatRepository.findById(id);
    }

    public Optional<Seat> findByBusIdAndSeatNumber(Integer busId, String seatNumber) {
        return seatRepository.findByBusIdAndSeatNumber(busId, seatNumber);
    }

}
