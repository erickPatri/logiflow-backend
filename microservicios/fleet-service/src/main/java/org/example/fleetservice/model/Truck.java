package org.example.fleetservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@DiscriminatorValue("CAMION")
public class Truck extends Vehicle {
    @Column(name = "load_capacity_tons")
    private Double loadCapacityTons; // capacidad de carga en toneladas

    @Column(name = "number_of_axles")
    private Integer numberOfAxles; // numero de ejes
}
