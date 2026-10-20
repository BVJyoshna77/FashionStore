package com.fashionstore.model;

import java.sql.Timestamp;

public class PendingRegistration {

    public static final int MAX_ATTEMPTS = 5;
    public static final long EXPIRY_MILLIS = 5 * 60 * 1000L; // 5 minutes
    public static final long COOLDOWN_MILLIS = 60 * 1000L;    // 60 seconds

    private String email;
    private String fullName;
    private String phone;
    private String passwordHash;
    private String gender;
    private String address;
    private String otpHash;
    private int attempts;
    private Timestamp createdAt;
    private Timestamp expiresAt;
    private Timestamp lastSentAt;

    public PendingRegistration() {
    }

    public PendingRegistration(String email, String fullName, String phone,
                               String passwordHash, String gender, String address,
                               String otpHash, int attempts, Timestamp createdAt,
                               Timestamp expiresAt, Timestamp lastSentAt) {
        this.email = email;
        this.fullName = fullName;
        this.phone = phone;
        this.passwordHash = passwordHash;
        this.gender = gender;
        this.address = address;
        this.otpHash = otpHash;
        this.attempts = attempts;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.lastSentAt = lastSentAt;
    }

    public boolean isExpired() {
        if (expiresAt == null) {
            return true;
        }
        return System.currentTimeMillis() > expiresAt.getTime();
    }

    public boolean isMaxAttemptsExceeded() {
        return attempts >= MAX_ATTEMPTS;
    }

    public int getRemainingAttempts() {
        return Math.max(0, MAX_ATTEMPTS - attempts);
    }

    public boolean canResend() {
        if (lastSentAt == null) {
            return true;
        }
        return (System.currentTimeMillis() - lastSentAt.getTime()) >= COOLDOWN_MILLIS;
    }

    public int getRemainingCooldownSeconds() {
        if (lastSentAt == null) {
            return 0;
        }
        long elapsed = System.currentTimeMillis() - lastSentAt.getTime();
        if (elapsed >= COOLDOWN_MILLIS) {
            return 0;
        }
        return (int) Math.ceil((COOLDOWN_MILLIS - elapsed) / 1000.0);
    }

    // Getters and Setters

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOtpHash() {
        return otpHash;
    }

    public void setOtpHash(String otpHash) {
        this.otpHash = otpHash;
    }

    public int getAttempts() {
        return attempts;
    }

    public void setAttempts(int attempts) {
        this.attempts = attempts;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Timestamp expiresAt) {
        this.expiresAt = expiresAt;
    }

    public Timestamp getLastSentAt() {
        return lastSentAt;
    }

    public void setLastSentAt(Timestamp lastSentAt) {
        this.lastSentAt = lastSentAt;
    }
}
