# DNF Backend - Plataforma de Búsqueda de Mascotas

Backend basado en arquitectura de microservicios con **Java Spring Boot**, orquestado con **Docker Compose**. Este sistema provee la lógica principal para una aplicación dedicada a reportar mascotas perdidas/encontradas, generar coincidencias automáticas y compartir historias de éxito en comunidad.

## 🏗️ Arquitectura y Microservicios

El proyecto está compuesto por los siguientes servicios:

| Servicio | Puerto (Local) | Descripción | Base de Datos |
| :--- | :--- | :--- | :--- |
| **`eurekaserver`** | `8761` | Servidor de descubrimiento (Service Discovery). | - |
| **`Api-Gateway`** | `8080` | Puerta de enlace (Spring Cloud Gateway MVC). Punto de entrada y validador de seguridad (JWT). | - |
| **`Ms-usuario`** | `8082` | Gestión de usuarios, registro y autenticación (Login/JWT). | PostgreSQL |
| **`Ms-mascota`** | `8081` | Gestión de reportes de mascotas (perdidas, encontradas). | PostgreSQL |
| **`Ms-coincidencias`** | `8083` | Motor para detectar cruces entre mascotas perdidas y encontradas. (Usa OpenFeign). | PostgreSQL |
| **`Ms-comunidad`** | `8094` | Historias de éxito y foros comunitarios. | PostgreSQL |
| **`Ms-notificaciones`**| `8095` | Envío de notificaciones/alertas (Email simulado/consola). | - |
| **`postgres-db`** | `5433` | Base de datos compartida por los microservicios. | - |

---

## 🛠️ Tecnologías Utilizadas

* **Java 17/21**
* **Spring Boot (3.2.3 / 4.0.5)**
* **Spring Cloud** (Netflix Eureka, Gateway MVC, OpenFeign, Resilience4j, LoadBalancer)
* **Spring Security & JWT** (Json Web Tokens) para autenticación y autorización.
* **PostgreSQL** (JPA / Hibernate)
* **Docker & Docker Compose**
* **JUnit & Mockito** para pruebas unitarias.
* **Swagger/Springdoc OpenAPI** para documentación de endpoints.

---

## 🚀 Requisitos Previos

* Docker y Docker Compose instalados.
* Java 17 o superior y Maven (si deseas correr los servicios localmente sin Docker).

---

## ⚙️ Cómo ejecutar el proyecto

### 1. Usando Docker Compose (Recomendado)

Desde la raíz del proyecto, ejecuta el siguiente comando para levantar la base de datos, Eureka, el Gateway y todos los microservicios:

```bash
docker compose up --build
```

*Nota: `eurekaserver` toma unos segundos en estar listo. Los demás microservicios se registrarán automáticamente una vez que esté online.*

### 2. Desarrollo Local (Servicio por Servicio)

Si deseas modificar código y correr un microservicio en tu máquina:

1. Inicia solo la base de datos:
   ```bash
   docker compose -f docker-compose-db.yml up
   ```
2. Levanta el servidor Eureka:
   ```bash
   cd eurekaserver
   ./mvnw spring-boot:run
   ```
3. Levanta el API Gateway y los demás microservicios ingresando a sus carpetas y ejecutando:
   ```bash
   ./mvnw spring-boot:run
   ```

---

## 🔐 Seguridad y JWT

El sistema cuenta con una arquitectura de doble validación:
1. Las peticiones llegan al **API-Gateway** (`localhost:8080`), el cual intercepta y verifica criptográficamente la firma del JWT. Si es inválido, rechaza la petición (`401`).
2. Si es válido, la petición pasa al **Microservicio** correspondiente, el cual vuelve a validar el token mediante Spring Security para establecer el contexto del usuario.

---

## 🧪 Pruebas en Postman (Endpoints Principales)

**Todas las peticiones deben hacerse al API-Gateway (Puerto 8080).**

### 1. Usuarios (Público)

* **Registrar Usuario:** `POST http://localhost:8080/api/usuarios`
  ```json
  {
    "name": "Juan Perez",
    "email": "juan@correo.com",
    "password": "password123",
    "rol": "ciudadano"
  }
  ```

* **Login (Obtener JWT):** `POST http://localhost:8080/api/usuarios/login`
  ```json
  {
    "email": "juan@correo.com",
    "password": "password123"
  }
  ```
  *(Copia el `token` de la respuesta para las siguientes peticiones).*

### 2. Mascotas (Requiere JWT)

> **Nota:** Para estas peticiones, ve a la pestaña **Authorization** en Postman, elige **Bearer Token** y pega tu token.

* **Listar Mascotas:** `GET http://localhost:8080/api/pets` (Soporta filtros `?status=lost&type=Perro`)
* **Reportar Mascota:** `POST http://localhost:8080/api/pets/report`
  ```json
  {
    "name": "Firulais",
    "type": "Perro",
    "breed": "Quiltro",
    "color": "Negro",
    "status": "lost",
    "usuarioId": 1
  }
  ```

### 3. Coincidencias (Requiere JWT)

* **Buscar Coincidencias:** `GET http://localhost:8080/api/coincidencias/buscar?especie=Perro&color=Negro`
* **Listar Coincidencias:** `GET http://localhost:8080/api/coincidencias`

### 4. Comunidad (Requiere JWT)

* **Historias de Éxito:** `GET http://localhost:8080/api/success-stories`

---

## 📝 Documentación API (Swagger)

Una vez que los microservicios estén corriendo, puedes acceder a la documentación interactiva de Swagger UI directamente en los puertos locales de cada servicio, por ejemplo:
* Mascotas: `http://localhost:8081/swagger-ui/index.html`
* Usuarios: `http://localhost:8082/swagger-ui/index.html`

---
*Desarrollado para el proyecto DNF (Dog Not Found) Backend.*