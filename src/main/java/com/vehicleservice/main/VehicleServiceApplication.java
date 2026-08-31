package com.vehicleservice.main;

import com.vehicleservice.model.*;
import com.vehicleservice.util.InputValidator;

import java.math.BigDecimal;
import java.time.LocalDate;

public class VehicleServiceApplication {

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println(" VEHICLE SERVICE MANAGEMENT SYSTEM");
        System.out.println("   Domain Layer & OOP Demonstration");
        System.out.println("========================================");
        System.out.println();

        // 1. Instantiate Customer (Inherits from Person)
        Customer customer = new Customer(1, "Arun Kumar", "9876543210", "arun@example.com");
        System.out.println("Created Customer: " + customer);
        System.out.println("  Valid Phone? " + InputValidator.isValidPhone(customer.getPhone()));

        // 2. Instantiate Vehicle owned by Customer
        Vehicle vehicle = new Vehicle(101, "TN-01-AB-1234", "Toyota", "Innova", VehicleType.CAR, customer.getCustomerId());
        System.out.println("Created Vehicle: " + vehicle);
        System.out.println("  Valid Reg No? " + InputValidator.isValidRegistrationNumber(vehicle.getRegistrationNumber()));

        // 3. Instantiate Mechanic (Inherits from Person)
        Mechanic mechanic = new Mechanic(201, "Ramesh", "9123456789", Specialization.ENGINE, Availability.AVAILABLE);
        System.out.println("Created Mechanic: " + mechanic);

        // 4. Instantiate Service catalog item
        Service service = new Service(301, "Full Engine Service", new BigDecimal("3500.00"), "Comprehensive engine checkup and oil change");
        System.out.println("Created Service: " + service);

        // 5. Instantiate Service Booking
        ServiceBooking booking = new ServiceBooking(401, vehicle.getVehicleId(), mechanic.getMechanicId(), service.getServiceId(), LocalDate.now(), BookingStatus.ASSIGNED, "Regular engine maintenance");
        System.out.println("Created Service Booking: " + booking);

        // 6. Instantiate Completed Service Record
        ServiceRecord record = new ServiceRecord(501, booking.getBookingId(), LocalDate.now(), "Engine oil replaced, filter cleaned", new BigDecimal("3500.00"));
        System.out.println("Created Service Record: " + record);

        // 7. Instantiate Service Detail line-item (Composition)
        ServiceDetail detail = new ServiceDetail(601, record.getRecordId(), service.getServiceId(), 1, service.getBasePrice());
        System.out.println("Created Service Detail: " + detail);

        // 8. Instantiate Bill with automatic calculation (Subtotal + Tax - Discount)
        Bill bill = new Bill(record.getRecordId(), record.getTotalServiceCost(), new BigDecimal("500.00"), new BigDecimal("300.00"), new BigDecimal("0.18"), new BigDecimal("200.00"));
        bill.setBillId(701);
        System.out.println("Generated Bill: " + bill);

        System.out.println("\nDomain models instantiated and validated successfully.");
    }
}
