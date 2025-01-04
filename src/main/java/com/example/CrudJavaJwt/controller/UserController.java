package com.example.CrudJavaJwt.controller;

import com.example.CrudJavaJwt.model.Users;
import com.example.CrudJavaJwt.service.UserService;
import com.example.CrudJavaJwt.payload.request.SignupRequest;
import com.example.CrudJavaJwt.payload.response.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Users>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Users> getUserById(@PathVariable Long id) {
        Users user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/users/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Users> updateUser(@PathVariable("id") Long id, @RequestBody SignupRequest signUpRequest) {
        Users updatedUser = userService.updateUser(id, signUpRequest);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/users/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MessageResponse> deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(new MessageResponse("User deleted successfully!"));
    }
}

