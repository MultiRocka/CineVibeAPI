package com.example.CineVibeAPI.repository;

import com.example.CineVibeAPI.model.Movie;
import com.example.CineVibeAPI.model.Rating;
import com.example.CineVibeAPI.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RatingRepository extends JpaRepository<Rating, Long> {

    Optional<Rating> findByMovieAndUser(Movie movie, User user);

    @Query("SELECT AVG(r.score) FROM Rating r WHERE r.movie.id = :movieId")
    Optional<Double> findAverageRatingByMovie(@Param("movieId") Long movieId);

    @Query("SELECT COUNT(r) FROM Rating r WHERE r.movie = :movie")
    int countByMovie(@Param("movie") Movie movie);
}
