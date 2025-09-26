package com.example.bank_midterm.dto.auth;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class UserResponse {
    private UUID id;
    private String email;
    private String phoneNum;
    private String firstName;
    private String lastName;
    private BigDecimal balance;
}
