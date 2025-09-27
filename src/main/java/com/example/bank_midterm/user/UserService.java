package com.example.bank_midterm.user;

import com.example.bank_midterm.auth.CreateUserRequest;
import com.example.bank_midterm.auth.UserResponse;

import java.util.List;

public interface UserService {
    UserResponse getUserByHandle(String handle);
    List<UserResponse> getAllUsers();
    UserResponse createUser(CreateUserRequest request);
}
