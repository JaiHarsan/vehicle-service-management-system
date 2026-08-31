package com.vehicleservice.service;

import com.vehicleservice.exception.MechanicNotFoundException;
import com.vehicleservice.model.Availability;
import com.vehicleservice.model.Mechanic;
import com.vehicleservice.model.Specialization;
import com.vehicleservice.repository.MechanicRepository;
import com.vehicleservice.util.InputValidator;

import java.util.List;

public class MechanicService {
    private final MechanicRepository mechanicRepository;

    public MechanicService(MechanicRepository mechanicRepository) {
        this.mechanicRepository = mechanicRepository;
    }

    public Mechanic addMechanic(String name, String phone, Specialization specialization) {
        if (!InputValidator.isValidName(name)) {
            throw new IllegalArgumentException("Mechanic name cannot be empty and must be at least 2 characters.");
        }
        if (!InputValidator.isValidPhone(phone)) {
            throw new IllegalArgumentException("Invalid phone number format for mechanic.");
        }

        Mechanic mechanic = new Mechanic(name, phone, specialization != null ? specialization : Specialization.GENERAL);
        return mechanicRepository.save(mechanic);
    }

    public List<Mechanic> getAllMechanics() {
        return mechanicRepository.findAll();
    }

    public Mechanic getMechanicById(int mechanicId) {
        return mechanicRepository.findById(mechanicId)
                .orElseThrow(() -> new MechanicNotFoundException("Mechanic not found with ID: " + mechanicId));
    }

    public boolean updateMechanic(Mechanic mechanic) {
        getMechanicById(mechanic.getMechanicId()); // Validate existence
        return mechanicRepository.update(mechanic);
    }

    public boolean updateAvailability(int mechanicId, Availability availability) {
        Mechanic mechanic = getMechanicById(mechanicId);
        mechanic.setAvailability(availability);
        return mechanicRepository.update(mechanic);
    }

    public boolean deleteMechanic(int mechanicId) {
        getMechanicById(mechanicId); // Validate existence
        return mechanicRepository.delete(mechanicId);
    }
}
