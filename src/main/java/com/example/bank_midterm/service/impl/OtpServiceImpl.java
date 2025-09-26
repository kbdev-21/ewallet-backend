package com.example.bank_midterm.service.impl;

import com.example.bank_midterm.entity.Otp;
import com.example.bank_midterm.exception.CustomException;
import com.example.bank_midterm.repository.OtpRepository;
import com.example.bank_midterm.service.OtpService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Date;
import java.util.UUID;

@Service
public class OtpServiceImpl implements OtpService {
    private final OtpRepository otpRepository;
    private final SecureRandom secureRandom = new SecureRandom();

    public OtpServiceImpl(OtpRepository otpRepository) {
        this.otpRepository = otpRepository;
    }

    public Otp createOtp(long durationInMillis) {
        Date now = new Date();
        Date expiredAt = new Date(now.getTime() + durationInMillis);

        Otp otp = new Otp();
        otp.setId(UUID.randomUUID());
        otp.setCode(generateRandomOtpCode());
        otp.setCreatedAt(now);
        otp.setExpiredAt(expiredAt);
        return otpRepository.save(otp);
    }

    public Otp validateOtp(UUID otpId, String confirmOtpCode) {
        Otp otp = otpRepository.findById(otpId).orElseThrow(
            () -> new CustomException(HttpStatus.NOT_FOUND, "OTP not existed")
        );

        Date now = new Date();
        if(now.after(otp.getExpiredAt())) {
            throw new CustomException(HttpStatus.CONFLICT, "Expired OTP");
        }

        if(confirmOtpCode.equals(otp.getCode())) {
            return otp;
        }
        else {
            throw new CustomException(HttpStatus.CONFLICT, "Invalid OTP Code");
        }
    }

    private String generateRandomOtpCode() {
        int length = 6;
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(secureRandom.nextInt(10));
        }
        return sb.toString();
    }
}
