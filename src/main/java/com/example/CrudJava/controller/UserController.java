package com.example.CrudJava.controller;

import com.example.CrudJava.model.User;
import com.example.CrudJava.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService service;

    @GetMapping
    public List<User> getAllUsers(){
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = service.findById(id);
        return user.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Criar novo usuário
    @PostMapping
    public User createUsuario(@RequestBody User user) {
        return service.save(user);
    }

    // Atualizar usuário
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUsuario(@PathVariable Long id, @RequestBody User userDetails) {
        Optional<User> user = service.findById(id);
        if (user.isPresent()) {
            User updatedUsuario = user.get();
            updatedUsuario.setName(userDetails.getName()   );
            updatedUsuario.setEmail(userDetails.getEmail());
            return ResponseEntity.ok(service.save(updatedUsuario));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
