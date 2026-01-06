package org.example.fleetservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true) // para que incluya los campos del padre
@Entity
@DiscriminatorValue("MOTORCYCLE") // para que asi guarde en la BD
public class Motorcycle extends Vehicle {
    @Column(name = "cylinder_capacity")
    private Integer cylinderCapacity;

    @Column(name = "helmet_included")
    private boolean helmetIncluded; // si incluye casco
}
