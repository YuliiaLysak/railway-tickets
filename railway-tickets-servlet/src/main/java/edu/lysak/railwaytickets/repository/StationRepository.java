package edu.lysak.railwaytickets.repository;

import edu.lysak.railwaytickets.model.Station;

import java.util.List;
import java.util.Optional;

public interface StationRepository {

    Station save(Station station);

    Optional<Station> findById(Long id);

    void deleteById(Long id);

    List<Station> findAll();

    Station findByCityAndName(String city, String name);

    void updateStationById(Long id, String city, String name);

    List<Station> findAllPaginated(int pageNo, int pageSize);

    int countStationRecords();
}
