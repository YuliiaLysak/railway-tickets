package service;

import exceptions.BusinessLogicException;
import model.Route;
import model.Ticket;
import repository.TicketRepository;

import java.time.LocalDateTime;

public class TicketService {
    private final TicketRepository ticketRepository;
    private final RouteService routeService;

    public TicketService(TicketRepository ticketRepository, RouteService routeService) {
        this.ticketRepository = ticketRepository;
        this.routeService = routeService;
    }

    public void buyTicket(Long userId, Long routeId) {
        Route route = routeService.findRouteById(routeId);
        if (routeService.getAvailableSeats(route) <= 0) {
            throw new BusinessLogicException("exception.ticket.unavailable");
        }

        Ticket ticket = new Ticket();
        ticket.setUserId(userId);
        ticket.setRouteId(route.getId());
        ticket.setPurchaseDate(LocalDateTime.now());
        ticketRepository.save(ticket);
    }
}
