package com.example.bank_midterm.dto.transaction;

import com.example.bank_midterm.entity.Transaction;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
public class InitTransactionRequest {
    @NotNull
    private UUID senderId;

    @NotNull
    private UUID receiverId;

    @NotNull
    @DecimalMin(value = "0.1")
    private BigDecimal amount;

    private String message;

    private Transaction.Type type;
}
