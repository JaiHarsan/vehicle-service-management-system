package com.vehicleservice.service;

import com.vehicleservice.model.Service;
import com.vehicleservice.repository.ServiceRepository;
import com.vehicleservice.util.InputValidator;

import java.math.BigDecimal;
import java.util.List;

public class ServiceManagementService {
    private final ServiceRepository serviceRepository;

    public ServiceManagementService(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    public Service addService(String serviceName, BigDecimal basePrice, String description) {
        if (!InputValidator.isValidName(serviceName)) {
            throw new IllegalArgumentException("Service name cannot be empty.");
        }
        if (!InputValidator.isValidPositiveAmount(basePrice)) {
            throw new IllegalArgumentException("Base price cannot be negative or null.");
        }

        Service service = new Service(serviceName, basePrice, description);
        return serviceRepository.save(service);
    }

    public List<Service> getAllServices() {
        return serviceRepository.findAll();
    }

    public Service getServiceById(int serviceId) {
        return serviceRepository.findById(serviceId)
                .orElseThrow(() -> new IllegalArgumentException("Service not found with ID: " + serviceId));
    }

    public boolean updateService(Service service) {
        getServiceById(service.getServiceId()); // Validate existence
        if (!InputValidator.isValidPositiveAmount(service.getBasePrice())) {
            throw new IllegalArgumentException("Base price cannot be negative.");
        }
        return serviceRepository.update(service);
    }

    public boolean deleteService(int serviceId) {
        getServiceById(serviceId); // Validate existence
        return serviceRepository.delete(serviceId);
    }
}
