package com.perfulandia.ms_pagos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

// ENTIDAD FACTURA (KAN-24): documento tributario generado a partir de un pago.
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "factura")
public class Factura 
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idFactura;

    // FK real: la factura se genera de un pago (vive en MI MS).
    @NotNull(message = "El pago es obligatorio")
    private Long idPago;

    @NotBlank(message = "El folio es obligatorio")
    private String folio;

    private LocalDateTime fechaEmision;

    @NotNull(message = "El monto total es obligatorio")
    @DecimalMin(value = "0.0", message = "El monto no puede ser negativo")
    private BigDecimal montoTotal;

    // Estado de la factura (enum)
    @NotNull(message = "El estado es obligatorio")
    @Enumerated(EnumType.STRING)
    private EstadoFactura estado;
}