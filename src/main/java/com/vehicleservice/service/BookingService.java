package com.vehicleservice.service;

import com.vehicleservice.exception.BookingNotFoundException;
import com.vehicleservice.exception.InvalidBookingException;
import com.vehicleservice.exception.MechanicNotFoundException;
import com.vehicleservice.exception.VehicleNotFoundException;
import com.vehicleservice.model.Availability;
import com.vehicleservice.model.BookingStatus;
import com.vehicleservice.model.Mechanic;
import com.vehicleservice.model.ServiceBooking;
import com.vehicleservice.repository.MechanicRepository;
import com.vehicleservice.repository.ServiceBookingRepository;
import com.vehicleservice.repository.ServiceRepository;
import com.vehicleservice.repository.VehicleRepository;

import java.time.LocalDate;
import java.util.List;

public class BookingService {
    private final ServiceBookingRepository bookingRepository;
    private final VehicleRepository vehicleRepository;
    private final ServiceRepository serviceRepository;
    private final MechanicRepository mechanicRepository;

    public BookingService(ServiceBookingRepository bookingRepository,
                          VehicleRepository vehicleRepository,
                          ServiceRepository serviceRepository,
                          MechanicRepository mechanicRepository) {
        this.bookingRepository = bookingRepository;
        this.vehicleRepository = vehicleRepository;
        this.serviceRepository = serviceRepository;
        this.mechanicRepository = mechanicRepository;
    }

    public ServiceBooking createBooking(int vehicleId, int serviceId, LocalDate serviceDate, String description) {
        // Validate vehicle existence
        vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new VehicleNotFoundException("Cannot create booking. Vehicle not found with ID: " + vehicleId));

        // Validate service existence
        serviceRepository.findById(serviceId)
                .orElseThrow(() -> new IllegalArgumentException("Cannot create booking. Service offering not found with ID: " + serviceId));

        LocalDate date = serviceDate != null ? serviceDate : LocalDate.now();
        ServiceBooking booking = new ServiceBooking(vehicleId, serviceId, date, description);
        return bookingRepository.save(booking);
    }

    public boolean assignMechanic(int bookingId, int mechanicId) {
        ServiceBooking booking = getBookingById(bookingId);

        if (booking.getStatus() != BookingStatus.BOOKED) {
            throw new InvalidBookingException("Cannot assign mechanic. Booking status must be BOOKED. Current status: " + booking.getStatus());
        }

        Mechanic mechanic = mechanicRepository.findById(mechanicId)
                .orElseThrow(() -> new MechanicNotFoundException("Mechanic not found with ID: " + mechanicId));

        if (mechanic.getAvailability() != Availability.AVAILABLE) {
            throw new InvalidBookingException("Mechanic '" + mechanic.getName() + "' is currently " + mechanic.getAvailability() + ".");
        }

        // Update booking status
        booking.setMechanicId(mechanicId);
        booking.setStatus(BookingStatus.ASSIGNED);
        bookingRepository.update(booking);

        // Update mechanic availability to BUSY
        mechanic.setAvailability(Availability.BUSY);
        mechanicRepository.update(mechanic);

        return true;
    }

    public boolean startService(int bookingId) {
        ServiceBooking booking = getBookingById(bookingId);

        if (booking.getStatus() != BookingStatus.ASSIGNED) {
            throw new InvalidBookingException("Cannot start service. Booking status must be ASSIGNED. Current status: " + booking.getStatus());
        }

        booking.setStatus(BookingStatus.IN_PROGRESS);
        return bookingRepository.update(booking);
    }

    public boolean completeBooking(int bookingId) {
        ServiceBooking booking = getBookingById(bookingId);

        if (booking.getStatus() != BookingStatus.IN_PROGRESS) {
            throw new InvalidBookingException("Cannot complete booking. Booking status must be IN_PROGRESS. Current status: " + booking.getStatus());
        }

        booking.setStatus(BookingStatus.COMPLETED);
        bookingRepository.update(booking);

        // Release mechanic back to AVAILABLE
        if (booking.getMechanicId() != null) {
            mechanicRepository.findById(booking.getMechanicId()).ifPresent(m -> {
                m.setAvailability(Availability.AVAILABLE);
                mechanicRepository.update(m);
            });
        }

        return true;
    }

    public boolean cancelBooking(int bookingId) {
        ServiceBooking booking = getBookingById(bookingId);

        if (booking.getStatus() == BookingStatus.COMPLETED) {
            throw new InvalidBookingException("Cannot cancel a completed service booking.");
        }

        booking.setStatus(BookingStatus.CANCELLED);
        bookingRepository.update(booking);

        // Release mechanic if assigned
        if (booking.getMechanicId() != null) {
            mechanicRepository.findById(booking.getMechanicId()).ifPresent(m -> {
                m.setAvailability(Availability.AVAILABLE);
                mechanicRepository.update(m);
            });
        }

        return true;
    }

    public ServiceBooking getBookingById(int bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException("Service Booking not found with ID: " + bookingId));
    }

    public List<ServiceBooking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public List<ServiceBooking> getBookingsByVehicleId(int vehicleId) {
        return bookingRepository.findByVehicleId(vehicleId);
    }
}
