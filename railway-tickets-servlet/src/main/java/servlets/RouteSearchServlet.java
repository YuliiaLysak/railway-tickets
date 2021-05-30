package servlets;

import dto.SearchRouteRequestDto;
import dto.SearchRouteResponseDto;
import service.RouteService;
import utils.ServiceLocator;
import utils.ServletUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

//@WebServlet(urlPatterns = "/api/routes/search")
public class RouteSearchServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(RouteSearchServlet.class.getName());

    /**
     * Search available routes ("/search").
     */
    // @PostMapping("/search")
    @Override
    protected void doPost(
            HttpServletRequest request, HttpServletResponse response
    ) throws ServletException, IOException {

        SearchRouteRequestDto searchRouteRequestDto = ServiceLocator.getGson()
                .fromJson(request.getReader(), SearchRouteRequestDto.class);
        RouteService routeService = ServiceLocator.getRouteService();
        List<SearchRouteResponseDto> availableRoutes = routeService.getAvailableRoutes(searchRouteRequestDto);

        ServletUtil.sendSuccessResponse(response, availableRoutes);
    }
}
