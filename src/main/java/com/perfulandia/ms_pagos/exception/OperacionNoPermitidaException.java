package com.perfulandia.ms_pagos.exception;

// EXCEPCIÓN PROPIA: error para cuando una operación no se permite por una regla de negocio.
// El handler la convierte en 409.
public class OperacionNoPermitidaException extends RuntimeException 
{
    public OperacionNoPermitidaException(String mensaje) 
    {
        super(mensaje);
    }
}