# FedePet — Contexto completo del proyecto

> Este archivo es el punto de partida para entender **qué es FedePet, para quién es, en qué estado está y qué sigue** — tanto para Claude como para cualquiera que retome el proyecto. Para el detalle técnico de este repo ver `CLAUDE.md`; para el plan de negocio original (precios, guión de ventas, objeciones) ver `ROADMAP.md` — pero ojo: **el pricing de `ROADMAP.md` todavía no está validado con datos reales del mercado** (ver sección "Negocio" más abajo).

## Qué es FedePet

Un SaaS de gestión veterinaria multi-tenant: cada clínica tiene su propia URL (slug), sus propios datos aislados, su propio color de marca, y gestiona mascotas, citas y doctores desde una app móvil + web en español. Pensado para clínicas pequeñas (1-4 veterinarios) que hoy llevan el control en papel, WhatsApp o Excel.

**Quién lo construye:** un único founder/developer que hace el desarrollo, la venta y el soporte él solo, sin equipo. Esto condiciona todas las decisiones de alcance: priorizar lo que un solo humano puede sostener (soporte, onboarding, infraestructura) por encima de features vistosas.

## Arquitectura (resumen — detalle completo en `CLAUDE.md`)

- **Backend** (este repo): Spring Boot 3.3.1 + JPA/Hibernate + JWT, corriendo en una instancia EC2. Base de datos Postgres en Supabase (free tier).
- **Frontend**: Angular 17+ standalone + Ionic + Capacitor, repo separado `fedepet-frontend`.
- Multi-tenant por slug: el slug solo aparece en las rutas de auth/info pública (`/{slug}/auth/...`, `/{slug}/public/info`); el resto de la API resuelve el `clinicId` desde el JWT.
- Roles: `SUPER_ADMIN` (el founder, gestiona clínicas) → `ADMIN` (dueño de clínica) → `DOCTOR` → `CLIENTE` (dueño de mascota).

## Negocio — modelo y mercado (⚠️ parcialmente sin validar)

- **Pricing borrador** (de `ROADMAP.md`, escrito antes de tener datos de mercado reales): Básico $75/mes (≤2 doctores), Estándar $130/mes (≤5 doctores + historial + fotos), Premium $200/mes (ilimitado + estadísticas), Onboarding $150 pago único.
- **Mercado objetivo**: clínicas veterinarias pequeñas en Panamá, zonas de clase media-alta (San Francisco, Marbella, Costa del Este, Albrook, Clayton).
- **Estado de la validación**: se corrió una investigación de mercado (cantidad real de clínicas, competencia local, poder de compra, métodos de pago como Yappy, obligaciones fiscales/ITBMS) pero la mayoría de las fuentes fallaron por errores de infraestructura — **no hay un TAM confiable ni confirmación de que $75-200/mes sea el precio correcto para Panamá**. El único dato que sobrevivió verificación: un comparable español (GVET) arranca en $20/mes, bastante por debajo del borrador actual; e ITBMS en Panamá es 7% plano (sin complicaciones de tramos). **Antes de fijar precio final, repetir esa investigación con fuentes específicas de Panamá** (cámara de comercio, no solo el directorio de la asociación de veterinarios).
- Pagos: sin integración todavía (ni Stripe ni Yappy Comercial) — si el plan es cobrar recurrente, esto es un bloqueante de negocio, no solo técnico.

## Prioridades antes de producción (ver detalle y razones en `CLAUDE.md` → "Gaps conocidos")

Orden recomendado:
1. **`ddl-auto=create-drop`** en `application.properties` — recrea el esquema completo en cada reinicio del backend. Si esto corre en el EC2 real, cualquier reinicio borra toda la base de datos real. Cambiar a `update` o meter Flyway/Liquibase.
2. CORS abierto a cualquier origen (`@CrossOrigin(origins = "*")`) en todos los controllers.
3. Sin rate limiting en `/auth/login` (fuerza bruta sin freno).
4. Sin validación de inputs (`@Valid`/`@NotBlank`/`@Email`) en los DTOs.
5. Sin refresh token (JWT dura 1h) — ya decidido cómo encararlo, no implementado.
6. Sin ambientes dev/prod separados — plan decidido (ver abajo), no implementado todavía.
7. Integración de pagos — inexistente.

## Plan de ambientes dev/prod (decidido, no implementado)

Para no gastar de más mientras no hay clientes pagando:
- **DB**: segundo proyecto Supabase gratis exclusivo para dev (el free tier permite 2 proyectos).
- **Backend**: separar por Spring profiles (`application-dev.properties` / `application-prod.properties`, activados con `SPRING_PROFILES_ACTIVE`). Dev puede correr como segundo proceso en la misma EC2 (otro puerto) sin costo extra, o en Render free tier si se prefiere no tocar la instancia de prod.
- **Frontend**: `environment.dev.ts` adicional + deploy por rama en Vercel/Netlify (gratis).
- Alternativas gratis a EC2 evaluadas para un ambiente de dev: Render (free, se duerme a los 15 min sin uso), Oracle Cloud "Always Free" (VM ARM 4 cores/24GB, sin fecha de vencimiento, mismo modelo operativo que EC2), Google Cloud Run (serverless, capa gratis generosa). Railway y Heroku **ya no tienen tier gratis real** — descartados.

## Qué falta para poder vender (no técnico)

Del `ROADMAP.md` original: contrato simple de 1 página, WhatsApp Business dedicado, dominio propio, clínica demo cargada con datos realistas. Nada de esto está hecho todavía.

## Dónde mirar para más detalle

- `CLAUDE.md` — convenciones de código, estructura de módulos, endpoints, gaps técnicos conocidos.
- `ROADMAP.md` — plan de negocio original completo (guión de ventas, manejo de objeciones, metas mensuales) — útil como estructura, pero **el pricing y el tamaño de mercado ahí escritos están sin validar**.
- `fedepet-frontend/CONTEXT.md` — mismo contexto de negocio, versión con el detalle técnico del frontend.
