package com.fashionstore.servlet;

import com.fashionstore.dao.PendingRegistrationDAO;
import com.fashionstore.dao.UserDAO;
import com.fashionstore.dao.impl.PendingRegistrationDAOImpl;
import com.fashionstore.dao.impl.UserDAOImpl;
import com.fashionstore.model.PendingRegistration;
import com.fashionstore.util.EmailService;
import com.fashionstore.util.OtpUtil;
import com.fashionstore.util.PasswordUtil;
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

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

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
        if (session != null && session.getAttribute("user") != null) {
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }
        request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String gender = request.getParameter("gender");
        String address = request.getParameter("address");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        // Validation
        StringBuilder errors = new StringBuilder();
        if (fullName == null || fullName.trim().isEmpty()) {
            errors.append("Full name is required. ");
        }
        if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            errors.append("A valid email address is required. ");
        }
        if (password == null || password.length() < 6) {
            errors.append("Password must be at least 6 characters long. ");
        } else if (!password.equals(confirmPassword)) {
            errors.append("Passwords do not match. ");
        }

        if (errors.length() > 0) {
            request.setAttribute("error", errors.toString().trim());
            request.setAttribute("fullName", fullName);
            request.setAttribute("email", email);
            request.setAttribute("phone", phone);
            request.setAttribute("gender", gender);
            request.setAttribute("address", address);
            request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
            return;
        }

        String normalizedEmail = email.trim().toLowerCase();

        try {
            // Check if user already exists
            if (userDAO.existsByEmail(normalizedEmail)) {
                request.setAttribute("error", "An account with this email already exists. Please log in.");
                request.setAttribute("fullName", fullName);
                request.setAttribute("phone", phone);
                request.setAttribute("address", address);
                request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
                return;
            }

            // Secure password hashing
            String hashedPassword = PasswordUtil.hashPassword(password);

            // Generate secure 6-digit OTP and its hash
            String plainOtp = OtpUtil.generateOtp();
            String otpHash = OtpUtil.hashOtp(normalizedEmail, plainOtp);

            // Attempt to send email via Gmail SMTP FIRST
            try {
                EmailService.sendOtpEmail(normalizedEmail, fullName.trim(), plainOtp);
            } catch (IllegalStateException e) {
                request.setAttribute("error", "Email service configuration is missing on the server. Please check FASHIONSTORE_GMAIL settings.");
                request.setAttribute("fullName", fullName);
                request.setAttribute("email", email);
                request.setAttribute("phone", phone);
                request.setAttribute("gender", gender);
                request.setAttribute("address", address);
                request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
                return;
            } catch (MessagingException e) {
                request.setAttribute("error", "Failed to send verification email. Please check your email address and try again.");
                request.setAttribute("fullName", fullName);
                request.setAttribute("email", email);
                request.setAttribute("phone", phone);
                request.setAttribute("gender", gender);
                request.setAttribute("address", address);
                request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
                return;
            }

            // Once email is sent, save/update pending registration securely
            long now = System.currentTimeMillis();
            PendingRegistration pending = new PendingRegistration(
                    normalizedEmail,
                    fullName.trim(),
                    phone != null ? phone.trim() : "",
                    hashedPassword,
                    gender != null ? gender.trim() : "Other",
                    address != null ? address.trim() : "",
                    otpHash,
                    0,
                    new Timestamp(now),
                    new Timestamp(now + PendingRegistration.EXPIRY_MILLIS),
                    new Timestamp(now)
            );

            pendingRegistrationDAO.saveOrUpdate(pending);

            // Store pending email in session for verify page
            HttpSession session = request.getSession();
            session.setAttribute("pendingEmail", normalizedEmail);
            session.setAttribute("flashInfo", "A 6-digit verification code has been sent to " + OtpUtil.maskEmail(normalizedEmail));

            response.sendRedirect(request.getContextPath() + "/verify-otp");

        } catch (SQLException e) {
            throw new ServletException("Database error during registration", e);
        }
    }
}
