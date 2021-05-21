package repository.impl;

import model.Role;
import model.User;
import repository.UserRepository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

public class UserRepositoryImpl implements UserRepository {
    private static final Logger LOGGER = Logger.getLogger(StationRepositoryImpl.class.getName());
    private final DataSource dataSource;

    public UserRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    @Override
    public User findByEmail(String email) {
        String query = "SELECT id," +
                "              first_name," +
                "              last_name," +
                "              phone," +
                "              password" +
                "        FROM users " +
                "       WHERE email = ?";
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
                    user.setRoles(findRolesByUserId(user.getId()));
                    return user;
                }
            }
        } catch (SQLException e) {
            LOGGER.severe(e.getMessage());
        }
        return null;
    }

    @Override
    public User findByEmailAndPassword(String email, String password) {
        String query = "SELECT id," +
                "              first_name," +
                "              last_name," +
                "              phone" +
                "       FROM users " +
                "       WHERE email = ? AND password = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {
            int parameterIndex = 0;
            preparedStatement.setString(++parameterIndex, email);
            preparedStatement.setString(++parameterIndex, password);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    User user = new User();
                    user.setId(resultSet.getLong("id"));
                    user.setFirstName(resultSet.getString("first_name"));
                    user.setLastName(resultSet.getString("last_name"));
                    user.setPhone(resultSet.getString("phone"));
                    user.setEmail(email);
                    user.setPassword(password);
                    user.setRoles(findRolesByUserId(user.getId()));
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
        String usersInsert = "INSERT INTO users ("
                + "                 first_name, "
                + "                 last_name, "
                + "                 phone, "
                + "                 email, "
                + "                 password) "
                + "     VALUES (?, ?, ?, ?, ?)";
        String rolesInsert = "INSERT INTO users_roles ("
                + "                 user_id, "
                + "                 roles) "
                + "     VALUES (?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement psUsersInsert = connection.prepareStatement(
                     usersInsert, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement psRolesInsert = connection.prepareStatement(rolesInsert)
        ) {
            connection.setAutoCommit(false);

            try {
                int parameterIndex1 = 0;
                psUsersInsert.setString(++parameterIndex1, user.getFirstName());
                psUsersInsert.setString(++parameterIndex1, user.getLastName());
                psUsersInsert.setString(++parameterIndex1, user.getPhone());
                psUsersInsert.setString(++parameterIndex1, user.getEmail());
                psUsersInsert.setString(++parameterIndex1, user.getPassword());
                if (psUsersInsert.executeUpdate() > 0) {
                    try (ResultSet generatedKeys = psUsersInsert.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            user.setId(generatedKeys.getLong(1));
                        }
                    }
                }

                int parameterIndex2 = 0;
                psRolesInsert.setLong(++parameterIndex2, user.getId());
                psRolesInsert.setString(++parameterIndex2, Role.USER.name());
                psRolesInsert.executeUpdate();

                connection.commit();
                connection.setAutoCommit(true);
                return user;
            } catch (SQLException e) {
                LOGGER.severe(e.getMessage());
                connection.rollback();
            }

        } catch (SQLException e) {
            LOGGER.severe(e.getMessage());
        }

        return null;
    }

    private Set<Role> findRolesByUserId(Long id) {
        String query = "SELECT roles FROM users_roles WHERE user_id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {

            preparedStatement.setLong(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                Set<Role> roles = new HashSet<>();
                while (resultSet.next()) {
                    roles.add(Role.valueOf(resultSet.getString("roles")));
                }
                return roles;
            }
        } catch (SQLException e) {
            LOGGER.severe(e.getMessage());
        }
        return Set.of();
    }
}
