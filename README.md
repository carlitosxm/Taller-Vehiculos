# Vehicle Maintenance Tracker

Sistema completo de gestión de mantenimiento vehicular para talleres y propietarios. Permite registrar propietarios y vehículos, crear y dar seguimiento a órdenes de mantenimiento, y visualizar un dashboard con alertas y costos.

## Stack tecnológico

| Capa | Tecnología |
|---|---|
| Backend | Spring Boot 3.3 + Java 25 |
| Frontend | Angular 18 + TypeScript + Tailwind CSS |
| Base de datos | PostgreSQL 15 |
| Contenedores | Docker + Docker Compose |
| CI/CD | GitHub Actions |
| ORM | JPA / Hibernate |
| Validación | Spring Validation + DTOs |

Ver [ARCHITECTURE.md](./ARCHITECTURE.md) para el detalle de decisiones arquitectónicas.

## Quick start

```bash
git clone <repo-url>
cd Taller-Vehiculos
docker-compose up --build
```

- Frontend: http://localhost
- Backend API: http://localhost:8080/api
- PostgreSQL: localhost:5432

Al levantar el backend, Hibernate crea automáticamente el esquema (`ddl-auto: update`) y se siembran los 8 tipos de mantenimiento predefinidos.

## Correr localmente (sin Docker)

### Backend

```bash
cd backend
# Requiere una instancia de PostgreSQL 15 corriendo localmente
# con la base de datos "vehicle_maintenance"
mvn spring-boot:run
```

Variables de entorno opcionales: `DB_USERNAME`, `DB_PASSWORD`, `CORS_ALLOWED_ORIGINS`.

### Frontend

```bash
cd frontend
npm install
npm start
```

La app queda disponible en http://localhost:4200 y consume la API en http://localhost:8080/api (ver `src/environments/environment.ts`).

## Endpoints principales

### Owners
```
GET    /api/owners
GET    /api/owners/{id}
GET    /api/owners/cedula/{cedulaOruc}
POST   /api/owners
PUT    /api/owners/{id}
DELETE /api/owners/{id}
GET    /api/owners/{id}/vehicles
```

### Vehicles
```
GET    /api/vehicles
GET    /api/vehicles/{id}
GET    /api/vehicles/placa/{placa}
POST   /api/vehicles
PUT    /api/vehicles/{id}
DELETE /api/vehicles/{id}
GET    /api/vehicles/{id}/maintenance
```

### Maintenance Types
```
GET    /api/maintenance-types
POST   /api/maintenance-types
```

### Maintenance Orders
```
GET    /api/maintenance-orders
GET    /api/maintenance-orders?status=PENDING|IN_PROCESS|COMPLETED
GET    /api/maintenance-orders/{id}
GET    /api/maintenance-orders/vehicle/{vehicleId}
POST   /api/maintenance-orders
PUT    /api/maintenance-orders/{id}
DELETE /api/maintenance-orders/{id}
```

### Dashboard
```
GET /api/dashboard/summary
GET /api/dashboard/alerts
GET /api/dashboard/costs
```

## Estructura de carpetas

```
Taller-Vehiculos/
├── backend/            Spring Boot API (controller/service/repository/model/dto)
├── frontend/            Angular 18 SPA (components/services/models)
├── .github/workflows/    Pipeline de CI/CD
├── docker-compose.yml
├── ARCHITECTURE.md
└── README.md
```

## Troubleshooting

| Problema | Solución |
|---|---|
| El frontend no llega al backend en Docker | Verificar que `nginx.conf` apunte a `http://backend:8080` y que ambos servicios estén en la misma red de `docker-compose.yml` |
| `ddl-auto` no crea las tablas | Confirmar que la base `vehicle_maintenance` exista y las credenciales coincidan con `DB_USERNAME`/`DB_PASSWORD` |
| CORS bloqueado en desarrollo | Ajustar `cors.allowed-origins` en `application.yml` o la variable `CORS_ALLOWED_ORIGINS` |
| `docker compose up` falla en el healthcheck del backend | El backend tarda en arrancar (JVM); el `start_period` ya contempla 30s, aumentarlo si la máquina es lenta |
