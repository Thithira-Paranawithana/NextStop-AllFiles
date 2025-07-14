package com.dotsline.bus_service.controller;

import com.dotsline.bus_service.model.BusRouteSchedule;
import com.dotsline.bus_service.model.Route;
import com.dotsline.bus_service.service.BusRouteScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("schedules")
public class BusRouteScheduleController {

    @Autowired
    private BusRouteScheduleService scheduleService;

    @PostMapping("addSchedule")
    public ResponseEntity<BusRouteSchedule> addSchedule(@RequestBody BusRouteSchedule schedule) {
        return ResponseEntity.ok(scheduleService.addSchedule(schedule));
    }

    @GetMapping("getAllSchedules")
    public ResponseEntity<List<BusRouteSchedule>> getAllSchedules() {
        return ResponseEntity.ok(scheduleService.getAllSchedules());
    }

    @GetMapping("getSchedule/{id}")
    public ResponseEntity<BusRouteSchedule> getScheduleById(@PathVariable Integer id) {
        return scheduleService.getScheduleById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<String> deleteSchedule(@PathVariable Integer id) {
        scheduleService.deleteScheduleById(id);
        return ResponseEntity.ok("Schedule deleted successfully.");
    }

    @PutMapping("updateSchedule/{id}")
    public ResponseEntity<BusRouteSchedule> updateSchedule(@PathVariable Integer id, @RequestBody BusRouteSchedule updatedSchedule) {
        return scheduleService.updateSchedule(id, updatedSchedule)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // get filtered schedules based on source, destination and travel date
    @GetMapping("/filter")
    public ResponseEntity<List<BusRouteSchedule>> filterSchedules(
            @RequestParam String sourceCity,
            @RequestParam String destinationCity,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate travelDate) {

        List<BusRouteSchedule> schedules = scheduleService
                .getFilteredSchedules(sourceCity, destinationCity, travelDate);
    
        return ResponseEntity.ok(schedules);
    }



}
