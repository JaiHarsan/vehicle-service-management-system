package com.vehicleservice.model;

import java.time.LocalDate;

public class ServiceBooking {
    private int bookingId;
    private int vehicleId;
    private Integer mechanicId; // Integer allows null when mechanic is not yet assigned
    private int serviceId;
    private LocalDate serviceDate;
    private BookingStatus status;
    private String description;

    public ServiceBooking() {
        this.status = BookingStatus.BOOKED;
        this.serviceDate = LocalDate.now();
    }

    public ServiceBooking(int bookingId, int vehicleId, Integer mechanicId, int serviceId, LocalDate serviceDate, BookingStatus status, String description) {
        this.bookingId = bookingId;
        this.vehicleId = vehicleId;
        this.mechanicId = mechanicId;
        this.serviceId = serviceId;
        this.serviceDate = serviceDate != null ? serviceDate : LocalDate.now();
        this.status = status != null ? status : BookingStatus.BOOKED;
        this.description = description;
    }

    public ServiceBooking(int vehicleId, int serviceId, LocalDate serviceDate, String description) {
        this.vehicleId = vehicleId;
        this.serviceId = serviceId;
        this.serviceDate = serviceDate != null ? serviceDate : LocalDate.now();
        this.status = BookingStatus.BOOKED;
        this.description = description;
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public int getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }

    public Integer getMechanicId() {
        return mechanicId;
    }

    public void setMechanicId(Integer mechanicId) {
        this.mechanicId = mechanicId;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public LocalDate getServiceDate() {
        return serviceDate;
    }

    public void setServiceDate(LocalDate serviceDate) {
        this.serviceDate = serviceDate;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "ServiceBooking{" +
                "bookingId=" + bookingId +
                ", vehicleId=" + vehicleId +
                ", mechanicId=" + (mechanicId != null ? mechanicId : "Unassigned") +
                ", serviceId=" + serviceId +
                ", serviceDate=" + serviceDate +
                ", status=" + status +
                ", description='" + description + '\'' +
                '}';
    }
}
