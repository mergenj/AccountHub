# AccountHub API

## Description
AccountHub API is a Spring Boot application designed to manage bank accounts, handle multiple currencies, and perform transactions such as retrieving balances, deposit, debit, and currency exchanges.

---

## Overview
The API allows users to:
1. Manage accounts and their associated balances in multiple currencies.
2. Perform transactions like deposits, debits, and currency exchanges.
3. Simulate calls to an external system.
4. Access API documentation via Swagger.

Technologies used:
- Spring Boot
- H2 Database
- Maven
- Springdoc OpenAPI for API documentation

---

## Setup and Run Instructions

### Prerequisites
1. Java 23 or later

### Steps to Set Up and Run

#### 1. Clone the Repository

#### 2. Build the Project

#### 3. Run the Application

The application will start at `http://localhost:8080`

#### 4. Access Swagger API Documentation
Open your browser and navigate to:
```
http://localhost:8080/swagger-ui/index.html
```

---

## Notes
- The database is preloaded with sample data for testing.
- Error handling is implemented for scenarios like invalid account IDs, insufficient funds, or invalid transaction amounts.

