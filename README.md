StaySync 🏨

StaySync is a hotel management and booking platform built using a Java Spring Boot microservices architecture. The system is divided into independently deployable services responsible for authentication, room management, bookings, and API routing.

🏗️ Architecture

StaySync follows a microservices architecture with the following components:

                         ┌─────────────────────┐
                         │      Client         │
                         │ Web / Mobile / API  │
                         └──────────┬──────────┘
                                    │
                                    ▼
                         ┌─────────────────────┐
                         │   API Gateway       │
                         │ staysync-api-gateway│
                         └──────────┬──────────┘
                                    │
              ┌─────────────────────┼─────────────────────┐
              │                     │                     │
              ▼                     ▼                     ▼
     ┌─────────────────┐   ┌─────────────────┐   ┌─────────────────┐
     │ Auth Service    │   │ Room Service    │   │ Booking Service │
     │                 │   │                 │   │                 │
     │ Authentication  │   │ Room Management │   │ Reservations    │
     │ & Authorization │   │ & Availability  │   │ & Bookings      │
     └─────────────────┘   └─────────────────┘   └─────────────────┘
              │                     │                     │
              └─────────────────────┼─────────────────────┘
                                    │
                                    ▼
                         ┌─────────────────────┐
                         │   Eureka Server     │
                         │ Service Discovery   │
                         └─────────────────────┘

📦 Microservices
Service	Description
staysync-api-gateway	Single entry point for client requests and routing to backend services
staysync-auth-service	Handles user authentication and authorization
staysync-booking-service	Manages hotel room reservations and booking operations
staysync-room-service	Manages rooms, room information, and availability
staysync-eureka-server	Service registry and discovery using Eureka
🛠️ Tech Stack

Java

Spring Boot

Spring Cloud

Spring Cloud Gateway

Netflix Eureka

Maven

REST APIs

Microservices Architecture

🚀 Getting Started
Prerequisites

Make sure the following are installed:

Java 17 or later

Maven 3.8+

Git

Verify your installations:

java -version
mvn -version
git --version

📥 Clone the Repository
git clone https://github.com/aka-tanush/StaySync.git
cd StaySync

▶️ Running the Services

Each microservice is an independent Spring Boot application.

Start the services in the following order:

1. Eureka Server
cd staysync-eureka-server
mvn spring-boot:run


The Eureka server provides service discovery for the other microservices.

2. Auth Service
cd staysync-auth-service
mvn spring-boot:run

3. Room Service
cd staysync-room-service
mvn spring-boot:run

4. Booking Service
cd staysync-booking-service
mvn spring-boot:run

5. API Gateway
cd staysync-api-gateway
mvn spring-boot:run


The API Gateway should be started after the backend services and Eureka server are available.

Note: Update the commands, ports, database configuration, and environment variables according to the configuration files in each service.

🔄 Service Discovery

StaySync uses Netflix Eureka for service registration and discovery.

When a service starts, it registers itself with the Eureka server. The API Gateway can then discover backend services without requiring hard-coded service locations.

Eureka Server
     │
     ├── Auth Service
     ├── Room Service
     ├── Booking Service
     └── API Gateway

🔐 Authentication

The authentication service is responsible for:

User registration

User login

Authentication

Authorization

Managing authentication-related information

Authenticated requests can be routed through the API Gateway to protected backend services.

🛏️ Room Management

The Room Service manages hotel room-related functionality, such as:

Creating rooms

Updating room information

Retrieving room details

Managing room availability

Removing rooms

📅 Booking Management

The Booking Service handles reservation-related operations, including:

Creating bookings

Retrieving bookings

Updating bookings

Cancelling bookings

Connecting reservations with available rooms

🌐 API Gateway

The API Gateway acts as the single entry point for clients.

Instead of communicating directly with individual microservices:

Client → Auth Service
Client → Room Service
Client → Booking Service


clients communicate through:

Client
   │
   ▼
API Gateway
   │
   ├── Auth Service
   ├── Room Service
   └── Booking Service


This provides a centralized location for request routing and allows additional cross-cutting functionality to be introduced later.

📁 Project Structure
StaySync/
│
├── staysync-api-gateway/
│   └── API Gateway
│
├── staysync-auth-service/
│   └── Authentication & Authorization
│
├── staysync-booking-service/
│   └── Booking Management
│
├── staysync-eureka-server/
│   └── Service Discovery
│
├── staysync-room-service/
│   └── Room Management
│
└── README.md

🧪 Testing

To run tests for an individual service:

mvn test


To build a service:

mvn clean package

🔧 Configuration

Each service contains its own Spring configuration.

Before running the complete system, verify:

Server ports

Eureka server URL

Database configuration

Service names

Gateway routes

Authentication configuration

Environment-specific properties

For production deployments, sensitive configuration such as database passwords and authentication secrets should be supplied through environment variables or a secure configuration system rather than committed to Git.

🐳 Docker

Docker support can be added to each microservice to simplify local development and deployment.

A typical deployment can contain:

                    ┌──────────────┐
                    │ API Gateway  │
                    └──────┬───────┘
                           │
        ┌──────────────────┼──────────────────┐
        │                  │                  │
        ▼                  ▼                  ▼
   Auth Service       Room Service      Booking Service
        │                  │                  │
        └──────────────────┼──────────────────┘
                           ▼
                    Eureka Server

🔮 Future Enhancements

Potential improvements include:

 Add centralized configuration with Spring Cloud Config

 Add distributed tracing

 Add centralized logging

 Add Docker and Docker Compose support

 Add API documentation with Swagger/OpenAPI

 Add automated integration tests

 Add CI/CD using GitHub Actions

 Add database migration using Flyway or Liquibase

 Add caching with Redis

 Add payment integration

 Add email/SMS booking notifications

 Add monitoring with Prometheus and Grafana

 Add rate limiting at the API Gateway

🤝 Contributing

Contributions are welcome.

Fork the repository.

Create a feature branch.

git checkout -b feature/your-feature


Make your changes.

Run the tests.

mvn test


Commit your changes.

git commit -m "Add your feature"


Push the branch.

git push origin feature/your-feature


Open a Pull Request.

📄 License

This project does not currently specify a license.

If you intend to make the project open source, consider adding an appropriate license file such as LICENSE.


⭐ If you find this project useful, consider giving the repository a star!
