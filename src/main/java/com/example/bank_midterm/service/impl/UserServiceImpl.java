package com.example.bank_midterm.service.impl;

import com.example.bank_midterm.dto.auth.CreateUserRequest;
import com.example.bank_midterm.dto.auth.UserResponse;
import com.example.bank_midterm.entity.User;
import com.example.bank_midterm.repository.UserRepository;
import com.example.bank_midterm.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
            .stream()
            .map(u -> modelMapper.map(u, UserResponse.class))
            .toList();
    }

    public UserResponse createUser(CreateUserRequest request) {
        String hashedPassword = passwordEncoder.encode(request.getPassword());

        User newUser = new User();
        newUser.setId(UUID.randomUUID());
        newUser.setEmail(request.getEmail());
        newUser.setPhoneNum(request.getPhoneNum());
        newUser.setFirstName(request.getFirstName());
        newUser.setLastName(request.getLastName());
        newUser.setHashedPassword(hashedPassword);
        newUser.setBalance(BigDecimal.ZERO);
        newUser.setCreatedAt(new Date());
        newUser.setUpdatedAt(new Date());
        newUser.setRole(User.Role.USER);

        var savedUser = userRepository.save(newUser);
        return modelMapper.map(savedUser, UserResponse.class);
    }
}
