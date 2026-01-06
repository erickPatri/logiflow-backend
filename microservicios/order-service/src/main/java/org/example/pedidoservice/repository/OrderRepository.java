package org.example.pedidoservice.repository;

import org.example.pedidoservice.model.Order;
import org.example.pedidoservice.model.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByClientId(Long clientId); // para buscar todos los pedidos de un cliente en especifico

    List<Order> findByStatus(OrderStatus status); // para buscar pedidos pendientes y mejorar la eficiencia del negocio
}
