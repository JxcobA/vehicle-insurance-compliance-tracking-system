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

            // If no active policy found - return null
            if (policy == null) {
                return false;
            }

            LocalDate expiry = policy.getExpiryDate();
            LocalDate today = LocalDate.now();

            // If policy exists but has not yet expired - return null
            if (expiry.isAfter(today)) {
                return false;
            }

            Trips lastTripStart = tripsDAO.getLastTripStart(regNumber);
            Trips lastTripEnd = tripsDAO.getLastTripEnd(regNumber);

            // If policy has expired, but no trip has started - return null
            if (lastTripStart == null) {
                return false;
            }

            // If policy has expired, a trip has started but not ended, and trip start is after expiry - violation
            // i.e. No trip end recorded — violation if trip start is after expiry
            if (lastTripEnd == null) {
                return lastTripStart.getEvent_timestamp().toLocalDate().isAfter(expiry);
            }

            // If policy has expired and trip start is after trip end - startAfterEnd = true
            boolean startAfterEnd =
                    lastTripStart.getEvent_timestamp().isAfter(lastTripEnd.getEvent_timestamp());

            // If policy has expired and trip start is after expiry - startAfterExpiry = true
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
