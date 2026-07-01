package com.perfulandia.ms_pagos.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

// Prueba del NotificacionClienteService (comunicación REST resiliente con MS Notificaciones).
@ExtendWith(MockitoExtension.class)
class NotificacionClienteServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private NotificacionClienteService notificacionClienteService;

    // KAN-24: comunicación normal -> llama al RestTemplate para avisar la factura
    @Test
    void notificarFacturaEmitida_llamaRestTemplate() {
        notificacionClienteService.notificarFacturaEmitida(1L, "cliente@correo.com");
        verify(restTemplate).postForObject(anyString(), any(), eq(Void.class));
    }

    // RESILIENCIA: si Notificaciones falla, el catch atrapa el error y Pagos NO se cae
    @Test
    void notificarFacturaEmitida_siFallaNoLanzaExcepcion() {
        doThrow(new RuntimeException("Notificaciones caido"))
                .when(restTemplate).postForObject(anyString(), any(), eq(Void.class));

        // No debe lanzar excepción (el try/catch la atrapa)
        notificacionClienteService.notificarFacturaEmitida(1L, "cliente@correo.com");

        verify(restTemplate).postForObject(anyString(), any(), eq(Void.class));
    }
}