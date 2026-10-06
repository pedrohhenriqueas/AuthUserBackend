package com.example.auth_user_backend.service;

import com.example.auth_user_backend.exception.ConflictException;
import com.example.auth_user_backend.exception.EmptyListException;
import com.example.auth_user_backend.model.Roles;
import com.example.auth_user_backend.model.Users;
import com.example.auth_user_backend.model.enums.ERole;
import com.example.auth_user_backend.payload.request.LoginRequest;
import com.example.auth_user_backend.payload.request.SignupRequest;
import com.example.auth_user_backend.payload.response.JwtResponse;
import com.example.auth_user_backend.payload.response.MessageResponse;
import com.example.auth_user_backend.repository.RoleRepository;
import com.example.auth_user_backend.security.jwt.JwtUtils;
import com.example.auth_user_backend.security.services.UserDetailsImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;
    private final UserService userService;

    public AuthService(AuthenticationManager authenticationManager,
            JwtUtils jwtUtils,
            RoleRepository roleRepository,
            PasswordEncoder encoder,
            UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
        this.userService = userService;
    }

    public JwtResponse authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String jwt = jwtUtils.generateJwtToken(authentication);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .toList();

        return new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(), userDetails.getEmail(), roles);
    }

    public MessageResponse registerUser(SignupRequest signUpRequest) {

        Users user = new Users(signUpRequest.getUsername(), signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        Set<Roles> roles = verifySignUpRoles(signUpRequest);

        user.setRoles(roles);
        userService.insertUser(user);

        return new MessageResponse("User registered successfully!");
    }

    public void verifySignUpBody(SignupRequest signUpRequest) {
        if (userService.userExistsByEmail(signUpRequest.getEmail())) {
            throw new ConflictException("Error: Email is already in use!");
        }
    }

    private Set<Roles> verifySignUpRoles(SignupRequest signUpRequest) {
        Set<String> strRoles = signUpRequest.getRole();
        Set<Roles> roles = new HashSet<>();

        if (strRoles == null) {
            Roles userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new EmptyListException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                if (role.equals("ROLE_ADMIN")) {
                    Roles adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                            .orElseThrow(() -> new EmptyListException("Error: Role ADMIN is not found."));
                    roles.add(adminRole);
                } else {
                    Roles userRole = roleRepository.findByName(ERole.ROLE_USER)
                            .orElseThrow(() -> new EmptyListException("Error: Role USER is not found."));
                    roles.add(userRole);
                }
            });
        }
        return roles;
    }
}
