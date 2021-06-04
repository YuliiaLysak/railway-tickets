package edu.lysak.railwaytickets.service;

import edu.lysak.railwaytickets.dto.PageableResponse;
import edu.lysak.railwaytickets.exceptions.BusinessLogicException;
import edu.lysak.railwaytickets.exceptions.InputValidationException;
import edu.lysak.railwaytickets.model.Station;
import edu.lysak.railwaytickets.repository.StationRepository;

import java.util.List;

/**
 * Used for processing operations with stations
 *
 * @author Yuliia Lysak
 */
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
        stationRepository.deleteById(stationId);
    }

    public void updateStation(Long stationId, Station station) {
        validateStationData(station);
        stationRepository.updateStationById(stationId, station.getCity(), station.getName());
    }

    /**
     * Validates information of station.
     *
     * @param station - station object for validation.
     *
     * @throws BusinessLogicException if station already exists
     * @throws InputValidationException if station city not present or empty
     * @throws InputValidationException if station name not present or empty
     */
    private void validateStationData(Station station) {
        if (stationRepository.findByCityAndName(station.getCity(), station.getName()) != null) {
            throw new BusinessLogicException("exception.station.exist");
        }
        if (station.getCity() == null || station.getCity().isEmpty()) {
            throw new InputValidationException("exception.stationCity.empty");
        }
        if (station.getName() == null || station.getName().isEmpty()) {
            throw new InputValidationException("exception.stationName.empty");
        }
    }

    /**
     * Gets PageableResponse object of stations
     *
     * @see PageableResponse
     *
     * @param pageNo - number of current page
     * @param pageSize - size of a page
     *
     * @return PageableResponse object with station list, number of current page and total pages
     */
    public PageableResponse<Station> getAllStationsPaginated(int pageNo, int pageSize) {
        List<Station> stations = stationRepository.findAllPaginated(pageNo - 1, pageSize);
        int stationCount = stationRepository.countStationRecords();
        int totalPages = (int) Math.ceil((double) stationCount / pageSize);
        return new PageableResponse<>(
                Math.max(Math.min(pageNo, totalPages), 1),
                totalPages,
                stations
        );
    }
}
