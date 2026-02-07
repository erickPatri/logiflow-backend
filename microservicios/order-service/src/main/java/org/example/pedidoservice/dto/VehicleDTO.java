package org.example.pedidoservice.dto;

import lombok.Data;

@Data
public class VehicleDTO {
    private Long id;
    private String plate;
    private String model;
    private String brand;
    private String vehicleType;
}
