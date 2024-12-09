package com.example.CineVibeAPI.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
@Entity
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull(message = "User cannot be null")
    private User user;

    @ManyToOne
    @JoinColumn(name = "movie_id", nullable = false)
    @NotNull(message = "Movie cannot be null")
    private Movie movie;

    @Column(nullable = false)
    @Min(value = 1, message = "Score must be at least 1")
    @Max(value = 10, message = "Score cannot be more than 10")
    private int score; // ocena od 1 do 10
}
