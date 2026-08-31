package com.vehicleservice.console;

import com.vehicleservice.model.*;
import com.vehicleservice.service.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class ConsoleUI {
    private final CustomerService customerService;
    private final VehicleService vehicleService;
    private final MechanicService mechanicService;
    private final ServiceManagementService catalogService;
    private final BookingService bookingService;
    private final ServiceRecordService recordService;
    private final BillingService billingService;

    public ConsoleUI(CustomerService customerService,
                     VehicleService vehicleService,
                     MechanicService mechanicService,
                     ServiceManagementService catalogService,
                     BookingService bookingService,
                     ServiceRecordService recordService,
                     BillingService billingService) {
        this.customerService = customerService;
        this.vehicleService = vehicleService;
        this.mechanicService = mechanicService;
        this.catalogService = catalogService;
        this.bookingService = bookingService;
        this.recordService = recordService;
        this.billingService = billingService;
    }

    public void start() {
        boolean running = true;
        while (running) {
            System.out.println("\n========================================");
            System.out.println(" VEHICLE SERVICE MANAGEMENT SYSTEM");
            System.out.println("========================================");
            System.out.println("1. Customer Management");
            System.out.println("2. Vehicle Management");
            System.out.println("3. Mechanic Management");
            System.out.println("4. Service Management (Catalog)");
            System.out.println("5. Service Booking");
            System.out.println("6. Service Tracking");
            System.out.println("7. Service History");
            System.out.println("8. Billing");
            System.out.println("9. Seed Sample Demo Data");
            System.out.println("0. Exit");
            System.out.println("----------------------------------------");

            int choice = ConsoleInput.readInt("Enter your choice: ");
            switch (choice) {
                case 1 -> customerMenu();
                case 2 -> vehicleMenu();
                case 3 -> mechanicMenu();
                case 4 -> serviceCatalogMenu();
                case 5 -> bookingMenu();
                case 6 -> serviceTrackingMenu();
                case 7 -> serviceHistoryMenu();
                case 8 -> billingMenu();
                case 9 -> seedSampleData();
                case 0 -> {
                    System.out.println("\nThank you for using Vehicle Service Management System. Goodbye!");
                    running = false;
                }
                default -> System.out.println("⚠️ Invalid choice. Please select from the menu.");
            }
        }
    }

    // --- 1. CUSTOMER MENU ---
    private void customerMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- CUSTOMER MANAGEMENT ---");
            System.out.println("1. Add Customer");
            System.out.println("2. View All Customers");
            System.out.println("3. Search Customer by ID");
            System.out.println("4. Update Customer");
            System.out.println("5. Delete Customer");
            System.out.println("6. Back to Main Menu");
            int choice = ConsoleInput.readInt("Choice: ");

            try {
                switch (choice) {
                    case 1 -> {
                        String name = ConsoleInput.readString("Enter Customer Name: ");
                        String phone = ConsoleInput.readString("Enter Phone (10 digits): ");
                        String email = ConsoleInput.readString("Enter Email: ");
                        Customer customer = customerService.addCustomer(name, phone, email);
                        System.out.println("✅ Customer Added Successfully: " + customer);
                    }
                    case 2 -> {
                        List<Customer> list = customerService.getAllCustomers();
                        if (list.isEmpty()) System.out.println("ℹ️ No customers found.");
                        else list.forEach(System.out::println);
                    }
                    case 3 -> {
                        int id = ConsoleInput.readInt("Enter Customer ID: ");
                        Customer customer = customerService.getCustomerById(id);
                        System.out.println("Found: " + customer);
                    }
                    case 4 -> {
                        int id = ConsoleInput.readInt("Enter Customer ID to Update: ");
                        Customer existing = customerService.getCustomerById(id);
                        String name = ConsoleInput.readString("New Name (" + existing.getName() + "): ");
                        String phone = ConsoleInput.readString("New Phone (" + existing.getPhone() + "): ");
                        String email = ConsoleInput.readString("New Email (" + existing.getEmail() + "): ");
                        existing.setName(!name.isEmpty() ? name : existing.getName());
                        existing.setPhone(!phone.isEmpty() ? phone : existing.getPhone());
                        existing.setEmail(!email.isEmpty() ? email : existing.getEmail());
                        customerService.updateCustomer(existing);
                        System.out.println("✅ Customer Updated Successfully.");
                    }
                    case 5 -> {
                        int id = ConsoleInput.readInt("Enter Customer ID to Delete: ");
                        customerService.deleteCustomer(id);
                        System.out.println("✅ Customer Deleted Successfully.");
                    }
                    case 6 -> back = true;
                    default -> System.out.println("⚠️ Invalid choice.");
                }
            } catch (Exception e) {
                System.out.println("❌ Error: " + e.getMessage());
            }
        }
    }

    // --- 2. VEHICLE MENU ---
    private void vehicleMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- VEHICLE MANAGEMENT ---");
            System.out.println("1. Add Vehicle");
            System.out.println("2. View All Vehicles");
            System.out.println("3. Search Vehicle by Registration Number");
            System.out.println("4. View Vehicles by Customer ID");
            System.out.println("5. Delete Vehicle");
            System.out.println("6. Back to Main Menu");
            int choice = ConsoleInput.readInt("Choice: ");

            try {
                switch (choice) {
                    case 1 -> {
                        int custId = ConsoleInput.readInt("Enter Customer ID: ");
                        String reg = ConsoleInput.readString("Enter Registration Number (e.g. TN-01-AB-1234): ");
                        String brand = ConsoleInput.readString("Enter Brand: ");
                        String model = ConsoleInput.readString("Enter Model: ");
                        System.out.println("Types: 1. CAR, 2. BIKE, 3. SUV, 4. OTHER");
                        int typeChoice = ConsoleInput.readInt("Vehicle Type (1-4): ");
                        VehicleType type = switch (typeChoice) {
                            case 2 -> VehicleType.BIKE;
                            case 3 -> VehicleType.SUV;
                            case 4 -> VehicleType.OTHER;
                            default -> VehicleType.CAR;
                        };
                        Vehicle vehicle = vehicleService.addVehicle(reg, brand, model, type, custId);
                        System.out.println("✅ Vehicle Added Successfully: " + vehicle);
                    }
                    case 2 -> {
                        List<Vehicle> list = vehicleService.getAllVehicles();
                        if (list.isEmpty()) System.out.println("ℹ️ No vehicles registered.");
                        else list.forEach(System.out::println);
                    }
                    case 3 -> {
                        String reg = ConsoleInput.readString("Enter Registration Number: ");
                        Vehicle vehicle = vehicleService.getVehicleByRegistrationNumber(reg);
                        System.out.println("Found: " + vehicle);
                    }
                    case 4 -> {
                        int custId = ConsoleInput.readInt("Enter Customer ID: ");
                        List<Vehicle> list = vehicleService.getVehiclesByCustomerId(custId);
                        if (list.isEmpty()) System.out.println("ℹ️ No vehicles found for this customer.");
                        else list.forEach(System.out::println);
                    }
                    case 5 -> {
                        int id = ConsoleInput.readInt("Enter Vehicle ID to Delete: ");
                        vehicleService.deleteVehicle(id);
                        System.out.println("✅ Vehicle Deleted Successfully.");
                    }
                    case 6 -> back = true;
                    default -> System.out.println("⚠️ Invalid choice.");
                }
            } catch (Exception e) {
                System.out.println("❌ Error: " + e.getMessage());
            }
        }
    }

    // --- 3. MECHANIC MENU ---
    private void mechanicMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- MECHANIC MANAGEMENT ---");
            System.out.println("1. Add Mechanic");
            System.out.println("2. View All Mechanics");
            System.out.println("3. Search Mechanic by ID");
            System.out.println("4. Update Mechanic Availability");
            System.out.println("5. Back to Main Menu");
            int choice = ConsoleInput.readInt("Choice: ");

            try {
                switch (choice) {
                    case 1 -> {
                        String name = ConsoleInput.readString("Enter Mechanic Name: ");
                        String phone = ConsoleInput.readString("Enter Phone Number: ");
                        System.out.println("Specialization: 1. GENERAL, 2. ENGINE, 3. BRAKE, 4. ELECTRICAL");
                        int specChoice = ConsoleInput.readInt("Select (1-4): ");
                        Specialization spec = switch (specChoice) {
                            case 2 -> Specialization.ENGINE;
                            case 3 -> Specialization.BRAKE;
                            case 4 -> Specialization.ELECTRICAL;
                            default -> Specialization.GENERAL;
                        };
                        Mechanic m = mechanicService.addMechanic(name, phone, spec);
                        System.out.println("✅ Mechanic Added: " + m);
                    }
                    case 2 -> {
                        List<Mechanic> list = mechanicService.getAllMechanics();
                        if (list.isEmpty()) System.out.println("ℹ️ No mechanics found.");
                        else list.forEach(System.out::println);
                    }
                    case 3 -> {
                        int id = ConsoleInput.readInt("Enter Mechanic ID: ");
                        System.out.println("Found: " + mechanicService.getMechanicById(id));
                    }
                    case 4 -> {
                        int id = ConsoleInput.readInt("Enter Mechanic ID: ");
                        System.out.println("Status: 1. AVAILABLE, 2. BUSY, 3. UNAVAILABLE");
                        int stChoice = ConsoleInput.readInt("Select (1-3): ");
                        Availability avail = switch (stChoice) {
                            case 2 -> Availability.BUSY;
                            case 3 -> Availability.UNAVAILABLE;
                            default -> Availability.AVAILABLE;
                        };
                        mechanicService.updateAvailability(id, avail);
                        System.out.println("✅ Availability Updated.");
                    }
                    case 5 -> back = true;
                    default -> System.out.println("⚠️ Invalid choice.");
                }
            } catch (Exception e) {
                System.out.println("❌ Error: " + e.getMessage());
            }
        }
    }

    // --- 4. SERVICE CATALOG MENU ---
    private void serviceCatalogMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- SERVICE CATALOG MANAGEMENT ---");
            System.out.println("1. Add Service Offering");
            System.out.println("2. View Catalog Services");
            System.out.println("3. Back to Main Menu");
            int choice = ConsoleInput.readInt("Choice: ");

            try {
                switch (choice) {
                    case 1 -> {
                        String name = ConsoleInput.readString("Service Name: ");
                        BigDecimal price = ConsoleInput.readBigDecimal("Base Price (₹): ");
                        String desc = ConsoleInput.readString("Description: ");
                        Service s = catalogService.addService(name, price, desc);
                        System.out.println("✅ Service Created: " + s);
                    }
                    case 2 -> {
                        List<Service> list = catalogService.getAllServices();
                        if (list.isEmpty()) System.out.println("ℹ️ Catalog is empty.");
                        else list.forEach(System.out::println);
                    }
                    case 3 -> back = true;
                    default -> System.out.println("⚠️ Invalid choice.");
                }
            } catch (Exception e) {
                System.out.println("❌ Error: " + e.getMessage());
            }
        }
    }

    // --- 5. BOOKING MENU ---
    private void bookingMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- SERVICE BOOKING MANAGEMENT ---");
            System.out.println("1. Create New Service Booking");
            System.out.println("2. Assign Mechanic to Booking");
            System.out.println("3. Start Service (Set IN_PROGRESS)");
            System.out.println("4. Complete Service (Set COMPLETED)");
            System.out.println("5. Cancel Booking");
            System.out.println("6. Back to Main Menu");
            int choice = ConsoleInput.readInt("Choice: ");

            try {
                switch (choice) {
                    case 1 -> {
                        int vId = ConsoleInput.readInt("Enter Vehicle ID: ");
                        int sId = ConsoleInput.readInt("Enter Catalog Service ID: ");
                        LocalDate date = ConsoleInput.readDate("Service Date");
                        String desc = ConsoleInput.readString("Problem/Service Description: ");
                        ServiceBooking b = bookingService.createBooking(vId, sId, date, desc);
                        System.out.println("✅ Booking Created Successfully: " + b);
                    }
                    case 2 -> {
                        int bId = ConsoleInput.readInt("Enter Booking ID: ");
                        int mId = ConsoleInput.readInt("Enter Mechanic ID to Assign: ");
                        bookingService.assignMechanic(bId, mId);
                        System.out.println("✅ Mechanic Assigned & Booking Status updated to ASSIGNED.");
                    }
                    case 3 -> {
                        int bId = ConsoleInput.readInt("Enter Booking ID: ");
                        bookingService.startService(bId);
                        System.out.println("✅ Service Started. Status updated to IN_PROGRESS.");
                    }
                    case 4 -> {
                        int bId = ConsoleInput.readInt("Enter Booking ID: ");
                        bookingService.completeBooking(bId);
                        System.out.println("✅ Service Completed! Status updated to COMPLETED. Mechanic released.");
                        
                        // Automatically prompt for Service Record creation
                        System.out.print("Create completed Service Record now? (y/n): ");
                        String resp = ConsoleInput.readString("");
                        if (resp.equalsIgnoreCase("y")) {
                            LocalDate compDate = ConsoleInput.readDate("Completion Date");
                            String remarks = ConsoleInput.readString("Mechanic Remarks: ");
                            BigDecimal extraCost = ConsoleInput.readBigDecimal("Extra Spare Parts Cost (₹): ");
                            ServiceRecord rec = recordService.createRecord(bId, compDate, remarks, extraCost);
                            System.out.println("✅ Service Record Created: " + rec);
                        }
                    }
                    case 5 -> {
                        int bId = ConsoleInput.readInt("Enter Booking ID: ");
                        bookingService.cancelBooking(bId);
                        System.out.println("✅ Booking Cancelled.");
                    }
                    case 6 -> back = true;
                    default -> System.out.println("⚠️ Invalid choice.");
                }
            } catch (Exception e) {
                System.out.println("❌ Error: " + e.getMessage());
            }
        }
    }

    // --- 6. SERVICE TRACKING MENU ---
    private void serviceTrackingMenu() {
        System.out.println("\n--- SERVICE TRACKING ---");
        List<ServiceBooking> list = bookingService.getAllBookings();
        if (list.isEmpty()) {
            System.out.println("ℹ️ No active or historical bookings found.");
            return;
        }

        System.out.printf("%-10s %-12s %-12s %-12s %-15s %-15s%n", "BookingID", "VehicleID", "MechanicID", "ServiceID", "Date", "Status");
        System.out.println("-----------------------------------------------------------------------------");
        for (ServiceBooking b : list) {
            String mStr = b.getMechanicId() != null ? String.valueOf(b.getMechanicId()) : "Unassigned";
            System.out.printf("%-10d %-12d %-12s %-12d %-15s %-15s%n",
                    b.getBookingId(), b.getVehicleId(), mStr, b.getServiceId(), b.getServiceDate(), b.getStatus());
        }
    }

    // --- 7. SERVICE HISTORY MENU ---
    private void serviceHistoryMenu() {
        System.out.println("\n--- VEHICLE SERVICE HISTORY ---");
        int vId = ConsoleInput.readInt("Enter Vehicle ID: ");
        try {
            Vehicle v = vehicleService.getVehicleById(vId);
            System.out.println("Vehicle: " + v.getRegistrationNumber() + " (" + v.getBrand() + " " + v.getModel() + ")");

            List<ServiceRecord> records = recordService.getHistoryByVehicleId(vId);
            if (records.isEmpty()) {
                System.out.println("ℹ️ No completed service history found for this vehicle.");
                return;
            }

            records.forEach(r -> {
                System.out.println("\n  Record ID: " + r.getRecordId() + " | Date: " + r.getCompletionDate());
                System.out.println("  Remarks: " + r.getRemarks());
                System.out.println("  Total Service Cost: ₹" + r.getTotalServiceCost());
                List<ServiceDetail> details = recordService.getDetailsForRecord(r.getRecordId());
                details.forEach(d -> System.out.println("    └─ Service #" + d.getServiceId() + " (Qty: " + d.getQuantity() + ", Subtotal: ₹" + d.getSubtotal() + ")"));
            });
        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

    // --- 8. BILLING MENU ---
    private void billingMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- BILLING MANAGEMENT ---");
            System.out.println("1. Generate Bill for Completed Service Record");
            System.out.println("2. View All Bills");
            System.out.println("3. Search Bill by ID");
            System.out.println("4. Mark Bill as PAID");
            System.out.println("5. Back to Main Menu");
            int choice = ConsoleInput.readInt("Choice: ");

            try {
                switch (choice) {
                    case 1 -> {
                        int rId = ConsoleInput.readInt("Enter Service Record ID: ");
                        BigDecimal parts = ConsoleInput.readBigDecimal("Parts Cost (₹): ");
                        BigDecimal labor = ConsoleInput.readBigDecimal("Labor Cost (₹): ");
                        BigDecimal taxRate = ConsoleInput.readBigDecimal("GST Tax Rate (e.g. 0.18 for 18%): ");
                        BigDecimal disc = ConsoleInput.readBigDecimal("Discount (₹): ");
                        Bill bill = billingService.generateBill(rId, parts, labor, taxRate, disc);
                        System.out.println("✅ Bill Generated Successfully:\n" + bill);
                    }
                    case 2 -> {
                        List<Bill> list = billingService.getAllBills();
                        if (list.isEmpty()) System.out.println("ℹ️ No bills generated yet.");
                        else list.forEach(System.out::println);
                    }
                    case 3 -> {
                        int id = ConsoleInput.readInt("Enter Bill ID: ");
                        System.out.println("Found: " + billingService.getBillById(id));
                    }
                    case 4 -> {
                        int id = ConsoleInput.readInt("Enter Bill ID to Mark as PAID: ");
                        billingService.markBillAsPaid(id);
                        System.out.println("✅ Payment Status Updated to PAID.");
                    }
                    case 5 -> back = true;
                    default -> System.out.println("⚠️ Invalid choice.");
                }
            } catch (Exception e) {
                System.out.println("❌ Error: " + e.getMessage());
            }
        }
    }

    // --- 9. SEED SAMPLE DEMO DATA ---
    public void seedSampleData() {
        try {
            Customer c1 = customerService.addCustomer("Arun Kumar", "9876543210", "arun@example.com");
            Customer c2 = customerService.addCustomer("Priya Sharma", "9123456789", "priya@example.com");

            Vehicle v1 = vehicleService.addVehicle("TN-01-AB-1234", "Toyota", "Innova", VehicleType.CAR, c1.getCustomerId());
            Vehicle v2 = vehicleService.addVehicle("TN-02-CD-5678", "Honda", "City", VehicleType.CAR, c2.getCustomerId());

            Mechanic m1 = mechanicService.addMechanic("Ramesh", "9988776655", Specialization.ENGINE);
            Mechanic m2 = mechanicService.addMechanic("Suresh", "9876123456", Specialization.ELECTRICAL);

            Service s1 = catalogService.addService("General Service", new BigDecimal("1500.00"), "Basic oil change & wash");
            Service s2 = catalogService.addService("Full Engine Checkup", new BigDecimal("3500.00"), "Complete engine inspection");

            System.out.println("✅ Sample Demo Data Seeded Successfully!");
            System.out.println("  Added 2 Customers, 2 Vehicles, 2 Mechanics, and 2 Catalog Services.");
        } catch (Exception e) {
            System.out.println("ℹ️ Sample data already seeded or partially populated.");
        }
    }
}
