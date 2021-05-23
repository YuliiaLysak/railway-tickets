package servlets;

import exceptions.BusinessLogicException;
import model.User;
import service.UserService;
import utils.ServiceLocator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

//@WebServlet(urlPatterns = "/registration")
public class RegistrationServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(RegistrationServlet.class.getName());

    @Override
    protected void doGet(
            HttpServletRequest request, HttpServletResponse response
    ) throws ServletException, IOException {

        request.getServletContext()
                .getRequestDispatcher("/WEB-INF/views/registration.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(
            HttpServletRequest request, HttpServletResponse response
    ) throws ServletException, IOException {

        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String password = request.getParameter("password");

        UserService userService = ServiceLocator.getUserService();
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPhone(phone);
        user.setPassword(password);

        if (!userService.addUser(user)) {
            throw new BusinessLogicException("User with this email already exists");
        }

        response.sendRedirect(request.getContextPath() + "/login");
    }
}
