# 🗓️ Schedule Management Project

A production-grade Schedule Management REST API built phase by phase, covering backend development, DevOps, security, caching, monitoring, and a React frontend.

---

## 🛠️ Tech Stack

| Layer | Technology |
|---|---|
| Backend | Java 17, Spring Boot 3.5.11 |
| Database | PostgreSQL 15 |
| Migrations | Flyway |
| Caching | Redis 7 |
| Security | Spring Security + JWT |
| Containerization | Docker + Docker Compose |
| Orchestration | Kubernetes (kind) |
| CI/CD | GitHub Actions |
| Monitoring | Prometheus + Grafana |
| API Docs | Swagger / OpenAPI |
| Frontend | React + Vite + Nginx |

---

## 🚀 Running Locally

### Prerequisites
- Java 17
- Maven
- Docker

### Start with Docker Compose
```bash
docker compose up --build
```

| Service | URL |
|---|---|
| Backend API | http://localhost:8081 |
| Frontend | http://localhost:3001 |
| Swagger UI | http://localhost:8081/swagger-ui.html |
| Prometheus | http://localhost:9091 |
| Grafana | http://localhost:3002 (admin/admin) |

---

## ☸️ Kubernetes (kind)

```bash
kind create cluster --name schedule-cluster
kubectl apply -f k8s/namespace.yml
kubectl apply -f k8s/configmap.yml
kubectl apply -f k8s/secret.yml
kubectl apply -f k8s/postgres-deployment.yml
kubectl apply -f k8s/redis-deployment.yml
kubectl apply -f k8s/app-deployment.yml
kubectl port-forward service/schedule-app-service 8082:8080 -n schedule-management
```

---

## 🔁 API Endpoints

### Auth (Public)
| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/auth/register` | Register a new user |
| POST | `/api/auth/login` | Login and get JWT token |

### Schedules (Protected)
| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/schedules` | Create a schedule |
| GET | `/api/schedules` | Get all schedules |
| GET | `/api/schedules/{id}` | Get schedule by ID |
| PUT | `/api/schedules/{id}` | Update a schedule |
| DELETE | `/api/schedules/{id}` | Delete a schedule |
| GET | `/api/schedules/paged` | Get schedules with pagination and filtering |

---

## 🐳 Docker Hub

Image: `abunahim/schedule-management:latest`

---

## 🌿 Branch Strategy
main ← dev ← feature/*

Every phase is developed on a feature branch, merged to `dev` via PR, then released to `main` via PR.

---

## 📦 Phases

### Phase 1 — Spring Boot Scaffolding
Initialized the project using Spring Initializr with Java 17, Spring Web, Spring Data JPA, PostgreSQL, Lombok, and Validation. Set up the base package structure and `application.properties`.

### Phase 2 — Schedule CRUD REST API
Built the core domain — `Schedule` entity with `ScheduleStatus` enum, `ScheduleRepository`, `ScheduleService`, and `ScheduleController` with full CRUD endpoints. Added a global exception handler with `ScheduleNotFoundException`.

### Phase 3 — DTO Layer
Introduced `ScheduleRequestDTO` and `ScheduleResponseDTO` to decouple the API layer from the entity. Built a `ScheduleMapper` for clean manual mapping between DTOs and entities.

### Phase 4 — Multi-Layer Testing
Added three layers of tests: unit tests for the service layer using Mockito, repository tests using `@DataJpaTest` with H2, and integration tests using `@SpringBootTest` with MockMvc. All 15 tests passing.

### Phase 5 — Docker + PostgreSQL
Created a multi-stage `Dockerfile` using `eclipse-temurin:17` and a `docker-compose.yml` with PostgreSQL and the Spring Boot app. Verified the full stack runs end-to-end in containers.

### Phase 6 — GitHub Actions CI/CD
Set up a GitHub Actions pipeline with two jobs: build and test on every push/PR to `dev` and `main`, and Docker build and push to Docker Hub on merge to `main`. Fixed `mvnw` executable permissions with `git update-index --chmod=+x`.

### Phase 7 — Kubernetes (kind)
Created Kubernetes manifests: Namespace, ConfigMap, Secret, PostgreSQL Deployment + Service, and App Deployment + Service. Deployed to a local `kind` cluster and accessed the app via `kubectl port-forward`.

### Phase 8 — Spring Profiles
Separated configuration into `application-dev.properties`, `application-prod.properties`, and `application-test.properties`. Dev profile used for Docker Compose, prod profile for Kubernetes with environment variable injection.

### Phase 9 — Redis Caching
Added Redis caching using `RedisTemplate` directly (bypassing Spring Cache abstraction to avoid serialization issues). Implemented cache hit/miss logging, TTL-based expiry, and cache eviction on create/update/delete.

### Phase 10 — React Frontend
Built a React + Vite frontend with Axios for API calls. Pages for listing, creating, editing, and deleting schedules. Containerized with Nginx and wired into Docker Compose with a reverse proxy to avoid CORS issues.

### Phase 11 — Flyway Migrations
Replaced `ddl-auto=update` with versioned SQL migrations using Flyway. Created `V1__create_schedules_table.sql` as the baseline migration. Future schema changes are now tracked and versioned.

### Phase 12 — Swagger / OpenAPI
Added `springdoc-openapi` for auto-generated API documentation. All endpoints are documented with `@Operation` and `@Tag` annotations. JWT Bearer authentication is supported in the Swagger UI.

### Phase 13 — Pagination & Filtering
Added a `/api/schedules/paged` endpoint supporting `page`, `size`, `status`, `sortBy`, and `sortDir` query parameters. Returns a `PagedResponseDTO` with total elements, total pages, and last page flag.

### Phase 14 — Spring Security + JWT
Secured all schedule endpoints with Spring Security and JWT. Added `User` entity with `Role` enum, `UserRepository`, `CustomUserDetailsService`, `JwtUtil`, `JwtAuthFilter`, and `SecurityConfig`. Public endpoints: `POST /api/auth/register` and `POST /api/auth/login`.

### Phase 15 — Prometheus + Grafana
Added Spring Boot Actuator and Micrometer Prometheus registry to expose metrics at `/actuator/prometheus`. Added Prometheus and Grafana to Docker Compose. Imported the Spring Boot 3.x Statistics dashboard (ID: 19004) in Grafana for real-time JVM, CPU, memory, and HTTP metrics.