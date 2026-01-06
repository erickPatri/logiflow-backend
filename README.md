# LogiFlow - Backend Microservices Architecture

Sistema de gestión logística distribuida basado en una arquitectura de microservicios, orquestado con Docker y gestionado mediante un API Gateway (Kong).

Este proyecto implementa patrones de diseño modernos para aplicaciones distribuidas, incluyendo seguridad centralizada, limitación de tráfico (Rate Limiting) y enrutamiento dinámico.

---

## Arquitectura del Sistema

El backend se compone de **4 microservicios** independientes desarrollados en Spring Boot, comunicándose a través de un **API Gateway**.

| Componente | Tecnología | Puerto (Docker/Local) | Descripción |
| :--- | :--- | :--- | :--- |
| **API Gateway** | Kong Gateway | `8000` (Proxy) | Punto de entrada único. Gestiona seguridad y enrutamiento. |
| **Dashboard** | Konga | `1337` (GUI) | Panel visual para administrar el Gateway. |
| **Auth Service** | Spring Boot | `8081` | Gestión de usuarios y generación de JWT. |
| **Fleet Service** | Spring Boot | `8082` | Gestión de conductores y vehículos. |
| **Order Service** | Spring Boot | `8083` | Gestión de pedidos y envíos. |
| **Billing Service**| Spring Boot | `8084` | Facturación y cobros. |

### Bases de Datos
* **PostgreSQL (Puerto 5433):** Almacena los datos de negocio (`db_auth`, `db_fleet`, etc.).
* **PostgreSQL (Puerto Interno):** Base de datos de configuración de Kong.
* **MongoDB (Puerto Interno):** Base de datos de configuración de Konga.

---

## Guía de Instalación y Ejecución

Sigue estos pasos para levantar el entorno de desarrollo completo.

### 1. Requisitos Previos
* Java JDK 17 o 21.
* Maven.
* Docker y Docker Compose instalados.
* Git.

### 2. Clonar el Repositorio
```bash
git clone [https://github.com/erickPatri/logiflow-backend.git](https://github.com/erickPatri/logiflow-backend.git)
cd logiflow-backend
```

### 3. Levantar la Infraestructura con Docker

Ejecuta el siguiente comando en la raíz del proyecto para descargar e iniciar los contenedores de bases de datos y Gateway:

```bash
docker-compose up -d
```
IMPORTANTE: Espera aproximadamente 30 segundos después de ejecutar este comando. Las bases de datos (Postgres y Mongo) necesitan tiempo para iniciarse antes de que Kong pueda conectarse.

Nota sobre Base de Datos: El archivo docker-compose.yml expone PostgreSQL en el puerto 5433 (para no chocar con instalaciones locales en el puerto 5432).

Si las bases de datos no se crean automáticamente, usa un cliente como PGAdmin para conectarte a localhost:5433 (User: postgres, Pass: admin) y crea manualmente las bases: db_auth, db_fleet, db_orders, db_billing.

### 4. Ejecutar los Microservicios

Abre el proyecto en tu IDE favorito (IntelliJ IDEA recomendado).

1. Espera a que Maven descargue las dependencias.

2. Busca y ejecuta las clases principales de cada servicio (botón "Run"):

*microservicios/auth-service/.../AuthServiceApplication.java

*microservicios/fleet-service/.../FleetServiceApplication.java

*microservicios/order-service/.../OrderServiceApplication.java

*microservicios/billing-service/.../BillingServiceApplication.java

Asegúrate de que la consola no muestre errores de conexión y que los 4 servicios estén corriendo simultáneamente.

### GUÍA DE PRUEBAS CON POSTMAN

Todas las peticiones deben dirigirse al Puerto 8000 (Gateway). NO llames directamente a los puertos 8081, 8082, etc.

1. Autenticación (Paso Obligatorio). Primero debes loguearte para obtener el Token JWT necesario para las rutas protegidas.

Método: POST

URL: http://localhost:8000/api/auth/login

Body (JSON):
{
    "username": "erick_admin",
    "password": "admin"
}

(Asegúrate de tener un usuario registrado en tu base de datos db_auth).

Respuesta Esperada: Recibirás un JSON con un campo token. Cópialo.

2. Consumo de Rutas Protegidas (Ej. Flotas). Para consultar datos en los servicios de Flota, Pedidos o Facturación, debes enviar el token.

Método: GET

URL: http://localhost:8000/api/fleet/vehicles

Pestaña Authorization (Postman):

Type: Bearer Token.

Token: (Pega aquí el token obtenido en el paso anterior).

Verificación: Si intentas acceder sin token o con uno expirado, el Gateway responderá automáticamente con un error 401 Unauthorized.

