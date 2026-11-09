# FedePet — Backend

Sistema de gestión veterinaria multi-tenant construido con **Spring Boot 3.3.1**.

## Stack
- Java 17 + Spring Boot 3.3.1
- Spring Data JPA + Hibernate (`ddl-auto=create-drop` en desarrollo — sin migraciones, el esquema se recrea en cada arranque)
- Spring Security + JWT (`JwtService`)
- Google OAuth2 (`GoogleAuthService`)
- AWS S3 para almacenamiento de archivos (`S3ServiceImpl`)
- Maven

## Arquitectura multi-tenant por slug

Cada clínica tiene un slug único, pero **el slug solo aparece en la URL de los endpoints de auth y de info pública**:
```
POST /{slug}/auth/login
POST /{slug}/auth/register
POST /{slug}/auth/google
GET  /{slug}/public/info      # nombre, logo, primaryColor — sin JWT
```
El resto de la API (`/api/pets`, `/api/doctors`, `/api/dates`, `/api/clinic/me`, ...) **no** lleva el slug en la ruta: el `clinicId` se extrae del JWT (`JwtAuthenticatorFilter`) y se guarda en `TenantContext` (ThreadLocal) para cada request. El aislamiento de datos se aplica en los services filtrando siempre por ese `clinicId`.

**Clase clave:** `TenantContext` — almacena el `clinicId` del request actual en un `ThreadLocal`.

## Roles
| Rol | Descripción |
|---|---|
| `SUPER_ADMIN` | El desarrollador — crea y gestiona clínicas (`/super-admin/auth/login`, sin slug) |
| `ADMIN` | Dueño de clínica — gestiona doctores y ve estadísticas |
| `DOCTOR` | Veterinario — gestiona citas e historial médico |
| `CLIENTE` | Dueño de mascota — ve sus mascotas y pide citas |

Todos los `@PreAuthorize` de los controllers usan estos nombres exactos (`hasRole('CLIENTE')`, etc.) — el rol legacy `USUARIO` ya no existe en ningún lado, se corrigió en `PetController` y `AppointmentController` donde había quedado desactualizado tras el rename.

## Módulos (`src/main/java/com/api/crud/`)
- `auth/` — login, registro, Google OAuth, SuperAdmin. **Sin refresh token todavía** (`RefreshToken`/`RefreshTokenRepository` existen como archivos vacíos, ver `ROADMAP.md` parte 1.2) — el access token dura 1 hora y el usuario debe volver a loguearse.
- `jwt/` — generación y validación de tokens. El JWT incluye `id`, `name`, `phone`, `role`, `clinicId` (si tiene clínica) y `doctorPhoto` (si es doctor).
- `user/` — entidad `UserInfo`, enum `Role`
- `clinic/` — entidad `Clinic` (slug, name, address, phone, email, logo, **primaryColor**, active), gestión de clínicas
- `doctor/` — entidad `Doctor`, alta y listado de veterinarios (sin edición ni cambio de disponibilidad todavía)
- `pet/` — entidad `Pet`, registro y gestión de mascotas
- `appointment/` — entidad `Dates`, citas veterinarias, DTOs
- `storage/` — integración con AWS S3
- `tenant/` — contexto multi-tenant por request
- `common/` — `ApiResponse<T>` (campos `status` y **`body`**, no `data`), `ApiException`, `ResponseCode`
- `config/` — `DataSeeder` (solo crea el usuario `SUPER_ADMIN` inicial, no hay clínica demo seedeada)
- `exception/` — `GlobalExceptionHandler`

## Theming por clínica
`Clinic.primaryColor` (hex, default `#ffa420`) se expone en `ClinicResponse` y se puede fijar al crear la clínica (`CreateClinicRequest.primaryColor`, opcional). El frontend lo lee desde `GET /{slug}/public/info` y lo aplica como color de marca de esa clínica en toda la app — no hay lógica de negocio asociada, es puramente de presentación.

## Onboarding de clínicas
`POST /api/clinic/create` (SUPER_ADMIN) y `GET /api/clinic/all` (SUPER_ADMIN) ya tienen UI consumidora en el frontend (`features/super-admin/`, ver `fedepet-frontend/CLAUDE.md`). No hay endpoint de edición/borrado de clínica — solo alta y listado.

## Convenciones
- Los controllers de auth/public reciben el slug como `@PathVariable`; el resto de controllers no llevan slug y confían en `TenantContext`.
- Los services siempre filtran por `clinicId` extraído del JWT o del slug.
- Respuestas envueltas en `ApiResponse<T> { status, body }`.
- Errores manejados centralmente en `GlobalExceptionHandler` via `ApiException` (mismo envelope, `body` trae el mensaje).

## Comandos comunes
```bash
./mvnw spring-boot:run          # Levantar en desarrollo
./mvnw compile                  # Compilar
./start.sh                      # Script de arranque propio
```

## Variables de entorno
Ver `.env` (no subir a git). Incluye credenciales de DB, JWT secret, AWS keys, Google OAuth.

## Gaps conocidos (ver `ROADMAP.md` para el plan completo)
- Sin refresh tokens ni paginación en listados.
- Sin `ProductController` (la tienda del frontend es solo mock).
- Sin endpoint de edición de doctor ni de cambio de disponibilidad.
- Sin validación `@Valid`/`@NotBlank` en los DTOs de entrada.
