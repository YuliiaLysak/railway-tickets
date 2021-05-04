package edu.lysak.railwaytickets.service;

import edu.lysak.railwaytickets.model.Station;
import edu.lysak.railwaytickets.repository.StationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StationService {
    private final StationRepository stationRepository;

    public StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public List<Station> getAllStations() {
        return stationRepository.findAll();
    }

    public Station addNewStation(Station station) {
        return stationRepository.save(station);
    }

    public void deleteStation(Long stationId) {
        stationRepository.deleteById(stationId);
    }

    public void updateStation(Long stationId, Station station) {
        stationRepository.updateStationById(stationId, station.getCity(), station.getName());
    }
}
