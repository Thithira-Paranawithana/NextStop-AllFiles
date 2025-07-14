package com.dotsline.bus_service.dao;

import com.dotsline.bus_service.model.BusRouteSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface BusRouteScheduleRepository extends JpaRepository<BusRouteSchedule, Integer> {
    @Query("SELECT s FROM BusRouteSchedule s " +
            "WHERE s.route.sourceCity = :sourceCity " +
            "AND s.route.destinationCity = :destinationCity")
//            "AND DATE(s.departureTime) = :travelDate")
    List<BusRouteSchedule> findSchedulesBySourceDestinationAndDate(
            @Param("sourceCity") String sourceCity,
            @Param("destinationCity") String destinationCity,
            @Param("travelDate") LocalDate travelDate);
}
