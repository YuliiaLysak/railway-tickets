package edu.lysak.railwaytickets.controllers;

import edu.lysak.railwaytickets.service.StationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/web/stations")
public class StationControllerWeb {
    private final StationService stationService;

    public StationControllerWeb(StationService stationService) {
        this.stationService = stationService;
    }

    @GetMapping
    public String getAllStations(Model model) {
        model.addAttribute("stations", stationService.getAllStations());
        return "stations";
    }
}
