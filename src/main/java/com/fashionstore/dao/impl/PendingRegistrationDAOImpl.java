package com.fashionstore.dao.impl;

import com.fashionstore.dao.PendingRegistrationDAO;
import com.fashionstore.model.PendingRegistration;
import com.fashionstore.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Optional;

public class PendingRegistrationDAOImpl implements PendingRegistrationDAO {

    @Override
    public boolean saveOrUpdate(PendingRegistration pending) throws SQLException {
        // Uses MySQL ON DUPLICATE KEY UPDATE so resubmissions overwrite pending registration
        String sql = "INSERT INTO pending_registrations "
                + "(email, full_name, phone, password_hash, gender, address, otp_hash, attempts, created_at, expires_at, last_sent_at) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) "
                + "ON DUPLICATE KEY UPDATE "
                + "full_name = VALUES(full_name), "
                + "phone = VALUES(phone), "
                + "password_hash = VALUES(password_hash), "
                + "gender = VALUES(gender), "
                + "address = VALUES(address), "
                + "otp_hash = VALUES(otp_hash), "
                + "attempts = VALUES(attempts), "
                + "expires_at = VALUES(expires_at), "
                + "last_sent_at = VALUES(last_sent_at)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, pending.getEmail().trim().toLowerCase());
            ps.setString(2, pending.getFullName());
            ps.setString(3, pending.getPhone());
            ps.setString(4, pending.getPasswordHash());
            ps.setString(5, pending.getGender());
            ps.setString(6, pending.getAddress());
            ps.setString(7, pending.getOtpHash());
            ps.setInt(8, pending.getAttempts());
            ps.setTimestamp(9, pending.getCreatedAt() != null ? pending.getCreatedAt() : new Timestamp(System.currentTimeMillis()));
            ps.setTimestamp(10, pending.getExpiresAt());
            ps.setTimestamp(11, pending.getLastSentAt());

            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public Optional<PendingRegistration> findByEmail(String email) throws SQLException {
        if (email == null) {
            return Optional.empty();
        }
        String sql = "SELECT * FROM pending_registrations WHERE email = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email.trim().toLowerCase());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    PendingRegistration pr = new PendingRegistration();
                    pr.setEmail(rs.getString("email"));
                    pr.setFullName(rs.getString("full_name"));
                    pr.setPhone(rs.getString("phone"));
                    pr.setPasswordHash(rs.getString("password_hash"));
                    pr.setGender(rs.getString("gender"));
                    pr.setAddress(rs.getString("address"));
                    pr.setOtpHash(rs.getString("otp_hash"));
                    pr.setAttempts(rs.getInt("attempts"));
                    pr.setCreatedAt(rs.getTimestamp("created_at"));
                    pr.setExpiresAt(rs.getTimestamp("expires_at"));
                    pr.setLastSentAt(rs.getTimestamp("last_sent_at"));
                    return Optional.of(pr);
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public boolean incrementAttempts(String email) throws SQLException {
        if (email == null) {
            return false;
        }
        String sql = "UPDATE pending_registrations SET attempts = attempts + 1 WHERE email = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email.trim().toLowerCase());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean updateOtpAndResetAttempts(String email, String otpHash, Timestamp expiresAt, Timestamp lastSentAt) throws SQLException {
        if (email == null) {
            return false;
        }
        String sql = "UPDATE pending_registrations SET otp_hash = ?, attempts = 0, expires_at = ?, last_sent_at = ? WHERE email = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, otpHash);
            ps.setTimestamp(2, expiresAt);
            ps.setTimestamp(3, lastSentAt);
            ps.setString(4, email.trim().toLowerCase());

            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean deleteByEmail(String email) throws SQLException {
        if (email == null) {
            return false;
        }
        String sql = "DELETE FROM pending_registrations WHERE email = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email.trim().toLowerCase());
            return ps.executeUpdate() > 0;
        }
    }
}
