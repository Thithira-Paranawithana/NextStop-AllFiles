NextStop - Microservices-based Bus Ticket Reservation System


<div align="center"> <img src="./assets/nextstop_icon.jpg" alt="NextStop Logo" width="200" height="200">
Smart Bus Ticket Reservation System
[
[
[
[
[

</div>
📋 Table of Contents
Overview

Features

Technology Stack

System Architecture

Database Design

Getting Started

Screenshots

Testing

Contributing

Team

Acknowledgments

🚀 Overview
NextStop is a modern, distributed microservices-based bus ticket reservation system that streamlines the entire booking process from seat selection to payment confirmation. Built with Spring Boot microservices architecture and a responsive React.js frontend, it provides a seamless experience for passengers while offering robust management capabilities for bus operators.

🌟 Key Highlights
Microservices Architecture: Scalable and maintainable distributed system

Real-time Operations: Live seat availability and instant booking confirmations

Secure Payments: Integrated Stripe payment gateway with test environment

User-Friendly Interface: Responsive design across all devices

Email Notifications: Automated booking confirmations and verification emails

✨ Features
👥 For Passengers
🔐 User Authentication: Secure registration and login with JWT tokens

🔍 Smart Search: Search buses by route, date, and time

💺 Seat Selection: Interactive seat map with real-time availability

💳 Secure Payment: Stripe integration with multiple payment options

📧 Email Notifications: Booking confirmations and verification emails

📱 Responsive Design: Seamless experience across devices

🎫 Digital Tickets: Download tickets in PDF format

📋 Booking History: View and manage past bookings

👨‍💼 For Administrators
🚌 Bus Management: Add, edit, and manage bus information

🛣️ Route Management: Configure routes and schedules

📊 Dashboard: Real-time analytics and reporting

👥 User Management: Handle user accounts and permissions

📋 Booking Management: Monitor and manage all bookings

🛠️ Technology Stack
Backend
Spring Boot 3.5 - Application Framework

Java 21 - Programming Language

Spring Cloud 2025.0.0 - Microservices Architecture

Netflix Eureka - Service Discovery

Spring Cloud Gateway - API Gateway

PostgreSQL 17 - Database

JWT - Authentication

Stripe API - Payment Processing

Spring Mail - Email Service

Frontend
React.js - Frontend Framework

TypeScript - Programming Language

Tailwind CSS - Styling Framework

React Hooks - State Management

Axios - HTTP Client

jsPDF - PDF Generation

Infrastructure
Netflix Eureka Server - Service Registry

Spring Cloud Gateway - Load Balancing

Docker - Containerization

Postman - API Testing

Git - Version Control

🏗️ System Architecture
<div align="center"> <img src="./assets/architecture.png" alt="System Architecture" width="800"> </div>
The NextStop system follows a microservices architecture pattern with the following components:

Core Services
User Service (Port: 8095) - User authentication, registration, and profile management

Bus Service (Port: 8090) - Bus information, routes, and schedules management

Booking Service (Port: 8765) - Seat bookings and availability processing

Payment Service (Port: 8080) - Payment processing with Stripe integration

Notification Service (Port: 8082) - Email notifications management

Infrastructure Components
Eureka Server (Port: 8761) - Service discovery and registration

API Gateway (Port: 8765) - Centralized routing and load balancing

Frontend Application (Port: 5173) - React.js user interface

🗄️ Database Design
User Database (PostgreSQL)
<div align="center"> <img src="./assets/userdb.png" alt="User Database Schema" width="600"> </div>
Tables:

users: User account information

roles: User roles (CUSTOMER, ADMIN, OPERATOR)

user_roles: User-role mapping

refresh_tokens: JWT refresh tokens

verification_tokens: Email verification tokens

Bus Database (PostgreSQL)
<div align="center"> <img src="./assets/busdb.png" alt="Bus Database Schema" width="600"> </div>
Tables:

buses: Bus information and specifications

routes: Bus routes between cities

schedules: Bus schedule information

seats: Seat configuration for each bus

Booking Database (PostgreSQL)
<div align="center"> <img src="./assets/bookingdb.png" alt="Booking Database Schema" width="600"> </div>
Tables:

bookings: Booking records and details

booking_seats: Seat reservations for bookings

Payment Database (PostgreSQL)
<div align="center"> <img src="./assets/paymentdb.png" alt="Payment Database Schema" width="600"> </div>
Tables:

payments: Payment transaction records

payment_methods: Supported payment methods

🚀 Getting Started
Prerequisites
☕ Java 21 or higher

🟢 Node.js 18 or higher

🐘 PostgreSQL 15

🐳 Docker (optional)

📂 Git

Installation
1. Clone the repository
bash
git clone https://github.com/Thithira-Paranawithana/NextStop-AllFiles.git
cd nextstop-bus-reservation
2. Setup Databases
bash
# Create databases
createdb userdb
createdb busdb
createdb bookingdb
createdb paymentdb

# Import database scripts
psql -d userdb -f NextStop-DB/userdb.sql
psql -d busdb -f NextStop-DB/busdb.sql
psql -d bookingdb -f NextStop-DB/bookingdb.sql
psql -d paymentdb -f NextStop-DB/paymentdb.sql
3. Configure Environment Variables
bash
# Update application.properties for each service
spring.datasource.url=jdbc:postgresql://localhost:5432/userdb
spring.datasource.username=your_username
spring.datasource.password=your_password
4. Start Services (in order)
bash
# 1. Start Eureka Server
cd eureka-server
./mvnw spring-boot:run

# 2. Start API Gateway
cd ../api-gateway
./mvnw spring-boot:run

# 3. Start Microservices
cd ../user-service
./mvnw spring-boot:run

cd ../bus-service
./mvnw spring-boot:run

cd ../booking-service
./mvnw spring-boot:run

cd ../payment-service
./mvnw spring-boot:run

cd ../notification-service
./mvnw spring-boot:run
5. Start Frontend
bash
cd frontend
npm install
npm run dev
6. Access the Application
🌐 Frontend: http://localhost:5173

📊 Eureka Dashboard: http://localhost:8761

🚪 API Gateway: http://localhost:8765

📸 Screenshots
Landing Page
<div align="center"> <img src="./assets/landing_page.png" alt="Landing Page" width="800"> </div>
🧪 Testing
API Testing with Postman
bash
# Import Postman collection
postman-collection/NextStop-API-Tests.json
Running Tests
bash
# Backend tests
./mvnw test

# Frontend tests
npm test
🤝 Contributing
Fork the repository

Create a feature branch (git checkout -b feature/AmazingFeature)

Commit your changes (git commit -m 'Add some AmazingFeature')

Push to the branch (git push origin feature/AmazingFeature)

Open a Pull Request

👥 Team
<div align="center">
Thithira Paranawithana 

Dilranga Dissanayake 

Nipuni Tennakoon 

</div>
🙏 Acknowledgments
🍃 Spring Boot Team for the excellent microservices framework

⚛️ React.js Community for the powerful frontend library

💳 Stripe for the payment integration

🐘 PostgreSQL for the robust database system

<div align="center"> <h3>Made with ❤️ by the NextStop Team</h3> <p>© 2025 NextStop. All rights reserved.</p> </div>