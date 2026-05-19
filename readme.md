# Bank Transfer Core

A clean-architecture, high-performance REST API designed to process internal bank transfers with strict domain isolation and progressive persistence phases. Built to demonstrate corporate engineering standards, resource optimization, and modern software lifecycle management.

## 🚀 Tech Stack & Core Decisions

* **Backend Core:** Java 21 & Spring Boot 3.x.
* **Concurrency Strategy:** Designed for high-throughput scaling via Java 21's OS-thread detachment capabilities (Virtual Threads optimization roadmap).
* **Data Precision:** Absolute financial accuracy using `BigDecimal` arithmetic to prevent floating-point calculation errors.
* **Architecture:** Hexagonal Architecture (Ports & Adapters) ensuring 100% decoupling of core business logic from frameworks and drivers.

## 🗺️ Engineering Roadmap

### Phase 1: Core Domain & Operational Skeleton (Current)
* Implementation of decoupled domain entities (`Account`, `Transfer`) utilizing primitive-free Java validation rules.
* Automated Schema DDL mapping via Hibernate configuration for rapid delivery and early domain verification.
* Standardized Input Validation using Jakarta validation constraints.

### Phase 2: Enterprise Persistence & Defensive Locking (In Development)
* Transition from automated DDL to immutable database version control utilizing **Flyway Migrations**.
* Implementation of **Pessimistic Locking (`@Lock`)** strategies to guarantee strict data consistency and eliminate race conditions under high concurrent transfer requests.
* Global Exception Interception via `@RestControllerAdvice` to guarantee clean API contracts and eliminate raw stack traces.

### Phase 3: High Availability & QA Verification (Planned)
* Idempotency layer implementation to handle network retry mechanisms safely.
* Unit and integration testing coverage with JUnit 5 and Mockito focusing on domain boundary conditions.
* Load testing execution scripts via Apache JMeter to simulate extreme concurrent stress.

## 🛠️ Architecture Blueprint

```text
       [ Infrastructure Layer ]
  (HTTP Controllers / Spring Data JPA)
               │       ▲
               ▼       │
        [ Application Ports ]
             (Interfaces)
               │       ▲
               ▼       │
        [ Core Domain Layer ]
          (Pure Java Engines)