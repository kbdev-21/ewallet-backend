package com.example.bank_midterm.otp;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OtpRepository extends JpaRepository<Otp, UUID> {

}
