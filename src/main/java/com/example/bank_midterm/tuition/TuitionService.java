package com.example.bank_midterm.tuition;

import com.example.bank_midterm.transaction.TransactionResponse;

import java.util.List;
import java.util.UUID;

public interface TuitionService {
    Tuition createTuition(CreateTuitionRequest request);
    TuitionResponse getTuitionById(UUID id);
    List<TuitionResponse> getTuitionByStudentCode(String studentCode);
    TransactionResponse initTuitionPayment(TuitionPaymentRequest request);
}
