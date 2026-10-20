package com.fashionstore.servlet;

import com.fashionstore.dao.UserDAO;
import com.fashionstore.dao.impl.UserDAOImpl;
import com.fashionstore.model.User;
import com.fashionstore.util.DBConnection;
import com.fashionstore.util.PasswordUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;

@WebServlet("/admin-setup")
public class AdminSetupServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    // Secure hashed verification token for default master authorization
    private static final String DEFAULT_ADMIN_KEY_HASH =
            "zWlE1ud04P1g3xzOs5MO1Q==:ztNrWVpHvZ50Li7LeGAyN8Z/1Nrstgi/3qERjCalU54=";

    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        userDAO = new UserDAOImpl();
    }

    private boolean verifyMasterKey(String inputKey) {
        if (inputKey == null || inputKey.trim().isEmpty()) {
            return false;
        }
        String trimmed = inputKey.trim();
        String envKey = System.getenv("ADMIN_SECRET_KEY");
        if (envKey != null && !envKey.trim().isEmpty()) {
            return trimmed.equals(envKey.trim());
        }
        String propKey = System.getProperty("admin.secret.key");
        if (propKey != null && !propKey.trim().isEmpty()) {
            return trimmed.equals(propKey.trim());
        }
        return PasswordUtil.checkPassword(trimmed, DEFAULT_ADMIN_KEY_HASH);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/admin-setup.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String secretKey = request.getParameter("secretKey");
        String email = request.getParameter("email");
        String fullName = request.getParameter("fullName");
        String password = request.getParameter("password");

        HttpSession session = request.getSession();

        if (!verifyMasterKey(secretKey)) {
            request.setAttribute("error", "Invalid Admin Security Key. Authorization failed.");
            request.setAttribute("email", email);
            request.setAttribute("fullName", fullName);
            request.getRequestDispatcher("/WEB-INF/views/admin-setup.jsp").forward(request, response);
            return;
        }

        if (email == null || email.trim().isEmpty()) {
            request.setAttribute("error", "Email address is required.");
            request.getRequestDispatcher("/WEB-INF/views/admin-setup.jsp").forward(request, response);
            return;
        }

        String targetEmail = email.trim().toLowerCase();

        try {
            Optional<User> existingUserOpt = userDAO.findByEmail(targetEmail);
            if (existingUserOpt.isPresent()) {
                User user = existingUserOpt.get();
                // Promote existing user to ADMIN
                try (Connection conn = DBConnection.getConnection();
                     PreparedStatement ps = conn.prepareStatement("UPDATE users SET role = 'ADMIN' WHERE user_id = ?")) {
                    ps.setInt(1, user.getUserId());
                    ps.executeUpdate();
                }

                // If password was also provided, update it
                if (password != null && !password.trim().isEmpty()) {
                    userDAO.updatePassword(user.getUserId(), PasswordUtil.hashPassword(password.trim()));
                }

                // If the currently logged-in user is this user, refresh their session
                User sessionUser = (User) session.getAttribute("user");
                if (sessionUser != null && sessionUser.getUserId() == user.getUserId()) {
                    sessionUser.setRole("ADMIN");
                    session.setAttribute("user", sessionUser);
                }

                session.setAttribute("flashSuccess", "Success: User '" + targetEmail + "' has been granted Administrator (ADMIN) access!");
                response.sendRedirect(request.getContextPath() + "/admin");
                return;
            } else {
                // Create a brand new admin user
                if (password == null || password.trim().length() < 6) {
                    request.setAttribute("error", "For a new admin account, password must be at least 6 characters.");
                    request.setAttribute("email", email);
                    request.setAttribute("fullName", fullName);
                    request.getRequestDispatcher("/WEB-INF/views/admin-setup.jsp").forward(request, response);
                    return;
                }

                User newAdmin = new User();
                newAdmin.setFullName((fullName != null && !fullName.trim().isEmpty()) ? fullName.trim() : "Store Administrator");
                newAdmin.setEmail(targetEmail);
                newAdmin.setPhone("9876543210");
                newAdmin.setGender("Other");
                newAdmin.setAddress("FashionStore HQ, Hyderabad");
                newAdmin.setPassword(PasswordUtil.hashPassword(password.trim()));
                newAdmin.setRole("ADMIN");

                userDAO.save(newAdmin);

                session.setAttribute("flashSuccess", "New Administrator account created successfully for '" + targetEmail + "'. Please sign in.");
                response.sendRedirect(request.getContextPath() + "/login?redirect=/admin");
                return;
            }

        } catch (SQLException e) {
            throw new ServletException("Database error during admin assignment", e);
        }
    }
}
