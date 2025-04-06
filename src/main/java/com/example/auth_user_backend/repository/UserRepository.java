package com.example.auth_user_backend.repository;

import com.example.auth_user_backend.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByName(String name);

    Boolean existsByName(String name);

    Boolean existsByEmail(String email);
}