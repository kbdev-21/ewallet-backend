package com.example.bank_midterm.auth;

public interface AuthService {
    UserResponse me();
    LoginResponse validateLoginAndSendToken(LoginRequest request);
}
