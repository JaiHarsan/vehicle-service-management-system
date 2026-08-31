package com.vehicleservice.repository;

import com.vehicleservice.config.DatabaseConnection;
import com.vehicleservice.model.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcServiceRepository implements ServiceRepository {

    @Override
    public Service save(Service service) {
        String sql = "INSERT INTO services (service_name, base_price, description) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, service.getServiceName());
            stmt.setBigDecimal(2, service.getBasePrice());
            stmt.setString(3, service.getDescription());

            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    service.setServiceId(generatedKeys.getInt(1));
                }
            }
            return service;
        } catch (SQLException e) {
            throw new RuntimeException("Error saving service offering to database: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Service> findById(int serviceId) {
        String sql = "SELECT * FROM services WHERE service_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, serviceId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToService(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching service by ID: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<Service> findAll() {
        List<Service> services = new ArrayList<>();
        String sql = "SELECT * FROM services ORDER BY service_id ASC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                services.add(mapResultSetToService(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching all services: " + e.getMessage(), e);
        }
        return services;
    }

    @Override
    public boolean update(Service service) {
        String sql = "UPDATE services SET service_name = ?, base_price = ?, description = ? WHERE service_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, service.getServiceName());
            stmt.setBigDecimal(2, service.getBasePrice());
            stmt.setString(3, service.getDescription());
            stmt.setInt(4, service.getServiceId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error updating service: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean delete(int serviceId) {
        String sql = "DELETE FROM services WHERE service_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, serviceId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting service: " + e.getMessage(), e);
        }
    }

    private Service mapResultSetToService(ResultSet rs) throws SQLException {
        return new Service(
                rs.getInt("service_id"),
                rs.getString("service_name"),
                rs.getBigDecimal("base_price"),
                rs.getString("description")
        );
    }
}
