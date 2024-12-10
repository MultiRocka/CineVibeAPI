package com.example.CineVibeAPI.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {
    private final Key secretKey;

    public JwtService(@Value("${jwt.secret}") String secret) {
        if (secret == null || secret.isEmpty()) {
            this.secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        } else {
            this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
        }
    }

    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    private static final long CLOCK_SKEW = 5 * 1000;  // Allow 5 seconds of clock skew

    public boolean validateToken(String token, String username) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .setClock(() -> new Date(System.currentTimeMillis() + CLOCK_SKEW))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String extractedUsername = claims.getSubject();
            System.out.println("Extracted username: " + extractedUsername); // Logowanie nazwy użytkownika
            return (extractedUsername.equals(username) && !claims.getExpiration().before(new Date()));
        } catch (JwtException e) {
            System.err.println("Error validating token: " + e.getMessage()); // Logowanie błędów
            return false;
        }
    }



    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody().getExpiration();
    }

    public String extractUsername(String token) {
        try {
            System.out.println("Received JWT Token: " + token); // Logowanie tokenu
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (JwtException e) {
            System.err.println("Invalid JWT token: " + e.getMessage()); // Logowanie błędu
            throw new RuntimeException("Invalid JWT token", e);
        }
    }



}
