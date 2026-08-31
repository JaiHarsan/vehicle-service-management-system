package com.vehicleservice.repository;

import com.vehicleservice.model.Bill;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryBillRepository implements BillRepository {
    private final List<Bill> bills = new ArrayList<>();
    private final AtomicInteger idGenerator = new AtomicInteger(701);

    @Override
    public Bill save(Bill bill) {
        if (bill.getBillId() <= 0) {
            bill.setBillId(idGenerator.getAndIncrement());
        }
        bills.add(bill);
        return bill;
    }

    @Override
    public Optional<Bill> findById(int billId) {
        return bills.stream()
                .filter(b -> b.getBillId() == billId)
                .findFirst();
    }

    @Override
    public Optional<Bill> findByRecordId(int recordId) {
        return bills.stream()
                .filter(b -> b.getRecordId() == recordId)
                .findFirst();
    }

    @Override
    public List<Bill> findAll() {
        return new ArrayList<>(bills);
    }

    @Override
    public boolean update(Bill bill) {
        for (int i = 0; i < bills.size(); i++) {
            if (bills.get(i).getBillId() == bill.getBillId()) {
                bills.set(i, bill);
                return true;
            }
        }
        return false;
    }
}
