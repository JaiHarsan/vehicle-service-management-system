# Vehicle Service Management System

A console-based Vehicle Service Management System developed using **Java, JDBC, SQL, and MySQL**.

The application helps a vehicle service center manage customers, vehicles, mechanics, services, service bookings, service records, and billing through a structured layered architecture.

---

## 📌 Project Overview

Vehicle service centers need to manage a large amount of information such as customer details, vehicle details, mechanic assignments, service bookings, service history, and billing.

Managing these records manually can lead to:

- Duplicate vehicle registrations
- Lost customer records
- Difficulty tracking service history
- Mechanic scheduling conflicts
- Incorrect billing calculations
- Difficulty searching and updating records

The **Vehicle Service Management System** provides a centralized Java-based console application that manages these operations and stores the data permanently in a **MySQL database** using **JDBC**.

---

## 🎯 Objectives

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
12. Demonstrate Java OOP, JDBC, SQL, and database concepts.

---

## 🛠️ Technologies Used

| Technology | Purpose |
|------------|---------|
| Java 17 | Application development |
| OOP | Object-oriented application design |
| Maven | Build and dependency management |
| JDBC | Java-MySQL connectivity |
| MySQL | Persistent relational database |
| SQL | Database operations |
| Git | Version control |
| GitHub | Remote repository |
| VS Code | Development environment |

---

## 🏗️ Architecture

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
              | Service Layer  |
              +-------+-------+
                      |
                      v
              +---------------+
              |  Repository    |
              |   Interface    |
              +-------+-------+
                      |
                      v
              +---------------+
              | JDBC Repository|
              +-------+-------+
                      |
                      v
              +---------------+
              |     JDBC       |
              +-------+-------+
                      |
                      v
              +---------------+
              |     MySQL      |
              +---------------+
