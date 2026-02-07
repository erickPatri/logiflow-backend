package org.example.pedidoservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String description;

    @Column(name = "client_id", nullable = false)  // relacion con auth-service para ver quien pidio el envio
    private Long clientId;

    @Column(name = "pickup_location", nullable = false) // donde se recoge el pedido
    private String pickupLocation;

    @Column(name = "delivery_location", nullable = false) // donde se entrega el pedido
    private String deliveryLocation;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "assigned_vehicle_id") // relacion futura con fleet-fervice para saber que vehiculo lo lleva
    private Long assignedVehicleId;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @PrePersist // esto se ejecuta automáticamente antes de guardar por primera vez en la BD
    public void prePersist() {
        this.createdAt = LocalDateTime.now(); // para guardar fecha y hora automaticamente a la hora de crear un pedido
        if (this.status == null) {
            this.status = OrderStatus.PENDIENTE; // si me olvido agregar el estado y para evitar errores de nulos, lo asigno por defecto como pendiente
        }
    }
}
