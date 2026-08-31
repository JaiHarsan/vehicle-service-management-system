package com.vehicleservice.repository;

import com.vehicleservice.model.Vehicle;
import java.util.List;
import java.util.Optional;

public interface VehicleRepository {
    Vehicle save(Vehicle vehicle);
    Optional<Vehicle> findById(int vehicleId);
    Optional<Vehicle> findByRegistrationNumber(String registrationNumber);
    List<Vehicle> findByCustomerId(int customerId);
    List<Vehicle> findAll();
    boolean update(Vehicle vehicle);
    boolean delete(int vehicleId);
}
