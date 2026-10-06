package com.example.auth_user_backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.auth_user_backend.payload.request.LoginRequest;
import com.example.auth_user_backend.payload.request.SignupRequest;
import com.example.auth_user_backend.payload.response.JwtResponse;
import com.example.auth_user_backend.payload.response.MessageResponse;
import com.example.auth_user_backend.service.AuthService;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signin")
    public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        JwtResponse jwtResponse = authService.authenticateUser(loginRequest);

        return ResponseEntity.ok(jwtResponse);
    }

    @PostMapping("/signup")
    public ResponseEntity<MessageResponse> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        authService.verifySignUpBody(signUpRequest);

        MessageResponse messageResponse = authService.registerUser(signUpRequest);
        return ResponseEntity.ok(messageResponse);
    }
}