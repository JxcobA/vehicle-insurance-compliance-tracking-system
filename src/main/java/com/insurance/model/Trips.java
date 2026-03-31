package com.insurance.model;

import java.time.LocalDateTime;

public class Trips {


    private String regNumber;
    private String event_type;
    private LocalDateTime event_timestamp;
    private String location;

    public Trips() {
    }

    public Trips(String regNumber, String event_type, LocalDateTime event_timestamp, String location) {
        this.regNumber = regNumber;
        this.event_type = event_type;
        this.event_timestamp = event_timestamp;
        this.location = location;
    }

    public String getRegNumber() {
        return regNumber;
    }
    public void setRegNumber(String regNumber) {
        this.regNumber = regNumber;
    }

    public String getEvent_type() {
        return event_type;
    }
    public void setEvent_type(String event_type) {
        this.event_type = event_type;
    }

    public LocalDateTime getEvent_timestamp() {
        return event_timestamp;
    }
    public void setEvent_timestamp(LocalDateTime event_timestamp) {
        this.event_timestamp = event_timestamp;
    }

    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }


}
