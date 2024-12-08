package com.example.CineVibe.controller;

import com.example.CineVibe.model.Movie;
import com.example.CineVibe.service.MovieService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movies")
public class MovieController {
    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping
    public List<Movie> getAllMovies() {
        return movieService.getAllMovies();
    }

    @PostMapping
    public Movie createMovie(@RequestBody Movie movie) {
        return movieService.saveMovie(movie);
    }

    @DeleteMapping("/{id}")
    public void deleteMovie(@PathVariable Long id) {
        movieService.deleteMovie(id);
    }
}
