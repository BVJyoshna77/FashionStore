
package com.fashionstore.dao;

import com.fashionstore.model.Cart;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface CartDAO {

    // Create
    boolean createForUser(int userId) throws SQLException;

    // Read
    Optional<Cart> findById(int cartId) throws SQLException;

    Optional<Cart> findByUserId(int userId) throws SQLException;

    List<Cart> findAll() throws SQLException;

    boolean existsByUserId(int userId) throws SQLException;

    // Update
    boolean update(Cart cart) throws SQLException;

    // Delete
    boolean deleteById(int cartId) throws SQLException;

    boolean deleteByUserId(int userId) throws SQLException;
}
