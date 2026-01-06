package org.example.pedidoservice.service;

import lombok.RequiredArgsConstructor;
import org.example.pedidoservice.dto.OrderRequest;
import org.example.pedidoservice.model.Order;
import org.example.pedidoservice.model.OrderStatus;
import org.example.pedidoservice.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;

    public Order createOrder(OrderRequest request) {
        Order order = Order.builder()
                .clientId(request.getClientId())
                .description(request.getDescription())
                .pickupLocation(request.getPickupLocation())
                .deliveryLocation(request.getDeliveryLocation())
                .build();

        // no asigno el status ni createdAt, ya que lo hice en el @PrePersist

        return orderRepository.save(order);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public List<Order> getOrdersByClient(Long clientId) { // para ver los pedidos de un cliente y asi pueda ver su historial
        return orderRepository.findByClientId(clientId);
    }

    public Order assignVehicleToOrder(Long orderId, Long vehicleId) {
        Order order = orderRepository.findById(orderId) // primero se busca el pedido
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        order.setAssignedVehicleId(vehicleId); // se asigna el vehiculos
        order.setStatus(OrderStatus.ASIGNADO); // se cambia el estado para que ya no este pendiente

        return orderRepository.save(order);
    }

    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));


        if (order.getStatus() == OrderStatus.ENTREGADO) { // para validar que no se pueda cancelar si ya se entrego
            throw new RuntimeException("No se puede cancelar un pedido ya entregado");
        }

        order.setStatus(OrderStatus.CANCELADO);
        orderRepository.save(order);
    }

    // En un sistema avanzado el OrderService deberia llamar al FleetService para saber el vehiculo existe y esta libre, por ahora, se envia el ID
    // correcto a proposito para que sea simple
}
