package com.vehicleservice.repository;

import com.vehicleservice.model.Bill;
import java.util.List;
import java.util.Optional;

public interface BillRepository {
    Bill save(Bill bill);
    Optional<Bill> findById(int billId);
    Optional<Bill> findByRecordId(int recordId);
    List<Bill> findAll();
    boolean update(Bill bill);
}
