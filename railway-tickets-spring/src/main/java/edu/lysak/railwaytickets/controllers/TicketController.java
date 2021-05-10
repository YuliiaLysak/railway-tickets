package edu.lysak.railwaytickets.controllers;

import edu.lysak.railwaytickets.model.Ticket;
import edu.lysak.railwaytickets.model.User;
import edu.lysak.railwaytickets.service.TicketService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {
    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }


    @PostMapping
    public Ticket buyTicket(@AuthenticationPrincipal User user,
                            @RequestParam Long routeId
    ) {
        return ticketService.buyTicket(user, routeId);
    }
}
