package servlets;

import dto.PageableResponse;
import model.Station;
import service.StationService;
import utils.ServiceLocator;
import utils.ServletUtil;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Used to provide API for searching stations by urlPattern "/api/stations/*"
 *
 * @author Yuliia Lysak
 */
public class StationServlet extends HttpServlet {

    /**
     * Returns paginated stations as JSON.
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
            int pageSize = size != null ? Integer.parseInt(size) : 10;

            StationService stationService = ServiceLocator.getStationService();
            PageableResponse<Station> stations = stationService.getAllStationsPaginated(pageNo, pageSize);
            ServletUtil.sendSuccessResponse(response, stations);
        } else {
            response.setStatus(405);
        }
    }

    /**
     * Add new station.
     * Accepts request body as application/json.
     * Sends response status code 405 if pathInfo is invalid.
     */
    @Override
    protected void doPost(
            HttpServletRequest request, HttpServletResponse response
    ) throws IOException {

        if ("/".equals(request.getPathInfo()) || request.getPathInfo() == null) {
            Station station = ServiceLocator.getGson().fromJson(request.getReader(), Station.class);

            StationService stationService = ServiceLocator.getStationService();
            ServletUtil.sendSuccessResponse(response, stationService.addNewStation(station));
        } else {
            response.setStatus(405);
        }
    }

    /**
     * Deletes station.
     * Accepts path variable: stationId
     * Sends response status code 405 if path variable is invalid or not found.
     */
    @Override
    protected void doDelete(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        Long stationId = ServletUtil.getPathVariable(request.getPathInfo());
        if (stationId == null) {
            response.setStatus(405);
            return;
        }

        StationService stationService = ServiceLocator.getStationService();
        stationService.deleteStation(stationId);
    }

    /**
     * Updates station information.
     * Accepts path variable: stationId
     * Accepts request body as application/json.
     * Sends response status code 405 if path variable is invalid or not found.
     */
    @Override
    protected void doPut(
            HttpServletRequest request, HttpServletResponse response
    ) throws IOException {

        Long stationId = ServletUtil.getPathVariable(request.getPathInfo());
        if (stationId == null) {
            response.setStatus(405);
            return;
        }

        Station station = ServiceLocator.getGson().fromJson(request.getReader(), Station.class);
        StationService stationService = ServiceLocator.getStationService();
        stationService.updateStation(stationId, station);
    }
}
