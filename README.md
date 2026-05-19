# 🛒 E-Commerce API

API REST backend para una aplicación de comercio electrónico, construida con **Spring Boot 4** y **Java 25**.

Proporciona gestión completa de productos, categorías, marcas, usuarios y autenticación basada en **JWT con HttpOnly cookies** y protección **CSRF**.

**Autor:** Joan Josep Lira

---

## 📋 Tabla de Contenidos

- [Tech Stack](#-tech-stack)
- [Arquitectura](#-arquitectura)
- [Requisitos Previos](#-requisitos-previos)
- [Inicio Rápido](#-inicio-rápido)
- [Variables de Entorno](#-variables-de-entorno)
- [Endpoints de la API](#-endpoints-de-la-api)
- [Autenticación y Seguridad](#-autenticación-y-seguridad)
- [Base de Datos](#-base-de-datos)
- [Swagger / OpenAPI](#-swagger--openapi)
- [Docker](#-docker)
- [Estructura del Proyecto](#-estructura-del-proyecto)

---

## 🛠 Tech Stack

| Tecnología | Versión | Propósito |
|---|---|---|
| **Java** | 25 | Lenguaje principal (Virtual Threads habilitados) |
| **Spring Boot** | 4.0.6 | Framework base |
| **Spring Security** | — | Autenticación y autorización (JWT + CSRF) |
| **Spring Data JPA** | — | Capa de persistencia (Hibernate) |
| **Spring Data Redis** | — | Caché distribuida |
| **Spring Cache** | — | Abstracción de caché (backend Redis) |
| **Spring Actuator** | — | Health checks y monitorización |
| **Flyway** | — | Migraciones de base de datos |
| **PostgreSQL** | 18 | Base de datos relacional |
| **Redis** | latest | Almacén de caché |
| **Auth0 java-jwt** | 4.5.0 | Creación y validación de tokens JWT |
| **SpringDoc OpenAPI** | 3.0.2 | Documentación Swagger UI |
| **Lombok** | — | Reducción de boilerplate |
| **Docker** | — | Contenerización |
| **Maven** | 3.9.12 | Gestión de dependencias y build |

---

## 🏗 Arquitectura

El proyecto sigue una **arquitectura modular por dominio** con capas bien definidas:

```
com.joanlica.ecommerce
├── auth/          # Autenticación, roles y gestión de usuarios
├── brands/        # Gestión de marcas
├── categories/    # Gestión de categorías
├── products/      # Gestión de productos
├── user/          # Perfil de usuario
├── common/        # Excepciones, handlers globales, utilidades
└── config/        # Seguridad, caché, web, Jackson, inicializadores
```

Cada módulo de dominio se organiza internamente en:

```
módulo/
├── controller/    # Endpoints REST
├── dto/           # Data Transfer Objects (records)
├── mapper/        # Conversión Entity ↔ DTO
├── model/         # Entidades JPA
├── repository/    # Interfaces Spring Data
└── service/       # Lógica de negocio
```

---

## ✅ Requisitos Previos

- **Docker** y **Docker Compose** instalados
- (Opcional) **Java 25** y **Maven 3.9+** para desarrollo local sin Docker

---

## 🚀 Inicio Rápido

### 1. Clonar el repositorio

```bash
git clone https://github.com/Joanjo17/e-commerce-api.git
cd e-commerce-api
```

### 2. Configurar las variables de entorno

Copia el archivo de ejemplo y ajusta los valores:

```bash
cp .env.example .env
```

Edita `.env` con tus credenciales reales. Consulta la sección [Variables de Entorno](#-variables-de-entorno) para más detalles.

### 3. Levantar con Docker Compose

```bash
docker compose up -d
```

Esto levantará tres servicios:

| Servicio | Puerto | Descripción |
|---|---|---|
| `postgres` | 5432 | Base de datos PostgreSQL |
| `redis` | 6379 | Servidor Redis (caché) |
| `backend-app` | 8080 | API Spring Boot |

La aplicación esperará automáticamente a que PostgreSQL y Redis estén healthy antes de arrancar.

### 4. Verificar que está funcionando

```bash
curl http://localhost:8080/actuator/health
```

---

## 🔧 Variables de Entorno

Todas las variables se leen desde el archivo `.env` en la raíz del proyecto.

| Variable | Descripción | Ejemplo |
|---|---|---|
| `PORT` | Puerto del servidor | `8080` |
| `SPRING_PROFILES_ACTIVE` | Perfil activo de Spring (`dev` / `prod`) | `dev` |
| `APP_CORS_ORIGINS` | Orígenes permitidos para CORS | `http://localhost:5173` |
| `DB_USER` | Usuario de PostgreSQL | `myuser` |
| `DB_PASSWORD` | Contraseña de PostgreSQL | `s3cur3P@ssw0rd` |
| `DB_NAME` | Nombre de la base de datos | `ecommerce_db` |
| `DB_URL` | URL JDBC completa | `jdbc:postgresql://postgres:5432/ecommerce_db` |
| `REDIS_HOST` | Host de Redis | `redis` |
| `REDIS_PORT` | Puerto de Redis | `6379` |
| `REDIS_PASSWORD` | Contraseña de Redis | `redis_s3cret` |
| `SECRET_KEY` | Clave secreta para firmar los JWT | `mi_clave_secreta_larga_y_aleatoria` |
| `USER_GENERATOR` | Identificador del emisor del JWT | `my-api-generator` |
| `JWT_TTL` | Tiempo de vida del access token (segundos) | `1800` |
| `JWT_REFRESH_TTL` | Tiempo de vida del refresh token (segundos) | `604800` |
| `SWAGGER_ENABLED` | Habilitar Swagger UI | `true` |
| `COOKIES_SECURE` | Flag `Secure` en las cookies | `false` |
| `COOKIES_SAME_SITE` | Política `SameSite` de las cookies | `Lax` |
| `ADMIN_USERNAME` | Email del usuario administrador inicial | `admin@example.com` |
| `ADMIN_PASSWORD` | Contraseña del usuario administrador inicial | `Admin123!` |

---

## 📡 Endpoints de la API

Base URL: `http://localhost:8080/api/v1`

### 🔐 Autenticación (`/auth`)

| Método | Endpoint | Acceso | Descripción |
|---|---|---|---|
| `POST` | `/auth/register` | Público | Registrar un nuevo usuario |
| `POST` | `/auth/login` | Público | Iniciar sesión |
| `POST` | `/auth/refresh` | Público | Refrescar el access token |
| `POST` | `/auth/logout` | Público | Cerrar sesión (elimina cookies) |
| `GET` | `/auth/csrf` | Público | Obtener token CSRF |

### 👤 Perfil de Usuario (`/profile`)

| Método | Endpoint | Acceso | Descripción |
|---|---|---|---|
| `GET` | `/profile` | Autenticado | Obtener perfil del usuario actual |
| `PUT` | `/profile` | Autenticado | Actualizar perfil del usuario actual |

### 🏷 Categorías (`/categories`)

| Método | Endpoint | Acceso | Descripción |
|---|---|---|---|
| `GET` | `/categories` | Público | Listar todas las categorías |
| `GET` | `/categories/{id}` | Público | Obtener categoría por ID |
| `POST` | `/categories` | ADMIN | Crear categoría |
| `PUT` | `/categories/{id}` | ADMIN | Actualizar categoría |

### 🏢 Marcas (`/brands`)

| Método | Endpoint | Acceso | Descripción |
|---|---|---|---|
| `GET` | `/brands` | Público | Listar marcas (paginado) |
| `GET` | `/brands/{id}` | Público | Obtener marca por ID |
| `POST` | `/brands` | ADMIN | Crear marca |
| `PUT` | `/brands/{id}` | ADMIN | Actualizar marca |

### 📦 Productos (`/products`)

| Método | Endpoint | Acceso | Descripción |
|---|---|---|---|
| `GET` | `/products` | Público | Listar productos (paginado) |
| `GET` | `/products/{id}` | Público | Obtener producto por ID |
| `POST` | `/products` | ADMIN | Crear producto |
| `PUT` | `/products/{id}` | ADMIN | Actualizar producto |

### 🛡 Roles (`/roles`) — Solo ADMIN

| Método | Endpoint | Acceso | Descripción |
|---|---|---|---|
| `GET` | `/roles` | ADMIN | Listar todos los roles |
| `GET` | `/roles/{id}` | ADMIN | Obtener rol por ID |
| `GET` | `/roles/name/{roleName}` | ADMIN | Obtener rol por nombre |
| `POST` | `/roles` | ADMIN | Crear rol |
| `PATCH` | `/roles/{id}` | ADMIN | Actualizar rol |

### ⚙️ Administración (`/admin`) — Solo ADMIN

| Método | Endpoint | Acceso | Descripción |
|---|---|---|---|
| `PUT` | `/admin/user/{email}/roles` | ADMIN | Actualizar roles de un usuario |

---

## 🔒 Autenticación y Seguridad

### Flujo de autenticación

1. El usuario se registra o inicia sesión vía `/auth/register` o `/auth/login`.
2. El servidor responde con los datos del usuario y establece dos **HttpOnly cookies**:
   - `access_token` — JWT de acceso (TTL configurable, por defecto 30 min).
   - `refresh_token` — JWT de refresco (TTL configurable, por defecto 7 días).
3. Los tokens **nunca** se devuelven en el cuerpo de la respuesta.
4. Para renovar el access token, se llama a `/auth/refresh` (usa la cookie `refresh_token`).
5. La sesión se cierra con `/auth/logout`, que elimina ambas cookies.

### Protección CSRF

- Se usa `CookieCsrfTokenRepository` (cookie `XSRF-TOKEN` legible por JavaScript).
- Las peticiones protegidas deben incluir el header `X-XSRF-TOKEN`.
- Los endpoints de registro y login están exentos de CSRF.
- Obtener el token CSRF: `GET /api/v1/auth/csrf`.

### Roles

- `USER` — Rol por defecto al registrarse.
- `ADMIN` — Acceso completo a gestión de productos, categorías, marcas, roles y usuarios.

### Usuario administrador inicial

Al arrancar la aplicación (en perfil `dev` o `prod`), se crea automáticamente un usuario administrador con las credenciales definidas en `ADMIN_USERNAME` y `ADMIN_PASSWORD`, asignándole los roles `ADMIN` y `USER`.

---

## 🗄 Base de Datos

### Esquema

Las migraciones se gestionan con **Flyway** (`classpath:db/migration`). El esquema incluye las siguientes tablas:

| Tabla | Descripción |
|---|---|
| `user_auths` | Credenciales de autenticación (email, password, flags de estado) |
| `user_profiles` | Perfil del usuario (nombre, apellido) — relación 1:1 con `user_auths` |
| `roles` | Roles del sistema (ADMIN, USER, etc.) |
| `user_roles` | Tabla intermedia usuarios ↔ roles (N:M) |
| `inventory_products` | Productos del catálogo |
| `category` | Categorías de productos |
| `product_category` | Tabla intermedia productos ↔ categorías (N:M) |
| `brands` | Marcas de productos |

### Relaciones principales

```
brands 1 ──── N inventory_products N ──── M category
                        │
user_auths 1 ── 1 user_profiles
     │
     N ──── M roles
```

### Configuración JPA

- `ddl-auto: validate` — Hibernate solo valida el esquema; Flyway se encarga de las migraciones.
- `open-in-view: false` — Deshabilitado para evitar problemas de N+1 y lazy loading fuera de transacciones.
- `default_batch_fetch_size: 100` — Optimización de consultas batch.

---

## 📖 Swagger / OpenAPI

Cuando `SWAGGER_ENABLED=true`, la documentación interactiva está disponible en:

| Recurso | URL |
|---|---|
| **Swagger UI** | `http://localhost:8080/swagger-ui.html` |
| **API Docs (JSON)** | `http://localhost:8080/v3/api-docs` |

La configuración de seguridad en Swagger incluye:
- **accessCookie** — Cookie `access_token` (API Key en Cookie).
- **csrfHeader** — Header `X-XSRF-TOKEN` (API Key en Header).

---

## 🐳 Docker

### Dockerfile (Multi-stage build)

El `Dockerfile` usa un **build multi-stage** para optimizar el tamaño de la imagen:

1. **Build stage** (`maven:3.9.12-eclipse-temurin-25-alpine`):
   - Copia `pom.xml` y descarga dependencias offline.
   - Compila el proyecto generando el JAR.
2. **Runtime stage** (`eclipse-temurin:25-jre-alpine`):
   - Solo incluye el JRE y el JAR compilado.
   - Configurado con `UseContainerSupport` y `MaxRAMPercentage=80`.

### Docker Compose

El archivo `docker-compose.yml` orquesta tres servicios:

- **postgres** — PostgreSQL 18 Alpine con health check.
- **redis** — Redis con autenticación y health check.
- **backend-app** — La API Spring Boot, que depende de ambos servicios.

Todos los servicios usan volúmenes persistentes (`postgres_data`, `redis_data`).

---

## 📂 Estructura del Proyecto

```
.
├── .env.example              # Variables de entorno de ejemplo
├── Dockerfile                # Build multi-stage de la aplicación
├── docker-compose.yml        # Orquestación de servicios
├── pom.xml                   # Configuración Maven
├── mvnw / mvnw.cmd           # Maven Wrapper
└── src/
    ├── main/
    │   ├── java/com/joanlica/ecommerce/
    │   │   ├── ECommerceApplication.java
    │   │   ├── auth/             # Autenticación, roles, admin
    │   │   │   ├── controller/
    │   │   │   ├── dto/
    │   │   │   ├── model/
    │   │   │   ├── repository/
    │   │   │   └── service/
    │   │   ├── brands/           # Gestión de marcas
    │   │   ├── categories/       # Gestión de categorías
    │   │   ├── products/         # Gestión de productos + seeder
    │   │   ├── user/             # Perfiles de usuario
    │   │   ├── common/           # Excepciones, handlers, utilidades
    │   │   └── config/           # Seguridad, caché, web, Jackson
    │   └── resources/
    │       ├── application.yml   # Configuración de Spring Boot
    │       └── db/migration/     # Migraciones Flyway
    └── test/                     # Tests
```

---

## 📄 Licencia

Este proyecto está licenciado bajo la [Licencia MIT](LICENSE).