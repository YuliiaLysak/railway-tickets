package servlets;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;

//@WebServlet(urlPatterns = "/logout")
public class LogoutServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getSession().invalidate();

        response.sendRedirect(request.getContextPath() + "/");
    }
}
