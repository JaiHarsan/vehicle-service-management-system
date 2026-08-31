package com.vehicleservice.repository;

import com.vehicleservice.model.Mechanic;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryMechanicRepository implements MechanicRepository {
    private final List<Mechanic> mechanics = new ArrayList<>();
    private final AtomicInteger idGenerator = new AtomicInteger(201);

    @Override
    public Mechanic save(Mechanic mechanic) {
        if (mechanic.getMechanicId() <= 0) {
            mechanic.setMechanicId(idGenerator.getAndIncrement());
        }
        mechanics.add(mechanic);
        return mechanic;
    }

    @Override
    public Optional<Mechanic> findById(int mechanicId) {
        return mechanics.stream()
                .filter(m -> m.getMechanicId() == mechanicId)
                .findFirst();
    }

    @Override
    public List<Mechanic> findAll() {
        return new ArrayList<>(mechanics);
    }

    @Override
    public boolean update(Mechanic mechanic) {
        for (int i = 0; i < mechanics.size(); i++) {
            if (mechanics.get(i).getMechanicId() == mechanic.getMechanicId()) {
                mechanics.set(i, mechanic);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean delete(int mechanicId) {
        return mechanics.removeIf(m -> m.getMechanicId() == mechanicId);
    }
}
