
package com.fashionstore.dao;

import com.fashionstore.model.Order;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface OrderDAO {

    // Create
    boolean save(Order order) throws SQLException;

    // Read
    Optional<Order> findById(int orderId) throws SQLException;

    List<Order> findAll() throws SQLException;

    List<Order> findByUserId(int userId) throws SQLException;

    List<Order> findByStatus(String orderStatus) throws SQLException;

    List<Order> findByUserIdAndStatus(
            int userId, String orderStatus) throws SQLException;

    boolean existsById(int orderId) throws SQLException;

    // Update
    boolean update(Order order) throws SQLException;

    boolean updateStatus(int orderId, String orderStatus)
            throws SQLException;

    // Delete
    boolean deleteById(int orderId) throws SQLException;
}
