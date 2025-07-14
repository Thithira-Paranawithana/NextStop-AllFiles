package com.dotsline.bus_service.controller;

import com.dotsline.bus_service.model.Bus;
import com.dotsline.bus_service.model.Route;
import com.dotsline.bus_service.service.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("routes")
public class RouteController {

    @Autowired
    private RouteService routeService;

    @PostMapping("createRoute")
    public ResponseEntity<Route> addRoute(@RequestBody Route route) {
        Route saveRoute = routeService.addRoute(route);
        return ResponseEntity.ok(saveRoute);

    }

    @GetMapping("getAllRoutes")
    public ResponseEntity<List<Route>> getAllRoutes() {
        return ResponseEntity.ok(routeService.getAllRoutes());
    }

    @GetMapping("getRoute/{id}")
    public ResponseEntity<Route> getRouteById(@PathVariable Integer id) {
        return routeService.getRouteById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<String> deleteRoute(@PathVariable Integer id) {
        routeService.deleteRouteById(id);
        return ResponseEntity.ok("Route deleted successfully.");
    }

    @PutMapping("updateRoute/{id}")
    public ResponseEntity<Route> updateBus(@PathVariable Integer id, @RequestBody Route updatedRoute) {
        Route route = routeService.updateRoute(id, updatedRoute);
        return ResponseEntity.ok(route);
    }

}
