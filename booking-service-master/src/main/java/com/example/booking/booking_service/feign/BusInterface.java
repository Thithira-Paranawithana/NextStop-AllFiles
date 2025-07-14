package com.example.booking.booking_service.feign;

import com.example.booking.booking_service.dto.BusRouteSchedule;
import com.example.booking.booking_service.model.Seat;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@FeignClient("BUS-SERVICE")
public interface BusInterface {

    @GetMapping("/seats/seats/{id}")
    ResponseEntity<Seat> getSeat(@PathVariable("id") Integer id);

    @GetMapping("/seats/bus/{busId}")
    ResponseEntity<Seat[]> getSeatsByBusId(@PathVariable("busId") Integer busId);

    @GetMapping("/schedules/getSchedule/{id}")
    ResponseEntity<BusRouteSchedule> getScheduleById(@PathVariable("id") Integer id);

    @GetMapping("/seats/bus/{busId}/seat/{seatNumber}")
    ResponseEntity<Seat> getSeatByBusIdAndSeatNumber(
            @PathVariable("busId") Integer busId,
            @PathVariable("seatNumber") String seatNumber
    );

}



