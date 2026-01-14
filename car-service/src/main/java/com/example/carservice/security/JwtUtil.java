package com.example.carservice.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

@Component
public class JwtUtil {
    private final SecretKey secretKey;
    private final long accessTokenValidityMs;

    public JwtUtil(@Value("${jwt.secret}") String secret,
                   @Value("${jwt.access-token-validity-ms:900000}") long accessTokenValidityMs) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
        this.accessTokenValidityMs = accessTokenValidityMs;
    }

    public boolean validateToken(String token) {
        try {
            getAllClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getUserId(String token) {
        return getClaim(token, Claims::getSubject);
    }

    @SuppressWarnings("unchecked")
    public Set<String> getRoles(String token) {
        // Hada howa l-code s7i7: 9raha bhal List 3ad 7wwelha l Set
        List<String> roles = getAllClaims(token).get("roles", List.class);
        return new HashSet<>(roles);
    }
    public <T> T getClaim(String token, Function<Claims, T> resolver) {
        return resolver.apply(getAllClaims(token));
    }

    private Claims getAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
    }
}