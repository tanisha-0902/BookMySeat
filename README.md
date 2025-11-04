# 🎟️ BookMySeat – Event Management Platform

**BookMySeat** is a comprehensive, full-stack web application designed to simplify **event discovery and registration** for users while providing administrators with powerful event management tools.  
Built with a **modern microservice architecture**, it ensures scalability, security, and maintainability.

---

## ✨ Key Features

- **Microservice Architecture:** Decoupled services for Users, Events, and Registrations.
- **Role-Based Access Control (Admin/User):**
  - **Admin Dashboard:** Full CRUD (Create, Read, Update, Delete) operations for events and viewing attendee lists.
  - **User Dashboard:** Browse, book, and cancel event registrations seamlessly.
- **Secure Authentication & Authorization:** End-to-end security using **Spring Security** and **JWT (JSON Web Tokens)**.
- **Data Persistence:** Reliable and efficient data storage with **MySQL** and **Spring Data JPA**.
- **Modern Frontend:** Responsive UI using **Thymeleaf**, **Bootstrap 5**, and **JavaScript**.
- **Business Logic:** Validates event dates to prevent booking or creation of past events.

---

## 🛠️ Tech Stack

| Layer | Technology |
|-------|-------------|
| **Backend** | Java 21, Spring Boot 3.x, Spring Security, Spring Data JPA |
| **Database** | MySQL |
| **Frontend** | Thymeleaf, Bootstrap 5, JavaScript |
| **Build Tool** | Maven |
| **Security** | JWT (JSON Web Tokens) |

---

## 🏛️ Architecture Overview

The application follows a **microservice architecture** with three core services:

| Service | Port | Description |
|----------|------|-------------|
| **user-service** | `8080` | Handles user authentication (login/registration), JWT generation, and serves the user dashboard. |
| **event-service** | `8081` | Manages the event lifecycle (CRUD operations) and serves the admin dashboard. |
| **registration-service** | `8082` | Handles user–event relationships, including bookings and cancellations. |

Each service communicates via **REST APIs** and is secured using **JWT tokens**.

---

## 🚀 Setup & Run the Project

### 🧩 Prerequisites

- **Java Development Kit (JDK):** Version 21 or higher  
- **Maven:** Version 3.6 or higher  
- **MySQL Server:** Running locally or accessible remotely  
- **Git:** Installed on your system  

---

### ⚙️ Setup Instructions

1. **Clone the repository**
   ```bash
   git clone https://github.com/tanisha-0902/BookMySeat.git
Navigate to the project directory

bash
Copy code
cd BookMySeat
Build the project using Maven

bash
Copy code
mvn clean install
Run the Spring Boot application

bash
Copy code
mvn spring-boot:run
🌐 Access the Application
Once the application starts, open your browser and go to:

👉 http://localhost:8080
