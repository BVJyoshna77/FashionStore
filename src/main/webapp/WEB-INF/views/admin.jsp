<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.fashionstore.model.Product" %>
<%@ page import="com.fashionstore.model.Category" %>
<%@ page import="com.fashionstore.model.Order" %>
<%@ page import="com.fashionstore.model.OrderItem" %>
<%@ page import="com.fashionstore.model.User" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<%
    String currentTab = (String) request.getAttribute("currentTab");
    if (currentTab == null) currentTab = "products";

    List<Product> products = (List<Product>) request.getAttribute("products");
    List<Category> categories = (List<Category>) request.getAttribute("categories");
    List<Order> orders = (List<Order>) request.getAttribute("orders");
    Map<Integer, List<OrderItem>> orderItemsMap = (Map<Integer, List<OrderItem>>) request.getAttribute("orderItemsMap");
    List<User> users = (List<User>) request.getAttribute("users");

    String contextPath = request.getContextPath();
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Dashboard | FashionStore</title>

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
            background-color: #f1f5f9;
            color: #1e293b;
        }
        h1, h2, h3, h4, .admin-title { font-family: 'Outfit', sans-serif; }
        .admin-nav .nav-link {
            font-weight: 600;
            color: #64748b;
            padding: 12px 20px;
            border-radius: 10px;
        }
        .admin-nav .nav-link.active {
            background-color: #0f172a;
            color: #ffffff;
        }
        .table-thumb {
            width: 48px;
            height: 52px;
            object-fit: cover;
            border-radius: 6px;
        }
    </style>
</head>
<body>

    <!-- Header Navigation -->
    <jsp:include page="navbar.jsp" />

    <main class="container my-5">
        <!-- Admin Title Bar -->
        <div class="d-flex flex-wrap justify-content-between align-items-center mb-4 pb-3 border-bottom gap-3">
            <div>
                <span class="badge bg-warning text-dark px-3 py-1 fw-bold text-uppercase">Admin Portal</span>
                <h2 class="fw-bolder text-dark mb-0 mt-1 admin-title">Store Management Dashboard</h2>
            </div>
            <div class="d-flex gap-2">
                <a href="<%= contextPath %>/admin-setup" class="btn btn-outline-warning text-dark fw-semibold">
                    Security / Admin Setup
                </a>
                <a href="<%= contextPath %>/home" class="btn btn-outline-dark fw-semibold" target="_blank">
                    View Live Store
                </a>
            </div>
        </div>

        <!-- Navigation Tabs -->
        <ul class="nav nav-pills admin-nav mb-4 bg-white p-2 rounded-4 border shadow-sm gap-2">
            <li class="nav-item">
                <a class="nav-link <%= "products".equals(currentTab) ? "active" : "" %>" href="<%= contextPath %>/admin?tab=products">
                    Products (<%= products != null ? products.size() : 0 %>)
                </a>
            </li>
            <li class="nav-item">
                <a class="nav-link <%= "categories".equals(currentTab) ? "active" : "" %>" href="<%= contextPath %>/admin?tab=categories">
                    Categories (<%= categories != null ? categories.size() : 0 %>)
                </a>
            </li>
            <li class="nav-item">
                <a class="nav-link <%= "orders".equals(currentTab) ? "active" : "" %>" href="<%= contextPath %>/admin?tab=orders">
                    Customer Orders (<%= orders != null ? orders.size() : 0 %>)
                </a>
            </li>
            <li class="nav-item">
                <a class="nav-link <%= "users".equals(currentTab) ? "active" : "" %>" href="<%= contextPath %>/admin?tab=users">
                    Users & Roles (<%= users != null ? users.size() : 0 %>)
                </a>
            </li>
        </ul>

        <!-- TAB 1: PRODUCTS -->
        <% if ("products".equals(currentTab)) { %>
            <div class="bg-white rounded-4 border shadow-sm p-4">
                <div class="d-flex justify-content-between align-items-center mb-4">
                    <h4 class="fw-bold text-dark mb-0">Catalog Inventory</h4>
                    <button class="btn btn-dark fw-semibold" data-bs-toggle="modal" data-bs-target="#addProductModal">
                        Add New Product
                    </button>
                </div>

                <div class="table-responsive">
                    <table class="table table-hover align-middle mb-0">
                        <thead class="bg-light text-muted small text-uppercase">
                            <tr>
                                <th>ID</th>
                                <th>Image</th>
                                <th>Product Details</th>
                                <th>Category</th>
                                <th>Price</th>
                                <th>Discount</th>
                                <th>Status</th>
                                <th class="text-end">Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <% if (products != null) {
                                for (Product p : products) {
                            %>
                                <tr>
                                    <td class="text-muted fw-bold">#<%= p.getProductId() %></td>
                                    <td>
                                        <% if (p.getImageUrl() != null && !p.getImageUrl().trim().isEmpty()) { %>
                                            <img src="<%= p.getImageUrl() %>" alt="<%= p.getProductName() %>" class="table-thumb border">
                                        <% } else { %>
                                            <div class="table-thumb border bg-light d-flex align-items-center justify-content-center text-muted small">No Img</div>
                                        <% } %>
                                    </td>
                                    <td>
                                        <div class="fw-bold text-dark"><%= p.getProductName() %></div>
                                        <div class="small text-muted text-truncate" style="max-width: 280px;">
                                            <%= p.getDescription() != null ? p.getDescription() : "" %>
                                        </div>
                                    </td>
                                    <td>
                                        <span class="badge bg-light text-dark border">
                                            Cat #<%= p.getCategoryId() %>
                                        </span>
                                    </td>
                                    <td class="fw-bold">₹<%= p.getPrice() != null ? p.getPrice().stripTrailingZeros().toPlainString() : "0" %></td>
                                    <td>
                                        <% if (p.getDiscountPercent() != null && p.getDiscountPercent().compareTo(java.math.BigDecimal.ZERO) > 0) { %>
                                            <span class="badge bg-danger"><%= p.getDiscountPercent().stripTrailingZeros().toPlainString() %>% OFF</span>
                                        <% } else { %>
                                            <span class="text-muted small">None</span>
                                        <% } %>
                                    </td>
                                    <td>
                                        <% if (p.isActive()) { %>
                                            <span class="badge bg-success">Active</span>
                                        <% } else { %>
                                            <span class="badge bg-secondary">Inactive</span>
                                        <% } %>
                                    </td>
                                    <td class="text-end">
                                        <div class="btn-group btn-group-sm">
                                            <!-- Edit Button -->
                                            <button type="button" class="btn btn-outline-primary"
                                                    onclick="openEditModal(<%= p.getProductId() %>, '<%= p.getProductName().replace("'", "\\'") %>', <%= p.getCategoryId() %>, '<%= p.getPrice() %>', '<%= p.getDiscountPercent() %>', '<%= p.getImageUrl() != null ? p.getImageUrl().replace("'", "\\'") : "" %>', '<%= p.getDescription() != null ? p.getDescription().replace("'", "\\'").replace("\n", " ") : "" %>', <%= p.isActive() %>)">
                                                Edit
                                            </button>

                                            <!-- Toggle Status -->
                                            <form action="<%= contextPath %>/admin" method="post" class="d-inline">
                                                <input type="hidden" name="action" value="toggleProduct">
                                                <input type="hidden" name="productId" value="<%= p.getProductId() %>">
                                                <input type="hidden" name="currentStatus" value="<%= p.isActive() %>">
                                                <button type="submit" class="btn btn-outline-secondary" title="Toggle active status">
                                                    <%= p.isActive() ? "Deactivate" : "Activate" %>
                                                </button>
                                            </form>

                                            <!-- Delete -->
                                            <form action="<%= contextPath %>/admin" method="post" class="d-inline" onsubmit="return confirm('Permanently delete this product?');">
                                                <input type="hidden" name="action" value="deleteProduct">
                                                <input type="hidden" name="productId" value="<%= p.getProductId() %>">
                                                <button type="submit" class="btn btn-outline-danger">
                                                    Delete
                                                </button>
                                            </form>
                                        </div>
                                    </td>
                                </tr>
                            <%  }
                               } %>
                        </tbody>
                    </table>
                </div>
            </div>

            <!-- Add Product Modal -->
            <div class="modal fade" id="addProductModal" tabindex="-1" aria-labelledby="addProductModalLabel" aria-hidden="true">
                <div class="modal-dialog modal-lg">
                    <div class="modal-content border-0 shadow rounded-4">
                        <div class="modal-header bg-dark text-white">
                            <h5 class="modal-title fw-bold" id="addProductModalLabel">Add New Product</h5>
                            <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
                        </div>
                        <form action="<%= contextPath %>/admin" method="post">
                            <input type="hidden" name="action" value="addProduct">
                            <div class="modal-body p-4">
                                <div class="row g-3">
                                    <div class="col-md-8">
                                        <label class="form-label fw-bold small">Product Name *</label>
                                        <input type="text" class="form-control" name="productName" required placeholder="e.g. Classic Denim Jacket">
                                    </div>
                                    <div class="col-md-4">
                                        <label class="form-label fw-bold small">Category *</label>
                                        <select class="form-select" name="categoryId" required>
                                            <% if (categories != null) {
                                                for (Category c : categories) {
                                            %>
                                                <option value="<%= c.getCategoryId() %>"><%= c.getCategoryName() %></option>
                                            <%  }
                                               } %>
                                        </select>
                                    </div>
                                    <div class="col-md-6">
                                        <label class="form-label fw-bold small">Price (₹) *</label>
                                        <input type="number" step="0.01" class="form-control" name="price" required placeholder="1299.00">
                                    </div>
                                    <div class="col-md-6">
                                        <label class="form-label fw-bold small">Discount (%)</label>
                                        <input type="number" step="0.01" class="form-control" name="discountPercent" value="0.00" placeholder="10.00">
                                    </div>
                                    <div class="col-12">
                                        <label class="form-label fw-bold small">Image URL / Path</label>
                                        <input type="text" class="form-control" name="imageUrl" placeholder="images/women-top.jpg or https://..." value="images/women-top.jpg">
                                    </div>
                                    <div class="col-12">
                                        <label class="form-label fw-bold small">Description</label>
                                        <textarea class="form-control" name="description" rows="3" placeholder="Crafted with pure cotton..."></textarea>
                                    </div>
                                    <div class="col-12">
                                        <div class="form-check form-switch">
                                            <input class="form-check-input" type="checkbox" name="isActive" id="activeSwitch" checked>
                                            <label class="form-check-label fw-bold" for="activeSwitch">Active on Storefront</label>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="modal-footer bg-light">
                                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                                <button type="submit" class="btn btn-dark fw-bold">Save Product</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>

            <!-- Edit Product Modal -->
            <div class="modal fade" id="editProductModal" tabindex="-1" aria-hidden="true">
                <div class="modal-dialog modal-lg">
                    <div class="modal-content border-0 shadow rounded-4">
                        <div class="modal-header bg-primary text-white">
                            <h5 class="modal-title fw-bold">Edit Product</h5>
                            <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
                        </div>
                        <form action="<%= contextPath %>/admin" method="post">
                            <input type="hidden" name="action" value="editProduct">
                            <input type="hidden" name="productId" id="editProductId">
                            <div class="modal-body p-4">
                                <div class="row g-3">
                                    <div class="col-md-8">
                                        <label class="form-label fw-bold small">Product Name *</label>
                                        <input type="text" class="form-control" name="productName" id="editProductName" required>
                                    </div>
                                    <div class="col-md-4">
                                        <label class="form-label fw-bold small">Category *</label>
                                        <select class="form-select" name="categoryId" id="editCategoryId" required>
                                            <% if (categories != null) {
                                                for (Category c : categories) {
                                            %>
                                                <option value="<%= c.getCategoryId() %>"><%= c.getCategoryName() %></option>
                                            <%  }
                                               } %>
                                        </select>
                                    </div>
                                    <div class="col-md-6">
                                        <label class="form-label fw-bold small">Price (₹) *</label>
                                        <input type="number" step="0.01" class="form-control" name="price" id="editPrice" required>
                                    </div>
                                    <div class="col-md-6">
                                        <label class="form-label fw-bold small">Discount (%)</label>
                                        <input type="number" step="0.01" class="form-control" name="discountPercent" id="editDiscount">
                                    </div>
                                    <div class="col-12">
                                        <label class="form-label fw-bold small">Image URL / Path</label>
                                        <input type="text" class="form-control" name="imageUrl" id="editImageUrl">
                                    </div>
                                    <div class="col-12">
                                        <label class="form-label fw-bold small">Description</label>
                                        <textarea class="form-control" name="description" id="editDescription" rows="3"></textarea>
                                    </div>
                                    <div class="col-12">
                                        <div class="form-check form-switch">
                                            <input class="form-check-input" type="checkbox" name="isActive" id="editIsActive">
                                            <label class="form-check-label fw-bold" for="editIsActive">Active on Storefront</label>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="modal-footer bg-light">
                                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                                <button type="submit" class="btn btn-primary fw-bold">Update Product</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        <% } %>

        <!-- TAB 2: CATEGORIES -->
        <% if ("categories".equals(currentTab)) { %>
            <div class="row g-4">
                <div class="col-md-5">
                    <div class="bg-white rounded-4 border shadow-sm p-4">
                        <h5 class="fw-bold text-dark mb-3">Create New Category</h5>
                        <form action="<%= contextPath %>/admin" method="post">
                            <input type="hidden" name="action" value="addCategory">
                            <div class="mb-3">
                                <label class="form-label small fw-bold text-muted">Category Name *</label>
                                <input type="text" class="form-control" name="categoryName" placeholder="e.g. Winter Wear" required>
                            </div>
                            <div class="mb-3">
                                <label class="form-label small fw-bold text-muted">Description</label>
                                <textarea class="form-control" name="description" rows="3" placeholder="Coats, sweaters, and hoodies..."></textarea>
                            </div>
                            <button type="submit" class="btn btn-dark fw-bold w-100">
                                Save Category
                            </button>
                        </form>
                    </div>
                </div>

                <div class="col-md-7">
                    <div class="bg-white rounded-4 border shadow-sm p-4">
                        <h5 class="fw-bold text-dark mb-3">Existing Categories</h5>
                        <div class="table-responsive">
                            <table class="table table-hover align-middle mb-0">
                                <thead class="bg-light text-muted small text-uppercase">
                                    <tr>
                                        <th>ID</th>
                                        <th>Name</th>
                                        <th>Description</th>
                                        <th>Status</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <% if (categories != null) {
                                        for (Category c : categories) {
                                    %>
                                        <tr>
                                            <td class="fw-bold text-muted">#<%= c.getCategoryId() %></td>
                                            <td class="fw-bold text-dark"><%= c.getCategoryName() %></td>
                                            <td class="small text-muted"><%= c.getDescription() != null ? c.getDescription() : "" %></td>
                                            <td>
                                                <% if (c.isActive()) { %>
                                                    <span class="badge bg-success">Active</span>
                                                <% } else { %>
                                                    <span class="badge bg-secondary">Inactive</span>
                                                <% } %>
                                            </td>
                                        </tr>
                                    <%  }
                                       } %>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        <% } %>

        <!-- TAB 3: ORDERS -->
        <% if ("orders".equals(currentTab)) { %>
            <div class="bg-white rounded-4 border shadow-sm p-4">
                <h4 class="fw-bold text-dark mb-4">Customer Orders & Fulfillment</h4>

                <div class="table-responsive">
                    <table class="table table-hover align-middle mb-0">
                        <thead class="bg-light text-muted small text-uppercase">
                            <tr>
                                <th>Order ID</th>
                                <th>Date</th>
                                <th>User ID</th>
                                <th>Total</th>
                                <th>Payment</th>
                                <th>Status</th>
                                <th>Change Status</th>
                            </tr>
                        </thead>
                        <tbody>
                            <% if (orders != null) {
                                for (Order o : orders) {
                                    String badgeClass = "bg-primary";
                                    if ("SHIPPED".equalsIgnoreCase(o.getOrderStatus())) badgeClass = "bg-info text-dark";
                                    if ("DELIVERED".equalsIgnoreCase(o.getOrderStatus())) badgeClass = "bg-success";
                                    if ("CANCELLED".equalsIgnoreCase(o.getOrderStatus())) badgeClass = "bg-danger";
                            %>
                                <tr>
                                    <td class="fw-bold text-dark">#FS-<%= o.getOrderId() %></td>
                                    <td class="small text-muted"><%= o.getOrderDate() != null ? o.getOrderDate().toString() : "" %></td>
                                    <td><span class="badge bg-light text-dark border">User #<%= o.getUserId() %></span></td>
                                    <td class="fw-bold">₹<%= o.getTotalAmount() != null ? o.getTotalAmount().stripTrailingZeros().toPlainString() : "0" %></td>
                                    <td class="small"><%= o.getPaymentMethod() %></td>
                                    <td>
                                        <span class="badge <%= badgeClass %>"><%= o.getOrderStatus() %></span>
                                    </td>
                                    <td>
                                        <form action="<%= contextPath %>/admin" method="post" class="d-flex gap-2 m-0">
                                            <input type="hidden" name="action" value="updateOrderStatus">
                                            <input type="hidden" name="orderId" value="<%= o.getOrderId() %>">
                                            <select name="orderStatus" class="form-select form-select-sm" style="width: 140px;">
                                                <option value="PLACED" <%= "PLACED".equalsIgnoreCase(o.getOrderStatus()) ? "selected" : "" %>>PLACED</option>
                                                <option value="PROCESSING" <%= "PROCESSING".equalsIgnoreCase(o.getOrderStatus()) ? "selected" : "" %>>PROCESSING</option>
                                                <option value="SHIPPED" <%= "SHIPPED".equalsIgnoreCase(o.getOrderStatus()) ? "selected" : "" %>>SHIPPED</option>
                                                <option value="DELIVERED" <%= "DELIVERED".equalsIgnoreCase(o.getOrderStatus()) ? "selected" : "" %>>DELIVERED</option>
                                                <option value="CANCELLED" <%= "CANCELLED".equalsIgnoreCase(o.getOrderStatus()) ? "selected" : "" %>>CANCELLED</option>
                                            </select>
                                            <button type="submit" class="btn btn-sm btn-outline-dark">Update</button>
                                        </form>
                                    </td>
                                </tr>
                            <%  }
                               } %>
                        </tbody>
                    </table>
                </div>
            </div>
        <% } %>

        <!-- TAB 4: USERS & ROLES -->
        <% if ("users".equals(currentTab)) { %>
            <div class="row g-4">
                <div class="col-md-5">
                    <div class="bg-white rounded-4 border shadow-sm p-4">
                        <h5 class="fw-bold text-dark mb-3">Create New Admin User</h5>
                        <form action="<%= contextPath %>/admin" method="post">
                            <input type="hidden" name="action" value="createAdminUser">
                            <div class="mb-3">
                                <label class="form-label small fw-bold text-muted">Full Name *</label>
                                <input type="text" class="form-control" name="fullName" placeholder="e.g. Priya Manager" required>
                            </div>
                            <div class="mb-3">
                                <label class="form-label small fw-bold text-muted">Email Address *</label>
                                <input type="email" class="form-control" name="email" placeholder="manager@fashionstore.com" required>
                            </div>
                            <div class="mb-3">
                                <label class="form-label small fw-bold text-muted">Phone Number</label>
                                <input type="tel" class="form-control" name="phone" placeholder="9876543210">
                            </div>
                            <div class="mb-3">
                                <label class="form-label small fw-bold text-muted">Password (min 6 chars) *</label>
                                <input type="password" class="form-control" name="password" placeholder="••••••••" required>
                            </div>
                            <button type="submit" class="btn btn-dark fw-bold w-100">
                                Create Admin Account
                            </button>
                        </form>
                    </div>
                </div>

                <div class="col-md-7">
                    <div class="bg-white rounded-4 border shadow-sm p-4">
                        <h5 class="fw-bold text-dark mb-3">User Accounts & Role Permissions</h5>
                        <div class="table-responsive">
                            <table class="table table-hover align-middle mb-0">
                                <thead class="bg-light text-muted small text-uppercase">
                                    <tr>
                                        <th>ID</th>
                                        <th>Name & Email</th>
                                        <th>Role</th>
                                        <th class="text-end">Change Role</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <% if (users != null) {
                                        for (User u : users) {
                                            boolean isAdmin = u.isAdmin();
                                    %>
                                        <tr>
                                            <td class="fw-bold text-muted">#<%= u.getUserId() %></td>
                                            <td>
                                                <div class="fw-bold text-dark"><%= u.getFullName() %></div>
                                                <div class="small text-muted"><%= u.getEmail() %></div>
                                            </td>
                                            <td>
                                                <% if (isAdmin) { %>
                                                    <span class="badge bg-warning text-dark fw-bold">ADMIN</span>
                                                <% } else { %>
                                                    <span class="badge bg-secondary">CUSTOMER</span>
                                                <% } %>
                                            </td>
                                            <td class="text-end">
                                                <form action="<%= contextPath %>/admin" method="post" class="d-inline m-0">
                                                    <input type="hidden" name="action" value="updateUserRole">
                                                    <input type="hidden" name="userId" value="<%= u.getUserId() %>">
                                                    <% if (isAdmin) { %>
                                                        <input type="hidden" name="role" value="CUSTOMER">
                                                        <button type="submit" class="btn btn-sm btn-outline-danger" title="Demote to Customer" onclick="return confirm('Demote this admin to customer?');">
                                                            Demote to Customer
                                                        </button>
                                                    <% } else { %>
                                                        <input type="hidden" name="role" value="ADMIN">
                                                        <button type="submit" class="btn btn-sm btn-outline-success" title="Promote to Admin" onclick="return confirm('Promote this customer to Administrator?');">
                                                            Promote to ADMIN
                                                        </button>
                                                    <% } %>
                                                </form>
                                            </td>
                                        </tr>
                                    <%  }
                                       } %>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        <% } %>

    </main>

    <!-- Footer Included -->
    <jsp:include page="partials/footer.jsp" />

    <script>
        function openEditModal(id, name, catId, price, discount, img, desc, active) {
            document.getElementById('editProductId').value = id;
            document.getElementById('editProductName').value = name;
            document.getElementById('editCategoryId').value = catId;
            document.getElementById('editPrice').value = price;
            document.getElementById('editDiscount').value = discount;
            document.getElementById('editImageUrl').value = img;
            document.getElementById('editDescription').value = desc;
            document.getElementById('editIsActive').checked = active;

            var modal = new bootstrap.Modal(document.getElementById('editProductModal'));
            modal.show();
        }
    </script>
</body>
</html>
