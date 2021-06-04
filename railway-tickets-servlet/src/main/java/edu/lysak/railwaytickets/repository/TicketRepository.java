package edu.lysak.railwaytickets.repository;

import edu.lysak.railwaytickets.model.Ticket;

public interface TicketRepository {

    Ticket save(Ticket ticket);

    int findPurchasedTickets(Long routeId);
}
