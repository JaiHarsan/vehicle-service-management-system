package com.vehicleservice.repository;

import com.vehicleservice.model.Mechanic;
import java.util.List;
import java.util.Optional;

public interface MechanicRepository {
    Mechanic save(Mechanic mechanic);
    Optional<Mechanic> findById(int mechanicId);
    List<Mechanic> findAll();
    boolean update(Mechanic mechanic);
    boolean delete(int mechanicId);
}
