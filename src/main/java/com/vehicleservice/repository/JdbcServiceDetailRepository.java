package com.vehicleservice.repository;

import com.vehicleservice.config.DatabaseConnection;
import com.vehicleservice.model.ServiceDetail;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcServiceDetailRepository implements ServiceDetailRepository {

    @Override
    public ServiceDetail save(ServiceDetail detail) {
        String sql = "INSERT INTO service_details (record_id, service_id, quantity, unit_price, subtotal) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, detail.getRecordId());
            stmt.setInt(2, detail.getServiceId());
            stmt.setInt(3, detail.getQuantity());
            stmt.setBigDecimal(4, detail.getUnitPrice());
            stmt.setBigDecimal(5, detail.getSubtotal());

            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    detail.setDetailId(generatedKeys.getInt(1));
                }
            }
            return detail;
        } catch (SQLException e) {
            throw new RuntimeException("Error saving service detail line item: " + e.getMessage(), e);
        }
    }

    @Override
    public List<ServiceDetail> findByRecordId(int recordId) {
        List<ServiceDetail> details = new ArrayList<>();
        String sql = "SELECT * FROM service_details WHERE record_id = ? ORDER BY detail_id ASC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, recordId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    details.add(mapResultSetToDetail(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching service details by record ID: " + e.getMessage(), e);
        }
        return details;
    }

    @Override
    public List<ServiceDetail> findAll() {
        List<ServiceDetail> details = new ArrayList<>();
        String sql = "SELECT * FROM service_details ORDER BY detail_id ASC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                details.add(mapResultSetToDetail(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching all service details: " + e.getMessage(), e);
        }
        return details;
    }

    private ServiceDetail mapResultSetToDetail(ResultSet rs) throws SQLException {
        ServiceDetail detail = new ServiceDetail();
        detail.setDetailId(rs.getInt("detail_id"));
        detail.setRecordId(rs.getInt("record_id"));
        detail.setServiceId(rs.getInt("service_id"));
        detail.setQuantity(rs.getInt("quantity"));
        detail.setUnitPrice(rs.getBigDecimal("unit_price"));
        detail.setSubtotal(rs.getBigDecimal("subtotal"));
        return detail;
    }
}
