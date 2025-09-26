package com.example.bank_midterm.controller;

import com.example.bank_midterm.dto.transaction.ConfirmTransactionRequest;
import com.example.bank_midterm.dto.transaction.TransactionResponse;
import com.example.bank_midterm.dto.tuition.CreateTuitionRequest;
import com.example.bank_midterm.dto.tuition.TuitionPaymentRequest;
import com.example.bank_midterm.dto.tuition.TuitionResponse;
import com.example.bank_midterm.entity.Tuition;
import com.example.bank_midterm.service.TuitionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class TuitionController {

    private final TuitionService tuitionService;

    public TuitionController(TuitionService tuitionService) {
        this.tuitionService = tuitionService;
    }

    @PostMapping("/api/tuition")
    public Tuition createTuition(@RequestBody CreateTuitionRequest request) {
        return tuitionService.createTuition(request);
    }

    @GetMapping("/api/tuition/{id}")
    public TuitionResponse getTuitionById(@PathVariable UUID id) {
        return tuitionService.getTuitionById(id);
    }

    @GetMapping("/api/tuition/by-studentcode/{studentCode}")
    public List<TuitionResponse> getTuitionsByStudentCode(@PathVariable String studentCode) {
        return tuitionService.getTuitionByStudentCode(studentCode);
    }

    @PostMapping("/api/tuition/payment")
    public TransactionResponse initTuitionPayment(@RequestBody TuitionPaymentRequest request) {
        return tuitionService.initTuitionPayment(request);
    }
}
