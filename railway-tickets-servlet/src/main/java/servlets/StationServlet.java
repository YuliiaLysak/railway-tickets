package servlets;

import model.Station;
import repository.ConnectionPoolHolder;
import repository.impl.StationRepositoryImpl;
import service.StationService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// @RestController
// @RequestMapping("/api/stations")
public class StationServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(StationServlet.class.getName());

    // @GetMapping  "/"
    @Override
    protected void doGet(
            HttpServletRequest request, HttpServletResponse response
    ) throws ServletException, IOException {

        HttpSession session = request.getSession(true);
        boolean sessionNew = session.isNew();
        String initiator;
        if (sessionNew) {
            initiator = "station";
            session.setAttribute("initiator", initiator);
        } else {
            initiator = (String) session.getAttribute("initiator");
        }

        LOGGER.warning(
                "getRequestedSessionId: " + request.getRequestedSessionId()
                        + "getMethod: " + request.getMethod()
                        + "getRequestURI: " + request.getRequestURI()
                        + "sessionNew: " + session.isNew()
                        + "initiator: " + initiator
        );

//        StationService stationService = new StationService(
//                new StationRepositoryImpl(ConnectionPoolHolder.getDataSource()));
//        List<Station> stations = stationService.getAllStations();
//        request.setAttribute("stations", stations);
//        request.getRequestDispatcher("stations.jsp").forward(request, response);
    }

    // TODO @PostMapping("/") <-- new лучше убрать в спринговом проекте
    @Override
    protected void doPost(
            HttpServletRequest request, HttpServletResponse response
    ) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String city = request.getParameter("city");
        String name = request.getParameter("name");

        Station station = new Station();
        station.setCity(city);
        station.setName(name);

        StationService stationService = new StationService(
                new StationRepositoryImpl(ConnectionPoolHolder.getDataSource()));
        if (stationService.addNewStation(station) != null) {
            out.print("You have successfully added station...");
        }
        out.close();
    }

    // @DeleteMapping("/{stationId}")
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        LOGGER.warning(
                "getContextPath: " + req.getContextPath()
                        + "getPathInfo: " + req.getPathInfo()
                        + "getServletPath: " + req.getServletPath()
                        + "getPathTranslated: " + req.getPathTranslated()
                        + "getRequestURI: " + req.getRequestURI()
                        + "getQueryString: " + req.getQueryString()
                        + "getRequestedSessionId: " + req.getRequestedSessionId()
                        + "pathParam: " + tryGetPathParam(req.getPathInfo())

        );

        //Long stationId = Long.getLong()

        // stationService.deleteStation(stationId);
    }

    private String tryGetPathParam(String path) {
        try {
            return getPathParam(path);
        } catch (Exception e) {
            LOGGER.throwing("StationServlet", "tryGetPathParam", e);
            return null;
        }
    }

    private String getPathParam(String path) {
        Pattern PATH_PARAM_PATTERN = Pattern.compile("^/(?<value>[0-9]+)(/|$)");
        return "" + Optional.ofNullable(path)
                .map(it -> {
                    Matcher matcher = PATH_PARAM_PATTERN.matcher(it);
                    if (matcher.find()) {
                        return matcher.group("value");
                    }
                    return null;
                })
                .map(Long::parseLong)
                .orElse(null);
    }

    // TODO @PutMapping("/{stationId}/edit") <-- /edit лучше убрать в спринговом проекте
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
    }
}
