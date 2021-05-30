package edu.lysak.railwaytickets.controllers;

import edu.lysak.railwaytickets.dto.PageableResponse;
import edu.lysak.railwaytickets.dto.RouteDto;
import edu.lysak.railwaytickets.dto.SearchRouteRequestDto;
import edu.lysak.railwaytickets.dto.SearchRouteResponseDto;
import edu.lysak.railwaytickets.model.Route;
import edu.lysak.railwaytickets.service.RouteService;
import org.springframework.data.domain.Page;
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
    public PageableResponse<Route> getAllRoutesPaginated(
            @RequestParam(required = false, defaultValue = "1") int pageNo,
            @RequestParam(required = false, defaultValue = "5") int pageSize
    ) {
        Page<Route> routes = routeService.getAllRoutesPaginated(pageNo, pageSize);
        return new PageableResponse<>(
                Math.max(Math.min(pageNo, routes.getTotalPages()), 1),
                routes.getTotalPages(),
                routes.getContent()
        );
    }

    @PostMapping
    public Route addNewRoute(@RequestBody RouteDto routeDto) {
        return routeService.addNewRoute(routeDto);
    }

    @DeleteMapping("/{routeId}")
    public void deleteRoute(@PathVariable Long routeId) {
        routeService.deleteRoute(routeId);
    }

    @PutMapping("/{routeId}")
    public void updateRoute(@PathVariable Long routeId, @RequestBody RouteDto routeDto) {
        routeService.updateRoute(routeId, routeDto);
    }

    @PostMapping("/search")
    public List<SearchRouteResponseDto> getAvailableRoutes(@RequestBody SearchRouteRequestDto searchRouteRequestDto) {
        return routeService.getAvailableRoutes(searchRouteRequestDto);
    }
}
