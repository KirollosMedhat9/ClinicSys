# ClinicSys - Microservices Architecture

A comprehensive clinic management system for laser body shaving clinics built with Spring Boot 3.0.0 and microservices architecture.

## 🏗️ Architecture Overview

This project implements a microservices-based architecture with the following components:

### Infrastructure Services
- **Service Registry (Eureka)** - Service discovery and registration
- **Config Server** - Centralized configuration management
- **API Gateway** - Unified entry point with routing and security

### Business Services
- **Auth Service** - Authentication and authorization
- **User Service** - Patient and user management
- **Clinic Service** - Clinic branch management
- **Package Service** - Treatment packages and pricing
- **Reservation Service** - Booking and appointment management
- **Schedule Service** - Time slot management
- **Payment Service** - Payment processing
- **Notification Service** - Email, SMS, and push notifications
- **Promo Service** - Promotional codes and discounts
- **Staff Service** - Staff management
- **Reporting Service** - Analytics and reporting
- **Inventory Service** - Stock management
- **Medical History Service** - Patient medical records
- **Feedback Service** - Patient feedback and ratings
- **Wallet Service** - Digital wallet functionality
- **Referral Service** - Referral program management
- **Audit Service** - Audit logging

## 🚀 Quick Start

### Prerequisites
- Java 17 or higher
- Maven 3.6+
- Docker and Docker Compose
- Git

### Running the Infrastructure

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd ClinicSys
   ```

2. **Build the project**
   ```bash
   mvn clean install
   ```

3. **Start the infrastructure services**
   ```bash
   # Start Service Registry first
   mvn spring-boot:run -pl service-registry
   
   # In another terminal, start Config Server
   mvn spring-boot:run -pl config-server
   
   # In another terminal, start API Gateway
   mvn spring-boot:run -pl api-gateway
   ```

### Service URLs

- **Service Registry**: http://localhost:8761
- **Config Server**: http://localhost:8888
- **API Gateway**: http://localhost:8080

## 🧪 Testing the Infrastructure

### 1. Service Registry Health Check
```bash
curl http://localhost:8761/actuator/health
```

### 2. Config Server Health Check
```bash
curl http://localhost:8888/actuator/health
```

### 3. API Gateway Health Check
```bash
curl http://localhost:8080/actuator/health
```

### 4. Eureka Dashboard
Open http://localhost:8761 in your browser to see the Eureka dashboard with registered services.

## 📁 Project Structure

```
ClinicSys/
├── pom.xml                          # Parent POM
├── service-registry/                # Eureka Server
├── config-server/                   # Spring Cloud Config Server
├── api-gateway/                     # Spring Cloud Gateway
├── common-lib/                      # Shared library
├── auth-service/                    # Authentication service
├── user-service/                    # User management
├── clinic-service/                  # Clinic management
├── package-service/                 # Package management
├── reservation-service/             # Reservation management
├── schedule-service/                # Schedule management
├── payment-service/                 # Payment processing
├── notification-service/            # Notification service
├── promo-service/                   # Promo code management
├── staff-service/                   # Staff management
├── reporting-service/               # Reporting and analytics
├── inventory-service/               # Inventory management
├── medicalhistory-service/          # Medical history
├── feedback-service/                # Feedback management
├── wallet-service/                  # Wallet functionality
├── referral-service/                # Referral program
├── audit-service/                   # Audit logging
└── README.md
```

## 🔧 Configuration

### Service Registry Configuration
- Port: 8761
- Self-preservation: Disabled for development
- Wait time: 0ms for faster startup

### Config Server Configuration
- Port: 8888
- Git repository for configuration storage
- Native configuration fallback

### API Gateway Configuration
- Port: 8080
- Load balancer integration
- CORS enabled
- Security configuration for public endpoints

## 🐳 Docker Support

To run with Docker Compose (when implemented):

```bash
docker-compose up --build
```

## 📚 API Documentation

Each service will have its own Swagger/OpenAPI documentation available at:
- `http://localhost:<service-port>/swagger-ui.html`

## 🔒 Security

- JWT-based authentication
- Role-based access control
- CORS configuration
- API Gateway security filters

## 📊 Monitoring

- Spring Boot Actuator endpoints enabled
- Health checks available for all services
- Metrics and monitoring endpoints

## 🚧 Development Status

- ✅ Infrastructure services implemented
- 🔄 Business services (in progress)
- ⏳ Database setup
- ⏳ Message queue setup (Kafka, RabbitMQ)
- ⏳ Redis cache setup
- ⏳ Docker configuration

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests
5. Submit a pull request

## 📄 License

This project is licensed under the MIT License.

## 🆘 Support

For support and questions, please open an issue in the repository.
