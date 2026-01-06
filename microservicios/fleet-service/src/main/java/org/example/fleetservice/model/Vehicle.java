package org.example.fleetservice.model;

import jakarta.persistence.*;
import lombok.Data;
import org.example.fleetservice.interfaces.IRoutable;

@Data
@Entity
@Table(name = "vehicles")
// para esta herencia, todo se guarda en una sola tabla, pero JPA hara uso de una columna extra ("vehicle_type") para saber que tipo de vehiculo es.
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "vehicle_type", discriminatorType = DiscriminatorType.STRING)
public abstract class Vehicle implements IRoutable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String plate; // placa

    @Column(name = "model", nullable = false)
    private String model;

    @Column(name = "brand", nullable = false)
    private String brand;

    // la relacion es que un vehiculo tiene un conductor o asignado o null si esta libre
    @OneToOne
    @JoinColumn(name = "driver_id", referencedColumnName = "id")
    private Driver driver;

    @Override
    public void calculateOptimalRoute(String origin, String destination) {  // la implementacion por defecto de la interfaz
        // la logica base, se podria dejar vacio para que los hijos lo sobreescriban tambien
        System.out.println("Calculando ruta genérica de " + origin + " a " + destination);
    }
}
