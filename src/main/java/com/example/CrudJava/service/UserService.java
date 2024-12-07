package com.example.CrudJava.service;

import com.example.CrudJava.model.User;
import com.example.CrudJava.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    // Listar todos os usuários
    public List<User> findAll() {
        return repository.findAll();
    }

    // Buscar usuário por ID
    public Optional<User> findById(Long id) {
        return repository.findById(id);
    }

    // Salvar ou atualizar usuário
    public User save(User usuario) {
        return repository.save(usuario);
    }

    // Deletar usuário
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
