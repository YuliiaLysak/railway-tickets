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

public class LoginLogoutServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {

        String email = request.getParameter("email");
//        String password = request.getParameter("password");
        UserService userService = ServiceLocator.getUserService();
        User user = userService.findByEmail(email);
//        User user = userService.findByEmailAndPassword(email, password);
        if (user != null) {
            HttpSession session = request.getSession();
            SessionUser sessionUser = new SessionUser(user.getId(), user.getRoles());
            session.setAttribute("sessionUser", sessionUser);
        }
//        todo - redirect to homepage or previous page
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getSession().invalidate();

        response.sendRedirect(request.getContextPath() + "/");
    }
}
