package repository.impl;

import model.Route;
import model.Station;
import repository.RouteRepository;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class RouteRepositoryImpl implements RouteRepository {
    private static final Logger LOGGER = Logger.getLogger(StationRepositoryImpl.class.getName());
    private final DataSource dataSource;

    public RouteRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    @Override
    public List<Route> findAll() {
        String query = "SELECT route.id,"
                + "            route.departure_time,"
                + "            route.arrival_time,"
                + "            route.train_name,"
                + "            route.total_seats,"
                + "            route.price_per_seat,"
                + "            departure_station.id,"
                + "            departure_station.city,"
                + "            departure_station.name,"
                + "            arrival_station.id,"
                + "            arrival_station.name,"
                + "            arrival_station.city"
                + "     FROM routes route"
                + "         INNER JOIN stations departure_station"
                + "            ON route.departure_station_id = departure_station.id"
                + "         INNER JOIN stations arrival_station"
                + "            ON route.arrival_station_id = arrival_station.id;";
        List<Route> routes = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)
        ) {
            while (resultSet.next()) {
                Station departureStation = new Station();
                departureStation.setId(resultSet.getLong("departure_station.id"));
                departureStation.setCity(resultSet.getString("departure_station.city"));
                departureStation.setName(resultSet.getString("departure_station.name"));

                Station arrivalStation = new Station();
                arrivalStation.setId(resultSet.getLong("arrival_station.id"));
                arrivalStation.setCity(resultSet.getString("arrival_station.city"));
                arrivalStation.setName(resultSet.getString("arrival_station.name"));

                Route route = new Route();
                route.setDepartureStation(departureStation);
                route.setArrivalStation(arrivalStation);
                route.setId(resultSet.getLong("route.id"));
                route.setDepartureTime(resultSet.getTimestamp("route.departure_time").toLocalDateTime());
                route.setArrivalTime(resultSet.getTimestamp("route.arrival_time").toLocalDateTime());
                route.setTrainName(resultSet.getString("route.train_name"));
                route.setTotalSeats(resultSet.getInt("route.total_seats"));
                route.setPricePerSeat(resultSet.getDouble("route.price_per_seat"));

                routes.add(route);
            }
        } catch (SQLException e) {
            LOGGER.severe(e.getMessage());
        }
        return routes;
    }

    @Override
    public Route save(Route route) {
        String query = "INSERT INTO routes ("
                + "                 departure_station_id, "
                + "                 arrival_station_id, "
                + "                 departure_time, "
                + "                 arrival_time, "
                + "                 train_name, "
                + "                 total_seats, "
                + "                 price_per_seat) "
                + "     VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     query, Statement.RETURN_GENERATED_KEYS)
        ) {
            int parameterIndex = 0;
            preparedStatement.setLong(++parameterIndex, route.getDepartureStation().getId());
            preparedStatement.setLong(++parameterIndex, route.getArrivalStation().getId());
            preparedStatement.setTimestamp(++parameterIndex, Timestamp.valueOf(route.getDepartureTime()));
            preparedStatement.setTimestamp(++parameterIndex, Timestamp.valueOf(route.getArrivalTime()));
            preparedStatement.setString(++parameterIndex, route.getTrainName());
            preparedStatement.setInt(++parameterIndex, route.getTotalSeats());
            preparedStatement.setDouble(++parameterIndex, route.getPricePerSeat());

            if (preparedStatement.executeUpdate() > 0) {
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        route.setId(generatedKeys.getLong(1));
                        return route;
                    }
                }
            }
        } catch (SQLException e) {
            LOGGER.severe(e.getMessage());
        }

        return null;
    }

    @Override
    public void deleteById(Long routeId) {
        String query = "DELETE FROM routes WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {
            preparedStatement.setLong(1, routeId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.severe(e.getMessage());
        }
    }

    @Override
    public Optional<Route> findById(Long routeId) {
        String query = "SELECT route.id,"
                + "            route.departure_time,"
                + "            route.arrival_time,"
                + "            route.train_name,"
                + "            route.total_seats,"
                + "            route.price_per_seat,"
                + "            departure_station.id,"
                + "            departure_station.city,"
                + "            departure_station.name,"
                + "            arrival_station.id,"
                + "            arrival_station.name,"
                + "            arrival_station.city"
                + "     FROM routes route"
                + "         INNER JOIN stations departure_station"
                + "            ON route.departure_station_id = departure_station.id"
                + "         INNER JOIN stations arrival_station"
                + "            ON route.arrival_station_id = arrival_station.id"
                + "     WHERE route.id = ?;";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {
            preparedStatement.setLong(1, routeId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Station departureStation = new Station();
                    departureStation.setId(resultSet.getLong("departure_station.id"));
                    departureStation.setCity(resultSet.getString("departure_station.city"));
                    departureStation.setName(resultSet.getString("departure_station.name"));

                    Station arrivalStation = new Station();
                    arrivalStation.setId(resultSet.getLong("arrival_station.id"));
                    arrivalStation.setCity(resultSet.getString("arrival_station.city"));
                    arrivalStation.setName(resultSet.getString("arrival_station.name"));

                    Route route = new Route();
                    route.setDepartureStation(departureStation);
                    route.setArrivalStation(arrivalStation);
                    route.setId(resultSet.getLong("route.id"));
                    route.setDepartureTime(resultSet.getTimestamp("route.departure_time").toLocalDateTime());
                    route.setArrivalTime(resultSet.getTimestamp("route.arrival_time").toLocalDateTime());
                    route.setTrainName(resultSet.getString("route.train_name"));
                    route.setTotalSeats(resultSet.getInt("route.total_seats"));
                    route.setPricePerSeat(resultSet.getDouble("route.price_per_seat"));

                    return Optional.of(route);
                }
            }
        } catch (SQLException e) {
            LOGGER.severe(e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<Route> findAvailableRoutesByCities(String departureCity, String arrivalCity, LocalDateTime departureDateStart, LocalDateTime departureDateEnd) {
        String query = "SELECT route.id,"
                + "            route.departure_time,"
                + "            route.arrival_time,"
                + "            route.train_name,"
                + "            route.total_seats,"
                + "            route.price_per_seat,"
                + "            departure_station.id,"
                + "            departure_station.city,"
                + "            departure_station.name,"
                + "            arrival_station.id,"
                + "            arrival_station.name,"
                + "            arrival_station.city"
                + "     FROM routes route"
                + "         INNER JOIN stations departure_station"
                + "            ON route.departure_station_id = departure_station.id"
                + "         INNER JOIN stations arrival_station"
                + "            ON route.arrival_station_id = arrival_station.id"
                + "     WHERE   departure_station.city = ?"
                + "         AND arrival_station.city = ?"
                + "         AND route.departure_time >= ?"
                + "         AND route.departure_time < ?;";

        List<Route> availableRoutes = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {
            int parameterIndex = 0;
            preparedStatement.setString(++parameterIndex, departureCity);
            preparedStatement.setString(++parameterIndex, arrivalCity);
            preparedStatement.setTimestamp(++parameterIndex, Timestamp.valueOf(departureDateStart));
            preparedStatement.setTimestamp(++parameterIndex, Timestamp.valueOf(departureDateEnd));
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Station departureStation = new Station();
                    departureStation.setId(resultSet.getLong("departure_station.id"));
                    departureStation.setCity(resultSet.getString("departure_station.city"));
                    departureStation.setName(resultSet.getString("departure_station.name"));

                    Station arrivalStation = new Station();
                    arrivalStation.setId(resultSet.getLong("arrival_station.id"));
                    arrivalStation.setCity(resultSet.getString("arrival_station.city"));
                    arrivalStation.setName(resultSet.getString("arrival_station.name"));

                    Route route = new Route();
                    route.setDepartureStation(departureStation);
                    route.setArrivalStation(arrivalStation);
                    route.setId(resultSet.getLong("route.id"));
                    route.setDepartureTime(resultSet.getTimestamp("route.departure_time").toLocalDateTime());
                    route.setArrivalTime(resultSet.getTimestamp("route.arrival_time").toLocalDateTime());
                    route.setTrainName(resultSet.getString("route.train_name"));
                    route.setTotalSeats(resultSet.getInt("route.total_seats"));
                    route.setPricePerSeat(resultSet.getDouble("route.price_per_seat"));

                    availableRoutes.add(route);
                }
            }
        } catch (SQLException e) {
            LOGGER.severe(e.getMessage());
        }

        return availableRoutes;
    }
}
