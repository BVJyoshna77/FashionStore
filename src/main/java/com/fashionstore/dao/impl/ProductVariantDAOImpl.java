
package com.fashionstore.dao.impl;

import com.fashionstore.dao.ProductVariantDAO;
import com.fashionstore.model.ProductVariant;
import com.fashionstore.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductVariantDAOImpl implements ProductVariantDAO {

    @Override
    public boolean save(ProductVariant variant) throws SQLException {
        String sql = "INSERT INTO product_sizes "
                + "(product_id, size_label, stock_quantity, "
                + "sku_code, is_available) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, variant.getProductId());
            ps.setString(2, variant.getSizeLabel());
            ps.setInt(3, variant.getStockQuantity());
            ps.setString(4, variant.getSkuCode());
            ps.setBoolean(5, variant.isAvailable());

            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public Optional<ProductVariant> findById(int productSizeId)
            throws SQLException {
        String sql = "SELECT * FROM product_sizes "
                + "WHERE product_size_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, productSizeId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapVariant(rs));
                }
            }
        }

        return Optional.empty();
    }

    @Override
    public Optional<ProductVariant> findBySkuCode(String skuCode)
            throws SQLException {
        String sql = "SELECT * FROM product_sizes WHERE sku_code = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, skuCode);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapVariant(rs));
                }
            }
        }

        return Optional.empty();
    }

    @Override
    public Optional<ProductVariant> findByProductAndSize(
            int productId, String sizeLabel) throws SQLException {

        String sql = "SELECT * FROM product_sizes "
                + "WHERE product_id = ? AND size_label = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, productId);
            ps.setString(2, sizeLabel);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapVariant(rs));
                }
            }
        }

        return Optional.empty();
    }

    @Override
    public List<ProductVariant> findAll() throws SQLException {
        String sql = "SELECT * FROM product_sizes "
                + "ORDER BY product_size_id";

        return executeListQuery(sql);
    }

    @Override
    public List<ProductVariant> findByProductId(int productId)
            throws SQLException {
        String sql = "SELECT * FROM product_sizes "
                + "WHERE product_id = ? ORDER BY product_size_id";

        return executeListQuery(sql, productId);
    }

    @Override
    public List<ProductVariant> findAvailableByProductId(int productId)
            throws SQLException {
        String sql = "SELECT * FROM product_sizes "
                + "WHERE product_id = ? AND is_available = TRUE "
                + "ORDER BY product_size_id";

        return executeListQuery(sql, productId);
    }

    @Override
    public List<ProductVariant> findInStockByProductId(int productId)
            throws SQLException {
        String sql = "SELECT * FROM product_sizes "
                + "WHERE product_id = ? AND is_available = TRUE "
                + "AND stock_quantity > 0 ORDER BY product_size_id";

        return executeListQuery(sql, productId);
    }

    @Override
    public boolean existsBySkuCode(String skuCode)
            throws SQLException {
        String sql = "SELECT 1 FROM product_sizes WHERE sku_code = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, skuCode);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    @Override
    public boolean update(ProductVariant variant) throws SQLException {
        String sql = "UPDATE product_sizes SET product_id = ?, "
                + "size_label = ?, stock_quantity = ?, "
                + "sku_code = ?, is_available = ? "
                + "WHERE product_size_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, variant.getProductId());
            ps.setString(2, variant.getSizeLabel());
            ps.setInt(3, variant.getStockQuantity());
            ps.setString(4, variant.getSkuCode());
            ps.setBoolean(5, variant.isAvailable());
            ps.setInt(6, variant.getProductSizeId());

            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean updateStock(int productSizeId, int stockQuantity)
            throws SQLException {
        if (stockQuantity < 0) {
            throw new IllegalArgumentException(
                    "Stock quantity cannot be negative.");
        }

        String sql = "UPDATE product_sizes SET stock_quantity = ? "
                + "WHERE product_size_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, stockQuantity);
            ps.setInt(2, productSizeId);

            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean adjustStock(int productSizeId, int quantityChange)
            throws SQLException {
        String sql = "UPDATE product_sizes "
                + "SET stock_quantity = stock_quantity + ? "
                + "WHERE product_size_id = ? "
                + "AND stock_quantity + ? >= 0";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, quantityChange);
            ps.setInt(2, productSizeId);
            ps.setInt(3, quantityChange);

            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean updateAvailability(int productSizeId, boolean available)
            throws SQLException {
        String sql = "UPDATE product_sizes SET is_available = ? "
                + "WHERE product_size_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setBoolean(1, available);
            ps.setInt(2, productSizeId);

            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean deleteById(int productSizeId) throws SQLException {
        String sql = "DELETE FROM product_sizes "
                + "WHERE product_size_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, productSizeId);

            return ps.executeUpdate() > 0;
        }
    }

    private List<ProductVariant> executeListQuery(
            String sql, Object... parameters) throws SQLException {

        List<ProductVariant> variants = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            for (int i = 0; i < parameters.length; i++) {
                ps.setObject(i + 1, parameters[i]);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    variants.add(mapVariant(rs));
                }
            }
        }

        return variants;
    }

    private ProductVariant mapVariant(ResultSet rs) throws SQLException {
        ProductVariant variant = new ProductVariant();

        variant.setProductSizeId(rs.getInt("product_size_id"));
        variant.setProductId(rs.getInt("product_id"));
        variant.setSizeLabel(rs.getString("size_label"));
        variant.setStockQuantity(rs.getInt("stock_quantity"));
        variant.setSkuCode(rs.getString("sku_code"));
        variant.setAvailable(rs.getBoolean("is_available"));

        return variant;
    }
}
