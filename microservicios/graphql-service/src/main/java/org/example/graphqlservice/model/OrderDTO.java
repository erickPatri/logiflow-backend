package org.example.graphqlservice.model;

import java.time.LocalDateTime;

public record OrderDTO(
        Long id,
        String description,
        Long clientId,
        String pickupLocation,
        String deliveryLocation,
        String status,
        LocalDateTime createdAt,
        Long assignedVehicleId,
        Double latitude,
        Double longitude
) {}
