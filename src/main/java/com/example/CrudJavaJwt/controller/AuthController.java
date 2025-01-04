package com.example.CrudJavaJwt.controller;

import com.example.CrudJavaJwt.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import com.example.CrudJavaJwt.payload.request.LoginRequest;
import com.example.CrudJavaJwt.payload.request.SignupRequest;
import com.example.CrudJavaJwt.payload.response.JwtResponse;
import com.example.CrudJavaJwt.payload.response.MessageResponse;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/signin")
    public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        JwtResponse jwtResponse = authService.authenticateUser(loginRequest);

        return ResponseEntity.ok(jwtResponse);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        MessageResponse messageResponse = authService.registerUser(signUpRequest);

        return ResponseEntity.ok(messageResponse);
    }
}