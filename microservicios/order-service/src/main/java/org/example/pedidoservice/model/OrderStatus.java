package org.example.pedidoservice.model;

public enum OrderStatus {
    PENDIENTE, // cuando este creado pero esperando conductor
    ASIGNADO,  // cuando ya tiene vehículo
    ENTREGADO,
    CANCELADO
}
