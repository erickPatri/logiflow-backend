package org.example.fleetservice.dto;

import lombok.Data;

@Data
public class DriverRequest {
    private Long userId; // ID del usuario en authservice
    private String licenseNumber;
    private String phoneNumber;

    // se espera: "DISPONIBLE", "NO_DISPONIBLE", "EN_RUTA"
    private String status;
}
