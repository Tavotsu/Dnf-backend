# DNF Backend - Arquitectura de Microservicios

Este proyecto constituye el backend de la aplicación DNF (Dogs n' Friends), desarrollado empleando **Java 21** y el ecosistema **Spring Boot**. El sistema está diseñado sobre una arquitectura de microservicios robusta y escalable, orquestada y gestionada íntegramente a través de contenedores **Docker**. Esta arquitectura promueve el bajo acoplamiento y facilita el mantenimiento y escalado de cada componente de manera independiente.

El proyecto incorpora patrones fundamentales de sistemas distribuidos, incluyendo el descubrimiento de servicios dinámico (Service Discovery) y un punto de entrada unificado y centralizado mediante un API Gateway.

## Tecnologías y Herramientas Utilizadas

* **Lenguaje Principal:** Java 21
* **Framework Principal:** Spring Boot (versiones 3.2.3 y 4.0.5)
* **Framework Cloud:** Spring Cloud (versiones 2023.0.1 y 2025.1.1)
* **Persistencia de Datos:** PostgreSQL 15, implementado mediante Spring Data JPA.
* **Contenedores y Orquestación Local:** Docker y Docker Compose
* **Comunicación entre Servicios:** OpenFeign y Resilience4j (para tolerancia a fallos)
* **Documentación Interactiva de API:** Swagger / OpenAPI 3 (mediante Springdoc)
* **Gestión de Dependencias y Construcción:** Maven (con Maven Wrapper `mvnw` incluido)
* **Reducción de Código Boilerplate:** Lombok

## Arquitectura del Sistema y Microservicios

El sistema se compone de varios módulos independientes. Todos pueden ser instanciados conjuntamente mediante el archivo `docker-compose.yml` en la raíz del proyecto.

| Servicio / Contenedor | Puerto Local | Descripción y Función Principal |
| :--- | :--- | :--- |
| **PostgreSQL DB** | `5433` | Base de datos relacional PostgreSQL. Contiene la base de datos compartida `dnf_db`. Se expone al host en el puerto 5433. |
| **Eureka Server** | `8761` | Servidor de descubrimiento (Netflix Eureka). Actúa como un directorio telefónico; todos los microservicios se registran aquí al iniciar para poder encontrarse entre sí sin requerir IPs estáticas. |
| **API Gateway** | `8080` | Punto de entrada único (`spring-cloud-starter-gateway-mvc`). Recibe todas las peticiones web y las enruta al microservicio correspondiente basándose en las rutas configuradas, resolviendo las direcciones mediante Eureka. |
| **Ms-Usuario** | `8082` | Microservicio encargado de la gestión integral de usuarios, autenticación y perfiles. Rutas base: `/api/usuarios/**`, `/api/auth/**`. |
| **Ms-Mascota** | `8081` | Microservicio responsable del alta, baja, modificación y consulta de información de las mascotas. Ruta base: `/api/pets/**`. |
| **Ms-Coincidencias** | `8083` | Microservicio que gestiona la lógica de "matches" o coincidencias entre mascotas/usuarios. Realiza llamadas internas a `Ms-Mascota` utilizando OpenFeign. Ruta base: `/api/coincidencias/**`. |
| **Ms-Comunidad** | `8094` | Microservicio orientado a la gestión de foros o historias de éxito dentro de la plataforma. Ruta base: `/api/success-stories/**`. |
| **Ms-Notificaciones** | `8095` | Microservicio independiente (sin base de datos propia) dedicado al envío de alertas y notificaciones (ej. correos electrónicos) a los usuarios. Ruta base: `/api/notificaciones/**`. |

## Requisitos Previos

Para ejecutar, compilar o modificar este proyecto en su entorno local, es estrictamente necesario contar con las siguientes herramientas instaladas:

1. [Docker](https://www.docker.com/) y [Docker Compose](https://docs.docker.com/compose/) (Obligatorio para correr el entorno completo).
2. [Java 21 JDK](https://adoptium.net/) (Obligatorio para compilar servicios de forma individual y desarrollar sin contenedores).

## Ejecución y Despliegue del Proyecto

El ecosistema permite dos modalidades principales de ejecución, dependiendo si se desea probar la aplicación en su totalidad o si se está desarrollando un componente específico.

### 1. Preparación Inicial

Independientemente del método elegido, el primer paso es obtener el código fuente:

```bash
git clone https://github.com/Tavotsu/Dnf-backend.git
cd dnf-backend
```

### 2. Despliegue Completo mediante Docker Compose (Recomendado)

Este método automatiza la construcción de las imágenes Docker para cada microservicio en Java y levanta toda la infraestructura (Base de Datos, Eureka, Gateway y todos los servicios backend) de forma secuencial y enlazada.

Ejecute el siguiente comando en la raíz del proyecto:

```bash
docker compose up --build
```

**Consideraciones Técnicas Críticas durante el Arranque:**

* **Dependencia de la Base de Datos:** Aunque los servicios están configurados para iniciar después del contenedor de PostgreSQL, el motor de base de datos tarda unos segundos adicionales en estar listo para aceptar conexiones. Es un comportamiento esperado que los microservicios Java muestren errores de conexión y se reinicien automáticamente hasta que la base de datos esté plenamente operativa. No cancele el proceso.
* **Registro en Eureka Server:** El servidor de Eureka requiere aproximadamente 10 segundos de inicialización. Durante este lapso, los demás microservicios no podrán registrarse y emitirán advertencias en la consola. Una vez Eureka esté en línea, se registrarán automáticamente en el siguiente ciclo de reintento.
* **Actualización del API Gateway:** El API Gateway delega el enrutamiento a Eureka. Cuando todos los servicios se han iniciado y registrado con éxito, el Gateway puede demorar alrededor de 30 segundos en actualizar su caché interna de rutas. Si nota que el Gateway (puerto `8080`) no encuentra las rutas hacia los microservicios inmediatamente (`404 Not Found`), simplemente espere medio minuto. Alternativamente, para forzar el refresco, puede reiniciar únicamente el contenedor del Gateway:
  ```bash
  docker compose restart api-gateway
  ```

### 3. Ejecución Local Específica para Desarrollo (Sin contenerizar los servicios Java)

Cuando se encuentra desarrollando o depurando (debugging) un microservicio en particular, es mucho más ágil ejecutar únicamente ese servicio localmente en su IDE o consola, pero conectándolo a la base de datos contenerizada.

**Paso A: Iniciar exclusivamente la Base de Datos**
Se ha proveído un archivo compose secundario dedicado exclusivamente a levantar PostgreSQL. Ejecútelo en la raíz del proyecto:

```bash
docker compose -f docker-compose-db.yml up
```
*(Nota: La base de datos estará disponible en `localhost:5433` con credenciales `admin` / `adminpassword` y la base de datos `dnf_db`).*

**Paso B: Ejecutar un microservicio de forma aislada**
Abra una nueva pestaña de terminal, navegue al directorio del servicio que desea modificar e inícielo usando el Maven Wrapper. Por ejemplo, para el microservicio de mascotas:

```bash
cd Ms-mascota
./mvnw spring-boot:run
```

**Paso C: Compilación Manual (Opcional)**
Si solo requiere compilar y empaquetar un microservicio específico en un archivo `.jar` (omitiendo la ejecución de los tests automáticos):

```bash
cd <nombre-del-directorio-del-servicio>
./mvnw -DskipTests package
```

## Próximas Mejoras y Deuda Técnica

El proyecto se encuentra en evolución activa. Se tienen identificadas las siguientes áreas de mejora estructural:

* Implementación de validaciones a nivel de DTO y Entidad utilizando la anotación `@Valid` (requiere la inclusión de la dependencia `spring-boot-starter-validation`).
* Centralización y optimización del manejo de excepciones globales mediante clases anotadas con `@ControllerAdvice` y `@ExceptionHandler`, para estandarizar las respuestas de error HTTP.
* Migración de configuraciones estáticas (como credenciales de DB y URLs de Eureka) hacia variables de entorno, preparando la aplicación para su despliegue en entornos Cloud.
* Reestructuración de la base de código (refactorización) para homogeneizar las convenciones de nombrado de paquetes, que actualmente difieren entre microservicios.
* Implementación de migraciones de base de datos versionadas utilizando herramientas como Flyway o Liquibase, reemplazando la propiedad actual `spring.jpa.hibernate.ddl-auto: update`. 