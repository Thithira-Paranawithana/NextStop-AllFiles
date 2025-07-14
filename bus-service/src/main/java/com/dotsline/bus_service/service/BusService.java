package com.dotsline.bus_service.service;

import com.dotsline.bus_service.dao.BusRepository;
import com.dotsline.bus_service.model.Bus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BusService {

    @Autowired
    private BusRepository busRepository;

    @Autowired
    private SeatService seatService;

    public Bus createBus(Bus bus) {
        Bus savedBus = busRepository.save(bus);
        seatService.createSeatsForBus(savedBus); // Create seats after saving
        return savedBus;
//        return busRepository.save(bus);
    }

    public List<Bus> getAllBuses() {
        return busRepository.findAll();
    }

    public Optional<Bus> getBusById(Integer id) {
        return busRepository.findById(id);
    }

    public Bus updateBus(Integer id, Bus updatedBus) {
        Bus existingBus = busRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bus not found with id: " + id));

        existingBus.setBusNumber(updatedBus.getBusNumber());
        existingBus.setType(updatedBus.getType());
        existingBus.setTotalSeats(updatedBus.getTotalSeats());
        existingBus.setOperatorName(updatedBus.getOperatorName());
        existingBus.setStatus(updatedBus.getStatus());

        return busRepository.save(existingBus);
    }

}
