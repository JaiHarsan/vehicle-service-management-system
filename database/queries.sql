-- ===================================================
-- VEHICLE SERVICE MANAGEMENT SYSTEM - USEFUL SQL QUERIES
-- ===================================================

USE vehicle_service;

-- 1. SELECT ALL RECORDS FROM TABLES
SELECT * FROM customers;
SELECT * FROM vehicles;
SELECT * FROM mechanics;
SELECT * FROM services;
SELECT * FROM service_bookings;
SELECT * FROM service_records;
SELECT * FROM service_details;
SELECT * FROM bills;

-- 2. CUSTOMER CRUD
-- Create Customer
INSERT INTO customers (name, phone, email) VALUES ('Anand', '9000011111', 'anand@example.com');
-- Read Customer
SELECT * FROM customers WHERE customer_id = 1;
-- Update Customer
UPDATE customers SET phone = '9999988888', email = 'anand_new@example.com' WHERE customer_id = 1;
-- Delete Customer
DELETE FROM customers WHERE customer_id = 99;

-- 3. VEHICLE CRUD & JOIN
SELECT v.vehicle_id, v.registration_number, v.brand, v.model, v.vehicle_type, c.name AS owner_name, c.phone
FROM vehicles v
JOIN customers c ON v.customer_id = c.customer_id;

-- 4. MASTER SERVICE BOOKING FULL JOIN QUERY (VIVA REVIEW DEMONSTRATION)
-- Demonstrates JOIN, Primary Keys, Foreign Keys, and 1:N Relationships
SELECT
    b.booking_id,
    c.name AS customer_name,
    c.phone AS customer_phone,
    v.registration_number,
    v.brand,
    v.model,
    s.service_name,
    s.base_price,
    COALESCE(m.name, 'Unassigned') AS mechanic_name,
    b.service_date,
    b.status AS booking_status
FROM service_bookings b
JOIN vehicles v ON b.vehicle_id = v.vehicle_id
JOIN customers c ON v.customer_id = c.customer_id
JOIN services s ON b.service_id = s.service_id
LEFT JOIN mechanics m ON b.mechanic_id = m.mechanic_id
ORDER BY b.booking_id DESC;

-- 5. COMPLETED SERVICE HISTORY & BILLING SUMMARY QUERY
SELECT
    r.record_id,
    b.booking_id,
    v.registration_number,
    c.name AS customer_name,
    r.completion_date,
    r.remarks,
    bi.service_cost,
    bi.parts_cost,
    bi.labor_cost,
    bi.tax,
    bi.discount,
    bi.total_amount,
    bi.payment_status
FROM service_records r
JOIN service_bookings b ON r.booking_id = b.booking_id
JOIN vehicles v ON b.vehicle_id = v.vehicle_id
JOIN customers c ON v.customer_id = c.customer_id
LEFT JOIN bills bi ON r.record_id = bi.record_id;
