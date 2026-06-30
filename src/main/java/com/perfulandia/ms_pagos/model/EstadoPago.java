package com.perfulandia.ms_pagos.model;

// Estados de un pago (KAN-23). Si falla el procesamiento -> RECHAZADO (402).
public enum EstadoPago 
{
    PENDIENTE,
    CONFIRMADO,
    RECHAZADO
}