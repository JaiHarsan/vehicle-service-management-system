package com.vehicleservice.repository;

import com.vehicleservice.model.Customer;
import java.util.List;
import java.util.Optional;

public interface CustomerRepository {
    Customer save(Customer customer);
    Optional<Customer> findById(int customerId);
    List<Customer> findAll();
    boolean update(Customer customer);
    boolean delete(int customerId);
}
