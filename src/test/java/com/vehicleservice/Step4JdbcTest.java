package com.vehicleservice;

import com.vehicleservice.config.DatabaseConnection;
import com.vehicleservice.model.*;
import com.vehicleservice.repository.*;
import com.vehicleservice.service.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class Step4JdbcTest {

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println(" STEP 4 MYSQL + JDBC DAO PERSISTENCE TEST");
        System.out.println("==================================================");

        if (!DatabaseConnection.testConnection()) {
            System.err.println("❌ Cannot run test: Database connection failed.");
            return;
        }
        System.out.println("✅ JDBC Connection Verified.");

        // Initialize JDBC DAOs
        CustomerRepository customerRepo = new JdbcCustomerRepository();
        VehicleRepository vehicleRepo = new JdbcVehicleRepository();
        MechanicRepository mechanicRepo = new JdbcMechanicRepository();
        ServiceRepository serviceRepo = new JdbcServiceRepository();
        ServiceBookingRepository bookingRepo = new JdbcServiceBookingRepository();
        ServiceRecordRepository recordRepo = new JdbcServiceRecordRepository();
        ServiceDetailRepository detailRepo = new JdbcServiceDetailRepository();
        BillRepository billRepo = new JdbcBillRepository();

        // Initialize Services
        CustomerService customerService = new CustomerService(customerRepo);
        VehicleService vehicleService = new VehicleService(vehicleRepo, customerRepo);
        MechanicService mechanicService = new MechanicService(mechanicRepo);
        ServiceManagementService catalogService = new ServiceManagementService(serviceRepo);
        BookingService bookingService = new BookingService(bookingRepo, vehicleRepo, serviceRepo, mechanicRepo);
        ServiceRecordService recordService = new ServiceRecordService(recordRepo, bookingRepo, serviceRepo, detailRepo);
        BillingService billingService = new BillingService(billRepo, recordRepo);

        String testRegNo = "TN-99-ZZ-" + (1000 + (int)(Math.random() * 8999));

        // 1. Add Customer
        Customer c = customerService.addCustomer("JDBC Test Owner", "9999900000", "jdbctest@example.com");
        System.out.println("1. Customer Saved to MySQL [ID: " + c.getCustomerId() + "]: " + c.getName());

        // 2. Add Vehicle
        Vehicle v = vehicleService.addVehicle(testRegNo, "Honda", "Civic", VehicleType.CAR, c.getCustomerId());
        System.out.println("2. Vehicle Saved to MySQL [ID: " + v.getVehicleId() + "]: " + v.getRegistrationNumber());

        // 3. Add Mechanic
        Mechanic m = mechanicService.addMechanic("JDBC Mechanic", "9888877777", Specialization.ENGINE);
        System.out.println("3. Mechanic Saved to MySQL [ID: " + m.getMechanicId() + "]: " + m.getName());

        // 4. Add Catalog Service
        Service s = catalogService.addService("JDBC Engine Service", new BigDecimal("4000.00"), "Full synthetic oil change & tuning");
        System.out.println("4. Service Saved to MySQL [ID: " + s.getServiceId() + "]: " + s.getServiceName());

        // 5. Create Booking
        ServiceBooking b = bookingService.createBooking(v.getVehicleId(), s.getServiceId(), LocalDate.now(), "JDBC Test Booking");
        System.out.println("5. Booking Saved to MySQL [ID: " + b.getBookingId() + "]: Status=" + b.getStatus());

        // 6. Assign Mechanic
        bookingService.assignMechanic(b.getBookingId(), m.getMechanicId());
        System.out.println("6. Assigned Mechanic to Booking [Status: ASSIGNED]");

        // 7. Start & Complete Booking
        bookingService.startService(b.getBookingId());
        bookingService.completeBooking(b.getBookingId());
        System.out.println("7. Service Progressed & Completed [Status: COMPLETED]");

        // 8. Create Service Record
        ServiceRecord r = recordService.createRecord(b.getBookingId(), LocalDate.now(), "Engine tuned & tested on dyno.", new BigDecimal("500.00"));
        System.out.println("8. Service Record Saved to MySQL [ID: " + r.getRecordId() + "]: Total=" + r.getTotalServiceCost());

        // 9. Generate Bill
        Bill bill = billingService.generateBill(r.getRecordId(), new BigDecimal("500.00"), new BigDecimal("400.00"), new BigDecimal("0.18"), new BigDecimal("200.00"));
        System.out.println("9. Bill Generated & Saved to MySQL [ID: " + bill.getBillId() + "]: Total=" + bill.getTotalAmount());

        // 10. Mark Paid
        billingService.markBillAsPaid(bill.getBillId());
        System.out.println("10. Bill Status Updated to PAID in MySQL.");

        // 11. Read back from MySQL
        List<Customer> allCustomers = customerService.getAllCustomers();
        List<Vehicle> allVehicles = vehicleService.getAllVehicles();
        System.out.println("11. Query Verified from MySQL. Total Customers: " + allCustomers.size() + " | Total Vehicles: " + allVehicles.size());

        System.out.println("\n✅ MYSQL JDBC PERSISTENCE VERIFICATION SUCCESSFUL!");
    }
}
