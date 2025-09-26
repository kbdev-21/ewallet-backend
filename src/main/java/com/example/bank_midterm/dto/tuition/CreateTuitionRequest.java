package com.example.bank_midterm.dto.tuition;

import com.example.bank_midterm.entity.Tuition;
import com.example.bank_midterm.entity.User;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
