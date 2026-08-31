package com.vehicleservice.repository;

import com.vehicleservice.model.Service;
import java.util.List;
import java.util.Optional;

public interface ServiceRepository {
    Service save(Service service);
    Optional<Service> findById(int serviceId);
    List<Service> findAll();
    boolean update(Service service);
    boolean delete(int serviceId);
}
