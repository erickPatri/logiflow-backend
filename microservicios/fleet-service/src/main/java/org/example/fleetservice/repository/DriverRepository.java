package org.example.fleetservice.repository;

import org.example.fleetservice.model.Driver;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DriverRepository extends JpaRepository<Driver, Long> {
    Optional<Driver> findDriverByLicenseNumber(String licenseNumber);
    Optional<Driver> findByUserId(Long userId); // para saber que conductor corresponde al usuario logueado
    Optional<Driver> existsByLicenseNumber(String licenseNumber); // si existe una licencia para validaciones
}
