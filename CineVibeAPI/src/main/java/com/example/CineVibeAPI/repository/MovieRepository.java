package com.example.CineVibeAPI.repository;

import com.example.CineVibeAPI.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, Long> {
}
