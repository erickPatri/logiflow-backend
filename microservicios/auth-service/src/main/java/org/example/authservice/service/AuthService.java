package org.example.authservice.service;
import lombok.RequiredArgsConstructor;
import org.example.authservice.dto.AuthResponse;
import org.example.authservice.dto.LoginRequest;
import org.example.authservice.dto.RegisterRequest;
import org.example.authservice.model.Role;
import org.example.authservice.model.User;
import org.example.authservice.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService; // para fabricar los tokens
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request){
        // validacion simple para ver si el usuario ya existe
        if(userRepository.existsByUsername(request.getUsername())){
            throw new RuntimeException("El nombre de usuario ya esta en uso, por favor elegir otro");
        }

        // caso contrario se crea el User, haciendo uso del builder para hacerlo mas limpio
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword())) // aqui se encripta la contraseña antes de guardarla
                .role(Role.valueOf(request.getRole()))
                .build();

        userRepository.save(user); // guardar en la BD

        String jwtToken = jwtService.generateToken(user.getUsername()); // genero el token real usando el username

        return AuthResponse.builder()
                .token(jwtToken)
                .message("Usuario registrado de manera correcta")
                .build();
    }

    public AuthResponse login(LoginRequest request){
        authenticationManager.authenticate( // este metodo automaticamente autentica con usuario y password, si esta mal lanza la exepcion
          new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        User user = userRepository.findByUsername(request.getUsername()).orElseThrow(); // si sus credenciales son validas se busca al usuario para generar su token
        String jwtToken = jwtService.generateToken(user.getUsername());

        return  AuthResponse.builder()
                .token(jwtToken)
                .message("Usuario logueado de manera correcta")
                .build();
    }
}
