package edu.lysak.railwaytickets.servlets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Used to process log out of users from the application by urlPattern "/logout"
 *
 * @author Yuliia Lysak
 */
public class LogoutServlet extends HttpServlet {

    /**
     * Invalidate session of authorized user
     */
    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException, ServletException {
        request.getSession().invalidate();
        request.setAttribute("signedOut", "signedOut");
        request.getServletContext()
                .getRequestDispatcher("/WEB-INF/views/login.jsp")
                .forward(request, response);
    }
}
