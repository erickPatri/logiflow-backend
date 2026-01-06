package org.example.pedidoservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.pedidoservice.dto.OrderRequest;
import org.example.pedidoservice.model.Order;
import org.example.pedidoservice.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    // http://localhost:8083/orders
    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody OrderRequest request) {
        return ResponseEntity.ok(orderService.createOrder(request));
    }

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    // http://localhost:8083/orders/client/5 para ver los pedidos de un cliente en especifico
    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<Order>> getOrdersByClient(@PathVariable Long clientId) {
        return ResponseEntity.ok(orderService.getOrdersByClient(clientId));
    }

    // http://localhost:8083/orders/1/assign/5 significa que al pedido 1 le voy a asignar el vehiculo 5 por ejemplo
    @PutMapping("/{orderId}/assign/{vehicleId}")
    public ResponseEntity<Order> assignVehicle(@PathVariable Long orderId, @PathVariable Long vehicleId) {
        return ResponseEntity.ok(orderService.assignVehicleToOrder(orderId, vehicleId));
    }

    // http://localhost:8083/orders/1
    @DeleteMapping("/{orderId}")
    public ResponseEntity<String> cancelOrder(@PathVariable Long orderId) {
        orderService.cancelOrder(orderId);
        return ResponseEntity.ok("Pedido cancelado exitosamente");
    }

}
