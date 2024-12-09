// src/components/MainPage.js
import React, { useEffect, useState } from 'react';
import axios from 'axios';

const MainPage = () => {
    const [movies, setMovies] = useState([]);

    useEffect(() => {
        // Łączymy się z API, aby pobrać filmy
        axios.get('http://localhost:8080/api/movies')
            .then(response => {
                setMovies(response.data); // Ustawiamy filmy w stanie
            })
            .catch(error => {
                console.error('Error fetching movies:', error);
            });
    }, []);

    return (
        <div>
            <h1>Movies</h1>
            <div className="movies-container">
                {movies.map(movie => (
                    <div key={movie.id} className="movie-card">
                        <h3>{movie.title}</h3>
                        <p>{movie.description}</p>
                        <p>Release Year: {movie.releaseYear}</p>
                        <p>Rating: {movie.rating}</p>
                    </div>
                ))}
            </div>
        </div>
    );
};

export default MainPage;
