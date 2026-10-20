
package com.fashionstore.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.fashionstore.dao.ProductDAO;
import com.fashionstore.dao.CategoryDAO;
import com.fashionstore.dao.impl.ProductDAOImpl;
import com.fashionstore.dao.impl.CategoryDAOImpl;
import com.fashionstore.model.Product;
import com.fashionstore.model.Category;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet({"/home", "/products"})
public class HomeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private ProductDAO productDAO;
    private CategoryDAO categoryDAO;

    @Override
    public void init() throws ServletException {
        productDAO = new ProductDAOImpl();
        categoryDAO = new CategoryDAOImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        try {
            List<Category> categories =
                    categoryDAO.findAllActive();

            String categoryParam = request.getParameter("categoryId");
            String categoryNameParam = request.getParameter("category");
            String keyword = request.getParameter("search");
            if (keyword == null || keyword.trim().isEmpty()) {
                keyword = request.getParameter("keyword"); // support both 'search' and 'keyword'
            }

            String selectedCategoryName = null;

            // If category was passed as name (e.g. category=Men)
            if ((categoryParam == null || categoryParam.trim().isEmpty()) && categoryNameParam != null && !categoryNameParam.trim().isEmpty()) {
                for (Category cat : categories) {
                    if (cat.getCategoryName().trim().equalsIgnoreCase(categoryNameParam.trim())) {
                        categoryParam = String.valueOf(cat.getCategoryId());
                        selectedCategoryName = cat.getCategoryName();
                        break;
                    }
                }
                if (selectedCategoryName == null) {
                    selectedCategoryName = categoryNameParam.trim();
                }
            }

            List<Product> products;

            if (keyword != null && !keyword.trim().isEmpty()) {
                products = productDAO.searchActiveByName(
                        keyword.trim());
                request.setAttribute("search", keyword.trim());
                request.setAttribute("isFiltered", true);

            } else if (categoryParam != null
                    && !categoryParam.trim().isEmpty()) {

                try {
                    int categoryId =
                            Integer.parseInt(categoryParam);

                    products =
                            productDAO.findActiveByCategoryId(
                                    categoryId);

                    request.setAttribute(
                            "selectedCategoryId", categoryId);
                    request.setAttribute("isFiltered", true);

                    if (selectedCategoryName == null) {
                        for (Category cat : categories) {
                            if (cat.getCategoryId() == categoryId) {
                                selectedCategoryName = cat.getCategoryName();
                                break;
                            }
                        }
                    }
                    request.setAttribute("selectedCategoryName", selectedCategoryName);

                } catch (NumberFormatException e) {
                    response.sendError(
                            HttpServletResponse.SC_BAD_REQUEST,
                            "Invalid category ID");
                    return;
                }

            } else {
                products = productDAO.findAllActive();
            }

            request.setAttribute("categories", categories);
            request.setAttribute("products", products);

            request.getRequestDispatcher(
                    "/WEB-INF/views/home.jsp")
                    .forward(request, response);

        } catch (SQLException e) {
            throw new ServletException(
                    "Unable to load products and categories", e);
        }
    }
}
