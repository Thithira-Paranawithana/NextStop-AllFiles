package com.dotsline.bus_service.service;

import com.dotsline.bus_service.dao.RouteRepository;
import com.dotsline.bus_service.model.Bus;
import com.dotsline.bus_service.model.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RouteService {

    @Autowired
    private RouteRepository routeRepository;

    public Route addRoute(Route route) {
        return routeRepository.save(route);
    }

    public List<Route> getAllRoutes() {
        return routeRepository.findAll();
    }

    public Optional<Route> getRouteById(Integer id) {
        return routeRepository.findById(id);
    }

    public void deleteRouteById(Integer id) {
        routeRepository.deleteById(id);
    }

    public Route updateRoute(Integer id, Route updatedRoute) {
        Route existingRoute = routeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Route not found with id: " + id));

        existingRoute.setSourceCity(updatedRoute.getSourceCity());
        existingRoute.setDestinationCity(updatedRoute.getDestinationCity());
        existingRoute.setDistanceKm(updatedRoute.getDistanceKm());
        existingRoute.setDuration(updatedRoute.getDuration());
        existingRoute.setStatus(updatedRoute.getStatus());

        return routeRepository.save(existingRoute);
    }
}
