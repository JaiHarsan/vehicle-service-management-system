package com.vehicleservice.service;

import com.vehicleservice.exception.CustomerNotFoundException;
import com.vehicleservice.exception.DuplicateVehicleException;
import com.vehicleservice.exception.VehicleNotFoundException;
import com.vehicleservice.model.Vehicle;
import com.vehicleservice.model.VehicleType;
import com.vehicleservice.repository.CustomerRepository;
import com.vehicleservice.repository.VehicleRepository;
import com.vehicleservice.util.InputValidator;

import java.util.List;

public class VehicleService {
    private final VehicleRepository vehicleRepository;
    private final CustomerRepository customerRepository;

    public VehicleService(VehicleRepository vehicleRepository, CustomerRepository customerRepository) {
        this.vehicleRepository = vehicleRepository;
        this.customerRepository = customerRepository;
    }

    public Vehicle addVehicle(String registrationNumber, String brand, String model, VehicleType vehicleType, int customerId) {
        // Rule 1: Customer must exist
        customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Cannot add vehicle. Customer not found with ID: " + customerId));

        // Rule 2: Registration number must be valid
        if (!InputValidator.isValidRegistrationNumber(registrationNumber)) {
            throw new IllegalArgumentException("Registration number cannot be empty.");
        }

        // Rule 3: Registration number must be unique
        if (vehicleRepository.findByRegistrationNumber(registrationNumber).isPresent()) {
            throw new DuplicateVehicleException("Vehicle with registration number '" + registrationNumber.toUpperCase() + "' already exists.");
        }

        Vehicle vehicle = new Vehicle(registrationNumber.trim().toUpperCase(), brand, model, vehicleType != null ? vehicleType : VehicleType.CAR, customerId);
        return vehicleRepository.save(vehicle);
    }

    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }

    public Vehicle getVehicleById(int vehicleId) {
        return vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new VehicleNotFoundException("Vehicle not found with ID: " + vehicleId));
    }

    public Vehicle getVehicleByRegistrationNumber(String registrationNumber) {
        return vehicleRepository.findByRegistrationNumber(registrationNumber)
                .orElseThrow(() -> new VehicleNotFoundException("Vehicle not found with Registration Number: " + registrationNumber));
    }

    public List<Vehicle> getVehiclesByCustomerId(int customerId) {
        customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with ID: " + customerId));
        return vehicleRepository.findByCustomerId(customerId);
    }

    public boolean updateVehicle(Vehicle vehicle) {
        getVehicleById(vehicle.getVehicleId()); // Validate existence
        return vehicleRepository.update(vehicle);
    }

    public boolean deleteVehicle(int vehicleId) {
        getVehicleById(vehicleId); // Validate existence
        return vehicleRepository.delete(vehicleId);
    }
}
