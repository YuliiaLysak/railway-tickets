package edu.lysak.railwaytickets.controllers;

import edu.lysak.railwaytickets.dto.PageableResponse;
import edu.lysak.railwaytickets.model.Station;
import edu.lysak.railwaytickets.service.StationService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stations")
public class StationController {
    private final StationService stationService;

    public StationController(StationService stationService) {
        this.stationService = stationService;
    }

    @GetMapping
    public PageableResponse<Station> getAllStationsPaginated(
            @RequestParam(required = false, defaultValue = "1") int pageNo,
            @RequestParam(required = false, defaultValue = "10") int pageSize
    ) {
        Page<Station> stations = stationService.getAllStationsPaginated(pageNo, pageSize);
        return new PageableResponse<>(
                Math.max(Math.min(pageNo, stations.getTotalPages()), 1),
                stations.getTotalPages(),
                stations.getContent()
        );
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
