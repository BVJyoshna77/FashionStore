
package com.fashionstore.dao;

import com.fashionstore.model.ProductVariant;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface ProductVariantDAO {

    // Create
    boolean save(ProductVariant variant) throws SQLException;

    // Read
    Optional<ProductVariant> findById(int productSizeId)
            throws SQLException;

    Optional<ProductVariant> findBySkuCode(String skuCode)
            throws SQLException;

    Optional<ProductVariant> findByProductAndSize(
            int productId, String sizeLabel) throws SQLException;

    List<ProductVariant> findAll() throws SQLException;

    List<ProductVariant> findByProductId(int productId)
            throws SQLException;

    List<ProductVariant> findAvailableByProductId(int productId)
            throws SQLException;

    List<ProductVariant> findInStockByProductId(int productId)
            throws SQLException;

    boolean existsBySkuCode(String skuCode) throws SQLException;

    // Update
    boolean update(ProductVariant variant) throws SQLException;

    boolean updateStock(int productSizeId, int stockQuantity)
            throws SQLException;

    boolean adjustStock(int productSizeId, int quantityChange)
            throws SQLException;

    boolean updateAvailability(int productSizeId, boolean available)
            throws SQLException;

    // Delete
    boolean deleteById(int productSizeId) throws SQLException;
}
