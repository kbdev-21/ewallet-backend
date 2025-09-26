package com.example.bank_midterm.service;

import com.example.bank_midterm.entity.Otp;

import java.util.UUID;

public interface OtpService {
    Otp createOtp(long durationInMillis);
    Otp validateOtp(UUID otpId, String confirmOtpCode);
}
