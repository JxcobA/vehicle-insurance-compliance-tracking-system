package com.insurance.dao;

import com.insurance.model.Vehicle;
import com.insurance.config.DatabaseConnection;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;

public class VehicleDAO extends BaseDAO {
    public Vehicle getVehicleByReg(String reg) {
        String sql = "SELECT * FROM vehicles WHERE registration_number = ?";

        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, reg);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Vehicle(
                        rs.getString("registration_number"),
                        rs.getString("make"),
                        rs.getString("model")


                );
            }
        } catch (SQLException e) {
            handleSQLException(e);
        }

        return null;
    }

    public Vehicle insertVehicle(String reg, String make, String model) {


        if (getVehicleByReg(reg) != null) {
            System.out.println("Vehicle already exists with registration: " + reg);
            return null;
        }

        String sql = "INSERT INTO vehicles (registration_number, make, model) VALUES (?, ?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, reg);
            stmt.setString(2, make);
            stmt.setString(3, model);

            int rowsInserted = stmt.executeUpdate();

            if (rowsInserted > 0) {
                return new Vehicle(reg, make, model);
            }

        } catch (SQLException e) {
            handleSQLException(e);
        }

        return null;
    }

    public Vehicle deleteByReg(String reg) {


        Vehicle existing = getVehicleByReg(reg);

        if (existing == null) {
            System.out.println("No vehicle found with reg: " + reg);
            return null;
        }

        String sql = "DELETE FROM vehicles WHERE registration_number = ?";

        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, reg);

            int rowsDeleted = stmt.executeUpdate();

            if (rowsDeleted > 0) {
                System.out.println("Deleted vehicle with reg: " + reg);
                return existing;
            }

        } catch (SQLException e) {
            handleSQLException(e);
        }

        return null;
    }

    public List<Vehicle> getAllVehicles() {
        String sql = "SELECT * FROM vehicles";
        List<Vehicle> vehicles = new ArrayList<>();

        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                vehicles.add(new Vehicle(
                        rs.getString("registration_number"),
                        rs.getString("make"),
                        rs.getString("model")
                ));
            }

        } catch (SQLException e) {
            handleSQLException(e);
        }

        return vehicles;
    }


}