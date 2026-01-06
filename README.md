# LogiFlow - Backend Microservices Architecture

Sistema de gestión logística distribuida basado en una arquitectura de microservicios, orquestado con Docker y gestionado mediante un API Gateway (Kong).

Este proyecto implementa patrones de diseño modernos para aplicaciones distribuidas, incluyendo seguridad centralizada con JWT, limitación de tráfico (Rate Limiting), enrutamiento dinámico y escalabilidad horizontal.

## 🚀 Características Principales

- **Arquitectura de Microservicios**: 4 servicios independientes desarrollados en Spring Boot.
- **API Gateway**: Kong Gateway como punto de entrada único, manejando autenticación y enrutamiento.
- **Seguridad**: Autenticación JWT centralizada y autorización basada en roles.
- **Orquestación**: Docker Compose para facilitar el despliegue y gestión de contenedores.
- **Bases de Datos**: PostgreSQL para datos de negocio y MongoDB para configuraciones.
- **Monitoreo**: Dashboard Konga para administración visual del Gateway.

## 🏗️ Arquitectura del Sistema

El backend se compone de **4 microservicios** independientes, comunicándose a través de un **API Gateway**.

| Componente          | Tecnología     | Puerto (Docker/Local) | Descripción |
|---------------------|----------------|-----------------------|-------------|
| **API Gateway**    | Kong Gateway  | `8000` (Proxy)       | Punto de entrada único. Gestiona seguridad y enrutamiento. |
| **Dashboard**      | Konga         | `1337` (GUI)         | Panel visual para administrar el Gateway. |
| **Auth Service**   | Spring Boot   | `8081`               | Gestión de usuarios y generación de JWT. |
| **Fleet Service**  | Spring Boot   | `8082`               | Gestión de conductores y vehículos. |
| **Order Service**  | Spring Boot   | `8083`               | Gestión de pedidos y envíos. |
| **Billing Service**| Spring Boot   | `8084`               | Facturación y cobros. |

### Bases de Datos
- **PostgreSQL (Puerto 5433)**: Almacena los datos de negocio (`db_auth`, `db_fleet`, `db_orders`, `db_billing`).
- **PostgreSQL (Puerto Interno)**: Base de datos de configuración de Kong.
- **MongoDB (Puerto Interno)**: Base de datos de configuración de Konga.

## 🛠️ Tecnologías Utilizadas

- **Backend**: Java 17+, Spring Boot 3.x, Maven
- **API Gateway**: Kong Gateway
- **Contenedores**: Docker, Docker Compose
- **Bases de Datos**: PostgreSQL, MongoDB
- **Herramientas**: Git, IntelliJ IDEA (recomendado)

## 📋 Requisitos Previos

Antes de comenzar, asegúrate de tener instalados:
- Java JDK 17 o superior
- Maven 3.x
- Docker y Docker Compose
- Git

## 🚀 Instalación y Ejecución

Sigue estos pasos para levantar el entorno de desarrollo completo.

### 1. Clonar el Repositorio

```bash
git clone https://github.com/erickPatri/logiflow-backend.git
cd logiflow-backend
```

### 2. Levantar la Infraestructura con Docker

Ejecuta el siguiente comando en la raíz del proyecto para descargar e iniciar los contenedores de bases de datos y Gateway:

```bash
docker-compose up -d
```

**Importante**: Espera aproximadamente 30 segundos después de ejecutar este comando. Las bases de datos (PostgreSQL y MongoDB) necesitan tiempo para iniciarse antes de que Kong pueda conectarse.

**Nota sobre Base de Datos**: El archivo `docker-compose.yml` expone PostgreSQL en el puerto 5433 (para no chocar con instalaciones locales en el puerto 5432).

Si las bases de datos no se crean automáticamente, usa un cliente como PGAdmin para conectarte a `localhost:5433` (Usuario: `postgres`, Contraseña: `admin`) y crea manualmente las bases de datos: `db_auth`, `db_fleet`, `db_orders`, `db_billing`.

### 3. Ejecutar los Microservicios

1. Abre el proyecto en tu IDE favorito (IntelliJ IDEA recomendado).
2. Espera a que Maven descargue las dependencias.
3. Busca y ejecuta las clases principales de cada servicio (botón "Run"):

   - `microservicios/auth-service/src/main/java/.../AuthServiceApplication.java`
   - `microservicios/fleet-service/src/main/java/.../FleetServiceApplication.java`
   - `microservicios/order-service/src/main/java/.../OrderServiceApplication.java`
   - `microservicios/billing-service/src/main/java/.../BillingServiceApplication.java`

4. Asegúrate de que la consola no muestre errores de conexión y que los 4 servicios estén corriendo simultáneamente.

## 📖 Uso

### Guía de Pruebas con Postman

Todas las peticiones deben dirigirse al Puerto `8000` (Gateway). **NO** llames directamente a los puertos `8081`, `8082`, etc.

#### 1. Autenticación (Paso Obligatorio)

Primero debes autenticarte para obtener el Token JWT necesario para las rutas protegidas.

- **Método**: `POST`
- **URL**: `http://localhost:8000/api/auth/login`
- **Body (JSON)**:
  ```json
  {
      "username": "erick_admin",
      "password": "admin"
  }
  ```

**Nota**: Asegúrate de tener un usuario registrado en tu base de datos `db_auth`.

**Respuesta Esperada**: Recibirás un JSON con un campo `token`. Cópialo para usar en las siguientes peticiones.

#### 2. Consumo de Rutas Protegidas

Para consultar datos en los servicios de Flota, Pedidos o Facturación, debes enviar el token en el header `Authorization`.

- **Método**: `GET`
- **URL**: `http://localhost:8000/api/fleet/vehicles`
- **Authorization** (en Postman):
  - Type: `Bearer Token`
  - Token: *(Pega aquí el token obtenido en el paso anterior)*

**Verificación**: Si intentas acceder sin token o con uno expirado, el Gateway responderá automáticamente con un error `401 Unauthorized`.

### Endpoints Principales

- **Auth Service**:
  - `POST /api/auth/login` - Iniciar sesión
  - `POST /api/auth/register` - Registrar usuario

- **Fleet Service**:
  - `GET /api/fleet/vehicles` - Listar vehículos
  - `GET /api/fleet/drivers` - Obtener conductores

- **Order Service**:
  - `GET /api/orders` - Listar pedidos
  - `POST /api/orders` - Crear pedido

- **Billing Service**:
  - `POST /api/bills` - Crear borrador de factura

**IMPORTANTE**: Para probar todas las rutas y funcionalidades puedes revisar los controladores de cada microservicio, si olvidar el /api Y TAMPOCO EL APUNTAR siempre al puerto 8000 del APIGATEWAY.

## 🔧 Solución de Problemas

- **Errores de conexión a la base de datos**: Verifica que Docker esté corriendo y que las bases de datos se hayan creado correctamente.
- **Kong no inicia**: Espera 30 segundos después de `docker-compose up -d` para que las dependencias se inicialicen.
- **Puertos ocupados**: Asegúrate de que los puertos 8000, 1337, 8081-8084 y 5433 estén libres.
- **Dependencias Maven**: Ejecuta `mvn clean install` en cada microservicio si hay problemas de dependencias.

## 📞 Contacto

- **Autor**: Erick Patricio Moreira Vinueza
- **GitHub**: [erickPatri](https://github.com/erickPatri)
- **Email**: erickvinueza11@gmail.com



