package com.vehicleservice.repository;

import com.vehicleservice.model.ServiceBooking;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class InMemoryServiceBookingRepository implements ServiceBookingRepository {
    private final List<ServiceBooking> bookings = new ArrayList<>();
    private final AtomicInteger idGenerator = new AtomicInteger(401);

    @Override
    public ServiceBooking save(ServiceBooking booking) {
        if (booking.getBookingId() <= 0) {
            booking.setBookingId(idGenerator.getAndIncrement());
        }
        bookings.add(booking);
        return booking;
    }

    @Override
    public Optional<ServiceBooking> findById(int bookingId) {
        return bookings.stream()
                .filter(b -> b.getBookingId() == bookingId)
                .findFirst();
    }

    @Override
    public List<ServiceBooking> findByVehicleId(int vehicleId) {
        return bookings.stream()
                .filter(b -> b.getVehicleId() == vehicleId)
                .collect(Collectors.toList());
    }

    @Override
    public List<ServiceBooking> findAll() {
        return new ArrayList<>(bookings);
    }

    @Override
    public boolean update(ServiceBooking booking) {
        for (int i = 0; i < bookings.size(); i++) {
            if (bookings.get(i).getBookingId() == booking.getBookingId()) {
                bookings.set(i, booking);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean delete(int bookingId) {
        return bookings.removeIf(b -> b.getBookingId() == bookingId);
    }
}
