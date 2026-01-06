package org.example.fleetservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "drivers")
public class Driver {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // para vincular el usuario registrado en caso de que sea un conductor por ejemplo y asi exista la relacion
    @Column(name = "user_id", unique = true, nullable = false)
    private Long userId;

    @Column(name = "license_number", nullable = false)
    private String licenseNumber;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private DriverStatus status;
}
