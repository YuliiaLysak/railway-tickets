package edu.lysak.railwaytickets.repository.impl;

import edu.lysak.railwaytickets.model.SessionAnalytic;
import edu.lysak.railwaytickets.repository.SessionAnalyticRepository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.logging.Logger;

public class SessionAnalyticRepositoryImpl implements SessionAnalyticRepository {
    private static final Logger LOGGER = Logger.getLogger(SessionAnalyticRepositoryImpl.class.getName());
    private final DataSource dataSource;

    public SessionAnalyticRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void save(SessionAnalytic sessionAnalytic) {
        String query = "INSERT INTO session_analytic (" +
                "                   session_id, " +
                "                   user_id, " +
                "                   creation_time, " +
                "                   last_accessed_time, " +
                "                   search_route_request_count, " +
                "                   buy_ticket_request_count) " +
                "       VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     query, Statement.RETURN_GENERATED_KEYS)
        ) {
            int parameterIndex = 0;
            preparedStatement.setString(++parameterIndex, sessionAnalytic.getSessionId());
            preparedStatement.setLong(++parameterIndex, sessionAnalytic.getUserId());
            preparedStatement.setTimestamp(++parameterIndex, Timestamp.valueOf(sessionAnalytic.getCreationTime()));
            preparedStatement.setTimestamp(++parameterIndex, Timestamp.valueOf(sessionAnalytic.getLastAccessedTime()));
            preparedStatement.setInt(++parameterIndex, sessionAnalytic.getSearchRouteRequestCount());
            preparedStatement.setInt(++parameterIndex, sessionAnalytic.getBuyTicketRequestCount());
            if (preparedStatement.executeUpdate() > 0) {
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        sessionAnalytic.setAnalyticId(generatedKeys.getLong(1));
                    }
                }
            }
        } catch (SQLException e) {
            LOGGER.severe(e.getMessage());
        }
    }
}
