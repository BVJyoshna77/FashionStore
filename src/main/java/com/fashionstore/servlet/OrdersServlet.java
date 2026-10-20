package com.fashionstore.servlet;

import com.fashionstore.dao.OrderDAO;
import com.fashionstore.dao.OrderItemDAO;
import com.fashionstore.dao.impl.OrderDAOImpl;
import com.fashionstore.dao.impl.OrderItemDAOImpl;
import com.fashionstore.model.Order;
import com.fashionstore.model.OrderItem;
import com.fashionstore.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/orders")
public class OrdersServlet extends HttpServlet {

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
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login?redirect=/orders");
            return;
        }

        User user = (User) session.getAttribute("user");
        try {
            List<Order> orders = orderDAO.findByUserId(user.getUserId());
            Map<Integer, List<OrderItem>> orderItemsMap = new HashMap<>();
            for (Order order : orders) {
                orderItemsMap.put(order.getOrderId(), orderItemDAO.findByOrderId(order.getOrderId()));
            }

            request.setAttribute("orders", orders);
            request.setAttribute("orderItemsMap", orderItemsMap);
            request.getRequestDispatcher("/WEB-INF/views/orders.jsp").forward(request, response);

        } catch (SQLException e) {
            throw new ServletException("Unable to load orders", e);
        }
    }
}
