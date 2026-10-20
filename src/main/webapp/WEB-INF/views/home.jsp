<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="java.math.RoundingMode" %>
<%@ page import="com.fashionstore.model.Product" %>
<%@ page import="com.fashionstore.model.Category" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<%
    List<Product> products = (List<Product>) request.getAttribute("products");
    List<Category> categories = (List<Category>) request.getAttribute("categories");
    Integer selectedCategoryId = (Integer) request.getAttribute("selectedCategoryId");
    String selectedCategoryName = (String) request.getAttribute("selectedCategoryName");
    Boolean isFiltered = (Boolean) request.getAttribute("isFiltered");
    String search = (String) request.getAttribute("search");
    String contextPath = request.getContextPath();
    boolean hasFilter = (isFiltered != null && isFiltered) || selectedCategoryId != null || (search != null && !search.trim().isEmpty());
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>FashionStore | Discover Your Style</title>

    <!-- Google Fonts: Inter & Outfit -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Outfit:wght@300;400;500;600;700;800&family=Plus+Jakarta+Sans:wght@400;500;600;700&display=swap" rel="stylesheet">

    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">

    <!-- Custom Styles -->
    <link rel="stylesheet" href="<%= contextPath %>/css/style.css">
    <link rel="stylesheet" href="<%= contextPath %>/css/home.css">

    <style>
        body {
            font-family: 'Plus Jakarta Sans', sans-serif;
            background-color: #f8fafc;
            color: #1e293b;
        }

        h1, h2, h3, h4, .navbar-brand {
            font-family: 'Outfit', sans-serif;
        }

        /* Hero Banner with Background Image & Gradient Overlay */
        .hero-banner {
            position: relative;
            min-height: 520px;
            background: linear-gradient(rgba(15, 23, 42, 0.72), rgba(15, 23, 42, 0.82)),
                        url('<%= contextPath %>/images/hero-banner.jpg') center/cover no-repeat;
            display: flex;
            align-items: center;
        }

        .hero-badge {
            display: inline-block;
            background-color: #f59e0b;
            color: #0f172a;
            font-size: 13px;
            font-weight: 700;
            letter-spacing: 2px;
            padding: 6px 16px;
            border-radius: 30px;
            margin-bottom: 18px;
            text-transform: uppercase;
        }

        .hero-title {
            font-size: clamp(2.5rem, 5vw, 4rem);
            font-weight: 800;
            color: #ffffff;
            line-height: 1.15;
            margin-bottom: 20px;
        }

        .hero-subtitle {
            font-size: clamp(1rem, 2vw, 1.25rem);
            color: #cbd5e1;
            max-width: 620px;
            line-height: 1.6;
            margin-bottom: 30px;
        }

        .btn-shop-now {
            background-color: #f59e0b;
            color: #0f172a;
            font-weight: 700;
            font-size: 17px;
            padding: 14px 34px;
            border-radius: 50px;
            text-decoration: none;
            transition: all 0.3s ease;
            box-shadow: 0 10px 25px rgba(245, 158, 11, 0.35);
            display: inline-flex;
            align-items: center;
            gap: 10px;
            border: none;
        }

        .btn-shop-now:hover {
            background-color: #d97706;
            color: #ffffff;
            transform: translateY(-2px);
            box-shadow: 0 15px 30px rgba(245, 158, 11, 0.45);
        }

        /* Features Bar */
        .features-strip {
            background-color: #ffffff;
            border-bottom: 1px solid #e2e8f0;
            padding: 22px 0;
        }

        .feature-item {
            display: flex;
            align-items: center;
            gap: 14px;
        }

        .feature-icon {
            width: 44px;
            height: 44px;
            border-radius: 12px;
            background-color: #fef3c7;
            color: #d97706;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 20px;
            flex-shrink: 0;
        }

        /* Category Chips */
        .category-chip-btn {
            display: inline-block;
            padding: 10px 22px;
            border-radius: 30px;
            border: 1px solid #cbd5e1;
            background-color: #ffffff;
            color: #334155;
            font-weight: 600;
            font-size: 14px;
            text-decoration: none;
            transition: all 0.2s ease;
            box-shadow: 0 2px 4px rgba(0,0,0,0.02);
        }

        .category-chip-btn:hover {
            border-color: #0f172a;
            background-color: #f8fafc;
            color: #0f172a;
        }

        .category-chip-btn.active {
            background-color: #0f172a;
            border-color: #0f172a;
            color: #ffffff;
            box-shadow: 0 4px 12px rgba(15, 23, 42, 0.25);
        }

        /* Product Cards */
        .product-card-modern {
            background-color: #ffffff;
            border-radius: 16px;
            border: 1px solid #e2e8f0;
            overflow: hidden;
            transition: all 0.3s cubic-bezier(0.16, 1, 0.3, 1);
            height: 100%;
            display: flex;
            flex-direction: column;
            box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.05);
        }

        .product-card-modern:hover {
            transform: translateY(-6px);
            box-shadow: 0 20px 25px -5px rgba(0, 0, 0, 0.1), 0 8px 10px -6px rgba(0, 0, 0, 0.05);
            border-color: #cbd5e1;
        }

        .product-img-wrap {
            position: relative;
            height: 260px;
            background-color: #f1f5f9;
            overflow: hidden;
        }

        .product-img-wrap img {
            width: 100%;
            height: 100%;
            object-fit: cover;
            transition: transform 0.5s ease;
        }

        .product-card-modern:hover .product-img-wrap img {
            transform: scale(1.05);
        }

        .discount-tag {
            position: absolute;
            top: 12px;
            left: 12px;
            background-color: #ef4444;
            color: #ffffff;
            font-size: 12px;
            font-weight: 700;
            padding: 4px 10px;
            border-radius: 6px;
            box-shadow: 0 2px 6px rgba(239, 68, 68, 0.4);
        }

        .product-body {
            padding: 18px;
            display: flex;
            flex-direction: column;
            flex-grow: 1;
        }

        .product-title-link {
            font-size: 16px;
            font-weight: 700;
            color: #0f172a;
            text-decoration: none;
            display: -webkit-box;
            -webkit-line-clamp: 1;
            -webkit-box-orient: vertical;
            overflow: hidden;
            margin-bottom: 6px;
            transition: color 0.2s;
        }

        .product-title-link:hover {
            color: #d97706;
        }

        .product-desc-text {
            font-size: 13px;
            color: #64748b;
            display: -webkit-box;
            -webkit-line-clamp: 2;
            -webkit-box-orient: vertical;
            overflow: hidden;
            line-height: 1.5;
            margin-bottom: 14px;
            flex-grow: 1;
        }

        .price-box {
            display: flex;
            align-items: baseline;
            gap: 8px;
            margin-bottom: 14px;
        }

        .current-price {
            font-size: 19px;
            font-weight: 800;
            color: #0f172a;
        }

        .original-price {
            font-size: 14px;
            color: #94a3b8;
            text-decoration: line-through;
        }

        .card-actions {
            display: grid;
            grid-template-columns: 1fr auto;
            gap: 8px;
        }
    </style>
</head>
<body>

    <!-- Header Navigation Bar -->
    <jsp:include page="navbar.jsp" />

    <% if (hasFilter) { %>
        <!-- Category & Search Header Banner -->
        <div class="bg-dark text-white py-4 border-bottom border-warning border-3 shadow-sm mb-4">
            <div class="container d-flex flex-column flex-md-row justify-content-between align-items-md-center gap-3">
                <div>
                    <nav aria-label="breadcrumb">
                        <ol class="breadcrumb mb-1 small text-uppercase" style="letter-spacing: 1px;">
                            <li class="breadcrumb-item"><a href="<%= contextPath %>/home" class="text-warning text-decoration-none">Home</a></li>
                            <li class="breadcrumb-item"><a href="<%= contextPath %>/products" class="text-secondary text-decoration-none">Shop</a></li>
                            <li class="breadcrumb-item active text-white" aria-current="page">
                                <%= selectedCategoryName != null ? selectedCategoryName : (search != null ? "Search" : "Filtered") %>
                            </li>
                        </ol>
                    </nav>
                    <h2 class="fw-bold mb-0 text-white">
                        <% if (selectedCategoryName != null) { %>
                            <%= selectedCategoryName %> Collection
                        <% } else if (search != null) { %>
                            Results for "<%= search %>"
                        <% } else { %>
                            Category Catalog
                        <% } %>
                    </h2>
                </div>
                <div>
                    <span class="badge bg-warning text-dark px-3 py-2 fw-bold fs-6">
                        <%= products != null ? products.size() : 0 %> Products Available
                    </span>
                </div>
            </div>
        </div>
    <% } else { %>
        <!-- Hero Banner Section -->
        <section class="hero-banner">
            <div class="container py-5">
                <div class="row align-items-center">
                    <div class="col-lg-8 col-xl-7">
                        <span class="hero-badge">NEW COLLECTION 2026</span>
                        <h1 class="hero-title">Discover Your Style</h1>
                        <p class="hero-subtitle">
                            Explore fashion for every day and every occasion. From timeless urban essentials to elegant ethnic wear, crafted with supreme comfort and unmatched sophistication.
                        </p>
                        <div class="d-flex flex-wrap gap-3">
                            <a href="#products" class="btn-shop-now">
                                Shop Now
                                <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" fill="currentColor" viewBox="0 0 16 16">
                                    <path fill-rule="evenodd" d="M1 8a.5.5 0 0 1 .5-.5h11.793l-3.147-3.146a.5.5 0 0 1 .708-.708l4 4a.5.5 0 0 1 0 .708l-4 4a.5.5 0 0 1-.708-.708L13.293 8.5H1.5A.5.5 0 0 1 1 8z"/>
                                </svg>
                            </a>
                            <a href="<%= contextPath %>/products?category=Women" class="btn btn-outline-light btn-lg px-4 py-3 rounded-pill fw-semibold">
                                Women's Edit
                            </a>
                            <a href="<%= contextPath %>/products?category=Men" class="btn btn-outline-light btn-lg px-4 py-3 rounded-pill fw-semibold">
                                Men's Edit
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        </section>

        <!-- Value Proposition Features Strip -->
        <section class="features-strip shadow-sm">
            <div class="container">
                <div class="row g-4 text-start">
                    <div class="col-6 col-lg-3">
                        <div class="feature-item ps-3 border-start border-2 border-warning">
                            <div>
                                <h6 class="fw-bold mb-0 text-dark">Free Delivery</h6>
                                <span class="small text-muted">On all orders over ₹999</span>
                            </div>
                        </div>
                    </div>
                    <div class="col-6 col-lg-3">
                        <div class="feature-item ps-3 border-start border-2 border-warning">
                            <div>
                                <h6 class="fw-bold mb-0 text-dark">7 Days Easy Return</h6>
                                <span class="small text-muted">Hassle-free exchange policy</span>
                            </div>
                        </div>
                    </div>
                    <div class="col-6 col-lg-3">
                        <div class="feature-item ps-3 border-start border-2 border-warning">
                            <div>
                                <h6 class="fw-bold mb-0 text-dark">100% Authentic</h6>
                                <span class="small text-muted">Direct from certified brands</span>
                            </div>
                        </div>
                    </div>
                    <div class="col-6 col-lg-3">
                        <div class="feature-item ps-3 border-start border-2 border-warning">
                            <div>
                                <h6 class="fw-bold mb-0 text-dark">Secure Checkout</h6>
                                <span class="small text-muted">Protected payment demo</span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </section>
    <% } %>

    <!-- Main Catalog Section with Left Category Sidebar -->
    <main class="container my-5" id="products">
        <div class="row g-4">
            <!-- Left Sidebar: Shop by Category -->
            <aside class="col-lg-3 col-md-4">
                <div class="card border-0 shadow-sm rounded-4 overflow-hidden sticky-top" style="top: 80px;">
                    <div class="bg-dark text-white p-3 d-flex align-items-center justify-content-between">
                        <h5 class="fw-bold mb-0 fs-6 text-uppercase" style="letter-spacing: 1px;">Shop by Category</h5>
                    </div>
                    <div class="p-2 bg-white">
                        <div class="list-group list-group-flush">
                            <a href="<%= contextPath %>/home"
                               class="list-group-item list-group-item-action d-flex justify-content-between align-items-center py-2 px-3 rounded-3 mb-1 border-0 <%= selectedCategoryId == null ? "bg-warning text-dark fw-bold shadow-sm" : "text-dark" %>">
                                <span>All Products</span>
                                <span class="badge <%= selectedCategoryId == null ? "bg-dark text-white" : "bg-light text-muted border" %> rounded-pill">
                                    All
                                </span>
                            </a>

                            <% if (categories != null) {
                                for (Category cat : categories) {
                                    boolean isSelected = selectedCategoryId != null && selectedCategoryId == cat.getCategoryId();
                            %>
                                <a href="<%= contextPath %>/home?categoryId=<%= cat.getCategoryId() %>"
                                   class="list-group-item list-group-item-action d-flex justify-content-between align-items-center py-2 px-3 rounded-3 mb-1 border-0 <%= isSelected ? "bg-warning text-dark fw-bold shadow-sm" : "text-dark" %>">
                                    <span><%= cat.getCategoryName() %></span>
                                    <% if (isSelected) { %>
                                        <span class="badge bg-dark text-white rounded-pill">Active</span>
                                    <% } %>
                                </a>
                            <%  }
                               } %>
                        </div>
                    </div>
                </div>
            </aside>

            <!-- Right Column: Products Listing -->
            <section class="col-lg-9 col-md-8">
                <!-- Product Listing Header & Search -->
                <div class="d-flex flex-column flex-md-row justify-content-between align-items-md-center gap-3 mb-4 pb-2 border-bottom">
                    <div>
                        <h3 class="fw-bold text-dark mb-1">
                            <% if (search != null && !search.trim().isEmpty()) { %>
                                Search Results for "<span class="text-warning text-dark"><%= search %></span>"
                            <% } else if (selectedCategoryName != null) { %>
                                <%= selectedCategoryName %> Collection
                            <% } else if (selectedCategoryId != null) { %>
                                Category Collection
                            <% } else { %>
                                Featured Fashion Products
                            <% } %>
                        </h3>
                        <p class="text-muted small mb-0">Showing <%= products != null ? products.size() : 0 %> premium items available</p>
                    </div>

                    <!-- Search Form -->
                    <form action="<%= contextPath %>/home" method="get" class="d-flex gap-2" style="max-width: 360px;">
                        <input type="text" name="search" class="form-control" placeholder="Search clothes, shoes..." value="<%= search != null ? search : "" %>">
                        <button type="submit" class="btn btn-dark px-4 fw-semibold">Search</button>
                        <% if ((search != null && !search.isEmpty()) || selectedCategoryId != null) { %>
                            <a href="<%= contextPath %>/home" class="btn btn-outline-secondary">Reset</a>
                        <% } %>
                    </form>
                </div>

                <!-- Product Grid -->
                <div class="row g-3 g-lg-4">
                    <% if (products != null && !products.isEmpty()) {
                        for (Product product : products) {
                            String imageUrl = product.getImageUrl();
                            BigDecimal originalPrice = product.getPrice() != null ? product.getPrice() : BigDecimal.ZERO;
                            BigDecimal discount = product.getDiscountPercent() != null ? product.getDiscountPercent() : BigDecimal.ZERO;

                            BigDecimal finalPrice = originalPrice;
                            if (discount.compareTo(BigDecimal.ZERO) > 0) {
                                BigDecimal multiplier = BigDecimal.valueOf(100).subtract(discount)
                                        .divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);
                                finalPrice = originalPrice.multiply(multiplier).setScale(2, RoundingMode.HALF_UP);
                            }
                    %>
                        <div class="col-12 col-sm-6 col-lg-4">
                            <div class="product-card-modern">
                                <!-- Product Image -->
                                <div class="product-img-wrap">
                                    <% if (discount.compareTo(BigDecimal.ZERO) > 0) { %>
                                        <span class="discount-tag">
                                            <%= discount.stripTrailingZeros().toPlainString() %>% OFF
                                        </span>
                                    <% } %>

                                    <a href="<%= contextPath %>/product?id=<%= product.getProductId() %>">
                                        <% if (imageUrl != null && !imageUrl.trim().isEmpty()) { %>
                                            <img src="<%= imageUrl %>" alt="<%= product.getProductName() %>" loading="lazy">
                                        <% } else { %>
                                            <div class="d-flex align-items-center justify-content-center h-100 text-muted">
                                                No Image
                                            </div>
                                        <% } %>
                                    </a>
                                </div>

                                <!-- Product Information -->
                                <div class="product-body">
                                    <a href="<%= contextPath %>/product?id=<%= product.getProductId() %>" class="product-title-link">
                                        <%= product.getProductName() %>
                                    </a>

                                    <div class="price-box">
                                        <span class="current-price">₹<%= finalPrice.stripTrailingZeros().toPlainString() %></span>
                                        <% if (discount.compareTo(BigDecimal.ZERO) > 0) { %>
                                            <span class="original-price">₹<%= originalPrice.stripTrailingZeros().toPlainString() %></span>
                                        <% } %>
                                    </div>

                                    <p class="product-desc-text">
                                        <%= product.getDescription() != null ? product.getDescription() : "Contemporary fashion staple." %>
                                    </p>

                                    <!-- Card Action Buttons -->
                                    <div class="card-actions mt-auto">
                                        <form action="<%= contextPath %>/cart" method="post" class="m-0">
                                            <input type="hidden" name="action" value="add">
                                            <input type="hidden" name="productId" value="<%= product.getProductId() %>">
                                            <input type="hidden" name="quantity" value="1">
                                            <input type="hidden" name="sizeLabel" value="M">
                                            <input type="hidden" name="redirect" value="/home">
                                            <button type="submit" class="btn btn-warning btn-sm w-100 fw-bold py-2 d-flex align-items-center justify-content-center gap-1 text-dark">
                                                <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" fill="currentColor" viewBox="0 0 16 16">
                                                    <path d="M0 1.5A.5.5 0 0 1 .5 1H2a.5.5 0 0 1 .485.379L2.89 3H14.5a.5.5 0 0 1 .491.592l-1.5 8A.5.5 0 0 1 13 12H4a.5.5 0 0 1-.491-.408L2.01 3.607 1.61 2H.5a.5.5 0 0 1-.5-.5zM3.102 4l1.313 7h8.17l1.313-7H3.102z"/>
                                                </svg>
                                                Add to Cart
                                            </button>
                                        </form>

                                        <a href="<%= contextPath %>/product?id=<%= product.getProductId() %>" class="btn btn-outline-dark btn-sm py-2 px-3 fw-semibold" title="View Details">
                                            Details
                                        </a>
                                    </div>
                                </div>
                            </div>
                        </div>
                    <%  }
                       } else { %>
                        <div class="col-12 py-5 text-center">
                            <div class="bg-white p-5 rounded-4 shadow-sm border mx-auto" style="max-width: 500px;">
                                <h4 class="fw-bold">No Products Found</h4>
                                <p class="text-muted">We couldn't find any items matching your category or search term.</p>
                                <a href="<%= contextPath %>/home" class="btn btn-warning px-4 py-2 fw-bold text-dark">View All Products</a>
                            </div>
                        </div>
                    <% } %>
                </div>
            </section>
        </div>
    </main>

    <!-- Footer Included -->
    <jsp:include page="partials/footer.jsp" />

</body>
</html>
