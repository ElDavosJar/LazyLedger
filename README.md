# 🧉 LazyLedger: A Voice-Based Finance Ledger

A backend project designed to explore a low-friction data capture solution, built on solid domain design and clean architecture principles.

## 1. The Problem: The Friction of Manual Recording

My experience with expense management applications has been consistently short-lived. I've identified a fundamental barrier that always leads me to abandon them: the friction of the manual data entry process.

The cycle is predictable: the act of opening the app and filling out a form for each transaction quickly becomes tedious, leading to postponing records and ultimately abandonment.

## 2. Hypothesis and Objective

**Hypothesis:** If recording an expense were as easy as sending an audio, the success rate would increase dramatically.

**Project Objective:** Build a robust backend that can serve as the core for a voice-based transaction recording application.

## 3. Domain Design (Current State: Foundation Complete!)

The heart of the application is its domain model. The design has focused on creating a solid foundation, technology-agnostic and highly maintainable before writing any other layer of the system.

The Transaction entity protects its own state (immutability, no setters) and is created through static factory methods. The use of Value Objects (TransactionId, Amount, etc.) ensures type safety and adds business context to the code.

This diagram represents the current and complete state of the domain model:

![Domain Model Diagram](./docs/diagrams/domain-model.jpg)

*(Note: Link to your domain class diagram)*

## Activity Diagram

This diagram shows the happy path flow of the application:

![Activity Diagram](./docs/diagrams/activity-diagram.jpg)

## 4. Transcription Module: Voice-to-Transaction Processing

The core innovation of LazyLedger is its ability to convert voice recordings into structured financial transactions using advanced AI.

### Architecture:
- **AudioTranscriber**: Static utility class that uses Google Gemini LLM to transcribe audio files to raw text.
- **DataExtractor**: Static utility class that analyzes transcription text with specialized financial prompts to extract transaction data (amount, currency, category, description, date).
- **TransactionDataDto**: Data transfer object containing extracted transaction fields with proper typing and nullability.

### LLM Integration:
- **Two-Phase Processing**: Audio → Raw Text → Structured Transaction Data
- **Business Rule Enforcement**: LLM follows strict financial categorization rules (positive amounts = INCOME, specific expense categories, ISO currency codes).
- **Date Handling**: Uses LLM-extracted dates when available, falls back to server date when not specified.

### Example Processing:
Input: WhatsApp voice note "Hoy he ganado en el canto trece dólares con nueve centavos."
- **Phase 1 (Transcription)**: Audio → "Hoy he ganado en el canto trece dólares con nueve centavos."
- **Phase 2 (Extraction)**: Text → {amount: 13.09, currency: "USD", category: "INCOME", description: "Ganancia en el canto"}

## 5. Selected Technology Stack

The implementation of this domain is being done with an enterprise-grade stack, focused on robustness and best practices.

- **Language:** Java 21 (OpenJDK).
- **Framework:** Spring Boot 3.2.0 (with Spring Web, Spring Data JPA).
- **Database:** PostgreSQL.
- **ORM:** Hibernate (via Spring Data JPA).
- **AI Integration:** Google Gemini LLM for audio transcription and financial data extraction.
- **Build Tool:** Maven.
- **Testing:** JUnit 5 with Spring Boot Test.
- **Design Principles:** Rich Domain Model (above anemic model), Clean Architecture foundations, Immutability.

## 5. Project Status and Next Steps

### Current Status:
✅ Domain Model (domain) defined and fully implemented. Includes the Transaction entity, Value Objects, and the TransactionRepository interface.
✅ Infrastructure layer (infrastructure) implemented using Spring Data JPA and PostgreSQL.
✅ RESTful API (api) built with Spring Boot to expose CRUD operations with standardized ApiResponse format.
✅ Transcription Module implemented with Google Gemini LLM integration for audio-to-text transcription and financial data extraction.
✅ MVP Core Features: Audio file processing, transaction extraction, and persistence with business rule validation.

### Next Steps:
🔜 Integrate Telegram Bot API for voice message capture and automated processing.
🔜 Deploy as a single service with Java-based automation (instead of n8n) for better performance and flexibility.
🔜 Add user authentication and multi-user support.
🔜 Implement transaction categorization improvements and reporting features.