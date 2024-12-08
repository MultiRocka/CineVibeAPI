package com.example.CineVibeAPI;

import com.example.CineVibeAPI.model.Role;
import com.example.CineVibeAPI.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class CineVibeApiApplication {

	@Bean
	CommandLineRunner initRoles(RoleRepository roleRepository) {
		return args -> {
			// Sprawdź, czy rola "USER" już istnieje, jeśli nie, to ją dodaj
			roleRepository.findByName("USER").ifPresentOrElse(
					role -> {}, // Jeśli rola istnieje, nic nie rób
					() -> roleRepository.save(new Role("USER")) // Jeśli nie, dodaj ją
			);

			roleRepository.findByName("CRITIC").ifPresentOrElse(
					role -> {},
					() -> roleRepository.save(new Role("CRITIC"))
			);

			roleRepository.findByName("ADMIN").ifPresentOrElse(
					role -> {},
					() -> roleRepository.save(new Role("ADMIN"))
			);
		};
	}
	public static void main(String[] args) {
		SpringApplication.run(CineVibeApiApplication.class, args);
	}

}
