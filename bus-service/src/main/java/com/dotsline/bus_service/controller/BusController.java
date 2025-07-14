package com.dotsline.bus_service.controller;

import com.dotsline.bus_service.model.Bus;
import com.dotsline.bus_service.model.Route;
import com.dotsline.bus_service.service.BusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bus")
public class BusController {

    @Autowired
    private BusService busService;

    @PostMapping("createBus")
    public ResponseEntity<Bus> createBus(@RequestBody Bus bus) {
        return ResponseEntity.ok(busService.createBus(bus));
    }

    @GetMapping("getAllBuses")
    public ResponseEntity<List<Bus>> getAllBuses() {
        return ResponseEntity.ok(busService.getAllBuses());
    }

    @GetMapping("getBus/{busId}")
    public ResponseEntity<Bus> getBusById(@PathVariable Integer busId) {
        return busService.getBusById(busId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("updateBus/{id}")
    public ResponseEntity<Bus> updateBus(@PathVariable Integer id, @RequestBody Bus updatedBus) {
        Bus bus = busService.updateBus(id, updatedBus);
        return ResponseEntity.ok(bus);
    }

}
