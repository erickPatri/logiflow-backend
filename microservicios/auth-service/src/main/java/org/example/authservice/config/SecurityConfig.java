package org.example.authservice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;

@Configuration // indica que esta clase tiene configuraciones
@EnableWebSecurity // habilita la seguridad web
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;

    // filtro de seguridad, se define que rutas son publicas y privadas
    @Bean
    public SecurityFilterChain SecurityFilterChain(HttpSecurity http) throws Exception{
        http
                .csrf(AbstractHttpConfigurer::disable) // Se desactiva CSRF porque se usara APIs REST (stateless)
                .authorizeHttpRequests(auth -> auth
                        // se permite el acceso total a lo que empiece por /auth/
                                .requestMatchers("/auth/**").permitAll()
                        // cualquier otra peticion, require estar autenticado
                        .anyRequest().authenticated()
                );
        return http.build();
    }
}
