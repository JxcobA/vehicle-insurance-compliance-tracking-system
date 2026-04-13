package com.insurance.model;

import java.time.LocalDateTime;

public class TripValidationRequest {

    private final Trips lastEvent;
    private final String newEventType;
    private final LocalDateTime newTimestamp;

    public TripValidationRequest(Trips lastEvent, String newEventType, LocalDateTime newTimestamp) {
        this.lastEvent = lastEvent;
        this.newEventType = newEventType;
        this.newTimestamp = newTimestamp;
    }

    public Trips getLastEvent() {
        return lastEvent;
    }

    public String getNewEventType() {
        return newEventType;
    }

    public LocalDateTime getNewTimestamp() {
        return newTimestamp;
    }
}