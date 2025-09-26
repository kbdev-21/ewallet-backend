package com.example.bank_midterm.dto.tuition;

import lombok.Data;

import java.util.UUID;

@Data
public class TuitionPaymentRequest {
    UUID payerId;
    UUID tuitionId;
}
