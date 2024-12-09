package com.example.CineVibeAPI.service;

import com.example.CineVibeAPI.model.Role;
import com.example.CineVibeAPI.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RoleServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleService roleService;

    private Role role;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        role = new Role("USER");
    }

    @Test
    void testCreateRole_Valid() {
        // Given
        when(roleRepository.findByName("USER")).thenReturn(java.util.Optional.empty());
        when(roleRepository.save(any(Role.class))).thenReturn(role);

        // When
        Role createdRole = roleService.createRole(role);

        // Then
        assertNotNull(createdRole);
        assertEquals("USER", createdRole.getName());
        verify(roleRepository).save(role);  // Verify that save was called
    }

    @Test
    void testCreateRole_RoleAlreadyExists() {
        // Given
        when(roleRepository.findByName("USER")).thenReturn(java.util.Optional.of(role));

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> roleService.createRole(role));
        assertEquals("Role with name USER already exists", exception.getMessage());
    }
}
