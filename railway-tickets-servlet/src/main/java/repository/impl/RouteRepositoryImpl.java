package repository.impl;

import exceptions.BusinessLogicException;
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
        String query = "SELECT r.id route_id,"
                + "            r.departure_time AS route_departure_time,"
                + "            r.arrival_time AS route_arrival_time,"
                + "            r.train_name AS route_train_name,"
                + "            r.total_seats AS route_total_seats,"
                + "            r.price_per_seat AS route_price_per_seat,"
                + "            d_s.id AS d_station_id,"
                + "            d_s.city AS d_station_city,"
                + "            d_s.name AS d_station_name,"
                + "            a_s.id AS a_station_id,"
                + "            a_s.city AS a_station_name,"
                + "            a_s.name AS a_station_city"
                + "     FROM routes AS r"
                + "         INNER JOIN stations AS d_s"
                + "            ON r.departure_station_id = d_s.id"
                + "         INNER JOIN stations AS a_s"
                + "            ON r.arrival_station_id = a_s.id";
        List<Route> routes = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)
        ) {
            while (resultSet.next()) {
                Station departureStation = toDepartureStation(resultSet);
                Station arrivalStation = toArrivalStation(resultSet);
                Route route = toRoute(resultSet, departureStation, arrivalStation);
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
                        return Route.builder(route)
                                .id(generatedKeys.getLong(1))
                                .build();
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
            LOGGER.severe("exception.route.delete");
            throw new BusinessLogicException(
                    "exception.route.delete"
            );
        }
    }

    @Override
    public Optional<Route> findById(Long routeId) {
        String query = "SELECT r.id route_id,"
                + "            r.departure_time AS route_departure_time,"
                + "            r.arrival_time AS route_arrival_time,"
                + "            r.train_name AS route_train_name,"
                + "            r.total_seats AS route_total_seats,"
                + "            r.price_per_seat AS route_price_per_seat,"
                + "            d_s.id AS d_station_id,"
                + "            d_s.city AS d_station_city,"
                + "            d_s.name AS d_station_name,"
                + "            a_s.id AS a_station_id,"
                + "            a_s.city AS a_station_name,"
                + "            a_s.name AS a_station_city"
                + "     FROM routes AS r"
                + "         INNER JOIN stations AS d_s"
                + "            ON r.departure_station_id = d_s.id"
                + "         INNER JOIN stations AS a_s"
                + "            ON r.arrival_station_id = a_s.id"
                + "     WHERE r.id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {
            preparedStatement.setLong(1, routeId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Station departureStation = toDepartureStation(resultSet);
                    Station arrivalStation = toArrivalStation(resultSet);
                    Route route = toRoute(resultSet, departureStation, arrivalStation);
                    return Optional.of(route);
                }
            }
        } catch (SQLException e) {
            LOGGER.severe(e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public void update(Route updatedRoute) {
        String query = "" +
                " UPDATE routes" +
                " SET departure_station_id = ?," +
                "     arrival_station_id = ?," +
                "     departure_time = ?," +
                "     arrival_time = ?," +
                "     train_name = ?," +
                "     total_seats = ?," +
                "     price_per_seat = ?" +
                " WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {
            int parameterIndex = 0;
            preparedStatement.setLong(++parameterIndex, updatedRoute.getDepartureStation().getId());
            preparedStatement.setLong(++parameterIndex, updatedRoute.getArrivalStation().getId());
            preparedStatement.setTimestamp(++parameterIndex, Timestamp.valueOf(updatedRoute.getDepartureTime()));
            preparedStatement.setTimestamp(++parameterIndex, Timestamp.valueOf(updatedRoute.getArrivalTime()));
            preparedStatement.setString(++parameterIndex, updatedRoute.getTrainName());
            preparedStatement.setInt(++parameterIndex, updatedRoute.getTotalSeats());
            preparedStatement.setDouble(++parameterIndex, updatedRoute.getPricePerSeat());
            preparedStatement.setLong(++parameterIndex, updatedRoute.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.severe(e.getMessage());
        }
    }

    @Override
    public List<Route> findAvailableRoutesByCities(String departureCity, String arrivalCity, LocalDateTime departureDateStart, LocalDateTime departureDateEnd) {
        String query = "SELECT r.id route_id,"
                + "            r.departure_time AS route_departure_time,"
                + "            r.arrival_time AS route_arrival_time,"
                + "            r.train_name AS route_train_name,"
                + "            r.total_seats AS route_total_seats,"
                + "            r.price_per_seat AS route_price_per_seat,"
                + "            d_s.id AS d_station_id,"
                + "            d_s.city AS d_station_city,"
                + "            d_s.name AS d_station_name,"
                + "            a_s.id AS a_station_id,"
                + "            a_s.city AS a_station_name,"
                + "            a_s.name AS a_station_city"
                + "     FROM routes AS r"
                + "         INNER JOIN stations AS d_s"
                + "            ON r.departure_station_id = d_s.id"
                + "         INNER JOIN stations AS a_s"
                + "            ON r.arrival_station_id = a_s.id"
                + "     WHERE   d_s.city = ?"
                + "         AND a_s.city = ?"
                + "         AND r.departure_time >= ?"
                + "         AND r.departure_time < ?;";

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
                    Station departureStation = toDepartureStation(resultSet);
                    Station arrivalStation = toArrivalStation(resultSet);
                    Route route = toRoute(resultSet, departureStation, arrivalStation);
                    availableRoutes.add(route);
                }
            }
        } catch (SQLException e) {
            LOGGER.severe(e.getMessage());
        }

        return availableRoutes;
    }

    private Route toRoute(ResultSet resultSet, Station departureStation, Station arrivalStation) throws SQLException {
        return Route.builder()
                .departureStation(departureStation)
                .arrivalStation(arrivalStation)
                .id(resultSet.getLong("route_id"))
                .departureTime(resultSet.getTimestamp("route_departure_time").toLocalDateTime())
                .arrivalTime(resultSet.getTimestamp("route_arrival_time").toLocalDateTime())
                .trainName(resultSet.getString("route_train_name"))
                .totalSeats(resultSet.getInt("route_total_seats"))
                .pricePerSeat((resultSet.getDouble("route_price_per_seat")))
                .build();
    }

    private Station toDepartureStation(ResultSet resultSet) throws SQLException {
        Station departureStation = new Station();
        departureStation.setId(resultSet.getLong("d_station_id"));
        departureStation.setCity(resultSet.getString("d_station_city"));
        departureStation.setName(resultSet.getString("d_station_name"));
        return departureStation;
    }

    private Station toArrivalStation(ResultSet resultSet) throws SQLException {
        Station arrivalStation = new Station();
        arrivalStation.setId(resultSet.getLong("a_station_id"));
        arrivalStation.setCity(resultSet.getString("a_station_city"));
        arrivalStation.setName(resultSet.getString("a_station_name"));
        return arrivalStation;
    }
}
