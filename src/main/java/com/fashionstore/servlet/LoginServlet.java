package com.fashionstore.servlet;

import com.fashionstore.dao.UserDAO;
import com.fashionstore.dao.impl.UserDAOImpl;
import com.fashionstore.model.User;
import com.fashionstore.util.PasswordUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        userDAO = new UserDAOImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String redirect = request.getParameter("redirect");
        if (redirect != null) {
            request.setAttribute("redirect", redirect);
        }

        request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String redirect = request.getParameter("redirect");

        if (email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            request.setAttribute("error", "Email and password are required.");
            request.setAttribute("email", email);
            request.setAttribute("redirect", redirect);
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
            return;
        }

        try {
            Optional<User> userOpt = userDAO.findByEmail(email.trim().toLowerCase());
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                if (PasswordUtil.checkPassword(password, user.getPassword())) {
                    HttpSession session = request.getSession();
                    session.setAttribute("user", user);
                    session.setAttribute("flashSuccess", "Welcome back, " + user.getFullName() + "!");

                    if (user.isAdmin() && (redirect == null || redirect.isEmpty())) {
                        response.sendRedirect(request.getContextPath() + "/admin");
                        return;
                    }

                    if (redirect != null && !redirect.trim().isEmpty() && !redirect.startsWith("http")) {
                        response.sendRedirect(request.getContextPath() + redirect);
                    } else {
                        response.sendRedirect(request.getContextPath() + "/");
                    }
                    return;
                }
            }

            request.setAttribute("error", "Invalid email or password.");
            request.setAttribute("email", email);
            request.setAttribute("redirect", redirect);
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);

        } catch (SQLException e) {
            throw new ServletException("Database error during login", e);
        }
    }
}
