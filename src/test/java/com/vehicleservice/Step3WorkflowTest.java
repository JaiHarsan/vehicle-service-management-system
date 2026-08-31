package com.vehicleservice;

import com.vehicleservice.model.*;
import com.vehicleservice.repository.*;
import com.vehicleservice.service.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class Step3WorkflowTest {
    public static void main(String[] args) {
        System.out.println("=============================================");
        System.out.println(" STEP 3 END-TO-END WORKFLOW VERIFICATION TEST");
        System.out.println("=============================================");

        // 1. Repositories
        CustomerRepository customerRepo = new InMemoryCustomerRepository();
        VehicleRepository vehicleRepo = new InMemoryVehicleRepository();
        MechanicRepository mechanicRepo = new InMemoryMechanicRepository();
        ServiceRepository serviceRepo = new InMemoryServiceRepository();
        ServiceBookingRepository bookingRepo = new InMemoryServiceBookingRepository();
        ServiceRecordRepository recordRepo = new InMemoryServiceRecordRepository();
        ServiceDetailRepository detailRepo = new InMemoryServiceDetailRepository();
        BillRepository billRepo = new InMemoryBillRepository();

        // 2. Services
        CustomerService customerService = new CustomerService(customerRepo);
        VehicleService vehicleService = new VehicleService(vehicleRepo, customerRepo);
        MechanicService mechanicService = new MechanicService(mechanicRepo);
        ServiceManagementService catalogService = new ServiceManagementService(serviceRepo);
        BookingService bookingService = new BookingService(bookingRepo, vehicleRepo, serviceRepo, mechanicRepo);
        ServiceRecordService recordService = new ServiceRecordService(recordRepo, bookingRepo, serviceRepo, detailRepo);
        BillingService billingService = new BillingService(billRepo, recordRepo);

        // Step 1: Add Customer
        Customer customer = customerService.addCustomer("Karthik Raja", "9876501234", "karthik@example.com");
        System.out.println("1. Customer Created: " + customer);

        // Step 2: Add Vehicle
        Vehicle vehicle = vehicleService.addVehicle("TN-38-EF-9999", "Hyundai", "Creta", VehicleType.SUV, customer.getCustomerId());
        System.out.println("2. Vehicle Created: " + vehicle);

        // Step 3: Add Mechanic
        Mechanic mechanic = mechanicService.addMechanic("Venkatesh", "9123400000", Specialization.BRAKE);
        System.out.println("3. Mechanic Created: " + mechanic);

        // Step 4: Add Catalog Service
        Service service = catalogService.addService("Brake Pad Replacement", new BigDecimal("2500.00"), "Front & rear brake pad renewal");
        System.out.println("4. Service Offering Created: " + service);

        // Step 5: Create Booking
        ServiceBooking booking = bookingService.createBooking(vehicle.getVehicleId(), service.getServiceId(), LocalDate.now(), "Squeaking noise in brakes");
        System.out.println("5. Booking Created: " + booking);

        // Step 6: Assign Mechanic
        bookingService.assignMechanic(booking.getBookingId(), mechanic.getMechanicId());
        booking = bookingService.getBookingById(booking.getBookingId());
        mechanic = mechanicService.getMechanicById(mechanic.getMechanicId());
        System.out.println("6. Mechanic Assigned. Booking Status: " + booking.getStatus() + " | Mechanic Availability: " + mechanic.getAvailability());

        // Step 7: Start Service
        bookingService.startService(booking.getBookingId());
        booking = bookingService.getBookingById(booking.getBookingId());
        System.out.println("7. Service Started. Booking Status: " + booking.getStatus());

        // Step 8: Complete Service
        bookingService.completeBooking(booking.getBookingId());
        booking = bookingService.getBookingById(booking.getBookingId());
        mechanic = mechanicService.getMechanicById(mechanic.getMechanicId());
        System.out.println("8. Service Completed. Booking Status: " + booking.getStatus() + " | Mechanic Availability: " + mechanic.getAvailability());

        // Step 9: Create Service Record
        ServiceRecord record = recordService.createRecord(booking.getBookingId(), LocalDate.now(), "Brake pads replaced successfully. Tested.", new BigDecimal("400.00"));
        System.out.println("9. Service Record Created: " + record);

        // Step 10: Generate Bill
        Bill bill = billingService.generateBill(record.getRecordId(), new BigDecimal("400.00"), new BigDecimal("300.00"), new BigDecimal("0.18"), new BigDecimal("100.00"));
        System.out.println("10. Bill Generated: " + bill);

        // Step 11: Mark Bill Paid
        billingService.markBillAsPaid(bill.getBillId());
        bill = billingService.getBillById(bill.getBillId());
        System.out.println("11. Bill Status Updated: " + bill.getPaymentStatus());

        // Step 12: View History
        List<ServiceRecord> history = recordService.getHistoryByVehicleId(vehicle.getVehicleId());
        System.out.println("12. History Records Found: " + history.size());

        System.out.println("\n✅ END-TO-END WORKFLOW VERIFICATION SUCCESSFUL!");
    }
}
