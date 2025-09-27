package com.example.bank_midterm.controller;

import com.example.bank_midterm.transaction.ConfirmTransactionRequest;
import com.example.bank_midterm.transaction.InitTransactionRequest;
import com.example.bank_midterm.transaction.TransactionResponse;
import com.example.bank_midterm.transaction.TransactionService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/api/transactions/{transactionId}")
    public TransactionResponse getCompletedTransactionById(@PathVariable UUID transactionId) {
        return transactionService.getCompletedTransactionById(transactionId);
    }

    @GetMapping("/api/transactions/by-userid/{userId}")
    public List<TransactionResponse> getByUserId(@PathVariable UUID userId) {
        return transactionService.getCompletedTransactionsByUserId(userId);
    }

    @PostMapping("/api/transactions/init")
    public TransactionResponse initTransaction(@Valid @RequestBody InitTransactionRequest request) {
        return transactionService.initTransaction(request);
    }

    @PostMapping("/api/transactions/confirm")
    public TransactionResponse confirmTransaction(@Valid @RequestBody ConfirmTransactionRequest request) {
        return transactionService.confirmTransaction(request);
    }
}
