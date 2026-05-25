package com.perfulandia.ms_pagos.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.perfulandia.ms_pagos.model.EstadoFactura;
import com.perfulandia.ms_pagos.model.Factura;

@Repository
public interface FacturaRepository extends JpaRepository<Factura, Long> {

    // Buscar factura por pago
    Optional<Factura> findByPagoIdPago(Long idPago);

    // Buscar facturas por estado
    List<Factura> findByEstado(EstadoFactura estado);
}
