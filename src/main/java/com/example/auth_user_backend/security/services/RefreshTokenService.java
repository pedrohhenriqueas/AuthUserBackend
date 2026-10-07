package com.example.auth_user_backend.security.services;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.auth_user_backend.exception.TokenRefreshException;
import com.example.auth_user_backend.model.RefreshTokenDto;
import com.example.auth_user_backend.repository.RefreshTokenRepository;
import com.example.auth_user_backend.service.UserService;

@Service
public class RefreshTokenService {

    @Value("${jwt.refresh.expirationS}")
    private Long refreshTokenDurationS;

    private final UserService userService;
    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenService(UserService userService,
            RefreshTokenRepository refreshTokenRepository) {
        this.userService = userService;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public RefreshTokenDto createRefreshToken(Long userId) {
        RefreshTokenDto refreshTokenDto = new RefreshTokenDto();
        refreshTokenDto.setUser(userService.findById(userId));
        refreshTokenDto.setExpiryDate(Instant.now().plusSeconds(refreshTokenDurationS));
        refreshTokenDto.setToken(UUID.randomUUID().toString());

        refreshTokenDto = refreshTokenRepository.save(refreshTokenDto);
        return refreshTokenDto;
    }

    public RefreshTokenDto verifyExpiration(RefreshTokenDto token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException(token.getToken(),
                    "Refresh token was expired. Please make a new signin request");
        }

        return token;
    }

    public Optional<RefreshTokenDto> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }
}
