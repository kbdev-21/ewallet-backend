package com.example.bank_midterm.dto.tuition;

import com.example.bank_midterm.entity.Tuition;
import com.example.bank_midterm.entity.User;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Data
public class TuitionResponse {
    private UUID id;
    private String studentCode;
    private String schoolName;
    private String title;
    private BigDecimal amount;
    private Tuition.Status status;
    private Date createdAt;
    private Date expiredAt;
}
