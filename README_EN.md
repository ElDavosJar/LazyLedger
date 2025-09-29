# 🧉 LazyLedger: Multi-User Financial Ledger Management

A backend project designed to explore collaborative financial management solutions, built on solid domain design and clean architecture principles.

## 1. The Problem: The Friction of Manual Recording

My experience with expense management applications has been consistently short-lived. I've identified a fundamental barrier that always leads me to abandon them: the friction of the manual data entry process.

The cycle is predictable: the act of opening the app and filling out a form for each transaction quickly becomes tedious, leading to postponing records and ultimately abandonment.

## 2. Hypothesis and Objective

**Hypothesis:** If recording an expense were as easy as sending an audio, the success rate would increase dramatically.

**Project Objective:** Build a robust backend that can serve as the core for a voice-based transaction recording application, with modular architecture supporting ledgers and user management.

## 3. Architecture Overview

LazyLedger follows Clean Architecture principles with modular design:

- **Ledger Module**: Multi-user ledger management with groups, memberships, and transaction handling
- **User Module**: User registration and domain management
- **Commons**: Shared domain objects, identifiers, and enums

## 4. Ledger Module: Collaborative Financial Management

The Ledger module provides comprehensive financial management capabilities, allowing users to create and manage ledgers with multiple participants and handle transactions within those ledgers.

### Domain Model:
- **Ledger Entity**: Represents a financial ledger with status management (ACTIVE, INACTIVE, ARCHIVED)
- **LedgerGroup Entity**: Groups multiple ledgers under a single owner
- **LedgerMembership Entity**: Manages user roles within ledgers (OWNER, MEMBER)
- **Transaction Entity**: Financial transactions linked to specific ledgers
- **Value Objects**: LedgerName, LedgerId, LedgerGroupId, UserId, TransactionId, Amount, Description, TransactionDate
- **Enums**: LedgerStatus, LedgerUserRole, Category

### Architecture:
- **Repository Pattern**: Clean abstraction over data persistence with Spring Data JPA
- **Use Case Pattern**: Business logic orchestration through dedicated use cases
- **Service Facade**: Simplified API interfaces hiding framework complexity
- **Domain-Driven Design**: Rich domain models with validation and business rules

### Features:
- **Ledger Lifecycle Management**: Create, deactivate, archive, and unarchive ledgers
- **Group Organization**: Organize ledgers into hierarchical groups
- **Role-Based Access Control**: Different permission levels for ledger participants
- **Membership Management**: User invitation and role assignment
- **Transaction Management**: Full CRUD operations for ledger transactions
- **Category Classification**: Predefined expense and income categories
- **Date Range Queries**: Transaction filtering by date ranges

### API Endpoints:

#### Ledger Management:
- `POST /ledgers/create` - Create new ledger with optional group assignment
- `GET /ledgers/{id}` - Retrieve ledger details
- `PUT /ledgers/{id}/status` - Update ledger status
- `DELETE /ledgers/{id}` - Archive ledger

#### Transaction Management:
- `POST /ledgers/{ledgerId}/transactions` - Add transaction to ledger
- `GET /ledgers/{ledgerId}/transactions/{transactionId}` - Get specific transaction
- `GET /ledgers/{ledgerId}/transactions` - List all transactions in ledger
- `GET /ledgers/{ledgerId}/transactions/range?startDate=...&endDate=...` - Filter by date range
- `DELETE /ledgers/{ledgerId}/transactions/{transactionId}` - Remove transaction

#### Group Management:
- `POST /groups/create` - Create ledger group
- `GET /groups/{id}` - Get group details
- `GET /groups/user/{userId}` - List user's groups

#### Membership Management:
- `POST /memberships/create` - Add user to ledger
- `GET /memberships/ledger/{ledgerId}` - List ledger members
- `PUT /memberships/{id}/role` - Update member role
- `DELETE /memberships/{id}` - Remove member

## 5. User Module: User Registration Domain

The User module handles user registration and basic user domain operations.

### Domain Model:
- **User Entity**: Represents registered users with immutable state
- **Value Objects**: UserName, Email (with validation)
- **Repository Interface**: UserRepository for user persistence

### Architecture:
- **UserRegistrationService**: Domain service for user registration with email uniqueness validation
- **Factory Methods**: Immutable user creation through static factory methods

### Features:
- **Email Validation**: Proper email format validation
- **Name Validation**: User name length and format constraints
- **Registration Logic**: Prevents duplicate email registrations

## 6. Selected Technology Stack

The implementation uses an enterprise-grade stack, focused on robustness and best practices.

- **Language:** Java 21 (OpenJDK).
- **Framework:** Spring Boot 3.2.0 (with Spring Web, Spring Data JPA).
- **Database:** PostgreSQL with UTF-8 encoding.
- **ORM:** Hibernate (via Spring Data JPA).
- **Build Tool:** Maven.
- **Testing:** JUnit 5 with Spring Boot Test.
- **Design Principles:** Rich Domain Model, Clean Architecture, Repository Pattern, Use Case Pattern, Dependency Injection, Immutability.

## 7. Setup Instructions

### Prerequisites
- Java 21 (OpenJDK)
- Maven 3.6+
- PostgreSQL 12+

### Configuration
1. Copy the example configuration file:
   ```bash
   cp src/main/resources/application.properties.example src/main/resources/application.properties
   ```

2. Update the `application.properties` file with your actual values:
   - Database credentials (PostgreSQL connection details)

### Database Setup
1. Create a PostgreSQL database named `lazyledger_db`
2. Update the connection details in `application.properties`

### Running the Application
```bash
mvn spring-boot:run
```

The application will start on port 8090.

## 8. Project Status and Next Steps

### Current Status:
✅ **Ledger Module**: Complete domain model, repositories, use cases, API layer, and transaction management
✅ **User Module**: Domain model for user registration with validation
✅ **Commons**: Shared identifiers, enums, and domain objects
✅ **Clean Architecture**: Proper separation of concerns with domain, application, infrastructure, and API layers
✅ **Database Integration**: PostgreSQL with JPA/Hibernate
✅ **REST API**: Complete RESTful endpoints for ledger and transaction management

### Architecture Highlights:
- **Modular Design**: Clean separation between ledger, user, and common concerns
- **Domain-Driven Design**: Rich domain models with value objects, entities, and domain services
- **Repository Pattern**: Abstraction over data persistence with Spring Data JPA
- **Use Case Pattern**: Business logic orchestration through dedicated use cases
- **Service Facade**: Simplified API interfaces hiding framework complexity
- **Immutable Entities**: Domain objects with proper validation and factory methods

### Next Steps:
🔜 **Voice Integration**: Add voice-to-text transcription capabilities (Google Gemini LLM)
🔜 **Telegram Bot**: Real-time message processing for voice transactions
🔜 **Security Module**: JWT authentication and API security with Spring Security
🔜 **Advanced Features**: Budget planning, export functionality, recurring transactions
🔜 **Infrastructure**: Docker containerization, CI/CD pipeline, monitoring, and database migrations