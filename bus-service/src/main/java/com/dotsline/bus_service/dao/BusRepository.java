package com.dotsline.bus_service.dao;

import com.dotsline.bus_service.model.Bus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BusRepository extends JpaRepository<Bus, Integer> {
}
