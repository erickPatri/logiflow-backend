package org.example.authservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data // para los getters y setters de forma automatica
@Builder // para crear objetos de forma mas fluida
@NoArgsConstructor // para constructor vacio
@AllArgsConstructor // para constructor con todos los argumentos
@Entity
@Table(name = "users")

public class User implements UserDetails { // hago uso de la interfaz sin crearla gracias a SpringSecurity
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // uso el identity porque es la manera sencilla, genera de 1 en 1 el ID cuando se inserte una fila,
    // pero no es bueno para insertar multiples usuarios de golpe por ejemplo
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {  // esto convierte el rol de String en una autoridad que Spring entienda
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    // estos metodos controlan si la cuenta expiro o esta bloqueada, en este caso siempre se devolvera que esta activa con el true
    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }
}
