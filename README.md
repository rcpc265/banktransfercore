# Bank Transfer Core

## Running with PostgreSQL

The application requires a PostgreSQL database. Start it with Docker Compose:

```bash
docker compose up -d
```

To stop:

```bash
docker compose down
```

The database will be available at `localhost:5432` with:
- Database: `bank_transfer`
- Username: `rafael`
- Password: `password`

Flyway migrations run automatically on application startup.
