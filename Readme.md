# Vehicle Service Management System

A full-stack **Vehicle Service Management System** built with **Java 17, Spring Boot, JDBC, MySQL, HTML, Vanilla CSS, and Vanilla JavaScript (Fetch API)**.

The system automates the complete operational workflow of a professional vehicle service center, including customer management, vehicle registration, mechanic assignment, catalog services, booking lifecycle tracking, post-service records, automated billing, and real-time dashboard analytics.

---

## 1. Project Overview

Vehicle service centers require structured tracking of customers, vehicles, mechanics, service offerings, appointments, completed records, and invoices. Manual tracking leads to duplicate vehicle registrations, lost customer records, scheduling conflicts, and inaccurate billing.

This application resolves these challenges by providing a modern web interface powered by a Spring Boot REST API backend and a persistent MySQL database via direct JDBC repositories.

### Key Objectives
1. **Customer Management**: Register, view, search, edit, and delete customer profiles.
2. **Vehicle Management**: Register vehicles with unique license plates linked directly to customer accounts.
3. **Mechanic Management**: Manage mechanic specializations and track live availability (`AVAILABLE` vs `BUSY`).
4. **Service Catalog**: Maintain service offerings and base pricing using `BigDecimal`.
5. **Service Bookings**: Create and track appointment lifecycles (`BOOKED` ➔ `ASSIGNED` ➔ `IN_PROGRESS` ➔ `COMPLETED` / `CANCELLED`).
6. **Automated Mechanic Scheduling**: Automatically transition mechanic status to `BUSY` upon assignment and release them back to `AVAILABLE` upon completion.
7. **Service Records**: Maintain detailed post-service documentation and service item composition.
8. **Billing & Payments**: Generate automated invoices with parts cost, labor cost, configurable tax (GST), and discounts. Track payment status (`PENDING` ➔ `PAID`).
9. **Dashboard Analytics**: Real-time operational summary metrics, active booking counters, and status distributions.

---

## 2. Technology Stack

| Component | Technology | Description |
| :--- | :--- | :--- |
| **Frontend** | HTML5, Vanilla CSS, Vanilla JavaScript (ES6+), Fetch API | Responsive UI with custom design system, dark-mode glassmorphism accents, dynamic micro-interactions, modals, and alerts. |
| **Backend Framework** | Java 17, Spring Boot 3.2.5, Spring Web | Restful API endpoints, Spring Web MVC controllers, global exception handling (`@ExceptionHandler`). |
| **Persistence** | JDBC (`JdbcTemplate` / Direct JDBC Repositories) | Low-level SQL execution, `PreparedStatement`, `GeneratedKeyHolder` for explicit data control without ORM overhead. |
| **Database** | MySQL 8.0+ | Relational schema with foreign key constraints, indexing, and transactional consistency. |
| **Build & Tooling** | Apache Maven | Project build, dependency management, and lifecycle execution (`mvn spring-boot:run`). |

---

## 3. System Architecture

The application uses a 4-tier layered architecture:

```text
  [ Browser / Web UI ]
  HTML5 / CSS3 / Vanilla JavaScript (Fetch API)
          │
          ▼  HTTP / REST JSON
  [ REST Controller Layer ]
  CustomerController, VehicleController, MechanicController,
  ServiceOfferingController, BookingController, ServiceRecordController, BillingController
          │
          ▼  Java Method Calls
  [ Service / Business Layer ]
  CustomerService, VehicleService, MechanicService,
  ServiceManagementService, BookingService, ServiceRecordService, BillingService
          │
          ▼  Repository Interfaces
  [ Data Access Layer ]
  JdbcCustomerRepository, JdbcVehicleRepository, JdbcMechanicRepository,
  JdbcServiceRepository, JdbcServiceBookingRepository, JdbcServiceRecordRepository, JdbcBillRepository
          │
          ▼  JDBC / PreparedStatement
  [ Database Layer ]
  MySQL Database (vehicle_service)
```

---

## 4. Operational Workflow

The application enforces an end-to-end lifecycle:

```text
  CUSTOMER REGISTRATION
           │
           ▼
    VEHICLE LINKING
           │
           ▼
     SERVICE BOOKING ──► Select Service & Appointment Date (Status: BOOKED)
           │
           ▼
   MECHANIC ASSIGNMENT ──► Assign Available Mechanic (Mechanic becomes BUSY, Status: ASSIGNED)
           │
           ▼
    SERVICE EXECUTION ──► Start Service (Status: IN_PROGRESS)
           │
           ▼
   SERVICE COMPLETION ──► Complete Service (Mechanic returns to AVAILABLE, Status: COMPLETED)
           │
           ▼
  CREATE SERVICE RECORD ──► Record Completion Date, Remarks & Extra Parts Cost
           │
           ▼
     GENERATE BILL ──► Subtotal = Service Cost + Parts Cost + Labor Cost
           │           Total = Subtotal + Tax (18% GST) - Discount
           ▼
     MARK AS PAID ──► Payment Status becomes PAID
           │
           ▼
  DASHBOARD METRICS ──► System Stats & Revenue Updates
```

---

## 5. Project Directory Structure

```text
Vehicle Service Management System/
├── src/
│   ├── main/
│   │   ├── java/com/vehicleservice/
│   │   │   ├── config/              # Application & Database Configuration
│   │   │   ├── controller/          # Spring Boot REST Controllers
│   │   │   ├── exception/           # Custom Domain Exceptions & Error Handlers
│   │   │   ├── model/               # Data Models & Enums
│   │   │   ├── repository/          # Repository Interfaces & JDBC Implementations
│   │   │   ├── service/             # Business Logic & Service Layer
│   │   │   └── util/                # Input Validators & Utility Classes
│   │   │
│   │   └── resources/
│   │       ├── application.properties
│   │       └── static/              # Web Frontend (Served by Spring Boot)
│   │           ├── index.html       # Dashboard Overview
│   │           ├── css/style.css    # Core Styling System
│   │           ├── js/app.js        # Global API & UI Helpers
│   │           └── pages/           # Module Views
│   │               ├── customers.html
│   │               ├── vehicles.html
│   │               ├── mechanics.html
│   │               ├── services.html
│   │               ├── bookings.html
│   │               ├── service-records.html
│   │               └── billing.html
│   │
│   └── test/java/com/vehicleservice/  # Integration Tests
│
├── database/                        # Database Scripts
│   ├── schema.sql                   # MySQL DDL Table Schema
│   └── sample_data.sql              # Initial Sample Dataset
│
├── .gitignore                       # Git Exclusion File
├── pom.xml                          # Maven Project Definition
└── README.md                        # Documentation
```

---

## 6. REST API Overview

| Module | HTTP Method | Endpoint | Description |
| :--- | :--- | :--- | :--- |
| **Customer** | `GET` | `/api/customers` | List all customers |
| | `GET` | `/api/customers/{id}` | Get customer details by ID |
| | `POST` | `/api/customers` | Register a new customer |
| | `PUT` | `/api/customers/{id}` | Update existing customer details |
| | `DELETE` | `/api/customers/{id}` | Delete customer (if no linked vehicles) |
| **Vehicle** | `GET` | `/api/vehicles` | List all vehicles |
| | `GET` | `/api/vehicles/{id}` | Get vehicle details by ID |
| | `GET` | `/api/vehicles?customerId={id}` | Get vehicles belonging to a customer |
| | `POST` | `/api/vehicles` | Register a new vehicle |
| | `PUT` | `/api/vehicles/{id}` | Update vehicle information |
| | `DELETE` | `/api/vehicles/{id}` | Delete vehicle |
| **Mechanic** | `GET` | `/api/mechanics` | List all mechanics |
| | `GET` | `/api/mechanics/{id}` | Get mechanic by ID |
| | `POST` | `/api/mechanics` | Add a new mechanic |
| | `PUT` | `/api/mechanics/{id}` | Update mechanic profile |
| | `PATCH` | `/api/mechanics/{id}/availability` | Toggle mechanic availability (`AVAILABLE`/`BUSY`) |
| | `DELETE` | `/api/mechanics/{id}` | Remove mechanic |
| **Service** | `GET` | `/api/services` | List catalog services |
| | `GET` | `/api/services/{id}` | Get catalog service details |
| | `POST` | `/api/services` | Add new catalog service |
| | `PUT` | `/api/services/{id}` | Update service offering/base price |
| | `DELETE` | `/api/services/{id}` | Remove service offering |
| **Booking** | `GET` | `/api/bookings` | List all service bookings |
| | `GET` | `/api/bookings/{id}` | Get booking details |
| | `GET` | `/api/bookings/vehicle/{vehicleId}` | Get bookings for a specific vehicle |
| | `POST` | `/api/bookings` | Create new service booking (`BOOKED`) |
| | `PUT` | `/api/bookings/{id}/assign?mechanicId={mId}` | Assign available mechanic (`ASSIGNED`) |
| | `PUT` | `/api/bookings/{id}/start` | Start service execution (`IN_PROGRESS`) |
| | `PUT` | `/api/bookings/{id}/complete` | Complete service (`COMPLETED`) |
| | `PUT` | `/api/bookings/{id}/cancel` | Cancel booking (`CANCELLED`) |
| | `DELETE` | `/api/bookings/{id}` | Delete booking |
| **Service Record** | `GET` | `/api/service-records` | List all completed service records |
| | `GET` | `/api/service-records/{id}` | Get service record by ID |
| | `GET` | `/api/service-records/booking/{bookingId}` | Get record for a booking |
| | `GET` | `/api/service-records/vehicle/{vehicleId}` | Get complete vehicle service history |
| | `GET` | `/api/service-records/{id}/details` | Get record composition line items |
| | `POST` | `/api/service-records?extraPartsCost={cost}` | Create service record for completed booking |
| **Billing** | `GET` | `/api/bills` | List all bills |
| | `GET` | `/api/bills/{id}` | Get bill details by ID |
| | `GET` | `/api/bills/record/{recordId}` | Get bill by service record ID |
| | `POST` | `/api/bills` | Generate invoice for service record |
| | `PUT` | `/api/bills/{id}/pay` | Mark bill status as `PAID` |

---

## 7. Database Setup & Configuration

### Prerequisites
- Java 17 JDK
- MySQL Server 8.0+
- Apache Maven 3.8+

### Database Initialization
1. Start MySQL Server and create database:
   ```sql
   CREATE DATABASE vehicle_service;
   ```
2. Execute DDL Schema script:
   ```bash
   mysql -u root -p vehicle_service < database/schema.sql
   ```
3. Load initial sample data:
   ```bash
   mysql -u root -p vehicle_service < database/sample_data.sql
   ```

### Configuration (`application.properties`)
Create or edit `src/main/resources/application.properties` with your local database credentials or set environment variables:
```properties
spring.application.name=vehicle-service-management-system
server.port=8080

spring.datasource.url=${DB_URL:jdbc:mysql://localhost:3306/vehicle_service?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC}
spring.datasource.username=${DB_USERNAME:root}
spring.datasource.password=${DB_PASSWORD:your_password_here}
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

---

## 8. Running the Application

### Command Line
Navigate to the project root directory and execute:
```bash
mvn clean spring-boot:run
```

Once started, access the Web Interface in your browser:
- **Dashboard**: `http://localhost:8080/`
- **Customers**: `http://localhost:8080/pages/customers.html`
- **Vehicles**: `http://localhost:8080/pages/vehicles.html`
- **Mechanics**: `http://localhost:8080/pages/mechanics.html`
- **Services**: `http://localhost:8080/pages/services.html`
- **Bookings**: `http://localhost:8080/pages/bookings.html`
- **Service Records**: `http://localhost:8080/pages/service-records.html`
- **Billing**: `http://localhost:8080/pages/billing.html`

---

## 9. Verification & Testing

### Test Suite Execution
The repository includes comprehensive automated verification suites covering end-to-end integration, error handling, status code responses, and static web asset serving.

```bash
powershell -ExecutionPolicy Bypass -File test_phase9g.ps1
```

### Initial Sample Dataset Baseline
- **Customers**: 5 (`1` to `5`)
- **Vehicles**: 5 (`101` to `105`)
- **Mechanics**: 4 (`201` to `204`)
- **Services**: 4 (`301` to `304`)
- **Bookings**: 4 (`401` to `404`)
- **Service Records**: 3 (`501` to `503`)
- **Bills**: 3 (`701` to `703`)

---

## 10. License & Maintenance

Developed as part of the **Vehicle Service Management System** project. All rights reserved.
