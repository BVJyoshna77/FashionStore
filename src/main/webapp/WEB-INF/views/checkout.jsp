<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.fashionstore.model.CartSession" %>
<%@ page import="com.fashionstore.model.CartItemDto" %>
<%@ page import="com.fashionstore.model.User" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<%
    CartSession cart = (CartSession) session.getAttribute("cart");
    User user = (User) session.getAttribute("user");
    String contextPath = request.getContextPath();
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Checkout | FashionStore</title>

    <!-- Google Fonts -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Outfit:wght@500;600;700;800&family=Plus+Jakarta+Sans:wght@400;500;600;700&display=swap" rel="stylesheet">

    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="<%= contextPath %>/css/style.css">

    <style>
        body {
            font-family: 'Plus Jakarta Sans', sans-serif;
            background-color: #f8fafc;
            color: #1e293b;
        }
        h1, h2, h3, h4 { font-family: 'Outfit', sans-serif; }
    </style>
</head>
<body>

    <!-- Header Navigation -->
    <jsp:include page="navbar.jsp" />

    <main class="container my-5">
        <h2 class="fw-bolder text-dark mb-4 pb-2 border-bottom">Secure Checkout</h2>

        <c:if test="${not empty error}">
            <div class="alert alert-danger py-2 px-3 small rounded-3 mb-4" role="alert">
                ${error}
            </div>
        </c:if>

        <form action="<%= contextPath %>/checkout" method="post">
            <div class="row g-5">
                <!-- Delivery & Contact Info Form (7 cols) -->
                <div class="col-lg-7">
                    <div class="bg-white p-4 p-md-5 rounded-4 border shadow-sm mb-4">
                        <h4 class="fw-bold text-dark mb-4">
                            Delivery Address
                        </h4>

                        <div class="row g-3">
                            <div class="col-12">
                                <label class="form-label small fw-bold text-uppercase text-muted">Full Recipient Name *</label>
                                <input type="text" class="form-control" name="fullName" value="<%= user != null ? user.getFullName() : "" %>" placeholder="e.g. Rahul Kumar" required>
                            </div>

                            <div class="col-md-6">
                                <label class="form-label small fw-bold text-uppercase text-muted">Email Address</label>
                                <input type="email" class="form-control" name="email" value="<%= user != null ? user.getEmail() : "" %>" placeholder="For order updates">
                            </div>

                            <div class="col-md-6">
                                <label class="form-label small fw-bold text-uppercase text-muted">Phone Number *</label>
                                <input type="tel" class="form-control" name="phone" value="<%= user != null ? user.getPhone() : "" %>" placeholder="10-digit mobile" required>
                            </div>

                            <div class="col-12">
                                <label class="form-label small fw-bold text-uppercase text-muted">Street Address & Flat / House No *</label>
                                <input type="text" class="form-control" name="address" value="<%= user != null ? user.getAddress() : "" %>" placeholder="Flat 402, Sunshine Apts, Banjara Hills" required>
                            </div>

                            <div class="col-md-4">
                                <label class="form-label small fw-bold text-uppercase text-muted">City *</label>
                                <input type="text" class="form-control" name="city" value="Hyderabad" required>
                            </div>

                            <div class="col-md-4">
                                <label class="form-label small fw-bold text-uppercase text-muted">State *</label>
                                <input type="text" class="form-control" name="state" value="Telangana" required>
                            </div>

                            <div class="col-md-4">
                                <label class="form-label small fw-bold text-uppercase text-muted">PIN Code *</label>
                                <input type="text" class="form-control" name="postalCode" value="500034" required>
                            </div>
                        </div>
                    </div>

                    <!-- Payment Options -->
                    <div class="bg-white p-4 p-md-5 rounded-4 border shadow-sm">
                        <h4 class="fw-bold text-dark mb-3">
                            Payment Method
                        </h4>
                        <div class="alert alert-info py-2 px-3 small rounded-3 mb-3">
                            <strong>Payment Notice:</strong> Orders will be processed safely with instant confirmation.
                        </div>

                        <div class="form-check p-3 border rounded-3 mb-2 bg-light">
                            <input class="form-check-input ms-0 me-2" type="radio" name="paymentMethod" id="payCod" value="Cash on Delivery" checked>
                            <label class="form-check-label fw-bold" for="payCod">
                                Cash on Delivery (COD)
                                <span class="d-block small text-muted fw-normal">Pay with cash upon delivery at your doorstep</span>
                            </label>
                        </div>

                        <div class="form-check p-3 border rounded-3 mb-2">
                            <input class="form-check-input ms-0 me-2" type="radio" name="paymentMethod" id="payUpi" value="UPI Demo">
                            <label class="form-check-label fw-bold" for="payUpi">
                                UPI / QR Payment
                                <span class="d-block small text-muted fw-normal">Google Pay, PhonePe, Paytm simulation</span>
                            </label>
                        </div>

                        <div class="form-check p-3 border rounded-3">
                            <input class="form-check-input ms-0 me-2" type="radio" name="paymentMethod" id="payCard" value="Credit/Debit Card Demo">
                            <label class="form-check-label fw-bold" for="payCard">
                                Credit / Debit Card
                                <span class="d-block small text-muted fw-normal">Visa, MasterCard, RuPay simulation</span>
                            </label>
                        </div>
                    </div>
                </div>

                <!-- Order Summary Sidebar (5 cols) -->
                <div class="col-lg-5">
                    <div class="bg-white p-4 rounded-4 border shadow-sm sticky-top" style="top: 80px;">
                        <h4 class="fw-bold text-dark mb-3">Order Items (<%= cart != null ? cart.getTotalItems() : 0 %>)</h4>

                        <% if (cart != null && !cart.isEmpty()) { %>
                            <div class="border-bottom pb-3 mb-3" style="max-height: 260px; overflow-y: auto;">
                                <% for (CartItemDto item : cart.getItems()) { %>
                                    <div class="d-flex justify-content-between align-items-center mb-2">
                                        <div class="d-flex align-items-center gap-2">
                                            <% if (item.getImageUrl() != null && !item.getImageUrl().trim().isEmpty()) { %>
                                                <img src="<%= item.getImageUrl() %>" alt="<%= item.getProductName() %>" style="width: 45px; height: 50px; object-fit: cover; border-radius: 6px;">
                                            <% } %>
                                            <div>
                                                <div class="fw-bold small text-dark"><%= item.getProductName() %></div>
                                                <div class="text-muted" style="font-size: 11px;">Size: <%= item.getSizeLabel() %> × <%= item.getQuantity() %></div>
                                            </div>
                                        </div>
                                        <div class="fw-bold small">₹<%= item.getSubtotal().stripTrailingZeros().toPlainString() %></div>
                                    </div>
                                <% } %>
                            </div>

                            <div class="d-flex justify-content-between mb-2 small">
                                <span class="text-muted">Subtotal</span>
                                <span class="fw-bold">₹<%= cart.getSubtotal().stripTrailingZeros().toPlainString() %></span>
                            </div>

                            <div class="d-flex justify-content-between mb-2 small text-success">
                                <span>Discount Savings</span>
                                <span>-₹<%= cart.getTotalSavings().stripTrailingZeros().toPlainString() %></span>
                            </div>

                            <div class="d-flex justify-content-between mb-3 small">
                                <span class="text-muted">Delivery</span>
                                <% if (cart.getShippingFee().compareTo(java.math.BigDecimal.ZERO) == 0) { %>
                                    <span class="badge bg-success-subtle text-success">FREE</span>
                                <% } else { %>
                                    <span class="fw-bold">₹<%= cart.getShippingFee().stripTrailingZeros().toPlainString() %></span>
                                <% } %>
                            </div>

                            <hr class="my-3">

                            <div class="d-flex justify-content-between align-items-baseline mb-4">
                                <span class="fs-5 fw-bold text-dark">Total Payable</span>
                                <span class="fs-2 fw-bolder text-dark">₹<%= cart.getTotalAmount().stripTrailingZeros().toPlainString() %></span>
                            </div>

                            <div class="d-grid gap-2">
                                <button type="submit" class="btn btn-warning btn-lg fw-bold text-dark py-3 shadow">
                                    Place Order Now
                                </button>
                                <a href="<%= contextPath %>/cart" class="btn btn-outline-secondary py-2 small">
                                    Back to Cart
                                </a>
                            </div>
                        <% } %>
                    </div>
                </div>
            </div>
        </form>
    </main>

    <!-- Footer Included -->
    <jsp:include page="partials/footer.jsp" />

</body>
</html>
