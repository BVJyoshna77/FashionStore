
package com.fashionstore.dao;

import com.fashionstore.model.OrderItem;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface OrderItemDAO {

    // Create
    boolean save(OrderItem orderItem) throws SQLException;

    // Read
    Optional<OrderItem> findById(int orderItemId)
            throws SQLException;

    List<OrderItem> findAll() throws SQLException;

    List<OrderItem> findByOrderId(int orderId) throws SQLException;

    List<OrderItem> findByProductId(int productId)
            throws SQLException;

    // Update
    boolean update(OrderItem orderItem) throws SQLException;

    // Delete
    boolean deleteById(int orderItemId) throws SQLException;

    boolean deleteByOrderId(int orderId) throws SQLException;
}
