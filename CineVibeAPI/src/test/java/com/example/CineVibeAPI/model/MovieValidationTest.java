package com.example.CineVibeAPI.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class MovieValidationTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        // Tworzymy instancję walidatora
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void testMovieValid() {
        // Tworzymy obiekt Movie z prawidłowymi danymi
        Movie movie = new Movie();
        movie.setTitle("Inception");
        movie.setDescription("A mind-bending thriller.");
        movie.setReleaseYear(2010);
        movie.setRating(8.8);
        movie.setTrailerUrl("https://www.youtube.com/watch?v=YoHD9XEInc0");

        // Walidujemy obiekt
        Set<ConstraintViolation<Movie>> violations = validator.validate(movie);

        // Sprawdzamy, że nie ma żadnych błędów walidacji
        assertTrue(violations.isEmpty());
    }

    @Test
    void testMovieInvalidTitleBlank() {
        // Tworzymy obiekt Movie z pustym tytułem
        Movie movie = new Movie();
        movie.setTitle(""); // pusty tytuł
        movie.setDescription("A thrilling movie.");
        movie.setReleaseYear(2020);
        movie.setRating(7.5);
        movie.setTrailerUrl("https://www.youtube.com/watch?v=abcd1234");

        // Walidujemy obiekt
        Set<ConstraintViolation<Movie>> violations = validator.validate(movie);

        // Sprawdzamy, że pojawił się błąd dla pola "title"
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Title cannot be blank", violations.iterator().next().getMessage());
    }

    @Test
    void testMovieInvalidDescriptionTooLong() {
        // Tworzymy obiekt Movie z za długim opisem
        Movie movie = new Movie();
        movie.setTitle("The Matrix");
        movie.setDescription("A".repeat(301));  // opis dłuższy niż 300 znaków
        movie.setReleaseYear(1999);
        movie.setRating(8.7);
        movie.setTrailerUrl("https://www.youtube.com/watch?v=vKQi3bBA1y8");

        // Walidujemy obiekt
        Set<ConstraintViolation<Movie>> violations = validator.validate(movie);

        // Sprawdzamy, że pojawił się błąd dla pola "description"
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Description can have at most 300 characters", violations.iterator().next().getMessage());
    }

    @Test
    void testMovieInvalidReleaseYearTooOld() {
        // Tworzymy obiekt Movie z rokiem produkcji przed wynalezieniem kina
        Movie movie = new Movie();
        movie.setTitle("Early Film");
        movie.setDescription("A very early film.");
        movie.setReleaseYear(1800);  // rok przed wynalezieniem kina
        movie.setRating(5.5);
        movie.setTrailerUrl("https://www.youtube.com/watch?v=abcd1234");

        // Walidujemy obiekt
        Set<ConstraintViolation<Movie>> violations = validator.validate(movie);

        // Sprawdzamy, że pojawił się błąd dla pola "releaseYear"
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Release year must be after the invention of cinema (1895)", violations.iterator().next().getMessage());
    }

    @Test
    void testMovieInvalidReleaseYearTooFuture() {
        // Tworzymy obiekt Movie z rokiem produkcji w przyszłości
        Movie movie = new Movie();
        movie.setTitle("Future Movie");
        movie.setDescription("A movie set in the future.");
        movie.setReleaseYear(2030);  // rok przyszły
        movie.setRating(9.0);
        movie.setTrailerUrl("https://www.youtube.com/watch?v=abcd1234");

        // Walidujemy obiekt
        Set<ConstraintViolation<Movie>> violations = validator.validate(movie);

        // Sprawdzamy, że pojawił się błąd dla pola "releaseYear"
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Release year cannot be more than 5 years into the future", violations.iterator().next().getMessage());
    }

    @Test
    void testMovieInvalidRatingTooLow() {
        // Tworzymy obiekt Movie z oceną poniżej 0.0
        Movie movie = new Movie();
        movie.setTitle("Bad Movie");
        movie.setDescription("A poorly rated movie.");
        movie.setReleaseYear(2000);
        movie.setRating(-1.0);  // ocena poniżej 0.0
        movie.setTrailerUrl("https://www.youtube.com/watch?v=abcd1234");

        // Walidujemy obiekt
        Set<ConstraintViolation<Movie>> violations = validator.validate(movie);

        // Sprawdzamy, że pojawił się błąd dla pola "rating"
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Rating must be at least 0.0", violations.iterator().next().getMessage());
    }

    @Test
    void testMovieInvalidRatingTooHigh() {
        // Tworzymy obiekt Movie z oceną powyżej 10.0
        Movie movie = new Movie();
        movie.setTitle("Superb Movie");
        movie.setDescription("An amazing movie.");
        movie.setReleaseYear(2023);
        movie.setRating(10.5);  // ocena powyżej 10.0
        movie.setTrailerUrl("https://www.youtube.com/watch?v=abcd1234");

        // Walidujemy obiekt
        Set<ConstraintViolation<Movie>> violations = validator.validate(movie);

        // Sprawdzamy, że pojawił się błąd dla pola "rating"
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Rating cannot be more than 10.0", violations.iterator().next().getMessage());
    }

    @Test
    void testMovieInvalidTrailerUrl() {
        // Tworzymy obiekt Movie z nieprawidłowym URL traileru
        Movie movie = new Movie();
        movie.setTitle("Invalid Trailer");
        movie.setDescription("A movie with an invalid trailer URL.");
        movie.setReleaseYear(2021);
        movie.setRating(7.0);
        movie.setTrailerUrl("invalid_url");  // nieprawidłowy URL

        // Walidujemy obiekt
        Set<ConstraintViolation<Movie>> violations = validator.validate(movie);

        // Sprawdzamy, że pojawił się błąd dla pola "trailerUrl"
        assertFalse(violations.isEmpty(), "Violations should not be empty");
        assertEquals(1, violations.size(), "Should be exactly one violation");
        assertTrue(violations.iterator().next().getMessage().contains("valid URL"), "Error message should contain 'valid URL'");
    }

}
