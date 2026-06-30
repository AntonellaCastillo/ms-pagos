package com.perfulandia.ms_pagos.service;

import com.perfulandia.ms_pagos.model.Pago;
import com.perfulandia.ms_pagos.model.EstadoPago;
import com.perfulandia.ms_pagos.repository.PagoRepository;
import com.perfulandia.ms_pagos.exception.RecursoNoEncontradoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PagoService 
{

    @Autowired
    private PagoRepository pagoRepository;

    // KAN-23: procesar un pago. Lo registra y lo deja CONFIRMADO o RECHAZADO.
    public Pago procesarPago(Pago pago) 
    {
        pago.setFecha(LocalDateTime.now());
        // Regla simple de procesamiento: si el monto es válido (>0), se confirma; si no, se rechaza.
        if (pago.getMonto() != null && pago.getMonto().signum() > 0) 
        {
            pago.setEstado(EstadoPago.CONFIRMADO);
        } else {
            pago.setEstado(EstadoPago.RECHAZADO);
        }
        return pagoRepository.save(pago);
    }

    // Listar todos los pagos
    public List<Pago> listarPagos() 
    {
        return pagoRepository.findAll();
    }

    // Buscar un pago por id
    public Optional<Pago> obtenerPagoPorId(Long id) 
    {
        return pagoRepository.findById(id);
    }

    // Buscar el pago de un pedido
    public Optional<Pago> obtenerPagoPorPedido(Long idPedido) 
    {
        return pagoRepository.findByIdPedido(idPedido);
    }

    // Actualizar el estado de un pago
    public Pago actualizarEstado(Long id, EstadoPago nuevoEstado) 
    {
        Pago pago = pagoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("El pago no existe"));
        pago.setEstado(nuevoEstado);
        return pagoRepository.save(pago);
    }

    // Eliminar un pago
    public void eliminarPago(Long id) 
    {
        pagoRepository.deleteById(id);
    }
}