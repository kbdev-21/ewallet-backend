package com.example.bank_midterm.transaction;

import java.util.List;
import java.util.UUID;

public interface TransactionService {
    TransactionResponse getCompletedTransactionById(UUID transactionId);
    List<TransactionResponse> getCompletedTransactionsByUserId(UUID userId);
    TransactionResponse initTransaction(InitTransactionRequest request);
    TransactionResponse confirmTransaction(ConfirmTransactionRequest request);
}
