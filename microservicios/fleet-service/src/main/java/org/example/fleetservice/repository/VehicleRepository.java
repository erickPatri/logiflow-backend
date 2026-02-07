package org.example.fleetservice.repository;

import org.example.fleetservice.model.DriverStatus;
import org.example.fleetservice.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    Optional<Vehicle> findByPlate(String plate); // porque es unica la placa
    boolean existsByPlate(String plate);
    List<Vehicle> findByDriver_Status(DriverStatus status);
    Optional<Vehicle> findByDriver_Id(Long driverId); // para obtener el vehiculo asignado a un conductor
}


