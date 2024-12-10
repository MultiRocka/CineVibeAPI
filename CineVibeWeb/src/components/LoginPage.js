import React, { useState, useContext } from 'react';
import { AuthContext } from '../context/AuthContext';
import { useNavigate } from 'react-router-dom';
import axios from "axios";

const Login = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const { login } = useContext(AuthContext);
    const navigate = useNavigate();

    const handleLogin = async (e) => {
        e.preventDefault();

        try {
            // Wysyłamy dane logowania do backendu
            const response = await axios.post('http://localhost:8080/api/auth/login', {
                username: username,
                password: password
            });

            // Pobieramy token z odpowiedzi
            const token = response.data;

            // Sprawdzamy, czy token jest prawidłowy
            if (!token) {
                alert('Failed to retrieve token. Please try again.');
                return;
            }

            // Zapisujemy token w localStorage
            localStorage.setItem('token', token);

            // Przekazujemy token do kontekstu aplikacji
            login(token);

            // Przekierowujemy na stronę główną po zalogowaniu
            navigate('/');
        } catch (error) {
            console.error('Login failed:', error.response ? error.response.data : error.message);
            alert(error.response?.data?.message || 'Login failed. Please check your credentials.');
        }
    };




    return (
        <form onSubmit={handleLogin}>
            <input
                type="text"
                placeholder="Username"
                value={username}
                onChange={(e) => setUsername(e.target.value)}
            />
            <input
                type="password"
                placeholder="Password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
            />
            <button type="submit">Login</button>
        </form>
    );
};

export default Login;
