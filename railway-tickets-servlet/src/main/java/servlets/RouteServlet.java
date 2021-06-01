package servlets;

import dto.PageableResponse;
import dto.RouteDto;
import dto.SearchRouteRequestDto;
import dto.SearchRouteResponseDto;
import model.Route;
import model.Station;
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

//@WebServlet(urlPatterns = "/api/routes/*")
public class RouteServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(StationServlet.class.getName());

    /**
     * Returns all routes as JSON.
     */
    // @GetMapping
    // @RequestParam(required = false, defaultValue = "1") int pageNo,
    // @RequestParam(required = false, defaultValue = "5") int pageSize
    @Override
    protected void doGet(
            HttpServletRequest request, HttpServletResponse response
    ) throws ServletException, IOException {

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
     * Update route in database.
     */
    // @PutMapping("/{routeId}")
    @Override
    protected void doPut(
            HttpServletRequest request, HttpServletResponse response
    ) throws ServletException, IOException {

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
     * Delete route from database.
     */
    // @DeleteMapping("/{routeId}")
    @Override
    protected void doDelete(
            HttpServletRequest request, HttpServletResponse response
    ) throws ServletException, IOException {

        Long routeId = ServletUtil.getPathVariable(request.getPathInfo());
        if (routeId == null) {
            response.setStatus(405);
            return;
        }

        RouteService routeService = ServiceLocator.getRouteService();
        routeService.deleteRoute(routeId);
    }

    /**
     * Add route to database ("").
     * Search available routes ("/search").
     */
    // @PostMapping
    // @PostMapping("/search")
    @Override
    protected void doPost(
            HttpServletRequest request, HttpServletResponse response
    ) throws ServletException, IOException {

        if ("/search".equals(request.getPathInfo()) || "/search/".equals(request.getPathInfo())) {
            processSearch(request, response);
        } else {
            processAdd(request, response);
        }
    }

    /**
     * Search available routes ("/search").
     */
    // @PostMapping("/search")
    private void processSearch(
            HttpServletRequest request, HttpServletResponse response
    ) throws ServletException, IOException {
        SearchRouteRequestDto searchRouteRequestDto = ServiceLocator.getGson().fromJson(request.getReader(), SearchRouteRequestDto.class);
        RouteService routeService = ServiceLocator.getRouteService();
        List<SearchRouteResponseDto> availableRoutes = routeService.getAvailableRoutes(searchRouteRequestDto);

        ServletUtil.sendSuccessResponse(response, availableRoutes);
    }

    /**
     * Add route to database ("").
     */
    // @PostMapping
    private void processAdd(
            HttpServletRequest request, HttpServletResponse response
    ) throws ServletException, IOException {

        if ("/".equals(request.getPathInfo()) || request.getPathInfo() == null) {
            RouteDto routeDto = ServiceLocator.getGson().fromJson(request.getReader(), RouteDto.class);

            RouteService routeService = ServiceLocator.getRouteService();

            ServletUtil.sendSuccessResponse(response, routeService.addNewRoute(routeDto));
        } else {
            response.setStatus(405);
        }
    }

}
