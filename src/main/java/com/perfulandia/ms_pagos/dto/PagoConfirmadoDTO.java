package com.perfulandia.ms_pagos.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

// DTO - Data Transfer Object
// QUÉ: el "sobre" con solo los datos que MS Envíos necesita cuando un pago se confirma.
// CÓMO: clase simple, SIN @Entity (no se guarda en BD). Solo campos + Lombok.
// PARA QUÉ: avisar a MS Envíos (evento pago.confirmado) para que marque el pedido como PAGADO,
//           sin exponer mi entidad Pago completa.
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PagoConfirmadoDTO 
{

    // El pedido que se pagó (Envíos lo usa para ubicar su pedido)
    private Long idPedido;

    // El nuevo estado a informar (ej: "PAGADO")
    private String estado;
}