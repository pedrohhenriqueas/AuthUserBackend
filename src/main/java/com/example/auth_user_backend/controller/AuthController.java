package com.example.auth_user_backend.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.auth_user_backend.exception.TokenRefreshException;
import com.example.auth_user_backend.model.RefreshTokenDto;
import com.example.auth_user_backend.payload.request.LoginRequest;
import com.example.auth_user_backend.payload.request.SignupRequest;
import com.example.auth_user_backend.payload.response.JwtResponse;
import com.example.auth_user_backend.payload.response.MessageResponse;
import com.example.auth_user_backend.payload.response.TokenRefreshResponse;
import com.example.auth_user_backend.security.jwt.JwtUtils;
import com.example.auth_user_backend.security.services.RefreshTokenService;
import com.example.auth_user_backend.service.AuthService;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;
    private final JwtUtils jwtUtils;

    public AuthController(AuthService authService,
            RefreshTokenService refreshTokenService,
            JwtUtils jwtUtils) {
        this.authService = authService;
        this.refreshTokenService = refreshTokenService;
        this.jwtUtils = jwtUtils;
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

    @PutMapping(path = "/refresh-token", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> refreshToken(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam("refreshToken") String refreshToken) {

        logger.info("[PUT] | auth/refresh-token/{refreshToken} | refreshToken: {}", refreshToken);

        if (authHeader == null) {
            throw new IllegalArgumentException("Empty JWT Token.");
        }

        if (refreshToken == null) {
            throw new IllegalArgumentException("Empty Refresh token.");
        }

        RefreshTokenDto tokenDto = refreshTokenService.findByToken(refreshToken)
                .orElseThrow(() -> new TokenRefreshException(
                        refreshToken, "Refresh token is not in database!"));

        refreshTokenService.verifyExpiration(tokenDto);

        String newJwt = jwtUtils.generateTokenFromEmail(tokenDto.getUser().getEmail());

        return ResponseEntity.ok(new TokenRefreshResponse(newJwt, refreshToken));

    }
}