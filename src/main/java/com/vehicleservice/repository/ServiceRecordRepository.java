package com.vehicleservice.repository;

import com.vehicleservice.model.ServiceRecord;
import java.util.List;
import java.util.Optional;

public interface ServiceRecordRepository {
    ServiceRecord save(ServiceRecord record);
    Optional<ServiceRecord> findById(int recordId);
    Optional<ServiceRecord> findByBookingId(int bookingId);
    List<ServiceRecord> findAll();
}
