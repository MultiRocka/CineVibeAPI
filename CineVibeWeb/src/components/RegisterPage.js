import React, { useState } from 'react';
import axios from '../axios';
import { useNavigate } from 'react-router-dom';

const RegisterPage = () => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const navigate = useNavigate();

    const handleRegister = async (e) => {
        e.preventDefault();
        if (password === confirmPassword) {
            try {
                await axios.post("/auth/register", { email, password });
                const loginResponse = await axios.post("/auth/login", { username: email, password });
                localStorage.setItem('token', loginResponse.data);
                navigate('/');  // Przekierowanie na stronę główną po zalogowaniu
            } catch (error) {
                console.error("Registration or login failed", error);
                alert('Registration failed');
            }
        } else {
            alert("Passwords don't match");
        }
    };

    return (
        <div>
            <h1>Register</h1>
            <form onSubmit={handleRegister}>
                <label>
                    Email:
                    <input type="email" value={email} onChange={(e) => setEmail(e.target.value)} required />
                </label>
                <br />
                <label>
                    Password:
                    <input type="password" value={password} onChange={(e) => setPassword(e.target.value)} required />
                </label>
                <br />
                <label>
                    Confirm Password:
                    <input type="password" value={confirmPassword} onChange={(e) => setConfirmPassword(e.target.value)} required />
                </label>
                <br />
                <button type="submit">Register</button>
            </form>
        </div>
    );
};

export default RegisterPage;
