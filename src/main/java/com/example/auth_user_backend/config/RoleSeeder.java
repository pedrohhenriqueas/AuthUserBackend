package com.example.auth_user_backend.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import com.example.auth_user_backend.model.ERole;
import com.example.auth_user_backend.model.Roles;
import com.example.auth_user_backend.repository.RoleRepository;

@Configuration
public class RoleSeeder {

    @Autowired
    private RoleRepository roleRepository;

    @Bean
    public CommandLineRunner insertRoles() {
        return args -> {
            insertIfNotExists(ERole.ROLE_USER);
            insertIfNotExists(ERole.ROLE_ADMIN);
        };
    }

    @Transactional
    public void insertIfNotExists(ERole roleName) {
        if (!roleRepository.existsByName(roleName)) {
            Roles roles = new Roles();
            roles.setName(roleName);
            roleRepository.save(roles);
        }
    }
}