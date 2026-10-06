package com.example.auth_user_backend.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.auth_user_backend.model.Roles;
import com.example.auth_user_backend.model.enums.ERole;
import com.example.auth_user_backend.repository.RoleRepository;

@Configuration
public class RoleSeeder {

    private final RoleRepository roleRepository;

    public RoleSeeder(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Bean
    public CommandLineRunner insertRoles() {
        return args -> {
            insertIfNotExists(ERole.ROLE_USER);
            insertIfNotExists(ERole.ROLE_ADMIN);
        };
    }

    public void insertIfNotExists(ERole roleName) {
        if (!roleRepository.existsByName(roleName)) {
            Roles roles = new Roles();
            roles.setName(roleName);
            roleRepository.save(roles);
        }
    }
}