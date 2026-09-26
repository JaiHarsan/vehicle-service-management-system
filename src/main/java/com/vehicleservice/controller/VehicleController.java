package com.vehicleservice.controller;

import com.vehicleservice.exception.CustomerNotFoundException;
import com.vehicleservice.exception.DuplicateVehicleException;
import com.vehicleservice.exception.VehicleNotFoundException;
import com.vehicleservice.model.Vehicle;
import com.vehicleservice.service.VehicleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {

    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @GetMapping
    public ResponseEntity<List<Vehicle>> getAllVehicles(
            @RequestParam(name = "customerId", required = false) Integer customerId,
            @RequestParam(name = "registrationNumber", required = false) String registrationNumber) {
        if (customerId != null) {
            return ResponseEntity.ok(vehicleService.getVehiclesByCustomerId(customerId));
        }
        if (registrationNumber != null && !registrationNumber.trim().isEmpty()) {
            Vehicle vehicle = vehicleService.getVehicleByRegistrationNumber(registrationNumber.trim());
            return ResponseEntity.ok(List.of(vehicle));
        }
        return ResponseEntity.ok(vehicleService.getAllVehicles());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Vehicle> getVehicleById(@PathVariable("id") int id) {
        Vehicle vehicle = vehicleService.getVehicleById(id);
        return ResponseEntity.ok(vehicle);
    }

    @PostMapping
    public ResponseEntity<Vehicle> createVehicle(@RequestBody Vehicle vehicle) {
        Vehicle created = vehicleService.addVehicle(
                vehicle.getRegistrationNumber(),
                vehicle.getBrand(),
                vehicle.getModel(),
                vehicle.getVehicleType(),
                vehicle.getCustomerId()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Vehicle> updateVehicle(@PathVariable("id") int id, @RequestBody Vehicle vehicle) {
        vehicle.setVehicleId(id);
        vehicleService.updateVehicle(vehicle);
        Vehicle updated = vehicleService.getVehicleById(id);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVehicle(@PathVariable("id") int id) {
        vehicleService.deleteVehicle(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(VehicleNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleVehicleNotFound(VehicleNotFoundException ex) {
        Map<String, Object> error = new LinkedHashMap<>();
        error.put("success", false);
        error.put("status", HttpStatus.NOT_FOUND.value());
        error.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(DuplicateVehicleException.class)
    public ResponseEntity<Map<String, Object>> handleDuplicateVehicle(DuplicateVehicleException ex) {
        Map<String, Object> error = new LinkedHashMap<>();
        error.put("success", false);
        error.put("status", HttpStatus.CONFLICT.value());
        error.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleCustomerNotFound(CustomerNotFoundException ex) {
        Map<String, Object> error = new LinkedHashMap<>();
        error.put("success", false);
        error.put("status", HttpStatus.NOT_FOUND.value());
        error.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException ex) {
        Map<String, Object> error = new LinkedHashMap<>();
        error.put("success", false);
        error.put("status", HttpStatus.BAD_REQUEST.value());
        error.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}
