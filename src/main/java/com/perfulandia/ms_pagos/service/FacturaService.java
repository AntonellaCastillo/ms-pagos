package com.perfulandia.ms_pagos.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.perfulandia.ms_pagos.model.EstadoFactura;
import com.perfulandia.ms_pagos.model.EstadoPago;
import com.perfulandia.ms_pagos.model.Factura;
import com.perfulandia.ms_pagos.model.Pago;
import com.perfulandia.ms_pagos.repository.FacturaRepository;
import com.perfulandia.ms_pagos.repository.PagoRepository;

@Service
public class FacturaService {

    private static final Logger log = LoggerFactory.getLogger(FacturaService.class);

    @Autowired
    private FacturaRepository facturaRepository;

    @Autowired
    private PagoRepository pagoRepository;

    @Autowired
    private RestTemplate restTemplate;

    private static final String MS_NOTIFICACIONES_URL = "http://localhost:8089/api/v1/notificaciones";

    // Listar todas las facturas
    public List<Factura> findAll() {
        log.info("Listando todas las facturas");
        return facturaRepository.findAll();
    }

    // Buscar factura por id
    public Optional<Factura> findById(Long id) {
        log.info("Buscando factura con id: {}", id);
        return facturaRepository.findById(id);
    }

    // Buscar factura por pago
    public Optional<Factura> findByPagoId(Long idPago) {
        log.info("Buscando factura del pago: {}", idPago);
        return facturaRepository.findByPagoIdPago(idPago);
    }

    // Buscar facturas por estado
    public List<Factura> findByEstado(EstadoFactura estado) {
        log.info("Buscando facturas con estado: {}", estado);
        return facturaRepository.findByEstado(estado);
    }

    // Generar factura — Regla de negocio: solo si el pago está APROBADO
    public Factura generarFactura(Long idPago, String folio) {
        log.info("Generando factura para pago: {}", idPago);

        Pago pago = pagoRepository.findById(idPago)
            .orElseThrow(() -> new RuntimeException("Pago no encontrado con id: " + idPago));

        if (!pago.getEstado().equals(EstadoPago.APROBADO)) {
            log.warn("No se puede generar factura para pago con estado: {}", pago.getEstado());
            throw new RuntimeException("Solo se puede generar factura para pagos APROBADOS");
        }

        Factura factura = new Factura();
        factura.setPago(pago);
        factura.setFolio(folio);
        factura.setFechaEmision(java.time.LocalDateTime.now());
        factura.setMontoTotal(pago.getMonto());
        factura.setEstado(EstadoFactura.GENERADA);

        Factura guardada = facturaRepository.save(factura);
        log.info("Factura generada con id: {}", guardada.getIdFactura());

        // Notificar a MS Notificaciones
        try {
            restTemplate.postForObject(MS_NOTIFICACIONES_URL, guardada, String.class);
            log.info("MS Notificaciones notificado correctamente");
        } catch (Exception e) {
            log.warn("MS Notificaciones no disponible: {}", e.getMessage());
        }

        return guardada;
    }

    // Anular factura
    public Optional<Factura> anularFactura(Long id) {
        log.info("Anulando factura con id: {}", id);
        return facturaRepository.findById(id).map(factura -> {
            if (factura.getEstado().equals(EstadoFactura.ANULADA)) {
                log.warn("La factura {} ya está anulada", id);
                throw new RuntimeException("La factura ya está anulada");
            }
            factura.setEstado(EstadoFactura.ANULADA);
            log.info("Factura {} anulada correctamente", id);
            return facturaRepository.save(factura);
        });
    }
}
