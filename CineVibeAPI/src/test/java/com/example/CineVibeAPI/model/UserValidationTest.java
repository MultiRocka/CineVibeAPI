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

public class UserValidationTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidUser() {
        // Given
        User user = new User("ValidUsername", "valid@example.com", "StrongPassword123");

        // When
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        // Then
        assertTrue(violations.isEmpty(), "No violations should occur for valid user data");
    }

    @Test
    void testInvalidUsername() {
        // Given
        User user = new User("ab", "valid@example.com", "StrongPassword123");

        // When
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        // Then
        assertEquals(1, violations.size());
        assertEquals("Username must be between 3 and 20 characters",
                violations.iterator().next().getMessage());
    }

    @Test
    void testInvalidEmail() {
        // Given
        User user = new User("ValidUsername", "invalid-email", "StrongPassword123");

        // When
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        // Then
        assertEquals(1, violations.size());
        assertEquals("Email should be valid", violations.iterator().next().getMessage());
    }

    @Test
    void testInvalidPassword() {
        // Given
        User user = new User("ValidUsername", "valid@example.com", "short");

        // When
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        // Then
        assertEquals(1, violations.size());
        assertEquals("Password must be at least 8 characters long",
                violations.iterator().next().getMessage());
    }
}
