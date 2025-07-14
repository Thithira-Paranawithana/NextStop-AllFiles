package com.example.booking.booking_service.service;

import com.example.booking.booking_service.dto.*;
import com.example.booking.booking_service.feign.BusInterface;
import com.example.booking.booking_service.model.Booking;
import com.example.booking.booking_service.model.BookingSeat;
import com.example.booking.booking_service.model.Seat;
import com.example.booking.booking_service.repository.BookingRepository;
import com.example.booking.booking_service.repository.BookingSeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingService {

    @Autowired
    private BusInterface busInterface;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private BookingSeatRepository bookingSeatRepository;


    public List<Integer> mapSeatNumbersToIds(Integer scheduleId, List<String> seatNumbers) {
        try {
            // Get schedule to get bus ID
            BusRouteSchedule schedule = busInterface.getScheduleById(scheduleId).getBody();
            if (schedule == null) {
                throw new RuntimeException("Schedule not found with ID: " + scheduleId);
            }

            Integer busId = schedule.getBus().getId();
            List<Integer> seatIds = new ArrayList<>();

            // Map each seat number to its corresponding seat ID
            for (String seatNumber : seatNumbers) {
                try {
                    ResponseEntity<Seat> seatResponse = busInterface.getSeatByBusIdAndSeatNumber(busId, seatNumber);
                    Seat seat = seatResponse.getBody();

                    if (seat != null) {
                        seatIds.add(seat.getId());
                    } else {
                        throw new RuntimeException("Seat not found: " + seatNumber + " for bus ID: " + busId);
                    }
                } catch (Exception e) {
                    throw new RuntimeException("Error mapping seat number " + seatNumber + ": " + e.getMessage());
                }
            }

            return seatIds;
        } catch (Exception e) {
            throw new RuntimeException("Error mapping seat numbers to IDs: " + e.getMessage());
        }
    }


    // Get booking details by booking id
    public BookingResponseDTO getBookingDetailsById(Integer bookingId) {
        // Find the booking
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        // Get seatNumbers from seatIds
        String[] seatIdStrings = booking.getSeatNumbers().split(",");
        List<String> seatNumbers = new ArrayList<>();
        for (String seatIdStr : seatIdStrings) {
            Integer seatId = Integer.valueOf(seatIdStr.trim());
            Seat seat = busInterface.getSeat(seatId).getBody();
            seatNumbers.add(seat.getSeatNumber());
        }

        // get bus details from the schedule
        BusRouteSchedule schedule = busInterface.getScheduleById(booking.getScheduleId()).getBody();
        Bus bus = schedule.getBus();

        // create response
        BookingResponseDTO response = new BookingResponseDTO();
        response.setBookingId(booking.getId());
        response.setUserId(booking.getUserId());
        response.setSeatNumbers(seatNumbers);
        response.setNumberOfSeats(booking.getNumberOfSeats());
        response.setPricePerSeat(booking.getPricePerSeat());
        response.setTotalPrice(booking.getTotalPrice());
        response.setSource(booking.getSource());
        response.setDestination(booking.getDestination());
        response.setDepartureTime(booking.getDepartureTime());
        response.setTravelDate(booking.getTravelDate());
        response.setBookingDateTime(booking.getBookingDateTime());
        response.setBusNumber(bus.getBusNumber());
        response.setBusType(bus.getType());
        response.setOperatorName(bus.getOperatorName());

        return response;
    }

    // get booking details by user id
    public List<BookingResponseDTO> getBookingsByUserId(Integer userId) {
        // find bookings
        List<Booking> bookings = bookingRepository.findByUserId(userId);

        return bookings.stream().map(booking -> {
            // convert seatIds to seatNumbers
            String[] seatIdStrings = booking.getSeatNumbers().split(",");
            List<String> seatNumbers = new ArrayList<>();

            for (String seatIdStr : seatIdStrings) {
                Integer seatId = Integer.valueOf(seatIdStr.trim());
                Seat seat = busInterface.getSeat(seatId).getBody();
                seatNumbers.add(seat.getSeatNumber());
            }

            // get bus details from the schedule
            BusRouteSchedule schedule = busInterface.getScheduleById(booking.getScheduleId()).getBody();
            Bus bus = schedule.getBus();

            // create response
            BookingResponseDTO response = new BookingResponseDTO();
            response.setBookingId(booking.getId());
            response.setUserId(booking.getUserId());
            response.setSeatNumbers(seatNumbers);
            response.setNumberOfSeats(booking.getNumberOfSeats());
            response.setPricePerSeat(booking.getPricePerSeat());
            response.setTotalPrice(booking.getTotalPrice());
            response.setSource(booking.getSource());
            response.setDestination(booking.getDestination());
            response.setDepartureTime(booking.getDepartureTime());
            response.setTravelDate(booking.getTravelDate());
            response.setBookingDateTime(booking.getBookingDateTime());
            response.setBusNumber(bus.getBusNumber());
            response.setBusType(bus.getType());
            response.setOperatorName(bus.getOperatorName());

            return response;
        }).collect(Collectors.toList());
    }


    public Booking bookSeat(BookingRequest request) {
        // Get schedule details
        BusRouteSchedule schedule = busInterface.getScheduleById(request.getScheduleId()).getBody();

        // Check if seats are available
        for (Integer seatId : request.getSeatIds()) {
            if (!bookingSeatRepository.findByScheduleIdAndTravelDateAndSeatId(request.getScheduleId(), request.getTravelDate(), seatId).isEmpty()) {
                throw new RuntimeException("Seat " + seatId + " is already booked for this trip!");
            }
        }

        // calculate total price
        double pricePerSeat = schedule.getFare();
        double totalPrice = pricePerSeat * request.getSeatIds().size();

        // Save booking
        Booking booking = new Booking();
        booking.setUserId(request.getUserId()); // Hardcoded user ID for now
        booking.setScheduleId(schedule.getId());
        booking.setSeatNumbers(request.getSeatIds().stream().map(Object::toString).collect(Collectors.joining(",")));
        booking.setNumberOfSeats(request.getSeatIds().size());
        booking.setPricePerSeat(pricePerSeat);
        booking.setTotalPrice(totalPrice);
        booking.setSource(schedule.getRoute().getSourceCity());
        booking.setDestination(schedule.getRoute().getDestinationCity());
        booking.setDepartureTime(schedule.getDepartureTime().toLocalTime());
        booking.setTravelDate(request.getTravelDate());
        booking.setBookingDateTime(LocalDateTime.now());

        Booking savedBooking = bookingRepository.save(booking);

        // Save seat reservations
        for (Integer seatId : request.getSeatIds()) {
            BookingSeat bookingSeat = new BookingSeat();
            bookingSeat.setSeatId(seatId);
            bookingSeat.setScheduleId(schedule.getId());
            bookingSeat.setTravelDate(request.getTravelDate());
            bookingSeat.setBooking(savedBooking);
            bookingSeatRepository.save(bookingSeat);
        }

        return savedBooking;
    }

    // check seat availability
    public List<SeatAvailabilityResponse> getSeatAvailability(Integer scheduleId, LocalDate travelDate) {
        // Get schedule from BUS-SERVICE (to get busId)
        BusRouteSchedule schedule = busInterface.getScheduleById(scheduleId).getBody();
        Integer busId = schedule.getBus().getId();

        // Get all seats of the bus
        ResponseEntity<Seat[]> seatResponse = busInterface.getSeatsByBusId(busId);
        List<Seat> seats = Arrays.asList(seatResponse.getBody());

        // get booked seat IDs for this scheduleId and travelDate
        List<Integer> bookedSeatIds = bookingSeatRepository.findSeatIdsByScheduleIdAndTravelDate(scheduleId, travelDate);

        // create response
        return seats.stream()
                .map(seat -> {
                    boolean isBooked = bookedSeatIds.contains(seat.getId());
                    return new SeatAvailabilityResponse(seat.getId(), seat.getSeatNumber(), !isBooked);
                })
                .collect(Collectors.toList());
    }

    public List<Booking> getAllRawBookings() {
        return bookingRepository.findAll();
    }

    public void cancelBookingById(Integer bookingId) {
        // Check if booking exists
        Optional<Booking> optionalBooking = bookingRepository.findById(bookingId);
        if (!optionalBooking.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Booking not found");
        }

        // Delete seat records from booking_seat table
        bookingSeatRepository.deleteByBookingId(bookingId);

        // Delete the booking record
        bookingRepository.deleteById(bookingId);
    }

}