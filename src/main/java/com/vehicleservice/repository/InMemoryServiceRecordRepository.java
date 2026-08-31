package com.vehicleservice.repository;

import com.vehicleservice.model.ServiceRecord;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryServiceRecordRepository implements ServiceRecordRepository {
    private final List<ServiceRecord> records = new ArrayList<>();
    private final AtomicInteger idGenerator = new AtomicInteger(501);

    @Override
    public ServiceRecord save(ServiceRecord record) {
        if (record.getRecordId() <= 0) {
            record.setRecordId(idGenerator.getAndIncrement());
        }
        records.add(record);
        return record;
    }

    @Override
    public Optional<ServiceRecord> findById(int recordId) {
        return records.stream()
                .filter(r -> r.getRecordId() == recordId)
                .findFirst();
    }

    @Override
    public Optional<ServiceRecord> findByBookingId(int bookingId) {
        return records.stream()
                .filter(r -> r.getBookingId() == bookingId)
                .findFirst();
    }

    @Override
    public List<ServiceRecord> findAll() {
        return new ArrayList<>(records);
    }
}
