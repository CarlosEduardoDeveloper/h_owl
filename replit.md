# Fundação Mobile + Backend

Base técnica de um aplicativo React Native conectado a uma API Spring Boot e PostgreSQL.

## Run & Operate

- `docker compose up --build` — start PostgreSQL and the Spring Boot backend
- `cd backend && mvn test` — run backend tests with Java 21
- `pnpm --filter @workspace/mobile run dev` — start the Expo mobile app
- `pnpm run typecheck` — full typecheck across all packages
- `pnpm run build` — typecheck + build all packages
- `pnpm --filter @workspace/api-spec run codegen` — regenerate API hooks and Zod schemas from the OpenAPI spec
- Required backend env: `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USERNAME`, `DB_PASSWORD`
- YouVersion (Replit Secrets): `YVP_APP_KEY`, `YVP_CALLBACK_URL=https://fundacao-mobile-backend.replit.app/callback`
- Required mobile env: `EXPO_PUBLIC_API_BASE_URL`

## Stack

- Mobile: React Native 0.81 + Expo SDK 54 + TypeScript strict
- Backend: Java 21 + Spring Boot 4.0.0 + Maven
- API: Spring Web MVC + Bean Validation + Actuator
- DB: PostgreSQL 17 + Spring Data JPA
- Migrations: Flyway
- API contract: OpenAPI + Orval-generated TypeScript client

## Where things live

- `backend/` — Spring Boot modular monolith
- `artifacts/mobile/` — Expo/React Native app
- `backend/src/main/resources/db/migration/` — Flyway migrations
- `lib/api-spec/openapi.yaml` — API contract source of truth
- `artifacts/mobile/constants/colors.ts` — temporary native theme tokens

## Architecture decisions

- Modules are organized by feature under `backend/.../modules`, not by global technical layers.
- The first Flyway migration intentionally has no business tables; schema starts with the first real module.
- JPA uses `ddl-auto=validate`; Flyway is the only schema evolution mechanism.
- The mobile app uses the generated API client and keeps the API base URL configurable for emulators and physical devices.

## Product

This is the initial technical foundation only. The temporary mobile screen reports whether the API integration is online.

## User preferences

Product screens and business capabilities will be added only when their requirements are provided.

## Gotchas

- The workflow uses the installed Java 21 JDK explicitly because the default Maven launcher may resolve an older JDK.
- A physical device cannot use its own `localhost` to reach the development machine; set `EXPO_PUBLIC_API_BASE_URL` to a reachable host address.

## Pointers

- See `README.md` for setup and environment variables.
- See the pnpm workspace guidance for shared TypeScript package conventions.
