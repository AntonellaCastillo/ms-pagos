package com.perfulandia.ms_pagos.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.perfulandia.ms_pagos.model.EstadoPago;
import com.perfulandia.ms_pagos.model.Pago;
import com.perfulandia.ms_pagos.model.TipoPago;
import com.perfulandia.ms_pagos.service.PagoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/pagos")
public class PagoController {

    private static final Logger log = LoggerFactory.getLogger(PagoController.class);

    @Autowired
    private PagoService pagoService;

    // GET — Listar todos los pagos
    @GetMapping
    public ResponseEntity<?> getPagos() {
        log.info("GET /api/v1/pagos - Listar todos los pagos");
        List<Pago> lista = pagoService.findAll();
        if (lista.isEmpty()) {
            log.warn("No hay pagos registrados");
            return ResponseEntity.status(404).body("No hay pagos registrados");
        }
        return ResponseEntity.ok(lista);
    }

    // GET — Buscar pago por id
    @GetMapping("/{id}")
    public ResponseEntity<?> getPagoById(@PathVariable Long id) {
        log.info("GET /api/v1/pagos/{} - Buscar pago por id", id);
        return pagoService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> {
                    log.warn("Pago con id {} no encontrado", id);
                    return ResponseEntity.status(404).build();
                });
    }

    // GET — Verificar pago por pedido
    @GetMapping("/pedido/{idPedido}")
    public ResponseEntity<?> getPagoByPedido(@PathVariable Long idPedido) {
        log.info("GET /api/v1/pagos/pedido/{} - Verificar pago por pedido", idPedido);
        return pagoService.findByIdPedido(idPedido)
                .map(ResponseEntity::ok)
                .orElseGet(() -> {
                    log.warn("No hay pago para el pedido {}", idPedido);
                    return ResponseEntity.status(404).build();
                });
    }

    // GET — Buscar pagos por estado
    @GetMapping("/estado/{estado}")
    public ResponseEntity<?> getPagosByEstado(@PathVariable EstadoPago estado) {
        log.info("GET /api/v1/pagos/estado/{} - Buscar pagos por estado", estado);
        List<Pago> lista = pagoService.findByEstado(estado);
        if (lista.isEmpty()) {
            log.warn("No hay pagos con estado {}", estado);
            return ResponseEntity.status(404).body("No hay pagos con ese estado");
        }
        return ResponseEntity.ok(lista);
    }

    // GET — Buscar pagos por tipo
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<?> getPagosByTipo(@PathVariable TipoPago tipo) {
        log.info("GET /api/v1/pagos/tipo/{} - Buscar pagos por tipo", tipo);
        List<Pago> lista = pagoService.findByTipo(tipo);
        if (lista.isEmpty()) {
            log.warn("No hay pagos con tipo {}", tipo);
            return ResponseEntity.status(404).body("No hay pagos con ese tipo");
        }
        return ResponseEntity.ok(lista);
    }

    // POST — Procesar pago
    @PostMapping
    public ResponseEntity<?> procesarPago(@Valid @RequestBody Pago pago) {
        log.info("POST /api/v1/pagos - Procesar nuevo pago");
        Pago nuevo = pagoService.procesarPago(pago);
        log.info("Pago procesado con id: {}", nuevo.getIdPago());
        return ResponseEntity.status(201).body(nuevo);
    }

    // PUT — Rechazar pago
    @PutMapping("/{id}/rechazar")
    public ResponseEntity<?> rechazarPago(@PathVariable Long id) {
        log.info("PUT /api/v1/pagos/{}/rechazar - Rechazar pago", id);
        try {
            return pagoService.rechazarPago(id)
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> {
                        log.warn("Pago con id {} no encontrado", id);
                        return ResponseEntity.status(404).build();
                    });
        } catch (RuntimeException e) {
            log.error("Error al rechazar pago {}: {}", id, e.getMessage());
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }
}