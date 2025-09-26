package com.example.bank_midterm.service;

import com.example.bank_midterm.dto.transaction.ConfirmTransactionRequest;
import com.example.bank_midterm.dto.transaction.InitTransactionRequest;
import com.example.bank_midterm.dto.transaction.TransactionResponse;

import java.util.List;
import java.util.UUID;

public interface TransactionService {
    TransactionResponse getCompletedTransactionById(UUID transactionId);
    List<TransactionResponse> getCompletedTransactionsByUserId(UUID userId);
    TransactionResponse initTransaction(InitTransactionRequest request);
    TransactionResponse confirmTransaction(ConfirmTransactionRequest request);
}
