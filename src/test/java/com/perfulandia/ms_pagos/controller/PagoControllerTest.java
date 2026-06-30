package com.perfulandia.ms_pagos.controller;

import com.perfulandia.ms_pagos.model.Pago;
import com.perfulandia.ms_pagos.model.EstadoPago;
import com.perfulandia.ms_pagos.service.PagoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

// Pruebas del PagoController llamando sus métodos directamente (sin MockMvc).
// @Mock crea un service falso; @InjectMocks lo inyecta en el controller real.
@ExtendWith(MockitoExtension.class)
class PagoControllerTest {

    @Mock
    private PagoService pagoService;

    @InjectMocks
    private PagoController pagoController;

    // POST procesar pago -> 201
    @Test
    void procesarPago_devuelve201() {
        Pago pago = new Pago();
        pago.setIdPago(1L);
        pago.setMonto(new BigDecimal("15000"));
        pago.setEstado(EstadoPago.CONFIRMADO);
        when(pagoService.procesarPago(any(Pago.class))).thenReturn(pago);

        ResponseEntity<Pago> respuesta = pagoController.procesarPago(pago);

        assertEquals(HttpStatus.CREATED, respuesta.getStatusCode());
        assertEquals(EstadoPago.CONFIRMADO, respuesta.getBody().getEstado());
    }

    // GET listar -> devuelve la lista
    @Test
    void listarPagos_devuelveLista() {
        when(pagoService.listarPagos()).thenReturn(Arrays.asList(new Pago(), new Pago()));

        List<Pago> respuesta = pagoController.obtenerTodos();

        assertEquals(2, respuesta.size());
    }

    // GET por id existente -> 200
    @Test
    void obtenerPorId_existe_devuelve200() {
        Pago pago = new Pago();
        pago.setIdPago(1L);
        when(pagoService.obtenerPagoPorId(1L)).thenReturn(Optional.of(pago));

        ResponseEntity<Pago> respuesta = pagoController.obtenerPorId(1L);

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
    }

    // GET por id inexistente -> 404
    @Test
    void obtenerPorId_noExiste_devuelve404() {
        when(pagoService.obtenerPagoPorId(99L)).thenReturn(Optional.empty());

        ResponseEntity<Pago> respuesta = pagoController.obtenerPorId(99L);

        assertEquals(HttpStatus.NOT_FOUND, respuesta.getStatusCode());
    }

    // PUT actualizar estado -> 200
    @Test
    void actualizarEstado_devuelve200() {
        Pago pago = new Pago();
        pago.setEstado(EstadoPago.CONFIRMADO);
        when(pagoService.actualizarEstado(anyLong(), any(EstadoPago.class))).thenReturn(pago);

        ResponseEntity<Pago> respuesta = pagoController.actualizarEstado(1L, EstadoPago.CONFIRMADO);

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
    }

    // DELETE -> 204
    @Test
    void eliminar_devuelve204() {
        doNothing().when(pagoService).eliminarPago(1L);

        ResponseEntity<Void> respuesta = pagoController.eliminar(1L);

        assertEquals(HttpStatus.NO_CONTENT, respuesta.getStatusCode());
    }
}