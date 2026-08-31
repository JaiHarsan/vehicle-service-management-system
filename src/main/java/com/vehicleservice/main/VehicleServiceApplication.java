package com.vehicleservice.main;

import com.vehicleservice.console.ConsoleUI;
import com.vehicleservice.repository.*;
import com.vehicleservice.service.*;

public class VehicleServiceApplication {

    public static void main(String[] args) {
        // 1. Initialize Repositories (In-Memory for Phase 1)
        CustomerRepository customerRepository = new InMemoryCustomerRepository();
        VehicleRepository vehicleRepository = new InMemoryVehicleRepository();
        MechanicRepository mechanicRepository = new InMemoryMechanicRepository();
        ServiceRepository serviceRepository = new InMemoryServiceRepository();
        ServiceBookingRepository bookingRepository = new InMemoryServiceBookingRepository();
        ServiceRecordRepository recordRepository = new InMemoryServiceRecordRepository();
        ServiceDetailRepository detailRepository = new InMemoryServiceDetailRepository();
        BillRepository billRepository = new InMemoryBillRepository();

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

        // 4. Pre-seed initial sample data for easy demonstration
        consoleUI.seedSampleData();

        // 5. Start Interactive CLI Main Menu
        consoleUI.start();
    }
}
