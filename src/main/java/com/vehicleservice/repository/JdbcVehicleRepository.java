package com.vehicleservice.repository;

import com.vehicleservice.config.DatabaseConnection;
import com.vehicleservice.model.Vehicle;
import com.vehicleservice.model.VehicleType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcVehicleRepository implements VehicleRepository {

    @Override
    public Vehicle save(Vehicle vehicle) {
        String sql = "INSERT INTO vehicles (registration_number, brand, model, vehicle_type, customer_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, vehicle.getRegistrationNumber());
            stmt.setString(2, vehicle.getBrand());
            stmt.setString(3, vehicle.getModel());
            stmt.setString(4, vehicle.getVehicleType().name());
            stmt.setInt(5, vehicle.getCustomerId());

            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    vehicle.setVehicleId(generatedKeys.getInt(1));
                }
            }
            return vehicle;
        } catch (SQLException e) {
            throw new RuntimeException("Error saving vehicle to database: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Vehicle> findById(int vehicleId) {
        String sql = "SELECT * FROM vehicles WHERE vehicle_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, vehicleId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToVehicle(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching vehicle by ID: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Vehicle> findByRegistrationNumber(String registrationNumber) {
        String sql = "SELECT * FROM vehicles WHERE UPPER(registration_number) = UPPER(?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, registrationNumber != null ? registrationNumber.trim() : "");
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToVehicle(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching vehicle by registration number: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<Vehicle> findByCustomerId(int customerId) {
        List<Vehicle> vehicles = new ArrayList<>();
        String sql = "SELECT * FROM vehicles WHERE customer_id = ? ORDER BY vehicle_id ASC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, customerId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    vehicles.add(mapResultSetToVehicle(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching vehicles by customer ID: " + e.getMessage(), e);
        }
        return vehicles;
    }

    @Override
    public List<Vehicle> findAll() {
        List<Vehicle> vehicles = new ArrayList<>();
        String sql = "SELECT * FROM vehicles ORDER BY vehicle_id ASC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                vehicles.add(mapResultSetToVehicle(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching all vehicles: " + e.getMessage(), e);
        }
        return vehicles;
    }

    @Override
    public boolean update(Vehicle vehicle) {
        String sql = "UPDATE vehicles SET registration_number = ?, brand = ?, model = ?, vehicle_type = ?, customer_id = ? WHERE vehicle_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, vehicle.getRegistrationNumber());
            stmt.setString(2, vehicle.getBrand());
            stmt.setString(3, vehicle.getModel());
            stmt.setString(4, vehicle.getVehicleType().name());
            stmt.setInt(5, vehicle.getCustomerId());
            stmt.setInt(6, vehicle.getVehicleId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error updating vehicle: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean delete(int vehicleId) {
        String sql = "DELETE FROM vehicles WHERE vehicle_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, vehicleId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting vehicle: " + e.getMessage(), e);
        }
    }

    private Vehicle mapResultSetToVehicle(ResultSet rs) throws SQLException {
        VehicleType type = VehicleType.CAR;
        try {
            type = VehicleType.valueOf(rs.getString("vehicle_type"));
        } catch (Exception ignored) {}

        return new Vehicle(
                rs.getInt("vehicle_id"),
                rs.getString("registration_number"),
                rs.getString("brand"),
                rs.getString("model"),
                type,
                rs.getInt("customer_id")
        );
    }
}
