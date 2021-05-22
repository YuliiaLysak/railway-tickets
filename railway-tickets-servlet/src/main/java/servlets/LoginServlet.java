package servlets;

import model.SessionUser;
import model.User;
import service.UserService;
import utils.ServiceLocator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.logging.Logger;

//@WebServlet(urlPatterns = "/login")
public class LoginServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(LoginServlet.class.getName());

    @Override
    protected void doGet(
            HttpServletRequest request, HttpServletResponse response
    ) throws ServletException, IOException {

        request.getServletContext()
                .getRequestDispatcher("/WEB-INF/views/login.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(
            HttpServletRequest request, HttpServletResponse response
    ) throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        UserService userService = ServiceLocator.getUserService();
        User user = userService.findByEmailAndPassword(email, password);
        if (user != null) {
            HttpSession session = request.getSession();
            SessionUser sessionUser = new SessionUser(
                    user.getId(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getRoles());
            session.setAttribute("sessionUser", sessionUser);
        }

        request.getServletContext()
                .getRequestDispatcher("/WEB-INF/views/home.jsp")
                .forward(request, response);
    }
}
