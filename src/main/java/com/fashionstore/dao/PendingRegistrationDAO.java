package com.fashionstore.dao;

import com.fashionstore.model.PendingRegistration;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Optional;

public interface PendingRegistrationDAO {

    /**
     * Inserts or replaces a pending registration record by email.
     */
    boolean saveOrUpdate(PendingRegistration pending) throws SQLException;

    /**
     * Finds pending registration by email.
     */
    Optional<PendingRegistration> findByEmail(String email) throws SQLException;

    /**
     * Increments the verification attempts counter for the given email.
     */
    boolean incrementAttempts(String email) throws SQLException;

    /**
     * Updates OTP hash, expiresAt, and lastSentAt when resending OTP, resetting attempts to 0.
     */
    boolean updateOtpAndResetAttempts(String email, String otpHash, Timestamp expiresAt, Timestamp lastSentAt) throws SQLException;

    /**
     * Deletes the pending registration after successful verification or when cancelled.
     */
    boolean deleteByEmail(String email) throws SQLException;
}
