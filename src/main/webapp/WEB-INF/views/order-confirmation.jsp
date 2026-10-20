<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.fashionstore.model.Order" %>
<%@ page import="com.fashionstore.model.OrderItem" %>
<%@ page import="java.util.List" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<%
    Order order = (Order) request.getAttribute("order");
    List<OrderItem> items = (List<OrderItem>) request.getAttribute("items");
    String contextPath = request.getContextPath();
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Order Confirmed | FashionStore</title>

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
        <div class="row justify-content-center">
            <div class="col-lg-8">
                <div class="bg-white p-4 p-md-5 rounded-4 border shadow-sm text-center mb-4">
                    <div class="mb-3">
                        <span class="badge bg-success text-white px-4 py-2 fs-6 rounded-pill text-uppercase">Order Confirmed</span>
                    </div>
                    <h2 class="fw-bolder text-dark mb-2">Thank you for your order!</h2>
                    <p class="text-muted mb-4">We've received your request and will prepare your fashion items right away.</p>

                    <div class="bg-light p-3 rounded-3 d-inline-block text-start mb-4 border">
                        <div><strong>Order ID:</strong> #FS-<%= order != null ? order.getOrderId() : "N/A" %></div>
                        <div><strong>Status:</strong> <span class="badge bg-primary"><%= order != null ? order.getOrderStatus() : "PLACED" %></span></div>
                        <div><strong>Date:</strong> <%= order != null && order.getOrderDate() != null ? order.getOrderDate().toString() : "Today" %></div>
                        <div><strong>Payment:</strong> <%= order != null ? order.getPaymentMethod() : "COD" %></div>
                    </div>

                    <div class="border rounded-4 text-start p-4 mb-4">
                        <h5 class="fw-bold text-dark mb-3">Delivery Information</h5>
                        <p class="text-secondary mb-0">
                            <strong>Destination:</strong><br>
                            <%= order != null ? order.getDeliveryAddress() : "Standard Delivery" %>
                        </p>
                    </div>

                    <!-- Items Table -->
                    <div class="table-responsive text-start border rounded-4 mb-4">
                        <table class="table mb-0 align-middle">
                            <thead class="bg-light text-muted small">
                                <tr>
                                    <th>Item</th>
                                    <th>Size</th>
                                    <th class="text-center">Qty</th>
                                    <th class="text-end">Unit Price</th>
                                    <th class="text-end">Subtotal</th>
                                </tr>
                            </thead>
                            <tbody>
                                <% if (items != null) {
                                    for (OrderItem item : items) {
                                %>
                                    <tr>
                                        <td class="fw-bold text-dark"><%= item.getProductName() %></td>
                                        <td><span class="badge bg-light text-dark border"><%= item.getSizeLabel() %></span></td>
                                        <td class="text-center"><%= item.getQuantity() %></td>
                                        <td class="text-end">₹<%= item.getUnitPrice().stripTrailingZeros().toPlainString() %></td>
                                        <td class="text-end fw-bold">₹<%= item.getSubtotal().stripTrailingZeros().toPlainString() %></td>
                                    </tr>
                                <%  }
                                   } %>
                            </tbody>
                            <tfoot class="border-top">
                                <tr>
                                    <th colspan="4" class="text-end">Total Amount:</th>
                                    <th class="text-end fs-5 text-dark">
                                        ₹<%= order != null && order.getTotalAmount() != null ? order.getTotalAmount().stripTrailingZeros().toPlainString() : "0" %>
                                    </th>
                                </tr>
                            </tfoot>
                        </table>
                    </div>

                    <div class="d-flex justify-content-center gap-3">
                        <a href="<%= contextPath %>/orders" class="btn btn-dark px-4 py-2 fw-semibold">
                            View My Orders
                        </a>
                        <a href="<%= contextPath %>/home" class="btn btn-outline-secondary px-4 py-2 fw-semibold">
                            Continue Shopping
                        </a>
                    </div>
                </div>
            </div>
        </div>
    </main>

    <!-- Footer Included -->
    <jsp:include page="partials/footer.jsp" />

</body>
</html>
