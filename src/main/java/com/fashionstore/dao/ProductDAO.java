
package com.fashionstore.dao;

import com.fashionstore.model.Product;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface ProductDAO {

    // Create
    boolean save(Product product) throws SQLException;

    // Read
    Optional<Product> findById(int productId) throws SQLException;

    List<Product> findAll() throws SQLException;

    List<Product> findAllActive() throws SQLException;

    List<Product> findByCategoryId(int categoryId)
            throws SQLException;

    List<Product> findActiveByCategoryId(int categoryId)
            throws SQLException;

    List<Product> searchByName(String keyword) throws SQLException;

    List<Product> searchActiveByName(String keyword)
            throws SQLException;

    List<Product> findByDiscountRange(
            BigDecimal minDiscount,
            BigDecimal maxDiscount) throws SQLException;

    List<Product> findActiveByDiscountRange(
            BigDecimal minDiscount,
            BigDecimal maxDiscount) throws SQLException;

    boolean existsById(int productId) throws SQLException;

    // Update
    boolean update(Product product) throws SQLException;

    boolean updateStatus(int productId, boolean active)
            throws SQLException;

    boolean updateDiscount(
            int productId,
            BigDecimal discountPercent) throws SQLException;

    // Delete
    boolean deleteById(int productId) throws SQLException;
}
