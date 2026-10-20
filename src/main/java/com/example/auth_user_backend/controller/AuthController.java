package com.example.auth_user_backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.auth_user_backend.payload.request.LoginRequest;
import com.example.auth_user_backend.payload.request.SignupRequest;
import com.example.auth_user_backend.payload.request.TokenRefreshRequest;
import com.example.auth_user_backend.payload.response.JwtResponse;
import com.example.auth_user_backend.payload.response.MessageResponse;
import com.example.auth_user_backend.payload.response.TokenRefreshResponse;
import com.example.auth_user_backend.security.services.RefreshTokenService;
import com.example.auth_user_backend.service.AuthService;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;

    public AuthController(AuthService authService,
            RefreshTokenService refreshTokenService) {
        this.authService = authService;
        this.refreshTokenService = refreshTokenService;
    }

    @PostMapping("/signin")
    public ResponseEntity<Object> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        JwtResponse jwtResponse = authService.authenticateUser(loginRequest);

        return ResponseEntity.ok(jwtResponse);
    }

    @PostMapping("/signup")
    public ResponseEntity<MessageResponse> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        authService.verifySignUpBody(signUpRequest);

        MessageResponse messageResponse = authService.registerUser(signUpRequest);
        return ResponseEntity.ok(messageResponse);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<TokenRefreshResponse> refreshToken(@Valid @RequestBody TokenRefreshRequest request) {
        return ResponseEntity.ok(refreshTokenService.refresh(request.getRefreshToken()));
    }
}
