package com.example.bank_midterm.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(
    name = "tuition_transaction",
    indexes = {
        @Index(name = "index_tuition_id", columnList = "tuition_id")
    }
)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TuitionTransaction {
    @Id
    private UUID transactionId;

    private UUID tuitionId;
}
