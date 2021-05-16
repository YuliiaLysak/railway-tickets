package edu.lysak.railwaytickets.controllers;

import edu.lysak.railwaytickets.model.Station;
import edu.lysak.railwaytickets.service.StationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stations")
public class StationController {
    private final StationService stationService;

    public StationController(StationService stationService) {
        this.stationService = stationService;
    }

    @GetMapping
    public List<Station> getAllStations() {
        return stationService.getAllStations();
    }

    @PostMapping
    public Station addNewStation(@RequestBody Station station) {
        return stationService.addNewStation(station);
    }

    @DeleteMapping("/{stationId}")
    public void deleteStation(@PathVariable Long stationId) {
        stationService.deleteStation(stationId);
    }

    @PutMapping("/{stationId}")
    public void updateStation(@PathVariable Long stationId, @RequestBody Station station) {
        stationService.updateStation(stationId, station);
    }
}
