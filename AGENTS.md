# AGENTS.md

Short guide for people (and agents) working in this repository.

## 1) Project goal

- Backend REST API for home budget management (`home-budget-api`).
- Stack: Kotlin + Spring Boot + PostgreSQL.
- The application serves data for clients (mobile/desktop/web).

## 2) Architecture and code structure

Layers (simple split):

1. `controller` - HTTP endpoints (`/api/...`), request/response mapping.
2. `service` - business and transactional logic.
3. `repository` - database access using `JdbcTemplate` and SQL.
4. `entity` - DTO/data models used between layers.

Important directories:

- `src/main/kotlin/com/example/demo/controller`
- `src/main/kotlin/com/example/demo/service`
- `src/main/kotlin/com/example/demo/repository`
- `src/main/kotlin/com/example/demo/entity`
- `src/test/kotlin/com/example/demo`
- `src/test/resources/schema.sql`

Architecture notes:

- SQL is centralized in `SqlQueries.kt`.
- JDBC operations are unified through `RepositoryHelper`.
- For multi-step DB/account-state changes, use `@Transactional` in the `service` layer.
- Scheduling is enabled in the app (`@EnableScheduling`).

## 3) Coding standards

Kotlin/Spring style:

- Prefer constructor injection (`private val ...` in class constructor).
- Keep business logic in `service`, not in controllers.
- Controllers should stay thin: input validation + delegation to service.
- Repositories should only handle data access.
- Reuse existing mappers and JDBC helpers instead of duplicating logic.
- Follow the current project style (even if it is not fully textbook).

Practical conventions:

- Do not change existing API endpoints unless necessary (client compatibility).
- For new SQL queries, add constants/methods in `SqlQueries.kt`.
- For financially critical changes, add integration tests.

## 4) Build and run

Requirements:

- Java 17
- Gradle Wrapper (use `./gradlew` / `./gradlew.bat`)

Most common commands:

- `./gradlew test` - run tests
- `./gradlew build` - full build
- `./gradlew test --warning-mode all` - tests + deprecation warnings

Docker/Jib:

- Image build is configured via `com.google.cloud.tools.jib` plugin.
- Base image: distroless Java 17.

## 5) Tests

The project relies mainly on integration tests.

How it works:

- Base class: `IntegrationTest`.
- Testcontainers starts PostgreSQL (`postgres:15-alpine`).
- Test schema is loaded from `src/test/resources/schema.sql`.
- After each test, tables are truncated and sequences are reset.
- Test profile: `@ActiveProfiles("test")`.
- `TestConfig` replaces the clock with `FakeClockProvider` for deterministic tests.

Testing best practices:

- Use descriptive test names (what should work).
- Prepare data using helpers from `IntegrationTest` (`createAccount`, `createInvoice`, etc.).
- Test both positive and negative cases (for example: missing data, null, conflict).

## 6) Change rules for agents

- Compatibility first, refactor second.
- Do not remove existing endpoints/deprecated flows without an explicit decision.
- After changes, run at least `./gradlew test`.
- For build/dependency changes, run `./gradlew test --warning-mode all`.
- Avoid unrelated changes.

## 7) Git boundaries (STRICT)

- Never run risky Git commands (for example: `reset --hard`, `clean -fd`, `rebase`, `cherry-pick`, `revert`, `push --force`, `checkout` that overwrites changes).
- Never fetch/pull remote changes (`git fetch`, `git pull`).
- Never delete files (neither via Git nor shell).
- Allowed Git operation: only local staging of new files (`git add <new-files>`).
- Do not commit, push, or rewrite history without a separate explicit user decision.
