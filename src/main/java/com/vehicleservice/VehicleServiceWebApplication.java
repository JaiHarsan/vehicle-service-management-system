package com.vehicleservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
    "com.vehicleservice.controller",
    "com.vehicleservice.service",
    "com.vehicleservice.repository",
    "com.vehicleservice.model",
    "com.vehicleservice.config"
})
public class VehicleServiceWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(VehicleServiceWebApplication.class, args);
    }
}
