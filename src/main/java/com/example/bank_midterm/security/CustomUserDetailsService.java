package com.example.bank_midterm.security;

import com.example.bank_midterm.repository.UserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String idString) throws UsernameNotFoundException {
            return userRepository.findById(UUID.fromString(idString))
                .map(user -> User.builder()
                    .username(user.getId().toString())
                    .password(user.getHashedPassword())
                    .roles(user.getRole().name())
                    .build()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
