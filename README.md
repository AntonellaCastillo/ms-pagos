# MS Pagos y Facturación — Perfulandia SPA

Microservicio de **Pagos y Facturación** del sistema Perfulandia SPA.
Procesa los pagos de pedidos web y ventas presenciales, y genera las facturas
(documentos tributarios) asociadas a cada pago.

## Stack
- Java 25
- Spring Boot 4.0.7
- Maven
- MySQL (XAMPP)
- Spring Data JPA · Bean Validation · Lombok · Actuator

## Configuración
- **Puerto:** 8086
- **Base de datos:** `db_perfulandia_pagos` (se crea sola con `createDatabaseIfNotExist=true`)
- **Tablas:** se generan automáticamente con `ddl-auto=update`

## Entidades
| Entidad | Descripción |
|---------|-------------|
| Pago | El pago de un pedido web (idPedido) o venta presencial (idVenta) |
| Factura | Documento tributario generado a partir de un pago, con folio único |

## Historias de Usuario cubiertas
- **KAN-23** Procesar pago (confirma o rechaza según el monto)
- **KAN-24** Generar factura (DTE) a partir de un pago

## Endpoints principales
Base URL: `http://localhost:8086`

### Pagos (`/api/v1/pagos`)
- `POST` procesar pago → 201
- `GET` listar todos
- `GET /{id}` por id
- `GET /pedido/{idPedido}` buscar pago de un pedido
- `PUT /{id}/estado?nuevoEstado=CONFIRMADO` actualizar estado
- `DELETE /{id}` eliminar

### Facturas (`/api/v1/facturas`)
- `POST` generar factura → 201
- `GET` listar todas
- `GET /{id}` por id
- `GET /pago/{idPago}` buscar factura de un pago
- `PUT /{id}/estado?nuevoEstado=ENVIADA` actualizar estado
- `DELETE /{id}` eliminar

## Comunicación con otros microservicios
- **DTO `PagoConfirmadoDTO`**: cuando un pago se confirma, se notifica a MS Envíos
  (evento `pago.confirmado`) para que marque el pedido como PAGADO. Lleva solo
  `idPedido` y `estado` (no expone la entidad Pago completa).

## Cómo ejecutar
1. Iniciar MySQL en XAMPP.
2. En la raíz del proyecto: `./mvnw spring-boot:run`
3. La app levanta en el puerto 8086 y crea la BD y las tablas automáticamente.

## Manejo de errores
GlobalExceptionHandler centralizado: 404 (no encontrado), 409 (operación no permitida /
integridad), 400 (validación / JSON inválido), 500 (error general).

## Autora
Antonella Castillo — Duoc UC · DSY1103 Desarrollo Fullstack I