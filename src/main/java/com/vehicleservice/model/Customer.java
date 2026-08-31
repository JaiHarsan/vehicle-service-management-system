package com.vehicleservice.model;

public class Customer extends Person {
    private int customerId;
    private String email;

    public Customer() {
        super();
    }

    public Customer(int customerId, String name, String phone, String email) {
        super(name, phone);
        this.customerId = customerId;
        this.email = email;
    }

    public Customer(String name, String phone, String email) {
        super(name, phone);
        this.email = email;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "customerId=" + customerId +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
