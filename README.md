# 🗺️ MetaMapa

> *La inteligencia colectiva, georreferenciada.*

**MetaMapa** es una plataforma colaborativa y de código abierto para recopilar, organizar y visualizar hechos geolocalizados en tiempo real. Desarrollada como Trabajo Práctico Anual para la materia **Diseño de Sistemas de Información** — UTN, 2025 — Grupo N°5.

---

## ¿Qué es MetaMapa?

Cada inundación, incendio, corte de luz o evento social que ocurre en el territorio deja una huella. **MetaMapa** transforma esas huellas en datos visuales, accesibles y verificables para comunidades, ONGs, universidades y organismos públicos.

- 📍 Los hechos se ubican en un **mapa interactivo**
- 🗂️ Se organizan en **colecciones** temáticas administradas
- 🤝 Cualquier persona puede **contribuir**, de forma anónima o registrada
- 🔍 Los administradores **curan** el contenido mediante algoritmos de consenso

---

## Arquitectura del Sistema

El sistema está compuesto por **microservicios independientes** que se comunican vía HTTP REST:

```
┌─────────────────────────────────────────────────────┐
│                  SISTEMA METAMAPA                   │
│                                                     │
│  ┌──────────────┐      ┌───────────────────────┐   │
│  │ Fuente       │─────▶│                       │   │
│  │ Estática     │      │   Servicio Agregador   │   │
│  │ (CSV, DB)    │      │                       │   │
│  └──────────────┘      │  • Colecciones        │   │
│                        │  • Algoritmos de      │   │
│  ┌──────────────┐      │    Consenso           │   │
│  │ Fuente       │─────▶│  • Solicitudes        │   │
│  │ Proxy        │      │                       │   │
│  │ (APIs ext.)  │      └───────────────────────┘   │
│  └──────────────┘              │                   │
│                                ▼                   │
│  ┌──────────────┐      ┌───────────────────────┐   │
│  │ Fuente       │─────▶│   Frontend (MVC)      │   │
│  │ Dinámica     │      │   Thymeleaf + Spring  │   │
│  │ (Usuarios)   │      └───────────────────────┘   │
│  └──────────────┘                                  │
│                                                     │
│  ┌──────────────┐      ┌───────────────────────┐   │
│  │ Auth Service │◀────▶│   Auth Manager        │   │
│  │ (JWT)        │      │   (Validación remota) │   │
│  └──────────────┘      └───────────────────────┘   │
└─────────────────────────────────────────────────────┘
```

### Módulos

| Módulo | Puerto | Descripción |
|---|---|---|
| `fuenteDinamica` | 8080 | Hechos aportados por usuarios, CRUD completo |
| `fuenteEstatica` | 8082 | Lectura de datasets CSV/DB (patrón Factory) |
| `fuenteProxy` | 8083 | Adaptador a APIs externas (patrón Strategy + Adapter) |
| `servicioAgregador` | 8081 | Colecciones, fuentes, solicitudes y estadísticas |
| `authService` | 8087 | Emisión y validación de tokens JWT |
| `serverFront` | — | Cliente liviano con Thymeleaf (SSR) |

---

## Stack Tecnológico

- **Backend:** Java 21 · Spring Boot · Spring MVC · Spring Security · JPA/Hibernate
- **Frontend:** Thymeleaf · HTML/CSS · JavaScript · OpenStreetMap (Leaflet)
- **Base de datos:** MySQL (esquemas separados por módulo)
- **Autenticación:** JWT (access token + refresh token)
- **Normalización semántica:** HuggingFace `all-MiniLM-L6-v2`
- **Build:** Maven (proyecto multimodulo)

---

## Patrones de Diseño Aplicados

```
🏭 Factory Method    →  Fuente Estática (DatasetCSV, DatasetDB, ...)
🔌 Adapter           →  Fuente Proxy (normaliza respuestas de APIs externas)
🎯 Strategy          →  Algoritmos de consenso + selección de fuente proxy
📐 Template Method   →  Clase abstracta Fuente con pasos comunes
🏗️  MVC              →  Arquitectura general del frontend
🔒 Auth Manager      →  Validación remota centralizada de tokens JWT
```

---

## Roles de Usuario

| Rol | Puede hacer |
|---|---|
| **Visualizador** | Ver hechos y colecciones, filtrar, solicitar eliminación |
| **Contribuyente** | Todo lo anterior + cargar/editar hechos propios (7 días) |
| **Administrador** | Todo lo anterior + gestionar colecciones, fuentes, solicitudes |
| **Anónimo** | Ver colecciones y hechos sin login |

---

## API REST — Endpoints Principales

### Fuente Dinámica
```http
GET    /api/dinamica                    # Listar hechos
POST   /api/dinamica                    # Crear hecho (anónimo)
POST   /api/dinamica/{idUsuario}        # Crear hecho (usuario registrado)
PUT    /api/dinamica/{id}               # Editar hecho (solo si editable=true)
DELETE /api/dinamica/{id}               # Eliminar hecho

POST   /api/dinamica/usuarios           # Registrar usuario
POST   /api/dinamica/usuarios/login     # Login
```

### Servicio Agregador
```http
GET    /api/colecciones                             # Listar colecciones
POST   /api/colecciones                             # Crear colección
GET    /api/colecciones/{id}/hechos?curada=true     # Hechos curados de una colección
PUT    /api/colecciones/{id}/algoritmo-consenso     # Cambiar algoritmo

POST   /api/solicitudes/eliminacion                 # Crear solicitud
PUT    /api/solicitudes/eliminacion/{id}/aprobar    # Aprobar solicitud
PUT    /api/solicitudes/eliminacion/{id}/denegar    # Rechazar solicitud
```

### Fuente Estática
```http
GET    /api/estatica/{tipo}/{archivo}               # Ej: /api/estatica/CSV/desastres.csv
```

### Fuente Proxy
```http
GET    /api/proxy/{api}                             # Ej: /api/proxy/disilab
GET    /api/proxy/{api}/{id}
```

### Auth Service
```http
POST   /api/auth                                    # Login → devuelve tokens JWT
POST   /api/auth/refresh                            # Renovar access token
GET    /api/auth/user/roles-permisos                # Roles del usuario autenticado
GET    /api/auth/validation                         # Validar token (usado por AuthManager)
```

---

## Algoritmos de Consenso

Las colecciones pueden configurarse con distintos algoritmos para determinar qué hechos son válidos en modo **curado**:

| Algoritmo | Descripción |
|---|---|
| `MAYORIASIMPLE` | El hecho aparece en más del 50% de las fuentes |
| `ABSOLUTA` | El hecho debe aparecer en todas las fuentes |
| `MULTIPLESMENCIONES` | El hecho aparece en al menos 2 fuentes |
| `NOHAYALGORITMO` | Se muestran todos los hechos sin filtrar |

---

## Normalización Semántica

Para evitar duplicados, el sistema incluye un **Normalizador** que compara títulos de hechos usando embeddings semánticos:

1. Los títulos se preprocesan (minúsculas, sin espacios extra)
2. Se consulta la API de HuggingFace (`all-MiniLM-L6-v2`)
3. Si la similitud coseno supera **0.7**, el hecho se considera duplicado y no se persiste

---

## Estructura del Proyecto

```
2025-tpa-ma-ma-grupo-05/
├── fuenteDinamica/         # Módulo de hechos de usuarios
├── fuenteEstatica/         # Módulo de datasets estáticos
├── fuenteProxy/            # Módulo adaptador de APIs externas
├── servicioAgregador/      # Módulo central: colecciones y solicitudes
├── servicioAutenticacion/  # Microservicio de autenticación JWT
├── frontend/               # Frontend MVC con Thymeleaf
├── estadisticas/           # Microservicio de Estadisticas
└── pom.xml                 # POM raíz multimodulo
```

---

## Entregas del TPA

| # | Tema | Estado |
|---|---|---|
| Entrega 1 | Modelado de dominio inicial, diagrama de clases y casos de uso | ✅ |
| Entrega 2 | Arquitectura multimódulo, patrón de capas, MVC | ✅ |
| Entrega 3 | APIs REST completas, endpoints documentados, diagrama de arquitectura | ✅ |
| Entrega 4 | Maquetado de interfaz de usuario, diseño UX/UI completo | ✅ |
| Entrega 5 | Arquitectura web MVC, autenticación JWT, paginación, AuthManager | ✅ |

---

## Equipo

**Grupo N°5** — Diseño de Sistemas de Información · UTN · 2025

---

> *"Transformar la inteligencia colectiva en acción."*
