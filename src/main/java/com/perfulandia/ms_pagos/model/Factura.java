package com.perfulandia.ms_pagos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "factura")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Factura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idFactura;

    @ManyToOne
    @JoinColumn(name = "id_pago", nullable = false)
    private Pago pago;

    @NotBlank(message = "El folio es obligatorio")
    private String folio;

    @NotNull(message = "La fecha de emision es obligatoria")
    private LocalDateTime fechaEmision;

    @NotNull(message = "El monto total es obligatorio")
    @DecimalMin(value = "0.0", message = "El monto total no puede ser negativo")
    private BigDecimal montoTotal;

    @NotNull(message = "El estado es obligatorio")
    @Enumerated(EnumType.STRING)
    private EstadoFactura estado;
}