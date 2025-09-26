package com.example.bank_midterm.repository;

import com.example.bank_midterm.entity.TuitionTransaction;
import com.example.bank_midterm.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
import java.util.List;

@Repository
public interface TuitionTransactionRepository extends JpaRepository<TuitionTransaction, UUID> {
}