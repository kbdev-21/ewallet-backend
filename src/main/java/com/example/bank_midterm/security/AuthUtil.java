package com.example.bank_midterm.security;

import com.example.bank_midterm.entity.User;
import com.example.bank_midterm.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class AuthUtil {
    private final UserRepository userRepository;

    public AuthUtil(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getAuthenticatedUser() {
        var userId = UUID.fromString(SecurityContextHolder.getContext().getAuthentication().getName());
        return userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
