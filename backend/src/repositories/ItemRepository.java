package com.myapp.repositories;

import com.myapp.models.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByPedidoId(Long pedidoId);
}
