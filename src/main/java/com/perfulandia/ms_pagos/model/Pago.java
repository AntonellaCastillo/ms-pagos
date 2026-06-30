package com.perfulandia.ms_pagos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

// ENTIDAD PAGO (KAN-23): procesa el pago de un pedido web o una venta presencial.
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pago")
public class Pago 
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPago;

    // Id Externo: si el pago es web, viene el pedido (MS Envíos). Opcional.
    private Long idPedido;

    // Id Externo: si el pago es presencial, viene la venta (MS Ventas). Opcional.
    private Long idVenta;

    // Regla: exactamente UNO de los dos (idPedido o idVenta) va presente.

    @NotNull(message = "El monto es obligatorio")
    @DecimalMin(value = "0.0", message = "El monto no puede ser negativo")
    private BigDecimal monto;

    // Estado del pago (enum): PENDIENTE / CONFIRMADO / RECHAZADO
    @NotNull(message = "El estado es obligatorio")
    @Enumerated(EnumType.STRING)
    private EstadoPago estado;

    private LocalDateTime fecha;

    @NotBlank(message = "El método de pago es obligatorio")
    private String metodoPago;
}
