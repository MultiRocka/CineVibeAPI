package com.example.CineVibeAPI.dto;
import com.example.CineVibeAPI.model.Movie;
import lombok.Data;

@Data
public class MovieDto {
    private Long id;
    private String title;
    private String description;
    private String director;
    private int releaseYear;
    private double rating;  // Średnia ocena
    private String trailerUrl;
    private int ratingCount;  // Liczba ocen

    public MovieDto() {
    }

    public MovieDto(Long id, String title, String description, int releaseYear) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.releaseYear = releaseYear;
    }

    public MovieDto(Movie movie, int ratingCount, double rating) {
        this.id = movie.getId();
        this.title = movie.getTitle();
        this.description = movie.getDescription();
        this.director = movie.getDirector();
        this.releaseYear = movie.getReleaseYear();
        this.rating = rating;  // Średnia ocena
        this.trailerUrl = movie.getTrailerUrl();
        this.ratingCount = ratingCount;
    }
}

