package com.example.bank_midterm.controller;

import com.example.bank_midterm.auth.LoginRequest;
import com.example.bank_midterm.auth.LoginResponse;
import com.example.bank_midterm.auth.UserResponse;
import com.example.bank_midterm.auth.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("api/auth/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.validateLoginAndSendToken(request);
    }

    @GetMapping("api/auth/me")
    public UserResponse me() {
        return authService.me();
    }
}
