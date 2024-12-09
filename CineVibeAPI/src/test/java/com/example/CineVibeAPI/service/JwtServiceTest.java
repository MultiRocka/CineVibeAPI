package com.example.CineVibeAPI.service;

import com.example.CineVibeAPI.service.JwtService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.Key;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class JwtServiceTest {

    private JwtService jwtService;
    private String secret;
    private Key secretKey;

    @BeforeEach
    public void setUp() {
        // Używamy klucza jako string
        secret = "tJ/zkOTvAiLjjyK4KK1Gok7GjVhn8EPhPUnjrKX1J75h0t1AZes4bk/u3FbYM30lhCakyUAxJ8554SwWS4Z1hg==";
        secretKey = Keys.hmacShaKeyFor(secret.getBytes());  // Convert the string to Key
        jwtService = new JwtService(secret);  // Pass the secret string to JwtService
    }

    @Test
    public void testGenerateToken() {
        // Given
        String username = "testUser";

        // When
        String token = jwtService.generateToken(username);

        // Then
        assertNotNull(token); // Upewniamy się, że token nie jest null
        assertTrue(token.split("\\.").length == 3); // Sprawdzamy, czy token ma 3 części (nagłówek, ładunek i podpis)

        // Możemy także sprawdzić, czy token zawiera nazwę użytkownika
        String extractedUsername = jwtService.extractUsername(token);
        assertEquals(username, extractedUsername); // Upewniamy się, że nazwa użytkownika w tokenie się zgadza
    }



    @Test
    public void testValidateToken_Valid() {
        // Given
        String username = "testUser";
        String token = jwtService.generateToken(username);

        // When
        boolean isValid = jwtService.validateToken(token, username);

        // Then
        assertTrue(isValid);
    }

    @Test
    public void testValidateToken_Invalid() {
        // Given
        String username = "testUser";
        String wrongUsername = "wrongUser";
        String token = jwtService.generateToken(username);

        // When
        boolean isValid = jwtService.validateToken(token, wrongUsername);

        // Then
        assertFalse(isValid);
    }

    @Test
    public void testValidateToken_Expired() {
        // Given
        String username = "testUser";

        // Tworzymy wygasły token (set the expiration to a past time)
        String expiredToken = Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() - 1000))  // Expired token (1 second ago)
                .signWith(secretKey, SignatureAlgorithm.HS256)  // Use the key object
                .compact();

        // When
        boolean isValid = jwtService.validateToken(expiredToken, username);

        // Then
        assertFalse(isValid);  // Ensure the token is invalid because it's expired
    }

    @Test
    public void testExtractUsername() {
        // Given
        String username = "testUser";
        String token = jwtService.generateToken(username);

        // When
        String extractedUsername = jwtService.extractUsername(token);

        // Then
        assertEquals(username, extractedUsername);
    }
}
