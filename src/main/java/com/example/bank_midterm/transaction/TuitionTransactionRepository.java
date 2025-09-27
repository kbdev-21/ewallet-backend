package com.example.bank_midterm.transaction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TuitionTransactionRepository extends JpaRepository<TuitionTransaction, UUID> {
}