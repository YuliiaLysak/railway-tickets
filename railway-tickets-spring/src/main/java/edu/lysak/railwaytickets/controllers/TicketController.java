package edu.lysak.railwaytickets.controllers;

import edu.lysak.railwaytickets.dto.SearchRouteRequestDto;
import edu.lysak.railwaytickets.model.User;
import edu.lysak.railwaytickets.service.TicketService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {
    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }


    @PostMapping
    public void buyTicket(@AuthenticationPrincipal User user,
                          @RequestBody SearchRouteRequestDto searchRouteRequestDto
    ) {
        ticketService.buyTicket(user, searchRouteRequestDto);
    }
}
