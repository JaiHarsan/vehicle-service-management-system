package com.vehicleservice.exception;

public class MechanicNotFoundException extends RuntimeException {
    public MechanicNotFoundException(String message) {
        super(message);
    }
}
