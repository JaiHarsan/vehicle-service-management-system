package com.vehicleservice.repository;

import com.vehicleservice.model.Customer;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryCustomerRepository implements CustomerRepository {
    private final List<Customer> customers = new ArrayList<>();
    private final AtomicInteger idGenerator = new AtomicInteger(1);

    @Override
    public Customer save(Customer customer) {
        if (customer.getCustomerId() <= 0) {
            customer.setCustomerId(idGenerator.getAndIncrement());
        }
        customers.add(customer);
        return customer;
    }

    @Override
    public Optional<Customer> findById(int customerId) {
        return customers.stream()
                .filter(c -> c.getCustomerId() == customerId)
                .findFirst();
    }

    @Override
    public List<Customer> findAll() {
        return new ArrayList<>(customers);
    }

    @Override
    public boolean update(Customer customer) {
        for (int i = 0; i < customers.size(); i++) {
            if (customers.get(i).getCustomerId() == customer.getCustomerId()) {
                customers.set(i, customer);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean delete(int customerId) {
        return customers.removeIf(c -> c.getCustomerId() == customerId);
    }
}
