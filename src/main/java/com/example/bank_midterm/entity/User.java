package com.example.bank_midterm.entity;

import com.example.bank_midterm.exception.CustomException;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
public class User {
    public enum Role {
        ADMIN, USER
    }

    @Id
    private UUID id;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String phoneNum;

    private String firstName;

    private String lastName;

    private BigDecimal balance;

    private String hashedPassword;

    private Date createdAt;

    private Date updatedAt;

    @Version
    private Long version;
}
