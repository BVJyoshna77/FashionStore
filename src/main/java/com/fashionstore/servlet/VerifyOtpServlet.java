package com.fashionstore.servlet;

import com.fashionstore.dao.PendingRegistrationDAO;
import com.fashionstore.dao.UserDAO;
import com.fashionstore.dao.impl.PendingRegistrationDAOImpl;
import com.fashionstore.dao.impl.UserDAOImpl;
import com.fashionstore.model.PendingRegistration;
import com.fashionstore.model.User;
import com.fashionstore.util.OtpUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

@WebServlet("/verify-otp")
public class VerifyOtpServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private UserDAO userDAO;
    private PendingRegistrationDAO pendingRegistrationDAO;

    @Override
    public void init() throws ServletException {
        userDAO = new UserDAOImpl();
        pendingRegistrationDAO = new PendingRegistrationDAOImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
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
                session.setAttribute("flashError", "No pending registration found. Please register first.");
            }
            response.sendRedirect(request.getContextPath() + "/register");
            return;
        }

        String normalizedEmail = pendingEmail.trim().toLowerCase();

        try {
            Optional<PendingRegistration> pendingOpt = pendingRegistrationDAO.findByEmail(normalizedEmail);
            if (pendingOpt.isEmpty()) {
                if (session != null) {
                    session.setAttribute("flashError", "No pending registration found or verification already completed. Please log in.");
                }
                response.sendRedirect(request.getContextPath() + "/login");
                return;
            }

            PendingRegistration pending = pendingOpt.get();
            request.setAttribute("pendingEmail", normalizedEmail);
            request.setAttribute("maskedEmail", OtpUtil.maskEmail(normalizedEmail));
            request.setAttribute("isExpired", pending.isExpired());
            request.setAttribute("isMaxAttemptsExceeded", pending.isMaxAttemptsExceeded());
            request.setAttribute("remainingAttempts", pending.getRemainingAttempts());
            request.setAttribute("cooldownSeconds", pending.getRemainingCooldownSeconds());

            request.getRequestDispatcher("/WEB-INF/views/verify-otp.jsp").forward(request, response);

        } catch (SQLException e) {
            throw new ServletException("Database error loading pending verification", e);
        }
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
        String otp = request.getParameter("otp");

        // Validate 6 digits
        if (otp == null || !otp.trim().matches("^[0-9]{6}$")) {
            request.setAttribute("error", "Please enter a valid 6-digit numeric verification code.");
            prepareViewAttributes(request, normalizedEmail);
            request.getRequestDispatcher("/WEB-INF/views/verify-otp.jsp").forward(request, response);
            return;
        }

        try {
            Optional<PendingRegistration> pendingOpt = pendingRegistrationDAO.findByEmail(normalizedEmail);
            if (pendingOpt.isEmpty()) {
                if (session != null) {
                    session.setAttribute("flashError", "This verification session is invalid or already used. Please log in.");
                }
                response.sendRedirect(request.getContextPath() + "/login");
                return;
            }

            PendingRegistration pending = pendingOpt.get();

            // Check expiration
            if (pending.isExpired()) {
                request.setAttribute("error", "Your OTP verification code has expired (valid for 5 minutes). Please click 'Resend Code' below.");
                prepareViewAttributes(request, pending);
                request.getRequestDispatcher("/WEB-INF/views/verify-otp.jsp").forward(request, response);
                return;
            }

            // Check maximum attempts
            if (pending.isMaxAttemptsExceeded()) {
                request.setAttribute("error", "Maximum verification attempts (5) exceeded for this code. Please request a new code.");
                prepareViewAttributes(request, pending);
                request.getRequestDispatcher("/WEB-INF/views/verify-otp.jsp").forward(request, response);
                return;
            }

            // Verify OTP hash
            boolean isMatch = OtpUtil.verifyOtp(normalizedEmail, otp.trim(), pending.getOtpHash());

            if (!isMatch) {
                pendingRegistrationDAO.incrementAttempts(normalizedEmail);
                int remaining = Math.max(0, PendingRegistration.MAX_ATTEMPTS - (pending.getAttempts() + 1));

                if (remaining == 0) {
                    request.setAttribute("error", "Invalid verification code. You have reached the maximum of 5 attempts. Please request a new code.");
                } else {
                    request.setAttribute("error", "Invalid verification code. You have " + remaining + " attempt" + (remaining == 1 ? "" : "s") + " remaining.");
                }

                pending.setAttempts(pending.getAttempts() + 1);
                prepareViewAttributes(request, pending);
                request.getRequestDispatcher("/WEB-INF/views/verify-otp.jsp").forward(request, response);
                return;
            }

            // Valid OTP! Guard against duplicate email before creating user
            if (userDAO.existsByEmail(normalizedEmail)) {
                pendingRegistrationDAO.deleteByEmail(normalizedEmail);
                if (session != null) {
                    session.removeAttribute("pendingEmail");
                    session.setAttribute("flashInfo", "An account with this email already exists. Please log in.");
                }
                response.sendRedirect(request.getContextPath() + "/login");
                return;
            }

            // Create customer in existing users table
            User newUser = new User();
            newUser.setFullName(pending.getFullName());
            newUser.setEmail(pending.getEmail());
            newUser.setPhone(pending.getPhone());
            newUser.setPassword(pending.getPasswordHash()); // Password was already hashed via PasswordUtil
            newUser.setGender(pending.getGender());
            newUser.setAddress(pending.getAddress());
            newUser.setRole("CUSTOMER");

            boolean saved = userDAO.save(newUser);
            if (saved) {
                // Delete pending registration to prevent reuse
                pendingRegistrationDAO.deleteByEmail(normalizedEmail);

                if (session != null) {
                    session.removeAttribute("pendingEmail");
                    session.setAttribute("flashSuccess", "Registration and email verification completed successfully! You can now log in.");
                }
                response.sendRedirect(request.getContextPath() + "/login");
            } else {
                request.setAttribute("error", "Failed to complete account registration. Please try again.");
                prepareViewAttributes(request, pending);
                request.getRequestDispatcher("/WEB-INF/views/verify-otp.jsp").forward(request, response);
            }

        } catch (SQLException e) {
            throw new ServletException("Database error during OTP verification", e);
        }
    }

    private void prepareViewAttributes(HttpServletRequest request, PendingRegistration pending) {
        request.setAttribute("pendingEmail", pending.getEmail());
        request.setAttribute("maskedEmail", OtpUtil.maskEmail(pending.getEmail()));
        request.setAttribute("isExpired", pending.isExpired());
        request.setAttribute("isMaxAttemptsExceeded", pending.isMaxAttemptsExceeded());
        request.setAttribute("remainingAttempts", pending.getRemainingAttempts());
        request.setAttribute("cooldownSeconds", pending.getRemainingCooldownSeconds());
    }

    private void prepareViewAttributes(HttpServletRequest request, String email) {
        try {
            Optional<PendingRegistration> opt = pendingRegistrationDAO.findByEmail(email);
            if (opt.isPresent()) {
                prepareViewAttributes(request, opt.get());
                return;
            }
        } catch (SQLException ignored) {
        }
        request.setAttribute("pendingEmail", email);
        request.setAttribute("maskedEmail", OtpUtil.maskEmail(email));
        request.setAttribute("remainingAttempts", 5);
        request.setAttribute("cooldownSeconds", 0);
    }
}
