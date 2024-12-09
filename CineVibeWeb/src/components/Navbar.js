import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

const Navbar = () => {
    const [isLoggedIn, setIsLoggedIn] = useState(false);
    const navigate = useNavigate();

    useEffect(() => {
        const token = localStorage.getItem('token');
        setIsLoggedIn(!!token);  // Sprawdzamy, czy token jest w localStorage
    }, []);

    const handleLogout = () => {
        localStorage.removeItem('token'); // Usuwamy token z localStorage
        setIsLoggedIn(false);  // Ustawiamy stan na false
        navigate('/'); // Przekierowujemy na stronę główną
    };

    return (
        <nav>
            <ul>
                <li><a href="/">Home</a></li>
                {isLoggedIn ? (
                    <>
                        <li><a href="/account">My Account</a></li>
                        <li><button onClick={handleLogout}>Log Out</button></li>
                    </>
                ) : (
                    <>
                        <li><a href="/login">Login</a></li>
                        <li><a href="/register">Register</a></li>
                    </>
                )}
            </ul>
        </nav>
    );
};

export default Navbar;
