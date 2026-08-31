package com.vehicleservice.service;

import com.vehicleservice.model.Bill;
import com.vehicleservice.model.PaymentStatus;
import com.vehicleservice.model.ServiceRecord;
import com.vehicleservice.repository.BillRepository;
import com.vehicleservice.repository.ServiceRecordRepository;
import com.vehicleservice.util.InputValidator;

import java.math.BigDecimal;
import java.util.List;

public class BillingService {
    private final BillRepository billRepository;
    private final ServiceRecordRepository recordRepository;

    public BillingService(BillRepository billRepository, ServiceRecordRepository recordRepository) {
        this.billRepository = billRepository;
        this.recordRepository = recordRepository;
    }

    public Bill generateBill(int recordId, BigDecimal partsCost, BigDecimal laborCost, BigDecimal taxRate, BigDecimal discount) {
        ServiceRecord record = recordRepository.findById(recordId)
                .orElseThrow(() -> new IllegalArgumentException("Cannot generate bill. Service record not found with ID: " + recordId));

        if (billRepository.findByRecordId(recordId).isPresent()) {
            throw new IllegalArgumentException("Bill already exists for Service Record ID: " + recordId);
        }

        BigDecimal pCost = partsCost != null ? partsCost : BigDecimal.ZERO;
        BigDecimal lCost = laborCost != null ? laborCost : BigDecimal.ZERO;
        BigDecimal tRate = taxRate != null ? taxRate : new BigDecimal("0.18"); // Default 18% tax
        BigDecimal disc = discount != null ? discount : BigDecimal.ZERO;

        if (!InputValidator.isValidPositiveAmount(pCost) || !InputValidator.isValidPositiveAmount(lCost) || !InputValidator.isValidPositiveAmount(disc)) {
            throw new IllegalArgumentException("Costs and discounts cannot be negative.");
        }

        Bill bill = new Bill(recordId, record.getTotalServiceCost(), pCost, lCost, tRate, disc);
        return billRepository.save(bill);
    }

    public List<Bill> getAllBills() {
        return billRepository.findAll();
    }

    public Bill getBillById(int billId) {
        return billRepository.findById(billId)
                .orElseThrow(() -> new IllegalArgumentException("Bill not found with ID: " + billId));
    }

    public Bill getBillByRecordId(int recordId) {
        return billRepository.findByRecordId(recordId)
                .orElseThrow(() -> new IllegalArgumentException("Bill not found for Service Record ID: " + recordId));
    }

    public boolean markBillAsPaid(int billId) {
        Bill bill = getBillById(billId);
        bill.setPaymentStatus(PaymentStatus.PAID);
        return billRepository.update(bill);
    }
}
