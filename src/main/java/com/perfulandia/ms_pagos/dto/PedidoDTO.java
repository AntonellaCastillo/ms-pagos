package com.perfulandia.ms_pagos.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoDTO {
    private Long idPedido;
    private Long idCliente;
    private Long idSucursal;
    private BigDecimal total;
    private String estado;
    private String direccionEnvio;
}