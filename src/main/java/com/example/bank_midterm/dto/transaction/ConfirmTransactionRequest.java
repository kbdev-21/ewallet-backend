package com.example.bank_midterm.dto.transaction;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class ConfirmTransactionRequest {
    @NotNull
    private UUID transactionId;

    @NotNull
    private String confirmOtpCode;
}
