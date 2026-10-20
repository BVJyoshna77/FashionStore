
package com.fashionstore.dao.impl;

import com.fashionstore.dao.CartDAO;
import com.fashionstore.model.Cart;
import com.fashionstore.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CartDAOImpl implements CartDAO {

    @Override
    public boolean createForUser(int userId) throws SQLException {
        String sql = "INSERT INTO cart (user_id) VALUES (?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, userId);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public Optional<Cart> findById(int cartId) throws SQLException {
        String sql = "SELECT * FROM cart WHERE cart_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, cartId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapCart(rs));
                }
            }
        }

        return Optional.empty();
    }

    @Override
    public Optional<Cart> findByUserId(int userId) throws SQLException {
        String sql = "SELECT * FROM cart WHERE user_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapCart(rs));
                }
            }
        }

        return Optional.empty();
    }

    @Override
    public List<Cart> findAll() throws SQLException {
        String sql = "SELECT * FROM cart ORDER BY cart_id";
        List<Cart> carts = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                carts.add(mapCart(rs));
            }
        }

        return carts;
    }

    @Override
    public boolean existsByUserId(int userId) throws SQLException {
        String sql = "SELECT 1 FROM cart WHERE user_id = ? LIMIT 1";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    @Override
    public boolean update(Cart cart) throws SQLException {
        String sql = "UPDATE cart SET user_id = ? WHERE cart_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, cart.getUserId());
            ps.setInt(2, cart.getCartId());

            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean deleteById(int cartId) throws SQLException {
        String sql = "DELETE FROM cart WHERE cart_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, cartId);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean deleteByUserId(int userId) throws SQLException {
        String sql = "DELETE FROM cart WHERE user_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, userId);
            return ps.executeUpdate() > 0;
        }
    }

    private Cart mapCart(ResultSet rs) throws SQLException {
        Cart cart = new Cart();

        cart.setCartId(rs.getInt("cart_id"));
        cart.setUserId(rs.getInt("user_id"));
        cart.setCreatedAt(rs.getTimestamp("created_at"));
        cart.setUpdatedAt(rs.getTimestamp("updated_at"));

        return cart;
    }
}
