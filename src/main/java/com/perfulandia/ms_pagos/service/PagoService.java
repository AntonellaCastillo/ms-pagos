package com.perfulandia.ms_pagos.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.perfulandia.ms_pagos.dto.PedidoDTO;
import com.perfulandia.ms_pagos.model.EstadoPago;
import com.perfulandia.ms_pagos.model.Pago;
import com.perfulandia.ms_pagos.model.TipoPago;
import com.perfulandia.ms_pagos.repository.PagoRepository;

@Service
public class PagoService {

    private static final Logger log = LoggerFactory.getLogger(PagoService.class);

    @Autowired
    private PagoRepository pagoRepository;

    @Autowired
    private RestTemplate restTemplate;

    private static final String MS_PEDIDOS_URL = "http://localhost:8091/api/v1/pedidos";
    private static final String MS_NOTIFICACIONES_URL = "http://localhost:8089/api/v1/notificaciones";

    public List<Pago> findAll() {
        log.info("Listando todos los pagos");
        return pagoRepository.findAll();
    }

    public Optional<Pago> findById(Long id) {
        log.info("Buscando pago con id: {}", id);
        return pagoRepository.findById(id);
    }

    public Optional<Pago> findByIdPedido(Long idPedido) {
        log.info("Verificando pago del pedido: {}", idPedido);
        return pagoRepository.findByIdPedido(idPedido);
    }

    public List<Pago> findByEstado(EstadoPago estado) {
        log.info("Buscando pagos con estado: {}", estado);
        return pagoRepository.findByEstado(estado);
    }

    public List<Pago> findByTipo(TipoPago tipo) {
        log.info("Buscando pagos con tipo: {}", tipo);
        return pagoRepository.findByTipo(tipo);
    }

    // Procesar pago — igual que el profesor con ClienteDTO
    public Pago procesarPago(Pago pago) {
        log.info("Procesando pago para pedido: {}", pago.getIdPedido());

        // Verificar que el pedido existe en MS Pedidos
        String url = MS_PEDIDOS_URL + "/" + pago.getIdPedido();
        PedidoDTO pedido = restTemplate.getForObject(url, PedidoDTO.class);

        if (pedido != null) {
            pago.setEstado(EstadoPago.APROBADO);
            log.info("Pedido {} verificado. Cliente: {}", pedido.getIdPedido(), pedido.getIdCliente());
        } else {
            log.warn("Pedido {} no encontrado. Procesando en contingencia", pago.getIdPedido());
            pago.setEstado(EstadoPago.PENDIENTE);
        }

        Pago guardado = pagoRepository.save(pago);

        // Notificar a MS Notificaciones
        try {
            restTemplate.postForObject(MS_NOTIFICACIONES_URL, guardado, String.class);
            log.info("MS Notificaciones notificado correctamente");
        } catch (Exception e) {
            log.warn("MS Notificaciones no disponible: {}", e.getMessage());
        }

        log.info("Pago procesado con id: {}", guardado.getIdPago());
        return guardado;
    }

    // Rechazar pago
    public Optional<Pago> rechazarPago(Long id) {
        log.info("Rechazando pago con id: {}", id);
        return pagoRepository.findById(id).map(pago -> {
            if (pago.getEstado().equals(EstadoPago.APROBADO)) {
                log.warn("No se puede rechazar un pago ya aprobado");
                throw new RuntimeException("No se puede rechazar un pago que ya fue aprobado");
            }
            pago.setEstado(EstadoPago.RECHAZADO);
            log.info("Pago {} rechazado correctamente", id);
            return pagoRepository.save(pago);
        });
    }
}