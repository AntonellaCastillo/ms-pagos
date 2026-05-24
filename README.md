# MS Pagos y Facturación - Perfulandia

Microservicio encargado de gestionar los pagos y la facturación en el ecosistema Perfulandia.

## Información del microservicio
- **Puerto:** 8086
- **Base de datos:** db_perfulandia_pagos
- **Tecnología:** Spring Boot 4.0.6, Java 25, MySQL

## Funcionalidades
- Procesar pago
- Verificar estado del pago
- Rechazar pago
- Generar factura
- Anular factura
- Listar pagos y facturas

## Endpoints
| Método | URL | Descripción |
|--------|-----|-------------|
| POST | /api/v1/pagos | Procesar pago |
| GET | /api/v1/pagos | Listar todos los pagos |
| GET | /api/v1/pagos/{id} | Buscar pago por ID |
| GET | /api/v1/pagos/pedido/{idPedido} | Verificar pago por pedido |
| PUT | /api/v1/pagos/{id}/rechazar | Rechazar pago |
| POST | /api/v1/facturas | Generar factura |
| GET | /api/v1/facturas | Listar todas las facturas |
| GET | /api/v1/facturas/{id} | Buscar factura por ID |
| PUT | /api/v1/facturas/{id}/anular | Anular factura |

## Conexión con otros microservicios
- **MS Pedidos (8091):** Verifica que el pedido existe antes de procesar el pago

## Cómo ejecutar
1. Tener XAMPP corriendo con MySQL
2. Crear la base de datos `db_perfulandia_pagos`
3. Ejecutar el proyecto con `./mvnw spring-boot:run`