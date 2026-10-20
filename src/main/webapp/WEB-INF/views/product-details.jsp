<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="java.math.RoundingMode" %>
<%@ page import="java.util.List" %>
<%@ page import="com.fashionstore.model.Product" %>
<%@ page import="com.fashionstore.model.ProductVariant" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<%
    Product product = (Product) request.getAttribute("product");
    List<ProductVariant> variants = (List<ProductVariant>) request.getAttribute("variants");
    String contextPath = request.getContextPath();

    BigDecimal originalPrice = (product != null && product.getPrice() != null) ? product.getPrice() : BigDecimal.ZERO;
    BigDecimal discount = (product != null && product.getDiscountPercent() != null) ? product.getDiscountPercent() : BigDecimal.ZERO;

    BigDecimal finalPrice = originalPrice;
    BigDecimal savings = BigDecimal.ZERO;
    if (discount.compareTo(BigDecimal.ZERO) > 0) {
        BigDecimal multiplier = BigDecimal.valueOf(100).subtract(discount)
                .divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);
        finalPrice = originalPrice.multiply(multiplier).setScale(2, RoundingMode.HALF_UP);
        savings = originalPrice.subtract(finalPrice).setScale(2, RoundingMode.HALF_UP);
    }
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><%= product != null ? product.getProductName() : "Product Details" %> | FashionStore</title>

    <!-- Google Fonts -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Outfit:wght@400;500;600;700;800&family=Plus+Jakarta+Sans:wght@400;500;600;700&display=swap" rel="stylesheet">

    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">

    <link rel="stylesheet" href="<%= contextPath %>/css/style.css">

    <style>
        body {
            font-family: 'Plus Jakarta Sans', sans-serif;
            background-color: #f8fafc;
            color: #1e293b;
        }

        h1, h2, h3, h4 {
            font-family: 'Outfit', sans-serif;
        }

        .product-img-card {
            background-color: #ffffff;
            border-radius: 20px;
            overflow: hidden;
            border: 1px solid #e2e8f0;
            box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.05);
            max-height: 540px;
            display: flex;
            align-items: center;
            justify-content: center;
        }

        .product-img-card img {
            width: 100%;
            height: 100%;
            max-height: 540px;
            object-fit: cover;
        }

        .size-radio-btn {
            display: none;
        }

        .size-label-btn {
            display: inline-block;
            padding: 10px 18px;
            border: 2px solid #cbd5e1;
            border-radius: 10px;
            font-weight: 700;
            cursor: pointer;
            transition: all 0.2s;
            user-select: none;
        }

        .size-radio-btn:checked + .size-label-btn {
            border-color: #0f172a;
            background-color: #0f172a;
            color: #ffffff;
        }

        .qty-input-group {
            max-width: 140px;
        }

        .qty-btn {
            width: 40px;
            font-weight: bold;
        }
    </style>
</head>
<body>

    <!-- Header Navigation -->
    <jsp:include page="navbar.jsp" />

    <main class="container my-5">
        <!-- Breadcrumb -->
        <nav aria-label="breadcrumb" class="mb-4">
            <ol class="breadcrumb small">
                <li class="breadcrumb-item"><a href="<%= contextPath %>/home" class="text-decoration-none text-muted">Home</a></li>
                <li class="breadcrumb-item"><a href="<%= contextPath %>/products" class="text-decoration-none text-muted">Products</a></li>
                <li class="breadcrumb-item active text-dark fw-bold" aria-current="page">
                    <%= product != null ? product.getProductName() : "Product" %>
                </li>
            </ol>
        </nav>

        <% if (product != null) {
            String imageUrl = product.getImageUrl();
        %>
            <div class="row g-5 align-items-start">
                <!-- Left: Product Image -->
                <div class="col-lg-6">
                    <div class="product-img-card position-relative">
                        <% if (discount.compareTo(BigDecimal.ZERO) > 0) { %>
                            <span class="badge bg-danger position-absolute top-0 start-0 m-3 px-3 py-2 fs-6 rounded-3 shadow">
                                <%= discount.stripTrailingZeros().toPlainString() %>% OFF
                            </span>
                        <% } %>

                        <% if (imageUrl != null && !imageUrl.trim().isEmpty()) { %>
                            <img src="<%= imageUrl %>" alt="<%= product.getProductName() %>" class="img-fluid">
                        <% } else { %>
                            <div class="p-5 text-muted fs-4">No Image Available</div>
                        <% } %>
                    </div>
                </div>

                <!-- Right: Product Info & Buy Form -->
                <div class="col-lg-6">
                    <div class="bg-white p-4 p-md-5 rounded-4 border shadow-sm">
                        <span class="badge bg-success-subtle text-success border border-success-subtle px-3 py-1 mb-2 fw-semibold">
                            In Stock & Ready to Ship
                        </span>

                        <h1 class="fw-bolder text-dark mb-2 fs-2"><%= product.getProductName() %></h1>

                        <!-- Price Section -->
                        <div class="d-flex align-items-baseline gap-3 my-3 pb-3 border-bottom">
                            <span class="fs-1 fw-bolder text-dark">₹<%= finalPrice.stripTrailingZeros().toPlainString() %></span>
                            <% if (discount.compareTo(BigDecimal.ZERO) > 0) { %>
                                <span class="fs-4 text-muted text-decoration-line-through">₹<%= originalPrice.stripTrailingZeros().toPlainString() %></span>
                                <span class="badge bg-danger-subtle text-danger border border-danger-subtle px-2 py-1 fs-6">
                                    Save ₹<%= savings.stripTrailingZeros().toPlainString() %>
                                </span>
                            <% } %>
                        </div>

                        <!-- Description -->
                        <div class="mb-4">
                            <h6 class="fw-bold text-dark text-uppercase small tracking-wide">Product Details</h6>
                            <p class="text-secondary leading-relaxed">
                                <%= product.getDescription() != null ? product.getDescription() : "High quality contemporary fashion piece crafted with top grade fabric, perfect for modern comfort and effortless elegance." %>
                            </p>
                        </div>

                        <!-- Add to Cart Form -->
                        <form action="<%= contextPath %>/cart" method="post">
                            <input type="hidden" name="action" value="add">
                            <input type="hidden" name="productId" value="<%= product.getProductId() %>">
                            <input type="hidden" name="redirect" value="/cart">

                            <!-- Size Selection -->
                            <div class="mb-4">
                                <label class="form-label fw-bold text-dark text-uppercase small d-flex justify-content-between">
                                    <span>Select Size</span>
                                    <span class="text-muted fw-normal">Standard Fit</span>
                                </label>
                                <div class="d-flex flex-wrap gap-2">
                                    <% if (variants != null && !variants.isEmpty()) {
                                        boolean first = true;
                                        for (ProductVariant v : variants) {
                                            String size = v.getSizeLabel();
                                    %>
                                        <input type="radio" class="size-radio-btn" name="sizeLabel" id="size_<%= size %>" value="<%= size %>" <%= first ? "checked" : "" %>>
                                        <label class="size-label-btn" for="size_<%= size %>">
                                            <%= size %>
                                            <span class="d-block small text-muted fw-normal" style="font-size: 10px;">
                                                (<%= v.getStockQuantity() %> left)
                                            </span>
                                        </label>
                                    <%      first = false;
                                        }
                                       } else { %>
                                        <input type="radio" class="size-radio-btn" name="sizeLabel" id="size_S" value="S">
                                        <label class="size-label-btn" for="size_S">S</label>

                                        <input type="radio" class="size-radio-btn" name="sizeLabel" id="size_M" value="M" checked>
                                        <label class="size-label-btn" for="size_M">M</label>

                                        <input type="radio" class="size-radio-btn" name="sizeLabel" id="size_L" value="L">
                                        <label class="size-label-btn" for="size_L">L</label>

                                        <input type="radio" class="size-radio-btn" name="sizeLabel" id="size_XL" value="XL">
                                        <label class="size-label-btn" for="size_XL">XL</label>
                                    <% } %>
                                </div>
                            </div>

                            <!-- Quantity Picker -->
                            <div class="mb-4">
                                <label class="form-label fw-bold text-dark text-uppercase small">Quantity</label>
                                <div class="input-group qty-input-group">
                                    <button class="btn btn-outline-secondary qty-btn" type="button" onclick="decrementQty()">-</button>
                                    <input type="number" id="qtyInput" name="quantity" class="form-control text-center fw-bold" value="1" min="1" max="10">
                                    <button class="btn btn-outline-secondary qty-btn" type="button" onclick="incrementQty()">+</button>
                                </div>
                            </div>

                            <!-- Action Buttons -->
                            <div class="d-grid gap-2 mb-4">
                                <button type="submit" class="btn btn-dark btn-lg py-3 fw-bold shadow-sm d-flex align-items-center justify-content-center gap-2">
                                    <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" fill="currentColor" viewBox="0 0 16 16">
                                        <path d="M0 1.5A.5.5 0 0 1 .5 1H2a.5.5 0 0 1 .485.379L2.89 3H14.5a.5.5 0 0 1 .491.592l-1.5 8A.5.5 0 0 1 13 12H4a.5.5 0 0 1-.491-.408L2.01 3.607 1.61 2H.5a.5.5 0 0 1-.5-.5zM3.102 4l1.313 7h8.17l1.313-7H3.102z"/>
                                    </svg>
                                    Add to Cart
                                </button>
                                <a href="<%= contextPath %>/home" class="btn btn-outline-secondary py-2 fw-semibold">
                                    Continue Shopping
                                </a>
                            </div>
                        </form>

                        <!-- Assurance Badges -->
                        <div class="row g-2 pt-3 border-top text-muted small">
                            <div class="col-6">Free Shipping on ₹999+</div>
                            <div class="col-6">7 Days Easy Returns</div>
                            <div class="col-6">100% Genuine Guaranteed</div>
                            <div class="col-6">Secure Payment</div>
                        </div>

                    </div>
                </div>
            </div>
        <% } else { %>
            <div class="text-center py-5">
                <h3 class="fw-bold">Product Not Found</h3>
                <p class="text-muted">The product you are looking for is unavailable or deactivated.</p>
                <a href="<%= contextPath %>/home" class="btn btn-dark px-4 py-2">Back to Catalog</a>
            </div>
        <% } %>
    </main>

    <!-- Footer Included -->
    <jsp:include page="partials/footer.jsp" />

    <script>
        function incrementQty() {
            var input = document.getElementById('qtyInput');
            var val = parseInt(input.value) || 1;
            if (val < 10) input.value = val + 1;
        }
        function decrementQty() {
            var input = document.getElementById('qtyInput');
            var val = parseInt(input.value) || 1;
            if (val > 1) input.value = val - 1;
        }
    </script>
</body>
</html>
