package edu.lysak.railwaytickets.repository;

import edu.lysak.railwaytickets.model.Route;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RouteRepository {

    List<Route> findAvailableRoutesByCities(
            String departureCity,
            String arrivalCity,
            LocalDateTime departureDateStart,
            LocalDateTime departureDateEnd
    );

    List<Route> findAll();

    Route save(Route route);

    void deleteById(Long routeId);

    Optional<Route> findById(Long routeId);

    void update(Route updatedRoute);

    List<Route> findAllPaginated(int pageNo, int pageSize);

    int countRouteRecords();
}
