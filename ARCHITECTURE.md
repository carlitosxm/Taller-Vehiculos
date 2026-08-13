# Arquitectura

## Decisiones tecnológicas

### Spring Boot vs Node.js

Se eligió **Spring Boot** por su ecosistema maduro para aplicaciones con reglas de negocio y validación robustas (Bean Validation), tipado fuerte que reduce errores en tiempo de compilación, y JPA/Hibernate para modelar relaciones 1:N (Owner→Vehicle→MaintenanceOrder) sin escribir SQL repetitivo. Node.js sería igualmente válido para I/O intensivo, pero este dominio (CRUD + relaciones + validaciones) encaja mejor con un stack tipado y orientado a objetos.

### PostgreSQL vs MongoDB

El dominio es intrínsecamente **relacional**: un propietario tiene N vehículos, un vehículo tiene N órdenes de mantenimiento, y cada orden referencia un tipo de mantenimiento. Estas relaciones con integridad referencial (FKs, unicidad de placa/cédula) se expresan de forma natural en un modelo relacional. MongoDB añadiría complejidad para mantener consistencia entre colecciones sin aportar ventajas de escalabilidad que este dominio no necesita.

### Angular vs React

Angular fue elegido por ser un framework completo (routing, forms reactivos, HTTP client, DI) que impone una estructura consistente entre `owner`, `vehicle`, `maintenance` y `dashboard`, útil en un proyecto con muchos componentes CRUD similares. Su sistema de tipado end-to-end con TypeScript y RxJS encaja con la naturaleza reactiva de filtrar órdenes por estado.

### Docker Compose vs Kubernetes

Kubernetes está sobredimensionado para una aplicación de tres servicios (frontend, backend, base de datos) sin requisitos de auto-escalado u orquestación multi-nodo. **Docker Compose** ofrece el mismo objetivo de portabilidad y reproducibilidad ("un comando levanta todo") con una curva de aprendizaje mínima, ideal para un proyecto de portfolio y despliegues simples.

## Patrones de diseño

### MVC + DTO

- **Model**: entidades JPA (`Owner`, `Vehicle`, `MaintenanceType`, `MaintenanceOrder`) representan el esquema de base de datos y sus relaciones.
- **DTO**: cada entidad expone un DTO (`OwnerDTO`, `VehicleDTO`, `MaintenanceOrderDTO`) que desacopla el contrato de la API del modelo de persistencia — permite validar en la frontera, enriquecer con datos derivados (ej. `ownerNombreCompleto`) y evitar exponer relaciones lazy que romperían la serialización.
- **Controller**: capa delgada, solo mapea rutas HTTP a llamadas de servicio; toda la lógica de negocio vive en el servicio.

### Capas: Controller → Service → Repository

```
HTTP request
   │
   ▼
Controller   (mapeo de rutas, validación de entrada vía @Valid)
   │
   ▼
Service      (reglas de negocio: unicidad, existencia de relaciones, transacciones)
   │
   ▼
Repository   (Spring Data JPA — acceso a datos)
   │
   ▼
PostgreSQL
```

Esta separación permite testear la lógica de negocio (Service) sin necesidad de un servidor HTTP levantado, y cambiar la capa de persistencia sin tocar controladores.

## Relaciones 1:N en base de datos

```
Owner (1) ──────< (N) Vehicle (1) ──────< (N) MaintenanceOrder >────── (1) MaintenanceType
```

- `owners.id` ← `vehicles.owner_id` (`ON DELETE` cascada gestionada por JPA `CascadeType.ALL` + `orphanRemoval`)
- `vehicles.id` ← `maintenance_orders.vehicle_id` (misma estrategia de cascada)
- `maintenance_types.id` ← `maintenance_orders.maintenance_type_id` (referencia simple, sin cascada — un tipo de mantenimiento no se elimina al borrar órdenes)

## Filtrado de órdenes por estado

El backend expone `GET /api/maintenance-orders?status=PENDING|IN_PROCESS|COMPLETED` mediante un query param opcional en el controller; si no se envía, retorna todas las órdenes. El frontend consume este endpoint tres veces (una por estado) para poblar las secciones visuales de la vista de mantenimiento, evitando filtrar en el cliente y manteniendo la fuente de verdad en el servidor.
