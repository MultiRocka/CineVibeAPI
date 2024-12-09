package com.example.CineVibeAPI.service;

import com.example.CineVibeAPI.dto.RegisterRequest;
import com.example.CineVibeAPI.model.Role;
import com.example.CineVibeAPI.model.User;
import com.example.CineVibeAPI.repository.RoleRepository;
import com.example.CineVibeAPI.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    private RegisterRequest registerRequest;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        registerRequest = new RegisterRequest("testUser", "test@example.com", "password123");
    }

    @Test
    public void testRegisterUser_Success() {
        // Given
        Role userRole = new Role();
        userRole.setName("USER");
        when(roleRepository.findByName("USER")).thenReturn(java.util.Optional.of(userRole));
        when(userRepository.findByUsername(registerRequest.getUsername())).thenReturn(java.util.Optional.empty());
        when(passwordEncoder.encode(registerRequest.getPassword())).thenReturn("encodedPassword");

        // When
        authService.registerUser(registerRequest);

        // Then
        verify(userRepository, times(1)).save(any(User.class));  // Verify that save is called
    }

    @Test
    public void testRegisterUser_UsernameAlreadyTaken() {
        // Given
        when(userRepository.findByUsername(registerRequest.getUsername())).thenReturn(java.util.Optional.of(new User()));

        // When & Then
        assertThrows(IllegalStateException.class, () -> authService.registerUser(registerRequest), "Username is already taken");
    }

    @Test
    public void testRegisterUser_NoUserRoleFound() {
        // Given
        when(roleRepository.findByName("USER")).thenReturn(java.util.Optional.empty());

        // When & Then
        assertThrows(IllegalStateException.class, () -> authService.registerUser(registerRequest), "Default role USER not found");
    }
}
