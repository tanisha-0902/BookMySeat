BookMySeat – Event Management Platform 🎟️
BookMySeat is a comprehensive, full-stack web application designed to simplify event discovery and registration for users while providing administrators with robust event management tools. It's built using a modern microservice architecture, ensuring scalability and maintainability.

✨ Key Features
Microservice Architecture: Decoupled services for Users, Events, and Registrations.
Role-Based Access Control (Admin/User):
Admin Dashboard: Full CRUD (Create, Read, Update, Delete) operations for events and viewing attendee lists.
User Dashboard: Seamless browsing, booking, and cancellation of event registrations.
Secure Authentication & Authorization: Stateless, end-to-end security using Spring Security and JSON Web Tokens (JWT).
Data Persistence: Reliable data storage using MySQL and Spring Data JPA.
Modern Frontend: Responsive UI built with server-side Thymeleaf templates and Bootstrap.
Business Logic: Includes date validation to prevent booking or creating past events.
🛠️ Tech Stack
Backend: Java 21, Spring Boot 3.x, Spring Security, Spring Data JPA
Database: MySQL
Frontend: Thymeleaf, Bootstrap 5, JavaScript
Build Tool: Maven
Security: JWT
🏛️ Architecture Overview
The application follows a microservice architecture:

user-service (Port 8080): Handles user authentication (login/registration), JWT generation, and serves the user dashboard.
event-service (Port 8081): Manages the event lifecycle (CRUD operations) and serves the admin dashboard.
registration-service (Port 8082): Manages the relationship between users and events (bookings).
All services communicate via REST APIs and are secured using JWT.

🚀 Setup & Run the Project
Prerequisites
Java Development Kit (JDK): Version 21 or higher.
Maven: Version 3.6 or higher.
MySQL Server: Running locally or accessible.
Git: For cloning the repository.
Setup
Clone the repository
https://github.com/tamannachopraaa/BookMySeat.git

Navigate to the project directory
cd BookMySeat

Build the project using Maven
mvn clean install

Run the Spring Boot application
mvn spring-boot:run

Access the application
Open your browser and go to: http://localhost:8080
