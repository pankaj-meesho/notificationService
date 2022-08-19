package com.example.notificationservice.utils;

public class phoneNumberValidator {
    public static boolean isValidNumber(String phoneNumber) {
        if (phoneNumber.length() != 13) {
            return false;
        } else if (phoneNumber.charAt(0) != '+') {
            return false;
        }
        return true;
    }
}
