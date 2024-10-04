
# Kitchensink application

![Spring Boot Logo](https://img.shields.io/badge/SpringBoot-Kitchensink-6DB33F?style=for-the-badge&logo=springboot)

## Overview
This is a **Spring Boot** application that provides an implementation of **Kitchensink** 
migration from a monolith to a **microservice** application. The application is built with the following stack:

- **Spring Boot** for the backend
- **JPA/Hibernate** for ORM and database interaction
- **H2DB** for the database on memory (Not Mongo in this version)
- **Lombok** to reduce boilerplate code
- **Spring Data JPA** for repository management
- **Swagger** for API documentation

## Features
- Create and Read on Member entities
- Validation of request payload
- Exception handling with custom error responses
- Unit tests

## Table of Contents
- [Getting Started](#getting-started)
- [API Endpoints](#api-endpoints)
- [Technologies](#technologies)
- [Database Configuration](#database-configuration)
- [Running the Application](#running-the-application)
- [Swagger Documentation](#swagger-documentation)

## Getting Started

To get started with the application, follow these steps:

### Prerequisites
Make sure you have the following installed:
- **Java 21**
- **Maven 3.x**

### Clone the Repository

```bash
git clone https://github.com/mgrellet/kitchensink
cd kitchensink
```

### Database Configuration

On-memory DB already configured in `src/main/resources/application.properties`:

```properties
# H2 Database configuration
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# Hibernate configuration
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=create
```

### Running the Application

You can run the Spring Boot application by executing:

```bash
mvn spring-boot:run
```

Alternatively, you can build the JAR file and run it:

```bash
mvn clean package
java -jar target/kitchensink-0.0.1-SNAPSHOT.jar
```

## API Endpoints

Below are the available API endpoints for managing users:

| HTTP Method | Endpoint               | Description             |
|-------------|------------------------|-------------------------|
| `POST`      | `/rest/members`            | Register a new member   |
| `GET`       | `/rest/members/`           | Retrieve all members    |
| `GET`       | `/rest/members/{id}`       | Retrieve a member by ID |

### Sample API Request

#### Create a new member
```bash
POST /rest/members
Content-Type: application/json

{
  "name": "John Doe",
  "email": "johndoe@example.com",
  "phoneNumber": "12345678912"
}
```

#### Response:
```json
{
  "message": "success",
  "status": 201,
  "data": {
    "id": 1,
    "name": "John Doe",
    "email": "johndoe@example.com",
    "phoneNumber": "12345678912"
  }
}
```

## Technologies
- **Java 21**
- **Spring Boot 3.3.4**
- **Spring Data JPA**
- **Hibernate**
- **H2DB**
- **Lombok**
- **Swagger** (API documentation)

## Swagger Documentation

Once the application is running, you can access the Swagger UI to explore the API:

```
http://localhost:8080/swagger-ui/index.html
```

This provides an interactive interface to test your API endpoints.

## Running Tests

To run the unit and integration tests:

```bash
mvn test
```
