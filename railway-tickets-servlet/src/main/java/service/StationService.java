package service;

import dto.PageableResponse;
import exceptions.BusinessLogicException;
import exceptions.InputValidationException;
import model.Station;
import repository.StationRepository;

import java.util.List;

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

    // TODO - add tests for this method
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
