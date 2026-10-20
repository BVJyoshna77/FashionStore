package com.fashionstore.servlet;

import com.fashionstore.dao.PendingRegistrationDAO;
import com.fashionstore.dao.impl.PendingRegistrationDAOImpl;
import com.fashionstore.model.PendingRegistration;
import com.fashionstore.util.EmailService;
import com.fashionstore.util.OtpUtil;
import jakarta.mail.MessagingException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Optional;

@WebServlet("/resend-otp")
public class ResendOtpServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private PendingRegistrationDAO pendingRegistrationDAO;

    @Override
    public void init() throws ServletException {
        pendingRegistrationDAO = new PendingRegistrationDAOImpl();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        String pendingEmail = null;

        if (session != null) {
            pendingEmail = (String) session.getAttribute("pendingEmail");
        }
        if (pendingEmail == null || pendingEmail.trim().isEmpty()) {
            pendingEmail = request.getParameter("email");
        }

        if (pendingEmail == null || pendingEmail.trim().isEmpty()) {
            if (session != null) {
                session.setAttribute("flashError", "Verification session expired. Please register again.");
            }
            response.sendRedirect(request.getContextPath() + "/register");
            return;
        }

        String normalizedEmail = pendingEmail.trim().toLowerCase();

        try {
            Optional<PendingRegistration> pendingOpt = pendingRegistrationDAO.findByEmail(normalizedEmail);
            if (pendingOpt.isEmpty()) {
                if (session != null) {
                    session.setAttribute("flashError", "No pending registration found or session expired. Please register again.");
                }
                response.sendRedirect(request.getContextPath() + "/register");
                return;
            }

            PendingRegistration pending = pendingOpt.get();

            // Enforce 60-second cooldown
            if (!pending.canResend()) {
                int remainingSeconds = pending.getRemainingCooldownSeconds();
                if (session != null) {
                    session.setAttribute("flashError", "Please wait " + remainingSeconds + " second"
                            + (remainingSeconds == 1 ? "" : "s") + " before requesting a new verification code.");
                }
                response.sendRedirect(request.getContextPath() + "/verify-otp");
                return;
            }

            // Generate new 6-digit OTP and compute hash
            String plainOtp = OtpUtil.generateOtp();
            String otpHash = OtpUtil.hashOtp(normalizedEmail, plainOtp);

            // Attempt to send email via Gmail SMTP
            try {
                EmailService.sendOtpEmail(normalizedEmail, pending.getFullName(), plainOtp);
            } catch (IllegalStateException e) {
                if (session != null) {
                    session.setAttribute("flashError", "Email service configuration is missing on the server. Please check FASHIONSTORE_GMAIL settings.");
                }
                response.sendRedirect(request.getContextPath() + "/verify-otp");
                return;
            } catch (MessagingException e) {
                if (session != null) {
                    session.setAttribute("flashError", "Failed to send verification email. Please check your connection and try again.");
                }
                response.sendRedirect(request.getContextPath() + "/verify-otp");
                return;
            }

            // Update pending registration in database
            long now = System.currentTimeMillis();
            Timestamp expiresAt = new Timestamp(now + PendingRegistration.EXPIRY_MILLIS);
            Timestamp lastSentAt = new Timestamp(now);

            pendingRegistrationDAO.updateOtpAndResetAttempts(normalizedEmail, otpHash, expiresAt, lastSentAt);

            if (session != null) {
                session.setAttribute("flashSuccess", "A new 6-digit verification code has been sent to " + OtpUtil.maskEmail(normalizedEmail));
            }
            response.sendRedirect(request.getContextPath() + "/verify-otp");

        } catch (SQLException e) {
            throw new ServletException("Database error during OTP resend", e);
        }
    }
}
