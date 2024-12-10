import React, { useContext } from 'react';
import { useNavigate } from 'react-router-dom';
import {AuthContext, AuthProvider} from "../context/AuthContext";
import '../css/Navbar.css';
const Navbar = () => {
    const { isLoggedIn, logout } = useContext(AuthContext);
    const navigate = useNavigate();

    const handleLogout = () => {
        logout();
        navigate('/'); // Przekierowanie na stronę główną
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
