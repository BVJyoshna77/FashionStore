
package com.fashionstore.dao.impl;

import com.fashionstore.dao.CartItemDAO;
import com.fashionstore.model.CartItem;
import com.fashionstore.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CartItemDAOImpl implements CartItemDAO {

    @Override
    public boolean save(CartItem cartItem) throws SQLException {
        String sql = "INSERT INTO cart_items "
                + "(cart_id, product_id, size_label, quantity, unit_price) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, cartItem.getCartId());
            ps.setInt(2, cartItem.getProductId());
            ps.setString(3, cartItem.getSizeLabel());
            ps.setInt(4, cartItem.getQuantity());
            ps.setBigDecimal(5, cartItem.getUnitPrice());

            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public Optional<CartItem> findById(int cartItemId) throws SQLException {
        String sql = "SELECT * FROM cart_items WHERE cart_item_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, cartItemId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapCartItem(rs));
                }
            }
        }

        return Optional.empty();
    }

    @Override
    public Optional<CartItem> findByCartProductAndSize(
            int cartId, int productId, String sizeLabel) throws SQLException {

        String sql = "SELECT * FROM cart_items "
                + "WHERE cart_id = ? AND product_id = ? AND size_label = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, cartId);
            ps.setInt(2, productId);
            ps.setString(3, sizeLabel);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapCartItem(rs));
                }
            }
        }

        return Optional.empty();
    }

    @Override
    public List<CartItem> findByCartId(int cartId) throws SQLException {
        String sql = "SELECT * FROM cart_items "
                + "WHERE cart_id = ? ORDER BY cart_item_id";

        return executeListQuery(sql, cartId);
    }

    @Override
    public List<CartItem> findByUserId(int userId) throws SQLException {
        String sql = "SELECT ci.* FROM cart_items ci "
                + "INNER JOIN cart c ON ci.cart_id = c.cart_id "
                + "WHERE c.user_id = ? ORDER BY ci.cart_item_id";

        return executeListQuery(sql, userId);
    }

    @Override
    public int countByCartId(int cartId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM cart_items WHERE cart_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, cartId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }

        return 0;
    }

    @Override
    public boolean update(CartItem cartItem) throws SQLException {
        String sql = "UPDATE cart_items SET cart_id = ?, product_id = ?, "
                + "size_label = ?, quantity = ?, unit_price = ? "
                + "WHERE cart_item_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, cartItem.getCartId());
            ps.setInt(2, cartItem.getProductId());
            ps.setString(3, cartItem.getSizeLabel());
            ps.setInt(4, cartItem.getQuantity());
            ps.setBigDecimal(5, cartItem.getUnitPrice());
            ps.setInt(6, cartItem.getCartItemId());

            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean updateQuantity(int cartItemId, int quantity)
            throws SQLException {

        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero.");
        }

        String sql = "UPDATE cart_items SET quantity = ? WHERE cart_item_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, quantity);
            ps.setInt(2, cartItemId);

            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean increaseQuantity(int cartItemId, int quantity)
            throws SQLException {

        if (quantity <= 0) {
            throw new IllegalArgumentException("Increase quantity must be greater than zero.");
        }

        String sql = "UPDATE cart_items SET quantity = quantity + ? "
                + "WHERE cart_item_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, quantity);
            ps.setInt(2, cartItemId);

            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean deleteById(int cartItemId) throws SQLException {
        String sql = "DELETE FROM cart_items WHERE cart_item_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, cartItemId);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean deleteByCartId(int cartId) throws SQLException {
        String sql = "DELETE FROM cart_items WHERE cart_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, cartId);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean clearCart(int cartId) throws SQLException {
        String sql = "DELETE FROM cart_items WHERE cart_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, cartId);
            ps.executeUpdate();

            // Clearing an already-empty cart is also successful.
            return true;
        }
    }

    private List<CartItem> executeListQuery(String sql, Object... parameters)
            throws SQLException {

        List<CartItem> items = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            for (int i = 0; i < parameters.length; i++) {
                ps.setObject(i + 1, parameters[i]);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    items.add(mapCartItem(rs));
                }
            }
        }

        return items;
    }

    private CartItem mapCartItem(ResultSet rs) throws SQLException {
        CartItem item = new CartItem();

        item.setCartItemId(rs.getInt("cart_item_id"));
        item.setCartId(rs.getInt("cart_id"));
        item.setProductId(rs.getInt("product_id"));
        item.setSizeLabel(rs.getString("size_label"));
        item.setQuantity(rs.getInt("quantity"));
        item.setUnitPrice(rs.getBigDecimal("unit_price"));
        item.setAddedAt(rs.getTimestamp("added_at"));

        return item;
    }
}
