
package com.fashionstore.dao;

import com.fashionstore.model.User;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface UserDAO {

    // Create
    boolean save(User user) throws SQLException;

    // Read
    Optional<User> findById(int userId) throws SQLException;

    Optional<User> findByEmail(String email) throws SQLException;

    List<User> findAll() throws SQLException;

    boolean existsByEmail(String email) throws SQLException;

    // Update
    boolean update(User user) throws SQLException;

    boolean updatePassword(int userId, String passwordHash)
            throws SQLException;

    // Delete
    boolean deleteById(int userId) throws SQLException;
}
