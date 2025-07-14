package com.example.booking.booking_service.controller;

import com.example.booking.booking_service.dto.BookingRequest;
import com.example.booking.booking_service.dto.BookingResponseDTO;
import com.example.booking.booking_service.dto.SeatAvailabilityResponse;
import com.example.booking.booking_service.dto.SeatMappingRequest;
import com.example.booking.booking_service.model.Booking;
import com.example.booking.booking_service.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/v1/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @GetMapping("/all")
    public ResponseEntity<List<Booking>> getAllRawBookings() {
        List<Booking> bookings = bookingService.getAllRawBookings();
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/get-booking/{id}")
    public ResponseEntity<BookingResponseDTO> getBookingDetails(@PathVariable Integer id) {
        return ResponseEntity.ok(bookingService.getBookingDetailsById(id));
    }

    @PostMapping("/book-seat")
    public ResponseEntity<Booking> bookSeat(@RequestBody BookingRequest request) {
        return ResponseEntity.ok(bookingService.bookSeat(request));
    }

    @GetMapping("/seats/availability/{scheduleId}")
    public ResponseEntity<List<SeatAvailabilityResponse>> getSeatAvailability(
            @PathVariable Integer scheduleId,
            @RequestParam("travelDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate travelDate) {

        List<SeatAvailabilityResponse> availabilityList = bookingService.getSeatAvailability(scheduleId, travelDate);
        return ResponseEntity.ok(availabilityList);
    }

    @PostMapping("/seats/map-seat-numbers")
    public ResponseEntity<List<Integer>> mapSeatNumbersToIds(@RequestBody SeatMappingRequest request) {
        List<Integer> seatIds = bookingService.mapSeatNumbersToIds(request.getScheduleId(), request.getSeatNumbers());
        return ResponseEntity.ok(seatIds);
    }


    @GetMapping("/userBookings/{userId}")
    public ResponseEntity<List<BookingResponseDTO>> getBookingsByUserId(@PathVariable Integer userId) {
        return ResponseEntity.ok(bookingService.getBookingsByUserId(userId));
    }

    @DeleteMapping("/cancel/{bookingId}")
    public ResponseEntity<String> cancelBooking(@PathVariable Integer bookingId) {
        bookingService.cancelBookingById(bookingId);
        return ResponseEntity.ok("Booking cancelled successfully.");
    }

}
