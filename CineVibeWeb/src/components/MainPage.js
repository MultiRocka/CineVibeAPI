import React, { useEffect, useState } from 'react';
import axios from 'axios';
import '../css/MainPage.css';

const MainPage = () => {
    const [movies, setMovies] = useState([]);
    const [newMovie, setNewMovie] = useState({
        title: '',
        description: '',
        releaseYear: '',
        rating: 0,
        trailerUrl: ''
    });
    const [editMovie, setEditMovie] = useState(null);

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

    const handleAddMovie = async (e) => {
        e.preventDefault();
        try {
            // Jeśli trailerUrl jest pusty, ustawiamy null
            const movieData = {
                ...newMovie,
                trailerUrl: newMovie.trailerUrl.trim() === '' ? null : newMovie.trailerUrl
            };
            const response = await axios.post('http://localhost:8080/api/movies', movieData);
            setMovies([...movies, response.data]); // Dodajemy nowy film do stanu
            setNewMovie({
                title: '',
                description: '',
                releaseYear: '',
                rating: 0,
                trailerUrl: ''
            });
        } catch (error) {
            console.error('Error adding movie:', error);
        }
    };

    const handleEditMovie = async (e) => {
        e.preventDefault();
        try {
            // Jeśli trailerUrl jest pusty, ustawiamy null
            const movieData = {
                ...editMovie,
                trailerUrl: editMovie.trailerUrl.trim() === '' ? null : editMovie.trailerUrl
            };
            const response = await axios.put(`http://localhost:8080/api/movies/${editMovie.id}`, movieData);
            setMovies(movies.map(movie => movie.id === editMovie.id ? response.data : movie));
            setEditMovie(null); // Zakończenie edycji
        } catch (error) {
            console.error('Error editing movie:', error);
        }
    };

    const handleDeleteMovie = async (id) => {
        try {
            await axios.delete(`http://localhost:8080/api/movies/${id}`);
            setMovies(movies.filter(movie => movie.id !== id)); // Usuwamy film z listy
        } catch (error) {
            console.error('Error deleting movie:', error);
        }
    };

    const handleRating = async (movieId, score) => {
        try {
            const response = await axios.post('http://localhost:8080/api/movies/rating', {
                movie: { id: movieId },
                score: score
            });
            setMovies(movies.map(movie => movie.id === movieId ? { ...movie, rating: response.data.score } : movie));
        } catch (error) {
            console.error('Error adding rating:', error);
        }
    };

    return (
        <div>
            <h1>Movies</h1>
            <form onSubmit={handleAddMovie}>
                <input
                    type="text"
                    placeholder="Title"
                    value={newMovie.title}
                    onChange={(e) => setNewMovie({ ...newMovie, title: e.target.value })}
                />
                <input
                    type="text"
                    placeholder="Description"
                    value={newMovie.description}
                    onChange={(e) => setNewMovie({ ...newMovie, description: e.target.value })}
                />
                <input
                    type="number"
                    placeholder="Release Year"
                    value={newMovie.releaseYear}
                    onChange={(e) => setNewMovie({ ...newMovie, releaseYear: e.target.value })}
                />
                <input
                    type="number"
                    placeholder="Rating"
                    value={newMovie.rating}
                    onChange={(e) => setNewMovie({ ...newMovie, rating: e.target.value })}
                />
                <input
                    type="text"
                    placeholder="Trailer URL"
                    value={newMovie.trailerUrl}
                    onChange={(e) => setNewMovie({ ...newMovie, trailerUrl: e.target.value })}
                />
                <button type="submit">Add Movie</button>
            </form>

            <div className="movies-container">
                {movies.map(movie => (
                    <div key={movie.id} className="movie-card">
                        <h3>{movie.title}</h3>
                        <p>{movie.description}</p>
                        <p>Release Year: {movie.releaseYear}</p>
                        <p>Rating: {movie.rating}</p>

                        {/* Rating */}
                        <button onClick={() => handleRating(movie.id, 10)}>Rate 10</button>
                        <button onClick={() => handleRating(movie.id, 5)}>Rate 5</button>

                        {/* Edit/Delete */}
                        <button onClick={() => setEditMovie(movie)}>Edit</button>
                        <button onClick={() => handleDeleteMovie(movie.id)}>Delete</button>

                        {/* Edit form */}
                        {editMovie && editMovie.id === movie.id && (
                            <form onSubmit={handleEditMovie}>
                                <input
                                    type="text"
                                    value={editMovie.title}
                                    onChange={(e) => setEditMovie({ ...editMovie, title: e.target.value })}
                                />
                                <input
                                    type="text"
                                    value={editMovie.description}
                                    onChange={(e) => setEditMovie({ ...editMovie, description: e.target.value })}
                                />
                                <input
                                    type="number"
                                    value={editMovie.releaseYear}
                                    onChange={(e) => setEditMovie({ ...editMovie, releaseYear: e.target.value })}
                                />
                                <input
                                    type="number"
                                    value={editMovie.rating}
                                    onChange={(e) => setEditMovie({ ...editMovie, rating: e.target.value })}
                                />
                                <input
                                    type="text"
                                    value={editMovie.trailerUrl}
                                    onChange={(e) => setEditMovie({ ...editMovie, trailerUrl: e.target.value })}
                                />
                                <button type="submit">Save</button>
                                <button onClick={() => setEditMovie(null)}>Cancel</button>
                            </form>
                        )}
                    </div>
                ))}
            </div>
        </div>
    );
};

export default MainPage;
