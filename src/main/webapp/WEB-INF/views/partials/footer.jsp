<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<footer class="bg-dark text-white pt-5 pb-4 mt-5 border-top border-secondary">
    <div class="container">
        <div class="row g-4">
            <!-- Brand & About -->
            <div class="col-lg-4 col-md-6">
                <h4 class="text-warning fw-bold mb-3 text-uppercase">
                    FashionStore
                </h4>
                <p class="text-light opacity-75 small leading-relaxed">
                    Discover the latest trends and timeless apparel crafted for confidence and style. Premium clothing, accessories, and footwear for every lifestyle.
                </p>
                <div class="text-warning small fw-semibold mt-3">
                    4.9 / 5.0 Customer Rating
                </div>
            </div>

            <!-- Quick Links -->
            <div class="col-lg-2 col-md-6 col-6">
                <h6 class="text-uppercase fw-bold text-white mb-3">Quick Links</h6>
                <ul class="list-unstyled small d-flex flex-column gap-2">
                    <li><a href="${pageContext.request.contextPath}/home" class="text-light text-decoration-none opacity-75 hover-opacity">Home</a></li>
                    <li><a href="${pageContext.request.contextPath}/products" class="text-light text-decoration-none opacity-75 hover-opacity">All Products</a></li>
                    <li><a href="${pageContext.request.contextPath}/cart" class="text-light text-decoration-none opacity-75 hover-opacity">Shopping Cart</a></li>
                    <li><a href="${pageContext.request.contextPath}/orders" class="text-light text-decoration-none opacity-75 hover-opacity">Track Orders</a></li>
                    <li><a href="${pageContext.request.contextPath}/login" class="text-light text-decoration-none opacity-75 hover-opacity">Sign In / Register</a></li>
                </ul>
            </div>

            <!-- Categories -->
            <div class="col-lg-2 col-md-6 col-6">
                <h6 class="text-uppercase fw-bold text-white mb-3">Categories</h6>
                <ul class="list-unstyled small d-flex flex-column gap-2">
                    <li><a href="${pageContext.request.contextPath}/products?category=Men" class="text-light text-decoration-none opacity-75 hover-opacity">Men's Wear</a></li>
                    <li><a href="${pageContext.request.contextPath}/products?category=Women" class="text-light text-decoration-none opacity-75 hover-opacity">Women's Wear</a></li>
                    <li><a href="${pageContext.request.contextPath}/products?category=Kids" class="text-light text-decoration-none opacity-75 hover-opacity">Kids Fashion</a></li>
                    <li><a href="${pageContext.request.contextPath}/products?category=Footwear" class="text-light text-decoration-none opacity-75 hover-opacity">Footwear</a></li>
                </ul>
            </div>

            <!-- Customer Care -->
            <div class="col-lg-4 col-md-6">
                <h6 class="text-uppercase fw-bold text-white mb-3">Customer Support</h6>
                <p class="text-light opacity-75 small mb-2">
                    <strong>Email:</strong> support@fashionstore.com<br>
                    <strong>Hotline:</strong> +91 1800-123-4567 (Mon-Sat, 9AM-8PM)<br>
                    <strong>Address:</strong> High Street Fashion Hub, Jubilee Hills, Hyderabad
                </p>
                <div class="badge bg-outline-light border border-secondary text-warning p-2 mt-2">
                    100% Genuine Products & Secure Payments
                </div>
            </div>
        </div>

        <hr class="border-secondary my-4">

        <div class="row align-items-center">
            <div class="col-md-6 text-center text-md-start small text-light opacity-75">
                &copy; 2026 <strong>FashionStore</strong>. All rights reserved. Built with Java 21 & Bootstrap 5.
            </div>
            <div class="col-md-6 text-center text-md-end small text-light opacity-75 mt-2 mt-md-0">
                <a href="${pageContext.request.contextPath}/home" class="text-light text-decoration-none me-3">Privacy Policy</a>
                <a href="${pageContext.request.contextPath}/home" class="text-light text-decoration-none me-3">Terms of Service</a>
                <a href="${pageContext.request.contextPath}/home" class="text-light text-decoration-none">Shipping & Returns</a>
            </div>
        </div>
    </div>
</footer>

<!-- Bootstrap 5 Bundle JS (Popper included) -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
