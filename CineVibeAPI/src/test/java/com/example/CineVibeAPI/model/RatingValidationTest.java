package com.example.CineVibeAPI.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class RatingValidationTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        // Tworzymy instancję walidatora
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void testRatingValid() {
        // Tworzymy obiekt Rating z prawidłowymi danymi
        User user = new User();
        user.setId(1L);
        Movie movie = new Movie();
        movie.setId(1L);

        Rating rating = new Rating();
        rating.setUser(user);
        rating.setMovie(movie);
        rating.setScore(5);  // prawidłowa ocena w przedziale 1-10

        // Walidujemy obiekt
        Set<ConstraintViolation<Rating>> violations = validator.validate(rating);

        // Sprawdzamy, że nie ma żadnych błędów walidacji
        assertTrue(violations.isEmpty());
    }

    @Test
    void testRatingInvalidScoreTooLow() {
        // Tworzymy obiekt Rating z nieprawidłową oceną (poniżej 1)
        User user = new User();
        user.setId(1L);
        Movie movie = new Movie();
        movie.setId(1L);

        Rating rating = new Rating();
        rating.setUser(user);
        rating.setMovie(movie);
        rating.setScore(0);  // nieprawidłowa ocena (poniżej 1)

        // Walidujemy obiekt
        Set<ConstraintViolation<Rating>> violations = validator.validate(rating);

        // Sprawdzamy, że pojawił się błąd dla pola "score"
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Score must be at least 1", violations.iterator().next().getMessage());
    }

    @Test
    void testRatingInvalidScoreTooHigh() {
        // Tworzymy obiekt Rating z nieprawidłową oceną (ponad 10)
        User user = new User();
        user.setId(1L);
        Movie movie = new Movie();
        movie.setId(1L);

        Rating rating = new Rating();
        rating.setUser(user);
        rating.setMovie(movie);
        rating.setScore(11);  // nieprawidłowa ocena (powyżej 10)

        // Walidujemy obiekt
        Set<ConstraintViolation<Rating>> violations = validator.validate(rating);

        // Sprawdzamy, że pojawił się błąd dla pola "score"
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Score cannot be more than 10", violations.iterator().next().getMessage());
    }

    @Test
    void testRatingInvalidUserNull() {
        // Tworzymy obiekt Rating z pustym użytkownikiem
        Movie movie = new Movie();
        movie.setId(1L);

        Rating rating = new Rating();
        rating.setUser(null);  // brak użytkownika
        rating.setMovie(movie);
        rating.setScore(5);

        // Walidujemy obiekt
        Set<ConstraintViolation<Rating>> violations = validator.validate(rating);

        // Sprawdzamy, że pojawił się błąd dla pola "user"
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("User cannot be null", violations.iterator().next().getMessage());
    }

    @Test
    void testRatingInvalidMovieNull() {
        // Tworzymy obiekt Rating z pustym filmem
        User user = new User();
        user.setId(1L);

        Rating rating = new Rating();
        rating.setUser(user);
        rating.setMovie(null);  // brak filmu
        rating.setScore(5);

        // Walidujemy obiekt
        Set<ConstraintViolation<Rating>> violations = validator.validate(rating);

        // Sprawdzamy, że pojawił się błąd dla pola "movie"
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Movie cannot be null", violations.iterator().next().getMessage());
    }
}
