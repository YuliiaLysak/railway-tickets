package edu.lysak.railwaytickets.repository;

import edu.lysak.railwaytickets.model.Route;
import edu.lysak.railwaytickets.model.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RouteRepository extends JpaRepository<Route, Long> {

    @Query("SELECT route FROM Route route" +
            " WHERE route.departureStation = :departureStation" +
            " AND route.arrivalStation = :arrivalStation" +
            " AND route.departureTime >= :departureDate")
    List<Route> findAvailableRoutes(
            @Param("departureStation") Station departureStation,
            @Param("arrivalStation") Station arrivalStation,
            @Param("departureDate") LocalDateTime departureDate
    );
}
