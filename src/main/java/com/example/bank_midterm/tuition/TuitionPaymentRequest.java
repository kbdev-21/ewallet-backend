package com.example.bank_midterm.tuition;

import lombok.Data;

import java.util.UUID;

@Data
public class TuitionPaymentRequest {
    UUID payerId;
    UUID tuitionId;
}
