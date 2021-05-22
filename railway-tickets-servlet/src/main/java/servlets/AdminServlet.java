package servlets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//@WebServlet(urlPatterns = "/admin/*")
public class AdminServlet extends HttpServlet {
    @Override
    protected void doGet(
            HttpServletRequest request, HttpServletResponse response
    ) throws ServletException, IOException {

        if ("/stations".equals(request.getPathInfo())
                || "/stations/".equals(request.getPathInfo())) {

            request.getServletContext()
                    .getRequestDispatcher("/WEB-INF/views/admin/stations.jsp")
                    .forward(request, response);

        } else if ("/routes".equals(request.getPathInfo())
                || "/routes/".equals(request.getPathInfo())) {

            request.getServletContext()
                    .getRequestDispatcher("/WEB-INF/views/admin/routes.jsp")
                    .forward(request, response);
        }
    }
}
