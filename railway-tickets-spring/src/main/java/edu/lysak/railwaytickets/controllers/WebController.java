package edu.lysak.railwaytickets.controllers;

import edu.lysak.railwaytickets.dto.SearchRouteResponseDto;
import edu.lysak.railwaytickets.service.RouteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class WebController {
    private final RouteService routeService;

    public WebController(RouteService routeService) {
        this.routeService = routeService;
    }

    @GetMapping("/admin/stations")
    public String adminStations() {
        return "admin/stations";
    }

    @GetMapping("/admin/routes")
    public String adminRoutes() {
        return "admin/routes";
    }

    @GetMapping("/routes/{routeId}")
    public String routeDetails(@PathVariable Long routeId, Model model) {
        SearchRouteResponseDto routeDto = routeService.getRouteResponseDto(routeId);
        model.addAttribute("routeDto", routeDto);
        return "route-details";
    }

}
