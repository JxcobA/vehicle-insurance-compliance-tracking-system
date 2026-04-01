package com.insurance.dao;

import com.insurance.model.Vehicle;
import com.insurance.model.Trips;
import com.insurance.config.DatabaseConnection;

import java.sql.*;
import java.time.LocalDateTime;

public class TripsDAO {

    public Trips getTripsofVehicle(String reg){
        String sql = "SELECT * FROM trip_events WHERE registration_number = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, reg);

            ResultSet rs = stmt.executeQuery();

            if ( rs.next()) {
                return new Trips(
                        rs.getInt("id"),
                        rs.getString("registration_number"),
                        rs.getString("event_type"),
                        rs.getTimestamp("event_timestamp").toLocalDateTime(),
                        rs.getString("location")
                                );
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    return null;
    };

    public Trips insertTrip(String reg, String eventType, LocalDateTime timestamp, String location) {

        String sql = "INSERT INTO trip_events (registration_number, event_type, event_timestamp, location) " +
                "VALUES (?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, reg);
            stmt.setString(2, eventType);
            stmt.setTimestamp(3, Timestamp.valueOf(timestamp));
            stmt.setString(4, location);

            int rowsInserted = stmt.executeUpdate();

            if (rowsInserted > 0) {

                // Get auto-generated ID
                ResultSet keys = stmt.getGeneratedKeys();
                if (keys.next()) {
                    int id = keys.getInt(1);

                    return new Trips(
                            id,
                            reg,
                            eventType,
                            timestamp,
                            location
                    );
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean deleteTripById(int id) {

        String sql = "DELETE FROM trip_events WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, id);

            int rowsDeleted = stmt.executeUpdate();

            return rowsDeleted > 0; // true if deleted, false if not found

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

}
