# MetaMapa: Sistema de Gestión de Mapeos Colaborativos

![Java 17](https://img.shields.io/badge/Java-17-007396?style=for-the-badge&logo=java&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.2.0-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-3.8%2B-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)
![Thymeleaf](https://img.shields.io/badge/Thymeleaf-HTML5/CSS3-005F0F?style=for-the-badge&logo=thymeleaf&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-Enabled-2496ED?style=for-the-badge&logo=docker&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![HuggingFace](https://img.shields.io/badge/HuggingFace-MiniLM_LLM-FFD21E?style=for-the-badge&logo=huggingface&logoColor=black)

> **Trabajo Práctico Anual Integrador (2025)**  
> **Asignatura:** Diseño de Sistemas de Información — UTN FRBA  
> **Equipo:** Grupo N°5

## 👥 Equipo de Desarrollo

| [Joseph Mansilla](https://github.com/josephmansilla) |  [Ignacio Castro](https://github.com/nacho-castro) | [Santiago Torres](https://github.com/SantiagoTorres24) | [Ignacio Scarfo](https://github.com/iscarfo) | [Sofia Baudo](https://github.com/SofiaBaudo) 
| :--: | :--: | :--: | :--: | :--: |
| <img src="https://avatars.githubusercontent.com/u/162230766?s=400&u=6ac208c05e9fedd414fefc12db5c38efe1c6fcd8&v=4" alt="Joseph Mansilla" width="76" height="76"> | <img src="https://avatars.githubusercontent.com/u/116680164?v=4" alt="Ignacio Castro" width="76" height="76"> | <img src="https://avatars.githubusercontent.com/u/135065796?v=4" alt="Santiago Torres" width="76" height="76"> | <img src="https://avatars.githubusercontent.com/u/164821165?v=4" alt="Ignacio Scarfo" width="76" height="76"> | <img src="https://avatars.githubusercontent.com/u/70955743?v=4" alt="Sofia Baudo" width="76" height="76">

---

## 📋 Resumen Ejecutivo

**MetaMapa** es una plataforma distribuida de gestión de mapeos colaborativos orientada a potenciar la **inteligencia colectiva**. Permite a comunidades, organizaciones no gubernamentales y organismos públicos recopilar, clasificar, validar y geolocalizar hechos y eventos relevantes en tiempo y espacio de manera confiable, abierta y descentralizada.

### 💡 Problema que Resuelve
La información crítica generada durante emergencias sociales o ambientales (focos de incendio, zonas de contaminación, inundaciones, desapariciones forzadas) suele estar dispersa, no estructurada o carecer de mecanismos de validación. MetaMapa proporciona un marco centralizado para integrar fuentes diversas, verificar contenido mediante algoritmos de consenso y procesar lenguaje natural mediante inteligencia artificial.

### 👥 Perfiles de Usuario

```text
┌─────────────────────────────────────────────────────────────────────────────────┐
│                              ROLES DE USUARIO                                   │
├──────────────────┬─────────────────────────────────┬────────────────────────────┤
│ Visualizador     │ Contribuyente                   │ Administrador              │
├──────────────────┼─────────────────────────────────┼────────────────────────────┤
│ • Acceso público │ • Anonimato o Registro          │ • Gestión de Colecciones   │
│ • Mapa interact. │ • Carga de hechos (texto/media) │ • Moderación & Solicitudes │
│ • Filtros/Buscar │ • Edición dentro de 7 días      │ • Algoritmos de Consenso   │
│ • Solicitud baja │ • Solicitud de eliminación      │ • Carga masiva (CSV >10k)  │
└──────────────────┴─────────────────────────────────┴────────────────────────────┘
```

---

## 🌟 Características Principales

- 🗺️ **Mapa Interactivo Dinámico:** Visualización georreferenciada en tiempo real de hechos con información detallada, coordenadas y soporte multimedia.
- 🔗 **Integración Multifuente:** Capacidad de consumir datos provenientes de tres tipos de fuentes:
  - **Estáticas:** Carga y parseo de archivos CSV masivos (más de 10.000 registros).
  - **Dinámicas:** Aportes directos de usuarios registrados o anónimos.
  - **Proxy:** Consumo en tiempo real de APIs REST externas (MetaMapa, Disilab).
- 🧠 **Detección de Duplicados vía IA:** Módulo Normalizador integrado con **HuggingFace LLM** (`sentence-transformers/all-MiniLM-L6-v2`) que evalúa la similitud semántica de títulos con un umbral de 0.7.
- ⚙️ **Algoritmos de Consenso Configurables:** Curaduría de hechos dentro de colecciones mediante estrategias como *Mayoría Simple*, *Absoluta*, *Múltiples Menciones* o *Sin Algoritmo*.
- 🛡️ **Sistema de Moderación y Auditoría:** Flujo de solicitudes de eliminación justificadas (mínimo 500 caracteres) y filtro anti-spam automatizado.
- 📊 **Métricas y Estadísticas:** Exportación de reportes agregados por provincia, categoría y nivel de spam tanto en JSON como en formato CSV.

---

## 🎯 Objetivos del Proyecto

### Funcionales
- Facilitar la recolección geolocalizada de hechos multimedia en tiempo real.
- Permitir la visualización abierta sin autenticación previa (acceso anónimo).
- Ofrecer mecanismos transparentes de curaduría y filtrado de datos (Modo Curado vs. Irrestricto).

### Técnicos
- Diseñar una arquitectura orientada a microservicios desacoplados y escalables.
- Garantizar la unidireccionalidad en las dependencias (Capa Controller → Service → Repository → Domain).
- Aplicar patrones de diseño GoF (*Strategy*, *Template Method*, *Factory Method*, *Adapter*).

### Académicos
- Cumplir con los requerimientos integradores de las entregas evaluativas de la cátedra de Diseño de Sistemas de Información (UTN FRBA, 2025).

---

## 🏗️ Arquitectura del Sistema

El sistema evoluciona de un diseño monolítico multimódulo hacia una **arquitectura de microservicios distribuida** comunicada vía API REST e inter-service HTTP clients (Spring WebFlux / WebClient).

### Diagrama General de Arquitectura (ASCII)

```text
                               ┌─────────────────────────┐
                               │   Navegador Web / Client│
                               └────────────┬────────────┘
                                            │ HTTP
                                            ▼
                               ┌─────────────────────────┐
                               │     frontend (Port 8080)│
                               │  Thymeleaf + Spring Web │
                               └────────────┬────────────┘
                                            │ HTTP / REST
                                            ▼
                               ┌─────────────────────────┐
                               │    servicioGateway      │
                               │  Spring WebFlux Gateway │
                               └────────────┬────────────┘
                                            │
        ┌───────────────────┬───────────────┼───────────────┬───────────────────┐
        │                   │               │               │                   │
        ▼                   ▼               ▼               ▼                   ▼
┌──────────────┐    ┌──────────────┐┌──────────────┐┌──────────────┐    ┌──────────────┐
│  servicio    │    │   fuente     ││   fuente     ││   fuente     │    │  servicio    │
│Autenticacion│    │  Dinamica    ││  Estatica    ││   Proxy      │    │ Estadisticas │
│ (JWT Auth)   │    │(PostgreSQL/  ││ (CSV Reader) ││ (External API│    │(Reportes CSV)│
└──────────────┘    │    MySQL)    │└──────────────┘│   Adapter)   │    └──────────────┘
                    └───────┬──────┘                └───────┬──────┘
                            │                               │
                            └───────────────┬───────────────┘
                                            │
                                            ▼
                               ┌─────────────────────────┐
                               │    servicioAgregador    │
                               │ (Colecciones, Consenso  │
                               │ & Normalizador LLM AI)  │
                               └────────────┬────────────┘
                                            │
                                            ▼
                               ┌─────────────────────────┐
                               │ HuggingFace Inference   │
                               │ (MiniLM-L6-v2 API)      │
                               └─────────────────────────┘
```

### Patrones de Diseño Aplicados

| Patrón | Aplicación en el Proyecto |
| :--- | :--- |
| **Strategy** | Definición dinámica de criterios de pertenencia de hechos y algoritmos de consenso (`TiposAlgoritmos`), así como selección del proveedor de API externa en `fuenteProxy`. |
| **Template Method** | Clase abstracta `Fuente` para estandarizar el algoritmo de procesamiento de hechos entre fuentes estáticas, dinámicas y proxy. |
| **Factory Method** | Instanciación dinámica de parsers de datasets (`DatasetCsv`, `DatasetDb`) según el tipo configurado. |
| **Adapter** | Normalización de DTOs heterogéneos provenientes de APIs externas (e.g. Disilab, MetaMapa) al modelo unificado `HechoDTOOutput`. |
| **Layered / MVC** | Separación estricta en capas con flujo unidireccional para evitar dependencias circulares. |

---

## 🛠️ Stack Tecnológico

| Capa / Dominio | Tecnología | Propósito |
| :--- | :--- | :--- |
| **Lenguaje Core** | Java 17 (LTS) | Lenguaje principal de desarrollo backend y microservicios. |
| **Framework Principal** | Spring Boot 3.2.0 | Framework base para controladores REST, inyección de dependencias y servicios. |
| **Frontend UI** | HTML5, CSS3, Thymeleaf | Renderizado dinámico del cliente liviano y maquetado de interfaz. |
| **Seguridad** | Spring Security + JJWT (0.11.5) | Autenticación basada en tokens JWT sin estado. |
| **Persistencia** | Spring Data JPA / Hibernate / JDBC | ORM y capa de acceso a datos relacionales. |
| **Base de Datos** | MySQL 8.0 / MariaDB | Motor de base de datos relacional. |
| **Comunicación Asíncrona** | Spring WebFlux (WebClient) | Llamadas HTTP no bloqueantes e inter-microservicio. |
| **Inteligencia Artificial** | HuggingFace Inference API | HuggingFace `sentence-transformers/all-MiniLM-L6-v2` para deduplicación semántica. |
| **Testing** | JUnit 5, Mockito 5.18, Reactor Test | Pruebas unitarias, de integración y mocks de servicios. |
| **Calidad de Código** | Checkstyle (Google Rules 9.0.1) | Validación de convenciones de código y linters. |
| **Containerización** | Docker & Docker Compose | Containerización y orquestación local de microservicios. |

---

## 📈 Evolución del Proyecto

El desarrollo del sistema se estructuró en cuatro entregas evolutivas:

```text
┌────────────────┐     ┌────────────────┐     ┌────────────────┐     ┌────────────────┐
│   ENTREGA 1    │ ──► │   ENTREGA 2    │ ──► │   ENTREGA 3    │ ──► │   ENTREGA 4    │
└────────────────┘     └────────────────┘     └────────────────┘     └────────────────┘
 Modelado de Dominio    Estructura Multimódulo  API REST & Gateway     UI/UX Thymeleaf
 OO Initial & Strategy   Patrón MVC & Capas     WebClient & Adapters   Deduplicación LLM
 Template Method        Aislamiento Módulos    CRUD de Hechos         DER & Dockerization
```

- **Entrega 1 (Modelado de Objetos I):** Definición del diagrama de clases inicial, patrones Strategy (criterios de pertenencia) y Template Method (fuentes).
- **Entrega 2 (Modelado de Objetos II):** Reestructuración a proyecto multimódulo en Maven (`pom.xml` padre), patrón MVC y desacoplamiento unidireccional de capas.
- **Entrega 3 (Modelado de Objetos III):** Implementación de controladores HTTP REST (`/api/estatica`, `/api/proxy`, `/api/dinamica`, `/api/colecciones`), comunicación inter-servicio vía WebClient y patrón Adapter para consumir APIs de terceros (Disilab, MetaMapa).
- **Entrega 4 (Interfaz & Maquetado):** Implementación del frontend completo con HTML/CSS y Thymeleaf, panel de administración, deduplicación mediante HuggingFace LLM, servicio de estadísticas con exportación a CSV, DER relacional y contenedorización Docker con script `setup.sh`.

---

## 📁 Estructura del Repositorio

```text
2025-tpa-ma-ma-grupo-05/
├── docs/                      # Documentación del proyecto por entregas
│   ├── ENTREGA1/              # Diagramas de clases, casos de uso y PDF
│   ├── ENTREGA2/              # Diagramas de arquitectura y PDF
│   ├── ENTREGA3/              # PlantUMLs, especificaciones de endpoints y PDF
│   ├── ENTREGA4/              # DERs, diseño UX/UI y PDF final
│   └── pendientes.md          # Control de tareas y pendientes del equipo
├── estadisticas/              # Microservicio de generación y exportación de métricas
├── frontend/                  # Cliente web (Thymeleaf, Spring MVC, Spring Security)
├── fuenteDinamica/            # Microservicio de hechos aportados por usuarios
├── fuenteEstatica/            # Microservicio de lectura de datasets masivos (CSV)
├── fuenteProxy/               # Microservicio proxy adaptador de APIs externas
├── servicioAgregador/         # Microservicio core (Colecciones, Consenso, Normalizador LLM)
├── servicioAutenticacion/     # Microservicio de autenticación y emisión JWT
├── servicioGateway/           # Spring Cloud / WebFlux API Gateway
├── pom.xml                    # POM Padre configurador del proyecto multimódulo
├── setup.sh                   # Script para generación de entornos Docker y variables (.env)
├── mvnw / mvnw.cmd            # Maven Wrapper
└── README.md                  # Documentación principal del repositorio
```

---

## 🚀 Instalación y Ejecución

### Requisitos Previos
- **Java Development Kit (JDK):** Versión 17 o superior.
- **Apache Maven:** Versión 3.8.1 o superior (o utilizar `./mvnw`).
- **Base de Datos:** MySQL Server 8.0+ en ejecución.
- **Docker / Docker Compose:** (Opcional) Para ejecución mediante contenedores.

### Paso 1: Clonar el Repositorio
```bash
git clone https://github.com/dds-utn/2025-tpa-ma-ma-grupo-05.git
cd 2025-tpa-ma-ma-grupo-05
```

### Paso 2: Compilación del Proyecto Multimódulo
Compilar todo el proyecto y verificar las dependencias desde el módulo raíz:
```bash
mvn clean install
```

### Paso 3: Configuración de Entornos mediante Script
El proyecto incluye un script shell automatizado para preparar las variables de entorno `.env` y los archivos `Dockerfile` de cada microservicio:

```bash
chmod +x setup.sh
./setup.sh
```

---

## ⚙️ Configuración

### Variables de Entorno (.env)
Cada microservicio requiere las siguientes credenciales para la conexión a la base de datos MySQL (generadas automáticamente por `setup.sh`):

```env
DISENO_DB_USERNAME=root
DISENO_DB_PASSWORD=tu_password_aqui
```

### Integración con HuggingFace LLM
El microservicio `servicioAgregador` utiliza la API de inferencia de HuggingFace. Asegurarse de contar con conectividad saliente HTTP a:
`https://api-inference.huggingface.co/models/sentence-transformers/all-MiniLM-L6-v2`

---

## 💻 API & Ejemplos de Uso

### 1. Fuente Estática (`/api/estatica`)
- **Obtener hechos de un dataset CSV:**
  ```http
  GET /api/estatica/CSV/desastres_naturales_argentina.csv
  ```
- **Respuesta JSON:**
  ```json
  [
    {
      "titulo": "Aluvión en Catamarca",
      "descripcion": "Un aluvión afectó el norte de Catamarca...",
      "fecha": "2023-01-15",
      "latitud": -28.4696,
      "longitud": -65.7852
    }
  ]
  ```

### 2. Fuente Dinámica (`/api/dinamica`)
- **Crear un nuevo hecho (Usuario Registrado):**
  ```http
  POST /api/dinamica/1
  Content-Type: application/json

  {
    "titulo": "Inundación en Buenos Aires",
    "descripcion": "Fuertes lluvias anegaron avenidas principales",
    "categoria": "Inundación",
    "fechaAcontecimiento": "2023-11-12",
    "lugarAcontecimiento": {
      "latitud": -34.6037,
      "longitud": -58.3816
    },
    "etiquetas": ["lluvia", "emergencia", "clima"]
  }
  ```

### 3. Servicio Agregador (`/api/colecciones`)
- **Crear una Colección con Múltiples Fuentes:**
  ```http
  POST /api/colecciones
  Content-Type: application/json

  {
    "titulo": "Colección de Desastres Naturales",
    "descripcion": "Hechos integrados de fuentes estáticas y proxies",
    "algoritmoConsenso": "MAYORIASIMPLE",
    "fuentes": [
      {
        "path": "http://localhost:8082/api/estatica/CSV",
        "pathInfo": "desastres_naturales_argentina.csv",
        "tipoFuente": "ESTATICA"
      }
    ]
  }
  ```

### 4. Servicio de Estadísticas (`/estadisticas`)
- **Exportar Estadísticas a CSV:**
  ```http
  GET /estadisticas/export/PROVINCIA_TOP_COLECCION
  ```
- **Respuesta (`text/csv`):**
  ```csv
  clave,valor,fecha
  Buenos Aires,120,2025-09-07T00:00:00
  Cordoba,85,2025-09-07T00:00:00
  ```

---

## 🧪 Testing y Calidad

Para ejecutar la suite completa de pruebas unitarias e integrales en todos los módulos:

```bash
mvn test
```

### Validación Exhaustiva de Código (Checkstyle & Cobertura)
Para ejecutar la validación formal requerida antes de cada entrega final:

```bash
mvn clean verify
```

Este comando ejecuta secuencialmente:
1. Ejecución de pruebas JUnit 5 y Mockito.
2. Validación de estándares de código mediante **Checkstyle** (Reglas Google `9.0.1`).
3. Detección automática de code smells y análisis estático.
4. Verificación de cobertura mínima de pruebas.

---

## 🐳 Despliegue con Docker

Cada microservicio cuenta con su propio `Dockerfile` basado en `eclipse-temurin:17`. Para construir y ejecutar la infraestructura containerizada:

```bash
# Construir las imágenes de todos los microservicios
docker build -t metamapa/frontend:latest ./frontend
docker build -t metamapa/agregador:latest ./servicioAgregador
docker build -t metamapa/dinamica:latest ./fuenteDinamica
docker build -t metamapa/estatica:latest ./fuenteEstatica
docker build -t metamapa/proxy:latest ./fuenteProxy
docker build -t metamapa/estadisticas:latest ./estadisticas
```

---

## 🧠 Módulo de Deduplicación y Normalización IA

El módulo `servicioAgregador` incorpora una solución para evitar hechos duplicados en la base de datos utilizando el modelo de lenguaje de **HuggingFace** (`sentence-transformers/all-MiniLM-L6-v2`).

```text
Entrada (Título Nuevo) ──► Normalización Texto ──► Embeddings API ──► Cosine Similarity (>=0.7) ──► Duplicado (Rechazar)
                                                                 └──► ( < 0.7 ) ───────────────► Único (Guardar)
```

```java
// Fragmento conceptual del algoritmo de verificación de similitud
public boolean compararSimilitud(List<Double> conjunto) {
    for (Double score : conjunto) {
        if (score >= 0.7) {
            return false; // Hecho duplicado detectado
        }
    }
    return true; // Hecho válido para persistencia
}
```

---

## 📑 Documentación Adicional

- 📄 [Consigna Oficial del Proyecto](https://docs.google.com/document/d/1ctxGwWrnM0XmPii38KWod9mTzphzNxxPRg9HkpXyNBg/edit?usp=sharing)
- 📊 [Casos de Uso en LucidChart](https://lucid.app/lucidchart/0f05727b-49f9-4f2f-8bbb-4c9f18b1c899/edit)
- 📘 [Justificaciones de Diseño PDF](https://docs.google.com/document/d/1Ggzs56TswRghTynTnS11iRFk_ax3MiMFnRU673B_o64/edit?usp=sharing)
- 🗂️ **PDFs de Entregas Oficiales:** Disponibles dentro del directorio [`/docs`](file:///c:/Users/pepe/Desktop/cursada-ing-sistemas-utn/tp-dsi-full-stack-app-2025/docs).


---

## 📄 Licencia

Este proyecto fue desarrollado con fines estrictamente académicos para la asignatura **Diseño de Sistemas de Información (2025)** en la Universidad Tecnológica Nacional, Facultad Regional Buenos Aires (UTN FRBA). Todos los derechos reservados.
