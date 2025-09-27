package com.example.bank_midterm.service.impl;

import com.example.bank_midterm.dto.auth.CreateUserRequest;
import com.example.bank_midterm.dto.auth.UserResponse;
import com.example.bank_midterm.entity.User;
import com.example.bank_midterm.exception.CustomException;
import com.example.bank_midterm.repository.UserRepository;
import com.example.bank_midterm.service.UserService;
import com.example.bank_midterm.shared.AppUtil;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
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

    @Override
    public UserResponse getUserByHandle(String handle) {
        if(AppUtil.isUUID(handle)) {
            return modelMapper.map(
                userRepository.findById(UUID.fromString(handle)).orElseThrow(
                    () -> new CustomException(HttpStatus.NOT_FOUND, "User not found")
                ),
                UserResponse.class
            );
        }

        return modelMapper.map(
            userRepository.findByEmail(handle).orElse(
                userRepository.findByPhoneNum(handle).orElseThrow(
                    () -> new CustomException(HttpStatus.NOT_FOUND, "User not found")
                )
            ),
            UserResponse.class
        );
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
            .stream()
            .map(u -> modelMapper.map(u, UserResponse.class))
            .toList();
    }

    public UserResponse createUser(CreateUserRequest request) {
        String searchPhrase = request.getEmail().toLowerCase() + " " + request.getPhoneNum() + " " + request.getFirstName().toLowerCase() + " " + request.getLastName().toLowerCase();
        String hashedPassword = passwordEncoder.encode(request.getPassword());

        User newUser = new User();
        newUser.setId(UUID.randomUUID());
        newUser.setEmail(request.getEmail());
        newUser.setPhoneNum(request.getPhoneNum());
        newUser.setFirstName(request.getFirstName());
        newUser.setLastName(request.getLastName());
        newUser.setSearchPhrase(searchPhrase);
        newUser.setHashedPassword(hashedPassword);
        newUser.setBalance(BigDecimal.ZERO);
        newUser.setCreatedAt(new Date());
        newUser.setUpdatedAt(new Date());
        newUser.setRole(User.Role.USER);

        var savedUser = userRepository.save(newUser);
        return modelMapper.map(savedUser, UserResponse.class);
    }
}
