package com.example.bank_midterm.repository;

import com.example.bank_midterm.entity.Otp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OtpRepository extends JpaRepository<Otp, UUID> {

}
