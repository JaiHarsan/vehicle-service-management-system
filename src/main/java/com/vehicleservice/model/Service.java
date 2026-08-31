package com.vehicleservice.model;

import java.math.BigDecimal;

public class Service {
    private int serviceId;
    private String serviceName;
    private BigDecimal basePrice;
    private String description;

    public Service() {
    }

    public Service(int serviceId, String serviceName, BigDecimal basePrice, String description) {
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.basePrice = basePrice;
        this.description = description;
    }

    public Service(String serviceName, BigDecimal basePrice, String description) {
        this.serviceName = serviceName;
        this.basePrice = basePrice;
        this.description = description;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(BigDecimal basePrice) {
        this.basePrice = basePrice;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Service{" +
                "serviceId=" + serviceId +
                ", serviceName='" + serviceName + '\'' +
                ", basePrice=" + basePrice +
                ", description='" + description + '\'' +
                '}';
    }
}
