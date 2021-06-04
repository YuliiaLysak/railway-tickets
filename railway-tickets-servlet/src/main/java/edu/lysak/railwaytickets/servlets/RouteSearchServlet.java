package edu.lysak.railwaytickets.servlets;

import edu.lysak.railwaytickets.dto.SearchRouteRequestDto;
import edu.lysak.railwaytickets.dto.SearchRouteResponseDto;
import edu.lysak.railwaytickets.service.RouteService;
import edu.lysak.railwaytickets.utils.ServiceLocator;
import edu.lysak.railwaytickets.utils.ServletUtil;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Used to provide API for searching available routes by urlPattern "/api/routes/search/*"
 *
 * @author Yuliia Lysak
 */
public class RouteSearchServlet extends HttpServlet {

    /**
     * Gets list of available routes.
     * Accepts request body as application/json.
     */
    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {

        SearchRouteRequestDto searchRouteRequestDto = ServiceLocator.getGson()
                .fromJson(request.getReader(), SearchRouteRequestDto.class);
        RouteService routeService = ServiceLocator.getRouteService();
        List<SearchRouteResponseDto> availableRoutes = routeService.getAvailableRoutes(searchRouteRequestDto);

        ServletUtil.sendSuccessResponse(response, availableRoutes);
    }
}
