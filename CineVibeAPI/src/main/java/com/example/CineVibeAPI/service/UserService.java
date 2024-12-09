package com.example.CineVibeAPI.service;

import com.example.CineVibeAPI.model.User;
import com.example.CineVibeAPI.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // Pobranie aktualnego użytkownika
    public User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
    }

    // Zmiana hasła użytkownika
    public void changePassword(String newPassword) {
        User user = getCurrentUser(); // Pobranie aktualnego użytkownika
        user.setPassword(passwordEncoder.encode(newPassword)); // Szyfrowanie hasła
        userRepository.save(user); // Zapisanie zaktualizowanego użytkownika
    }
}
