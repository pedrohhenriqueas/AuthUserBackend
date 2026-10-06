package com.example.auth_user_backend.repository;

import com.example.auth_user_backend.model.Roles;
import com.example.auth_user_backend.model.enums.ERole;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Roles, Long> {
    Optional<Roles> findByName(ERole name);

    boolean existsByName(com.example.auth_user_backend.model.enums.ERole name);
}