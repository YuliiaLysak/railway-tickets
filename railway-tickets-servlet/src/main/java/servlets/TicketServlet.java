package servlets;

import model.SessionUser;
import service.TicketService;
import utils.ServiceLocator;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Used to provide API for buying a ticket by urlPattern "/api/tickets/*"
 *
 * @author Yuliia Lysak
 */
public class TicketServlet extends HttpServlet {

    /**
     * Process buying a ticket.
     * Accept roueId as path variable.
     * Sends response status code 405 if pathInfo is invalid
     */
    @Override
    protected void doPost(
            HttpServletRequest request, HttpServletResponse response
    ) {

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
