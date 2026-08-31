package com.vehicleservice.repository;

import com.vehicleservice.model.ServiceDetail;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class InMemoryServiceDetailRepository implements ServiceDetailRepository {
    private final List<ServiceDetail> details = new ArrayList<>();
    private final AtomicInteger idGenerator = new AtomicInteger(601);

    @Override
    public ServiceDetail save(ServiceDetail detail) {
        if (detail.getDetailId() <= 0) {
            detail.setDetailId(idGenerator.getAndIncrement());
        }
        details.add(detail);
        return detail;
    }

    @Override
    public List<ServiceDetail> findByRecordId(int recordId) {
        return details.stream()
                .filter(d -> d.getRecordId() == recordId)
                .collect(Collectors.toList());
    }

    @Override
    public List<ServiceDetail> findAll() {
        return new ArrayList<>(details);
    }
}
