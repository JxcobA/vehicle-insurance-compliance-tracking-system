package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:postgresql://localhost:5432/Vehicle%20Insurance%20Compliance%20Tracking%20System";
    private static final String USER = "postgres";
    private static final String PASSWORD = "Billy030707!";

    public static Connection getConnection() throws SQLException{
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
