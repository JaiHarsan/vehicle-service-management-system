package com.vehicleservice.util;

import java.math.BigDecimal;
import java.util.regex.Pattern;

public class InputValidator {

    private static final Pattern PHONE_PATTERN = Pattern.compile("^[0-9]{10}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private static final Pattern REG_NO_PATTERN = Pattern.compile("^[A-Z]{2}-[0-9]{2}-[A-Z]{1,2}-[0-9]{4}$");

    public static boolean isValidName(String name) {
        return name != null && !name.trim().isEmpty() && name.trim().length() >= 2;
    }

    public static boolean isValidPhone(String phone) {
        return phone != null && PHONE_PATTERN.matcher(phone.trim()).matches();
    }

    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return true; // Optional field
        }
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    public static boolean isValidRegistrationNumber(String regNo) {
        if (regNo == null || regNo.trim().isEmpty()) {
            return false;
        }
        String formatted = regNo.trim().toUpperCase();
        return REG_NO_PATTERN.matcher(formatted).matches() || formatted.length() >= 6;
    }

    public static boolean isValidPositiveAmount(BigDecimal amount) {
        return amount != null && amount.compareTo(BigDecimal.ZERO) >= 0;
    }
}
