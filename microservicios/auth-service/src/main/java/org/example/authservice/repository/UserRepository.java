package org.example.authservice.repository;

import org.example.authservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

//JPARepository sirve para los metodos de guardar, encontrar y eliminar por ejemplo de la BD
public interface UserRepository extends JpaRepository<User, Long> {
    // En SQL seria el equivalente a: SELECT * FROM users WHERE username = ?
    Optional<User> findByUsername(String username);

    // para validar si existe un usuario antes de registrarlo
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

}
