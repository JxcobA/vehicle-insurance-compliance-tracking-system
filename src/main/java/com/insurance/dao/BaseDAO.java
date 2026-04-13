package com.insurance.dao;

import java.sql.Connection;
import java.sql.SQLException;
import com.insurance.config.DatabaseConnection;

public abstract class BaseDAO {

    protected Connection getConnection() throws SQLException {
        return DatabaseConnection.getConnection();
    }

    protected void handleSQLException(SQLException e) {
        System.err.println("Database error in" + this.getClass().getSimpleName() + ": " + e.getMessage());
        e.printStackTrace();
    }
}
