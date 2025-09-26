package com.example.bank_midterm.dto.transaction;

import com.example.bank_midterm.entity.Transaction;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
public class TransactionResponse {
    private UUID id;

    private String senderFirstName;

    private String senderLastName;

    private String receiverFirstName;

    private String receiverLastName;

    private String message;

    private BigDecimal amount;

    private BigDecimal fee;

    private Transaction.Type type;

    private Date completedAt;
}
