package org.example.fleetservice.controller;


import lombok.RequiredArgsConstructor;
import org.example.fleetservice.dto.DriverRequest;
import org.example.fleetservice.dto.VehicleRequest;
import org.example.fleetservice.model.Driver;
import org.example.fleetservice.model.Vehicle;
import org.example.fleetservice.service.FleetService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fleet")
@RequiredArgsConstructor
public class FleetController {
    private final FleetService fleetService;

    // http://localhost:8082/fleet/drivers
    @PostMapping("/drivers")
    public ResponseEntity<Driver> createDriver(@RequestBody DriverRequest request) {
        return ResponseEntity.ok(fleetService.createDriver(request));
    }

    @GetMapping("/drivers")
    public ResponseEntity<List<Driver>> getAllDrivers() {
        return ResponseEntity.ok(fleetService.getAllDrivers());
    }

    // http://localhost:8082/fleet/vehicles
    @PostMapping("/vehicles")
    public ResponseEntity<Vehicle> createVehicle(@RequestBody VehicleRequest request) {
        return ResponseEntity.ok(fleetService.createVehicle(request));
    }

    @GetMapping("/vehicles")
    public ResponseEntity<List<Vehicle>> getAllVehicles() {
        return ResponseEntity.ok(fleetService.getAllVehicles());
    }

    // http://localhost:8082/fleet/vehicles/1/assign/5 , que significa que al vehículo 1, le voy a asignar el conductor 2
    @PutMapping("/vehicles/{vehicleId}/assign/{driverId}")
    public ResponseEntity<Vehicle> assignDriver(@PathVariable Long vehicleId, @PathVariable Long driverId) {
        return ResponseEntity.ok(fleetService.assignDriverToVehicle(vehicleId, driverId));
    }
}
