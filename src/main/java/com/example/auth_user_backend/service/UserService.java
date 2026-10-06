package com.example.auth_user_backend.service;

import com.example.auth_user_backend.exception.UserNotFoundException;
import com.example.auth_user_backend.model.Roles;
import com.example.auth_user_backend.model.Users;
import com.example.auth_user_backend.model.enums.ERole;
import com.example.auth_user_backend.payload.request.SignupRequest;
import com.example.auth_user_backend.repository.RoleRepository;
import com.example.auth_user_backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserService(UserRepository userRepository,
            RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public List<Users> getAllUsers() {
        return userRepository.findAll();
    }

    public Users findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    }

    public Users updateUser(Long id,
            SignupRequest signUpRequest) {
        Users existingUser = findById(id);

        if (signUpRequest.getUsername() != null) {
            existingUser.setName(signUpRequest.getUsername());
        }
        if (signUpRequest.getEmail() != null) {
            existingUser.setEmail(signUpRequest.getEmail());
        }

        Set<String> strRoles = signUpRequest.getRole();
        if (strRoles != null) {
            Set<Roles> roles = new HashSet<>();
            strRoles.forEach(role -> {
                if (role.equals("admin")) {
                    Roles adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                            .orElseThrow(() -> new RuntimeException("Error: Role ADMIN is not found."));
                    roles.add(adminRole);
                } else {
                    Roles userRole = roleRepository.findByName(ERole.ROLE_USER)
                            .orElseThrow(() -> new RuntimeException("Error: Role USER is not found."));
                    roles.add(userRole);
                }
            });
            existingUser.setRoles(roles);
        }

        return userRepository.save(existingUser);
    }

    public void deleteUser(Long id) {
        Users user = findById(id);

        userRepository.delete(user);
    }

    public Users findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
    }

    public Users findByName(String name) {
        return userRepository.findByEmail(name)
                .orElseThrow(() -> new UserNotFoundException("User not found with name: " + name));
    }

    public boolean userExistsByName(String name) {
        return userRepository.existsByName(name);
    }

    public boolean userExistsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public void insertUser(Users user) {
        userRepository.save(user);
    }

    public Users findByEmailWithRoles(String email) {
        return userRepository.findByEmailWithRoles(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
    }
}
