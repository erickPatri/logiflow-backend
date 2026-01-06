package org.example.fleetservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@DiscriminatorValue("AUTO")
public class Car extends Vehicle {
    @Column(name = "trunk_capacity")
    private Double trunkCapacity; // capacidad del maletero

    @Column(name = "is_electric")
    private boolean isElectric;
}
