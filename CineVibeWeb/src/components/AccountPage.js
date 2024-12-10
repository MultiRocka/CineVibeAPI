import React, { useState, useEffect } from 'react';
import axios from '../axios';
import { useNavigate } from 'react-router-dom';
import '../css/AccountPage.css';

const AccountPage = () => {
    const [user, setUser] = useState(null);
    const [isEditing, setIsEditing] = useState(false);
    const [newPassword, setNewPassword] = useState('');
    const [loading, setLoading] = useState(true); // Stan ładowania danych
    const [error, setError] = useState(null); // Stan błędu
    const navigate = useNavigate();

    // Funkcja do pobrania danych użytkownika
    useEffect(() => {
        const token = localStorage.getItem('token');
        if (!token) {
            navigate('/login'); // Jeśli nie ma tokenu, przekierowanie na stronę logowania
            return;
        }

        const fetchUserData = async () => {
            try {
                const response = await axios.get('/user', {
                    headers: {
                        Authorization: `Bearer ${token}`,
                    },
                });
                setUser(response.data);
                setLoading(false);
            } catch (error) {
                setLoading(false);
                setError(error.response ? error.response.data : 'Failed to load user data.');
                console.error("Error fetching user data:", error.response ? error.response.data : error.message);
            }
        };

        fetchUserData();
    }, [navigate]);

    // Funkcja do zmiany hasła
    const handlePasswordChange = async (e) => {
        e.preventDefault();

        // Sprawdzenie, czy nowe hasło jest wystarczająco długie
        if (!newPassword || newPassword.length < 8) {
            alert("Password must be at least 8 characters long.");
            return;
        }

        try {
            const token = localStorage.getItem('token');
            console.log("JWT Token being sent:", token);
            // Wysyłamy PUT request z odpowiednią strukturą
            const response = await axios.put('/user/password', { password: newPassword }, {
                headers: {
                    Authorization: `Bearer ${token}`,
                },

            });

            console.log("Received JWT Token:", token);
            if (response.status === 200) {
                alert("Password updated successfully!");
                setIsEditing(false); // Po zapisaniu hasła kończymy edycję
                setNewPassword(''); // Resetowanie hasła po zapisaniu
            }
        } catch (error) {
            // Obsługa błędów z backendu
            const errorMessage = error.response ? error.response.data : "Error updating password";
            alert(errorMessage);
            console.error("Error updating password:", errorMessage);
        }
    };

    if (loading) {
        return <div>Loading...</div>; // Wyświetlamy ładowanie, jeśli dane użytkownika nie zostały jeszcze załadowane
    }

    if (error) {
        return <div>{error}</div>; // Wyświetlamy błąd, jeśli wystąpił
    }

    return (
        <div>
            <h1>Your Account</h1>
            <p><strong>Username:</strong> {user.username}</p>
            <p><strong>Email:</strong> {user.email}</p>
            {/* Zabezpieczenie: nie wyświetlamy hasła */}
            <p><strong>Password:</strong> ********</p>

            {/* Edycja hasła */}
            {isEditing ? (
                <form onSubmit={handlePasswordChange}>
                    <label>
                        New Password:
                        <input
                            type="password"
                            value={newPassword}
                            onChange={(e) => setNewPassword(e.target.value)}
                            required
                        />
                    </label>
                    <br />
                    <button type="submit">Save</button>
                    <button type="button" onClick={() => setIsEditing(false)}>Cancel</button>
                </form>
            ) : (
                <button onClick={() => setIsEditing(true)}>Edit Password</button>
            )}
        </div>
    );
};

export default AccountPage;
