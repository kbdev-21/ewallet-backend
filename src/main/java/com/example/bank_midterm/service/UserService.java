package com.example.bank_midterm.service;

import com.example.bank_midterm.dto.auth.CreateUserRequest;
import com.example.bank_midterm.dto.auth.UserResponse;
import com.example.bank_midterm.entity.User;

import java.util.List;

public interface UserService {
    UserResponse getUserByHandle(String handle);
    List<UserResponse> getAllUsers();
    UserResponse createUser(CreateUserRequest request);
}
