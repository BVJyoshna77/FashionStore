
package com.fashionstore.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

import com.fashionstore.dao.ProductDAO;
import com.fashionstore.dao.impl.ProductDAOImpl;
import com.fashionstore.model.Product;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/product")
public class ProductDetailsServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private ProductDAO productDAO;
    private com.fashionstore.dao.ProductVariantDAO variantDAO;

    @Override
    public void init() throws ServletException {
        productDAO = new ProductDAOImpl();
        variantDAO = new com.fashionstore.dao.impl.ProductVariantDAOImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        String idParam = request.getParameter("id");

        // Check whether the product ID was provided
        if (idParam == null || idParam.trim().isEmpty()) {
            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Product ID is required."
            );
            return;
        }

        int productId;

        // Validate the product ID
        try {
            productId = Integer.parseInt(idParam);
        } catch (NumberFormatException e) {
            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid product ID."
            );
            return;
        }

        // Fetch the product from the database
        try {
            Optional<Product> result =
                    productDAO.findById(productId);

            if (result.isEmpty() || !result.get().isActive()) {
                response.sendError(
                        HttpServletResponse.SC_NOT_FOUND,
                        "Product not found."
                );
                return;
            }

            // Send the product to the JSP page
            request.setAttribute("product", result.get());

            java.util.List<com.fashionstore.model.ProductVariant> variants =
                    variantDAO.findAvailableByProductId(productId);
            request.setAttribute("variants", variants);

            request.getRequestDispatcher(
                    "/WEB-INF/views/product-details.jsp"
            ).forward(request, response);

        } catch (SQLException e) {
            throw new ServletException(
                    "Unable to load product details.", e
            );
        }
    }
}
