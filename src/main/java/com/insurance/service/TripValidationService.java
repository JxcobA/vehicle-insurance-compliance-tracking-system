package com.insurance.service;

import com.insurance.exceptions.TripSequenceException;
import com.insurance.model.Trips;
import com.insurance.model.TripValidationRequest;

import java.time.LocalDateTime;

public class TripValidationService implements Validatable<TripValidationRequest> {

    @Override
    public void validate(TripValidationRequest request) {
        validateTrips(request.getLastEvent(), request.getNewEventType(), request.getNewTimestamp());
    }

    public void validateTrips(Trips lastEvent, String newEventType, LocalDateTime newTimestamp) {

        if (newEventType == null || newTimestamp == null) {
            throw new TripSequenceException("Event type and timestamp must be filled");
        }

        if (!newEventType.equals("TRIP_START") && !newEventType.equals("TRIP_END")) {
            throw new TripSequenceException("Invalid event type: " + newEventType);
        }

        if (lastEvent == null) {
            if (newEventType.equals("TRIP_END")) {
                throw new TripSequenceException("Cannot record TRIP_END without prior TRIP_START");
            }
            return;
        }

        if (newTimestamp.isBefore(lastEvent.getEvent_timestamp())) {
            throw new TripSequenceException("New event timestamp cannot be before last recorded event");
        }

        if (lastEvent.getEvent_type().equals("TRIP_START") && newEventType.equals("TRIP_START")) {
            throw new TripSequenceException("Cannot record consecutive TRIP_START event");
        }

        if (lastEvent.getEvent_type().equals("TRIP_END") && newEventType.equals("TRIP_END")) {
            throw new TripSequenceException("Cannot record consecutive TRIP_END event");
        }
    }

    public boolean canStartTrip(Trips lastEvent) {
        return lastEvent == null || lastEvent.getEvent_type().equals("TRIP_END");
    }

    public boolean canEndTrip(Trips lastEvent) {
        return lastEvent != null && lastEvent.getEvent_type().equals("TRIP_START");
    }
}