package com.perfulandia.ms_pagos.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

// Cliente de comunicación con MS Notificaciones (comunicación REST entre microservicios).
@Service
public class NotificacionClienteService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${ms.notificaciones.url:http://localhost:8090}")
    private String urlNotificaciones;

    // KAN-24: al emitir una factura, avisa a Notificaciones para enviarla por correo.
    // try/catch = RESILIENCIA: si Notificaciones está caído, Pagos NO se cae.
    public void notificarFacturaEmitida(Long idFactura, String correo) {
        try {
            String url = urlNotificaciones + "/api/v1/notificaciones/factura/" + idFactura;
            restTemplate.postForObject(url, correo, Void.class);
        } catch (Exception e) {
            System.out.println("No se pudo notificar la factura a MS Notificaciones: " + e.getMessage());
        }
    }
}