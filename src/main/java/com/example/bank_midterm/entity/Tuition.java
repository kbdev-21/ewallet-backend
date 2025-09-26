package com.example.bank_midterm.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "tuitions")
@Data
@NoArgsConstructor
public class Tuition {
    public enum Status {
        UNPAID, PAID
    }

    @Id
    private UUID id;

    private String studentCode;

    private String schoolName;

    private String title;

    private BigDecimal amount;

    @ManyToOne(fetch = FetchType.EAGER)
    private User receiver;

    @Enumerated(EnumType.STRING)
    private Status status;

    private Date createdAt;

    private Date expiredAt;

    private Date paidAt;
}
