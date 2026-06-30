package com.perfulandia.ms_pagos.controller;

import com.perfulandia.ms_pagos.model.Factura;
import com.perfulandia.ms_pagos.model.EstadoFactura;
import com.perfulandia.ms_pagos.service.FacturaService;
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

// Pruebas del FacturaController llamando sus métodos directamente (Mockito puro, sin MockMvc).
@ExtendWith(MockitoExtension.class)
class FacturaControllerTest {

    @Mock
    private FacturaService facturaService;

    @InjectMocks
    private FacturaController facturaController;

    // POST generar factura -> 201
    @Test
    void generarFactura_devuelve201() {
        Factura factura = new Factura();
        factura.setIdFactura(1L);
        factura.setMontoTotal(new BigDecimal("20000"));
        factura.setEstado(EstadoFactura.GENERADA);
        when(facturaService.generarFactura(any(Factura.class))).thenReturn(factura);

        ResponseEntity<Factura> respuesta = facturaController.generarFactura(factura);

        assertEquals(HttpStatus.CREATED, respuesta.getStatusCode());
    }

    // GET listar -> devuelve la lista
    @Test
    void listarFacturas_devuelveLista() {
        when(facturaService.listarFacturas()).thenReturn(Arrays.asList(new Factura(), new Factura()));

        List<Factura> respuesta = facturaController.obtenerTodas();

        assertEquals(2, respuesta.size());
    }

    // GET por id existente -> 200
    @Test
    void obtenerPorId_existe_devuelve200() {
        Factura factura = new Factura();
        factura.setIdFactura(1L);
        when(facturaService.obtenerFacturaPorId(1L)).thenReturn(Optional.of(factura));

        ResponseEntity<Factura> respuesta = facturaController.obtenerPorId(1L);

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
    }

    // GET por id inexistente -> 404
    @Test
    void obtenerPorId_noExiste_devuelve404() {
        when(facturaService.obtenerFacturaPorId(99L)).thenReturn(Optional.empty());

        ResponseEntity<Factura> respuesta = facturaController.obtenerPorId(99L);

        assertEquals(HttpStatus.NOT_FOUND, respuesta.getStatusCode());
    }

    // PUT actualizar estado -> 200
    @Test
    void actualizarEstado_devuelve200() {
        Factura factura = new Factura();
        factura.setEstado(EstadoFactura.ENVIADA);
        when(facturaService.actualizarEstado(anyLong(), any(EstadoFactura.class))).thenReturn(factura);

        ResponseEntity<Factura> respuesta = facturaController.actualizarEstado(1L, EstadoFactura.ENVIADA);

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
    }

    // DELETE -> 204
    @Test
    void eliminar_devuelve204() {
        doNothing().when(facturaService).eliminarFactura(1L);

        ResponseEntity<Void> respuesta = facturaController.eliminar(1L);

        assertEquals(HttpStatus.NO_CONTENT, respuesta.getStatusCode());
    }
}