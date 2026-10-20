package com.fashionstore.servlet;

import com.fashionstore.dao.OrderDAO;
import com.fashionstore.dao.OrderItemDAO;
import com.fashionstore.dao.impl.OrderDAOImpl;
import com.fashionstore.dao.impl.OrderItemDAOImpl;
import com.fashionstore.model.Order;
import com.fashionstore.model.OrderItem;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@WebServlet("/order-confirmation")
public class OrderConfirmationServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private OrderDAO orderDAO;
    private OrderItemDAO orderItemDAO;

    @Override
    public void init() throws ServletException {
        orderDAO = new OrderDAOImpl();
        orderItemDAO = new OrderItemDAOImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idParam = request.getParameter("orderId");
        if (idParam == null || idParam.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }

        try {
            int orderId = Integer.parseInt(idParam);
            Optional<Order> orderOpt = orderDAO.findById(orderId);
            if (orderOpt.isEmpty()) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Order not found");
                return;
            }

            List<OrderItem> items = orderItemDAO.findByOrderId(orderId);

            request.setAttribute("order", orderOpt.get());
            request.setAttribute("items", items);
            request.getRequestDispatcher("/WEB-INF/views/order-confirmation.jsp").forward(request, response);

        } catch (NumberFormatException | SQLException e) {
            throw new ServletException("Unable to load order confirmation", e);
        }
    }
}
