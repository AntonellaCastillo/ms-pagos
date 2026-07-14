package com.perfulandia.ms_pagos.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

// Cliente de comunicación con MS Notificaciones (comunicación REST entre microservicios).
@Service
public class NotificacionClienteService {

    private static final Logger log = LoggerFactory.getLogger(NotificacionClienteService.class);

    @Autowired
    private RestTemplate restTemplate;

    @Value("${ms.notificaciones.url:http://localhost:8089}")
    private String urlNotificaciones;

    // KAN-24: al emitir una factura, avisa a Notificaciones para enviarla por correo.
    // try/catch = RESILIENCIA: si Notificaciones está caído, Pagos NO se cae.
    public void notificarFacturaEmitida(Long idFactura, String correo) {
        try {
            String url = urlNotificaciones + "/api/notificaciones/enviar";
            restTemplate.postForObject(url, correo, Void.class);
            log.info("Notificacion de factura emitida enviada para la factura {}", idFactura);
        } catch (Exception e) {
            log.warn("No se pudo notificar la factura {} a MS Notificaciones: {}", idFactura, e.getMessage());
        }
    }
}