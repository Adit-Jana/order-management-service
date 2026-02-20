# Order Management Service

A robust backend service for managing orders in an e-commerce marketplace platform. Built with Spring Boot and Java, featuring advanced JWT-based security, comprehensive logging, and role-based access control.



## 📋 Table of Contents

- [Features](#features)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
- [API Endpoints](#api-endpoints)
  - [Authentication](#authentication)
  - [Orders](#orders)
  - [Admin Operations](#admin-operations)
- [Security](#security)
- [Configuration](#configuration)
- [Development](#development)
- [License](#license)

## ✨ Features

- **JWT Authentication**: Secure token-based authentication for all endpoints
- **Role-Based Access Control (RBAC)**: Fine-grained access control with different user roles
- **Comprehensive Logging**: Detailed logging mechanism for tracking all operations
- **Order Management**: Full CRUD operations for order management
- **Scalable Architecture**: Designed for high-performance e-commerce operations
- **Exception Handling**: Robust error handling and validation
- **RESTful API**: Clean and intuitive REST API design

## 🛠 Tech Stack

- **Runtime**: Java 11+
- **Framework**: Spring Boot 2.7.x / 3.x
- **Build Tool**: Maven
- **Authentication**: JWT (JSON Web Tokens)
- **Logging**: SLF4J / Logback
- **Database**: MySQL / PostgreSQL (configurable)

## 🚀 Getting Started

### Prerequisites

- Java 11 or higher
- Maven 3.6.0 or higher
- MySQL 5.7+ or PostgreSQL 10+

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/Adit-Jana/order-management-service.git
   cd order-management-service
   ```

2. **Configure database** (update `src/main/resources/application.yml`)
   ```yaml
   spring:
     datasource:
       url: jdbc:mysql://localhost:3306/order_management
       username: root
       password: your_password
     jpa:
       hibernate:
         ddl-auto: update
   ```

3. **Build the project**
   ```bash
   ./mvnw clean install
   ```

4. **Run the application**
   ```bash
   ./mvnw spring-boot:run
   ```

The application will start on `http://localhost:8080`

## 📡 API Endpoints

### Base URL
```
http://localhost:8080/api
```

### Authentication

#### Login
- **Endpoint**: `POST /auth/login`
- **Description**: Authenticate user and receive JWT token
- **Request Body**:
  ```json
  {
    "username": "user@example.com",
    "password": "password123"
  }
  ```
- **Response** (200):
  ```json
  {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "type": "Bearer",
    "expiresIn": 86400
  }
  ```

#### Register
- **Endpoint**: `POST /auth/register`
- **Description**: Register new user account
- **Request Body**:
  ```json
  {
    "username": "newuser",
    "email": "user@example.com",
    "password": "password123",
    "role": "CUSTOMER"
  }
  ```
- **Response** (201): User registered successfully

### Orders

#### Get All Orders
- **Endpoint**: `GET /orders`
- **Description**: Retrieve all orders (Paginated)
- **Authentication**: Required (Bearer Token)
- **Query Parameters**:
  - `page` (default: 0)
  - `size` (default: 10)
  - `sortBy` (default: createdAt)
- **Response** (200):
  ```json
  {
    "content": [
      {
        "id": "ORD-001",
        "userId": "USER-123",
        "totalAmount": 2499.99,
        "status": "CONFIRMED",
        "createdAt": "2026-02-01T10:30:00Z",
        "items": [
          {
            "productId": "PROD-001",
            "quantity": 2,
            "price": 1249.99
          }
        ]
      }
    ],
    "totalElements": 100,
    "totalPages": 10
  }
  ```

#### Get Order by ID
- **Endpoint**: `GET /orders/{orderId}`
- **Description**: Retrieve specific order details
- **Authentication**: Required (Bearer Token)
- **Response** (200):
  ```json
  {
    "id": "ORD-001",
    "userId": "USER-123",
    "totalAmount": 2499.99,
    "status": "CONFIRMED",
    "description": "Order description",
    "createdAt": "2026-02-01T10:30:00Z",
    "items": [
      {
        "productId": "PROD-001",
        "quantity": 2,
        "price": 1249.99
      }
    ]
  }
  ```

#### Create Order
- **Endpoint**: `POST /orders`
- **Description**: Create a new order
- **Authentication**: Required (Bearer Token)
- **Request Body**:
  ```json
  {
    "userId": "USER-123",
    "items": [
      {
        "productId": "PROD-001",
        "quantity": 2,
        "price": 1249.99
      }
    ],
    "description": "Order for customer",
    "shippingAddress": "123 Main St, City, State 12345"
  }
  ```
- **Response** (201):
  ```json
  {
    "id": "ORD-001",
    "status": "PENDING",
    "message": "Order created successfully"
  }
  ```

#### Update Order
- **Endpoint**: `PUT /orders/{orderId}`
- **Description**: Update order details and description
- **Authentication**: Required (Bearer Token)
- **Request Body**:
  ```json
  {
    "description": "Updated order description",
    "status": "PROCESSING"
  }
  ```
- **Response** (200):
  ```json
  {
    "id": "ORD-001",
    "status": "PROCESSING",
    "message": "Order updated successfully"
  }
  ```

#### Cancel Order
- **Endpoint**: `DELETE /orders/{orderId}`
- **Description**: Cancel/delete an order
- **Authentication**: Required (Bearer Token)
- **Response** (200):
  ```json
  {
    "message": "Order cancelled successfully"
  }
  ```

### Admin Operations

#### Get All Orders (Admin)
- **Endpoint**: `GET /admin/orders`
- **Description**: Admin view of all orders with filters
- **Authentication**: Required (ADMIN role)
- **Query Parameters**:
  - `status` (PENDING, CONFIRMED, PROCESSING, SHIPPED, DELIVERED)
  - `userId` (optional filter)
  - `dateFrom` (optional filter)
  - `dateTo` (optional filter)
- **Response** (200): List of orders

#### Update Order Status (Admin)
- **Endpoint**: `PUT /admin/orders/{orderId}/status`
- **Description**: Update order status
- **Authentication**: Required (ADMIN role)
- **Request Body**:
  ```json
  {
    "status": "SHIPPED",
    "notes": "Order dispatched"
  }
  ```
- **Response** (200): Updated order

#### Get User Orders (Admin)
- **Endpoint**: `GET /admin/users/{userId}/orders`
- **Description**: View all orders for a specific user
- **Authentication**: Required (ADMIN role)
- **Response** (200): List of user orders

## 🔒 Security

### Authentication
The API uses JWT (JSON Web Tokens) for authentication. All protected endpoints require a valid JWT token in the Authorization header.

### Authorization
```
Authorization: Bearer <your_jwt_token>
```

### Roles
- **CUSTOMER**: Standard user role with basic order management
- **ADMIN**: Administrative role with full access
- **MANAGER**: Manager role with operational permissions

### Password Policy
- Minimum 8 characters
- Must contain uppercase, lowercase, numbers, and special characters
- Passwords are hashed using BCrypt

### CORS Configuration
The service is configured to accept requests from specified origins. Update `application.yml` for CORS settings.

## ⚙️ Configuration

### Environment Variables

Create a `.env` file or set these environment variables:

```env
SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/order_management
SPRING_DATASOURCE_USERNAME=root
SPRING_DATASOURCE_PASSWORD=password
JWT_SECRET=your_jwt_secret_key_here
JWT_EXPIRATION=86400000
SPRING_JPA_HIBERNATE_DDL_AUTO=update
```

### Application Properties

Key configuration properties in `application.yml`:

```yaml
spring:
  application:
    name: order-management-service
  datasource:
    url: jdbc:mysql://localhost:3306/order_management
    driver-class-name: com.mysql.cj.jdbc.Driver
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: false

jwt:
  secret: your_secret_key
  expiration: 86400000

logging:
  level:
    root: INFO
    com.ordermanagement: DEBUG
```

## 🔨 Development

### Running Tests
```bash
./mvnw test
```

### Build
```bash
./mvnw clean build
```

### Code Style
The project follows Google Java Style Guide. Run formatter:
```bash
./mvnw spotless:apply
```

### API Documentation
Swagger UI documentation is available at:
```
http://localhost:8080/swagger-ui.html
```

## 📝 Version History

- **v1.1** - Current version
  - Enhanced order management endpoints
  - Improved error handling
  - Added pagination support
  - Role-based access control

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 🆘 Support

For issues and questions, please open a GitHub issue or contact the development team.

---

**Last Updated**: February 8, 2026
