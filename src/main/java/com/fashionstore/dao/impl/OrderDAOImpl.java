
package com.fashionstore.dao.impl;

import com.fashionstore.dao.OrderDAO;
import com.fashionstore.model.Order;
import com.fashionstore.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrderDAOImpl implements OrderDAO {

    @Override
    public boolean save(Order order) throws SQLException {
        String sql = "INSERT INTO orders "
                + "(user_id, total_amount, payment_method, order_status, delivery_address) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(
                     sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, order.getUserId());
            ps.setBigDecimal(2, order.getTotalAmount());
            ps.setString(3, order.getPaymentMethod());
            ps.setString(4, order.getOrderStatus());
            ps.setString(5, order.getDeliveryAddress());

            int rows = ps.executeUpdate();

            if (rows > 0) {
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        order.setOrderId(keys.getInt(1));
                    }
                }
                return true;
            }

            return false;
        }
    }

    @Override
    public Optional<Order> findById(int orderId) throws SQLException {
        String sql = "SELECT * FROM orders WHERE order_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, orderId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapOrder(rs));
                }
            }
        }

        return Optional.empty();
    }

    @Override
    public List<Order> findAll() throws SQLException {
        String sql = "SELECT * FROM orders ORDER BY order_date DESC";
        return executeListQuery(sql);
    }

    @Override
    public List<Order> findByUserId(int userId) throws SQLException {
        String sql = "SELECT * FROM orders "
                + "WHERE user_id = ? ORDER BY order_date DESC";

        return executeListQuery(sql, userId);
    }

    @Override
    public List<Order> findByStatus(String orderStatus) throws SQLException {
        String sql = "SELECT * FROM orders "
                + "WHERE order_status = ? ORDER BY order_date DESC";

        return executeListQuery(sql, orderStatus);
    }

    @Override
    public List<Order> findByUserIdAndStatus(
            int userId, String orderStatus) throws SQLException {

        String sql = "SELECT * FROM orders "
                + "WHERE user_id = ? AND order_status = ? "
                + "ORDER BY order_date DESC";

        return executeListQuery(sql, userId, orderStatus);
    }

    @Override
    public boolean existsById(int orderId) throws SQLException {
        String sql = "SELECT 1 FROM orders WHERE order_id = ? LIMIT 1";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, orderId);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    @Override
    public boolean update(Order order) throws SQLException {
        String sql = "UPDATE orders SET user_id = ?, total_amount = ?, "
                + "payment_method = ?, order_status = ?, delivery_address = ? "
                + "WHERE order_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, order.getUserId());
            ps.setBigDecimal(2, order.getTotalAmount());
            ps.setString(3, order.getPaymentMethod());
            ps.setString(4, order.getOrderStatus());
            ps.setString(5, order.getDeliveryAddress());
            ps.setInt(6, order.getOrderId());

            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean updateStatus(int orderId, String orderStatus)
            throws SQLException {

        String sql = "UPDATE orders SET order_status = ? WHERE order_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, orderStatus);
            ps.setInt(2, orderId);

            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean deleteById(int orderId) throws SQLException {
        String sql = "DELETE FROM orders WHERE order_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, orderId);
            return ps.executeUpdate() > 0;
        }
    }

    private List<Order> executeListQuery(
            String sql, Object... parameters) throws SQLException {

        List<Order> orders = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            for (int i = 0; i < parameters.length; i++) {
                ps.setObject(i + 1, parameters[i]);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    orders.add(mapOrder(rs));
                }
            }
        }

        return orders;
    }

    private Order mapOrder(ResultSet rs) throws SQLException {
        Order order = new Order();

        order.setOrderId(rs.getInt("order_id"));
        order.setUserId(rs.getInt("user_id"));
        order.setOrderDate(rs.getTimestamp("order_date"));
        order.setTotalAmount(rs.getBigDecimal("total_amount"));
        order.setPaymentMethod(rs.getString("payment_method"));
        order.setOrderStatus(rs.getString("order_status"));
        order.setDeliveryAddress(rs.getString("delivery_address"));

        return order;
    }
}
