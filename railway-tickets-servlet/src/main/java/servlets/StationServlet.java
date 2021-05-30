package servlets;

import model.Station;
import service.StationService;
import utils.ServiceLocator;
import utils.ServletUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

//@WebServlet(urlPatterns = "/api/stations/*")
public class StationServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(StationServlet.class.getName());

    /**
     * Returns all stations as JSON.
     */
    // @GetMapping
    @Override
    protected void doGet(
            HttpServletRequest request, HttpServletResponse response
    ) throws ServletException, IOException {

        if ("/".equals(request.getPathInfo()) || request.getPathInfo() == null) {
            StationService stationService = ServiceLocator.getStationService();
            List<Station> stations = stationService.getAllStations();
            ServletUtil.sendSuccessResponse(response, stations);
        } else {
            response.setStatus(405);
        }
    }

    /**
     * Add station to database.
     */
    // @PostMapping
    @Override
    protected void doPost(
            HttpServletRequest request, HttpServletResponse response
    ) throws ServletException, IOException {

        if ("/".equals(request.getPathInfo()) || request.getPathInfo() == null) {
            Station station = ServiceLocator.getGson().fromJson(request.getReader(), Station.class);

            StationService stationService = ServiceLocator.getStationService();
            ServletUtil.sendSuccessResponse(response, stationService.addNewStation(station));
        } else {
            response.setStatus(405);
        }
    }

    /**
     * Delete station from database.
     */
    // @DeleteMapping("/{stationId}")
    @Override
    protected void doDelete(
            HttpServletRequest request, HttpServletResponse response
    ) throws ServletException, IOException {

        Long stationId = ServletUtil.getPathVariable(request.getPathInfo());
        if (stationId == null) {
            response.setStatus(405);
            return;
        }

        StationService stationService = ServiceLocator.getStationService();
        stationService.deleteStation(stationId);
    }

    /**
     * Update station in database.
     */
    // @PutMapping("/{stationId}")
    @Override
    protected void doPut(
            HttpServletRequest request, HttpServletResponse response
    ) throws ServletException, IOException {

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
