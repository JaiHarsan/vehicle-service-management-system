package com.vehicleservice.config;

import com.vehicleservice.repository.*;
import com.vehicleservice.service.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class AppConfig {

    public AppConfig(DataSource dataSource) {
        // Wire Spring Boot managed DataSource into DatabaseConnection
        DatabaseConnection.setDataSource(dataSource);
    }

    // --- Repositories (JDBC implementations as primary Spring beans) ---

    @Bean
    public CustomerRepository customerRepository() {
        return new JdbcCustomerRepository();
    }

    @Bean
    public VehicleRepository vehicleRepository() {
        return new JdbcVehicleRepository();
    }

    @Bean
    public MechanicRepository mechanicRepository() {
        return new JdbcMechanicRepository();
    }

    @Bean
    public ServiceRepository serviceRepository() {
        return new JdbcServiceRepository();
    }

    @Bean
    public ServiceBookingRepository serviceBookingRepository() {
        return new JdbcServiceBookingRepository();
    }

    @Bean
    public ServiceRecordRepository serviceRecordRepository() {
        return new JdbcServiceRecordRepository();
    }

    @Bean
    public ServiceDetailRepository serviceDetailRepository() {
        return new JdbcServiceDetailRepository();
    }

    @Bean
    public BillRepository billRepository() {
        return new JdbcBillRepository();
    }

    // --- Service Layer ---

    @Bean
    public CustomerService customerService(CustomerRepository customerRepository) {
        return new CustomerService(customerRepository);
    }

    @Bean
    public VehicleService vehicleService(VehicleRepository vehicleRepository, CustomerRepository customerRepository) {
        return new VehicleService(vehicleRepository, customerRepository);
    }

    @Bean
    public MechanicService mechanicService(MechanicRepository mechanicRepository) {
        return new MechanicService(mechanicRepository);
    }

    @Bean
    public ServiceManagementService serviceManagementService(ServiceRepository serviceRepository) {
        return new ServiceManagementService(serviceRepository);
    }

    @Bean
    public BookingService bookingService(ServiceBookingRepository bookingRepository,
                                         VehicleRepository vehicleRepository,
                                         ServiceRepository serviceRepository,
                                         MechanicRepository mechanicRepository) {
        return new BookingService(bookingRepository, vehicleRepository, serviceRepository, mechanicRepository);
    }

    @Bean
    public ServiceRecordService serviceRecordService(ServiceRecordRepository recordRepository,
                                                     ServiceBookingRepository bookingRepository,
                                                     ServiceRepository serviceRepository,
                                                     ServiceDetailRepository detailRepository) {
        return new ServiceRecordService(recordRepository, bookingRepository, serviceRepository, detailRepository);
    }

    @Bean
    public BillingService billingService(BillRepository billRepository, ServiceRecordRepository recordRepository) {
        return new BillingService(billRepository, recordRepository);
    }
}
