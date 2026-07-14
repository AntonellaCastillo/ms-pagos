package com.perfulandia.ms_pagos.service;

import com.perfulandia.ms_pagos.dto.PagoConfirmadoDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

// Cliente de comunicación con MS Envíos (comunicación REST entre microservicios).
@Service
public class EnvioClienteService {

    private static final Logger log = LoggerFactory.getLogger(EnvioClienteService.class);

    @Autowired
    private RestTemplate restTemplate;

    // La URL de MS Envíos se lee del application.properties (configurable por entorno)
    @Value("${ms.envios.url:http://localhost:8091}")
    private String urlEnvios;

    // Avisa a MS Envíos que un pago se confirmó, para que marque el pedido como PAGADO.
    // CÓMO: arma el DTO y lo manda por PUT. Va en try/catch para ser RESILIENTE:
    // si MS Envíos está caído, Pagos NO se cae, solo registra el fallo y sigue.
    public void notificarPagoConfirmado(Long idPedido) {
        try {
            PagoConfirmadoDTO dto = new PagoConfirmadoDTO(idPedido, "PAGADO");
            String url = urlEnvios + "/api/v1/pedidos/" + idPedido + "/estado?nuevoEstado=PAGADO";
            restTemplate.put(url, dto);
            log.info("Notificacion de pago confirmado enviada a MS Envios para el pedido {}", idPedido);
        } catch (Exception e) {
            // Resiliencia: si Envíos no responde, el pago igual queda registrado.
            log.warn("No se pudo notificar a MS Envios para el pedido {}: {}", idPedido, e.getMessage());
        }
    }
}