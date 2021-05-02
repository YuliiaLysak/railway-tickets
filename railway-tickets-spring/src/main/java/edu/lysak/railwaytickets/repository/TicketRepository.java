package edu.lysak.railwaytickets.repository;

import edu.lysak.railwaytickets.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    @Query("SELECT COUNT(id) FROM Ticket WHERE routeId = :routeId")
    int findPurchasedTickets(@Param("routeId") Long routeId);
}
