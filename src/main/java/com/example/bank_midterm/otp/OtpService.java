package com.example.bank_midterm.otp;

import java.util.UUID;

public interface OtpService {
    Otp createOtp(long durationInMillis);
    Otp validateOtp(UUID otpId, String confirmOtpCode);
}
