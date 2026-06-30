package com.perfulandia.ms_pagos.exception;

// EXCEPCIÓN PROPIA: error claro para cuando algo no existe (pago, factura).
// El handler la convierte en un 404.
public class RecursoNoEncontradoException extends RuntimeException 
{
    public RecursoNoEncontradoException(String mensaje) 
    {
        super(mensaje);
    }
}