package edu.lysak.railwaytickets.servlets;

import edu.lysak.railwaytickets.model.User;
import edu.lysak.railwaytickets.service.UserService;
import edu.lysak.railwaytickets.utils.ServiceLocator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Used to process registration of users to the application by urlPattern "/registration"
 *
 * @author Yuliia Lysak
 */
public class RegistrationServlet extends HttpServlet {

    /**
     * Returns registration page
     */
    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        request.getServletContext()
                .getRequestDispatcher("/WEB-INF/views/registration.jsp")
                .forward(request, response);
    }

    /**
     * Process user registration request.
     * Accept form parameters as application/x-www-form-urlencoded:
     * firstName, lastName, email, phone, password.
     */
    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {

        User user = new User();
        user.setFirstName(request.getParameter("firstName"));
        user.setLastName(request.getParameter("lastName"));
        user.setEmail(request.getParameter("email"));
        user.setPhone(request.getParameter("phone"));
        user.setPassword(request.getParameter("password"));

        UserService userService = ServiceLocator.getUserService();
        userService.addUser(user);

        response.sendRedirect(request.getContextPath() + "/login");
    }
}
