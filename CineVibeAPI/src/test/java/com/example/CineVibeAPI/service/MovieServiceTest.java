package com.example.CineVibeAPI.service;
import com.example.CineVibeAPI.dto.MovieDto;
import com.example.CineVibeAPI.model.Movie;
import com.example.CineVibeAPI.model.Rating;
import com.example.CineVibeAPI.model.User;
import com.example.CineVibeAPI.repository.MovieRepository;
import com.example.CineVibeAPI.repository.RatingRepository;
import com.example.CineVibeAPI.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class MovieServiceTest {

    @InjectMocks
    private MovieService movieService;

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private RatingRepository ratingRepository;

    @Mock
    private UserRepository userRepository;

    @Test
    void testGetAllMovies() {
        Movie movie = new Movie();
        movie.setTitle("Test Movie");
        when(movieRepository.findAll()).thenReturn(List.of(movie));

        List<MovieDto> movies = movieService.getAllMovies();

        assertNotNull(movies);
        assertEquals(1, movies.size());
        assertEquals("Test Movie", movies.get(0).getTitle());
    }

    @Test
    void testGetMovieById() {
        // Given
        Movie movie = new Movie();
        movie.setTitle("Test Movie");
        movie.setId(1L);
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));

        // When
        MovieDto movieDto = movieService.getMovieById(1L);

        // Then
        assertNotNull(movieDto);
        assertEquals("Test Movie", movieDto.getTitle());
    }

    @Test
    void testGetMovieById_NotFound() {
        // Given
        when(movieRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> movieService.getMovieById(1L));
    }

    @Test
    void testAddMovie() {
        // Given
        MovieDto movieDto = new MovieDto();
        movieDto.setTitle("New Movie");
        movieDto.setDescription("A new movie description");
        movieDto.setDirector("Director");
        movieDto.setReleaseYear(2024);
        movieDto.setRating(5.0);
        movieDto.setTrailerUrl("http://trailer.url");

        Movie movie = new Movie();
        movie.setTitle(movieDto.getTitle());

        // Mock the repository to return a new movie when save is called
        when(movieRepository.save(any(Movie.class))).thenReturn(movie);

        // When
        MovieDto addedMovie = movieService.addMovie(movieDto);

        // Then
        assertNotNull(addedMovie);
        assertEquals("New Movie", addedMovie.getTitle());
    }


    @Test
    void testUpdateMovie() {
        // Given
        MovieDto movieDto = new MovieDto();
        movieDto.setTitle("Updated Movie");
        movieDto.setDescription("Updated description");
        movieDto.setDirector("Updated Director");
        movieDto.setReleaseYear(2025);
        movieDto.setRating(4.5);
        movieDto.setTrailerUrl("http://updated.url");

        Movie existingMovie = new Movie();
        existingMovie.setId(1L);
        existingMovie.setTitle("Old Movie");
        when(movieRepository.findById(1L)).thenReturn(Optional.of(existingMovie));
        when(movieRepository.save(any(Movie.class))).thenReturn(existingMovie);

        // When
        MovieDto updatedMovie = movieService.updateMovie(1L, movieDto);

        // Then
        assertNotNull(updatedMovie);
        assertEquals("Updated Movie", updatedMovie.getTitle());
    }

    @Test
    void testUpdateMovie_NotFound() {
        // Given
        MovieDto movieDto = new MovieDto();
        when(movieRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> movieService.updateMovie(1L, movieDto));
    }

    @Test
    void testRateMovie() {
        // Given
        Movie movie = new Movie();
        movie.setId(1L);
        movie.setTitle("Test Movie");
        User user = new User();
        user.setId(1L);
        Rating rating = new Rating();
        rating.setMovie(movie);
        rating.setUser(user);
        rating.setScore(3);

        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(ratingRepository.findByMovieAndUser(movie, user)).thenReturn(Optional.of(rating));
        when(ratingRepository.save(any(Rating.class))).thenReturn(rating);

        // When
        movieService.rateMovie(1L, 1L, 4);

        // Then
        verify(ratingRepository).save(any(Rating.class)); // Check that save is called
        assertEquals(4, rating.getScore()); // Ensure rating is updated
    }

    @Test
    void testRateMovie_NewRating() {
        // Given
        Movie movie = new Movie();
        movie.setId(1L);
        movie.setTitle("Test Movie");
        User user = new User();
        user.setId(1L);
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(ratingRepository.findByMovieAndUser(movie, user)).thenReturn(Optional.empty());
        when(ratingRepository.save(any(Rating.class))).thenReturn(new Rating());

        // When
        movieService.rateMovie(1L, 1L, 4);

        // Then
        verify(ratingRepository).save(any(Rating.class)); // Check that save is called
    }

}
