package com.perfulandia.ms_pagos.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.perfulandia.ms_pagos.model.EstadoFactura;
import com.perfulandia.ms_pagos.model.Factura;
import com.perfulandia.ms_pagos.service.FacturaService;

@RestController
@RequestMapping("/api/v1/facturas")
public class FacturaController {

    private static final Logger log = LoggerFactory.getLogger(FacturaController.class);

    @Autowired
    private FacturaService facturaService;

    // GET — Listar todas las facturas
    @GetMapping
    public ResponseEntity<?> getFacturas() {
        log.info("GET /api/v1/facturas - Listar todas las facturas");
        List<Factura> lista = facturaService.findAll();
        if (lista.isEmpty()) {
            log.warn("No hay facturas registradas");
            return ResponseEntity.status(404).body("No hay facturas registradas");
        }
        return ResponseEntity.ok(lista);
    }

    // GET — Buscar factura por id
    @GetMapping("/{id}")
    public ResponseEntity<?> getFacturaById(@PathVariable Long id) {
        log.info("GET /api/v1/facturas/{} - Buscar factura por id", id);
        return facturaService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> {
                    log.warn("Factura con id {} no encontrada", id);
                    return ResponseEntity.status(404).build();
                });
    }

    // GET — Buscar factura por pago
    @GetMapping("/pago/{idPago}")
    public ResponseEntity<?> getFacturaByPago(@PathVariable Long idPago) {
        log.info("GET /api/v1/facturas/pago/{} - Buscar factura por pago", idPago);
        return facturaService.findByPagoId(idPago)
                .map(ResponseEntity::ok)
                .orElseGet(() -> {
                    log.warn("No hay factura para el pago {}", idPago);
                    return ResponseEntity.status(404).build();
                });
    }

    // GET — Buscar facturas por estado
    @GetMapping("/estado/{estado}")
    public ResponseEntity<?> getFacturasByEstado(@PathVariable EstadoFactura estado) {
        log.info("GET /api/v1/facturas/estado/{} - Buscar facturas por estado", estado);
        List<Factura> lista = facturaService.findByEstado(estado);
        if (lista.isEmpty()) {
            log.warn("No hay facturas con estado {}", estado);
            return ResponseEntity.status(404).body("No hay facturas con ese estado");
        }
        return ResponseEntity.ok(lista);
    }

    // POST — Generar factura
    @PostMapping("/generar/{idPago}")
    public ResponseEntity<?> generarFactura(@PathVariable Long idPago,
            @RequestParam String folio) {
        log.info("POST /api/v1/facturas/generar/{} - Generar factura", idPago);
        try {
            Factura nueva = facturaService.generarFactura(idPago, folio);
            log.info("Factura generada con id: {}", nueva.getIdFactura());
            return ResponseEntity.status(201).body(nueva);
        } catch (RuntimeException e) {
            log.error("Error al generar factura: {}", e.getMessage());
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    // PUT — Anular factura
    @PutMapping("/{id}/anular")
    public ResponseEntity<?> anularFactura(@PathVariable Long id) {
        log.info("PUT /api/v1/facturas/{}/anular - Anular factura", id);
        try {
            return facturaService.anularFactura(id)
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> {
                        log.warn("Factura con id {} no encontrada", id);
                        return ResponseEntity.status(404).build();
                    });
        } catch (RuntimeException e) {
            log.error("Error al anular factura {}: {}", id, e.getMessage());
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }
}