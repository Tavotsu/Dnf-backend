# DNF Backend - Arquitectura de Microservicios

Este proyecto es el backend de la aplicación DNF, construido con **Java 21** y **Spring Boot**. Utiliza una arquitectura de microservicios escalable, gestionada a través de contenedores Docker, e incluye descubrimiento de servicios y un API Gateway para centralizar las peticiones.

## 🛠️ Tecnologías Utilizadas

* **Lenguaje:** Java 21
* **Framework:** Spring Boot (3.2.3) y Spring Cloud (2023.0.1)
* **Base de Datos:** PostgreSQL 15
* **Contenedores:** Docker y Docker Compose
* **Documentación de API:** Swagger / OpenAPI (Springdoc)
* **Herramientas:** Maven, Lombok

## 🏗️ Arquitectura del Sistema

El sistema está compuesto por los siguientes servicios, todos orquestados mediante `docker-compose`:

| Servicio | Puerto Local | Descripción |
| :--- | :--- | :--- |
| **PostgreSQL DB** | `5432` | Base de datos relacional compartida (`dnf_db`). |
| **Eureka Server** | `8761` | Servidor de descubrimiento de servicios (Netflix Eureka). |
| **API Gateway** | `8080` | Punto de entrada único que enruta las peticiones a los microservicios. |
| **Ms-Usuario** | `8082` | Microservicio para la gestión de usuarios. |
| **Ms-Mascota** | `8081` | Microservicio para la gestión de mascotas. |
| **Ms-Coincidencias** | `8083` | Microservicio para la lógica de coincidencias (matches). |

## 🚀 Requisitos Previos

Para ejecutar este proyecto en tu entorno local, necesitas tener instalado:
* [Docker](https://www.docker.com/) y [Docker Compose](https://docs.docker.com/compose/)
* [Java 21 JDK](https://adoptium.net/) (Para compilar localmente)

## Proximas mejoras
* Implementacion de @Valid
* Implementacion de bloques de try catch optimizado con Exepcion handler
* Implementacion de base de datos en la nube con variables de entorno 
* Restructuracion y optimizacion de codigo 
## ⚙️ Instalación y Despliegue

Sigue estos pasos para levantar toda la infraestructura:

### 1. Clonar el repositorio
```bash
git clone <URL_DE_TU_REPOSITORIO>
cd dnf-backend



