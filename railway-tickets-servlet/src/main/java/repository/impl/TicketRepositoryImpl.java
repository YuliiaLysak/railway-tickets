package repository.impl;

import model.Ticket;
import repository.TicketRepository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.logging.Logger;

public class TicketRepositoryImpl implements TicketRepository {
    private static final Logger LOGGER = Logger.getLogger(StationRepositoryImpl.class.getName());
    private final DataSource dataSource;

    public TicketRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Ticket save(Ticket ticket) {
        String query = "INSERT INTO tickets (user_id, route_id, purchase_date) VALUES (?, ?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     query, Statement.RETURN_GENERATED_KEYS)
        ) {
            int parameterIndex = 0;
            preparedStatement.setLong(++parameterIndex, ticket.getUserId());
            preparedStatement.setLong(++parameterIndex, ticket.getRouteId());
            preparedStatement.setTimestamp(++parameterIndex, Timestamp.valueOf(ticket.getPurchaseDate()));
            if (preparedStatement.executeUpdate() > 0) {
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        ticket.setId(generatedKeys.getLong(1));
                        return ticket;
                    }
                }
            }
        } catch (SQLException e) {
            LOGGER.severe(e.getMessage());
        }

        return null;
    }

    @Override
    public int findPurchasedTickets(Long routeId) {
        String query = "SELECT COUNT(id) AS total FROM tickets WHERE route_id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {
            preparedStatement.setLong(1, routeId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("total");
                }
            }
        } catch (SQLException e) {
            LOGGER.severe(e.getMessage());
        }
        return 0;
    }
}
