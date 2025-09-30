[🇪🇸 Español](#-lazyledger-gestión-colaborativa-de-libros-contables-financieros) | [🇺🇸 English](#-lazyledger-multi-user-financial-ledger-management)

---

# 🧉 LazyLedger: Gestión Colaborativa de Libros Contables Financieros

Un proyecto backend diseñado para explorar soluciones de gestión financiera colaborativa, construido sobre principios sólidos de diseño de dominio y arquitectura limpia.

## 1. El Problema: La Fricción del Registro Manual

Mi experiencia con aplicaciones de gestión de gastos ha sido consistentemente efímera. He identificado una barrera fundamental que siempre me lleva a abandonarlas: la fricción del proceso de entrada manual de datos.

El ciclo es predecible: el acto de abrir la aplicación y completar un formulario para cada transacción rápidamente se vuelve tedioso, llevando a posponer registros y finalmente al abandono.

## 2. Hipótesis y Objetivo

**Hipótesis:** Si registrar un gasto fuera tan fácil como enviar un audio, la tasa de éxito aumentaría dramáticamente.

**Objetivo del Proyecto:** Construir un backend robusto que pueda servir como núcleo para una aplicación de registro de transacciones basada en voz, con arquitectura modular que soporte libros contables y gestión de usuarios.

## 3. Resumen de Arquitectura

LazyLedger sigue principios de Arquitectura Limpia con diseño modular:

- **Módulo Ledger**: Gestión de libros contables multiusuario con grupos, membresías y manejo de transacciones
- **Módulo User**: Registro de usuarios y operaciones de dominio de usuario
- **Commons**: Objetos de dominio compartidos, identificadores y enums

## 4. Módulo Ledger: Gestión Financiera Colaborativa

El módulo Ledger proporciona capacidades integrales de gestión financiera, permitiendo a los usuarios crear y gestionar libros contables con múltiples participantes y manejar transacciones dentro de esos libros.

### Modelo de Dominio:
- **Entidad Ledger**: Representa un libro contable financiero con gestión de estado (ACTIVE, INACTIVE, ARCHIVED)
- **Entidad LedgerGroup**: Agrupa múltiples libros contables bajo un solo propietario
- **Entidad LedgerMembership**: Gestiona roles de usuario dentro de libros contables (OWNER, MEMBER)
- **Entidad Transaction**: Transacciones financieras vinculadas a libros contables específicos
- **Objetos de Valor**: LedgerName, LedgerId, LedgerGroupId, UserId, TransactionId, Amount, Description, TransactionDate
- **Enums**: LedgerStatus, LedgerUserRole, Category

### Arquitectura:
- **Patrón Repository**: Abstracción limpia sobre persistencia de datos con Spring Data JPA
- **Patrón Use Case**: Orquestación de lógica de negocio a través de casos de uso dedicados
- **Service Facade**: Interfaces de API simplificadas que ocultan complejidad del framework
- **Domain-Driven Design**: Modelos de dominio ricos con validación y reglas de negocio

### Características:
- **Gestión del Ciclo de Vida del Ledger**: Crear, desactivar, archivar y desarchivar libros contables
- **Organización en Grupos**: Organizar libros contables en grupos jerárquicos
- **Control de Acceso Basado en Roles**: Diferentes niveles de permisos para participantes del libro
- **Gestión de Membresías**: Invitación de usuarios y asignación de roles
- **Gestión de Transacciones**: Operaciones CRUD completas para transacciones del libro
- **Clasificación por Categorías**: Categorías predefinidas de gastos e ingresos
- **Consultas por Rango de Fechas**: Filtrado de transacciones por rangos de fechas

### Endpoints de API:

#### Gestión de Ledger:
- `POST /ledgers/create` - Crear nuevo libro contable con asignación opcional de grupo
- `GET /ledgers/{id}` - Recuperar detalles del libro contable
- `PUT /ledgers/{id}/status` - Actualizar estado del libro contable
- `DELETE /ledgers/{id}` - Archivar libro contable

#### Gestión de Transacciones:
- `POST /ledgers/{ledgerId}/transactions` - Agregar transacción al libro contable
- `GET /ledgers/{ledgerId}/transactions/{transactionId}` - Obtener transacción específica
- `GET /ledgers/{ledgerId}/transactions` - Listar todas las transacciones en el libro
- `GET /ledgers/{ledgerId}/transactions/range?startDate=...&endDate=...` - Filtrar por rango de fechas
- `DELETE /ledgers/{ledgerId}/transactions/{transactionId}` - Eliminar transacción

#### Gestión de Grupos:
- `POST /groups/create` - Crear grupo de libros contables
- `GET /groups/{id}` - Obtener detalles del grupo
- `GET /groups/user/{userId}` - Listar grupos del usuario

#### Gestión de Membresías:
- `POST /memberships/create` - Agregar usuario al libro contable
- `GET /memberships/ledger/{ledgerId}` - Listar miembros del libro
- `PUT /memberships/{id}/role` - Actualizar rol del miembro
- `DELETE /memberships/{id}` - Eliminar miembro

## 5. Módulo User: Dominio de Registro de Usuarios

El módulo User maneja el registro de usuarios y operaciones básicas del dominio de usuario.

### Modelo de Dominio:
- **Entidad User**: Representa usuarios registrados con estado inmutable
- **Objetos de Valor**: UserName, Email (con validación)
- **Interfaz Repository**: UserRepository para persistencia de usuarios

### Arquitectura:
- **UserRegistrationService**: Servicio de dominio para registro de usuarios con validación de unicidad de email
- **Métodos Factory**: Creación inmutable de usuarios a través de métodos factory estáticos

### Características:
- **Validación de Email**: Validación apropiada de formato de email
- **Validación de Nombre**: Restricciones de longitud y formato del nombre de usuario
- **Lógica de Registro**: Previene registros duplicados de email

## 6. Stack Tecnológico Seleccionado

La implementación utiliza un stack de grado empresarial, enfocado en robustez y mejores prácticas.

- **Lenguaje:** Java 21 (OpenJDK).
- **Framework:** Spring Boot 3.2.0 (con Spring Web, Spring Data JPA).
- **Base de Datos:** PostgreSQL con codificación UTF-8.
- **ORM:** Hibernate (vía Spring Data JPA).
- **Herramienta de Build:** Maven.
- **Testing:** JUnit 5 con Spring Boot Test.
- **Principios de Diseño:** Modelo de Dominio Rico, Arquitectura Limpia, Patrón Repository, Patrón Use Case, Inyección de Dependencias, Inmutabilidad.

## 7. Instrucciones de Configuración

### Prerrequisitos
- Java 21 (OpenJDK)
- Maven 3.6+
- PostgreSQL 12+

### Configuración
1. Copiar el archivo de configuración de ejemplo:
   ```bash
   cp src/main/resources/application.properties.example src/main/resources/application.properties
   ```

2. Actualizar el archivo `application.properties` con tus valores reales:
   - Credenciales de base de datos (detalles de conexión PostgreSQL)

### Configuración de Base de Datos
1. Crear una base de datos PostgreSQL llamada `lazyledger_db`
2. Actualizar los detalles de conexión en `application.properties`

### Ejecutar la Aplicación
```bash
mvn spring-boot:run
```

La aplicación iniciará en el puerto 8090.

### Acceso a la Documentación API
Una vez que la aplicación esté ejecutándose, puedes acceder a la documentación interactiva de la API en:
- **Swagger UI**: `http://localhost:8090/swagger-ui.html`
- **OpenAPI JSON**: `http://localhost:8090/v3/api-docs`
- **OpenAPI YAML**: `http://localhost:8090/v3/api-docs.yaml`

## 8. Estado del Proyecto y Próximos Pasos

### Estado Actual:
✅ **Módulo Ledger**: Modelo de dominio completo, repositorios, casos de uso, capa API y gestión de transacciones
✅ **Módulo User**: Modelo de dominio para registro de usuarios con validación
✅ **Commons**: Identificadores compartidos, enums y objetos de dominio
✅ **Arquitectura Limpia**: Separación apropiada de responsabilidades con capas de dominio, aplicación, infraestructura y API
✅ **Integración de Base de Datos**: PostgreSQL con JPA/Hibernate
✅ **API REST**: Endpoints RESTful completos para gestión de libros contables y transacciones
✅ **Manejo de Excepciones por Capas**: Sistema completo de excepciones jerárquicas con validación end-to-end
✅ **Documentación OpenAPI**: Documentación interactiva completa con SpringDoc OpenAPI 3.x

### Aspectos Destacados de Arquitectura:
- **Diseño Modular**: Separación limpia entre preocupaciones de ledger, usuario y comunes
- **Domain-Driven Design**: Modelos de dominio ricos con objetos de valor, entidades y servicios de dominio
- **Patrón Repository**: Abstracción sobre persistencia de datos con Spring Data JPA
- **Patrón Use Case**: Orquestación de lógica de negocio a través de casos de uso dedicados
- **Service Facade**: Interfaces de API simplificadas que ocultan complejidad del framework
- **Entidades Inmutables**: Objetos de dominio con validación apropiada y métodos factory

### Próximos Pasos:

🔜 **Integración de Voz**:
- Agregar capacidades de transcripción de voz a texto (Google Gemini LLM)
- Implementar procesamiento de audio para extracción de datos de transacciones

🔜 **Bot de Telegram**:
- Procesamiento de mensajes en tiempo real para transacciones de voz
- Integración con Telegram Bot API para recepción de mensajes de audio

🔜 **Módulo de Seguridad**:
- Autenticación JWT y seguridad de API con Spring Security
- Implementar control de acceso basado en roles para operaciones de ledger

🔜 **Características Avanzadas**:
- Planificación de presupuestos y alertas de gastos
- Funcionalidad de exportación (PDF, Excel, CSV)
- Transacciones recurrentes y recordatorios automáticos

🔜 **Infraestructura**:
- Containerización Docker completa del proyecto
- Pipeline CI/CD con GitHub Actions
- Monitoreo y logging con ELK stack
- Migraciones de base de datos con Flyway

---

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

### API Documentation Access
Once the application is running, you can access the interactive API documentation at:
- **Swagger UI**: `http://localhost:8090/swagger-ui.html`
- **OpenAPI JSON**: `http://localhost:8090/v3/api-docs`
- **OpenAPI YAML**: `http://localhost:8090/v3/api-docs.yaml`

## 8. Project Status and Next Steps

### Current Status:
✅ **Ledger Module**: Complete domain model, repositories, use cases, API layer, and transaction management
✅ **User Module**: Domain model for user registration with validation
✅ **Commons**: Shared identifiers, enums, and domain objects
✅ **Clean Architecture**: Proper separation of concerns with domain, application, infrastructure, and API layers
✅ **Database Integration**: PostgreSQL with JPA/Hibernate
✅ **REST API**: Complete RESTful endpoints for ledger and transaction management
✅ **Layered Exception Handling**: Complete hierarchical exception system with end-to-end validation
✅ **OpenAPI Documentation**: Complete interactive documentation with SpringDoc OpenAPI 3.x

### Architecture Highlights:
- **Modular Design**: Clean separation between ledger, user, and common concerns
- **Domain-Driven Design**: Rich domain models with value objects, entities, and domain services
- **Repository Pattern**: Abstraction over data persistence with Spring Data JPA
- **Use Case Pattern**: Business logic orchestration through dedicated use cases
- **Service Facade**: Simplified API interfaces hiding framework complexity
- **Immutable Entities**: Domain objects with proper validation and factory methods

### Next Steps:

🔜 **Voice Integration**:
- Add voice-to-text transcription capabilities (Google Gemini LLM)
- Implement audio processing for transaction data extraction

🔜 **Telegram Bot**:
- Real-time message processing for voice transactions
- Integration with Telegram Bot API for audio message reception

🔜 **Security Module**:
- JWT authentication and API security with Spring Security
- Implement role-based access control for ledger operations

🔜 **Advanced Features**:
- Budget planning and expense alerts
- Export functionality (PDF, Excel, CSV)
- Recurring transactions and automatic reminders

🔜 **Infrastructure**:
- Complete Docker containerization of the project
- CI/CD pipeline with GitHub Actions
- Monitoring and logging with ELK stack
- Database migrations with Flyway