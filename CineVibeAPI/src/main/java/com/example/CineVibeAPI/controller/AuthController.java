package com.example.CineVibeAPI.controller;

import com.example.CineVibeAPI.model.User;
import com.example.CineVibeAPI.repository.UserRepository;
import com.example.CineVibeAPI.service.AuthService;
import com.example.CineVibeAPI.service.JwtService;
import com.example.CineVibeAPI.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Autowired
    private UserService userService;

    public AuthController(AuthService authService, UserRepository userRepository,
                          PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.authService = authService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        try {
            userService.registerUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody Map<String, String> loginRequest) {
        String login = loginRequest.get("username");  // login może być nazwą użytkownika lub emailem
        String password = loginRequest.get("password");

        // Sprawdzamy, czy login jest emailem czy nazwą użytkownika
        User user = userRepository.findByUsernameOrEmail(login, login)
                .orElse(null);

        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            return ResponseEntity.badRequest().body("Invalid username or password");
        }

        String token = jwtService.generateToken(user.getUsername());
        return ResponseEntity.ok(token);
    }



}
