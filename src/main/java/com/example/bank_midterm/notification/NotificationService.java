package com.example.bank_midterm.notification;

public interface NotificationService {
    void sendEmail(String toEmail, String subject, String body);
}
