package com.example.CrudJavaJwt.config;


import com.example.CrudJavaJwt.model.ERole;
import com.example.CrudJavaJwt.model.Roles;
import com.example.CrudJavaJwt.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

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