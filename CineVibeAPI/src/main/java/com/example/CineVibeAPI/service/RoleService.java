package com.example.CineVibeAPI.service;

import com.example.CineVibeAPI.model.Role;
import com.example.CineVibeAPI.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public Role createRole(Role role) {
        // Sprawdzamy, czy rola o tej nazwie już istnieje w bazie danych
        if (roleRepository.findByName(role.getName()).isPresent()) {
            throw new IllegalArgumentException("Role with name " + role.getName() + " already exists");
        }

        // Jeśli nie istnieje, zapisujemy nową rolę w bazie danych
        return roleRepository.save(role);
    }
}
