package com.vehicleservice.controller;

import com.vehicleservice.exception.MechanicNotFoundException;
import com.vehicleservice.model.Availability;
import com.vehicleservice.model.Mechanic;
import com.vehicleservice.service.MechanicService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/mechanics")
public class MechanicController {

    private final MechanicService mechanicService;

    public MechanicController(MechanicService mechanicService) {
        this.mechanicService = mechanicService;
    }

    @GetMapping
    public ResponseEntity<List<Mechanic>> getAllMechanics() {
        return ResponseEntity.ok(mechanicService.getAllMechanics());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Mechanic> getMechanicById(@PathVariable("id") int id) {
        Mechanic mechanic = mechanicService.getMechanicById(id);
        return ResponseEntity.ok(mechanic);
    }

    @PostMapping
    public ResponseEntity<Mechanic> createMechanic(@RequestBody Mechanic mechanic) {
        Mechanic created = mechanicService.addMechanic(
                mechanic.getName(),
                mechanic.getPhone(),
                mechanic.getSpecialization()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Mechanic> updateMechanic(@PathVariable("id") int id, @RequestBody Mechanic mechanic) {
        mechanic.setMechanicId(id);
        mechanicService.updateMechanic(mechanic);
        Mechanic updated = mechanicService.getMechanicById(id);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{id}/availability")
    public ResponseEntity<Mechanic> updateAvailability(@PathVariable("id") int id, @RequestParam("status") Availability availability) {
        mechanicService.updateAvailability(id, availability);
        Mechanic updated = mechanicService.getMechanicById(id);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMechanic(@PathVariable("id") int id) {
        mechanicService.deleteMechanic(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(MechanicNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleMechanicNotFound(MechanicNotFoundException ex) {
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
