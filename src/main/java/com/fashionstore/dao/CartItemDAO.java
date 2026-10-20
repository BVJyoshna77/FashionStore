
package com.fashionstore.dao;

import com.fashionstore.model.CartItem;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface CartItemDAO {

    // Create
    boolean save(CartItem cartItem) throws SQLException;

    // Read
    Optional<CartItem> findById(int cartItemId) throws SQLException;

    Optional<CartItem> findByCartProductAndSize(
            int cartId, int productId, String sizeLabel)
            throws SQLException;

    List<CartItem> findByCartId(int cartId) throws SQLException;

    List<CartItem> findByUserId(int userId) throws SQLException;

    int countByCartId(int cartId) throws SQLException;

    // Update
    boolean update(CartItem cartItem) throws SQLException;

    boolean updateQuantity(int cartItemId, int quantity)
            throws SQLException;

    boolean increaseQuantity(int cartItemId, int quantity)
            throws SQLException;

    // Delete
    boolean deleteById(int cartItemId) throws SQLException;

    boolean deleteByCartId(int cartId) throws SQLException;

    boolean clearCart(int cartId) throws SQLException;
}
