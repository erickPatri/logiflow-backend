package org.example.fleetservice.repository;

import org.example.fleetservice.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    Optional<Vehicle> findByPlate(String plate); // porque es unica la placa
    boolean existsByPlate(String plate);
}


