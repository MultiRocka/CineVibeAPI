package com.example.CineVibeAPI.controller;

import com.example.CineVibeAPI.dto.PasswordChangeRequest;
import com.example.CineVibeAPI.dto.UserDto;
import com.example.CineVibeAPI.model.User;
import com.example.CineVibeAPI.service.JwtService;
import com.example.CineVibeAPI.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private final JwtService jwtService;

    public UserController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @GetMapping("/user")
    public ResponseEntity<UserDto> getUserData(@RequestHeader("Authorization") String token) {
        try {
            // Usunięcie 'Bearer ' z tokenu
            String jwtToken;
            if (token != null && token.startsWith("Bearer ")) {
                jwtToken = token.substring(7);
            } else {
                throw new RuntimeException("Invalid token format in getUserData");
            }

            System.out.println("Calling extractUsername from getUserData");
            if (jwtService.validateToken(jwtToken, jwtService.extractUsername(jwtToken))) {
                String username = jwtService.extractUsername(jwtToken);
                User user = userService.findByUsername(username); // Pobierz użytkownika na podstawie nazwy
                UserDto userDTO = new UserDto(user.getUsername(), user.getEmail()); // Utwórz DTO
                return ResponseEntity.ok(userDTO);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    @PutMapping("/user/username")
    public ResponseEntity<Void> updateUsername(@RequestBody Map<String, String> request, @RequestHeader("Authorization") String token) {
        String newUsername = request.get("username");

        try {
            userService.changeUsername(token, newUsername);  // Przekazujemy token do UserService
            return ResponseEntity.ok().build();  // Zwracamy odpowiedź 200 OK
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);  // Zwracamy 400, jeśli nazwa użytkownika już istnieje
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);  // Zwracamy 500 w przypadku innych błędów
        }
    }


    @PutMapping("/user/password")
    public ResponseEntity<String> updatePassword(@RequestBody PasswordChangeRequest request, @RequestHeader("Authorization") String token) {
        try {
            String jwtToken;
            if (token != null && token.startsWith("Bearer ")) {
                jwtToken = token.substring(7);
            } else {
                throw new RuntimeException("Invalid token format in updatePassword");
            }

            System.out.println("Calling extractUsername from updatePassword");
            // Wydobycie nazwy użytkownika z tokenu JWT
            String username = jwtService.extractUsername(jwtToken);

            if (username == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
            }

            User user = userService.findByUsername(username);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }

            userService.changePassword(jwtToken, request.getPassword());
            return ResponseEntity.ok().build(); // Zwrócenie odpowiedzi 200 OK
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while updating password: " + e.getMessage());
        }
    }






}
