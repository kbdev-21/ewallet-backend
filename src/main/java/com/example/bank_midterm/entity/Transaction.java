package com.example.bank_midterm.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(
    name = "transactions",
    indexes = {
        @Index(name = "index_transaction_sender_id", columnList = "sender_id"),
        @Index(name = "index_transaction_receiver_id", columnList = "receiver_id")
    }
)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
    public enum Status {
        PENDING, COMPLETED, CANCELLED, FAILED
    }

    public enum Type {
        TRANSFER, TUITION_PAYMENT
    }

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.EAGER)
    private User sender;

    @ManyToOne(fetch = FetchType.EAGER)
    private User receiver;

    private String message;

    private BigDecimal amount;

    private BigDecimal fee;

    @Enumerated(EnumType.STRING)
    private Type type;

    @Enumerated(EnumType.STRING)
    private Status status;

    private UUID otpId;

    private Date initAt;

    private Date completedAt;
}
