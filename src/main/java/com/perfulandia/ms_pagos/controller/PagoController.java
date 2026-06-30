package com.perfulandia.ms_pagos.controller;

import com.perfulandia.ms_pagos.model.Pago;
import com.perfulandia.ms_pagos.model.EstadoPago;
import com.perfulandia.ms_pagos.service.PagoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/pagos")
public class PagoController 
{

    @Autowired
    private PagoService pagoService;

    // POST: procesar pago (KAN-23) -> 201
    @PostMapping
    public ResponseEntity<Pago> procesarPago(@Valid @RequestBody Pago pago) 
    {
        Pago nuevo = pagoService.procesarPago(pago);
        return new ResponseEntity<>(nuevo, HttpStatus.CREATED); // 201
    }

    // GET: listar todos
    @GetMapping
    public List<Pago> obtenerTodos() 
    {
        return pagoService.listarPagos();
    }

    // GET por id
    @GetMapping("/{id}")
    public ResponseEntity<Pago> obtenerPorId(@PathVariable Long id) 
    {
        return pagoService.obtenerPagoPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // GET por pedido
    @GetMapping("/pedido/{idPedido}")
    public ResponseEntity<Pago> obtenerPorPedido(@PathVariable Long idPedido) 
    {
        return pagoService.obtenerPagoPorPedido(idPedido)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // PUT estado
    @PutMapping("/{id}/estado")
    public ResponseEntity<Pago> actualizarEstado(@PathVariable Long id,
                                                 @RequestParam EstadoPago nuevoEstado) 
    {
        return ResponseEntity.ok(pagoService.actualizarEstado(id, nuevoEstado));
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) 
    {
        pagoService.eliminarPago(id);
        return ResponseEntity.noContent().build();
    }
}