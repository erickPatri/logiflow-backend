package org.example.authservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    private String username;
    private String email;
    private String password;
    // El rol es en este caso opcional, se podria asignarlo por defecto, pero lo voy a enviar para las pruebas
    private String role;
}
