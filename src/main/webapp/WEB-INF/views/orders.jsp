<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.fashionstore.model.Order" %>
<%@ page import="com.fashionstore.model.OrderItem" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<%
    List<Order> orders = (List<Order>) request.getAttribute("orders");
    Map<Integer, List<OrderItem>> orderItemsMap = (Map<Integer, List<OrderItem>>) request.getAttribute("orderItemsMap");
    String contextPath = request.getContextPath();
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Orders | FashionStore</title>

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
        <h2 class="fw-bolder text-dark mb-4 pb-2 border-bottom">My Orders</h2>

        <% if (orders != null && !orders.isEmpty()) { %>
            <div class="row g-4">
                <% for (Order o : orders) {
                    List<OrderItem> items = orderItemsMap != null ? orderItemsMap.get(o.getOrderId()) : null;
                    String statusClass = "bg-primary";
                    if ("SHIPPED".equalsIgnoreCase(o.getOrderStatus())) statusClass = "bg-info text-dark";
                    if ("DELIVERED".equalsIgnoreCase(o.getOrderStatus())) statusClass = "bg-success";
                    if ("CANCELLED".equalsIgnoreCase(o.getOrderStatus())) statusClass = "bg-danger";
                %>
                    <div class="col-12">
                        <div class="bg-white rounded-4 border shadow-sm p-4">
                            <div class="d-flex flex-wrap justify-content-between align-items-center pb-3 border-bottom mb-3 gap-2">
                                <div>
                                    <span class="fw-bold fs-5 text-dark">Order #FS-<%= o.getOrderId() %></span>
                                    <span class="text-muted small ms-2"><%= o.getOrderDate() != null ? o.getOrderDate().toString() : "" %></span>
                                </div>
                                <div class="d-flex align-items-center gap-3">
                                    <span class="badge <%= statusClass %> px-3 py-2 text-uppercase fs-6">
                                        <%= o.getOrderStatus() %>
                                    </span>
                                    <span class="fs-5 fw-bolder text-dark">
                                        ₹<%= o.getTotalAmount() != null ? o.getTotalAmount().stripTrailingZeros().toPlainString() : "0" %>
                                    </span>
                                </div>
                            </div>

                            <div class="row g-3">
                                <div class="col-md-8">
                                    <h6 class="text-muted small text-uppercase fw-bold">Items in this order:</h6>
                                    <ul class="list-unstyled mb-0 small">
                                        <% if (items != null) {
                                            for (OrderItem item : items) {
                                        %>
                                            <li class="d-flex justify-content-between py-1 border-bottom border-light">
                                                <span>
                                                    <strong><%= item.getProductName() %></strong> 
                                                    <span class="badge bg-light text-dark border">Size: <%= item.getSizeLabel() %></span> 
                                                    × <%= item.getQuantity() %>
                                                </span>
                                                <span class="fw-bold">₹<%= item.getSubtotal().stripTrailingZeros().toPlainString() %></span>
                                            </li>
                                        <%  }
                                           } %>
                                    </ul>
                                </div>
                                <div class="col-md-4 border-start-md ps-md-4">
                                    <h6 class="text-muted small text-uppercase fw-bold">Shipping Destination:</h6>
                                    <p class="small text-secondary mb-2"><%= o.getDeliveryAddress() %></p>
                                    <div class="small text-muted"><strong>Payment:</strong> <%= o.getPaymentMethod() %></div>
                                </div>
                            </div>
                        </div>
                    </div>
                <% } %>
            </div>
        <% } else { %>
            <div class="text-center py-5">
                <div class="bg-white p-5 rounded-4 border shadow-sm mx-auto" style="max-width: 500px;">
                    <h4 class="fw-bold text-dark mb-2">No Orders Placed Yet</h4>
                    <p class="text-muted mb-4">When you place orders, they will appear here with real-time status updates.</p>
                    <a href="<%= contextPath %>/home" class="btn btn-warning px-4 py-2 fw-bold text-dark rounded-pill">
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
