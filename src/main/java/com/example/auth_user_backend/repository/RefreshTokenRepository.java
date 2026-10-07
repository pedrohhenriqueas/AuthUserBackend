package com.example.auth_user_backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.auth_user_backend.model.RefreshTokenDto;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshTokenDto, Long> {
    Optional<RefreshTokenDto> findByToken(String token);

}
