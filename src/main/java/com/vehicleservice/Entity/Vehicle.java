package com.vehicleservice.model;

public class Vehicle {
    private int vehicleId;
    private String registrationNumber;
    private String brand;
    private String model;
    private VehicleType vehicleType;
    private int customerId;

    public Vehicle() {
    }

    public Vehicle(int vehicleId, String registrationNumber, String brand, String model, VehicleType vehicleType, int customerId) {
        this.vehicleId = vehicleId;
        this.registrationNumber = registrationNumber;
        this.brand = brand;
        this.model = model;
        this.vehicleType = vehicleType;
        this.customerId = customerId;
    }

    public Vehicle(String registrationNumber, String brand, String model, VehicleType vehicleType, int customerId) {
        this.registrationNumber = registrationNumber;
        this.brand = brand;
        this.model = model;
        this.vehicleType = vehicleType;
        this.customerId = customerId;
    }

    public int getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "vehicleId=" + vehicleId +
                ", registrationNumber='" + registrationNumber + '\'' +
                ", brand='" + brand + '\'' +
                ", model='" + model + '\'' +
                ", vehicleType=" + vehicleType +
                ", customerId=" + customerId +
                '}';
    }
}
