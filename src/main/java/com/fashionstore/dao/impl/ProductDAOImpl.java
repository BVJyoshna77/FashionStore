
package com.fashionstore.dao.impl;

import com.fashionstore.dao.ProductDAO;
import com.fashionstore.model.Product;
import com.fashionstore.util.DBConnection;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductDAOImpl implements ProductDAO {

    @Override
    public boolean save(Product product) throws SQLException {

        String sql = "INSERT INTO products "
                + "(category_id, product_name, description, price, "
                + "discount_percent, image_url, is_active) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, product.getCategoryId());
            ps.setString(2, product.getProductName());
            ps.setString(3, product.getDescription());
            ps.setBigDecimal(4, product.getPrice());
            ps.setBigDecimal(5, product.getDiscountPercent());
            ps.setString(6, product.getImageUrl());
            ps.setBoolean(7, product.isActive());

            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public Optional<Product> findById(int productId)
            throws SQLException {

        String sql = "SELECT * FROM products WHERE product_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, productId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapProduct(rs));
                }
            }
        }

        return Optional.empty();
    }

    @Override
    public List<Product> findAll() throws SQLException {

        String sql = "SELECT * FROM products ORDER BY product_id";

        return executeProductListQuery(sql);
    }

    @Override
    public List<Product> findAllActive() throws SQLException {

        String sql = "SELECT * FROM products "
                + "WHERE is_active = TRUE ORDER BY product_id";

        return executeProductListQuery(sql);
    }

    @Override
    public List<Product> findByCategoryId(int categoryId)
            throws SQLException {

        String sql = "SELECT * FROM products "
                + "WHERE category_id = ? ORDER BY product_id";

        return executeProductListQuery(sql, categoryId);
    }

    @Override
    public List<Product> findActiveByCategoryId(int categoryId)
            throws SQLException {

        String sql = "SELECT * FROM products "
                + "WHERE category_id = ? AND is_active = TRUE "
                + "ORDER BY product_id";

        return executeProductListQuery(sql, categoryId);
    }

    @Override
    public List<Product> searchByName(String keyword)
            throws SQLException {

        String sql = "SELECT * FROM products "
                + "WHERE product_name LIKE ? ORDER BY product_id";

        return executeProductListQuery(sql, "%" + keyword + "%");
    }

    @Override
    public List<Product> searchActiveByName(String keyword)
            throws SQLException {

        String sql = "SELECT * FROM products "
                + "WHERE product_name LIKE ? AND is_active = TRUE "
                + "ORDER BY product_id";

        return executeProductListQuery(sql, "%" + keyword + "%");
    }

    @Override
    public List<Product> findByDiscountRange(
            BigDecimal minDiscount, BigDecimal maxDiscount)
            throws SQLException {

        String sql = "SELECT * FROM products "
                + "WHERE discount_percent BETWEEN ? AND ? "
                + "ORDER BY product_id";

        return executeProductListQuery(
                sql, minDiscount, maxDiscount);
    }

    @Override
    public List<Product> findActiveByDiscountRange(
            BigDecimal minDiscount, BigDecimal maxDiscount)
            throws SQLException {

        String sql = "SELECT * FROM products "
                + "WHERE discount_percent BETWEEN ? AND ? "
                + "AND is_active = TRUE ORDER BY product_id";

        return executeProductListQuery(
                sql, minDiscount, maxDiscount);
    }

    @Override
    public boolean existsById(int productId) throws SQLException {

        String sql = "SELECT 1 FROM products WHERE product_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, productId);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    @Override
    public boolean update(Product product) throws SQLException {

        String sql = "UPDATE products SET category_id = ?, "
                + "product_name = ?, description = ?, price = ?, "
                + "discount_percent = ?, image_url = ?, "
                + "is_active = ? WHERE product_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, product.getCategoryId());
            ps.setString(2, product.getProductName());
            ps.setString(3, product.getDescription());
            ps.setBigDecimal(4, product.getPrice());
            ps.setBigDecimal(5, product.getDiscountPercent());
            ps.setString(6, product.getImageUrl());
            ps.setBoolean(7, product.isActive());
            ps.setInt(8, product.getProductId());

            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean updateStatus(int productId, boolean active)
            throws SQLException {

        String sql = "UPDATE products SET is_active = ? "
                + "WHERE product_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setBoolean(1, active);
            ps.setInt(2, productId);

            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean updateDiscount(
            int productId, BigDecimal discountPercent)
            throws SQLException {

        String sql = "UPDATE products SET discount_percent = ? "
                + "WHERE product_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setBigDecimal(1, discountPercent);
            ps.setInt(2, productId);

            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean deleteById(int productId) throws SQLException {

        String sql = "DELETE FROM products WHERE product_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, productId);

            return ps.executeUpdate() > 0;
        }
    }

    private List<Product> executeProductListQuery(
            String sql, Object... parameters) throws SQLException {

        List<Product> products = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            for (int i = 0; i < parameters.length; i++) {
                ps.setObject(i + 1, parameters[i]);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    products.add(mapProduct(rs));
                }
            }
        }

        return products;
    }

    private Product mapProduct(ResultSet rs) throws SQLException {

        Product product = new Product();

        product.setProductId(rs.getInt("product_id"));
        product.setCategoryId(rs.getInt("category_id"));
        product.setProductName(rs.getString("product_name"));
        product.setDescription(rs.getString("description"));
        product.setPrice(rs.getBigDecimal("price"));
        product.setDiscountPercent(
                rs.getBigDecimal("discount_percent"));
        product.setImageUrl(rs.getString("image_url"));
        product.setActive(rs.getBoolean("is_active"));

        return product;
    }
}
