package com.vehicleservice.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ServiceRecord {
    private int recordId;
    private int bookingId;
    private LocalDate completionDate;
    private String remarks;
    private BigDecimal totalServiceCost;

    public ServiceRecord() {
        this.completionDate = LocalDate.now();
        this.totalServiceCost = BigDecimal.ZERO;
    }

    public ServiceRecord(int recordId, int bookingId, LocalDate completionDate, String remarks, BigDecimal totalServiceCost) {
        this.recordId = recordId;
        this.bookingId = bookingId;
        this.completionDate = completionDate != null ? completionDate : LocalDate.now();
        this.remarks = remarks;
        this.totalServiceCost = totalServiceCost != null ? totalServiceCost : BigDecimal.ZERO;
    }

    public ServiceRecord(int bookingId, LocalDate completionDate, String remarks, BigDecimal totalServiceCost) {
        this.bookingId = bookingId;
        this.completionDate = completionDate != null ? completionDate : LocalDate.now();
        this.remarks = remarks;
        this.totalServiceCost = totalServiceCost != null ? totalServiceCost : BigDecimal.ZERO;
    }

    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public LocalDate getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(LocalDate completionDate) {
        this.completionDate = completionDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public BigDecimal getTotalServiceCost() {
        return totalServiceCost;
    }

    public void setTotalServiceCost(BigDecimal totalServiceCost) {
        this.totalServiceCost = totalServiceCost;
    }

    @Override
    public String toString() {
        return "ServiceRecord{" +
                "recordId=" + recordId +
                ", bookingId=" + bookingId +
                ", completionDate=" + completionDate +
                ", remarks='" + remarks + '\'' +
                ", totalServiceCost=" + totalServiceCost +
                '}';
    }
}
