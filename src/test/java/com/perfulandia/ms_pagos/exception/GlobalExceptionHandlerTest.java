package com.perfulandia.ms_pagos.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void manejarNoEncontrado_devuelve404() {
        RecursoNoEncontradoException ex = new RecursoNoEncontradoException("No existe");
        ResponseEntity<Map<String, String>> r = handler.manejarNoEncontrado(ex);
        assertEquals(HttpStatus.NOT_FOUND, r.getStatusCode());
    }

    @Test
    void manejarOperacionNoPermitida_devuelve409() {
        OperacionNoPermitidaException ex = new OperacionNoPermitidaException("No permitido");
        ResponseEntity<Map<String, String>> r = handler.manejarOperacionNoPermitida(ex);
        assertEquals(HttpStatus.CONFLICT, r.getStatusCode());
    }

    @Test
    void manejarPagoRechazado_devuelve402() {
        PagoRechazadoException ex = new PagoRechazadoException("Rechazado");
        ResponseEntity<Map<String, String>> r = handler.manejarPagoRechazado(ex);
        assertEquals(HttpStatus.PAYMENT_REQUIRED, r.getStatusCode());
    }

    @Test
    void manejarGeneral_devuelve500() {
        RuntimeException ex = new RuntimeException("Error");
        ResponseEntity<Map<String, String>> r = handler.manejarGeneral(ex);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, r.getStatusCode());
    }
}