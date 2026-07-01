package com.perfulandia.ms_pagos.service;

import com.perfulandia.ms_pagos.model.Pago;
import com.perfulandia.ms_pagos.model.EstadoPago;
import com.perfulandia.ms_pagos.repository.PagoRepository;
import com.perfulandia.ms_pagos.exception.PagoRechazadoException;
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
class PagoServiceTest 
{

    @Mock
    private PagoRepository pagoRepository;

    @InjectMocks
    private PagoService pagoService;

    @Test
    void procesarPago_montoValido_confirmaPago() 
    {
        Pago pago = new Pago();
        pago.setMonto(new BigDecimal("15000"));
        pago.setMetodoPago("TARJETA");
        when(pagoRepository.save(any(Pago.class))).thenAnswer(i -> i.getArgument(0));

        Pago resultado = pagoService.procesarPago(pago);

        assertEquals(EstadoPago.CONFIRMADO, resultado.getEstado());
        verify(pagoRepository).save(pago);
    }

    @Test
    void procesarPago_montoInvalido_lanzaRechazado() 
    {
        Pago pago = new Pago();
        pago.setMonto(BigDecimal.ZERO);

        assertThrows(PagoRechazadoException.class, () -> pagoService.procesarPago(pago));
        verify(pagoRepository, never()).save(any());
    }

    @Test
    void listarPagos_devuelveLista() 
    {
        when(pagoRepository.findAll()).thenReturn(Arrays.asList(new Pago(), new Pago()));
        List<Pago> resultado = pagoService.listarPagos();
        assertEquals(2, resultado.size());
    }

    @Test
    void obtenerPagoPorId_existe_devuelvePago() 
    {
        Pago pago = new Pago();
        pago.setIdPago(1L);
        when(pagoRepository.findById(1L)).thenReturn(Optional.of(pago));

        Optional<Pago> resultado = pagoService.obtenerPagoPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getIdPago());
    }

    @Test
    void obtenerPagoPorId_noExiste_devuelveVacio() 
    {
        when(pagoRepository.findById(99L)).thenReturn(Optional.empty());
        Optional<Pago> resultado = pagoService.obtenerPagoPorId(99L);
        assertTrue(resultado.isEmpty());
    }

    @Test
    void obtenerPagoPorPedido_existe_devuelvePago() 
    {
        Pago pago = new Pago();
        pago.setIdPedido(1L);
        when(pagoRepository.findByIdPedido(1L)).thenReturn(Optional.of(pago));

        Optional<Pago> resultado = pagoService.obtenerPagoPorPedido(1L);

        assertTrue(resultado.isPresent());
    }

    @Test
    void actualizarEstado_existe_cambiaEstado() 
    {
        Pago pago = new Pago();
        pago.setIdPago(1L);
        pago.setEstado(EstadoPago.PENDIENTE);
        when(pagoRepository.findById(1L)).thenReturn(Optional.of(pago));
        when(pagoRepository.save(any(Pago.class))).thenAnswer(i -> i.getArgument(0));

        Pago resultado = pagoService.actualizarEstado(1L, EstadoPago.CONFIRMADO);

        assertEquals(EstadoPago.CONFIRMADO, resultado.getEstado());
    }

    @Test
    void actualizarEstado_noExiste_lanzaExcepcion() 
    {
        when(pagoRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RecursoNoEncontradoException.class,
                () -> pagoService.actualizarEstado(99L, EstadoPago.CONFIRMADO));
    }

    @Test
    void eliminarPago_llamaDeleteById() 
    {
        pagoService.eliminarPago(1L);
        verify(pagoRepository).deleteById(1L);
    }
}