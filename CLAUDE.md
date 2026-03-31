# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project

Spring Boot 3.4.4 REST API (Java 21) for sending emails via SMTP. Single endpoint: `POST /email`. Uses Lombok, Java Records, and supports GraalVM native image compilation.

## Build & Run Commands

```bash
./mvnw clean package              # Build
./mvnw test                       # Run all tests
./mvnw test -Dtest=ClassName      # Run a single test class
./mvnw spring-boot:run            # Run the app
./mvnw spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"  # Run with dev profile (CORS=*)
./mvnw clean package -DskipTests -Pnative native:compile  # Build GraalVM native image
```

## Architecture

All source is in `com.andreferraz.mailservice` (single flat package):

- **MailSenderController** — REST controller, `POST /email` accepts `EmailRequestBody` (record: senderName, senderEmailAddress, text), returns 201
- **MailSenderService** — Sends email using Spring's `MailSender`; sends TO the configured mail username (contact-form pattern)
- **CorsConfig** — CORS for `/email`, allowed origins from `${cors.allowedOrigins}` (wildcard in dev profile)
- **DefaultExceptionHandler** — `@ControllerAdvice` catching `MailException`, returns `ErrorResponse` record with 500

## Configuration

SMTP and CORS configured via environment variables (see `application.properties`): `MAIL_HOST`, `MAIL_PORT`, `MAIL_USERNAME`, `MAIL_PASSWORD`, `MAIL_SMTP_AUTH`, `MAIL_SMTP_STARTTLS_ENABLED`, `CORS_ALLOWED_ORIGINS`. A `.env` file is gitignored.

## Key Conventions

- Constructor injection via Lombok `@RequiredArgsConstructor`
- Java Records for DTOs (`EmailRequestBody`, `ErrorResponse`)
- Logging via Lombok `@Slf4j`
