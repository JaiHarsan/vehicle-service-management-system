package com.vehicleservice.main;

import com.vehicleservice.config.DatabaseConnection;
import com.vehicleservice.console.ConsoleUI;
import com.vehicleservice.repository.*;
import com.vehicleservice.service.*;

public class VehicleServiceApplication {

    public static void main(String[] args) {
        System.out.println("Connecting to MySQL Database...");
        if (!DatabaseConnection.testConnection()) {
            System.err.println("❌ Failed to connect to MySQL database!");
            System.err.println("Please check DatabaseConnection configuration or application.properties file.");
            return;
        }
        System.out.println("✅ Connected successfully to MySQL Database: vehicle_service");

        // 1. Initialize JDBC DAO Repositories (Phase 2 - MySQL Persistence)
        CustomerRepository customerRepository = new JdbcCustomerRepository();
        VehicleRepository vehicleRepository = new JdbcVehicleRepository();
        MechanicRepository mechanicRepository = new JdbcMechanicRepository();
        ServiceRepository serviceRepository = new JdbcServiceRepository();
        ServiceBookingRepository bookingRepository = new JdbcServiceBookingRepository();
        ServiceRecordRepository recordRepository = new JdbcServiceRecordRepository();
        ServiceDetailRepository detailRepository = new JdbcServiceDetailRepository();
        BillRepository billRepository = new JdbcBillRepository();

        // 2. Initialize Business Service Layer
        CustomerService customerService = new CustomerService(customerRepository);
        VehicleService vehicleService = new VehicleService(vehicleRepository, customerRepository);
        MechanicService mechanicService = new MechanicService(mechanicRepository);
        ServiceManagementService catalogService = new ServiceManagementService(serviceRepository);
        BookingService bookingService = new BookingService(bookingRepository, vehicleRepository, serviceRepository, mechanicRepository);
        ServiceRecordService recordService = new ServiceRecordService(recordRepository, bookingRepository, serviceRepository, detailRepository);
        BillingService billingService = new BillingService(billRepository, recordRepository);

        // 3. Initialize Console User Interface
        ConsoleUI consoleUI = new ConsoleUI(customerService, vehicleService, mechanicService, catalogService, bookingService, recordService, billingService);

        // 4. Start Interactive CLI Main Menu
        consoleUI.start();
    }
}
