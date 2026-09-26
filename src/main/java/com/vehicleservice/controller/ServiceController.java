package com.vehicleservice.controller;

import com.vehicleservice.model.Service;
import com.vehicleservice.service.ServiceManagementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/services")
public class ServiceController {

    private final ServiceManagementService serviceManagementService;

    public ServiceController(ServiceManagementService serviceManagementService) {
        this.serviceManagementService = serviceManagementService;
    }

    @GetMapping
    public ResponseEntity<List<Service>> getAllServices() {
        return ResponseEntity.ok(serviceManagementService.getAllServices());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Service> getServiceById(@PathVariable("id") int id) {
        Service service = serviceManagementService.getServiceById(id);
        return ResponseEntity.ok(service);
    }

    @PostMapping
    public ResponseEntity<Service> createService(@RequestBody Service service) {
        Service created = serviceManagementService.addService(
                service.getServiceName(),
                service.getBasePrice(),
                service.getDescription()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Service> updateService(@PathVariable("id") int id, @RequestBody Service service) {
        service.setServiceId(id);
        serviceManagementService.updateService(service);
        Service updated = serviceManagementService.getServiceById(id);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteService(@PathVariable("id") int id) {
        serviceManagementService.deleteService(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException ex) {
        Map<String, Object> error = new LinkedHashMap<>();
        error.put("success", false);
        
        // Since ServiceManagementService throws IllegalArgumentException for both 
        // Validation errors and Not Found errors, we must map them correctly.
        if (ex.getMessage() != null && ex.getMessage().contains("not found with ID")) {
            error.put("status", HttpStatus.NOT_FOUND.value());
            error.put("message", ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        } else {
            error.put("status", HttpStatus.BAD_REQUEST.value());
            error.put("message", ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }
}
