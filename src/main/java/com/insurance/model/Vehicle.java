package com.insurance.model;

import java.time.LocalDate;

public class Vehicle {

    private final String regNumber; // PK
    private String model;
    private String make;


    public Vehicle(String regNumber, String make, String model) {
        this.regNumber = regNumber;
        this.make = make;
        this.model = model;

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

//    public void PrintVehicleInfo(Vehicle v){
//        if (v != null) {
//            System.out.println("Registration: " + v.getRegNumber());
//            System.out.println("Make: " + v.getMake());
//            System.out.println("Model: " + v.getModel());
//        } else {
//            System.out.println("Vehicle not found.");
//        }
//    }

    // Implemented the above as a toString override


    @Override
    public String toString() {
        return "Vehicle: " + make + " " + model + "\nRegistration: " + regNumber;
    }
}
