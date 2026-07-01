package com.perfulandia.ms_pagos.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import static org.mockito.Mockito.*;

// Prueba del EnvioClienteService (comunicación REST resiliente con MS Envíos).
@ExtendWith(MockitoExtension.class)
class EnvioClienteServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private EnvioClienteService envioClienteService;

    // Comunicación normal: llama al RestTemplate
    @Test
    void notificarPagoConfirmado_llamaRestTemplate() {
        envioClienteService.notificarPagoConfirmado(1L);
        verify(restTemplate).put(anyString(), any());
    }

    // RESILIENCIA: si Envíos falla, el catch atrapa el error y NO se cae
    @Test
    void notificarPagoConfirmado_siFallaNoLanzaExcepcion() {
        doThrow(new RuntimeException("Envios caido")).when(restTemplate).put(anyString(), any());

        envioClienteService.notificarPagoConfirmado(1L);

        verify(restTemplate).put(anyString(), any());
    }
}