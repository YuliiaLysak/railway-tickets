package repository.impl;

import model.Station;
import repository.StationRepository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class StationRepositoryImpl implements StationRepository {
    private static final Logger LOGGER = Logger.getLogger(StationRepositoryImpl.class.getName());
    private final DataSource dataSource;

    public StationRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Station findByCityAndName(String city, String name) {
        String query = "SELECT * FROM stations WHERE city = ? AND name = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {
            int parameterIndex = 0;
            preparedStatement.setString(++parameterIndex, city);
            preparedStatement.setString(++parameterIndex, name);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Long id = resultSet.getLong("id");
                    Station station = new Station();
                    station.setId(id);
                    station.setCity(city);
                    station.setName(name);
                    return station;
                }
            }
        } catch (SQLException e) {
            LOGGER.severe(e.getMessage());
        }
        return null;
    }

    @Override
    public void updateStationById(Long id, String city, String name) {
        String query = "UPDATE stations SET city = ?, name = ? WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {
            int parameterIndex = 0;
            preparedStatement.setString(++parameterIndex, city);
            preparedStatement.setString(++parameterIndex, name);
            preparedStatement.setLong(++parameterIndex, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.severe(e.getMessage());
        }
    }

    @Override
    public Station save(Station station) {
        String query = "INSERT INTO stations (city, name) VALUES (?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     query, Statement.RETURN_GENERATED_KEYS)
        ) {
            int parameterIndex = 0;
            preparedStatement.setString(++parameterIndex, station.getCity());
            preparedStatement.setString(++parameterIndex, station.getName());
            if (preparedStatement.executeUpdate() > 0) {
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        station.setId(generatedKeys.getLong(1));
                        return station;
                    }
                }
            }
        } catch (SQLException e) {
            LOGGER.severe(e.getMessage());
        }

        return null;
    }

    @Override
    public Optional<Station> findById(Long id) {
        String query = "SELECT * FROM stations WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {
            preparedStatement.setLong(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Station station = new Station();
                    station.setId(id);
                    station.setCity(resultSet.getString("city"));
                    station.setName(resultSet.getString("name"));
                    return Optional.of(station);
                }
            }
        } catch (SQLException e) {
            LOGGER.severe(e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public void deleteById(Long id) {
        String query = "DELETE FROM stations WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.severe(e.getMessage());
        }
    }

    @Override
    public List<Station> findAll() {
        String query = "SELECT * FROM stations";
        List<Station> stations = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)
        ) {
            while (resultSet.next()) {
                Station station = new Station();
                station.setId(resultSet.getLong("id"));
                station.setCity(resultSet.getString("city"));
                station.setName(resultSet.getString("name"));
                stations.add(station);
            }
        } catch (SQLException e) {
            LOGGER.severe(e.getMessage());
        }
        return stations;
    }
}
