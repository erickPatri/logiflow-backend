package org.example.graphqlservice.model;

// se utiliza Records porque una vez que se mete los datos ( es decir cuando llega el JSON), nadie puede modificarlos por accidente
public record DriverDTO(
        Long id,
        Long userId,
        String licenseNumber,
        String phoneNumber,
        String status
) {
}
