package com.vehicleservice.repository;

import com.vehicleservice.model.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryServiceRepository implements ServiceRepository {
    private final List<Service> services = new ArrayList<>();
    private final AtomicInteger idGenerator = new AtomicInteger(301);

    @Override
    public Service save(Service service) {
        if (service.getServiceId() <= 0) {
            service.setServiceId(idGenerator.getAndIncrement());
        }
        services.add(service);
        return service;
    }

    @Override
    public Optional<Service> findById(int serviceId) {
        return services.stream()
                .filter(s -> s.getServiceId() == serviceId)
                .findFirst();
    }

    @Override
    public List<Service> findAll() {
        return new ArrayList<>(services);
    }

    @Override
    public boolean update(Service service) {
        for (int i = 0; i < services.size(); i++) {
            if (services.get(i).getServiceId() == service.getServiceId()) {
                services.set(i, service);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean delete(int serviceId) {
        return services.removeIf(s -> s.getServiceId() == serviceId);
    }
}
