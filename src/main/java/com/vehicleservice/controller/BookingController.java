package com.vehicleservice.controller;

import com.vehicleservice.exception.BookingNotFoundException;
import com.vehicleservice.exception.InvalidBookingException;
import com.vehicleservice.exception.MechanicNotFoundException;
import com.vehicleservice.exception.VehicleNotFoundException;
import com.vehicleservice.model.ServiceBooking;
import com.vehicleservice.service.BookingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping
    public ResponseEntity<List<ServiceBooking>> getAllBookings() {
        List<ServiceBooking> bookings = bookingService.getAllBookings();
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceBooking> getBookingById(@PathVariable("id") int id) {
        ServiceBooking booking = bookingService.getBookingById(id);
        return ResponseEntity.ok(booking);
    }

    @GetMapping("/vehicle/{vehicleId}")
    public ResponseEntity<List<ServiceBooking>> getBookingsByVehicleId(@PathVariable("vehicleId") int vehicleId) {
        List<ServiceBooking> bookings = bookingService.getBookingsByVehicleId(vehicleId);
        return ResponseEntity.ok(bookings);
    }

    @PostMapping
    public ResponseEntity<ServiceBooking> createBooking(@RequestBody ServiceBooking booking) {
        ServiceBooking created = bookingService.createBooking(
                booking.getVehicleId(),
                booking.getServiceId(),
                booking.getServiceDate(),
                booking.getDescription()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}/assign")
    public ResponseEntity<Void> assignMechanic(@PathVariable("id") int id, @RequestParam("mechanicId") int mechanicId) {
        bookingService.assignMechanic(id, mechanicId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/start")
    public ResponseEntity<Void> startService(@PathVariable("id") int id) {
        bookingService.startService(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<Void> completeBooking(@PathVariable("id") int id) {
        bookingService.completeBooking(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelBooking(@PathVariable("id") int id) {
        bookingService.cancelBooking(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable("id") int id) {
        bookingService.cancelBooking(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(BookingNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleBookingNotFound(BookingNotFoundException ex) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(VehicleNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleVehicleNotFound(VehicleNotFoundException ex) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(MechanicNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleMechanicNotFound(MechanicNotFoundException ex) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(InvalidBookingException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidBooking(InvalidBookingException ex) {
        return buildErrorResponse(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException ex) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    private ResponseEntity<Map<String, Object>> buildErrorResponse(HttpStatus status, String message) {
        Map<String, Object> error = new LinkedHashMap<>();
        error.put("success", false);
        error.put("status", status.value());
        error.put("message", message);
        return ResponseEntity.status(status).body(error);
    }
}
