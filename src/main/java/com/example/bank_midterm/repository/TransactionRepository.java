package com.example.bank_midterm.repository;

import com.example.bank_midterm.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    List<Transaction> findBySenderId(UUID senderId);
    List<Transaction> findByReceiverId(UUID receiverId);
}
