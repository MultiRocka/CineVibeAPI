import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Navbar from './components/Navbar';
import MainPage from './components/MainPage';
import RegisterPage from './components/RegisterPage';
import LoginPage from './components/LoginPage';
import AccountPage from './components/AccountPage';
import {AuthProvider} from "./context/AuthContext";

const App = () => {
    return (
        <AuthProvider>
            <Router>
                <Navbar />
                <Routes>
                    <Route path="/" element={<MainPage />} />
                    <Route path="/register" element={<RegisterPage />} />
                    <Route path="/login" element={<LoginPage />} />
                    <Route path="/account" element={<AccountPage />} />
                </Routes>
            </Router>
        </AuthProvider>
    );
};

export default App;
