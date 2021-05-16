package servlets;

import com.google.gson.Gson;
import model.Station;
import repository.ConnectionPoolHolder;
import repository.impl.StationRepositoryImpl;
import service.StationService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

//@WebServlet(urlPatterns = "/api/stations/*")
public class StationServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(StationServlet.class.getName());
    private final Gson gson = new Gson();

    /**
     * Returns all stations as JSON.
     */
    // @GetMapping
    @Override
    protected void doGet(
            HttpServletRequest request, HttpServletResponse response
    ) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        if ("/".equals(request.getPathInfo()) || request.getPathInfo() == null) {
            StationService stationService = new StationService(
                    new StationRepositoryImpl(ConnectionPoolHolder.getDataSource()));
            List<Station> stations = stationService.getAllStations();
            String jsonStations = gson.toJson(stations);

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(jsonStations);
            out.flush();
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
        request.setCharacterEncoding("UTF-8");

        if ("/".equals(request.getPathInfo()) || request.getPathInfo() == null) {
            Station station = gson.fromJson(request.getReader(), Station.class);

            StationService stationService = new StationService(
                    new StationRepositoryImpl(ConnectionPoolHolder.getDataSource()));
            String jsonStation = gson.toJson(stationService.addNewStation(station));
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(jsonStation);
            out.flush();
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
        request.setCharacterEncoding("UTF-8");

        Long stationId = getPathVariable(request.getPathInfo());
        if (stationId == null) {
            response.setStatus(405);
            return;
        }

        StationService stationService = new StationService(
                new StationRepositoryImpl(ConnectionPoolHolder.getDataSource()));
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
        request.setCharacterEncoding("UTF-8");

        Station station = gson.fromJson(request.getReader(), Station.class);
        Long stationId = getPathVariable(request.getPathInfo());
        if (stationId == null) {
            response.setStatus(405);
            return;
        }

        StationService stationService = new StationService(
                new StationRepositoryImpl(ConnectionPoolHolder.getDataSource()));
        stationService.updateStation(stationId, station);
    }

    private Long getPathVariable(String path) {
        String[] pathParts = path.split("/");
        long variablesCount = Arrays.stream(pathParts)
                .filter(variable -> !variable.isEmpty())
                .count();
        if (variablesCount != 1) {
            return null;
        }
        try {
            return Long.parseLong(pathParts[1]);
        } catch (Exception exception) {
            return null;
        }
    }
}
