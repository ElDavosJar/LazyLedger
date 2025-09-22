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

![Domain Model Diagram](./docs/diagrams/domain-model.png)

*(Note: Link to your domain class diagram)*

## Activity Diagram

This diagram shows the happy path flow of the application:

![Activity Diagram](./docs/diagrams/activity-diagram.png)

## 4. Selected Technology Stack

The implementation of this domain is being done with an enterprise-grade stack, focused on robustness and best practices.

- **Language:** Java 21 (OpenJDK).
- **Design Principles:** Pragmatic Domain-Driven Design (DDD), Clean Architecture, Immutability.

## 5. Project Status and Next Steps

### Current Status:
✅ Domain Model (domain) defined and fully implemented. Includes the Transaction entity, Value Objects, and the TransactionRepository interface.

### Next Steps:
🔜 Implement the Infrastructure layer (infrastructure) using Spring Data JPA and PostgreSQL.  
🔜 Build the RESTful API (api) with Spring Boot to expose CRUD operations.  
🔜 Integrate with n8n for the voice capture workflow.