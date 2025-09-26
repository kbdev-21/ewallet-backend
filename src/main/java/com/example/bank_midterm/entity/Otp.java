package com.example.bank_midterm.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "otps")
@NoArgsConstructor
@Data
public class Otp {
    @Id
    private UUID id;

    private String code;

    private Date createdAt;

    private Date expiredAt;
}
