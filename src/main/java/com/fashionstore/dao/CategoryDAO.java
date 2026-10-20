
package com.fashionstore.dao;

import com.fashionstore.model.Category;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface CategoryDAO {

    // Create
    boolean save(Category category) throws SQLException;

    // Read
    Optional<Category> findById(int categoryId) throws SQLException;

    Optional<Category> findByName(String categoryName)
            throws SQLException;

    List<Category> findAll() throws SQLException;

    List<Category> findAllActive() throws SQLException;

    boolean existsByName(String categoryName) throws SQLException;

    // Update
    boolean update(Category category) throws SQLException;

    boolean updateStatus(int categoryId, boolean active)
            throws SQLException;

    // Delete
    boolean deleteById(int categoryId) throws SQLException;
}
