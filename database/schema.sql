-- ===================================================
-- VEHICLE SERVICE MANAGEMENT SYSTEM - DATABASE SCHEMA
-- Target Database: MySQL 8.0+ / 9.0+
-- ===================================================

CREATE DATABASE IF NOT EXISTS vehicle_service;
USE vehicle_service;

-- Disable Foreign Key checks temporarily for clean re-creation
SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS bills;
DROP TABLE IF EXISTS service_details;
DROP TABLE IF EXISTS service_records;
DROP TABLE IF EXISTS service_bookings;
DROP TABLE IF EXISTS services;
DROP TABLE IF EXISTS mechanics;
DROP TABLE IF EXISTS vehicles;
DROP TABLE IF EXISTS customers;

SET FOREIGN_KEY_CHECKS = 1;

-- 1. Customers Table (Vehicle Owners)
CREATE TABLE customers (
    customer_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    phone VARCHAR(15) NOT NULL,
    email VARCHAR(100)
);

-- 2. Vehicles Table
CREATE TABLE vehicles (
    vehicle_id INT PRIMARY KEY AUTO_INCREMENT,
    registration_number VARCHAR(30) NOT NULL UNIQUE,
    brand VARCHAR(50) NOT NULL,
    model VARCHAR(50) NOT NULL,
    vehicle_type VARCHAR(30) NOT NULL,
    customer_id INT NOT NULL,
    CONSTRAINT fk_vehicle_customer FOREIGN KEY (customer_id) 
        REFERENCES customers(customer_id) ON DELETE CASCADE
);

-- 3. Mechanics Table
CREATE TABLE mechanics (
    mechanic_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    phone VARCHAR(15) NOT NULL,
    specialization VARCHAR(50) NOT NULL,
    availability VARCHAR(30) NOT NULL DEFAULT 'AVAILABLE'
);

-- 4. Service Catalog Table
CREATE TABLE services (
    service_id INT PRIMARY KEY AUTO_INCREMENT,
    service_name VARCHAR(100) NOT NULL,
    base_price DECIMAL(10,2) NOT NULL,
    description VARCHAR(255)
);

-- 5. Service Bookings Table
CREATE TABLE service_bookings (
    booking_id INT PRIMARY KEY AUTO_INCREMENT,
    vehicle_id INT NOT NULL,
    mechanic_id INT NULL,
    service_id INT NOT NULL,
    service_date DATE NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'BOOKED',
    description VARCHAR(255),
    CONSTRAINT fk_booking_vehicle FOREIGN KEY (vehicle_id) 
        REFERENCES vehicles(vehicle_id) ON DELETE CASCADE,
    CONSTRAINT fk_booking_mechanic FOREIGN KEY (mechanic_id) 
        REFERENCES mechanics(mechanic_id) ON DELETE SET NULL,
    CONSTRAINT fk_booking_service FOREIGN KEY (service_id) 
        REFERENCES services(service_id) ON DELETE RESTRICT
);

-- 6. Service Records Table (Completed Work)
CREATE TABLE service_records (
    record_id INT PRIMARY KEY AUTO_INCREMENT,
    booking_id INT NOT NULL UNIQUE,
    completion_date DATE NOT NULL,
    remarks VARCHAR(255),
    total_service_cost DECIMAL(10,2) NOT NULL,
    CONSTRAINT fk_record_booking FOREIGN KEY (booking_id) 
        REFERENCES service_bookings(booking_id) ON DELETE CASCADE
);

-- 7. Service Details Table (Line Items Composition)
CREATE TABLE service_details (
    detail_id INT PRIMARY KEY AUTO_INCREMENT,
    record_id INT NOT NULL,
    service_id INT NOT NULL,
    quantity INT NOT NULL DEFAULT 1,
    unit_price DECIMAL(10,2) NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL,
    CONSTRAINT fk_detail_record FOREIGN KEY (record_id) 
        REFERENCES service_records(record_id) ON DELETE CASCADE,
    CONSTRAINT fk_detail_service FOREIGN KEY (service_id) 
        REFERENCES services(service_id) ON DELETE RESTRICT
);

-- 8. Bills Table (Financial Invoices)
CREATE TABLE bills (
    bill_id INT PRIMARY KEY AUTO_INCREMENT,
    record_id INT NOT NULL UNIQUE,
    service_cost DECIMAL(10,2) NOT NULL,
    parts_cost DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    labor_cost DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    tax DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    discount DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    total_amount DECIMAL(10,2) NOT NULL,
    payment_status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    CONSTRAINT fk_bill_record FOREIGN KEY (record_id) 
        REFERENCES service_records(record_id) ON DELETE CASCADE
);
