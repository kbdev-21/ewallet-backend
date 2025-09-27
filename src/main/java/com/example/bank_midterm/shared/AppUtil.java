package com.example.bank_midterm.shared;

import org.springframework.stereotype.Component;

import java.util.UUID;

public class AppUtil {
    public static boolean isUUID(String str) {
        if (str == null) return false;
        try {
            UUID.fromString(str);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
