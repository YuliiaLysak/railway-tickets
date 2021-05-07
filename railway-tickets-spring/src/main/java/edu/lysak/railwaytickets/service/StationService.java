package edu.lysak.railwaytickets.service;

import edu.lysak.railwaytickets.exceptions.BusinessLogicException;
import edu.lysak.railwaytickets.exceptions.InputValidationException;
import edu.lysak.railwaytickets.model.Station;
import edu.lysak.railwaytickets.repository.StationRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

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
        validateStationData(station);
        return stationRepository.save(station);
    }

    public void deleteStation(Long stationId) {
        try {
            stationRepository.deleteById(stationId);
        } catch (DataIntegrityViolationException e) {
            throw new BusinessLogicException(
                    "Station from the active route cannot be deleted"
            );
        }
    }

    public void updateStation(Long stationId, Station station) {
        validateStationData(station);
        stationRepository.updateStationById(stationId, station.getCity(), station.getName());
    }

    private void validateStationData(Station station) {
        if (stationRepository.findByCityAndName(station.getCity(), station.getName()) != null) {
            throw new BusinessLogicException("Station with the same city and name already exists");
        }
        if (ObjectUtils.isEmpty(station.getCity())) {
            throw new InputValidationException("Station city should not be empty");
        }
        if (ObjectUtils.isEmpty(station.getName())) {
            throw new InputValidationException("Station name should not be empty");
        }
    }
}
