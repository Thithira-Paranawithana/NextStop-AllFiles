package com.dotsline.bus_service.dao;

import com.dotsline.bus_service.model.Route;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RouteRepository extends JpaRepository<Route, Integer> {
}
