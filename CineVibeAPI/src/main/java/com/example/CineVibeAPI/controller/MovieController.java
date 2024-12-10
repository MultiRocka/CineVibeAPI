package com.example.CineVibeAPI.controller;

import com.example.CineVibeAPI.dto.MovieDto;
import com.example.CineVibeAPI.service.MovieService;
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
    public List<MovieDto> getAllMovies() {
        return movieService.getAllMovies();
    }

    @GetMapping("/{id}")
    public MovieDto getMovieById(@PathVariable Long id) {
        return movieService.getMovieById(id);
    }

    @PostMapping
    public MovieDto addMovie(@RequestBody MovieDto movieDto) {
        return movieService.addMovie(movieDto);
    }

    @PutMapping("/{id}")
    public MovieDto updateMovie(@PathVariable Long id, @RequestBody MovieDto movieDto) {
        return movieService.updateMovie(id, movieDto);
    }
    @DeleteMapping("/{id}")
    public void deleteMovie(@PathVariable Long id) {
        movieService.deleteMovie(id);
    }
    @PostMapping("/{id}/rate")
    public void rateMovie(@PathVariable Long id, @RequestParam Long userId, @RequestParam int score) {
        movieService.rateMovie(id, userId, score);
    }

}
