# Bank Transfer Core

## Requirements

- Java 21 (JRE)
- Docker (optional, only for PostgreSQL)

## Run (without Docker — H2 in-memory)

```bash
java -jar target/bank-transfer-core.jar
```

Uses H2 automatically. H2 Console: http://localhost:8080/h2-console

## Run (with PostgreSQL)

```bash
docker compose up -d
SPRING_PROFILES_ACTIVE=prod java -jar target/bank-transfer-core.jar
```

Flyway migrations run automatically on application startup.

## Build

```bash
./mvnw package -DskipTests
```

The JAR is generated at `target/bank-transfer-core.jar`.
