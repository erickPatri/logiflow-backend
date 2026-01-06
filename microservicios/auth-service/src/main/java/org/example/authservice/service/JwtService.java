package org.example.authservice.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    private static final String SECRET_KEY = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970"; // esta es una clave generada al azar que es
    // la llave secreta, seria como la firma, si alguien cambia el token, la firma ya no coincidira.

    // generar el token, se le pasa el username y devuelve el String del token
    public String generateToken(String username){
        Map<String, Object> extraClaims = new HashMap<>();
        return createToken(extraClaims, username);
    }

    // metodo para construir el token de verdad
    private String createToken(Map<String, Object> extraClaims, String subject){
        return Jwts.builder()
                .setClaims(extraClaims)
                .setIssuer("logiflow")
                .setSubject(subject) // el dueño del token
                .setIssuedAt(new Date(System.currentTimeMillis())) // fecha de creacion
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 *  60 * 10)) // expira en 10 horas
                .signWith(getSignKey(), SignatureAlgorithm.HS256) // se firma con la clave secreta
                .compact();

    }

    private Key getSignKey() { // obtener la clave de la firma
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // metodos de validacion que se usaran para proteger rutas
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

}
