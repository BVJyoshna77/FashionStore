package com.fashionstore.servlet;

import com.fashionstore.dao.CategoryDAO;
import com.fashionstore.dao.OrderDAO;
import com.fashionstore.dao.OrderItemDAO;
import com.fashionstore.dao.ProductDAO;
import com.fashionstore.dao.UserDAO;
import com.fashionstore.dao.impl.CategoryDAOImpl;
import com.fashionstore.dao.impl.OrderDAOImpl;
import com.fashionstore.dao.impl.OrderItemDAOImpl;
import com.fashionstore.dao.impl.ProductDAOImpl;
import com.fashionstore.dao.impl.UserDAOImpl;
import com.fashionstore.model.Category;
import com.fashionstore.model.Order;
import com.fashionstore.model.OrderItem;
import com.fashionstore.model.Product;
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
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@WebServlet("/admin")
public class AdminServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private ProductDAO productDAO;
    private CategoryDAO categoryDAO;
    private OrderDAO orderDAO;
    private OrderItemDAO orderItemDAO;
    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        productDAO = new ProductDAOImpl();
        categoryDAO = new CategoryDAOImpl();
        orderDAO = new OrderDAOImpl();
        orderItemDAO = new OrderItemDAOImpl();
        userDAO = new UserDAOImpl();
    }

    private boolean checkAdmin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login?redirect=/admin");
            return false;
        }

        User user = (User) session.getAttribute("user");

        // Dynamically refresh user role from DB in case it was updated
        if (!user.isAdmin()) {
            try {
                Optional<User> freshOpt = userDAO.findById(user.getUserId());
                if (freshOpt.isPresent() && freshOpt.get().isAdmin()) {
                    user = freshOpt.get();
                    session.setAttribute("user", user);
                }
            } catch (SQLException ignored) {}
        }

        if (!user.isAdmin()) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            request.getRequestDispatcher("/WEB-INF/views/access-denied.jsp").forward(request, response);
            return false;
        }
        return true;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!checkAdmin(request, response)) return;

        String tab = request.getParameter("tab");
        if (tab == null || tab.trim().isEmpty()) {
            tab = "products";
        }

        try {
            List<Category> categories = categoryDAO.findAll();
            request.setAttribute("categories", categories);
            request.setAttribute("currentTab", tab);

            if ("orders".equals(tab)) {
                List<Order> orders = orderDAO.findAll();
                Map<Integer, List<OrderItem>> orderItemsMap = new HashMap<>();
                for (Order o : orders) {
                    orderItemsMap.put(o.getOrderId(), orderItemDAO.findByOrderId(o.getOrderId()));
                }
                request.setAttribute("orders", orders);
                request.setAttribute("orderItemsMap", orderItemsMap);
            } else if ("categories".equals(tab)) {
                // categories already loaded
            } else if ("users".equals(tab)) {
                List<User> users = userDAO.findAll();
                request.setAttribute("users", users);
            } else {
                // Products tab (default)
                List<Product> products = productDAO.findAll();
                request.setAttribute("products", products);
            }

            request.getRequestDispatcher("/WEB-INF/views/admin.jsp").forward(request, response);

        } catch (SQLException e) {
            throw new ServletException("Error loading admin dashboard", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!checkAdmin(request, response)) return;

        String action = request.getParameter("action");
        HttpSession session = request.getSession();

        try {
            if ("addProduct".equals(action)) {
                handleAddProduct(request, session);
                response.sendRedirect(request.getContextPath() + "/admin?tab=products");
            } else if ("editProduct".equals(action)) {
                handleEditProduct(request, session);
                response.sendRedirect(request.getContextPath() + "/admin?tab=products");
            } else if ("toggleProduct".equals(action)) {
                int productId = Integer.parseInt(request.getParameter("productId"));
                boolean currentStatus = Boolean.parseBoolean(request.getParameter("currentStatus"));
                productDAO.updateStatus(productId, !currentStatus);
                session.setAttribute("flashSuccess", "Product status updated.");
                response.sendRedirect(request.getContextPath() + "/admin?tab=products");
            } else if ("deleteProduct".equals(action)) {
                int productId = Integer.parseInt(request.getParameter("productId"));
                productDAO.deleteById(productId);
                session.setAttribute("flashSuccess", "Product deleted successfully.");
                response.sendRedirect(request.getContextPath() + "/admin?tab=products");
            } else if ("addCategory".equals(action)) {
                String name = request.getParameter("categoryName");
                String desc = request.getParameter("description");
                if (name != null && !name.trim().isEmpty()) {
                    Category cat = new Category();
                    cat.setCategoryName(name.trim());
                    cat.setDescription(desc != null ? desc.trim() : "");
                    cat.setActive(true);
                    categoryDAO.save(cat);
                    session.setAttribute("flashSuccess", "Category created successfully.");
                }
                response.sendRedirect(request.getContextPath() + "/admin?tab=categories");
            } else if ("updateOrderStatus".equals(action)) {
                int orderId = Integer.parseInt(request.getParameter("orderId"));
                String newStatus = request.getParameter("orderStatus");
                if (newStatus != null) {
                    orderDAO.updateStatus(orderId, newStatus.trim().toUpperCase());
                    session.setAttribute("flashSuccess", "Order #" + orderId + " status updated to " + newStatus);
                }
                response.sendRedirect(request.getContextPath() + "/admin?tab=orders");
            } else if ("updateUserRole".equals(action)) {
                int targetUserId = Integer.parseInt(request.getParameter("userId"));
                String newRole = request.getParameter("role");
                if (newRole != null && ("ADMIN".equalsIgnoreCase(newRole) || "CUSTOMER".equalsIgnoreCase(newRole))) {
                    try (Connection conn = DBConnection.getConnection();
                         PreparedStatement ps = conn.prepareStatement("UPDATE users SET role = ? WHERE user_id = ?")) {
                        ps.setString(1, newRole.toUpperCase());
                        ps.setInt(2, targetUserId);
                        ps.executeUpdate();
                    }
                    session.setAttribute("flashSuccess", "User #" + targetUserId + " role updated to " + newRole.toUpperCase() + ".");
                }
                response.sendRedirect(request.getContextPath() + "/admin?tab=users");
            } else if ("createAdminUser".equals(action)) {
                handleCreateAdminUser(request, session);
                response.sendRedirect(request.getContextPath() + "/admin?tab=users");
            } else {
                response.sendRedirect(request.getContextPath() + "/admin");
            }
        } catch (Exception e) {
            session.setAttribute("flashError", "Admin action failed: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/admin");
        }
    }

    private void handleAddProduct(HttpServletRequest request, HttpSession session) throws SQLException {
        int categoryId = Integer.parseInt(request.getParameter("categoryId"));
        String name = request.getParameter("productName");
        String desc = request.getParameter("description");
        BigDecimal price = new BigDecimal(request.getParameter("price"));
        BigDecimal discount = BigDecimal.ZERO;
        String discParam = request.getParameter("discountPercent");
        if (discParam != null && !discParam.trim().isEmpty()) {
            discount = new BigDecimal(discParam);
        }
        String imageUrl = request.getParameter("imageUrl");
        if (imageUrl == null || imageUrl.trim().isEmpty()) {
            imageUrl = "images/men-tshirt.jpg"; // fallback
        }
        boolean isActive = "on".equals(request.getParameter("isActive")) || "true".equals(request.getParameter("isActive"));

        Product p = new Product();
        p.setCategoryId(categoryId);
        p.setProductName(name.trim());
        p.setDescription(desc != null ? desc.trim() : "");
        p.setPrice(price);
        p.setDiscountPercent(discount);
        p.setImageUrl(imageUrl.trim());
        p.setActive(isActive);

        productDAO.save(p);
        session.setAttribute("flashSuccess", "Product '" + p.getProductName() + "' created successfully.");
    }

    private void handleEditProduct(HttpServletRequest request, HttpSession session) throws SQLException {
        int productId = Integer.parseInt(request.getParameter("productId"));
        int categoryId = Integer.parseInt(request.getParameter("categoryId"));
        String name = request.getParameter("productName");
        String desc = request.getParameter("description");
        BigDecimal price = new BigDecimal(request.getParameter("price"));
        BigDecimal discount = BigDecimal.ZERO;
        String discParam = request.getParameter("discountPercent");
        if (discParam != null && !discParam.trim().isEmpty()) {
            discount = new BigDecimal(discParam);
        }
        String imageUrl = request.getParameter("imageUrl");
        boolean isActive = "on".equals(request.getParameter("isActive")) || "true".equals(request.getParameter("isActive"));

        Optional<Product> opt = productDAO.findById(productId);
        if (opt.isPresent()) {
            Product p = opt.get();
            p.setCategoryId(categoryId);
            p.setProductName(name.trim());
            p.setDescription(desc != null ? desc.trim() : "");
            p.setPrice(price);
            p.setDiscountPercent(discount);
            if (imageUrl != null && !imageUrl.trim().isEmpty()) {
                p.setImageUrl(imageUrl.trim());
            }
            p.setActive(isActive);

            productDAO.update(p);
            session.setAttribute("flashSuccess", "Product updated successfully.");
        }
    }

    private void handleCreateAdminUser(HttpServletRequest request, HttpSession session) throws SQLException {
        String name = request.getParameter("fullName");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String phone = request.getParameter("phone");

        if (email == null || email.trim().isEmpty() || password == null || password.trim().length() < 6) {
            session.setAttribute("flashError", "Valid email and password (min 6 chars) are required.");
            return;
        }

        String targetEmail = email.trim().toLowerCase();
        if (userDAO.existsByEmail(targetEmail)) {
            // Update to admin
            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement ps = conn.prepareStatement("UPDATE users SET role = 'ADMIN' WHERE email = ?")) {
                ps.setString(1, targetEmail);
                ps.executeUpdate();
            }
            session.setAttribute("flashSuccess", "Existing user '" + targetEmail + "' upgraded to ADMIN.");
        } else {
            User admin = new User();
            admin.setFullName((name != null && !name.trim().isEmpty()) ? name.trim() : "Admin User");
            admin.setEmail(targetEmail);
            admin.setPassword(PasswordUtil.hashPassword(password.trim()));
            admin.setPhone(phone != null ? phone.trim() : "9876543210");
            admin.setGender("Other");
            admin.setAddress("Store Office");
            admin.setRole("ADMIN");
            userDAO.save(admin);
            session.setAttribute("flashSuccess", "New ADMIN user '" + targetEmail + "' created successfully.");
        }
    }
}
