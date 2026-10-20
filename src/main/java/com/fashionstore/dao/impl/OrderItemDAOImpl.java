
package com.fashionstore.dao.impl;

import com.fashionstore.dao.OrderItemDAO;
import com.fashionstore.model.OrderItem;
import com.fashionstore.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrderItemDAOImpl implements OrderItemDAO {

    @Override
    public boolean save(OrderItem orderItem) throws SQLException {
        String sql = "INSERT INTO order_items "
                + "(order_id, product_id, product_name, size_label, "
                + "quantity, unit_price, subtotal) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(
                     sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, orderItem.getOrderId());

            if (orderItem.getProductId() == null) {
                ps.setNull(2, java.sql.Types.INTEGER);
            } else {
                ps.setInt(2, orderItem.getProductId());
            }

            ps.setString(3, orderItem.getProductName());
            ps.setString(4, orderItem.getSizeLabel());
            ps.setInt(5, orderItem.getQuantity());
            ps.setBigDecimal(6, orderItem.getUnitPrice());
            ps.setBigDecimal(7, orderItem.getSubtotal());

            int rows = ps.executeUpdate();

            if (rows > 0) {
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        orderItem.setOrderItemId(keys.getInt(1));
                    }
                }
                return true;
            }

            return false;
        }
    }

    @Override
    public Optional<OrderItem> findById(int orderItemId)
            throws SQLException {

        String sql = "SELECT * FROM order_items WHERE order_item_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, orderItemId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapOrderItem(rs));
                }
            }
        }

        return Optional.empty();
    }

    @Override
    public List<OrderItem> findAll() throws SQLException {
        String sql = "SELECT * FROM order_items ORDER BY order_item_id";
        return executeListQuery(sql);
    }

    @Override
    public List<OrderItem> findByOrderId(int orderId)
            throws SQLException {

        String sql = "SELECT * FROM order_items "
                + "WHERE order_id = ? ORDER BY order_item_id";

        return executeListQuery(sql, orderId);
    }

    @Override
    public List<OrderItem> findByProductId(int productId)
            throws SQLException {

        String sql = "SELECT * FROM order_items "
                + "WHERE product_id = ? ORDER BY order_item_id";

        return executeListQuery(sql, productId);
    }

    @Override
    public boolean update(OrderItem orderItem) throws SQLException {
        String sql = "UPDATE order_items SET order_id = ?, product_id = ?, "
                + "product_name = ?, size_label = ?, quantity = ?, "
                + "unit_price = ?, subtotal = ? WHERE order_item_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, orderItem.getOrderId());

            if (orderItem.getProductId() == null) {
                ps.setNull(2, java.sql.Types.INTEGER);
            } else {
                ps.setInt(2, orderItem.getProductId());
            }

            ps.setString(3, orderItem.getProductName());
            ps.setString(4, orderItem.getSizeLabel());
            ps.setInt(5, orderItem.getQuantity());
            ps.setBigDecimal(6, orderItem.getUnitPrice());
            ps.setBigDecimal(7, orderItem.getSubtotal());
            ps.setInt(8, orderItem.getOrderItemId());

            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean deleteById(int orderItemId) throws SQLException {
        String sql = "DELETE FROM order_items WHERE order_item_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, orderItemId);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean deleteByOrderId(int orderId) throws SQLException {
        String sql = "DELETE FROM order_items WHERE order_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, orderId);
            return ps.executeUpdate() > 0;
        }
    }

    private List<OrderItem> executeListQuery(
            String sql, Object... parameters) throws SQLException {

        List<OrderItem> items = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            for (int i = 0; i < parameters.length; i++) {
                ps.setObject(i + 1, parameters[i]);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    items.add(mapOrderItem(rs));
                }
            }
        }

        return items;
    }

    private OrderItem mapOrderItem(ResultSet rs) throws SQLException {
        OrderItem item = new OrderItem();

        item.setOrderItemId(rs.getInt("order_item_id"));
        item.setOrderId(rs.getInt("order_id"));

        int productId = rs.getInt("product_id");
        if (rs.wasNull()) {
            item.setProductId(null);
        } else {
            item.setProductId(productId);
        }

        item.setProductName(rs.getString("product_name"));
        item.setSizeLabel(rs.getString("size_label"));
        item.setQuantity(rs.getInt("quantity"));
        item.setUnitPrice(rs.getBigDecimal("unit_price"));
        item.setSubtotal(rs.getBigDecimal("subtotal"));

        return item;
    }
}
