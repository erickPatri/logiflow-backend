package org.example.pedidoservice.service;

import lombok.RequiredArgsConstructor;
import org.example.pedidoservice.config.RabbitMQConfig;
import org.example.pedidoservice.dto.OrderRequest;
import org.example.pedidoservice.dto.VehicleDTO;
import org.example.pedidoservice.model.Order;
import org.example.pedidoservice.model.OrderStatus;
import org.example.pedidoservice.repository.OrderRepository;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final AmqpTemplate amqpTemplate;
    private final RestTemplate restTemplate;

    public Order createOrder(Order order) {
        order.setCreatedAt(LocalDateTime.now());

        // LÓGICA DE ASIGNACIÓN
        try {
            RestTemplate restTemplate = new RestTemplate();
            String fleetUrl = "http://localhost:8082/fleet/available";

            // se llama al microservicio de flota
            VehicleDTO assignedVehicle = restTemplate.getForObject(fleetUrl, VehicleDTO.class);

            if (assignedVehicle != null) {
                //  CONDUCTOR LIBRE
                order.setAssignedVehicleId(assignedVehicle.getId());
                order.setStatus(OrderStatus.ASIGNADO);
                System.out.println("Pedido asignado al vehículo: " + assignedVehicle.getBrand() + " " + assignedVehicle.getPlate());
            } else {
                // TODOS ESTÁN OCUPADOS
                order.setStatus(OrderStatus.PENDIENTE);
                order.setAssignedVehicleId(null);
                System.out.println("Flota ocupada. El pedido quedará PENDIENTE.");
            }

        } catch (Exception e) {
            // ERROR DE CONEXIÓN (Fleet Service caído)
            System.err.println("Error conectando con Fleet Service: " + e.getMessage());
            order.setStatus(OrderStatus.PENDIENTE);
            order.setAssignedVehicleId(null);
        }
        // ---------------------------------

        // Guardamos y notificamos (RabbitMQ)
        Order savedOrder = orderRepository.save(order);

        try {
            amqpTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY, savedOrder);
        } catch (Exception e) {
            System.err.println("Error enviando a RabbitMQ: " + e.getMessage());
        }

        return savedOrder;
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


    public Order updateOrderStatus(Long orderId, String newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        OrderStatus status;
        // Convertimos el texto (ej: "EN_RUTA") al Enum real
        try {
            status = OrderStatus.valueOf(newStatus);
            order.setStatus(status);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Estado inválido: " + newStatus);
        }

        if (status == OrderStatus.ENTREGADO && order.getAssignedVehicleId() != null) {
            try {
                RestTemplate localRestTemplate = new RestTemplate();
                // Endpoint que creamos en fleet-service
                String fleetUrl = "http://localhost:8082/fleet/vehicles/" + order.getAssignedVehicleId() + "/release";

                // Hacemos una petición PUT vacía
                localRestTemplate.put(fleetUrl, null);

                System.out.println("Solicitud de liberación enviada para el vehículo ID: " + order.getAssignedVehicleId());
            } catch (Exception e) {
                // Si falla, no rompemos el pedido, solo avisamos en consola
                System.err.println("Error al intentar liberar vehículo (Fleet Service offline?): " + e.getMessage());
            }
        }

        Order updatedOrder = orderRepository.save(order);

        try {
            amqpTemplate.convertAndSend(
                    RabbitMQConfig.EXCHANGE_NAME,
                    RabbitMQConfig.ROUTING_KEY,
                    updatedOrder
            );
            System.out.println("Notificación de actualización enviada a RabbitMQ: " + updatedOrder.getId());
        } catch (Exception e) {
            System.err.println("Error notificando actualización: " + e.getMessage());
        }

        return updatedOrder;
    }

}
