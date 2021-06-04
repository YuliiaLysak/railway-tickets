package edu.lysak.railwaytickets.repository;

import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

public class ConnectionPoolHolder {
    private static final Logger LOGGER = Logger.getLogger(ConnectionPoolHolder.class.getName());

    private static volatile DataSource dataSource;

    public static DataSource getDataSource() {
        if (dataSource == null) {
            synchronized (ConnectionPoolHolder.class) {
                if (dataSource == null) {
                    Properties properties = readProperties("application.properties");
                    BasicDataSource ds = new BasicDataSource();
                    ds.setDriverClassName(properties.getProperty("connection.driver"));
                    ds.setUrl(properties.getProperty("connection.url"));
                    ds.setUsername(properties.getProperty("connection.username"));
                    ds.setPassword(properties.getProperty("connection.password"));
                    ds.setMinIdle(5);
                    ds.setMaxIdle(10);
                    ds.setMaxOpenPreparedStatements(100);
                    dataSource = ds;
                }
            }
        }
        return dataSource;
    }

    private static Properties readProperties(String fileName) {
        ClassLoader classLoader = ConnectionPoolHolder.class.getClassLoader();
        try (InputStream input = classLoader.getResourceAsStream(fileName)) {
            Properties properties = new Properties();
            properties.load(input);
            return properties;
        } catch (IOException ex) {
            LOGGER.severe(ex.getMessage());
            throw new RuntimeException(ex);
        }
    }
}
