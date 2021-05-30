package edu.lysak.railwaytickets.repository;

import edu.lysak.railwaytickets.model.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RouteRepository extends JpaRepository<Route, Long> {

    @Query("SELECT route FROM Route route" +
            " WHERE route.departureStation.city = :departureCity" +
            " AND route.arrivalStation.city = :arrivalCity" +
            " AND route.departureTime >= :departureDateStart" +
            " AND route.departureTime < :departureDateEnd")
    List<Route> findAvailableRoutesByCities(
            @Param("departureCity") String departureCity,
            @Param("arrivalCity") String arrivalCity,
            @Param("departureDateStart") LocalDateTime departureDateStart,
            @Param("departureDateEnd") LocalDateTime departureDateEnd
    );

    @Modifying
    @Query("DELETE FROM Route WHERE id = :id")
    void deleteById(@Param("id") Long id);
}
