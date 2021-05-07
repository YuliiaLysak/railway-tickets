package edu.lysak.railwaytickets.controllers;

import edu.lysak.railwaytickets.dto.RouteDto;
import edu.lysak.railwaytickets.dto.SearchRouteRequestDto;
import edu.lysak.railwaytickets.dto.SearchRouteResponseDto;
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

    @PostMapping("/new")
    public Route addNewRoute(@RequestBody RouteDto routeDto) {
        return routeService.addNewRoute(routeDto);
    }

    @DeleteMapping("/{routeId}")
    public void deleteRoute(@PathVariable Long routeId) {
        routeService.deleteRoute(routeId);
    }

    @PutMapping("/{routeId}/edit")
    public void updateRoute(@PathVariable Long routeId, @RequestBody RouteDto routeDto) {
        routeService.updateRoute(routeId, routeDto);
    }

    @PostMapping("/search")
    public List<SearchRouteResponseDto> getAvailableRoutes(@RequestBody SearchRouteRequestDto searchRouteRequestDto) {
        return routeService.getAvailableRoutes(searchRouteRequestDto);
    }
}
