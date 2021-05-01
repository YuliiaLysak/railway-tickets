package edu.lysak.railwaytickets.controllers;

import edu.lysak.railwaytickets.dto.SearchRouteDto;
import edu.lysak.railwaytickets.model.Route;
import edu.lysak.railwaytickets.service.RouteService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/routes")
public class RouteController {
    private final RouteService routeService;

    public RouteController(RouteService routeService) {
        this.routeService = routeService;
    }

    @GetMapping
    public List<Route> getAllRoutes() {
        return routeService.getAllRoutes();
    }

    @PostMapping
    public void addNewRoute(@RequestBody Route route) {
        routeService.addNewRoute(route);
    }

    @DeleteMapping("{routeId}")
    public void deleteRoute(@PathVariable Long routeId) {
        routeService.deleteRoute(routeId);
    }

    @PutMapping("{routeId}")
    public void updateRoute(@PathVariable Long routeId, @RequestBody Route route) {
        routeService.updateRoute(routeId, route);
    }

    @PostMapping("/search")
    public List<Route> getAvailableRoutes(@RequestBody SearchRouteDto searchRouteDto) {
        return routeService.getAvailableRoutes(searchRouteDto);
    }
}
