package com.perfulandia.ms_pagos.service;

import com.perfulandia.ms_pagos.model.Factura;
import com.perfulandia.ms_pagos.model.EstadoFactura;
import com.perfulandia.ms_pagos.repository.FacturaRepository;
import com.perfulandia.ms_pagos.exception.RecursoNoEncontradoException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FacturaServiceTest {

    @Mock
    private FacturaRepository facturaRepository;

    @InjectMocks
    private FacturaService facturaService;

    @Test
    void generarFactura_creaConFolioYEstado() 
    {
        Factura factura = new Factura();
        factura.setIdPago(1L);
        factura.setMontoTotal(new BigDecimal("20000"));
        when(facturaRepository.save(any(Factura.class))).thenAnswer(i -> i.getArgument(0));

        Factura resultado = facturaService.generarFactura(factura);

        assertNotNull(resultado.getFolio());
        assertTrue(resultado.getFolio().startsWith("F-"));
        assertEquals(EstadoFactura.GENERADA, resultado.getEstado());
        assertNotNull(resultado.getFechaEmision());
    }

    // Cubre la rama donde el folio YA viene (el if es false)
    @Test
    void generarFactura_conFolioExistente_noGeneraNuevo() 
    {
        Factura factura = new Factura();
        factura.setFolio("F-MANUAL");
        factura.setEstado(EstadoFactura.ENVIADA);
        when(facturaRepository.save(any(Factura.class))).thenAnswer(i -> i.getArgument(0));

        Factura resultado = facturaService.generarFactura(factura);

        assertEquals("F-MANUAL", resultado.getFolio());
        assertEquals(EstadoFactura.ENVIADA, resultado.getEstado());
    }

    @Test
    void listarFacturas_devuelveLista() 
    {
        when(facturaRepository.findAll()).thenReturn(Arrays.asList(new Factura(), new Factura()));
        List<Factura> resultado = facturaService.listarFacturas();
        assertEquals(2, resultado.size());
    }

    @Test
    void obtenerFacturaPorId_existe_devuelveFactura() 
    {
        Factura factura = new Factura();
        factura.setIdFactura(1L);
        when(facturaRepository.findById(1L)).thenReturn(Optional.of(factura));

        Optional<Factura> resultado = facturaService.obtenerFacturaPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getIdFactura());
    }

    @Test
    void obtenerFacturaPorId_noExiste_devuelveVacio() 
    {
        when(facturaRepository.findById(99L)).thenReturn(Optional.empty());
        Optional<Factura> resultado = facturaService.obtenerFacturaPorId(99L);
        assertTrue(resultado.isEmpty());
    }

    @Test
    void obtenerFacturaPorPago_existe_devuelveFactura() 
    {
        Factura factura = new Factura();
        factura.setIdPago(1L);
        when(facturaRepository.findByIdPago(1L)).thenReturn(Optional.of(factura));

        Optional<Factura> resultado = facturaService.obtenerFacturaPorPago(1L);

        assertTrue(resultado.isPresent());
    }

    @Test
    void actualizarEstado_existe_cambiaEstado() 
    {
        Factura factura = new Factura();
        factura.setIdFactura(1L);
        factura.setEstado(EstadoFactura.GENERADA);
        when(facturaRepository.findById(1L)).thenReturn(Optional.of(factura));
        when(facturaRepository.save(any(Factura.class))).thenAnswer(i -> i.getArgument(0));

        Factura resultado = facturaService.actualizarEstado(1L, EstadoFactura.ENVIADA);

        assertEquals(EstadoFactura.ENVIADA, resultado.getEstado());
    }

    @Test
    void actualizarEstado_noExiste_lanzaExcepcion() 
    {
        when(facturaRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RecursoNoEncontradoException.class,
                () -> facturaService.actualizarEstado(99L, EstadoFactura.ENVIADA));
    }

    @Test
    void eliminarFactura_llamaDeleteById() 
    {
        facturaService.eliminarFactura(1L);
        verify(facturaRepository).deleteById(1L);
    }
}