package servlets;

import dto.SearchRouteResponseDto;
import service.RouteService;
import utils.ServiceLocator;
import utils.ServletUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//@WebServlet(urlPatterns = "/routes/*")
public class RouteInfoServlet extends HttpServlet {
    @Override
    protected void doGet(
            HttpServletRequest request, HttpServletResponse response
    ) throws ServletException, IOException {

        Long routeId = ServletUtil.getPathVariable(request.getPathInfo());
        if (routeId == null) {
            response.setStatus(405);
            return;
        }

        RouteService routeService = ServiceLocator.getRouteService();
        SearchRouteResponseDto routeDto = routeService.getRouteResponseDto(routeId);
        request.setAttribute("routeDto", routeDto);

        request.getServletContext()
                .getRequestDispatcher("/WEB-INF/views/route-details.jsp")
                .forward(request, response);
    }
}
