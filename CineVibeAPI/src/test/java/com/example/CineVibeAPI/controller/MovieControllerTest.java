package com.example.CineVibeAPI.controller;

import com.example.CineVibeAPI.dto.MovieDto;
import com.example.CineVibeAPI.service.MovieService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;

@SpringBootTest
@AutoConfigureMockMvc
public class MovieControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Mock
    private MovieService movieService;

    @InjectMocks
    private MovieController movieController;

    private MovieDto movieDto;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(movieController).build();
        movieDto = new MovieDto(1L, "Inception", "Sci-Fi", 2010);
    }

    @Test
    public void testGetAllMovies() throws Exception {
        // Given
        when(movieService.getAllMovies()).thenReturn(Arrays.asList(movieDto));

        // When & Then
        mockMvc.perform(get("/api/movies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Inception"))
                .andExpect(jsonPath("$[0].releaseYear").value(2010));
    }

    @Test
    public void testGetMovieById() throws Exception {
        // Given
        when(movieService.getMovieById(1L)).thenReturn(movieDto);

        // When & Then
        mockMvc.perform(get("/api/movies/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Inception"))
                .andExpect(jsonPath("$.releaseYear").value(2010));
    }

    @Test
    public void testAddMovie() throws Exception {
        // Given
        when(movieService.addMovie(any(MovieDto.class))).thenReturn(movieDto);

        // When & Then
        mockMvc.perform(post("/api/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Inception\",\"genre\":\"Sci-Fi\",\"year\":2010}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Inception"))
                .andExpect(jsonPath("$.releaseYear").value(2010));
    }

    @Test
    public void testUpdateMovie() throws Exception {
        // Given
        when(movieService.updateMovie(eq(1L), any(MovieDto.class))).thenReturn(movieDto);

        // When & Then
        mockMvc.perform(put("/api/movies/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Inception Updated\",\"genre\":\"Sci-Fi\",\"year\":2010}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Inception"))
                .andExpect(jsonPath("$.releaseYear").value(2010));
    }

    @Test
    public void testRateMovie() throws Exception {
        // When & Then
        mockMvc.perform(post("/api/movies/{id}/rate", 1L)
                        .param("userId", "1")
                        .param("score", "5"))
                .andExpect(status().isOk());

        // Verify that movieService.rateMovie is called once
        verify(movieService, times(1)).rateMovie(eq(1L), eq(1L), eq(5));
    }
}
