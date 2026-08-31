package com.vehicleservice.repository;

import com.vehicleservice.config.DatabaseConnection;
import com.vehicleservice.model.BookingStatus;
import com.vehicleservice.model.ServiceBooking;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcServiceBookingRepository implements ServiceBookingRepository {

    @Override
    public ServiceBooking save(ServiceBooking booking) {
        String sql = "INSERT INTO service_bookings (vehicle_id, mechanic_id, service_id, service_date, status, description) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, booking.getVehicleId());
            if (booking.getMechanicId() != null) {
                stmt.setInt(2, booking.getMechanicId());
            } else {
                stmt.setNull(2, Types.INTEGER);
            }
            stmt.setInt(3, booking.getServiceId());
            stmt.setDate(4, Date.valueOf(booking.getServiceDate()));
            stmt.setString(5, booking.getStatus().name());
            stmt.setString(6, booking.getDescription());

            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    booking.setBookingId(generatedKeys.getInt(1));
                }
            }
            return booking;
        } catch (SQLException e) {
            throw new RuntimeException("Error saving booking to database: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<ServiceBooking> findById(int bookingId) {
        String sql = "SELECT * FROM service_bookings WHERE booking_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, bookingId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToBooking(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching booking by ID: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<ServiceBooking> findByVehicleId(int vehicleId) {
        List<ServiceBooking> bookings = new ArrayList<>();
        String sql = "SELECT * FROM service_bookings WHERE vehicle_id = ? ORDER BY booking_id ASC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, vehicleId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    bookings.add(mapResultSetToBooking(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching bookings by vehicle ID: " + e.getMessage(), e);
        }
        return bookings;
    }

    @Override
    public List<ServiceBooking> findAll() {
        List<ServiceBooking> bookings = new ArrayList<>();
        String sql = "SELECT * FROM service_bookings ORDER BY booking_id ASC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                bookings.add(mapResultSetToBooking(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching all bookings: " + e.getMessage(), e);
        }
        return bookings;
    }

    @Override
    public boolean update(ServiceBooking booking) {
        String sql = "UPDATE service_bookings SET vehicle_id = ?, mechanic_id = ?, service_id = ?, service_date = ?, status = ?, description = ? WHERE booking_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, booking.getVehicleId());
            if (booking.getMechanicId() != null) {
                stmt.setInt(2, booking.getMechanicId());
            } else {
                stmt.setNull(2, Types.INTEGER);
            }
            stmt.setInt(3, booking.getServiceId());
            stmt.setDate(4, Date.valueOf(booking.getServiceDate()));
            stmt.setString(5, booking.getStatus().name());
            stmt.setString(6, booking.getDescription());
            stmt.setInt(7, booking.getBookingId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error updating booking: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean delete(int bookingId) {
        String sql = "DELETE FROM service_bookings WHERE booking_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, bookingId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting booking: " + e.getMessage(), e);
        }
    }

    private ServiceBooking mapResultSetToBooking(ResultSet rs) throws SQLException {
        Integer mechanicId = rs.getInt("mechanic_id");
        if (rs.wasNull()) {
            mechanicId = null;
        }

        BookingStatus status = BookingStatus.BOOKED;
        try {
            status = BookingStatus.valueOf(rs.getString("status"));
        } catch (Exception ignored) {}

        Date sDate = rs.getDate("service_date");

        return new ServiceBooking(
                rs.getInt("booking_id"),
                rs.getInt("vehicle_id"),
                mechanicId,
                rs.getInt("service_id"),
                sDate != null ? sDate.toLocalDate() : null,
                status,
                rs.getString("description")
        );
    }
}
