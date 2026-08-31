package com.vehicleservice.repository;

import com.vehicleservice.config.DatabaseConnection;
import com.vehicleservice.model.Availability;
import com.vehicleservice.model.Mechanic;
import com.vehicleservice.model.Specialization;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcMechanicRepository implements MechanicRepository {

    @Override
    public Mechanic save(Mechanic mechanic) {
        String sql = "INSERT INTO mechanics (name, phone, specialization, availability) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, mechanic.getName());
            stmt.setString(2, mechanic.getPhone());
            stmt.setString(3, mechanic.getSpecialization().name());
            stmt.setString(4, mechanic.getAvailability().name());

            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    mechanic.setMechanicId(generatedKeys.getInt(1));
                }
            }
            return mechanic;
        } catch (SQLException e) {
            throw new RuntimeException("Error saving mechanic to database: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Mechanic> findById(int mechanicId) {
        String sql = "SELECT * FROM mechanics WHERE mechanic_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, mechanicId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToMechanic(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching mechanic by ID: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<Mechanic> findAll() {
        List<Mechanic> mechanics = new ArrayList<>();
        String sql = "SELECT * FROM mechanics ORDER BY mechanic_id ASC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                mechanics.add(mapResultSetToMechanic(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching all mechanics: " + e.getMessage(), e);
        }
        return mechanics;
    }

    @Override
    public boolean update(Mechanic mechanic) {
        String sql = "UPDATE mechanics SET name = ?, phone = ?, specialization = ?, availability = ? WHERE mechanic_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, mechanic.getName());
            stmt.setString(2, mechanic.getPhone());
            stmt.setString(3, mechanic.getSpecialization().name());
            stmt.setString(4, mechanic.getAvailability().name());
            stmt.setInt(5, mechanic.getMechanicId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error updating mechanic: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean delete(int mechanicId) {
        String sql = "DELETE FROM mechanics WHERE mechanic_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, mechanicId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting mechanic: " + e.getMessage(), e);
        }
    }

    private Mechanic mapResultSetToMechanic(ResultSet rs) throws SQLException {
        Specialization spec = Specialization.GENERAL;
        try {
            spec = Specialization.valueOf(rs.getString("specialization"));
        } catch (Exception ignored) {}

        Availability avail = Availability.AVAILABLE;
        try {
            avail = Availability.valueOf(rs.getString("availability"));
        } catch (Exception ignored) {}

        return new Mechanic(
                rs.getInt("mechanic_id"),
                rs.getString("name"),
                rs.getString("phone"),
                spec,
                avail
        );
    }
}
