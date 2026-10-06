package com.example.auth_user_backend.security.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.example.auth_user_backend.model.Users;
import com.example.auth_user_backend.service.UserService;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	private final UserService userService;

	public UserDetailsServiceImpl(UserService userService) {
		this.userService = userService;
	}

	public UserDetails loadUserByEmail(String email) {
		Users users = userService.findByEmailWithRoles(email);
		return UserDetailsImpl.build(users);
	}

	public UserDetails loadUserByUsername(String username) {
		Users users = userService.findByName(username);
		return UserDetailsImpl.build(users);
	}
}