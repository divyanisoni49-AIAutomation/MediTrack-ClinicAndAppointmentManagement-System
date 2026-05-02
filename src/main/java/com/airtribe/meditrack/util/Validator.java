package com.airtribe.meditrack.util;

public class Validator {

    private static String fieldName;

    public static void validateNonEmptyString(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Field [" + fieldName + "] cannot be null or empty.");
        }
    }

    public static void validatePositiveDouble(double value, String fieldName) {
        if (value <= 0) {
            throw new IllegalArgumentException("Field [" + fieldName + "] must be a positive number.");
        }
    }

    public static void validateNonNull(Object value, String fieldName) {
        if (value == null) {
            throw new IllegalArgumentException("Field [" + fieldName + "] cannot be null.");
        }
    }

    public static void validateEmail(String email) {
        if (email == null || !email.matches("^[\\w.-]+@[\\w.-]+\\.\\w+$")) {
            throw new IllegalArgumentException("Field [" + fieldName + "] must be a valid email address.");
        }
    }

    public static void validatePhoneNumber(String phone, String fieldName) {
        if (phone == null || !phone.matches("^\\+?[0-9]{7,15}$")) {
            throw new IllegalArgumentException("Field [" + fieldName + "] must be a valid phone number.");
        }
    }

    public static void validateName(String name) {
        validateNonEmptyString(name, "name");
        if (!name.matches("^[a-zA-Z\\s]+$")) {
            throw new IllegalArgumentException("Field [name] can only contain letters and spaces.");
        }
    }

    public static void validatePhone(String phone) {
        validatePhoneNumber(phone, "phone");

    }

    public static void validateAge(int age) {
        if (age < 0 || age > 120) {
            throw new IllegalArgumentException("Field [age] must be between 0 and 120.");
        }
    }

    public static void validateFee(double consultationFee) {
        validatePositiveDouble(consultationFee, "consultationFee");

    }

    public static void validateExperience(int experienceYears) {
        if (experienceYears < 0 || experienceYears > 80) {
            throw new IllegalArgumentException("Field [experienceYears] must be between 0 and 80.");
        }
    }
}
