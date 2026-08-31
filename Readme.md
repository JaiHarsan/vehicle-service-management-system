# Vehicle Service Management System

A production-ready Core Java console application for managing vehicle service center operations, customer records, vehicle details, mechanic assignments, service bookings, completed work history, and financial billing backed by MySQL database persistence via JDBC.

---

## Technical Stack
* **Language**: Java 17 LTS
* **Build System**: Apache Maven 3.9+
* **Database**: MySQL Server 8.0+ / 9.0+
* **Persistence API**: JDBC (`com.mysql:mysql-connector-j:8.3.0`)
* **Version Control**: Git & GitHub

---

## Application Architecture

```text
                 USER INTERACTION
                        │
                        ▼
               CONSOLE MENU LAYER
                   (ConsoleUI)
                        │
                        ▼
                  SERVICE LAYER
   (CustomerService, VehicleService, BookingService, etc.)
                        │
                        ▼
              REPOSITORY INTERFACES
  (CustomerRepository, VehicleRepository, BillRepository, etc.)
                        │
                        ▼
             JDBC DAO IMPLEMENTATIONS
  (JdbcCustomerRepository, JdbcVehicleRepository, etc.)
                        │
                        ▼
                    JDBC API
               (PreparedStatement)
                        │
                        ▼
                 MYSQL DATABASE
             (vehicle_service DB)
```

---

## Database Tables & Schema Relationships

```text
customers (1) ─────── (N) vehicles (1) ─────── (N) service_bookings (1) ─────── (1) service_records
                                                       │                                  │
                                                       ├── (N:1) services                 ├── (1:N) service_details
                                                       └── (N:1) mechanics                └── (1:1) bills
```

1. `customers`: `customer_id` (PK), `name`, `phone`, `email`
2. `vehicles`: `vehicle_id` (PK), `registration_number` (UNIQUE), `brand`, `model`, `vehicle_type`, `customer_id` (FK)
3. `mechanics`: `mechanic_id` (PK), `name`, `phone`, `specialization`, `availability`
4. `services`: `service_id` (PK), `service_name`, `base_price`, `description`
5. `service_bookings`: `booking_id` (PK), `vehicle_id` (FK), `mechanic_id` (FK nullable), `service_id` (FK), `service_date`, `status`, `description`
6. `service_records`: `record_id` (PK), `booking_id` (FK UNIQUE), `completion_date`, `remarks`, `total_service_cost`
7. `service_details`: `detail_id` (PK), `record_id` (FK), `service_id` (FK), `quantity`, `unit_price`, `subtotal`
8. `bills`: `bill_id` (PK), `record_id` (FK UNIQUE), `service_cost`, `parts_cost`, `labor_cost`, `tax`, `discount`, `total_amount`, `payment_status`

---

## Local Setup & Quickstart Guide

### 1. Database Setup
Start MySQL Server locally and initialize database:
```bash
mysql -u root -p
```
Run initialization query:
```sql
CREATE DATABASE IF NOT EXISTS vehicle_service;
USE vehicle_service;
```
Import schema and sample data:
```bash
mysql -u root -p vehicle_service < database/schema.sql
mysql -u root -p vehicle_service < database/sample_data.sql
```

### 2. Configure Credentials (Excluded from Git)
Create or update `application.properties` in the project root:
```properties
db.url=jdbc:mysql://localhost:3306/vehicle_service?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
db.username=root
db.password=YOUR_LOCAL_MYSQL_PASSWORD
```
Alternatively, set environment variables: `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`.

### 3. Build & Run Application
Compile project:
```powershell
mvn clean compile
```
Execute console application:
```powershell
mvn compile exec:java
```

---

## Viva & Project Review Q&A Guide

### Q1: Why did you use JDBC instead of Spring Boot or Hibernate?
**Answer**: For this project review, using native JDBC allows demonstrating foundational understanding of relational database connectivity, explicit SQL queries (`PreparedStatement`), ResultSet mapping, resource management (`try-with-resources`), and manual transaction handling before introducing high-level ORM frameworks.

### Q2: Why did you separate Repository Interfaces from JDBC Implementations?
**Answer**: It enforces Abstraction and Dependency Inversion. The Service layer (`CustomerService`, `VehicleService`) depends strictly on repository interfaces (`CustomerRepository`). This allowed us to build and verify Phase 1 with In-Memory storage (`InMemoryCustomerRepository`) and seamlessly transition to Phase 2 MySQL persistence (`JdbcCustomerRepository`) without modifying a single line of business logic in the service layer.

### Q3: Why did you use `PreparedStatement` instead of standard `Statement`?
**Answer**: `PreparedStatement` uses parameterized queries (`?` placeholders), which pre-compiles the SQL structure on the database server. This completely eliminates SQL Injection vulnerabilities and improves execution performance.

### Q4: Why use `BigDecimal` for monetary amounts instead of `double`?
**Answer**: Floating-point primitives (`double`/`float`) suffer from IEEE 754 binary representation rounding inaccuracies. `BigDecimal` guarantees exact decimal precision for financial billing, tax calculations, and invoice totals.
