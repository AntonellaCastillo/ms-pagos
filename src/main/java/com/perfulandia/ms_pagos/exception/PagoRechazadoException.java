package com.perfulandia.ms_pagos.exception;

// EXCEPCIÓN PROPIA (KAN-23): cuando un pago no se puede procesar.
// El handler la convierte en un 402 Payment Required.
public class PagoRechazadoException extends RuntimeException 
{
    public PagoRechazadoException(String mensaje) 
    {
        super(mensaje);
    }
}