package edu.lysak.railwaytickets.service;

import edu.lysak.railwaytickets.exceptions.BusinessLogicException;
import edu.lysak.railwaytickets.model.Route;
import edu.lysak.railwaytickets.model.Ticket;
import edu.lysak.railwaytickets.model.User;
import edu.lysak.railwaytickets.repository.TicketRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TicketService {
    private final TicketRepository ticketRepository;
    private final RouteService routeService;

    public TicketService(TicketRepository ticketRepository, RouteService routeService) {
        this.ticketRepository = ticketRepository;
        this.routeService = routeService;
    }

    public void buyTicket(User user, Long routeId) {
        Route route = routeService.findRouteById(routeId);
        if (routeService.getAvailableSeats(route) <= 0) {
            throw new BusinessLogicException("There is no available tickets");
        }
        Ticket ticket = new Ticket();
        ticket.setOwner(user);
        ticket.setRouteId(route.getId());
        ticket.setPurchaseDate(LocalDateTime.now());
        ticketRepository.save(ticket);
    }
}
