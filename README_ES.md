# 🧉 LazyLedger: Gestión Colaborativa de Libros Contables Financieros

Un proyecto backend diseñado para explorar soluciones de gestión financiera colaborativa, construido sobre principios sólidos de diseño de dominio y arquitectura limpia.

## 1. El Problema: La Fricción del Registro Manual

Mi experiencia con aplicaciones de gestión de gastos ha sido consistentemente efímera. He identificado una barrera fundamental que siempre me lleva a abandonarlas: la fricción del proceso de entrada manual de datos.

El ciclo es predecible: el acto de abrir la aplicación y completar un formulario para cada transacción rápidamente se vuelve tedioso, llevando a posponer registros y finalmente al abandono.

## 2. Hipótesis y Objetivo

**Hipótesis:** Si registrar un gasto fuera tan fácil como enviar un audio, la tasa de éxito aumentaría dramáticamente.

**Objetivo del Proyecto:** Construir un backend robusto que pueda servir como núcleo para una aplicación de registro de transacciones basada en voz, con arquitectura modular que soporte libros contables y gestión de usuarios.

## 3. Resumen de Arquitectura

LazyLedger sigue principios de Arquitectura Limpia con diseño modular:

- **Módulo Ledger**: Gestión de libros contables multiusuario con grupos, membresías y manejo de transacciones
- **Módulo User**: Registro de usuarios y operaciones de dominio de usuario
- **Commons**: Objetos de dominio compartidos, identificadores y enums

## 4. Módulo Ledger: Gestión Financiera Colaborativa

El módulo Ledger proporciona capacidades integrales de gestión financiera, permitiendo a los usuarios crear y gestionar libros contables con múltiples participantes y manejar transacciones dentro de esos libros.

### Modelo de Dominio:
- **Entidad Ledger**: Representa un libro contable financiero con gestión de estado (ACTIVE, INACTIVE, ARCHIVED)
- **Entidad LedgerGroup**: Agrupa múltiples libros contables bajo un solo propietario
- **Entidad LedgerMembership**: Gestiona roles de usuario dentro de libros contables (OWNER, MEMBER)
- **Entidad Transaction**: Transacciones financieras vinculadas a libros contables específicos
- **Objetos de Valor**: LedgerName, LedgerId, LedgerGroupId, UserId, TransactionId, Amount, Description, TransactionDate
- **Enums**: LedgerStatus, LedgerUserRole, Category

### Arquitectura:
- **Patrón Repository**: Abstracción limpia sobre persistencia de datos con Spring Data JPA
- **Patrón Use Case**: Orquestación de lógica de negocio a través de casos de uso dedicados
- **Service Facade**: Interfaces de API simplificadas que ocultan complejidad del framework
- **Domain-Driven Design**: Modelos de dominio ricos con validación y reglas de negocio

### Características:
- **Gestión del Ciclo de Vida del Ledger**: Crear, desactivar, archivar y desarchivar libros contables
- **Organización en Grupos**: Organizar libros contables en grupos jerárquicos
- **Control de Acceso Basado en Roles**: Diferentes niveles de permisos para participantes del libro
- **Gestión de Membresías**: Invitación de usuarios y asignación de roles
- **Gestión de Transacciones**: Operaciones CRUD completas para transacciones del libro
- **Clasificación por Categorías**: Categorías predefinidas de gastos e ingresos
- **Consultas por Rango de Fechas**: Filtrado de transacciones por rangos de fechas

### Endpoints de API:

#### Gestión de Ledger:
- `POST /ledgers/create` - Crear nuevo libro contable con asignación opcional de grupo
- `GET /ledgers/{id}` - Recuperar detalles del libro contable
- `PUT /ledgers/{id}/status` - Actualizar estado del libro contable
- `DELETE /ledgers/{id}` - Archivar libro contable

#### Gestión de Transacciones:
- `POST /ledgers/{ledgerId}/transactions` - Agregar transacción al libro contable
- `GET /ledgers/{ledgerId}/transactions/{transactionId}` - Obtener transacción específica
- `GET /ledgers/{ledgerId}/transactions` - Listar todas las transacciones en el libro
- `GET /ledgers/{ledgerId}/transactions/range?startDate=...&endDate=...` - Filtrar por rango de fechas
- `DELETE /ledgers/{ledgerId}/transactions/{transactionId}` - Eliminar transacción

#### Gestión de Grupos:
- `POST /groups/create` - Crear grupo de libros contables
- `GET /groups/{id}` - Obtener detalles del grupo
- `GET /groups/user/{userId}` - Listar grupos del usuario

#### Gestión de Membresías:
- `POST /memberships/create` - Agregar usuario al libro contable
- `GET /memberships/ledger/{ledgerId}` - Listar miembros del libro
- `PUT /memberships/{id}/role` - Actualizar rol del miembro
- `DELETE /memberships/{id}` - Eliminar miembro

## 5. Módulo User: Dominio de Registro de Usuarios

El módulo User maneja el registro de usuarios y operaciones básicas del dominio de usuario.

### Modelo de Dominio:
- **Entidad User**: Representa usuarios registrados con estado inmutable
- **Objetos de Valor**: UserName, Email (con validación)
- **Interfaz Repository**: UserRepository para persistencia de usuarios

### Arquitectura:
- **UserRegistrationService**: Servicio de dominio para registro de usuarios con validación de unicidad de email
- **Métodos Factory**: Creación inmutable de usuarios a través de métodos factory estáticos

### Características:
- **Validación de Email**: Validación apropiada de formato de email
- **Validación de Nombre**: Restricciones de longitud y formato del nombre de usuario
- **Lógica de Registro**: Previene registros duplicados de email

## 6. Stack Tecnológico Seleccionado

La implementación utiliza un stack de grado empresarial, enfocado en robustez y mejores prácticas.

- **Lenguaje:** Java 21 (OpenJDK).
- **Framework:** Spring Boot 3.2.0 (con Spring Web, Spring Data JPA).
- **Base de Datos:** PostgreSQL con codificación UTF-8.
- **ORM:** Hibernate (vía Spring Data JPA).
- **Herramienta de Build:** Maven.
- **Testing:** JUnit 5 con Spring Boot Test.
- **Principios de Diseño:** Modelo de Dominio Rico, Arquitectura Limpia, Patrón Repository, Patrón Use Case, Inyección de Dependencias, Inmutabilidad.

## 7. Instrucciones de Configuración

### Prerrequisitos
- Java 21 (OpenJDK)
- Maven 3.6+
- PostgreSQL 12+

### Configuración
1. Copiar el archivo de configuración de ejemplo:
   ```bash
   cp src/main/resources/application.properties.example src/main/resources/application.properties
   ```

2. Actualizar el archivo `application.properties` con tus valores reales:
   - Credenciales de base de datos (detalles de conexión PostgreSQL)

### Configuración de Base de Datos
1. Crear una base de datos PostgreSQL llamada `lazyledger_db`
2. Actualizar los detalles de conexión en `application.properties`

### Ejecutar la Aplicación
```bash
mvn spring-boot:run
```

La aplicación iniciará en el puerto 8090.

## 8. Estado del Proyecto y Próximos Pasos

### Estado Actual:
✅ **Módulo Ledger**: Modelo de dominio completo, repositorios, casos de uso, capa API y gestión de transacciones
✅ **Módulo User**: Modelo de dominio para registro de usuarios con validación
✅ **Commons**: Identificadores compartidos, enums y objetos de dominio
✅ **Arquitectura Limpia**: Separación apropiada de responsabilidades con capas de dominio, aplicación, infraestructura y API
✅ **Integración de Base de Datos**: PostgreSQL con JPA/Hibernate
✅ **API REST**: Endpoints RESTful completos para gestión de libros contables y transacciones

### Aspectos Destacados de Arquitectura:
- **Diseño Modular**: Separación limpia entre preocupaciones de ledger, usuario y comunes
- **Domain-Driven Design**: Modelos de dominio ricos con objetos de valor, entidades y servicios de dominio
- **Patrón Repository**: Abstracción sobre persistencia de datos con Spring Data JPA
- **Patrón Use Case**: Orquestación de lógica de negocio a través de casos de uso dedicados
- **Service Facade**: Interfaces de API simplificadas que ocultan complejidad del framework
- **Entidades Inmutables**: Objetos de dominio con validación apropiada y métodos factory

### Próximos Pasos:
🔜 **Integración de Voz**: Agregar capacidades de transcripción de voz a texto (Google Gemini LLM)
🔜 **Bot de Telegram**: Procesamiento de mensajes en tiempo real para transacciones de voz
🔜 **Módulo de Seguridad**: Autenticación JWT y seguridad de API con Spring Security
🔜 **Características Avanzadas**: Planificación de presupuestos, funcionalidad de exportación, transacciones recurrentes
🔜 **Infraestructura**: Containerización Docker, pipeline CI/CD, monitoreo y migraciones de base de datos