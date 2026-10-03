-- ===================================================
-- VEHICLE SERVICE MANAGEMENT SYSTEM - SAMPLE SEED DATA
-- ===================================================

USE vehicle_service;

-- Insert Customers
INSERT INTO customers (customer_id, name, phone, email) VALUES
(1, 'Arun Kumar', '9876543210', 'arun@example.com'),
(2, 'Priya Sharma', '9123456789', 'priya@example.com'),
(3, 'Karthik Raja', '9876501234', 'karthik@example.com');

-- Insert Vehicles
INSERT INTO vehicles (vehicle_id, registration_number, brand, model, vehicle_type, customer_id) VALUES
(101, 'TN-01-AB-1234', 'Toyota', 'Innova', 'CAR', 1),
(102, 'TN-02-CD-5678', 'Honda', 'City', 'CAR', 2),
(103, 'TN-38-EF-9999', 'Hyundai', 'Creta', 'SUV', 3);

-- Insert Mechanics
INSERT INTO mechanics (mechanic_id, name, phone, specialization, availability) VALUES
(201, 'Ramesh', '9988776655', 'ENGINE', 'AVAILABLE'),
(202, 'Suresh', '9876123456', 'ELECTRICAL', 'AVAILABLE'),
(203, 'Venkatesh', '9123400000', 'BRAKE', 'AVAILABLE');

-- Insert Catalog Services
INSERT INTO services (service_id, service_name, base_price, description) VALUES
(301, 'General Service', 1500.00, 'Basic engine oil change, washing and fluid checkup'),
(302, 'Full Engine Checkup', 3500.00, 'Comprehensive engine overhaul & computerized diagnostics'),
(303, 'Brake Pad Replacement', 2500.00, 'Front and rear brake pad replacement and calibration');

-- Insert Sample Bookings
INSERT INTO service_bookings (booking_id, vehicle_id, mechanic_id, service_id, service_date, status, description) VALUES
(401, 101, 201, 301, '2026-08-30', 'COMPLETED', 'Regular maintenance service'),
(402, 102, 202, 302, '2026-08-31', 'IN_PROGRESS', 'Engine light warning inspection');

-- Insert Completed Service Records
INSERT INTO service_records (record_id, booking_id, completion_date, remarks, total_service_cost) VALUES
(501, 401, '2026-08-30', 'Engine oil changed, filter replaced, water wash done', 1500.00);

-- Insert Service Details
INSERT INTO service_details (detail_id, record_id, service_id, quantity, unit_price, subtotal) VALUES
(601, 501, 301, 1, 1500.00, 1500.00);

-- Insert Bills
INSERT INTO bills (bill_id, record_id, service_cost, parts_cost, labor_cost, tax, discount, total_amount, payment_status) VALUES
(701, 501, 1500.00, 300.00, 200.00, 360.00, 100.00, 2260.00, 'PAID');
