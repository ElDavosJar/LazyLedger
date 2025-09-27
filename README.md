# 🧉 LazyLedger: A Voice-Based Finance Ledger

A backend project designed to explore a low-friction data capture solution, built on solid domain design and clean architecture principles.

## 1. The Problem: The Friction of Manual Recording

My experience with expense management applications has been consistently short-lived. I've identified a fundamental barrier that always leads me to abandon them: the friction of the manual data entry process.

The cycle is predictable: the act of opening the app and filling out a form for each transaction quickly becomes tedious, leading to postponing records and ultimately abandonment.

## 2. Hypothesis and Objective

**Hypothesis:** If recording an expense were as easy as sending an audio, the success rate would increase dramatically.

**Project Objective:** Build a robust backend that can serve as the core for a voice-based transaction recording application, with modular architecture supporting ledgers and user management.

## 3. Architecture Overview

LazyLedger follows Clean Architecture principles with modular design:

- **Transaction Module**: Core transaction processing with voice/image input support
- **Ledger Module**: Multi-user ledger management with groups and memberships
- **User Module**: User registration and domain management
- **Commons**: Shared domain objects, identifiers, and enums

## 4. Transaction Module: Voice-Based Transaction Processing

The core innovation of LazyLedger is its ability to convert various input types (voice, images, documents, text) into structured financial transactions using advanced AI.

### Domain Model:
- **Transaction Entity**: Immutable entity with factory methods, representing financial transactions
- **Value Objects**: TransactionId, Amount (with currency), Description, TransactionDate
- **Repository Interface**: TransactionRepository for persistence operations

### Architecture:
- **Transcriber Interface**: Defines the contract for converting binary data to plain text.
- **AudioTranscriber**: Implements transcription for audio files using Google Gemini LLM.
- **ImageTranscriber**: Placeholder for OCR-based image transcription.
- **DocTranscriber**: Placeholder for document parsing to text.
- **TranscriberFactory**: Strategy pattern factory for selecting the appropriate transcriber based on media type.
- **TranscriptionService**: Orchestrates transcription and data extraction, returning TransactionDataDto list.
- **DataExtractor**: Static utility that analyzes transcription text with specialized financial prompts to extract transaction data.

### LLM Integration:
- **Multi-Phase Processing**: Binary Data → Plain Text → Structured Transaction Data
- **Business Rule Enforcement**: LLM follows strict financial categorization rules (positive amounts = INCOME, specific expense categories, ISO currency codes).
- **Date Handling**: Uses LLM-extracted dates when available, falls back to server date when not specified.

### Example Processing:
Input: WhatsApp voice note "Hoy he gastado veinte dólares en comida."
- **Phase 1 (Transcription)**: Audio → "Hoy he gastado veinte dólares en comida."
- **Phase 2 (Extraction)**: Text → {amount: -20.00, currency: "USD", category: "FOOD", description: "Comida"}

### Telegram Bot Integration: Real-Time Message Processing

LazyLedger integrates with Telegram Bot API to capture messages in real-time and process them automatically.

### Architecture:
- **ApiListener Interface**: Defines the contract for processing incoming updates.
- **TelegramApiListener**: Adapter that extracts raw message data (text, binary files) from Telegram updates.
- **MessageDto**: Universal DTO containing chatId, content, msg (caption), and binaryData.
- **LazyLedgerBot**: Telegram bot implementation using long polling for message reception.
- **MessageProcessorService**: Orchestrates transcription, extraction, and persistence, with asynchronous feedback to users.

### Features:
- **Multi-Media Support**: Handles text, audio, images, documents.
- **Binary Processing**: Downloads and transcribes files to plain text for LLM processing.
- **Asynchronous Feedback**: Sends confirmation messages to users after transaction saving, including transaction code.
- **Transaction Codes**: Sequential ID per user for UX (e.g., TX-001), with internal UUID for machine processing.

### User Experience:
1. User sends voice/image/document to bot.
2. System transcribes to text, extracts transaction data, saves to DB.
3. Bot replies: "Se ha guardado. Ha registrado un gasto de $20.00 USD en fecha 2025-09-24. Código de transacción: TX-001"

## 5. Ledger Module: Multi-User Ledger Management

The Ledger module provides collaborative financial management capabilities, allowing users to create and manage ledgers with multiple participants.

### Domain Model:
- **Ledger Entity**: Represents a financial ledger with status management (ACTIVE, INACTIVE, ARCHIVED)
- **LedgerGroup Entity**: Groups multiple ledgers under a single owner
- **LedgerMembership Entity**: Manages user roles within ledgers (OWNER, ASSISTANT, VIEWER)
- **Value Objects**: LedgerName, LedgerId, LedgerGroupId, UserId
- **Enums**: LedgerStatus, LedgerUserRole

### Architecture:
- **LedgerRepository**: Interface for ledger persistence operations
- **LedgerGroupRepository**: Interface for ledger group management
- **LedgerMembershipRepository**: Interface for membership operations
- **LedgerUserService**: Application service for managing user memberships and roles

### Features:
- **Ledger Lifecycle**: Create, deactivate, archive, and unarchive ledgers
- **Group Management**: Organize ledgers into groups for better organization
- **Role-Based Access**: Different permission levels for ledger participants
- **Membership Management**: Invite users, change roles, remove members

## 6. User Module: User Registration Domain

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

## 7. Selected Technology Stack

The implementation uses an enterprise-grade stack, focused on robustness and best practices.

- **Language:** Java 21 (OpenJDK).
- **Framework:** Spring Boot 3.2.0 (with Spring Web, Spring Data JPA).
- **Database:** PostgreSQL with UTF-8 encoding.
- **ORM:** Hibernate (via Spring Data JPA).
- **AI Integration:** Google Gemini LLM for multi-modal transcription and financial data extraction.
- **Bot Integration:** Telegram Bot API for real-time message processing.
- **Build Tool:** Maven.
- **Testing:** JUnit 5 with Spring Boot Test.
- **Design Principles:** Rich Domain Model, Clean Architecture, Strategy Pattern, Dependency Injection, Immutability.

## 8. Setup Instructions

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
   - Database credentials
   - Google AI API key (from Google AI Studio)
   - Telegram bot token (from @BotFather)

### Database Setup
1. Create a PostgreSQL database named `lazyledger_db`
2. Update the connection details in `application.properties`

### Running the Application
```bash
mvn spring-boot:run
```

The application will start on port 8090 and the Telegram bot will be active.

## 9. Project Status and Next Steps

### Current Status:
✅ **Transaction Module**: Complete domain model, infrastructure, API, transcription with LLM, Telegram bot integration, sequential numbering.
✅ **Ledger Module**: Domain entities (Ledger, LedgerGroup, LedgerMembership), repositories, services for multi-user ledger management.
✅ **User Module**: Domain model for user registration with validation.
✅ **Commons**: Shared identifiers, enums, and domain objects.
✅ **Security**: Sensitive configuration removed from repository, proper .gitignore setup.

### Considerations:
- **Modular Architecture**: Clean separation between transaction, ledger, and user concerns.
- **Domain-Driven Design**: Rich domain models with value objects, entities, and domain services.
- **Transaction IDs**: UUID for internal machine processing, sequential per-user codes (e.g., TX-001) for UX.
- **Security**: Sensitive configs (API keys, tokens) excluded from git, prepared for secret vault integration.
- **Architecture**: Clean separation with adapters for external APIs, strategy pattern for transcribers, use cases for business logic.

### Next Steps:
🔜 **Infrastructure Implementation**: Complete persistence layer for ledger and user modules with Spring Data JPA.
🔜 **API Layer**: RESTful APIs for ledger and user management.
🔜 **Security Module**: JWT authentication, API security with Spring Security, user verification for Telegram bot.
🔜 **Advanced Features**: Transaction categories management, budget planning, export functionality, recurring transactions.
🔜 **Infrastructure**: Docker containerization, CI/CD pipeline, monitoring, and database migrations.