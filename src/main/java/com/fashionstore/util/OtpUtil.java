package com.fashionstore.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HexFormat;

public class OtpUtil {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private static final String DEFAULT_PEPPER = "FashionStore_Secure_Otp_Pepper_Default_2026";
    private static volatile String cachedPepper = null;

    /**
     * Retrieves the secret pepper from environment, system properties, or registry.
     * Caches the value after first retrieval for maximum performance.
     */
    public static String getPepper() {
        if (cachedPepper != null) {
            return cachedPepper;
        }
        String pepper = EmailService.resolveConfig("FASHIONSTORE_OTP_PEPPER");
        if (pepper == null || pepper.trim().isEmpty()) {
            cachedPepper = DEFAULT_PEPPER;
        } else {
            cachedPepper = pepper.trim();
        }
        return cachedPepper;
    }

    /**
     * Generates a cryptographically secure 6-digit numeric OTP (100000 - 999999).
     */
    public static String generateOtp() {
        int code = 100000 + SECURE_RANDOM.nextInt(900000);
        return String.valueOf(code);
    }

    /**
     * Generates a SHA-256 hash of the OTP salted with the user's email and secret pepper.
     * Never stores or exposes the plain OTP.
     */
    public static String hashOtp(String email, String otp) {
        if (email == null || otp == null) {
            throw new IllegalArgumentException("Email and OTP cannot be null");
        }
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            String combined = email.trim().toLowerCase() + ":" + otp.trim() + ":" + getPepper();
            byte[] hash = digest.digest(combined.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not available", e);
        }
    }

    /**
     * Verifies the submitted plain OTP against the stored OTP hash using constant-time comparison.
     */
    public static boolean verifyOtp(String email, String plainOtp, String storedHash) {
        if (email == null || plainOtp == null || storedHash == null) {
            return false;
        }
        String calculatedHash = hashOtp(email, plainOtp);
        byte[] a = calculatedHash.getBytes(StandardCharsets.UTF_8);
        byte[] b = storedHash.getBytes(StandardCharsets.UTF_8);
        return MessageDigest.isEqual(a, b);
    }

    /**
     * Masks an email address for safe display on the verification UI (e.g., j***a@gmail.com).
     */
    public static String maskEmail(String email) {
        if (email == null || !email.contains("@")) {
            return email;
        }
        int atIndex = email.indexOf('@');
        String name = email.substring(0, atIndex);
        String domain = email.substring(atIndex);

        if (name.length() <= 2) {
            return name.charAt(0) + "***" + domain;
        }
        return name.charAt(0) + "***" + name.charAt(name.length() - 1) + domain;
    }
}
