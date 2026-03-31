package com.insurance.model;

import java.time.LocalDate;

public class Vehicle {

    private final String regNumber; // PK
    private String model;
    private String make;
    private LocalDate createdAt; // When vehicle is added to the system

    public Vehicle(String regNumber, String make, String model, LocalDate createdAt) {
        this.regNumber = regNumber;
        this.make = make;
        this.model = model;
        this.createdAt = LocalDate.now();

    }

    public String getRegNumber() {
        return regNumber;
    }

    public String getMake() {
        return make;
    }
    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }
    public void setModel(String model) {
        this.model = model;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

}
