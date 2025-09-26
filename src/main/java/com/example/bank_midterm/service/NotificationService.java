package com.example.bank_midterm.service;

public interface NotificationService {
    void sendEmail(String toEmail, String subject, String body);
}
