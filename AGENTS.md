# AGENTS.md

## Project

Multi-vendor e-commerce platform. Two separate apps in one repo:

| App | Dir | Stack | Port |
|-----|-----|-------|------|
| Backend | `src/` | Spring Boot 4.0.1 + Java 17 + Maven | `5454` |
| Frontend | `frontent/` (note the typo) | CRA + TypeScript + React 19 + MUI v9 + Tailwind | `3000` |

- Package: `com.penguinshop`
- DB: MariaDB with `ddl-auto=update` (schema auto-managed, no migrations)
- Auth: JWT + OTP login (via email)
- Payments: Razorpay + Stripe
- JPA annotations: `@EqualsAndHashCode` present on `User` but **not** on `Product` (inconsistency)

## Build & Run

```bash
./mvnw spring-boot:run          # Backend (port 5454)
cd frontent && npm start         # Frontend (port 3000, proxy to 5454)
./mvnw test                      # All backend tests (only 1 exists: contextLoads)
cd frontent && npm test           # Frontend tests (CRA watch mode)
cd frontent && npm run build      # Frontend production build
```

## Required Env Vars

Set these for the backend (no `.env` loader — Spring reads env directly):

```
MARIA_DB_USERNAME, MARIA_DB_PASSWORD
SPRING_MAIL_USERNAME, SPRING_MAIL_PASSWORD
RAZORPAY_API_KEY, RAZORPAY_API_SECRET
STRIPE_SECRET_KEY
```

## Security

- `/api/**` — requires JWT (except `/api/products/*/reviews`)
- All other paths (`/auth/**`, etc.) — public
- JWT header: `Authorization: Bearer <token>`

## Architecture

- **Controllers** in `controller/`, **service interfaces** in `service/`, **implementations** in `impl/`
- All services use `@RequiredArgsConstructor` + constructor injection (Lombok)
- Global error handling via `@ControllerAdvice` in `exceptions/GlobalException.java`
- **Known quirk**: `WishlistServiceimpl.java` breaks PascalCase (lowercase `i`)

## Frontend

- CRA with `react-scripts` (configs abstracted, no custom webpack)
- MUI v9 (`@mui/material`), Tailwind v3, Emotion for styling
- `src/customer/components/` — component directory

## Code Style

- Extensive inline comments explaining business logic — follow this convention
- No linter or formatter configured
