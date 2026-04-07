package com.insurance.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ViolationRecord {

    private String regNumber;
    private LocalDate expiryDate;
    private LocalDateTime lastTripStart;
    private LocalDateTime lastTripEnd;
    private String reason;

    public ViolationRecord(String regNumber, LocalDate expiryDate,
                           LocalDateTime lastTripStart, LocalDateTime lastTripEnd,
                           String reason) {
        this.regNumber = regNumber;
        this.expiryDate = expiryDate;
        this.lastTripStart = lastTripStart;
        this.lastTripEnd = lastTripEnd;
        this.reason = reason;
    }

    public String getRegNumber() {
        return regNumber;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public LocalDateTime getLastTripStart() {
        return lastTripStart;
    }

    public LocalDateTime getLastTripEnd() {
        return lastTripEnd;
    }

    public String getReason() {
        return reason;
    }
}