package com.fashionstore.servlet;

import com.fashionstore.dao.UserDAO;
import com.fashionstore.dao.impl.UserDAOImpl;
import com.fashionstore.model.CartItemDto;
import com.fashionstore.model.CartSession;
import com.fashionstore.model.User;
import com.fashionstore.util.DBConnection;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

@WebServlet("/checkout")
public class CheckoutServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        userDAO = new UserDAOImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        CartSession cart = (CartSession) session.getAttribute("cart");

        if (cart == null || cart.isEmpty()) {
            session.setAttribute("flashError", "Your cart is empty. Please add items before checking out.");
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }

        User user = (User) session.getAttribute("user");
        if (user == null) {
            // Optional: redirect to login or allow guest checkout
            // We allow guest checkout or quick login
            session.setAttribute("flashInfo", "You can log in to auto-fill your delivery details, or continue as guest below.");
        }

        request.getRequestDispatcher("/WEB-INF/views/checkout.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        CartSession cart = (CartSession) session.getAttribute("cart");

        if (cart == null || cart.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }

        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        String city = request.getParameter("city");
        String state = request.getParameter("state");
        String postalCode = request.getParameter("postalCode");
        String paymentMethod = request.getParameter("paymentMethod");

        if (fullName == null || fullName.trim().isEmpty()
                || phone == null || phone.trim().isEmpty()
                || address == null || address.trim().isEmpty()
                || city == null || city.trim().isEmpty()
                || postalCode == null || postalCode.trim().isEmpty()) {
            request.setAttribute("error", "Please fill in all required delivery details.");
            request.getRequestDispatcher("/WEB-INF/views/checkout.jsp").forward(request, response);
            return;
        }

        if (paymentMethod == null || paymentMethod.trim().isEmpty()) {
            paymentMethod = "Cash on Delivery";
        }

        String fullAddress = fullName.trim() + ", " + address.trim() + ", " + city.trim()
                + ", " + (state != null ? state.trim() : "") + " - " + postalCode.trim()
                + " (Phone: " + phone.trim() + ")";

        User currentUser = (User) session.getAttribute("user");
        int userId = 1; // default to first customer if guest

        try {
            if (currentUser != null) {
                userId = currentUser.getUserId();
            } else if (email != null && !email.trim().isEmpty()) {
                Optional<User> existing = userDAO.findByEmail(email.trim().toLowerCase());
                if (existing.isPresent()) {
                    userId = existing.get().getUserId();
                } else {
                    // Create guest customer
                    User guest = new User();
                    guest.setFullName(fullName.trim());
                    guest.setEmail(email.trim().toLowerCase());
                    guest.setPhone(phone.trim());
                    guest.setAddress(fullAddress);
                    guest.setPassword("Guest_" + System.currentTimeMillis());
                    guest.setRole("CUSTOMER");
                    userDAO.save(guest);
                    Optional<User> saved = userDAO.findByEmail(guest.getEmail());
                    if (saved.isPresent()) {
                        userId = saved.get().getUserId();
                    }
                }
            }
        } catch (SQLException e) {
            userId = 1; // fallback
        }

        // Transactional Order Placement
        int generatedOrderId = -1;
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); // Begin transaction

            // 1. Insert into orders
            String orderSql = "INSERT INTO orders (user_id, total_amount, payment_method, order_status, delivery_address) "
                    + "VALUES (?, ?, ?, 'PLACED', ?)";
            try (PreparedStatement orderPs = conn.prepareStatement(orderSql, Statement.RETURN_GENERATED_KEYS)) {
                orderPs.setInt(1, userId);
                orderPs.setBigDecimal(2, cart.getTotalAmount());
                orderPs.setString(3, paymentMethod);
                orderPs.setString(4, fullAddress);
                orderPs.executeUpdate();

                try (ResultSet rs = orderPs.getGeneratedKeys()) {
                    if (rs.next()) {
                        generatedOrderId = rs.getInt(1);
                    }
                }
            }

            if (generatedOrderId == -1) {
                throw new SQLException("Failed to retrieve generated order ID.");
            }

            // 2. Insert into order_items
            String itemSql = "INSERT INTO order_items (order_id, product_id, product_name, size_label, quantity, unit_price, subtotal) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement itemPs = conn.prepareStatement(itemSql)) {
                for (CartItemDto item : cart.getItems()) {
                    itemPs.setInt(1, generatedOrderId);
                    itemPs.setInt(2, item.getProductId());
                    itemPs.setString(3, item.getProductName());
                    itemPs.setString(4, item.getSizeLabel());
                    itemPs.setInt(5, item.getQuantity());
                    itemPs.setBigDecimal(6, item.getUnitPrice());
                    itemPs.setBigDecimal(7, item.getSubtotal());
                    itemPs.addBatch();
                }
                itemPs.executeBatch();
            }

            // Commit transaction
            conn.commit();

            // Clear session cart
            cart.clear();
            session.setAttribute("flashSuccess", "Order placed successfully!");
            response.sendRedirect(request.getContextPath() + "/order-confirmation?orderId=" + generatedOrderId);

        } catch (Exception e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ignored) {}
            }
            throw new ServletException("Order transaction failed: " + e.getMessage(), e);
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException ignored) {}
            }
        }
    }
}
