# Vehicle Service Management System

A console-based **Vehicle Service Management System** developed using **Java, JDBC, SQL, and MySQL**.

The system is designed to automate the major operations of a vehicle service center, including customer management, vehicle registration, mechanic management, service booking, service tracking, service history, and billing.

--- 

## 1. Project Overview

Vehicle service centers need to maintain information about customers, vehicles, mechanics, services, bookings, service records, and payments.

Managing these records manually can result in:

* Duplicate vehicle registrations
* Loss of customer information
* Difficulty maintaining service history
* Mechanic scheduling conflicts
* Incorrect billing calculations
* Difficulty searching and updating records

To solve these problems, this project provides a centralized console-based application using Java and MySQL.

The Java application communicates with the MySQL database using **JDBC**.

---

## 2. Objectives

The main objectives of this project are:

1. Manage customer information.
2. Register and manage vehicles.
3. Manage mechanics and their availability.
4. Maintain a service catalog.
5. Create and manage service bookings.
6. Assign available mechanics.
7. Track service status.
8. Maintain completed service history.
9. Generate service bills.
10. Track payment status.
11. Store application data permanently in MySQL.
12. Demonstrate Java OOP concepts.
13. Demonstrate JDBC and SQL concepts.
14. Implement a layered software architecture.

---

## 3. Technologies Used

| Technology                  | Purpose                         |
| --------------------------- | ------------------------------- |
| Java 17                     | Application development         |
| Object-Oriented Programming | Application design              |
| Maven                       | Build and dependency management |
| JDBC                        | Java-MySQL connectivity         |
| MySQL                       | Relational database             |
| SQL                         | Database operations             |
| Git                         | Version control                 |
| GitHub                      | Source code hosting             |
| VS Code                     | Development environment         |

---

## 4. System Architecture

The application follows a layered architecture.

```text
                    USER
                      |
                      v
              +---------------+
              |  Console UI    |
              +-------+-------+
                      |
                      v
              +---------------+
              | Service Layer |
              +-------+-------+
                      |
                      v
              +---------------+
              |  Repository   |
              |   Interface   |
              +-------+-------+
                      |
                      v
              +---------------+
              | JDBC Repository|
              +-------+-------+
                      |
                      v
              +---------------+
              |     JDBC      |
              +-------+-------+
                      |
                      v
              +---------------+
              |     MySQL     |
              +---------------+
```

### Application Flow

```text
User
 ↓
ConsoleUI
 ↓
Service Layer
 ↓
Repository Interface
 ↓
JDBC Repository
 ↓
JDBC API
 ↓
MySQL Database
```

---

## 5. Layer Responsibilities

### Console UI Layer

Responsible for:

* Displaying menus
* Reading user input
* Menu navigation
* Displaying application results
* Handling console interaction

Main classes:

```text
ConsoleUI.java
ConsoleInput.java
```

---

### Service Layer

Responsible for:

* Business logic
* Validation
* Customer operations
* Vehicle operations
* Mechanic management
* Booking workflow
* Service status transitions
* Billing calculations

Main classes:

```text
CustomerService.java
VehicleService.java
MechanicService.java
ServiceManagementService.java
BookingService.java
ServiceRecordService.java
BillingService.java
```

---

### Repository Layer

The Repository layer separates business logic from data storage.

Repository interfaces include:

```text
CustomerRepository
VehicleRepository
MechanicRepository
ServiceRepository
ServiceBookingRepository
ServiceRecordRepository
ServiceDetailRepository
BillRepository
```

There are two types of implementations:

```text
In-Memory Repository
        |
        +----------------------+
        |                      |
        v                      v
InMemoryCustomerRepository   JdbcCustomerRepository
```

The in-memory repositories were used during the initial development phase, while JDBC repositories provide permanent MySQL persistence.

---

### JDBC Persistence Layer

The JDBC repositories communicate with MySQL using:

```text
DriverManager
Connection
PreparedStatement
ResultSet
Statement.RETURN_GENERATED_KEYS
```

---

### Database Layer

MySQL provides persistent storage for application data.

Database:

```text
vehicle_service
```

---

# 6. Project Structure

```text
vehicle-service-management-system/
│
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com/
│   │           └── vehicleservice/
│   │               │
│   │               ├── config/
│   │               │   └── DatabaseConnection.java
│   │               │
│   │               ├── console/
│   │               │   ├── ConsoleInput.java
│   │               │   └── ConsoleUI.java
│   │               │
│   │               ├── exception/
│   │               │   ├── BookingNotFoundException.java
│   │               │   ├── CustomerNotFoundException.java
│   │               │   ├── DuplicateVehicleException.java
│   │               │   ├── InvalidBookingException.java
│   │               │   ├── MechanicNotFoundException.java
│   │               │   └── VehicleNotFoundException.java
│   │               │
│   │               ├── main/
│   │               │   ├── Step4JdbcTest.java
│   │               │   └── VehicleServiceApplication.java
│   │               │
│   │               ├── model/
│   │               │   ├── Availability.java
│   │               │   ├── Bill.java
│   │               │   ├── BookingStatus.java
│   │               │   ├── Customer.java
│   │               │   ├── Mechanic.java
│   │               │   ├── PaymentStatus.java
│   │               │   ├── Person.java
│   │               │   ├── Service.java
│   │               │   ├── ServiceBooking.java
│   │               │   ├── ServiceDetail.java
│   │               │   ├── ServiceRecord.java
│   │               │   ├── Specialization.java
│   │               │   ├── Vehicle.java
│   │               │   └── VehicleType.java
│   │               │
│   │               ├── repository/
│   │               │   ├── BillRepository.java
│   │               │   ├── CustomerRepository.java
│   │               │   ├── InMemoryBillRepository.java
│   │               │   ├── InMemoryCustomerRepository.java
│   │               │   ├── InMemoryMechanicRepository.java
│   │               │   ├── InMemoryServiceBookingRepository.java
│   │               │   ├── InMemoryServiceDetailRepository.java
│   │               │   ├── InMemoryServiceRecordRepository.java
│   │               │   ├── InMemoryServiceRepository.java
│   │               │   ├── InMemoryVehicleRepository.java
│   │               │   ├── JdbcBillRepository.java
│   │               │   ├── JdbcCustomerRepository.java
│   │               │   ├── JdbcMechanicRepository.java
│   │               │   ├── JdbcServiceBookingRepository.java
│   │               │   ├── JdbcServiceDetailRepository.java
│   │               │   ├── JdbcServiceRecordRepository.java
│   │               │   ├── JdbcServiceRepository.java
│   │               │   ├── JdbcVehicleRepository.java
│   │               │   ├── MechanicRepository.java
│   │               │   ├── ServiceBookingRepository.java
│   │               │   ├── ServiceDetailRepository.java
│   │               │   ├── ServiceRecordRepository.java
│   │               │   ├── ServiceRepository.java
│   │               │   └── VehicleRepository.java
│   │               │
│   │               ├── service/
│   │               │   ├── BillingService.java
│   │               │   ├── BookingService.java
│   │               │   ├── CustomerService.java
│   │               │   ├── MechanicService.java
│   │               │   ├── ServiceManagementService.java
│   │               │   ├── ServiceRecordService.java
│   │               │   └── VehicleService.java
│   │               │
│   │               └── util/
│   │                   └── InputValidator.java
│   │
│   └── test/
│       └── java/
│           └── com/
│               └── vehicleservice/
│                   ├── Step3WorkflowTest.java
│                   └── Step4JdbcTest.java
│
├── database/
│   ├── schema.sql
│   ├── sample_data.sql
│   └── queries.sql
│
├── application.properties
├── .gitignore
├── pom.xml
└── README.md
```

---

# 7. Functional Modules

The project contains the following major modules:

```text
1. Customer Management
2. Vehicle Management
3. Mechanic Management
4. Service Catalog
5. Service Booking
6. Mechanic Assignment
7. Service Status Tracking
8. Service Records
9. Billing
10. Payment Management
```

---

# 8. Customer Management

The system supports:

* Add customer
* View all customers
* Search customer by ID
* Update customer
* Delete customer

Customer information is stored in the:

```text
customers
```

table.

---

# 9. Vehicle Management

The system supports:

* Add vehicle
* Link vehicle to customer
* View all vehicles
* Search vehicle by registration number
* View vehicles belonging to a customer
* Delete vehicle

Each vehicle belongs to a customer.

Relationship:

```text
Customer
   |
   | 1 : N
   |
Vehicle
```

The registration number is unique.

---

# 10. Mechanic Management

The system supports:

* Add mechanic
* Assign specialization
* View mechanics
* Update mechanic availability

Mechanic availability is represented using an enum:

```text
AVAILABLE
BUSY
```

When a mechanic is assigned to an active booking:

```text
AVAILABLE
     |
     v
    BUSY
```

After completing the service:

```text
BUSY
  |
  v
AVAILABLE
```

This prevents the same mechanic from being assigned to multiple active services.

---

# 11. Service Catalog

The service catalog contains the services offered by the service center.

Operations include:

* Add service
* View services
* Update service price

Examples:

```text
Oil Change
Brake Service
Engine Service
Full Service
```

Service prices are represented using:

```java
BigDecimal
```

---

# 12. Service Booking

A customer can book a service for a vehicle.

The booking workflow is:

```text
BOOKED
   |
   v
ASSIGNED
   |
   v
IN_PROGRESS
   |
   v
COMPLETED
```

Each state represents a stage of the service lifecycle.

---

## Booking Status

The application uses:

```text
BOOKED
ASSIGNED
IN_PROGRESS
COMPLETED
```

The Service layer validates whether a status transition is allowed.

Invalid transitions result in:

```text
InvalidBookingException
```

---

# 13. Mechanic Assignment

When a booking is created, it initially has the status:

```text
BOOKED
```

An available mechanic can then be assigned.

The booking becomes:

```text
ASSIGNED
```

At the same time, the mechanic becomes:

```text
BUSY
```

After completion, the mechanic is automatically released:

```text
AVAILABLE
```

---

# 14. Service Records

After completing a service, a service record is created.

A service record represents the history of work performed on a vehicle.

A service record can contain multiple service details.

Example:

```text
Service Record
      |
      +---- Engine Oil Change
      |
      +---- Oil Filter Replacement
      |
      +---- Brake Inspection
```

This demonstrates **composition** in the object-oriented design.

---

# 15. Billing

The billing module generates invoices for completed services.

The billing calculation is:

```text
Service Cost
     +
Parts Cost
     +
Labor Cost
     =
Subtotal
```

Then:

```text
Tax = Subtotal × Tax Rate
```

Final calculation:

```text
Total = Subtotal + Tax - Discount
```

Payment status:

```text
PENDING
   |
   v
PAID
```

---

# 16. Database Design

Database name:

```text
vehicle_service
```

The main tables are:

```text
customers
vehicles
mechanics
services
service_bookings
service_records
service_details
bills
```

---

# 17. Entity Relationships

```text
customers
    |
    | 1 : N
    v
vehicles
    |
    | 1 : N
    v
service_bookings
    |
    +------------ services
    |
    +------------ mechanics
    |
    v
service_records
    |
    | 1 : N
    v
service_details
    |
    v
bills
```

---

# 18. Database Tables

## customers

Primary key:

```text
customer_id
```

Stores customer information.

---

## vehicles

Primary key:

```text
vehicle_id
```

Foreign key:

```text
customer_id
```

Unique field:

```text
registration_number
```

Relationship:

```text
vehicles.customer_id
        ↓
customers.customer_id
```

---

## mechanics

Primary key:

```text
mechanic_id
```

Stores mechanic details and availability.

---

## services

Primary key:

```text
service_id
```

Stores available service offerings and prices.

---

## service_bookings

Primary key:

```text
booking_id
```

Foreign keys:

```text
vehicle_id
mechanic_id
service_id
```

Stores service appointments and their status.

---

## service_records

Primary key:

```text
record_id
```

Foreign key:

```text
booking_id
```

The booking relationship is unique because one completed booking creates one service record.

---

## service_details

Primary key:

```text
detail_id
```

Foreign keys:

```text
record_id
service_id
```

Stores individual service line items.

---

## bills

Primary key:

```text
bill_id
```

Foreign key:

```text
record_id
```

Stores billing information.

Monetary values use:

```text
DECIMAL(10,2)
```

---

# 19. SQL Concepts Used

The project demonstrates:

* CREATE DATABASE
* CREATE TABLE
* INSERT
* SELECT
* UPDATE
* DELETE
* WHERE
* JOIN
* PRIMARY KEY
* FOREIGN KEY
* UNIQUE
* NOT NULL
* DECIMAL
* AUTO_INCREMENT

---

# 20. Primary Key

A primary key uniquely identifies each record.

Example:

```text
customer_id
```

Two customers cannot have the same primary key.

---

# 21. Foreign Key

A foreign key creates a relationship between tables.

Example:

```text
vehicles.customer_id
```

references:

```text
customers.customer_id
```

This maintains referential integrity.

---

# 22. UNIQUE Constraint

The vehicle registration number is unique.

Example:

```text
TN01AB1234
```

cannot be registered for two different vehicles.

---

# 23. JOIN

JOIN is used to retrieve related information from multiple tables.

For example, a booking can be displayed with:

```text
Customer Name
Vehicle Registration Number
Service Name
Mechanic Name
Booking Status
```

These values can come from multiple tables.

---

# 24. JDBC

JDBC stands for:

```text
Java Database Connectivity
```

It is a Java API that allows Java applications to communicate with relational databases.

In this project:

```text
Java
  |
 JDBC
  |
MySQL
```

---

# 25. JDBC Components Used

## DriverManager

Used to create database connections.

```text
DriverManager
      |
      v
Connection
```

---

## Connection

Represents the active connection between Java and MySQL.

---

## PreparedStatement

Used for parameterized SQL queries.

Example:

```sql
SELECT *
FROM customers
WHERE customer_id = ?
```

The `?` is replaced with a parameter.

### Why PreparedStatement?

* Prevents SQL injection
* Safely handles user input
* Separates SQL structure from data
* Makes queries easier to manage

---

## ResultSet

Used to process data returned from SELECT queries.

Example:

```text
Database
   |
   v
ResultSet
   |
   v
Java Object
```

Rows are processed using:

```java
rs.next()
```

---

## Generated Keys

MySQL uses auto-increment IDs.

JDBC can retrieve the generated ID using:

```java
Statement.RETURN_GENERATED_KEYS
```

and:

```java
getGeneratedKeys()
```

---

## Try-with-resources

Used to automatically close:

```text
Connection
PreparedStatement
ResultSet
```

This helps prevent database resource leaks.

---

# 26. Java OOP Concepts

The project demonstrates the following OOP concepts:

```text
Encapsulation
Inheritance
Abstraction
Polymorphism
Composition
Interfaces
```

---

# 27. Encapsulation

Encapsulation means wrapping data and methods together and restricting direct access to internal data.

Example:

```java
private String name;
```

The field is private and accessed using methods such as:

```java
getName()
setName()
```

### Why?

To protect object state and control how data is modified.

---

# 28. Inheritance

The project contains:

```text
             Person
            /      \
           /        \
      Customer     Mechanic
```

`Customer` and `Mechanic` inherit common properties from `Person`.

### Why?

To reuse common properties and behavior.

---

# 29. Abstraction

`Person` is an abstract class.

Repository interfaces also provide abstraction.

### Why?

Abstraction hides implementation details and exposes only the required behavior.

---

# 30. Polymorphism

Polymorphism allows an interface reference to work with different implementations.

Example:

```text
CustomerRepository
        |
        +----------------------------+
        |                            |
        v                            v
InMemoryCustomerRepository   JdbcCustomerRepository
```

The Service layer can interact with:

```text
CustomerRepository
```

without needing to know which implementation is being used.

### Why?

It reduces coupling and makes the application easier to extend.

---

# 31. Composition

A `ServiceRecord` contains multiple `ServiceDetail` objects.

```text
ServiceRecord
      |
      +---- ServiceDetail
      |
      +---- ServiceDetail
      |
      +---- ServiceDetail
```

### Why?

It represents the real-world relationship where a service record consists of multiple service items.

---

# 32. Interfaces

Repository interfaces define contracts such as:

```text
save()
findById()
findAll()
update()
delete()
```

The actual implementation is provided by repository classes.

### Why?

Interfaces provide abstraction and allow different storage implementations.

---

# 33. Enum

The project uses enums for fixed values.

Examples:

```text
BookingStatus
PaymentStatus
Availability
VehicleType
Specialization
```

Example:

```text
BOOKED
ASSIGNED
IN_PROGRESS
COMPLETED
```

### Why use Enum?

Enums provide:

* Type safety
* Restricted valid values
* Better readability
* Compile-time checking

---

# 34. BigDecimal

`BigDecimal` is used for monetary values.

Examples:

```text
Service price
Parts cost
Labor cost
Tax
Discount
Total
```

### Why?

`double` can introduce floating-point rounding errors.

`BigDecimal` provides accurate decimal arithmetic, which is important for financial calculations.

---

# 35. Exception Handling

The project uses custom exceptions.

Examples:

```text
CustomerNotFoundException
VehicleNotFoundException
MechanicNotFoundException
BookingNotFoundException
DuplicateVehicleException
InvalidBookingException
```

### Why?

Custom exceptions make errors easier to identify and allow the application to handle different failure conditions clearly.

---

# 36. Validation

Input validation is handled using:

```text
InputValidator.java
```

Validation helps ensure that:

* Required fields are not empty
* IDs are valid
* Prices are positive
* Vehicle registration numbers are valid
* Invalid booking transitions are rejected

---

# 37. Security

Database credentials should not be hardcoded into Java source files.

Configuration is stored locally in:

```text
application.properties
```

Example:

```properties
db.url=jdbc:mysql://localhost:3306/vehicle_service
db.username=root
db.password=YOUR_PASSWORD
```

The actual password should not be committed to GitHub.

The configuration file should be included in `.gitignore`.

---

# 38. Maven

Maven is used as the project's build and dependency management tool.

The project contains:

```text
pom.xml
```

The `pom.xml` manages:

* Java version
* MySQL JDBC dependency
* Maven plugins
* Project build configuration

---

# 39. Prerequisites

Before running the project, install:

```text
Java 17
Maven
MySQL
Git
VS Code
```

---

# 40. Verify Java

Open the VS Code terminal and run:

```powershell
java -version
```

Then:

```powershell
javac -version
```

Expected Java version:

```text
17.x
```

---

# 41. Verify Maven

Run:

```powershell
mvn -version
```

If Maven is correctly configured, its version information will be displayed.

---

# 42. Verify MySQL

Run:

```powershell
mysql --version
```

---

# 43. Start MySQL

Open MySQL using:

```powershell
mysql -u root -p
```

Enter your MySQL password when prompted.

---

# 44. Create Database

Inside MySQL:

```sql
CREATE DATABASE IF NOT EXISTS vehicle_service;
```

Select the database:

```sql
USE vehicle_service;
```

Verify:

```sql
SELECT DATABASE();
```

Expected:

```text
vehicle_service
```

---

# 45. Create Database Tables

The database schema is available at:

```text
database/schema.sql
```

Execute the schema file using MySQL.

The schema creates the required tables and relationships.

---

# 46. Insert Sample Data

Sample data is available at:

```text
database/sample_data.sql
```

This can be executed after creating the tables.

---

# 47. Database Queries

Additional SQL queries are available at:

```text
database/queries.sql
```

These queries can be used to verify the stored data and demonstrate SQL operations during the project review.

---

# 48. Configure Database Connection

The application uses:

```text
application.properties
```

Example:

```properties
db.url=jdbc:mysql://localhost:3306/vehicle_service
db.username=root
db.password=YOUR_PASSWORD
```

Replace `YOUR_PASSWORD` with the local MySQL password.

Do not upload the real password to GitHub.

---

# 49. Compile the Project

Open the VS Code terminal in the project root:

```powershell
mvn clean compile
```

Explanation:

```text
mvn
```

Runs Maven.

```text
clean
```

Deletes previous build output from the `target` directory.

```text
compile
```

Compiles the Java source files.

Expected result:

```text
BUILD SUCCESS
```

---

# 50. Run the Console Application

After compilation, run:

```powershell
mvn compile exec:java
```

This starts:

```text
VehicleServiceApplication
```

The application then displays the interactive console menu.

---

# 51. If Maven Is Not Recognized

If PowerShell displays:

```text
mvn : The term 'mvn' is not recognized
```

Maven may not be available in the current terminal PATH.

If Maven is installed at:

```text
C:\maven\apache-maven-3.9.9\bin
```

run:

```powershell
$env:Path += ";C:\maven\apache-maven-3.9.9\bin"
```

Then:

```powershell
mvn -version
```

If Maven is detected, run:

```powershell
mvn clean compile
```

and:

```powershell
mvn compile exec:java
```

---

# 52. Alternative Maven Command

Maven can also be executed directly:

```powershell
C:\maven\apache-maven-3.9.9\bin\mvn.cmd clean compile
```

Then:

```powershell
C:\maven\apache-maven-3.9.9\bin\mvn.cmd compile exec:java
```

---

# 53. Complete Application Workflow

A typical service workflow is:

```text
1. Add Customer
        ↓
2. Add Vehicle
        ↓
3. Add/Select Service
        ↓
4. Book Service
        ↓
5. Assign Mechanic
        ↓
6. Start Service
        ↓
7. Complete Service
        ↓
8. Generate Service Record
        ↓
9. Generate Bill
        ↓
10. Mark Payment as PAID
```

---

# 54. Complete Data Flow Example

Suppose a user wants to add a customer.

The request flows through:

```text
User
 ↓
ConsoleUI
 ↓
CustomerService
 ↓
CustomerRepository
 ↓
JdbcCustomerRepository
 ↓
PreparedStatement
 ↓
MySQL
 ↓
customers table
```

The same layered approach is used for other modules.

---

# 55. Testing

The project contains tests for different development phases.

```text
Step3WorkflowTest
Step4JdbcTest
```

The JDBC test verifies the database integration and CRUD workflow.

The project compilation was verified using:

```powershell
mvn clean compile
```

---

# 56. JDBC End-to-End Verification

The JDBC workflow verifies:

```text
Java Application
      ↓
JDBC Connection
      ↓
MySQL Database
      ↓
INSERT
      ↓
SELECT
      ↓
UPDATE
      ↓
DELETE
```

Data inserted into MySQL remains available after the application is restarted.

---

# 57. Important Design Decisions

## Why Java?

Java provides:

* Object-oriented programming
* Strong type safety
* Exception handling
* Platform independence
* Large ecosystem
* JDBC support

---

## Why MySQL?

MySQL provides:

* Persistent data storage
* Relational database structure
* Primary and foreign keys
* Constraints
* SQL queries
* Transaction support

---

## Why JDBC?

JDBC provides direct Java-to-database connectivity and allows the application to execute SQL commands.

---

## Why Maven?

Maven simplifies:

* Dependency management
* Compilation
* Build automation
* Plugin management
* Project structure

---

## Why Layered Architecture?

Layered architecture separates responsibilities.

```text
Console
   ↓
Business Logic
   ↓
Data Access
   ↓
Database
```

This makes the project easier to understand, maintain, test, and extend.

---

# 58. Why Not Put SQL Inside Main?

SQL should not be directly placed inside the main class.

If SQL is placed inside the UI:

```text
UI + Business Logic + Database
```

becomes tightly coupled.

Instead:

```text
UI
 ↓
Service
 ↓
Repository
 ↓
JDBC
 ↓
Database
```

This follows separation of concerns.

---

# 59. Why Repository Interface?

The repository interface separates business logic from storage implementation.

For example:

```text
CustomerRepository
```

can have:

```text
InMemoryCustomerRepository
JdbcCustomerRepository
```

This means the application can change its storage implementation without rewriting the entire Service layer.

---

# 60. Why MySQL Instead of ArrayList?

An `ArrayList` stores information in memory.

```text
Java Application
      |
      v
ArrayList
```

When the application terminates, the data is lost.

MySQL provides persistent storage:

```text
Java Application
      |
      v
MySQL
      |
      v
Persistent Database
```

Therefore, MySQL is more appropriate when data needs to remain after application restarts.

---

# 61. Security Against SQL Injection

SQL Injection occurs when malicious user input is inserted directly into SQL queries.

Unsafe approach:

```java
String sql = "SELECT * FROM customers WHERE name = '" + name + "'";
```

The project instead uses:

```sql
SELECT *
FROM customers
WHERE name = ?
```

with:

```text
PreparedStatement
```

This separates SQL commands from user-provided values.

---

# 62. Project Advantages

The system provides:

* Centralized data management
* Persistent database storage
* Structured architecture
* Input validation
* Exception handling
* Mechanic availability tracking
* Service lifecycle management
* Automated billing
* Service history
* SQL-based reporting
* Secure parameterized database queries

---

# 63. Limitations

The current version is a console-based application.

It does not currently provide:

* Web interface
* Mobile application
* Online payment gateway
* Customer login
* Mechanic login
* Email notifications
* SMS notifications
* Cloud deployment

These can be added in future versions.

---

# 64. Future Enhancements

The project can be converted into a web application.

Possible future architecture:

```text
React Frontend
       |
       v
Spring Boot REST API
       |
       v
Service Layer
       |
       v
Repository Layer
       |
       v
MySQL
```

Possible future features:

* Customer login
* Admin dashboard
* Mechanic dashboard
* Online appointment booking
* Online payment
* Email notifications
* SMS notifications
* Service reminders
* Reports and analytics
* Cloud deployment
* REST APIs

---

# 65. College Review – Project Explanation

If asked:

### "Explain your project."

Answer:

> Our project is a Vehicle Service Management System developed using Java, JDBC, SQL, and MySQL. It is a console-based application used to manage customers, vehicles, mechanics, services, service bookings, service history, and billing. We follow a layered architecture where the Console UI communicates with the Service layer, the Service layer uses Repository interfaces, and JDBC repository implementations communicate with MySQL. The project demonstrates OOP concepts such as encapsulation, inheritance, abstraction, polymorphism, interfaces, and composition along with JDBC and SQL concepts.

---

# 66. Important Viva Questions

### What is JDBC?

JDBC stands for Java Database Connectivity. It is a Java API used to connect Java applications with relational databases such as MySQL.

---

### Why did you use PreparedStatement?

We use PreparedStatement because it supports parameterized SQL queries, improves security against SQL injection, and safely handles user input.

---

### What is ResultSet?

ResultSet represents the data returned by a SELECT query. We use `rs.next()` to process the returned rows.

---

### What is a Primary Key?

A primary key uniquely identifies each record in a database table.

---

### What is a Foreign Key?

A foreign key references a primary key of another table and establishes a relationship between the tables.

---

### What is Encapsulation?

Encapsulation means keeping data private inside a class and providing controlled access through methods such as getters and setters.

---

### What is Inheritance?

Inheritance allows one class to acquire properties and behavior from another class.

In this project:

```text
Person
 ├── Customer
 └── Mechanic
```

---

### What is Polymorphism?

Polymorphism allows objects of different classes to be accessed through a common interface or parent type.

In this project, repository implementations demonstrate polymorphism.

---

### Why is Person abstract?

`Person` represents common properties shared by Customer and Mechanic. A generic Person object is not required directly, so it is modeled as an abstract class.

---

### Why use BigDecimal?

BigDecimal provides accurate decimal calculations and is appropriate for financial values such as service prices and bills.

---

### Why use Enum?

Enums provide type-safe predefined values for fields such as booking status and payment status.

---

### Why use MySQL?

MySQL provides persistent relational storage and supports keys, constraints, relationships, and SQL queries.

---

### What happens when the application restarts?

Data stored in MySQL remains available because the database provides persistent storage.

---

### Why use Repository Pattern?

The Repository Pattern separates data-access logic from business logic and allows different repository implementations.

---

### How is mechanic availability managed?

When a mechanic is assigned, the status changes from `AVAILABLE` to `BUSY`. When the service is completed, it changes back to `AVAILABLE`.

---

### How are invalid booking transitions handled?

The Service layer checks the current booking status before allowing a transition. If the transition is invalid, an `InvalidBookingException` is thrown.

---

### Why not put SQL inside ConsoleUI?

It would tightly couple user-interface logic with database logic. The layered architecture separates UI, business logic, and persistence.

---

# 67. Key Architecture to Memorize

For the college review, remember this:

```text
                USER
                  |
                  v
             Console UI
                  |
                  v
           Service Layer
                  |
                  v
        Repository Interface
                  |
                  v
        JDBC Repository / DAO
                  |
                  v
             JDBC API
                  |
                  v
              MySQL
```

---

# 68. Key OOP Concepts to Memorize

```text
Encapsulation
     ↓
Private fields + getters/setters

Inheritance
     ↓
Person → Customer / Mechanic

Abstraction
     ↓
Abstract Person + Repository Interfaces

Polymorphism
     ↓
Repository Interface + Multiple Implementations

Composition
     ↓
ServiceRecord → ServiceDetails
```

---

# 69. Key JDBC Concepts to Memorize

```text
DriverManager
     ↓
Connection
     ↓
PreparedStatement
     ↓
ResultSet
     ↓
Java Objects
```

Also remember:

```text
Statement.RETURN_GENERATED_KEYS
try-with-resources
```

---

# 70. Key Database Concepts to Memorize

```text
Primary Key
Foreign Key
Unique Constraint
NOT NULL
AUTO_INCREMENT
JOIN
DECIMAL
CRUD
Referential Integrity
```

---

# 71. Git and GitHub

Git is used for version control.

GitHub is used to host the project remotely.

Common commands:

```powershell
git status
```

Check repository status.

```powershell
git add .
```

Stage changes.

```powershell
git commit -m "commit message"
```

Create a commit.

```powershell
git push
```

Push committed changes to GitHub.

The correct command is:

```powershell
git push
```

not:

```powershell
git origin push
```

`origin` is the name of the remote repository; it is used with commands such as:

```powershell
git push origin main
```

---

# 72. Build Commands

Compile the project:

```powershell
mvn clean compile
```

Run the console application:

```powershell
mvn compile exec:java
```

---

# 73. Expected Execution Flow

After running:

```powershell
mvn compile exec:java
```

the application starts and displays the console-based Vehicle Service Management System.

The user can then interact with the system through the available menu options.

---

# 74. Conclusion

The Vehicle Service Management System demonstrates how a real-world service center workflow can be implemented using Java and MySQL.

The project combines:

```text
Java
+
OOP
+
Exception Handling
+
JDBC
+
SQL
+
MySQL
+
Maven
+
Layered Architecture
+
Git/GitHub
```

The system provides a structured foundation that can later be extended into a full-stack web application using technologies such as Spring Boot and React.

---

# 75. Author

**Jai Harsan S**

B.Tech – Computer Science and Business Systems (CSBS)

Sri Eshwar College of Engineering

---

## License

This project is developed for academic and educational purposes.
