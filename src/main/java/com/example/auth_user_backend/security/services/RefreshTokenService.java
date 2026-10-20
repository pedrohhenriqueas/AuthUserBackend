package com.example.auth_user_backend.security.services;

import java.time.Instant;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.auth_user_backend.exception.TokenRefreshException;
import com.example.auth_user_backend.model.RefreshTokenDto;
import com.example.auth_user_backend.model.Users;
import com.example.auth_user_backend.payload.response.TokenRefreshResponse;
import com.example.auth_user_backend.repository.RefreshTokenRepository;
import com.example.auth_user_backend.security.jwt.JwtUtils;
import com.example.auth_user_backend.service.UserService;

@Service
public class RefreshTokenService {

    @Value("${jwt.refresh.expirationS}")
    private Long refreshTokenDurationS;

    private final UserService userService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtils jwtUtils;

    public RefreshTokenService(UserService userService,
            RefreshTokenRepository refreshTokenRepository,
            JwtUtils jwtUtils) {
        this.userService = userService;
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtUtils = jwtUtils;
    }

    public RefreshTokenDto createRefreshToken(Long userId) {
        Users user = userService.findById(userId);

        RefreshTokenDto refreshTokenDto = refreshTokenRepository.findByUser(user)
                .orElseGet(RefreshTokenDto::new);
        refreshTokenDto.setUser(user);
        refreshTokenDto.setExpiryDate(Instant.now().plusSeconds(refreshTokenDurationS));
        refreshTokenDto.setToken(UUID.randomUUID().toString());

        return refreshTokenRepository.save(refreshTokenDto);
    }

    public TokenRefreshResponse refresh(String token) {
        RefreshTokenDto tokenDto = findByToken(token);

        verifyExpiration(tokenDto);

        Users user = tokenDto.getUser();
        String newJwt = jwtUtils.generateTokenFromEmail(user.getEmail());
        // rotação: gera um refresh token novo e invalida o antigo
        RefreshTokenDto newRefreshToken = createRefreshToken(user.getId());

        return new TokenRefreshResponse(newJwt, newRefreshToken.getToken());
    }

    public RefreshTokenDto verifyExpiration(RefreshTokenDto token) {
        if (token.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException("Refresh token was expired. Please make a new signin request");
        }

        return token;
    }

    public RefreshTokenDto findByToken(String token) {
        return refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new TokenRefreshException("Refresh token is not in database!"));
    }
}
