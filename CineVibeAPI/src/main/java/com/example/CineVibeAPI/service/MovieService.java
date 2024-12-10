package com.example.CineVibeAPI.service;

import com.example.CineVibeAPI.model.Movie;
import com.example.CineVibeAPI.model.Rating;
import com.example.CineVibeAPI.model.User;
import com.example.CineVibeAPI.repository.MovieRepository;
import com.example.CineVibeAPI.dto.MovieDto;
import com.example.CineVibeAPI.repository.RatingRepository;
import com.example.CineVibeAPI.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MovieService {

    private final MovieRepository movieRepository;
    private final RatingRepository ratingRepository;
    private final UserRepository userRepository;

    public MovieService(MovieRepository movieRepository, RatingRepository ratingRepository, UserRepository userRepository) {
        this.movieRepository = movieRepository;
        this.ratingRepository = ratingRepository;
        this.userRepository = userRepository;
    }

    public List<MovieDto> getAllMovies() {
        return movieRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public MovieDto getMovieById(Long id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movie not found"));
        return convertToDto(movie);
    }

    public MovieDto addMovie(MovieDto movieDto) {
        Movie movie = convertToEntity(movieDto);
        Movie savedMovie = movieRepository.save(movie);
        return convertToDto(savedMovie);
    }

    private MovieDto convertToDto(Movie movie) {
        MovieDto dto = new MovieDto();
        dto.setId(movie.getId());
        dto.setTitle(movie.getTitle());
        dto.setDescription(movie.getDescription());
        dto.setDirector(movie.getDirector());
        dto.setReleaseYear(movie.getReleaseYear());
        dto.setRating(movie.getRating());
        dto.setTrailerUrl(movie.getTrailerUrl());
        return dto;
    }

    private Movie convertToEntity(MovieDto dto) {
        Movie movie = new Movie();
        movie.setTitle(dto.getTitle());
        movie.setDescription(dto.getDescription());
        movie.setDirector(dto.getDirector());
        movie.setReleaseYear(dto.getReleaseYear());
        movie.setRating(dto.getRating());
        movie.setTrailerUrl(dto.getTrailerUrl());
        return movie;
    }

    public MovieDto updateMovie(Long id, MovieDto movieDto) {
        Movie movie = movieRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Movie not found"));
        movie.setTitle(movieDto.getTitle());
        movie.setDescription(movieDto.getDescription());
        movie.setDirector(movieDto.getDirector());
        movie.setReleaseYear(movieDto.getReleaseYear());
        movie.setRating(movieDto.getRating());
        movie.setTrailerUrl(movieDto.getTrailerUrl());
        movieRepository.save(movie);
        return movieDto;
    }

    @Transactional
    public void rateMovie(Long movieId, Long userId, int score) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new IllegalArgumentException("Movie not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Sprawdzamy, czy użytkownik już ocenił ten film
        Optional<Rating> existingRating = ratingRepository.findByMovieAndUser(movie, user);
        if (existingRating.isPresent()) {
            Rating rating = existingRating.get();
            rating.setScore(score); // Aktualizujemy ocenę
            ratingRepository.save(rating); // Zapisujemy tylko ocenę
        } else {
            Rating rating = new Rating();
            rating.setMovie(movie);
            rating.setUser(user);
            rating.setScore(score);
            ratingRepository.save(rating); // Zapisujemy nową ocenę
        }

        // Po dodaniu/aktualizacji oceny, obliczamy średnią ocenę
        updateMovieRating(movie);
    }

    private void updateMovieRating(Movie movie) {
        // Obliczamy średnią ocenę
        double averageRating = ratingRepository.findAverageRatingByMovie(movie.getId())
                .orElse(0.0);

        // Jeśli średnia się zmieniła, zapisujemy film
        if (movie.getRating() != averageRating) {
            movie.setRating(averageRating);
            movieRepository.save(movie); // Zapisujemy tylko jeśli rating się zmienił
        }
    }


    public Movie findMovieById(Long id) {
        return movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movie not found with id: " + id));
    }

    public void deleteMovie(Long id) {
        movieRepository.deleteById(id);
    }
}
