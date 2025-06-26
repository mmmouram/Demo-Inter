package com.myapp.repositories;

import com.myapp.models.Observacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ObservacaoRepository extends JpaRepository<Observacao, Long> {
    List<Observacao> findByPedidoId(Long pedidoId);
}
