package com.example.bank_midterm.service;

import com.example.bank_midterm.dto.auth.LoginRequest;
import com.example.bank_midterm.dto.auth.LoginResponse;
import com.example.bank_midterm.dto.auth.UserResponse;

public interface AuthService {
    UserResponse me();
    LoginResponse validateLoginAndSendToken(LoginRequest request);
}
