package com.vehicleservice.model;

import java.math.BigDecimal;

public class Bill {
    private int billId;
    private int recordId;
    private BigDecimal serviceCost;
    private BigDecimal partsCost;
    private BigDecimal laborCost;
    private BigDecimal tax;
    private BigDecimal discount;
    private BigDecimal totalAmount;
    private PaymentStatus paymentStatus;

    public Bill() {
        this.serviceCost = BigDecimal.ZERO;
        this.partsCost = BigDecimal.ZERO;
        this.laborCost = BigDecimal.ZERO;
        this.tax = BigDecimal.ZERO;
        this.discount = BigDecimal.ZERO;
        this.totalAmount = BigDecimal.ZERO;
        this.paymentStatus = PaymentStatus.PENDING;
    }

    public Bill(int billId, int recordId, BigDecimal serviceCost, BigDecimal partsCost, BigDecimal laborCost, BigDecimal tax, BigDecimal discount, PaymentStatus paymentStatus) {
        this.billId = billId;
        this.recordId = recordId;
        this.serviceCost = serviceCost != null ? serviceCost : BigDecimal.ZERO;
        this.partsCost = partsCost != null ? partsCost : BigDecimal.ZERO;
        this.laborCost = laborCost != null ? laborCost : BigDecimal.ZERO;
        this.tax = tax != null ? tax : BigDecimal.ZERO;
        this.discount = discount != null ? discount : BigDecimal.ZERO;
        this.paymentStatus = paymentStatus != null ? paymentStatus : PaymentStatus.PENDING;
        calculateTotal();
    }

    // Overloaded constructor taking a percentage tax rate (e.g., 0.18 for 18% GST)
    public Bill(int recordId, BigDecimal serviceCost, BigDecimal partsCost, BigDecimal laborCost, BigDecimal taxRate, BigDecimal discount) {
        this.recordId = recordId;
        this.serviceCost = serviceCost != null ? serviceCost : BigDecimal.ZERO;
        this.partsCost = partsCost != null ? partsCost : BigDecimal.ZERO;
        this.laborCost = laborCost != null ? laborCost : BigDecimal.ZERO;
        this.discount = discount != null ? discount : BigDecimal.ZERO;
        
        BigDecimal subtotal = this.serviceCost.add(this.partsCost).add(this.laborCost);
        BigDecimal rate = taxRate != null ? taxRate : BigDecimal.ZERO;
        this.tax = subtotal.multiply(rate);
        this.paymentStatus = PaymentStatus.PENDING;
        calculateTotal();
    }

    public void calculateTotal() {
        BigDecimal subtotal = this.serviceCost.add(this.partsCost).add(this.laborCost);
        this.totalAmount = subtotal.add(this.tax).subtract(this.discount);
    }

    public int getBillId() {
        return billId;
    }

    public void setBillId(int billId) {
        this.billId = billId;
    }

    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public BigDecimal getServiceCost() {
        return serviceCost;
    }

    public void setServiceCost(BigDecimal serviceCost) {
        this.serviceCost = serviceCost;
        calculateTotal();
    }

    public BigDecimal getPartsCost() {
        return partsCost;
    }

    public void setPartsCost(BigDecimal partsCost) {
        this.partsCost = partsCost;
        calculateTotal();
    }

    public BigDecimal getLaborCost() {
        return laborCost;
    }

    public void setLaborCost(BigDecimal laborCost) {
        this.laborCost = laborCost;
        calculateTotal();
    }

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
        calculateTotal();
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
        calculateTotal();
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    @Override
    public String toString() {
        return "Bill{" +
                "billId=" + billId +
                ", recordId=" + recordId +
                ", serviceCost=" + serviceCost +
                ", partsCost=" + partsCost +
                ", laborCost=" + laborCost +
                ", taxAmount=" + tax +
                ", discount=" + discount +
                ", totalAmount=" + totalAmount +
                ", paymentStatus=" + paymentStatus +
                '}';
    }
}
