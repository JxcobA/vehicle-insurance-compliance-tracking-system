package com.insurance.dao;

import com.insurance.model.InsurancePolicy;
import com.insurance.config.DatabaseConnection;
import com.insurance.service.PolicyValidationService;

import java.sql.*;

import java.sql.SQLException;

public class InsurancePolicyDAO {

    // Create: Insert a new insurance policy
    public void createPolicy(InsurancePolicy insurancePolicy) throws SQLException {
        new PolicyValidationService().validatePolicy(insurancePolicy);

        String sql = """
                INSERT INTO insurance_policies
                (registration_number, policy_type, issue_date, expiry_date, is_active)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection connection = DatabaseConnection.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, insurancePolicy.getRegNumber());
            ps.setString(2, insurancePolicy.getPolicyType());
            ps.setDate(3, Date.valueOf(insurancePolicy.getIssueDate()));
            ps.setDate(4, Date.valueOf(insurancePolicy.getExpiryDate()));
            ps.setBoolean(5, insurancePolicy.isActive());
            ps.executeUpdate(); // Executes INSERT query

            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                insurancePolicy.setId(keys.getInt(1));
            }
        }
    }

    // Read: get active policy for a vehicle
    public InsurancePolicy getActivePolicy(String regNumber) throws SQLException {
        String sql = """
                SELECT * FROM insurance_policies
                WHERE registration_number = ?
                AND is_active = TRUE
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, regNumber);
            ResultSet rs = ps.executeQuery(); // ResultSet rs: An object that represents the data returned from the sql query

            if (rs.next()) { // Moves cursor to next line
                InsurancePolicy policy = new InsurancePolicy(
                        rs.getString("registration_number"),
                        rs.getDate("issue_date").toLocalDate(),
                        rs.getDate("expiry_date").toLocalDate(),
                        rs.getString("policy_type"),
                        rs.getBoolean("is_active")
                );
                policy.setId(rs.getInt("id"));
                return policy;
            }
        }
        return null;
    }

    // Update: deactivate a policy when using a new one
    public void deactivatePolicy(int policyId) throws SQLException {
        String sql = """
                UPDATE insurance_policies
                SET is_active = FALSE
                WHERE id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, policyId);
            ps.executeUpdate();
        }

    }




}
