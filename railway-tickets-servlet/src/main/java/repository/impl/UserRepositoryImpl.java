package repository.impl;

import model.Station;
import model.User;
import repository.UserRepository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.logging.Logger;

public class UserRepositoryImpl implements UserRepository {
    private static final Logger LOGGER = Logger.getLogger(StationRepositoryImpl.class.getName());
    private final DataSource dataSource;

    public UserRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    @Override
    public User findByEmail(String email) {
        String query = "SELECT * FROM users WHERE email = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {
            preparedStatement.setString(1, email);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    User user = new User();
                    user.setId(resultSet.getLong("id"));
                    user.setFirstName(resultSet.getString("first_name"));
                    user.setLastName(resultSet.getString("last_name"));
                    user.setPhone(resultSet.getString("phone"));
                    user.setEmail(email);
                    user.setPassword(resultSet.getString("password"));
//                    TODO - implement roles
//                    user.setRoles();
                    return user;
                }
            }
        } catch (SQLException e) {
            LOGGER.severe(e.getMessage());
        }
        return null;
    }

    @Override
    public User save(User user) {
//      TODO - implement roles
        String query = "INSERT INTO users ("
                + "                 first_name, "
                + "                 last_name, "
                + "                 phone, "
                + "                 email, "
                + "                 password) "
                + "     VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     query, Statement.RETURN_GENERATED_KEYS)
        ) {
            int parameterIndex = 0;
            preparedStatement.setString(++parameterIndex, user.getFirstName());
            preparedStatement.setString(++parameterIndex, user.getLastName());
            preparedStatement.setString(++parameterIndex, user.getPhone());
            preparedStatement.setString(++parameterIndex, user.getEmail());
            preparedStatement.setString(++parameterIndex, user.getPassword());
            if (preparedStatement.executeUpdate() > 0) {
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        user.setId(generatedKeys.getLong(1));
                        return user;
                    }
                }
            }
        } catch (SQLException e) {
            LOGGER.severe(e.getMessage());
        }

        return null;
    }
}
