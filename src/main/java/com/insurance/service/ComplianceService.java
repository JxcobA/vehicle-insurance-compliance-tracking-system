package com.insurance.service;

import com.insurance.dao.InsurancePolicyDAO;
import com.insurance.dao.TripsDAO;
import com.insurance.model.InsurancePolicy;
import com.insurance.model.Trips;
import com.insurance.model.ViolationRecord;

import java.sql.SQLException;
import java.time.LocalDate;

public class ComplianceService {

    private final InsurancePolicyDAO policyDAO = new InsurancePolicyDAO();
    private final TripsDAO tripsDAO = new TripsDAO();

    public boolean isVehicleViolating(String regNumber) {
        try {
            InsurancePolicy policy = policyDAO.getActivePolicy(regNumber);

            if (policy == null) {
                return false;
            }

            LocalDate expiry = policy.getExpiryDate();
            LocalDate today = LocalDate.now();

            if (expiry.isAfter(today)) {
                return false;
            }

            Trips lastTripStart = tripsDAO.getLastTripStart(regNumber);
            Trips lastTripEnd = tripsDAO.getLastTripEnd(regNumber);

            if (lastTripStart == null) {
                return false;
            }

            if (lastTripEnd == null) {
                return lastTripStart.getEvent_timestamp().toLocalDate().isAfter(expiry);
            }

            boolean startAfterEnd =
                    lastTripStart.getEvent_timestamp().isAfter(lastTripEnd.getEvent_timestamp());

            boolean startAfterExpiry =
                    lastTripStart.getEvent_timestamp().toLocalDate().isAfter(expiry);

            return startAfterEnd && startAfterExpiry;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ViolationRecord getViolationRecord(String regNumber) {
        try {
            InsurancePolicy policy = policyDAO.getActivePolicy(regNumber);

            if (policy == null) {
                return null;
            }

            LocalDate expiry = policy.getExpiryDate();
            LocalDate today = LocalDate.now();

            if (expiry.isAfter(today)) {
                return null;
            }

            Trips lastTripStart = tripsDAO.getLastTripStart(regNumber);
            Trips lastTripEnd = tripsDAO.getLastTripEnd(regNumber);

            if (lastTripStart == null) {
                return null;
            }

            boolean isViolation;

            if (lastTripEnd == null) {
                isViolation = lastTripStart.getEvent_timestamp().toLocalDate().isAfter(expiry);
            } else {
                boolean startAfterEnd =
                        lastTripStart.getEvent_timestamp().isAfter(lastTripEnd.getEvent_timestamp());

                boolean startAfterExpiry =
                        lastTripStart.getEvent_timestamp().toLocalDate().isAfter(expiry);

                isViolation = startAfterEnd && startAfterExpiry;
            }

            if (!isViolation) {
                return null;
            }

            return new ViolationRecord(
                    regNumber,
                    expiry,
                    lastTripStart.getEvent_timestamp(),
                    lastTripEnd != null ? lastTripEnd.getEvent_timestamp() : null,
                    "Vehicle used after insurance expiry"
            );

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
