package com.example.bank_midterm.auth;

import com.example.bank_midterm.exception.CustomException;
import com.example.bank_midterm.user.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    private final AuthUtil authUtil;
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    public AuthServiceImpl(
        CustomUserDetailsService customUserDetailsService,
        JwtProvider jwtProvider,
        AuthUtil authUtil,
        UserRepository userRepository,
        PasswordEncoder passwordEncoder,
        ModelMapper modelMapper
    ) {
        this.authUtil = authUtil;
        this.customUserDetailsService = customUserDetailsService;
        this.jwtProvider = jwtProvider;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
    }

    public UserResponse me() {
        var user = authUtil.getAuthenticatedUser();
        return modelMapper.map(user, UserResponse.class);
    }

    public LoginResponse validateLoginAndSendToken(LoginRequest request) {
        var loginUser = userRepository.findByEmail(request.getEmail()).orElseThrow(
            () -> new CustomException(HttpStatus.UNAUTHORIZED, "Invalid credentials")
        );

        var isPasswordValid = passwordEncoder.matches(request.getPassword(), loginUser.getHashedPassword());
        if(!isPasswordValid) {
            throw new CustomException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }

        var userDetail = customUserDetailsService.loadUserByUsername(loginUser.getId().toString());
        var token = jwtProvider.generateToken(userDetail);

        return new LoginResponse(
            modelMapper.map(loginUser, UserResponse.class),
            token
        );
    }
}
