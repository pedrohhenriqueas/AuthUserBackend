package com.example.auth_user_backend.repository;

import com.example.auth_user_backend.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByName(String name);

    Optional<Users> findByEmail(String email);

    boolean existsByName(String name);

    boolean existsByEmail(String email);

    @Query("SELECT u FROM Users u JOIN FETCH u.roles WHERE u.email = :email")
    Optional<Users> findByEmailWithRoles(@Param("email") String email);
}