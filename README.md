# LogiFlow - Sistema de Gestión Logística Distribuida

Sistema completo de gestión logística basado en una arquitectura de microservicios, con orquestación mediante Docker y Kubernetes, incluyendo un frontend moderno en React.

Este proyecto implementa patrones de diseño modernos para aplicaciones distribuidas, incluyendo seguridad centralizada con JWT, limitación de tráfico (Rate Limiting), enrutamiento dinámico, escalabilidad horizontal e interfaces interactivas con mapas en tiempo real.

## 🚀 Características Principales

- **Arquitectura de Microservicios**: 5 servicios independientes desarrollados en Spring Boot + 1 servicio Node.js.
- **API Gateway**: Kong Gateway como punto de entrada único, manejando autenticación y enrutamiento.
- **Frontend Moderno**: Aplicación React-Vite con Apollo Client, Socket.io y mapas interactivos (Leaflet).
- **Seguridad**: Autenticación JWT centralizada y autorización basada en roles.
- **Orquestación**: Docker Compose para desarrollo y Kubernetes (Minikube) para producción.
- **Bases de Datos**: PostgreSQL para datos de negocio y MongoDB para configuraciones.
- **GraphQL**: Servicio dedicado para consultas unificadas y orquestación de datos.
- **Notificaciones en Tiempo Real**: WebSockets para actualizaciones instantáneas.
- **Monitoreo**: Dashboard Konga para administración visual del Gateway.
- **Escalabilidad**: Despliegue automático en Kubernetes con replicación de servicios.

## 🏗️ Arquitectura del Sistema

El sistema completo se compone de un **Frontend React**, **5 microservicios backend**, **1 servicio Node.js** para notificaciones y un **API Gateway** (Kong), todos orquestados mediante Docker o Kubernetes.

### Componentes del Sistema

| Componente          | Tecnología     | Puerto (Docker/Local) | Puerto (K8s) | Descripción |
|---------------------|----------------|-----------------------|---|-------------|
| **API Gateway**    | Kong Gateway  | `8000` (Proxy)       | `8000` | Punto de entrada único. Gestiona seguridad y enrutamiento. |
| **Dashboard**      | Konga         | `1337` (GUI)         | - | Panel visual para administrar el Gateway. |
| **Auth Service**   | Spring Boot   | `8081`               | `8081` | Gestión de usuarios y generación de JWT. |
| **Fleet Service**  | Spring Boot   | `8082`               | `8082` | Gestión de conductores y vehículos. |
| **Order Service**  | Spring Boot   | `8083`               | `8083` | Gestión de pedidos y envíos. |
| **Billing Service**| Spring Boot   | `8084`               | `8084` | Facturación y cobros. |
| **GraphQL Service**| Spring for GraphQL   | `8085`               | `8085` | Orquestador y punto de consulta unificado. |
| **Notification Service** | Node.js | `3001` | `3001` | WebSockets para notificaciones en tiempo real. |
| **Frontend**       | React + Vite | `5173` | `3000` | Aplicación web con dashboards para clientes y conductores. |

### Stack Tecnológico del Frontend

- **Framework**: React 19 con Vite
- **Estado Global**: Apollo Client para GraphQL
- **Enrutamiento**: React Router v7
- **Comunicación en Tiempo Real**: Socket.io Client
- **Mapas**: Leaflet y React-Leaflet
- **HTTP**: Axios
- **Linting**: ESLint

### Bases de Datos
- **PostgreSQL (Puerto 5433)**: Almacena los datos de negocio (`db_auth`, `db_fleet`, `db_orders`, `db_billing`).
- **PostgreSQL (Puerto Interno)**: Base de datos de configuración de Kong.
- **MongoDB (Puerto Interno)**: Base de datos de configuración de Konga.

### Diagrama de Flujo

```
┌─────────────────────────────────────────────────────────────────┐
│                      LOGIFLOW - ARQUITECTURA                     │
│                                                                   │
│  ┌──────────────────┐         ┌────────────────────────────┐   │
│  │    Frontend      │         │    Navegadores / Clientes  │   │
│  │  React + Vite   │◄────────┤                            │   │
│  │  (Puerto 5173)  │         │  • Client Dashboard        │   │
│  │                  │         │  • Driver Dashboard        │   │
│  └────────┬─────────┘         │  • Admin Panel             │   │
│           │                   └────────────────────────────┘   │
│           │                                                     │
│           │ HTTP + WebSocket                                    │
│           │                                                     │
│  ┌────────▼──────────────────────────────────────┐             │
│  │    API GATEWAY (Kong)                          │             │
│  │    Puerto 8000 - Autenticación JWT + Routing  │             │
│  └────────┬──────────────────────────────────────┘             │
│           │                                                     │
│    ┌──────┴──────────┬─────────────┬───────────┬─────────────┐ │
│    │                 │             │           │             │ │
│    ▼                 ▼             ▼           ▼             ▼ │
│  ┌──────┐      ┌──────────┐    ┌───────┐  ┌────────┐    ┌─────┐│
│  │ Auth │      │  Fleet   │    │Orders │  │Billing │    │ GQL ││
│  │ 8081 │      │   8082   │    │ 8083  │  │  8084  │    │8085 ││
│  └──────┘      └──────────┘    └───────┘  └────────┘    └─────┘│
│                                                             │    │
│  ┌────────────────────────────────────────────────────────┘    │
│  │                                                             │
│  ▼                                                             │
│  ┌──────────────────────┐    ┌──────────────────────┐        │
│  │ Notification Service │    │   PostgreSQL (5433)  │        │
│  │   Node.js (3001)     │    │ Auth, Fleet, Orders  │        │
│  │   WebSockets         │    │      Billing         │        │
│  └──────────────────────┘    └──────────────────────┘        │
│                                                                │
│                    ┌─────────────────────┐                    │
│                    │   MongoDB / Konga   │                    │
│                    │   (Config Kong)     │                    │
│                    └─────────────────────┘                    │
└─────────────────────────────────────────────────────────────────┘
```

## 🛠️ Tecnologías Utilizadas

### Backend
- **Java 17+**, Spring Boot 3.x, Maven
- **GraphQL**: Spring for GraphQL
- **Node.js** (Notification Service)

### Frontend
- **React 19** con Vite
- **Apollo Client** para GraphQL
- **React Router v7** para enrutamiento
- **Socket.io Client** para WebSockets
- **Leaflet + React-Leaflet** para mapas interactivos
- **Axios** para peticiones HTTP

### Infraestructura & DevOps
- **Contenedores**: Docker, Docker Compose
- **Orquestación**: Kubernetes (Minikube para desarrollo)
- **API Gateway**: Kong Gateway
- **Bases de Datos**: PostgreSQL, MongoDB
- **Herramientas**: Git, IntelliJ IDEA (Backend), VS Code (Frontend)

## 📋 Requisitos Previos

Antes de comenzar, asegúrate de tener instalados:

### Esencial (para ambos entornos)
- **Java JDK 17+** - Backend (Spring Boot)
- **Maven 3.6+** - Construcción del backend
- **Node.js 18+ y npm** - Frontend y Notification Service
- **Docker y Docker Compose** - Contenedores
- **Git** - Control de versiones

### Opcional (para Kubernetes)
- **Minikube 1.30+** - Cluster Kubernetes local
- **kubectl** - Cliente de Kubernetes
- **Docker** (requisito para Minikube)

## 🚀 Instalación y Ejecución

### Opción 1: Ejecución Completa (Docker Compose + Local)

Sigue estos pasos para levantar el entorno de desarrollo local.

#### Paso 1: Clonar los Repositorios

```bash
# Backend
git clone https://github.com/erickPatri/logiflow-backend.git && cd logiflow_backend

# Frontend (en otra terminal o después)
git clone https://github.com/erickPatri/logiflow-frontend.git && cd logiflow_frontend/logiflow-web
```

#### Paso 2: Levantar la Infraestructura (Docker)

En la carpeta raíz del backend (`logiflow_backend`):

```bash
docker-compose up -d
```

**Importante**: Espera aproximadamente 30 segundos después de ejecutar este comando. Las bases de datos (PostgreSQL y MongoDB) necesitan tiempo para iniciarse antes de que Kong pueda conectarse.

**Nota sobre Base de Datos**: El archivo `docker-compose.yml` expone PostgreSQL en el puerto 5433 (para no chocar con instalaciones locales en el puerto 5432).

Si las bases de datos no se crean automáticamente, usa un cliente como PGAdmin para conectarte a `localhost:5433` (Usuario: `postgres`, Contraseña: `admin`) y crea manualmente las bases de datos: `db_auth`, `db_fleet`, `db_orders`, `db_billing`.

#### Paso 3: Ejecutar los Microservicios Backend

En cada carpeta de microservicio (`microservicios/auth-service`, `microservicios/fleet-service`, etc.):

**Opción A**: Con Maven desde terminal
```bash
cd microservicios/auth-service
mvn clean install
mvn spring-boot:run
```

**Opción B**: Desde el IDE (IntelliJ IDEA)
- Click derecho en `AuthServiceApplication.java` > Run

**Servicios a ejecutar** (en 5 terminales diferentes):
1. `microservicios/auth-service/src/main/java/.../AuthServiceApplication.java` (Puerto 8081)
2. `microservicios/fleet-service/src/main/java/.../FleetServiceApplication.java` (Puerto 8082)
3. `microservicios/order-service/src/main/java/.../OrderServiceApplication.java` (Puerto 8083)
4. `microservicios/billing-service/src/main/java/.../BillingServiceApplication.java` (Puerto 8084)
5. `microservicios/graphql-service/src/main/java/.../GraphQLServiceApplication.java` (Puerto 8085)

**Verificación**: Asegúrate de que la consola no muestre errores de conexión y que los 5 servicios estén corriendo simultáneamente.

#### Paso 4: Ejecutar la Notification Service (Node.js)

En la carpeta `microservicios/notification-service`:

```bash
npm install
npm start
```

El servicio se ejecutará en el puerto `3001`.

#### Paso 5: Ejecutar el Frontend

En la carpeta `logiflow_frontend/logiflow-web`:

```bash
# Instalar dependencias
npm install

# Verificar que .env esté configurado correctamente
cat .env

# Ejecutar servidor de desarrollo
npm run dev
```

El frontend estará disponible en `http://localhost:5173`

**Verificar conexiones**: El frontend debe conectarse exitosamente a:
- GraphQL Server: `http://localhost:8085/graphql`
- WebSocket Server: `http://localhost:3001`
- Auth Service: `http://localhost:8001/auth`
- Fleet Service: `http://localhost:8082`
- Order Service: `http://localhost:8083`

---

### Opción 2: Despliegue con Kubernetes (Minikube)

Esta opción es ideal para simular un ambiente de producción.

#### Paso 1: Iniciar Minikube

```bash
minikube start --driver=docker
# Verifica el estado
minikube status
```

#### Paso 2: Construir y Pushear las Imágenes Docker

**Nota**: Las imágenes deben estar en Docker Hub o un registro privado accesible.

```bash
# Auth Service
cd microservicios/auth-service
docker build -t erickvinu/auth-service:v1 .
docker push erickvinu/auth-service:v1

# Fleet Service
cd ../fleet-service
docker build -t erickvinu/fleet-service:v1 .
docker push erickvinu/fleet-service:v1

# Order Service
cd ../order-service
docker build -t erickvinu/order-service:v1 .
docker push erickvinu/order-service:v1

# Notification Service
cd ../notification-service
docker build -t erickvinu/notification-service:v1 .
docker push erickvinu/notification-service:v1

# GraphQL Service
cd ../graphql-service
docker build -t erickvinu/graphql-service:v1 .
docker push erickvinu/graphql-service:v1
```

#### Paso 3: Aplicar los Manifiestos de Kubernetes

```bash
# Desde la carpeta logiflow_backend/k8s
kubectl apply -f auth-deployment.yaml
kubectl apply -f fleet-deployment.yaml
kubectl apply -f order-deployment.yaml
kubectl apply -f notification-deployment.yaml
kubectl apply -f graphql-deployment.yaml
```

#### Paso 4: Verificar Despliegues

```bash
# Ver pods
kubectl get pods

# Ver servicios
kubectl get svc

# Ver logs de un pod
kubectl logs <pod-name>
```

#### Paso 5: Exponer Servicios (Opcional)

Para acceder a los servicios desde tu máquina local:

```bash
minikube service auth-service
minikube service fleet-service
minikube service order-service
minikube service notification-service
minikube service graphql-service
```

#### Paso 6: Acceder al Dashboard de Kubernetes

```bash
minikube dashboard
```

---

### Opción 3: Build Rápido (Todo Junto)

Si ya tienes todo configurado:

```bash
# Terminal 1: Base de datos + Backend
cd logiflow_backend
docker-compose up -d

# En múltiples terminales paralelamente:
(cd microservicios/auth-service && mvn spring-boot:run) &
(cd microservicios/fleet-service && mvn spring-boot:run) &
(cd microservicios/order-service && mvn spring-boot:run) &
(cd microservicios/billing-service && mvn spring-boot:run) &
(cd microservicios/graphql-service && mvn spring-boot:run) &
(cd microservicios/notification-service && npm start) &

# Terminal 2: Frontend
cd logiflow_frontend/logiflow-web
npm run dev
```

## 📖 Uso del Sistema

### 1. Acceder a la Aplicación

La aplicación consta de una interfaz web moderna con dashboards específicos:

```
Frontend: http://localhost:5173
```

**Usuarios de Prueba** (credenciales por defecto):
- **Admin**: `erick_admin` / `admin`
- **Conductor**: `pepe_moto` / `123`
- **Cliente**: `pedro` / `123`

**Nota**: Verifica la tabla `users` en `db_auth` para usuarios disponibles.

### 2. Dashboards Disponibles

#### Dashboard de Cliente
- Ver pedidos activos e histórico
- Crear nuevos pedidos
- Recibir notificaciones de estado

#### Dashboard de Conductor
- Ver asignaciones del día
- Aceptar/Rechazar entregas
- Ver ubicación en tiempo real

#### Dashboard de Admin/Supervisor
- Monitoreo de toda la flota
- Consultas GraphQL 
- Visualización de vehículos y pedidos
- Exportación de pedidos a formato csv 

### 3. Pruebas con Postman (API REST)

**Importante**: Todas las peticiones deben dirigirse al Puerto `8000` (API Gateway). **NO** llames directamente a los puertos `8081`, `8082`, etc.

#### Paso 1: Autenticación (Obligatorio)

- **Método**: `POST`
- **URL**: `http://localhost:8000/api/auth/login`
- **Body (JSON)**:
  ```json
  {
      "username": "erick_admin",
      "password": "admin"
  }
  ```

**Respuesta Esperada**: 
```json
{
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

Copia este token para usar en las siguientes peticiones.

#### Paso 2: Consumo de Rutas Protegidas

Para consultar datos, envía el token en el header `Authorization`:

```http
GET http://localhost:8000/api/fleet/vehicles
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

**En Postman**:
- Type: `Bearer Token`
- Token: *(Pega aquí el token)*

**Verificación**: Si accedes sin token o con uno expirado, recibirás: `401 Unauthorized`

### Endpoints Principales

#### Auth Service
- `POST /api/auth/login` - Autenticarse
- `POST /api/auth/register` - Registrar usuario
- `POST /api/auth/refresh` - Renovar token

#### Fleet Service
- `GET /api/fleet/vehicles` - Listar vehículos
- `GET /api/fleet/drivers` - Listar conductores
- `GET /api/fleet/drivers/{id}` - Obtener conductor específico
- `GET /api/fleet/vehicles/{id}` - Obtener vehículo específico

#### Order Service
- `GET /api/orders` - Listar pedidos
- `POST /api/orders` - Crear pedido
- `GET /api/orders/{id}` - Obtener pedido específico
- `PUT /api/orders/{id}/status` - Actualizar estado del pedido

#### Billing Service
- `POST /api/bills` - Crear factura
- `GET /api/bills` - Listar facturas
- `GET /api/bills/{id}` - Obtener factura específica

#### GraphQL Service
- `POST /graphql` - Consultas/Mutaciones GraphQL
- `GET /graphiql` - Interfaz gráfica para pruebas (sin autenticación)

---

### 4. Consultas GraphQL

#### Acceso a la Interfaz GraphiQL

Abre en tu navegador: `http://localhost:8085/graphiql`

Aquí puedes escribir y ejecutar consultas interactivamente.

#### Ejemplo: Obtener Pedidos con Información de Vehículos

```graphql
query {
  orders {
    id
    description
    status
    deliveryLocation
    # Datos agregados desde Fleet Service
    vehicle {
      plate
      model
      brand
      vehicleType
    }
  }
}
```

#### Desde Postman/Frontend

```http
POST http://localhost:8085/graphql
Authorization: Bearer <tu-token>
Content-Type: application/json

{
  "query": "query { orders { id description status } }"
}
```

---

### 5. WebSockets (Notificaciones en Tiempo Real)

El frontend se conecta automáticamente a `http://localhost:3001` para recibir:
- Actualizaciones de estado de pedidos
- Ubicación en tiempo real de conductores
- Notificaciones de entrega
- Alertas del sistema

**Ejemplo (JavaScript)**:
```javascript
import io from 'socket.io-client';

const socket = io('http://localhost:3001');

socket.on('order:updated', (data) => {
  console.log('Pedido actualizado:', data);
});

socket.emit('subscribe:orders', { clientId: 123 });
```

## 🔧 Solución de Problemas

### Backend - Errores Generales

- **Errores de conexión a la base de datos**:
  - Verifica que Docker esté corriendo: `docker ps`
  - Verifica que PostgreSQL esté listo: `docker logs postgres-container`
  - Espera 30-45 segundos después de `docker-compose up -d`

- **Kong no inicia**:
  - Espera 30 segundos después de `docker-compose up -d`
  - Verifica logs: `docker logs kong-container`
  - Asegúrate que MongoDB y PostgreSQL estén ejecutándose

- **Puertos ocupados**:
  - Backend: `8000, 8081-8085`
  - Frontend: `5173`
  - Notification: `3001`
  - Base de datos: `5433` (PostgreSQL), `27017` (MongoDB)
  - Konga Dashboard: `1337`
  
- **Dependencias Maven**:
  ```bash
  mvn clean install
  mvn -U clean install  # Fuerza actualización de dependencias
  ```

### Frontend - Errores Comunes

- **Conexión rechazada a APIs**:
  - Verifica que los microservicios estén ejecutándose
  - Revisa el archivo `.env` en `logiflow_frontend/logiflow-web`
  - Asegúrate que las URLs apunten a `localhost:8000` (Gateway)

- **Token expirado**:
  - Vuelve a autenticarte desde el dashboard
  - Verifica que el backend esté devolviendo tokens válidos

- **Mapas no se cargan**:
  - Verifica conexión a Internet
  - Revisa la consola del navegador para errores de Leaflet
  - Asegúrate que React-Leaflet esté correctamente instalado

- **WebSockets no conectan**:
  ```bash
  # Verifica que Notification Service esté ejecutándose
  curl http://localhost:3001
  
  # Revisa logs del frontend en DevTools (F12)
  ```

### Kubernetes - Errores con Minikube

- **Minikube no inicia**:
  ```bash
  minikube delete  # Elimina cluster anterior
  minikube start --driver=docker
  ```

- **Pods no se inician**:
  ```bash
  kubectl describe pod <pod-name>  # Ve los detalles del error
  kubectl logs <pod-name>           # Ve los logs
  ```

- **ImagePullBackOff**:
  - Las imágenes Docker no existen en Docker Hub
  - Solución: Construir y pushear imágenes manualmente:
    ```bash
    docker build -t erickvinu/auth-service:v1 -f Dockerfile .
    docker push erickvinu/auth-service:v1
    ```

- **Conectarse a base de datos desde K8s**:
  - Los pods usan `host.minikube.internal:5433` para acceder a PostgreSQL
  - Asegúrate que las BDs estén creadas en el host

- **Acceder a servicios K8s**:
  ```bash
  minikube service <service-name>  # Abre un túnel a través del navegador
  kubectl port-forward svc/auth-service 8081:8081  # Redirecciona puerto local
  ```

### Verificación Rápida de Salud

```bash
# Backend
curl http://localhost:8000/health
curl http://localhost:8081/auth/health
curl http://localhost:8082/fleet/health
curl http://localhost:8083/orders/health

# Frontend
curl http://localhost:5173

# Notification Service
curl http://localhost:3001

# GraphQL
curl -X POST http://localhost:8085/graphql -H "Content-Type: application/json" -d '{"query":"{ __typename }"}'
```

---

## 📁 Estructura del Proyecto

```
logiflow_backend/
├── docker-compose.yml          # Orquestación de contenedores locales
├── k8s/                        # Manifiestos de Kubernetes
│   ├── auth-deployment.yaml
│   ├── fleet-deployment.yaml
│   ├── order-deployment.yaml
│   ├── notification-deployment.yaml
│   └── graphql-deployment.yaml
├── microservicios/
│   ├── auth-service/           # Servicio de autenticación (Spring Boot)
│   ├── fleet-service/          # Servicio de flota (Spring Boot)
│   ├── order-service/          # Servicio de pedidos (Spring Boot)
│   ├── billing-service/        # Servicio de facturación (Spring Boot)
│   ├── graphql-service/        # Servicio GraphQL (Spring Boot)
│   └── notification-service/   # Servicio de notificaciones (Node.js)
├── sql/
│   └── init.sql                # Scripts de inicialización de BD
└── README.md

logiflow_frontend/
└── logiflow-web/
    ├── src/
    │   ├── pages/              # Páginas de la aplicación
    │   │   ├── ClientDashboard.jsx
    │   │   ├── DriverDashboard.jsx
    │   │   └── ...
    │   ├── components/         # Componentes reutilizables
    │   │   ├── FleetMap.jsx    # Mapa interactivo
    │   │   ├── ProtectedRoute.jsx
    │   │   └── ...
    │   ├── App.jsx
    │   └── main.jsx
    ├── .env                    # Variables de entorno
    ├── package.json
    ├── vite.config.js
    └── README.md
```

---

## 🎨 Características del Frontend
**Link del repo del Frontend**

- **Nombre**: Erick Patricio Moreira Vinueza
- **GitHub**: (https://github.com/erickPatri/logiflow-backend)

### Dashboards Interactivos

- **Cliente**: 
  - Crear pedidos en tiempo real
  - Historial de transacciones
  - Sistema de notificaciones

- **Conductor**: 
  - Ver asignaciones diarias
  - Rastreo GPS en tiempo real
  - Aceptar/Rechazar entregas

- **Supervisor**: 
  - Monitoreo de flota completa
  - Consultas GraphQL personalizadas
  - Estadísticas y reportes
  - Control de administración

### Tecnologías Frontend Destacadas

- **Apollo Client**: Gestión de estado global con GraphQL
- **React Router**: Navegación sin recarga de página
- **Socket.io**: Actualizaciones en tiempo real
- **Leaflet Maps**: Visualización de rutas y ubicaciones
- **Responsive Design**: Compatible con móvil, tablet y desktop

---

## 🛠️ Guía de Desarrollo

### Agregar un Nuevo Microservicio

1. **Crear la carpeta**: `microservicios/nuevo-servicio`
2. **Configurar Spring Boot**:
   ```bash
   mvn archetype:generate -DgroupId=com.logiflow -DartifactId=nuevo-service
   ```
3. **Crear Dockerfile** en la raíz del servicio
4. **Crear deployment YAML** en `k8s/nuevo-deployment.yaml`
5. **Registrar en Kong Gateway** con el puerto correspondiente

### Agregar una Nueva Página en el Frontend

1. **Crear componente**: `src/pages/NuevaPage.jsx`
2. **Agregar ruta**: Editar `src/App.jsx`
3. **Configurar protección**: Usar `<ProtectedRoute>`
4. **Prubar**: `npm run dev`

### Variables de Entorno

**Backend (.env)**:
```env
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5433/db_auth
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=admin
JWT_SECRET=tu-secret-key
```

**Frontend (.env)**:
```env
VITE_AUTH_URL=http://localhost:8000/api/auth
VITE_GRAPHQL_URL=http://localhost:8085/graphql
VITE_WS_URL=http://localhost:3001
```

### Build para Producción

```bash
# Backend
mvn clean package

# Frontend
npm run build
# Archivos generados en logiflow-web/dist/
```

---

## 📊 Escalabilidad y Rendimiento

### Replicación en Kubernetes

Modificar el campo `replicas` en los archivos YAML:

```yaml
spec:
  replicas: 3  # Aumentar para mayor disponibilidad
```

### Load Balancing

Kong Gateway distribuye automáticamente el tráfico entre las réplicas.

### Monitoreo

```bash
# Ver recursos usados por los pods
kubectl top pods

# Ver eventos del cluster
kubectl get events --sort-by='.lastTimestamp'
```

---

## 📞 Contacto y Contribuciones

### Información del Autor

- **Nombre**: Erick Patricio Moreira Vinueza
- **GitHub**: [erickPatri](https://github.com/erickPatri)
- **Universidad**: ESPE (Escuela Politécnica del Ejército)
- **Materia**: Aplicaciones Distribuidas

---





