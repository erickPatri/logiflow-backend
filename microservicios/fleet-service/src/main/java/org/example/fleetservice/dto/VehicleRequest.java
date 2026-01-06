package org.example.fleetservice.dto;

import lombok.Data;

@Data
public class VehicleRequest {
    private String plate; // los campos que comparten todos
    private String model;
    private String brand;
    private String type; // valores esperados son: "MOTORCYCLE", "CAR", "TRUCK"

    private Integer cylinderCapacity; // los campos de moto
    private boolean helmetIncluded;

    private Double trunkCapacity; // los campos de auto
    private boolean isElectric;

    private Double loadCapacityTons; // los campos de camion
    private Integer numberOfAxles;
}
