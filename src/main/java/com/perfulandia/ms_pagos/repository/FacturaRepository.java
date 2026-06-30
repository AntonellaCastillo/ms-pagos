package com.perfulandia.ms_pagos.repository;

import com.perfulandia.ms_pagos.model.Factura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

// CAPA REPOSITORY de Factura - hereda el CRUD de JpaRepository
@Repository
public interface FacturaRepository extends JpaRepository<Factura, Long> 
{

    // KAN-24: buscar la factura de un pago
    Optional<Factura> findByIdPago(Long idPago);
}