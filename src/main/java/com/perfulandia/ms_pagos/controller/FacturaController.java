package com.perfulandia.ms_pagos.controller;

import com.perfulandia.ms_pagos.model.Factura;
import com.perfulandia.ms_pagos.model.EstadoFactura;
import com.perfulandia.ms_pagos.service.FacturaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/facturas")
public class FacturaController 
{

    @Autowired
    private FacturaService facturaService;

    // POST: generar factura (KAN-24) -> 201
    @PostMapping
    public ResponseEntity<Factura> generarFactura(@Valid @RequestBody Factura factura) 
    {
        Factura nueva = facturaService.generarFactura(factura);
        return new ResponseEntity<>(nueva, HttpStatus.CREATED); // 201
    }

    // GET: listar todas
    @GetMapping
    public List<Factura> obtenerTodas() 
    {
        return facturaService.listarFacturas();
    }

    // GET por id
    @GetMapping("/{id}")
    public ResponseEntity<Factura> obtenerPorId(@PathVariable Long id) 
    {
        return facturaService.obtenerFacturaPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // GET por pago (KAN-24)
    @GetMapping("/pago/{idPago}")
    public ResponseEntity<Factura> obtenerPorPago(@PathVariable Long idPago) 
    {
        return facturaService.obtenerFacturaPorPago(idPago)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // PUT estado
    @PutMapping("/{id}/estado")
    public ResponseEntity<Factura> actualizarEstado(@PathVariable Long id,
                                                    @RequestParam EstadoFactura nuevoEstado) 
    {
        return ResponseEntity.ok(facturaService.actualizarEstado(id, nuevoEstado));
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) 
    {
        facturaService.eliminarFactura(id);
        return ResponseEntity.noContent().build();
    }
}