package org.example.fleetservice.config;

import org.example.fleetservice.model.Car;
import org.example.fleetservice.model.Driver;
import org.example.fleetservice.model.DriverStatus;
import org.example.fleetservice.model.Motorcycle;
import org.example.fleetservice.repository.DriverRepository;
import org.example.fleetservice.repository.VehicleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataLoader {
    @Bean
    CommandLineRunner initDatabase(VehicleRepository vehicleRepository, DriverRepository driverRepository) {
        return args -> {
            if (vehicleRepository.count() == 0) {
                System.out.println("Base de datos vacía. Creando datos iniciales...");

                // 1. Crear Conductor por defecto si es que no hay nada
                Driver driver1 = Driver.builder()
                        .userId(101L)
                        .licenseNumber("LIC-DEFAULT-001")
                        .phoneNumber("0991111111")
                        .status(DriverStatus.DISPONIBLE)
                        .build();
                driverRepository.save(driver1);

                // 2. Crear Auto y asignarle el conductor
                Car car = new Car();
                car.setBrand("Chevrolet");
                car.setModel("Sail");
                car.setPlate("PBA-1000");
                car.setTrunkCapacity(400.0);
                car.setElectric(false);
                car.setDriver(driver1); // Asignado y disponible
                vehicleRepository.save(car);
                System.out.println("Datos creados por defecto");

                // 3. Crear Conductor Ocupado
                Driver driver2 = Driver.builder()
                        .userId(102L)
                        .licenseNumber("LIC-002")
                        .status(DriverStatus.EN_RUTA)
                        .build();
                driverRepository.save(driver2);

                // 4. Crear Moto con conductor ocupado
                Motorcycle moto = new Motorcycle();
                moto.setBrand("Yamaha");
                moto.setModel("FZ");
                moto.setPlate("MBA-2000");
                moto.setCylinderCapacity(150);
                moto.setDriver(driver2);
                vehicleRepository.save(moto);

                System.out.println("Flota cargada: 1 Auto (Disp) y 1 Moto (Ocupada)");
            } else {
                System.out.println("La base de datos ya tiene vehículos. Iniciando en modo persistente.");
            }
        };
    }
}
