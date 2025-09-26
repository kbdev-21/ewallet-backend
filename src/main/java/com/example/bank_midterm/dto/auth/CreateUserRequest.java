package com.example.bank_midterm.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class CreateUserRequest {
    @Email
    @NotBlank
    private String email;

    @Pattern(regexp = "\\d{8,11}")
    @NotNull
    private String phoneNum;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @Length(min = 6, max = 50)
    @NotNull
    private String password;
}
