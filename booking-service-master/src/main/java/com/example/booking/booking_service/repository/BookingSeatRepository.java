package com.example.booking.booking_service.repository;

import com.example.booking.booking_service.model.BookingSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

public interface BookingSeatRepository extends JpaRepository<BookingSeat, Integer> {

    List<BookingSeat> findByScheduleIdAndTravelDate(Integer scheduleId, LocalDate travelDate);

    List<BookingSeat> findByScheduleIdAndTravelDateAndSeatId(Integer scheduleId, LocalDate travelDate, Integer seatId);

    @Query("SELECT bs.seatId FROM BookingSeat bs WHERE bs.scheduleId = :scheduleId AND bs.travelDate = :travelDate")
    List<Integer> findSeatIdsByScheduleIdAndTravelDate(@Param("scheduleId") Integer scheduleId,
                                                       @Param("travelDate") LocalDate travelDate);

    @Transactional
    @Modifying
    @Query("DELETE FROM BookingSeat bs WHERE bs.booking.id = :bookingId")
    void deleteByBookingId(@Param("bookingId") Integer bookingId);

}
