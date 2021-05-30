package edu.lysak.railwaytickets.service;

import edu.lysak.railwaytickets.exceptions.BusinessLogicException;
import edu.lysak.railwaytickets.exceptions.InputValidationException;
import edu.lysak.railwaytickets.model.Station;
import edu.lysak.railwaytickets.repository.StationRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
public class StationService {
    private final StationRepository stationRepository;

    public StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public Page<Station> getAllStationsPaginated(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return stationRepository.findAll(pageable);
    }

    public Station addNewStation(Station station) {
        validateStationData(station);
        return stationRepository.save(station);
    }

    public void deleteStation(Long stationId) {
        try {
            stationRepository.deleteById(stationId);
        } catch (DataIntegrityViolationException e) {
            throw new BusinessLogicException("exception.station.delete");
        }
    }

    public void updateStation(Long stationId, Station station) {
        validateStationData(station);
        stationRepository.updateStationById(stationId, station.getCity(), station.getName());
    }

    private void validateStationData(Station station) {
        if (stationRepository.findByCityAndName(station.getCity(), station.getName()) != null) {
            throw new BusinessLogicException("exception.station.exist");
        }
        if (ObjectUtils.isEmpty(station.getCity())) {
            throw new InputValidationException("exception.stationCity.empty");
        }
        if (ObjectUtils.isEmpty(station.getName())) {
            throw new InputValidationException("exception.stationName.empty");
        }
    }
}
