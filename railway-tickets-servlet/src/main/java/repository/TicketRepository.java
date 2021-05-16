package repository;

import model.Ticket;

public interface TicketRepository {

    Ticket save(Ticket ticket);
    int findPurchasedTickets(Long routeId);
}
