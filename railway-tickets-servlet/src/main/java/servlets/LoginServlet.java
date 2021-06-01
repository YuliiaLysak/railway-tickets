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

//@WebServlet(urlPatterns = "/login")
public class LoginServlet extends HttpServlet {

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
    ) throws IOException {

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

            String redirectAfterLogin = (String) session.getAttribute("redirectAfterLogin");
            if (redirectAfterLogin != null) {
                session.removeAttribute("redirectAfterLogin");
                response.sendRedirect(request.getContextPath() + redirectAfterLogin);
                return;
            }
        }

        response.sendRedirect(request.getContextPath() + "/");
    }
}
