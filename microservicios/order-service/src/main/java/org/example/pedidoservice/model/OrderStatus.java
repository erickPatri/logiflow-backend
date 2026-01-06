package org.example.pedidoservice.model;

public enum OrderStatus {
    PENDIENTE, // cuando este creado pero esperando conductor
    ASIGNADO,  // cuando ya tiene vehículo
    EN_TRANSITO,
    ENTREGADO,
    CANCELADO
}
