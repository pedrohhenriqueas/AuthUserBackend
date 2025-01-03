package com.example.CrudJavaJwt.service;

import com.example.CrudJavaJwt.model.Users;
import com.example.CrudJavaJwt.payload.request.SignupRequest;
import com.example.CrudJavaJwt.repository.RoleRepository;
import com.example.CrudJavaJwt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
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

    public Optional<Users> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Users updateUser(Long id, SignupRequest signUpRequest) {
        Optional<Users> existingUserOpt = userRepository.findById(id);

        if (!existingUserOpt.isPresent()) {
            throw new RuntimeException("User not found with id: " + id);
        }

        Users existingUser = existingUserOpt.get();

        existingUser.setName(signUpRequest.getUsername());
        existingUser.setEmail(signUpRequest.getEmail());
        existingUser.setPassword(encoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = signUpRequest.getRole();
        Set<com.example.CrudJavaJwt.model.Roles> roles = new HashSet<>();

        if (strRoles == null) {
            com.example.CrudJavaJwt.model.Roles userRole = roleRepository.findByName(com.example.CrudJavaJwt.model.ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                if (role.equals("admin")) {
                    com.example.CrudJavaJwt.model.Roles adminRole = roleRepository.findByName(com.example.CrudJavaJwt.model.ERole.ROLE_ADMIN)
                            .orElseThrow(() -> new RuntimeException("Error: Role ADMIN is not found."));
                    roles.add(adminRole);
                } else {
                    com.example.CrudJavaJwt.model.Roles userRole = roleRepository.findByName(com.example.CrudJavaJwt.model.ERole.ROLE_USER)
                            .orElseThrow(() -> new RuntimeException("Error: Role USER is not found."));
                    roles.add(userRole);
                }
            });
        }

        existingUser.setRoles(roles);
        return userRepository.save(existingUser);
    }

    public void deleteUser(Long id) {
        Optional<Users> userOpt = userRepository.findById(id);

        if (!userOpt.isPresent()) {
            throw new RuntimeException("User not found with id: " + id);
        }

        userRepository.deleteById(id);
    }
}
