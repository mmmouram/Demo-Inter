package com.myapp.repositories;

import com.myapp.models.NotaFiscal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotaFiscalRepository extends JpaRepository<NotaFiscal, Long> {
    List<NotaFiscal> findByPedidoId(Long pedidoId);
}
