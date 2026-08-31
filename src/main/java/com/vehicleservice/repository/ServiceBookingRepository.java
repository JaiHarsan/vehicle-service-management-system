package com.vehicleservice.repository;

import com.vehicleservice.model.ServiceBooking;
import java.util.List;
import java.util.Optional;

public interface ServiceBookingRepository {
    ServiceBooking save(ServiceBooking booking);
    Optional<ServiceBooking> findById(int bookingId);
    List<ServiceBooking> findByVehicleId(int vehicleId);
    List<ServiceBooking> findAll();
    boolean update(ServiceBooking booking);
    boolean delete(int bookingId);
}
