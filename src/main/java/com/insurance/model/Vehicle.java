package com.insurance.model;

public class Vehicle {

    private String regNumber;
    private String model;
    private String make;

    public Vehicle(String regNumber, String model, String make) {
        this.regNumber = regNumber;
        this.model = model;
        this.make = make;
    }

    public String getRegNumber() {
        return regNumber;
    }
    public void setRegNumber(String regNumber) {
        this.regNumber = regNumber;
    }

    public String getModel() {
        return model;
    }
    public void setModel(String model) {
        this.model = model;
    }

    public String getMake() {
        return make;
    }
    public void setMake(String make) {
        this.make = make;
    }

}
