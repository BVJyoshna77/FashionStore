<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.fashionstore.model.CartSession" %>
<%@ page import="com.fashionstore.model.CartItemDto" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<%
    CartSession cart = (CartSession) session.getAttribute("cart");
    String contextPath = request.getContextPath();
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Shopping Cart | FashionStore</title>

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

        .cart-img-thumb {
            width: 80px;
            height: 90px;
            object-fit: cover;
            border-radius: 8px;
            background-color: #f1f5f9;
        }

        .qty-control-btn {
            width: 32px;
            height: 32px;
            padding: 0;
            display: inline-flex;
            align-items: center;
            justify-content: center;
            font-weight: bold;
            border-radius: 6px;
        }

        .qty-val {
            min-width: 36px;
            text-align: center;
            font-weight: 700;
        }
    </style>
</head>
<body>

    <!-- Header Navigation -->
    <jsp:include page="navbar.jsp" />

    <main class="container my-5">
        <div class="d-flex align-items-center justify-content-between mb-4 pb-2 border-bottom">
            <h2 class="fw-bolder text-dark mb-0">Your Shopping Cart</h2>
            <% if (cart != null && !cart.isEmpty()) { %>
                <form action="<%= contextPath %>/cart" method="post" class="m-0">
                    <input type="hidden" name="action" value="clear">
                    <button type="submit" class="btn btn-outline-danger btn-sm" onclick="return confirm('Clear entire cart?');">
                        Clear Cart
                    </button>
                </form>
            <% } %>
        </div>

        <% if (cart != null && !cart.isEmpty()) { %>
            <div class="row g-4">
                <!-- Cart Items List (8 cols) -->
                <div class="col-lg-8">
                    <div class="bg-white rounded-4 border shadow-sm p-4">
                        <div class="table-responsive">
                            <table class="table align-middle mb-0">
                                <thead class="text-muted small text-uppercase">
                                    <tr>
                                        <th scope="col" style="min-width: 260px;">Product</th>
                                        <th scope="col" class="text-center">Price</th>
                                        <th scope="col" class="text-center">Quantity</th>
                                        <th scope="col" class="text-end">Total</th>
                                        <th scope="col" class="text-center">Action</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <% for (CartItemDto item : cart.getItems()) { %>
                                        <tr>
                                            <td>
                                                <div class="d-flex align-items-center gap-3">
                                                    <% if (item.getImageUrl() != null && !item.getImageUrl().trim().isEmpty()) { %>
                                                        <img src="<%= item.getImageUrl() %>" alt="<%= item.getProductName() %>" class="cart-img-thumb border">
                                                    <% } else { %>
                                                        <div class="cart-img-thumb border d-flex align-items-center justify-content-center text-muted small">No Img</div>
                                                    <% } %>
                                                    <div>
                                                        <a href="<%= contextPath %>/product?id=<%= item.getProductId() %>" class="fw-bold text-dark text-decoration-none d-block">
                                                            <%= item.getProductName() %>
                                                        </a>
                                                        <span class="badge bg-light text-dark border mt-1">
                                                            Size: <%= item.getSizeLabel() %>
                                                        </span>
                                                    </div>
                                                </div>
                                            </td>

                                            <td class="text-center">
                                                <span class="fw-bold text-dark">₹<%= item.getUnitPrice().stripTrailingZeros().toPlainString() %></span>
                                                <% if (item.getDiscountPercent().compareTo(java.math.BigDecimal.ZERO) > 0) { %>
                                                    <div class="small text-muted text-decoration-line-through">
                                                        ₹<%= item.getOriginalPrice().stripTrailingZeros().toPlainString() %>
                                                    </div>
                                                <% } %>
                                            </td>

                                            <td class="text-center">
                                                <div class="d-inline-flex align-items-center border rounded-2 p-1 bg-light">
                                                    <!-- Decrement Button -->
                                                    <form action="<%= contextPath %>/cart" method="post" class="m-0">
                                                        <input type="hidden" name="action" value="decrement">
                                                        <input type="hidden" name="itemKey" value="<%= item.getItemKey() %>">
                                                        <button type="submit" class="btn btn-outline-secondary btn-sm qty-control-btn">-</button>
                                                    </form>

                                                    <span class="qty-val"><%= item.getQuantity() %></span>

                                                    <!-- Increment Button -->
                                                    <form action="<%= contextPath %>/cart" method="post" class="m-0">
                                                        <input type="hidden" name="action" value="increment">
                                                        <input type="hidden" name="itemKey" value="<%= item.getItemKey() %>">
                                                        <button type="submit" class="btn btn-outline-secondary btn-sm qty-control-btn">+</button>
                                                    </form>
                                                </div>
                                            </td>

                                            <td class="text-end fw-bolder text-dark">
                                                ₹<%= item.getSubtotal().stripTrailingZeros().toPlainString() %>
                                            </td>

                                            <td class="text-center">
                                                <form action="<%= contextPath %>/cart" method="post" class="m-0">
                                                    <input type="hidden" name="action" value="remove">
                                                    <input type="hidden" name="itemKey" value="<%= item.getItemKey() %>">
                                                    <button type="submit" class="btn btn-link text-danger p-0" title="Remove item">
                                                        <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" fill="currentColor" viewBox="0 0 16 16">
                                                            <path d="M5.5 5.5A.5.5 0 0 1 6 6v6a.5.5 0 0 1-1 0V6a.5.5 0 0 1 .5-.5zm2.5 0a.5.5 0 0 1 .5.5v6a.5.5 0 0 1-1 0V6a.5.5 0 0 1 .5-.5zm3 .5a.5.5 0 0 0-1 0v6a.5.5 0 0 0 1 0V6z"/>
                                                            <path fill-rule="evenodd" d="M14.5 3a1 1 0 0 1-1 1H13v9a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V4h-.5a1 1 0 0 1-1-1V2a1 1 0 0 1 1-1H6a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1h3.5a1 1 0 0 1 1 1v1zM4.118 4 4 4.059V13a1 1 0 0 0 1 1h6a1 1 0 0 0 1-1V4.059L11.882 4H4.118zM2.5 3V2h11v1h-11z"/>
                                                        </svg>
                                                    </button>
                                                </form>
                                            </td>
                                        </tr>
                                    <% } %>
                                </tbody>
                            </table>
                        </div>

                        <div class="mt-4 pt-3 border-top d-flex justify-content-between align-items-center">
                            <a href="<%= contextPath %>/home" class="btn btn-outline-secondary fw-semibold">
                                Continue Shopping
                            </a>
                            <span class="text-muted small">All prices include applicable taxes</span>
                        </div>
                    </div>
                </div>

                <!-- Order Summary Sidebar (4 cols) -->
                <div class="col-lg-4">
                    <div class="bg-white rounded-4 border shadow-sm p-4 sticky-top" style="top: 80px;">
                        <h4 class="fw-bold text-dark mb-4">Order Summary</h4>

                        <div class="d-flex justify-content-between mb-2">
                            <span class="text-muted">Total Items</span>
                            <span class="fw-bold"><%= cart.getTotalItems() %> pcs</span>
                        </div>

                        <div class="d-flex justify-content-between mb-2">
                            <span class="text-muted">Subtotal</span>
                            <span class="fw-bold">₹<%= cart.getSubtotal().stripTrailingZeros().toPlainString() %></span>
                        </div>

                        <% if (cart.getTotalSavings().compareTo(java.math.BigDecimal.ZERO) > 0) { %>
                            <div class="d-flex justify-content-between mb-2 text-success">
                                <span>Discount Savings</span>
                                <span>-₹<%= cart.getTotalSavings().stripTrailingZeros().toPlainString() %></span>
                            </div>
                        <% } %>

                        <div class="d-flex justify-content-between mb-3">
                            <span class="text-muted">Delivery Fee</span>
                            <% if (cart.getShippingFee().compareTo(java.math.BigDecimal.ZERO) == 0) { %>
                                <span class="badge bg-success-subtle text-success border border-success-subtle">FREE</span>
                            <% } else { %>
                                <span class="fw-bold">₹<%= cart.getShippingFee().stripTrailingZeros().toPlainString() %></span>
                            <% } %>
                        </div>

                        <% if (cart.getSubtotal().compareTo(java.math.BigDecimal.valueOf(999)) < 0) { %>
                            <div class="alert alert-warning py-2 px-3 small rounded-3 mb-3">
                                Add <strong>₹<%= java.math.BigDecimal.valueOf(999).subtract(cart.getSubtotal()).stripTrailingZeros().toPlainString() %></strong> more to unlock <strong>FREE Delivery</strong>!
                            </div>
                        <% } %>

                        <hr class="my-3">

                        <div class="d-flex justify-content-between align-items-baseline mb-4">
                            <span class="fs-5 fw-bold text-dark">Estimated Total</span>
                            <span class="fs-3 fw-bolder text-dark">₹<%= cart.getTotalAmount().stripTrailingZeros().toPlainString() %></span>
                        </div>

                        <div class="d-grid gap-2">
                            <a href="<%= contextPath %>/checkout" class="btn btn-warning btn-lg fw-bold text-dark py-3 shadow-sm d-flex align-items-center justify-content-center gap-2">
                                <span>Proceed to Checkout</span>
                                <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" fill="currentColor" viewBox="0 0 16 16">
                                    <path fill-rule="evenodd" d="M1 8a.5.5 0 0 1 .5-.5h11.793l-3.147-3.146a.5.5 0 0 1 .708-.708l4 4a.5.5 0 0 1 0 .708l-4 4a.5.5 0 0 1-.708-.708L13.293 8.5H1.5A.5.5 0 0 1 1 8z"/>
                                </svg>
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        <% } else { %>
            <!-- Empty Cart State -->
            <div class="text-center py-5">
                <div class="bg-white p-5 rounded-4 border shadow-sm mx-auto" style="max-width: 500px;">
                    <h3 class="fw-bold text-dark">Your Cart is Empty</h3>
                    <p class="text-muted mb-4">Looks like you haven't added any fashion items to your shopping bag yet.</p>
                    <a href="<%= contextPath %>/home" class="btn btn-warning btn-lg px-5 py-3 fw-bold text-dark rounded-pill shadow-sm">
                        Start Shopping
                    </a>
                </div>
            </div>
        <% } %>
    </main>

    <!-- Footer Included -->
    <jsp:include page="partials/footer.jsp" />

</body>
</html>
