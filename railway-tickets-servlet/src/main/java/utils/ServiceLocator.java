package utils;

import com.google.gson.*;
import repository.ConnectionPoolHolder;
import repository.impl.RouteRepositoryImpl;
import repository.impl.StationRepositoryImpl;
import repository.impl.TicketRepositoryImpl;
import repository.impl.UserRepositoryImpl;
import service.RouteService;
import service.StationService;
import service.TicketService;
import service.UserService;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class ServiceLocator {

    private static final RouteService routeService;
    private static final StationService stationService;
    private static final TicketService ticketService;
    private static final UserService userService;
    private static final Gson gson;

    static {
        DataSource dataSource = ConnectionPoolHolder.getDataSource();

        gson = createGson();

        RouteRepositoryImpl routeRepository = new RouteRepositoryImpl(dataSource);
        StationRepositoryImpl stationRepository = new StationRepositoryImpl(dataSource);
        TicketRepositoryImpl ticketRepository = new TicketRepositoryImpl(dataSource);
        UserRepositoryImpl userRepository = new UserRepositoryImpl(dataSource);

        routeService = new RouteService(routeRepository, stationRepository, ticketRepository);
        stationService = new StationService(stationRepository);
        ticketService = new TicketService(ticketRepository, routeService);
        userService = new UserService(userRepository);
    }

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

    public static Gson getGson() {
        return gson;
    }


    private ServiceLocator() {
    }
}
