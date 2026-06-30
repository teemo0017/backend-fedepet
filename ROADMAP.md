# FedePet — Roadmap a Producción y Plan de Ventas Panama

---

## PARTE 1: BACKEND — LO QUE FALTA

### 1.1 Correcciones críticas (1–2 días)

| # | Tarea | Impacto |
|---|-------|---------|
| 1 | **Corregir expiración del JWT** — cambiar `1000 * 60 * 24` (24 min) a `1000 * 60 * 60` (1 hora) en `JwtService.java:48` | El usuario pierde sesión en 24 minutos |
| 2 | **Mover SECRET_KEY a variable de entorno** — actualmente está hardcodeada en `JwtService.java:23`. Usar `@Value("${jwt.secret}")` y definirlo en `.env` | Vulnerabilidad de seguridad grave |
| 3 | **Validación de inputs** — agregar `@Valid` en controllers + anotaciones `@NotBlank`, `@Email`, `@Size` en todos los DTOs | Cualquiera puede enviar datos vacíos o malformados |

**Ejemplo fix JWT secret:**
```java
@Value("${jwt.secret}")
private String secretKey;
```
```properties
# application.properties
jwt.secret=${JWT_SECRET}
```

---

### 1.2 Funcionalidad faltante (1 semana)

#### Refresh Tokens
**Por qué:** El access token dura 1 hora. Sin refresh token, el usuario debe re-loguearse cada hora.

**Qué implementar:**
1. Entidad `RefreshToken` — id, token (UUID), userId, expiresAt (7 días), revoked
2. `RefreshTokenRepository` + `RefreshTokenService` (crear, validar, revocar)
3. Actualizar `AuthResponse` para incluir `{ accessToken, refreshToken, expiresIn }`
4. Nuevo endpoint `POST /auth/refresh` — recibe refreshToken, devuelve nuevo accessToken
5. Nuevo endpoint `POST /auth/logout` — revoca el refreshToken

#### Paginación en listados
Cualquier endpoint que devuelva una lista (`/api/pets`, `/api/appointments`, etc.) necesita paginación para no traer miles de registros.
```java
// Spring Data lo hace automático:
Page<Pet> findAll(Pageable pageable);
// El frontend envía: GET /api/pets?page=0&size=20
```

#### Manejo de imágenes (ya tienes S3 configurado)
- Endpoint `POST /api/upload/image` que recibe un archivo y devuelve la URL pública de S3
- Validar que solo se suban imágenes (jpg, png, webp) y con tamaño máximo (ej: 5 MB)

---

### 1.3 Mejoras opcionales para demo (opcional)

- Soft delete en mascotas y citas (campo `deletedAt` en lugar de borrar)
- Endpoint de estadísticas para el dashboard (`/api/stats` — total mascotas, citas del mes, etc.)
- Envío de correo de confirmación al registrarse (SendGrid o Resend — plan gratis disponible)

---

## PARTE 2: INFRAESTRUCTURA Y DESPLIEGUE

### 2.1 Backend — Railway (recomendado para empezar)

**Por qué Railway y no AWS/GCP:** Costo mínimo (~$5/mes), despliegue en minutos desde GitHub, sin configurar servidores.

**Pasos:**
1. Crear cuenta en [railway.app](https://railway.app)
2. Conectar el repositorio de GitHub
3. Railway detecta el `pom.xml` y construye automáticamente
4. Configurar variables de entorno en Railway Dashboard:

```env
JWT_SECRET=una-clave-de-minimo-256-bits-aleatoria
DB_URL=jdbc:postgresql://tu-host-supabase/postgres
DB_USERNAME=postgres
DB_PASSWORD=tu-password-supabase
AWS_ACCESS_KEY_ID=...
AWS_SECRET_ACCESS_KEY=...
AWS_BUCKET_NAME=fedepet-imgs
AWS_REGION=us-east-1
```

5. Railway asigna una URL pública: `https://fedepet-api.railway.app`

**Costo estimado Railway:** $5–$10/mes cuando tengas pocos clientes.

---

### 2.2 Base de datos — Supabase (ya tienes esto)

- Ya está corriendo en Supabase con PostgreSQL
- Usar el **connection pooler** de Supabase para evitar saturar conexiones
- Hacer **backups automáticos** — Supabase los activa en el plan gratuito (7 días de retención)
- Crear un proyecto separado de Supabase para producción (no uses el mismo de desarrollo)

---

### 2.3 Dominio y SSL

1. Comprar un dominio en Namecheap o GoDaddy: `fedepet.app` o `fedepet.com.pa` (~$15/año)
2. Apuntar el dominio a Railway (Railway provee SSL automático con Let's Encrypt)
3. URL final: `https://api.fedepet.app`

---

### 2.4 Monitoreo básico

- **Logs:** Railway muestra logs en tiempo real en su dashboard
- **Uptime:** Registrar en [UptimeRobot](https://uptimerobot.com) (gratis) para recibir alerta por email si el servidor cae
- **Cuando crezcas:** Agregar Sentry para rastrear errores en producción

---

## PARTE 3: FRONTEND

> Esta sección asume que el frontend está en construcción o es una SPA (React/Next.js/Vue).

### 3.1 Lo mínimo para el demo

| Pantalla | Descripción |
|----------|-------------|
| Login / Registro | Con email+contraseña y botón de Google |
| Dashboard | Estadísticas simples: mascotas registradas, citas del mes |
| Mascotas | Listado, agregar, ver detalle con foto |
| Citas | Calendario o lista, agendar nueva cita |
| Perfil del doctor | Foto, especialidad, horario |

### 3.2 Despliegue del frontend

- **Vercel** (recomendado, gratis): conectas tu repo de GitHub y en 2 minutos está en producción
- Configuras la variable `NEXT_PUBLIC_API_URL=https://api.fedepet.app`

---

## PARTE 4: PREPARACIÓN DEL DEMO

### 4.1 La clínica demo

Antes de ir a vender, crea una clínica ficticia completa:

1. Registra una clínica: **"Clínica Veterinaria San Francisco"**
2. Crea 2 doctores con fotos reales (puedes usar fotos de stock profesionales)
3. Agrega 10–15 mascotas con fotos y historial
4. Crea citas pasadas y futuras para que el calendario se vea activo
5. El dashboard debe mostrar números que impresionen (no ceros)

### 4.2 Antes de cada reunión

- Abrir el demo en tu laptop Y en tu celular (muestra que es responsive)
- Tener credenciales de acceso listos: `demo@fedepet.app / Demo2024`
- Preparar el flujo de 5 minutos:
  1. Login (5 seg)
  2. Dashboard con estadísticas (30 seg)
  3. Agregar una mascota nueva en vivo (1 min)
  4. Agendar una cita (1 min)
  5. Ver historial de la mascota (30 seg)
  6. Mostrar en celular que funciona igual (30 seg)

---

## PARTE 5: PLAN DE VENTAS — PANAMA

### 5.1 Precios recomendados

| Plan | Precio/mes | Incluye |
|------|------------|---------|
| **Básico** | $75/mes | Hasta 2 doctores, gestión de mascotas y citas |
| **Estándar** | $130/mes | Hasta 5 doctores + historial médico + fotos |
| **Premium** | $200/mes | Doctores ilimitados + estadísticas + soporte prioritario |
| **Onboarding** | $150 (único) | Configuración inicial, carga de datos existentes, capacitación |

**Estrategia de precio:** Ofrece el primer mes GRATIS. El onboarding lo cobras siempre (cubre tu tiempo de setup).

---

### 5.2 A quién venderle primero (perfil del cliente ideal)

**Criterios de selección:**
- Clínicas con 1–4 veterinarios (las grandes ya tienen sistemas)
- Que lleven el control en papel, WhatsApp o Excel (fácil de reemplazar)
- Dueño joven o abierto a tecnología (< 45 años idealmente)
- Clínicas en zonas de clase media-alta: San Francisco, Marbella, Costa del Este, Albrook, Clayton

**Evita por ahora:**
- Clínicas con sistema propio o contrato vigente con otro software
- Dueños mayores muy resistentes al cambio

---

### 5.3 Cómo encontrarlos

1. **Google Maps:** Busca "veterinaria Panama" y saca una lista de 30–50 clínicas con nombre, dirección y teléfono
2. **Instagram:** Muchas clínicas tienen IG activo — es una forma de ver si son "digitales"
3. **Referidos:** Cuando consigas el primer cliente, pídele que te refiera a otra clínica
4. **Grupos de Facebook:** "Mascotas en Panama", "Veterinarios de Panama" — ofrece valor antes de vender

---

### 5.4 El proceso de venta (paso a paso)

#### Semana 1–2: Prospección
- Lista de 30 clínicas objetivo
- Visita o llama a 5 por día
- Objetivo: conseguir 10 reuniones de demo

**Guión de primer contacto (por teléfono o presencial):**
> *"Hola, soy [nombre], desarrollo software para clínicas veterinarias en Panama. Creé un sistema que les permite manejar citas, historial de mascotas y doctores desde cualquier dispositivo. ¿Tienen 15 minutos esta semana para que les muestre cómo funciona?"*

#### Semana 3–4: Demos
- Haz el demo en su clínica (no por Zoom — ve presencialmente)
- Lleva el demo en tu laptop Y en tu celular
- No expliques cómo funciona técnicamente — muestra el beneficio
- Al final pregunta: *"¿Esto resolvería un problema que tienen hoy?"*

#### Semana 5: Cierre
- Si dijeron que sí al demo: *"¿Empezamos la semana que viene? El primer mes es gratis para que lo prueben con calma."*
- Si piden pensar: *"Entendido, ¿puedo llamarles el jueves para ver si tienen preguntas?"*
- Si dicen que el precio es alto: *"El plan básico a $75 equivale a lo que cuesta un libro de citas cada mes — con esto nunca pierden información si se pierde el libro."*

---

### 5.5 Meta realista por mes

| Mes | Meta | Ingresos estimados |
|-----|------|--------------------|
| Mes 1 | 0 clientes (configuración y demos) | $0 |
| Mes 2 | 2 clientes pagando | $260/mes |
| Mes 3 | 4 clientes pagando | $520/mes |
| Mes 6 | 10 clientes pagando | $1,300/mes |
| Mes 12 | 20 clientes pagando | $2,600/mes |

**Punto de inflexión:** Con 10 clientes en Estándar ($130) ya tienes $1,300 recurrentes. A partir de ahí, considera contratar a alguien de ventas o soporte.

---

### 5.6 Objeciones comunes y cómo manejarlas

| Objeción | Respuesta |
|----------|-----------|
| *"Ya usamos WhatsApp para las citas"* | "WhatsApp no guarda historial médico ni les avisa cuando una mascota cumple vacuna. Con esto sí." |
| *"Es muy caro"* | "El plan básico son $2.50 al día. Una consulta de urgencia les cuesta más que eso." |
| *"¿Y si el sistema falla?"* | "Está en la nube — si su internet funciona, el sistema funciona. Además tengo soporte directo por WhatsApp." |
| *"Necesito consultarlo con mi socio"* | "Claro, ¿cuándo se reúnen? Puedo ir a hacerles el demo a los dos juntos." |
| *"Ya tenemos un sistema"* | "¿Están contentos con él? ¿Puedo preguntar qué es lo que más les falta?" |

---

### 5.7 Lo que necesitas antes de salir a vender

- [ ] Backend deployado en Railway con URL pública
- [ ] Frontend deployado en Vercel con URL pública
- [ ] Clínica demo cargada con datos realistas
- [ ] Dominio propio (no una URL de railway.app)
- [ ] WhatsApp Business con número dedicado para FedePet
- [ ] Tarjeta de presentación digital (puedes usar [Beacons.ai](https://beacons.ai) gratis)
- [ ] Contrato simple de 1 página (términos de uso, política de cancelación)

---

## RESUMEN EJECUTIVO

```
SEMANAS 1–2:   Correcciones técnicas (JWT, validación, env vars)
SEMANAS 3–4:   Refresh tokens + paginación + subida de imágenes
SEMANAS 5–6:   Deploy en Railway + Vercel + dominio propio
SEMANAS 7–8:   Clínica demo lista + material de ventas
MES 2:         Primeras 30 visitas comerciales en Panama
MES 3:         Primeros 2–4 clientes pagando
MES 6:         10 clientes, $1,300 MRR
MES 12:        20 clientes, $2,600 MRR
```

**Tu ventaja competitiva en Panama:** La mayoría de las clínicas veterinarias pequeñas no tienen sistema. Las que sí tienen, usan soluciones genéricas caras y complicadas. Tú llegas con algo simple, en español, con soporte local y precio accesible.

---

*Generado para el proyecto FedePet — Junio 2026*
