package com.perfulandia.ms_pagos.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.perfulandia.ms_pagos.model.EstadoPago;
import com.perfulandia.ms_pagos.model.Pago;
import com.perfulandia.ms_pagos.model.TipoPago;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {

    // Buscar pago por pedido
    Optional<Pago> findByIdPedido(Long idPedido);

    // Buscar pagos por estado
    List<Pago> findByEstado(EstadoPago estado);

    // Buscar pagos por tipo
    List<Pago> findByTipo(TipoPago tipo);
}