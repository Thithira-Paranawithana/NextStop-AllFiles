package com.dotsline.bus_service.service;

import com.dotsline.bus_service.dao.BusRouteScheduleRepository;
import com.dotsline.bus_service.model.BusRouteSchedule;
import com.dotsline.bus_service.model.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class BusRouteScheduleService {

    @Autowired
    private BusRouteScheduleRepository scheduleRepository;

    // add a schedule
    public BusRouteSchedule addSchedule(BusRouteSchedule schedule) {
        return scheduleRepository.save(schedule);
    }

    // get all the schedules stored
    public List<BusRouteSchedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }

    // get a schedule by id
    public Optional<BusRouteSchedule> getScheduleById(Integer id) {
        return scheduleRepository.findById(id);
    }

    public void deleteScheduleById(Integer id) {
        scheduleRepository.deleteById(id);
    }

    // Update schedule
    public Optional<BusRouteSchedule> updateSchedule(Integer id, BusRouteSchedule updatedSchedule) {
        return scheduleRepository.findById(id).map(existingSchedule -> {
            existingSchedule.setBus(updatedSchedule.getBus());
            existingSchedule.setRoute(updatedSchedule.getRoute());
            existingSchedule.setDepartureTime(updatedSchedule.getDepartureTime());
            existingSchedule.setArrivalTime(updatedSchedule.getArrivalTime());
            existingSchedule.setFare(updatedSchedule.getFare());
            existingSchedule.setStatus(updatedSchedule.getStatus());
            return scheduleRepository.save(existingSchedule);
        });
    }

    // get filtered schedules based on source, destination and travel date
    public List<BusRouteSchedule> getFilteredSchedules(String sourceCity, String destinationCity, LocalDate travelDate) {
        return scheduleRepository.findSchedulesBySourceDestinationAndDate(sourceCity, destinationCity, travelDate);
    }


}
