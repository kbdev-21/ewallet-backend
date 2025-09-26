package com.example.bank_midterm.service;

import com.example.bank_midterm.dto.transaction.ConfirmTransactionRequest;
import com.example.bank_midterm.dto.transaction.TransactionResponse;
import com.example.bank_midterm.dto.tuition.CreateTuitionRequest;
import com.example.bank_midterm.dto.tuition.TuitionPaymentRequest;
import com.example.bank_midterm.dto.tuition.TuitionResponse;
import com.example.bank_midterm.entity.Tuition;

import java.util.List;
import java.util.UUID;

public interface TuitionService {
    Tuition createTuition(CreateTuitionRequest request);
    TuitionResponse getTuitionById(UUID id);
    List<TuitionResponse> getTuitionByStudentCode(String studentCode);
    TransactionResponse initTuitionPayment(TuitionPaymentRequest request);
}
