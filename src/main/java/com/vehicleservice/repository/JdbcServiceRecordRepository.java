package com.vehicleservice.repository;

import com.vehicleservice.config.DatabaseConnection;
import com.vehicleservice.model.ServiceRecord;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcServiceRecordRepository implements ServiceRecordRepository {

    @Override
    public ServiceRecord save(ServiceRecord record) {
        String sql = "INSERT INTO service_records (booking_id, completion_date, remarks, total_service_cost) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, record.getBookingId());
            stmt.setDate(2, Date.valueOf(record.getCompletionDate()));
            stmt.setString(3, record.getRemarks());
            stmt.setBigDecimal(4, record.getTotalServiceCost());

            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    record.setRecordId(generatedKeys.getInt(1));
                }
            }
            return record;
        } catch (SQLException e) {
            throw new RuntimeException("Error saving service record to database: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<ServiceRecord> findById(int recordId) {
        String sql = "SELECT * FROM service_records WHERE record_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, recordId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToRecord(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching service record by ID: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<ServiceRecord> findByBookingId(int bookingId) {
        String sql = "SELECT * FROM service_records WHERE booking_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, bookingId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToRecord(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching service record by booking ID: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<ServiceRecord> findAll() {
        List<ServiceRecord> records = new ArrayList<>();
        String sql = "SELECT * FROM service_records ORDER BY record_id ASC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                records.add(mapResultSetToRecord(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching all service records: " + e.getMessage(), e);
        }
        return records;
    }

    private ServiceRecord mapResultSetToRecord(ResultSet rs) throws SQLException {
        Date cDate = rs.getDate("completion_date");
        return new ServiceRecord(
                rs.getInt("record_id"),
                rs.getInt("booking_id"),
                cDate != null ? cDate.toLocalDate() : null,
                rs.getString("remarks"),
                rs.getBigDecimal("total_service_cost")
        );
    }
}
