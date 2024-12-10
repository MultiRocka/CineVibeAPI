package com.example.CineVibeAPI.service;

import com.example.CineVibeAPI.model.User;
import com.example.CineVibeAPI.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    public UserService(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    public User getCurrentUser(String token) {
        // Usuwamy 'Bearer ' z tokenu
        String jwtToken = token;

        // Wydobycie nazwy użytkownika z tokenu JWT
        String username = jwtService.extractUsername(jwtToken);

        // Pobranie użytkownika na podstawie nazwy
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // Zmiana hasła dla użytkownika
    public void changePassword(String token, String newPassword) {
        User user = getCurrentUser(token);  // Używamy tokenu, by pobrać użytkownika
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        user.setPassword(passwordEncoder.encode(newPassword));  // Haszowanie nowego hasła
        System.out.println("Saving user: " + user.getUsername());
        userRepository.save(user);
    }

    // Zmiana nazwy użytkownika
    public void changeUsername(String token, String newUsername) {
        User currentUser = getCurrentUser(token);  // Używamy tokenu, by pobrać użytkownika
        if (userRepository.existsByUsername(newUsername)) {
            throw new IllegalArgumentException("Username already exists");
        }
        currentUser.setUsername(newUsername);
        userRepository.save(currentUser);  // Zapisanie zaktualizowanego użytkownika
    }

    // Rejestracja nowego użytkownika
    public void registerUser(User user) {
        // Sprawdzanie, czy e-mail już istnieje w bazie
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        // Sprawdzanie, czy nazwa użytkownika już istnieje
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }

        // Haszowanie hasła przed zapisaniem użytkownika
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Zapisanie użytkownika w bazie
        userRepository.save(user);
    }

    // Wyszukiwanie użytkownika po nazwie użytkownika
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
    }
}
