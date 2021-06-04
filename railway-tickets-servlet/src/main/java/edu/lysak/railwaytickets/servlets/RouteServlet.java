package edu.lysak.railwaytickets.servlets;

import edu.lysak.railwaytickets.dto.PageableResponse;
import edu.lysak.railwaytickets.dto.RouteDto;
import edu.lysak.railwaytickets.dto.SearchRouteRequestDto;
import edu.lysak.railwaytickets.dto.SearchRouteResponseDto;
import edu.lysak.railwaytickets.model.Route;
import edu.lysak.railwaytickets.service.RouteService;
import edu.lysak.railwaytickets.utils.ServiceLocator;
import edu.lysak.railwaytickets.utils.ServletUtil;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Used to provide API for searching routes by urlPattern "/api/routes/*"
 *
 * @author Yuliia Lysak
 */
public class RouteServlet extends HttpServlet {

    /**
     * Returns paginated routes as JSON.
     * Accepts query parameters: pageNo, pageSize.
     * Sends response status code 405 if path info is invalid.
     */
    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {

        if ("/".equals(request.getPathInfo()) || request.getPathInfo() == null) {
            String number = request.getParameter("pageNo");
            String size = request.getParameter("pageSize");
            int pageNo = number != null ? Integer.parseInt(number) : 1;
            int pageSize = size != null ? Integer.parseInt(size) : 5;

            RouteService routeService = ServiceLocator.getRouteService();
            PageableResponse<Route> routes = routeService.getAllRoutesPaginated(pageNo, pageSize);
            ServletUtil.sendSuccessResponse(response, routes);
        } else {
            response.setStatus(405);
        }
    }

    /**
     * Updates route information.
     * Accepts path variable: routeId
     * Accepts request body as application/json.
     * Sends response status code 405 if path variable is invalid or not found.
     */
    @Override
    protected void doPut(
            HttpServletRequest request, HttpServletResponse response
    ) throws IOException {

        Long routeId = ServletUtil.getPathVariable(request.getPathInfo());
        if (routeId == null) {
            response.setStatus(405);
            return;
        }

        RouteDto routeDto = ServiceLocator.getGson()
                .fromJson(request.getReader(), RouteDto.class);

        RouteService routeService = ServiceLocator.getRouteService();
        routeService.updateRoute(routeId, routeDto);
    }

    /**
     * Deletes route.
     * Accepts path variable: routeId
     * Sends response status code 405 if path variable is invalid or not found.
     */
    @Override
    protected void doDelete(
            HttpServletRequest request, HttpServletResponse response
    ) {

        Long routeId = ServletUtil.getPathVariable(request.getPathInfo());
        if (routeId == null) {
            response.setStatus(405);
            return;
        }

        RouteService routeService = ServiceLocator.getRouteService();
        routeService.deleteRoute(routeId);
    }

    /**
     * Process searching for pathInfo "/search".
     * Process adding if pathInfo not present.
     */
    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {

        if ("/search".equals(request.getPathInfo()) || "/search/".equals(request.getPathInfo())) {
            processSearch(request, response);
        } else {
            processAdd(request, response);
        }
    }

    /**
     * Returns available routes for pathInfo "/search".
     * Accepts request body as application/json.
     */
    private void processSearch(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        SearchRouteRequestDto searchRouteRequestDto = ServiceLocator.getGson()
                .fromJson(request.getReader(), SearchRouteRequestDto.class);
        RouteService routeService = ServiceLocator.getRouteService();
        List<SearchRouteResponseDto> availableRoutes = routeService.getAvailableRoutes(searchRouteRequestDto);

        ServletUtil.sendSuccessResponse(response, availableRoutes);
    }

    /**
     * Add new route.
     * Accepts request body as application/json.
     * Sends response status code 405 if pathInfo is invalid.
     */
    private void processAdd(
            HttpServletRequest request, HttpServletResponse response
    ) throws IOException {

        if ("/".equals(request.getPathInfo()) || request.getPathInfo() == null) {
            RouteDto routeDto = ServiceLocator.getGson().fromJson(request.getReader(), RouteDto.class);

            RouteService routeService = ServiceLocator.getRouteService();

            ServletUtil.sendSuccessResponse(response, routeService.addNewRoute(routeDto));
        } else {
            response.setStatus(405);
        }
    }
}
