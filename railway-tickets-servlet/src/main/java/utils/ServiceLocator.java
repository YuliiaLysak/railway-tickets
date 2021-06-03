package utils;

import com.google.gson.*;
import repository.ConnectionPoolHolder;
import repository.impl.*;
import service.*;

import javax.sql.DataSource;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Used to get service instances that implement business logic of the application
 *
 * @author Yuliia Lysak
 */
public final class ServiceLocator {

    private static final RouteService routeService;
    private static final StationService stationService;
    private static final TicketService ticketService;
    private static final UserService userService;
    private static final SessionAnalyticService sessionAnalyticService;
    private static final Gson gson;

    static {
        DataSource dataSource = ConnectionPoolHolder.getDataSource();

        gson = createGson();

        var routeRepository = new RouteRepositoryImpl(dataSource);
        var stationRepository = new StationRepositoryImpl(dataSource);
        var ticketRepository = new TicketRepositoryImpl(dataSource);
        var userRepository = new UserRepositoryImpl(dataSource);
        var sessionAnalyticsRepository = new SessionAnalyticRepositoryImpl(dataSource);

        routeService = new RouteService(routeRepository, stationRepository, ticketRepository);
        stationService = new StationService(stationRepository);
        ticketService = new TicketService(ticketRepository, routeService);
        userService = new UserService(userRepository, () -> {
            try {
                return MessageDigest.getInstance("SHA-256");
            } catch (NoSuchAlgorithmException exception) {
                throw new RuntimeException(exception);
            }
        });
        sessionAnalyticService = new SessionAnalyticService(sessionAnalyticsRepository);
    }

    /**
     * Creates Gson object with serialization/deserialization functionality
     * for LocalDateTime and LocalDate
     *
     * @return Gson object
     */
    private static Gson createGson() {
        return new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, (JsonSerializer<LocalDateTime>) (src, type, context) ->
                        new JsonPrimitive(src.format(DateTimeFormatter.ISO_DATE_TIME))
                )
                .registerTypeAdapter(LocalDateTime.class, (JsonDeserializer<LocalDateTime>) (src, type, context) ->
                        LocalDateTime.parse(src.getAsString(), DateTimeFormatter.ISO_DATE_TIME)
                )
                .registerTypeAdapter(LocalDate.class, (JsonSerializer<LocalDate>) (src, type, context) ->
                        new JsonPrimitive(src.format(DateTimeFormatter.ISO_DATE))
                )
                .registerTypeAdapter(LocalDate.class, (JsonDeserializer<LocalDate>) (src, type, context) ->
                        LocalDate.parse(src.getAsString(), DateTimeFormatter.ISO_DATE)
                )
                .create();
    }


    public static RouteService getRouteService() {
        return routeService;
    }

    public static StationService getStationService() {
        return stationService;
    }

    public static TicketService getTicketService() {
        return ticketService;
    }

    public static UserService getUserService() {
        return userService;
    }

    public static SessionAnalyticService getSessionAnalyticService() {
        return sessionAnalyticService;
    }

    public static Gson getGson() {
        return gson;
    }


    private ServiceLocator() {
    }
}
