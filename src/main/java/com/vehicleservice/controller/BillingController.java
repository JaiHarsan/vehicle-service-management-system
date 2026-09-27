package com.vehicleservice.controller;

import com.vehicleservice.model.Bill;
import com.vehicleservice.service.BillingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bills")
public class BillingController {

    private final BillingService billingService;

    public BillingController(BillingService billingService) {
        this.billingService = billingService;
    }

    @GetMapping
    public ResponseEntity<List<Bill>> getAllBills() {
        return ResponseEntity.ok(billingService.getAllBills());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Bill> getBillById(@PathVariable("id") int id) {
        return ResponseEntity.ok(billingService.getBillById(id));
    }

    @GetMapping("/record/{recordId}")
    public ResponseEntity<Bill> getBillByRecordId(@PathVariable("recordId") int recordId) {
        return ResponseEntity.ok(billingService.getBillByRecordId(recordId));
    }

    @PostMapping
    public ResponseEntity<Bill> generateBill(@RequestBody GenerateBillRequest request) {
        Bill created = billingService.generateBill(
                request.getRecordId(),
                request.getPartsCost(),
                request.getLaborCost(),
                request.getTaxRate(),
                request.getDiscount()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}/pay")
    public ResponseEntity<Void> markBillAsPaid(@PathVariable("id") int id) {
        billingService.markBillAsPaid(id);
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException ex) {
        String msg = ex.getMessage();
        HttpStatus status = HttpStatus.BAD_REQUEST;
        
        if (msg != null) {
            if (msg.contains("not found")) {
                status = HttpStatus.NOT_FOUND;
            } else if (msg.contains("already exists")) {
                status = HttpStatus.CONFLICT;
            }
        }
        
        Map<String, Object> error = new LinkedHashMap<>();
        error.put("success", false);
        error.put("status", status.value());
        error.put("message", msg);
        return ResponseEntity.status(status).body(error);
    }

    public static class GenerateBillRequest {
        private int recordId;
        private BigDecimal partsCost;
        private BigDecimal laborCost;
        private BigDecimal taxRate;
        private BigDecimal discount;

        public int getRecordId() {
            return recordId;
        }

        public void setRecordId(int recordId) {
            this.recordId = recordId;
        }

        public BigDecimal getPartsCost() {
            return partsCost;
        }

        public void setPartsCost(BigDecimal partsCost) {
            this.partsCost = partsCost;
        }

        public BigDecimal getLaborCost() {
            return laborCost;
        }

        public void setLaborCost(BigDecimal laborCost) {
            this.laborCost = laborCost;
        }

        public BigDecimal getTaxRate() {
            return taxRate;
        }

        public void setTaxRate(BigDecimal taxRate) {
            this.taxRate = taxRate;
        }

        public BigDecimal getDiscount() {
            return discount;
        }

        public void setDiscount(BigDecimal discount) {
            this.discount = discount;
        }
    }
}
