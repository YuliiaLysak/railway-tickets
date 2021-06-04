package edu.lysak.railwaytickets.servlets;

import edu.lysak.railwaytickets.model.SessionUser;
import edu.lysak.railwaytickets.model.User;
import edu.lysak.railwaytickets.service.UserService;
import edu.lysak.railwaytickets.utils.ServiceLocator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Used to process authorization of users by urlPattern "/login"
 *
 * @author Yuliia Lysak
 */
public class LoginServlet extends HttpServlet {

    /**
     * Returns login page
     */
    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        request.getServletContext()
                .getRequestDispatcher("/WEB-INF/views/login.jsp")
                .forward(request, response);
    }

    /**
     * Process user authorization request.
     * Accept form parameters as application/x-www-form-urlencoded: email, password.
     * Creates session for authorized user.
     */
    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException, ServletException {

        UserService userService = ServiceLocator.getUserService();
        User user = userService.findByEmailAndPassword(
                request.getParameter("email"),
                request.getParameter("password")
        );

        if (user == null) {
            request.setAttribute("invalidSignin", "invalidSignin");
            request.getServletContext()
                    .getRequestDispatcher("/WEB-INF/views/login.jsp")
                    .forward(request, response);
            return;
        }

        HttpSession session = request.getSession();
        SessionUser sessionUser = new SessionUser(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getRoles());
        session.setAttribute("sessionUser", sessionUser);

        String redirectAfterLogin = (String) session.getAttribute("redirectAfterLogin");
        if (redirectAfterLogin != null) {
            session.removeAttribute("redirectAfterLogin");
            response.sendRedirect(request.getContextPath() + redirectAfterLogin);
            return;
        }

        response.sendRedirect(request.getContextPath() + "/");
    }
}
