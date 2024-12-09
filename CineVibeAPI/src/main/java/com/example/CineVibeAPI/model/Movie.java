package com.example.CineVibeAPI.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
@Entity
@Table(name = "movies")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "Title cannot be blank")
    private String title;

    @Column(nullable = false, length = 300)
    @NotBlank(message = "Description cannot be blank")
    @Size(max = 300, message = "Description can have at most 300 characters")
    private String description;

    private String director;

    @Min(value = 1895, message = "Release year must be after the invention of cinema (1895)")
    @Max(value = 2029, message = "Release year cannot be more than 5 years into the future")
    private int releaseYear;

    @Column(nullable = false)
    @DecimalMin(value = "0.0", inclusive = true, message = "Rating must be at least 0.0")
    @DecimalMax(value = "10.0", inclusive = true, message = "Rating cannot be more than 10.0")
    private double rating;

    @Pattern(regexp = "^(https?://)(www\\.)?([\\w-]+)+([\\w/_.-]*)*(\\?[\\S]+)?$",
            message = "Trailer URL must be a valid URL")
    private String trailerUrl;
}
