
package com.fashionstore.util;

import java.sql.SQLException;

import com.fashionstore.dao.UserDAO;
import com.fashionstore.dao.CategoryDAO;
import com.fashionstore.dao.ProductDAO;
import com.fashionstore.dao.ProductVariantDAO;
import com.fashionstore.dao.CartDAO;
import com.fashionstore.dao.CartItemDAO;
import com.fashionstore.dao.OrderDAO;
import com.fashionstore.dao.OrderItemDAO;

import com.fashionstore.dao.impl.UserDAOImpl;
import com.fashionstore.dao.impl.CategoryDAOImpl;
import com.fashionstore.dao.impl.ProductDAOImpl;
import com.fashionstore.dao.impl.ProductVariantDAOImpl;
import com.fashionstore.dao.impl.CartDAOImpl;
import com.fashionstore.dao.impl.CartItemDAOImpl;
import com.fashionstore.dao.impl.OrderDAOImpl;
import com.fashionstore.dao.impl.OrderItemDAOImpl;

public class DAOTest {

    public static void main(String[] args) {

        try {
            // Create DAO objects
            UserDAO userDAO = new UserDAOImpl();
            CategoryDAO categoryDAO = new CategoryDAOImpl();
            ProductDAO productDAO = new ProductDAOImpl();
            ProductVariantDAO variantDAO = new ProductVariantDAOImpl();
            CartDAO cartDAO = new CartDAOImpl();
            CartItemDAO cartItemDAO = new CartItemDAOImpl();
            OrderDAO orderDAO = new OrderDAOImpl();
            OrderItemDAO orderItemDAO = new OrderItemDAOImpl();

            System.out.println("================================");
            System.out.println("   FASHIONSTORE DAO TESTING");
            System.out.println("================================");

            // 1. Test UserDAO
            System.out.println("\n1. USERS");
            System.out.println("Total users: "
                    + userDAO.findAll().size());

            userDAO.findAll().forEach(user ->
                System.out.println(user.getUserId()
                    + " - " + user.getFullName()
                    + " - " + user.getEmail())
            );

            // 2. Test CategoryDAO
            System.out.println("\n2. CATEGORIES");
            System.out.println("Active categories: "
                    + categoryDAO.findAllActive().size());

            categoryDAO.findAllActive().forEach(category ->
                System.out.println(category.getCategoryId()
                    + " - " + category.getCategoryName())
            );

            // 3. Test ProductDAO
            System.out.println("\n3. ACTIVE PRODUCTS");
            System.out.println("Active products: "
                    + productDAO.findAllActive().size());

            productDAO.findAllActive().forEach(product ->
                System.out.println(product.getProductId()
                    + " - " + product.getProductName()
                    + " - Discount: "
                    + product.getDiscountPercent() + "%")
            );

            // 4. Test ProductVariantDAO
            System.out.println("\n4. PRODUCT SIZES");
            System.out.println("Total product sizes: "
                    + variantDAO.findAll().size());

            variantDAO.findAll().forEach(variant ->
                System.out.println("Product ID: "
                    + variant.getProductId()
                    + ", Size: " + variant.getSizeLabel()
                    + ", Stock: " + variant.getStockQuantity()
                    + ", SKU: " + variant.getSkuCode())
            );

            // 5. Test CartDAO
            System.out.println("\n5. CARTS");
            System.out.println("Total carts: "
                    + cartDAO.findAll().size());

            cartDAO.findAll().forEach(cart ->
                System.out.println("Cart ID: " + cart.getCartId()
                    + ", User ID: " + cart.getUserId())
            );

            // 6. Test CartItemDAO
            System.out.println("\n6. CART ITEMS");
            System.out.println("Items in cart 1: "
                    + cartItemDAO.findByCartId(1).size());

            cartItemDAO.findByCartId(1).forEach(item ->
                System.out.println("Product ID: "
                    + item.getProductId()
                    + ", Size: " + item.getSizeLabel()
                    + ", Quantity: " + item.getQuantity()
                    + ", Unit price: " + item.getUnitPrice())
            );

            // 7. Test OrderDAO
            System.out.println("\n7. ORDERS");
            System.out.println("Total orders: "
                    + orderDAO.findAll().size());

            orderDAO.findAll().forEach(order ->
                System.out.println("Order ID: " + order.getOrderId()
                    + ", User ID: " + order.getUserId()
                    + ", Amount: " + order.getTotalAmount()
                    + ", Status: " + order.getOrderStatus())
            );

            // 8. Test OrderItemDAO
            System.out.println("\n8. ORDER ITEMS");
            System.out.println("Total order items: "
                    + orderItemDAO.findAll().size());

            orderItemDAO.findAll().forEach(item ->
                System.out.println("Order ID: " + item.getOrderId()
                    + ", Product: " + item.getProductName()
                    + ", Quantity: " + item.getQuantity()
                    + ", Subtotal: " + item.getSubtotal())
            );

            System.out.println("\n================================");
            System.out.println("   DAO TESTING COMPLETED");
            System.out.println("================================");

        } catch (SQLException e) {
            System.err.println("DAO TEST FAILED!");
            System.err.println("Database error: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("TEST FAILED!");
            e.printStackTrace();
        }
    }
}
