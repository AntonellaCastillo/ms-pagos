package com.perfulandia.ms_pagos.service;

import com.perfulandia.ms_pagos.model.Factura;
import com.perfulandia.ms_pagos.model.EstadoFactura;
import com.perfulandia.ms_pagos.repository.FacturaRepository;
import com.perfulandia.ms_pagos.exception.RecursoNoEncontradoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class FacturaService 
{

    @Autowired
    private FacturaRepository facturaRepository;

    // KAN-24: generar una factura. Crea folio único y la deja GENERADA.
    public Factura generarFactura(Factura factura) 
    {
        // Genera un folio único si no viene
        if (factura.getFolio() == null || factura.getFolio().isBlank()) 
        {
            factura.setFolio("F-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        }
        factura.setFechaEmision(LocalDateTime.now());
        if (factura.getEstado() == null) {
            factura.setEstado(EstadoFactura.GENERADA);
        }
        return facturaRepository.save(factura);
    }

    // Listar todas las facturas
    public List<Factura> listarFacturas() 
    {
        return facturaRepository.findAll();
    }

    // Buscar una factura por id
    public Optional<Factura> obtenerFacturaPorId(Long id) 
    {
        return facturaRepository.findById(id);
    }

    // KAN-24: buscar la factura de un pago
    public Optional<Factura> obtenerFacturaPorPago(Long idPago) 
    {
        return facturaRepository.findByIdPago(idPago);
    }

    // Actualizar el estado de una factura (GENERADA -> ENVIADA -> ANULADA)
    public Factura actualizarEstado(Long id, EstadoFactura nuevoEstado) 
    {
        Factura factura = facturaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("La factura no existe"));
        factura.setEstado(nuevoEstado);
        return facturaRepository.save(factura);
    }

    // Eliminar una factura
    public void eliminarFactura(Long id) 
    {
        facturaRepository.deleteById(id);
    }
}