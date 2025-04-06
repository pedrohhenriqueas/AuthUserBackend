package com.example.auth_user_backend.service;

import com.example.auth_user_backend.exception.UserNotFoundException;
import com.example.auth_user_backend.model.ERole;
import com.example.auth_user_backend.model.Roles;
import com.example.auth_user_backend.model.Users;
import com.example.auth_user_backend.payload.request.SignupRequest;
import com.example.auth_user_backend.repository.RoleRepository;
import com.example.auth_user_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder encoder;

    public List<Users> getAllUsers() {
        return userRepository.findAll();
    }

    public Users getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    }

    public Users updateUser(Long id, SignupRequest signUpRequest) {
        Users existingUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

        if (signUpRequest.getUsername() != null) {
            existingUser.setName(signUpRequest.getUsername());
        }
        if (signUpRequest.getEmail() != null) {
            existingUser.setEmail(signUpRequest.getEmail());
        }
        if (signUpRequest.getPassword() != null) {
            existingUser.setPassword(encoder.encode(signUpRequest.getPassword()));
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
        Users user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

        userRepository.delete(user);
    }
}
