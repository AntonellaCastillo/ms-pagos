package com.perfulandia.ms_pagos.repository;

import com.perfulandia.ms_pagos.model.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

// CAPA REPOSITORY de Pago - hereda el CRUD de JpaRepository
@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> 
{

    // Buscar el pago de un pedido (para saber si un pedido ya fue pagado)
    Optional<Pago> findByIdPedido(Long idPedido);
}