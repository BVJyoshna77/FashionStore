
package com.fashionstore.dao.impl;

import com.fashionstore.dao.CategoryDAO;
import com.fashionstore.model.Category;
import com.fashionstore.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CategoryDAOImpl implements CategoryDAO {

    @Override
    public boolean save(Category category) throws SQLException {
        String sql = "INSERT INTO categories "
                + "(category_name, description, is_active) "
                + "VALUES (?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, category.getCategoryName());
            ps.setString(2, category.getDescription());
            ps.setBoolean(3, category.isActive());

            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public Optional<Category> findById(int categoryId)
            throws SQLException {

        String sql = "SELECT * FROM categories WHERE category_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, categoryId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapCategory(rs));
                }
            }
        }

        return Optional.empty();
    }

    @Override
    public Optional<Category> findByName(String categoryName)
            throws SQLException {

        String sql = "SELECT * FROM categories WHERE category_name = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, categoryName);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapCategory(rs));
                }
            }
        }

        return Optional.empty();
    }

    @Override
    public List<Category> findAll() throws SQLException {
        String sql = "SELECT * FROM categories ORDER BY category_id";

        List<Category> categories = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                categories.add(mapCategory(rs));
            }
        }

        return categories;
    }

    @Override
    public List<Category> findAllActive() throws SQLException {
        String sql = "SELECT * FROM categories "
                + "WHERE is_active = TRUE ORDER BY category_id";

        List<Category> categories = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                categories.add(mapCategory(rs));
            }
        }

        return categories;
    }

    @Override
    public boolean existsByName(String categoryName)
            throws SQLException {

        String sql = "SELECT 1 FROM categories WHERE category_name = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, categoryName);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    @Override
    public boolean update(Category category) throws SQLException {
        String sql = "UPDATE categories SET category_name = ?, "
                + "description = ?, is_active = ? "
                + "WHERE category_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, category.getCategoryName());
            ps.setString(2, category.getDescription());
            ps.setBoolean(3, category.isActive());
            ps.setInt(4, category.getCategoryId());

            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean updateStatus(int categoryId, boolean active)
            throws SQLException {

        String sql = "UPDATE categories SET is_active = ? "
                + "WHERE category_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setBoolean(1, active);
            ps.setInt(2, categoryId);

            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean deleteById(int categoryId) throws SQLException {
        String sql = "DELETE FROM categories WHERE category_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, categoryId);

            return ps.executeUpdate() > 0;
        }
    }

    private Category mapCategory(ResultSet rs) throws SQLException {
        Category category = new Category();

        category.setCategoryId(rs.getInt("category_id"));
        category.setCategoryName(rs.getString("category_name"));
        category.setDescription(rs.getString("description"));
        category.setActive(rs.getBoolean("is_active"));

        return category;
    }
}
