package com.example.CineVibeAPI.model;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RoleValidationTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidRole() {
        // Given
        Role role = new Role("USER");

        // When
        Set<ConstraintViolation<Role>> violations = validator.validate(role);

        // Then
        assertTrue(violations.isEmpty(), "No violations should occur for valid role data");
    }

    @Test
    void testInvalidRoleName() {
        // Given
        Role role = new Role("");  // empty name, invalid

        // When
        Set<ConstraintViolation<Role>> violations = validator.validate(role);

        // Then
        assertEquals(1, violations.size());
        assertEquals("Role name cannot be blank", violations.iterator().next().getMessage());
    }

    @Test
    void testUniqueRoleName() {
        // Given
        Role role = new Role("ADMIN");

        // When
        // Normally, we would need to mock the database interaction to check for uniqueness,
        // but for this example, we will just test the validation constraint (if implemented at DB level).

        Set<ConstraintViolation<Role>> violations = validator.validate(role);

        // Then
        assertTrue(violations.isEmpty(), "No violations should occur for unique role name");
        // In a real scenario, we'd also check if the name is already in the DB (not done here).
    }
}
