package com.example.bank_midterm.tuition;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Data
public class CreateTuitionRequest {
    @NotBlank
    private String studentCode;

    @NotBlank
    private String schoolName;

    @NotBlank
    private String title;

    @NotBlank
    @DecimalMin(value = "0.1")
    private BigDecimal amount;

    @NotBlank
    private UUID receiverId;

    @NotBlank
    private Date expiredAt;
}
