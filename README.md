# Zoo Management – Full-Stack (Spring Boot 3 & Angular)

Portfolio-oriented, production-like REST API for managing animals, vet visits, medications, feed logs, diet plans, and weight logs.
Clear business rules (400/404/409), global exception handling, and JWT-based security. Frontend: Angular admin UI integrates with this API.

## Table of Contents
- Features
- Tech Stack
- Getting Started
- API Documentation
- Postman Tests
- Example Requests
- Business Rules
- Project Structure
- License

## Features
- Domains: animals, vet visits, medications, feed logs, diet plans, weight logs (RESTful CRUD).
- Business rules: duplicate/overlap detection returns 409; validation errors return 400; missing resources return 404.
- Date/range logic: per-domain guards (e.g., same animal + timestamp or date-range cannot overlap).
- Security: JWT/Bearer authentication; obtain token via /api/auth/login and send with Authorization: Bearer <token>.
- Documentation and testing: Swagger/OpenAPI UI for exploration; Postman collection for smoke tests (CRUD + conflict).
- Persistence: H2 in-memory by default; MySQL configuration supported.
- Serialization: safe JSON for lazy relations to avoid cycles (e.g., ignore back-references, expose animalId).

## Tech Stack
Backend
- Java 17, Spring Boot 3 (Spring Web, Spring Security JWT/Bearer)
- Spring Data JPA, Hibernate, Jackson (JSON), REST/HTTP
- OpenAPI 3 / Swagger UI, Maven

Frontend
- Angular, TypeScript, RxJS, Angular CLI
- Angular HttpClient, Router, Reactive Forms
- HTML5, CSS3/SCSS

Database, Testing, Tooling
- H2, MySQL
- Postman (Collections)
- Git, GitHub

## Getting Started

Prerequisites
- JDK 17+
- Maven 3.9+
- Optional: MySQL 8+ (H2 in-memory works by default)

Quick run (H2 in-memory)
```bash
mvn clean spring-boot:run
# Swagger UI: http://localhost:8080/swagger-ui/index.html
# OpenAPI JSON: http://localhost:8080/v3/api-docs

Authentication (JWT)
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin"}'
# Response: { "token": "..." }
# Use it in requests: -H "Authorization: Bearer <token>"





