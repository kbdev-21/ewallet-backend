package com.example.bank_midterm.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
    UserResponse user;
    String token;
}
