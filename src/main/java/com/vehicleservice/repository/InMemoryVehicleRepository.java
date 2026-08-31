package com.vehicleservice.repository;

import com.vehicleservice.model.Vehicle;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class InMemoryVehicleRepository implements VehicleRepository {
    private final List<Vehicle> vehicles = new ArrayList<>();
    private final AtomicInteger idGenerator = new AtomicInteger(101);

    @Override
    public Vehicle save(Vehicle vehicle) {
        if (vehicle.getVehicleId() <= 0) {
            vehicle.setVehicleId(idGenerator.getAndIncrement());
        }
        vehicles.add(vehicle);
        return vehicle;
    }

    @Override
    public Optional<Vehicle> findById(int vehicleId) {
        return vehicles.stream()
                .filter(v -> v.getVehicleId() == vehicleId)
                .findFirst();
    }

    @Override
    public Optional<Vehicle> findByRegistrationNumber(String registrationNumber) {
        if (registrationNumber == null) return Optional.empty();
        return vehicles.stream()
                .filter(v -> v.getRegistrationNumber().equalsIgnoreCase(registrationNumber.trim()))
                .findFirst();
    }

    @Override
    public List<Vehicle> findByCustomerId(int customerId) {
        return vehicles.stream()
                .filter(v -> v.getCustomerId() == customerId)
                .collect(Collectors.toList());
    }

    @Override
    public List<Vehicle> findAll() {
        return new ArrayList<>(vehicles);
    }

    @Override
    public boolean update(Vehicle vehicle) {
        for (int i = 0; i < vehicles.size(); i++) {
            if (vehicles.get(i).getVehicleId() == vehicle.getVehicleId()) {
                vehicles.set(i, vehicle);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean delete(int vehicleId) {
        return vehicles.removeIf(v -> v.getVehicleId() == vehicleId);
    }
}
