package com.vehicleservice.repository;

import com.vehicleservice.config.DatabaseConnection;
import com.vehicleservice.model.Bill;
import com.vehicleservice.model.PaymentStatus;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcBillRepository implements BillRepository {

    @Override
    public Bill save(Bill bill) {
        String sql = "INSERT INTO bills (record_id, service_cost, parts_cost, labor_cost, tax, discount, total_amount, payment_status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, bill.getRecordId());
            stmt.setBigDecimal(2, bill.getServiceCost());
            stmt.setBigDecimal(3, bill.getPartsCost());
            stmt.setBigDecimal(4, bill.getLaborCost());
            stmt.setBigDecimal(5, bill.getTax());
            stmt.setBigDecimal(6, bill.getDiscount());
            stmt.setBigDecimal(7, bill.getTotalAmount());
            stmt.setString(8, bill.getPaymentStatus().name());

            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    bill.setBillId(generatedKeys.getInt(1));
                }
            }
            return bill;
        } catch (SQLException e) {
            throw new RuntimeException("Error saving bill to database: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Bill> findById(int billId) {
        String sql = "SELECT * FROM bills WHERE bill_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, billId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToBill(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching bill by ID: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Bill> findByRecordId(int recordId) {
        String sql = "SELECT * FROM bills WHERE record_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, recordId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToBill(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching bill by record ID: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<Bill> findAll() {
        List<Bill> bills = new ArrayList<>();
        String sql = "SELECT * FROM bills ORDER BY bill_id ASC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                bills.add(mapResultSetToBill(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching all bills: " + e.getMessage(), e);
        }
        return bills;
    }

    @Override
    public boolean update(Bill bill) {
        String sql = "UPDATE bills SET record_id = ?, service_cost = ?, parts_cost = ?, labor_cost = ?, tax = ?, discount = ?, total_amount = ?, payment_status = ? WHERE bill_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, bill.getRecordId());
            stmt.setBigDecimal(2, bill.getServiceCost());
            stmt.setBigDecimal(3, bill.getPartsCost());
            stmt.setBigDecimal(4, bill.getLaborCost());
            stmt.setBigDecimal(5, bill.getTax());
            stmt.setBigDecimal(6, bill.getDiscount());
            stmt.setBigDecimal(7, bill.getTotalAmount());
            stmt.setString(8, bill.getPaymentStatus().name());
            stmt.setInt(9, bill.getBillId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error updating bill: " + e.getMessage(), e);
        }
    }

    private Bill mapResultSetToBill(ResultSet rs) throws SQLException {
        PaymentStatus status = PaymentStatus.PENDING;
        try {
            status = PaymentStatus.valueOf(rs.getString("payment_status"));
        } catch (Exception ignored) {}

        return new Bill(
                rs.getInt("bill_id"),
                rs.getInt("record_id"),
                rs.getBigDecimal("service_cost"),
                rs.getBigDecimal("parts_cost"),
                rs.getBigDecimal("labor_cost"),
                rs.getBigDecimal("tax"),
                rs.getBigDecimal("discount"),
                status
        );
    }
}
