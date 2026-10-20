package com.fashionstore.servlet;

import com.fashionstore.dao.CartDAO;
import com.fashionstore.dao.CartItemDAO;
import com.fashionstore.dao.ProductDAO;
import com.fashionstore.dao.impl.CartDAOImpl;
import com.fashionstore.dao.impl.CartItemDAOImpl;
import com.fashionstore.dao.impl.ProductDAOImpl;
import com.fashionstore.model.CartItemDto;
import com.fashionstore.model.CartSession;
import com.fashionstore.model.Product;
import com.fashionstore.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

@WebServlet("/cart")
public class CartServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private ProductDAO productDAO;
    private CartDAO cartDAO;
    private CartItemDAO cartItemDAO;

    @Override
    public void init() throws ServletException {
        productDAO = new ProductDAOImpl();
        cartDAO = new CartDAOImpl();
        cartItemDAO = new CartItemDAOImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        CartSession cart = (CartSession) session.getAttribute("cart");
        if (cart == null) {
            cart = new CartSession();
            session.setAttribute("cart", cart);
        }

        request.getRequestDispatcher("/WEB-INF/views/cart.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        CartSession cart = (CartSession) session.getAttribute("cart");
        if (cart == null) {
            cart = new CartSession();
            session.setAttribute("cart", cart);
        }

        String action = request.getParameter("action");
        if (action == null) {
            action = "view";
        }

        try {
            switch (action) {
                case "add":
                    handleAdd(request, session, cart);
                    break;
                case "increment":
                    handleIncrement(request, session, cart);
                    break;
                case "decrement":
                    handleDecrement(request, session, cart);
                    break;
                case "update":
                    handleUpdate(request, session, cart);
                    break;
                case "remove":
                    handleRemove(request, session, cart);
                    break;
                case "clear":
                    cart.clear();
                    session.setAttribute("flashSuccess", "Shopping cart cleared.");
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            session.setAttribute("flashError", "Failed to update cart: " + e.getMessage());
        }

        String redirectUrl = request.getParameter("redirect");
        if (redirectUrl != null && !redirectUrl.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + redirectUrl);
        } else {
            response.sendRedirect(request.getContextPath() + "/cart");
        }
    }

    private void handleAdd(HttpServletRequest request, HttpSession session, CartSession cart)
            throws SQLException {
        String idParam = request.getParameter("productId");
        String sizeParam = request.getParameter("sizeLabel");
        String qtyParam = request.getParameter("quantity");

        if (idParam == null || idParam.trim().isEmpty()) {
            session.setAttribute("flashError", "Invalid product ID.");
            return;
        }

        int productId = Integer.parseInt(idParam);
        int quantity = 1;
        if (qtyParam != null && !qtyParam.trim().isEmpty()) {
            try {
                quantity = Math.max(1, Integer.parseInt(qtyParam));
            } catch (NumberFormatException ignored) {}
        }

        String size = (sizeParam != null && !sizeParam.trim().isEmpty()) ? sizeParam.trim() : "M";

        Optional<Product> prodOpt = productDAO.findById(productId);
        if (prodOpt.isPresent() && prodOpt.get().isActive()) {
            Product p = prodOpt.get();
            CartItemDto dto = new CartItemDto(
                    p.getProductId(),
                    p.getProductName(),
                    p.getImageUrl(),
                    size,
                    quantity,
                    p.getPrice(),
                    p.getDiscountPercent()
            );
            cart.addItem(dto);
            session.setAttribute("flashSuccess", p.getProductName() + " (Size: " + size + ") added to your cart!");
        } else {
            session.setAttribute("flashError", "Product not available.");
        }
    }

    private void handleIncrement(HttpServletRequest request, HttpSession session, CartSession cart) {
        String key = request.getParameter("itemKey");
        if (key != null) {
            cart.incrementQuantity(key);
        }
    }

    private void handleDecrement(HttpServletRequest request, HttpSession session, CartSession cart) {
        String key = request.getParameter("itemKey");
        if (key != null) {
            cart.decrementQuantity(key);
        }
    }

    private void handleUpdate(HttpServletRequest request, HttpSession session, CartSession cart) {
        String key = request.getParameter("itemKey");
        String qtyParam = request.getParameter("quantity");
        if (key != null && qtyParam != null) {
            try {
                int qty = Integer.parseInt(qtyParam);
                cart.updateQuantity(key, qty);
                session.setAttribute("flashSuccess", "Quantity updated.");
            } catch (NumberFormatException ignored) {}
        }
    }

    private void handleRemove(HttpServletRequest request, HttpSession session, CartSession cart) {
        String key = request.getParameter("itemKey");
        if (key != null) {
            cart.removeItem(key);
            session.setAttribute("flashSuccess", "Item removed from cart.");
        }
    }
}
