package servlets;

import model.SessionUser;
import service.TicketService;
import utils.ServiceLocator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

//@WebServlet(urlPatterns = "/api/tickets/*")
public class TicketServlet extends HttpServlet {

    /**
     * Process buying a ticket.
     */
    // @PostMapping
    @Override
    protected void doPost(
            HttpServletRequest request, HttpServletResponse response
    ) throws ServletException, IOException {

        if ("/".equals(request.getPathInfo()) || request.getPathInfo() == null) {
            Long routeId = Long.parseLong(request.getParameter("routeId"));
            HttpSession session = request.getSession();
            SessionUser sessionUser = (SessionUser) session.getAttribute("sessionUser");

            TicketService ticketService = ServiceLocator.getTicketService();
            ticketService.buyTicket(sessionUser.getId(), routeId);
        } else {
            response.setStatus(405);
        }
    }
}
