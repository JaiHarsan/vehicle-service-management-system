package com.vehicleservice.service;

import com.vehicleservice.exception.CustomerNotFoundException;
import com.vehicleservice.model.Customer;
import com.vehicleservice.repository.CustomerRepository;
import com.vehicleservice.util.InputValidator;

import java.util.List;

public class CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer addCustomer(String name, String phone, String email) {
        if (!InputValidator.isValidName(name)) {
            throw new IllegalArgumentException("Customer name cannot be empty and must be at least 2 characters.");
        }
        if (!InputValidator.isValidPhone(phone)) {
            throw new IllegalArgumentException("Invalid phone number. Must be a 10-digit number.");
        }
        if (!InputValidator.isValidEmail(email)) {
            throw new IllegalArgumentException("Invalid email format.");
        }

        Customer customer = new Customer(name, phone, email);
        return customerRepository.save(customer);
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Customer getCustomerById(int customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with ID: " + customerId));
    }

    public boolean updateCustomer(Customer customer) {
        getCustomerById(customer.getCustomerId()); // Validate existence
        if (!InputValidator.isValidName(customer.getName()) || !InputValidator.isValidPhone(customer.getPhone())) {
            throw new IllegalArgumentException("Invalid customer details for update.");
        }
        return customerRepository.update(customer);
    }

    public boolean deleteCustomer(int customerId) {
        getCustomerById(customerId); // Validate existence
        return customerRepository.delete(customerId);
    }
}
