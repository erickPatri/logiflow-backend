package org.example.graphqlservice.model;

public record VehicleDTO(
        Long id,
        String plate,
        String model,
        String brand,
        String vehicleType,
        DriverDTO driver
) {
}
