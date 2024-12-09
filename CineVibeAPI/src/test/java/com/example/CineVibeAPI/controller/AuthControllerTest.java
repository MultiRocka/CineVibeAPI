package com.example.CineVibeAPI.controller;

import com.example.CineVibeAPI.dto.RegisterRequest;
import com.example.CineVibeAPI.model.User;
import com.example.CineVibeAPI.repository.UserRepository;
import com.example.CineVibeAPI.service.AuthService;
import com.example.CineVibeAPI.service.JwtService;
import jakarta.validation.Valid;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Map;

public class AuthControllerTest {

    @Mock
    private AuthService authService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthController authController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @Test
    public void testRegisterUser_Success() throws Exception {
        // Given
        RegisterRequest registerRequest = new RegisterRequest("testUser", "test@example.com", "password123");

        doNothing().when(authService).registerUser(registerRequest);  // Mockowanie wywołania metody registerUser

        // When & Then
        mockMvc.perform(post("/api/auth/register")
                        .contentType("application/json")
                        .content("{\"username\":\"testUser\",\"email\":\"test@example.com\",\"password\":\"password123\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("User registered successfully"));

        // Verify that authService.registerUser() was called once
        verify(authService, times(1)).registerUser(registerRequest);
    }

    @Test
    public void testRegisterUser_ValidationFailed() throws Exception {
        // Given (brak wymaganych pól w requeście)
        mockMvc.perform(post("/api/auth/register")
                        .contentType("application/json")
                        .content("{\"username\":\"\",\"email\":\"\",\"password\":\"\"}"))
                .andExpect(status().isBadRequest());  // Sprawdzamy, czy odpowiedź ma status 400 (Bad Request)
    }

    @Test
    public void testLoginUser_Success() throws Exception {
        // Given
        Map<String, String> loginRequest = Map.of("username", "testUser", "password", "password123");
        User user = new User("testUser", "test@example.com", "encodedPassword");
        when(userRepository.findByUsernameOrEmail("testUser", "testUser")).thenReturn(java.util.Optional.of(user));
        when(passwordEncoder.matches("password123", "encodedPassword")).thenReturn(true);
        when(jwtService.generateToken("testUser")).thenReturn("token");

        // When & Then
        mockMvc.perform(post("/api/auth/login")
                        .contentType("application/json")
                        .content("{\"username\":\"testUser\",\"password\":\"password123\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("token"));

        // Verify that the user was authenticated and a token was generated
        verify(userRepository, times(1)).findByUsernameOrEmail("testUser", "testUser");
        verify(passwordEncoder, times(1)).matches("password123", "encodedPassword");
        verify(jwtService, times(1)).generateToken("testUser");
    }

    @Test
    public void testLoginUser_InvalidCredentials() throws Exception {
        // Given
        Map<String, String> loginRequest = Map.of("username", "wrongUser", "password", "wrongPassword");

        // When & Then
        mockMvc.perform(post("/api/auth/login")
                        .contentType("application/json")
                        .content("{\"username\":\"wrongUser\",\"password\":\"wrongPassword\"}"))
                .andExpect(status().isBadRequest())  // 400, ponieważ dane logowania są błędne
                .andExpect(content().string("Invalid username or password"));
    }



    @Test
    public void testLoginUser_UserNotFound() throws Exception {
        // Given
        Map<String, String> loginRequest = Map.of("username", "nonExistentUser", "password", "password123");

        when(userRepository.findByUsernameOrEmail("nonExistentUser", "nonExistentUser")).thenReturn(java.util.Optional.empty());

        // When & Then
        mockMvc.perform(post("/api/auth/login")
                        .contentType("application/json")
                        .content("{\"username\":\"nonExistentUser\",\"password\":\"password123\"}"))
                .andExpect(status().isBadRequest())  // 400, ponieważ użytkownik nie istnieje
                .andExpect(content().string("Invalid username or password"));
    }

}
