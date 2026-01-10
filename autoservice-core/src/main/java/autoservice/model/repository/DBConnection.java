package autoservice.model.repository;

import autoservice.model.exceptions.DBException;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class DBConnection {
    private static DBConnection instance;
    private Connection connection;

    private final String SOURCEFILE = "secrets.properties";
    private DBConnection() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(SOURCEFILE)) {
            Properties props = new Properties();
            props.load(input);
            String url = props.getProperty("jdbc.url");
            String username = props.getProperty("jdbc.username");
            String password = props.getProperty("jdbc.password");
            connection = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            throw new DBException("Cannot connect to DB: " + e);
        }
    }

    public static DBConnection getInstance() {
        if (instance == null) {
            instance = new DBConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}
