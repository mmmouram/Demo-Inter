package com.myapp.repositories;

import com.myapp.models.PedidoBloqueio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PedidoBloqueioRepository extends JpaRepository<PedidoBloqueio, Long> {
    List<PedidoBloqueio> findByPedidoId(Long pedidoId);
}
