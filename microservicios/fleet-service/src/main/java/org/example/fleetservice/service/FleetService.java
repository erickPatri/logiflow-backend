package org.example.fleetservice.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.fleetservice.dto.DriverRequest;
import org.example.fleetservice.dto.VehicleRequest;
import org.example.fleetservice.model.*;
import org.example.fleetservice.repository.DriverRepository;
import org.example.fleetservice.repository.VehicleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Transactional // para asegurar que se guarden los cambios, si algo falla de hace rollback de forma automatica
@Service
@RequiredArgsConstructor
public class FleetService {
    private final VehicleRepository vehicleRepository;
    private final DriverRepository driverRepository;

    // para los conductores
    public Driver createDriver(DriverRequest request) {
        DriverStatus estadoInicialConductor = DriverStatus.DISPONIBLE; // valor por defecto

        if (request.getStatus() != null && !request.getStatus().isEmpty()) {
            try {
                // se convierte el texto String al Enum
                estadoInicialConductor = DriverStatus.valueOf(request.getStatus().toUpperCase());
            } catch (IllegalArgumentException e) {
                // si se manda un estado inventado, se lanza un error
                throw new IllegalArgumentException("Estado no válido. Use: DISPONIBLE, EN_RUTA, NO_DISPONIBLE");
            }
        }

        Driver driver = Driver.builder()
                .userId(request.getUserId())
                .licenseNumber(request.getLicenseNumber())
                .phoneNumber(request.getPhoneNumber())
                .status(estadoInicialConductor)
                .build();
        return driverRepository.save(driver);
    }

    public List<Driver> getAllDrivers() {
        return driverRepository.findAll();
    }

    // para los vehiculos, aqui se usa factory porque se decide que tipo de vehiculo se instancia segun el caso
    public Vehicle createVehicle(VehicleRequest request) {
        Vehicle vehicle;

        switch (request.getType().toUpperCase()) {
            case "MOTORCYCLE" -> {
                Motorcycle moto = new Motorcycle();
                moto.setCylinderCapacity(request.getCylinderCapacity());
                moto.setHelmetIncluded(request.isHelmetIncluded());
                vehicle = moto;
            }
            case "CAR" -> {
                Car car = new Car();
                car.setTrunkCapacity(request.getTrunkCapacity());
                car.setElectric(request.isElectric());
                vehicle = car;
            }
            case "TRUCK" -> {
                Truck truck = new Truck();
                truck.setLoadCapacityTons(request.getLoadCapacityTons());
                truck.setNumberOfAxles(request.getNumberOfAxles());
                vehicle = truck;
            }
            default -> throw new IllegalArgumentException("EL tipo de vehículo no es válido: " + request.getType());
        }

        // se llenan los datos comunes del padre
        vehicle.setPlate(request.getPlate());
        vehicle.setModel(request.getModel());
        vehicle.setBrand(request.getBrand());

        // se guarda y gracias a Hibernate se sabe en cual tabla y con que discriminador guardar
        return vehicleRepository.save(vehicle);
    }

    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }

    // buscar un vehiculo por su ID
    public Vehicle getVehicleById(Long id) {
        return vehicleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vehículo no encontrado con ID: " + id));
    }

    public Vehicle assignDriverToVehicle(Long vehicleId, Long driverId) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new RuntimeException("Vehículo no encontrado"));

        Driver driver = driverRepository.findById(driverId) // buscar vehiculo y conductor
                .orElseThrow(() -> new RuntimeException("Conductor no encontrado"));

        if(driver.getStatus() != DriverStatus.DISPONIBLE) { // validar si el conductor no esta disponible
            throw new RuntimeException("El conductor de momento no se encuentra disponible");
        }

        vehicle.setDriver(driver); // se asigna el conductor al vehiculo
        driver.setStatus(DriverStatus.EN_RUTA); // cambiar el estado del conductor
        driverRepository.save(driver);

        return vehicleRepository.save(vehicle); // guardar cambios del vehiculo
    }



}
