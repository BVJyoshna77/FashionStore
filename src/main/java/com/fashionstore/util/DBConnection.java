
package com.fashionstore.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL =
            "jdbc:mysql://localhost:3306/fashion_store";

    private static final String USERNAME = "root";

    private static final String PASSWORD = "JYOSHNA";

    public static Connection getConnection() throws SQLException {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException(
                    "MySQL JDBC driver is missing. Check Maven Dependencies.",
                    e
            );
        }

        return DriverManager.getConnection(
                URL, USERNAME, PASSWORD
        );
    }

    public static void main(String[] args) {
        try (Connection connection = getConnection()) {
            System.out.println(
                    "FashionStore database connected successfully!"
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
