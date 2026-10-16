# REX Backend

Spring Boot 3.4 / Java 21 backend for the REX multi-tenant invoice, receipt, and expense management platform.

## Stack

- Spring Boot 3.4 + Spring Security (Keycloak OIDC)
- Spring Data JPA + PostgreSQL 16
- Flyway migrations
- MapStruct, Lombok
- Swagger / OpenAPI

## Prerequisites

- Java 21
- Maven
- Docker (for PostgreSQL + Keycloak)

## Setup

### 1. Start infrastructure

```bash
docker compose up -d
```

This starts PostgreSQL and Keycloak.

### 2. Configure environment

```bash
cp .env.example .env
```

Edit `.env` and fill in any values marked `change-me`. The defaults are sufficient for local development except for secrets you need to supply (e.g. Hetzner S3 keys, SMTP credentials).

### 3. Run the application

Load the `.env` file and start the app:

```bash
export $(grep -v '^#' .env | xargs) && mvn spring-boot:run
```

Or if you use a tool like [direnv](https://direnv.net/), add a `.envrc`:

```bash
dotenv
```

Then just run:

```bash
mvn spring-boot:run
```

The API will be available at `http://localhost:8080`.  
Swagger UI: `http://localhost:8080/swagger-ui/index.html`

## Environment Variables

See [`.env.example`](.env.example) for the full list with descriptions. Key variables:

| Variable | Description |
|---|---|
| `POSTGRES_USER` / `POSTGRES_PASSWORD` | Database credentials |
| `JWT_SECRET_KEY` | Secret for signing JWT tokens |
| `KEYCLOAK_JWKS_URL` | Keycloak JWKS endpoint for token validation |
| `SMTP_*` | Mail server configuration |
| `HETZNER_S3_ACCESS_KEY` / `HETZNER_S3_SECRET_KEY` | Object storage credentials |
| `INVOICE_SYNC_ENABLED` | Enable D-velop invoice sync (default: `false`) |

## Database Migrations

Flyway runs automatically on startup. Migrations live in `rex-backend-flyway/rex-backend-flyway/migrations/`.

To run migrations manually:

```bash
mvn flyway:migrate
```

## License

Apache License 2.0
