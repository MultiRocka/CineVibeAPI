package com.example.CineVibeAPI.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
@Entity
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    @NotBlank(message = "Role name cannot be blank")
    @Size(max = 50, message = "Role name must not exceed 50 characters")
    private String name;

    public Role(String name) {
        this.name = name;
    }

    public Role() {
    }
}
