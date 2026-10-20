<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<nav class="navbar navbar-expand-lg navbar-dark bg-dark sticky-top shadow-sm py-2">
    <div class="container">
        <!-- Logo -->
        <a class="navbar-brand fw-bold fs-3 text-uppercase tracking-wider d-flex align-items-center" href="${pageContext.request.contextPath}/">
            Fashion<span class="text-warning">Store</span>
        </a>

        <!-- Mobile Toggler -->
        <button class="navbar-toggler border-0" type="button" data-bs-toggle="collapse" data-bs-target="#mainNavbar" aria-controls="mainNavbar" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>

        <!-- Navbar Content -->
        <div class="collapse navbar-collapse" id="mainNavbar">
            <!-- Nav Links -->
            <ul class="navbar-nav me-auto mb-2 mb-lg-0 ms-lg-3">
                <li class="nav-item">
                    <a class="nav-link px-3" href="${pageContext.request.contextPath}/home">Home</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link px-3" href="${pageContext.request.contextPath}/products">Shop</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link px-3" href="${pageContext.request.contextPath}/products?category=Men">Men</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link px-3" href="${pageContext.request.contextPath}/products?category=Women">Women</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link px-3" href="${pageContext.request.contextPath}/products?category=Kids">Kids</a>
                </li>
            </ul>

            <!-- Search Form -->
            <form class="d-flex my-2 my-lg-0 me-lg-3" action="${pageContext.request.contextPath}/products" method="get" role="search">
                <div class="input-group input-group-sm">
                    <input class="form-control border-secondary bg-dark text-white" type="search" name="search" placeholder="Search fashion..." aria-label="Search">
                    <button class="btn btn-outline-warning" type="submit">
                        <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" fill="currentColor" viewBox="0 0 16 16">
                            <path d="M11.742 10.344a6.5 6.5 0 1 0-1.397 1.398h-.001c.03.04.062.078.098.115l3.85 3.85a1 1 0 0 0 1.415-1.414l-3.85-3.85a1.007 1.007 0 0 0-.115-.1zM12 6.5a5.5 5.5 0 1 1-11 0 5.5 5.5 0 0 1 11 0z"/>
                        </svg>
                    </button>
                </div>
            </form>

            <!-- Top-Right Actions (Cart & Auth) -->
            <div class="d-flex align-items-center gap-2">
                <!-- Cart Button with Count Badge -->
                <a href="${pageContext.request.contextPath}/cart" class="btn btn-outline-light btn-sm position-relative px-3 py-1 d-flex align-items-center gap-1">
                    <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" viewBox="0 0 16 16">
                        <path d="M0 1.5A.5.5 0 0 1 .5 1H2a.5.5 0 0 1 .485.379L2.89 3H14.5a.5.5 0 0 1 .491.592l-1.5 8A.5.5 0 0 1 13 12H4a.5.5 0 0 1-.491-.408L2.01 3.607 1.61 2H.5a.5.5 0 0 1-.5-.5zM3.102 4l1.313 7h8.17l1.313-7H3.102zM5 12a2 2 0 1 0 0 4 2 2 0 0 0 0-4zm7 0a2 2 0 1 0 0 4 2 2 0 0 0 0-4zm-7 1a1 1 0 1 1 0 2 1 1 0 0 1 0-2zm7 0a1 1 0 1 1 0 2 1 1 0 0 1 0-2z"/>
                    </svg>
                    <span>Cart</span>
                    <c:set var="cartCount" value="${sessionScope.cart != null ? sessionScope.cart.totalItems : 0}" />
                    <c:if test="${cartCount > 0}">
                        <span class="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-danger">
                            ${cartCount}
                        </span>
                    </c:if>
                </a>

                <!-- User Account Dropdown (when signed in) -->
                <c:choose>
                    <c:when test="${not empty sessionScope.user}">
                        <div class="dropdown">
                            <button class="btn btn-sm btn-warning dropdown-toggle fw-semibold d-flex align-items-center gap-2 shadow-sm" type="button" id="userMenuButton" data-bs-toggle="dropdown" aria-expanded="false">
                                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" viewBox="0 0 16 16">
                                    <path d="M8 8a3 3 0 1 0 0-6 3 3 0 0 0 0 6zm2-3a2 2 0 1 1-4 0 2 2 0 0 1 4 0zm4 8c0 1-1 1-1 1H3s-1 0-1-1 1-4 6-4 6 3 6 4zm-1-.004c-.001-.246-.154-.986-.832-1.664C11.516 10.68 10.289 10 8 10c-2.29 0-3.516.68-4.168 1.332-.678.678-.83 1.418-.832 1.664h10z"/>
                                </svg>
                                <span>${sessionScope.user.fullName}</span>
                            </button>
                            <ul class="dropdown-menu dropdown-menu-end shadow" aria-labelledby="userMenuButton">
                                <li class="dropdown-header text-muted small">${sessionScope.user.email}</li>
                                <li><a class="dropdown-item d-flex align-items-center gap-2" href="${pageContext.request.contextPath}/orders">
                                    <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" fill="currentColor" viewBox="0 0 16 16">
                                        <path d="M0 2.5A.5.5 0 0 1 .5 2H2a.5.5 0 0 1 .485.379L2.89 4H14.5a.5.5 0 0 1 .485.621l-1.5 6A.5.5 0 0 1 13 11H4a.5.5 0 0 1-.485-.379L1.61 3H.5a.5.5 0 0 1-.5-.5z"/>
                                    </svg>
                                    <span>My Orders</span>
                                </a></li>
                                <c:if test="${sessionScope.user.admin}">
                                    <li><hr class="dropdown-divider"></li>
                                    <li><a class="dropdown-item text-primary fw-bold d-flex align-items-center gap-2" href="${pageContext.request.contextPath}/admin">
                                        <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" fill="currentColor" viewBox="0 0 16 16">
                                            <path d="M1 0 0 1l2.2 3.081a1 1 0 0 0 .815.419h.07a1 1 0 0 1 .708.293l2.675 2.675-2.617 2.654A3.003 3.003 0 0 0 0 13a3 3 0 1 0 5.878-.851l2.654-2.617.968.968-.305.914a1 1 0 0 0 .242 1.023l3.27 3.27a.997.997 0 0 0 1.414 0l1.586-1.586a.997.997 0 0 0 0-1.414l-3.27-3.27a1 1 0 0 0-1.023-.242L10.5 9.5l-.96-.96 2.68-2.643A3.005 3.005 0 0 0 16 3c0-.269-.035-.53-.102-.777l-2.14 2.141L12 4l-.364-1.757L13.777.101A3.003 3.003 0 0 0 11 0a3.003 3.003 0 0 0-2.163.916L6.19 3.606 3.515.931A1 1 0 0 0 2.808.638H2.74a1 1 0 0 0-.816.419L1 0z"/>
                                        </svg>
                                        <span>Admin Dashboard</span>
                                    </a></li>
                                </c:if>
                                <li><hr class="dropdown-divider"></li>
                                <li><a class="dropdown-item text-danger d-flex align-items-center gap-2" href="${pageContext.request.contextPath}/logout">
                                    <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" fill="currentColor" viewBox="0 0 16 16">
                                        <path fill-rule="evenodd" d="M10 12.5a.5.5 0 0 1-.5.5h-8a.5.5 0 0 1-.5-.5v-9a.5.5 0 0 1 .5-.5h8a.5.5 0 0 1 .5.5v2a.5.5 0 0 0 1 0v-2A1.5 1.5 0 0 0 9.5 1h-8A1.5 1.5 0 0 0 0 2.5v9A1.5 1.5 0 0 0 1.5 13h8a1.5 1.5 0 0 0 1.5-1.5v-2a.5.5 0 0 0-1 0v2z"/>
                                        <path fill-rule="evenodd" d="M15.854 8.354a.5.5 0 0 0 0-.708l-3-3a.5.5 0 0 0-.708.708L14.293 7.5H5.5a.5.5 0 0 0 0 1h8.793l-2.147 2.146a.5.5 0 0 0 .708.708l3-3z"/>
                                    </svg>
                                    <span>Logout</span>
                                </a></li>
                            </ul>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <!-- Sign In Button with User Icon (when NOT logged in) -->
                        <a href="${pageContext.request.contextPath}/login" class="btn btn-outline-warning btn-sm d-flex align-items-center gap-2 px-3 py-1 fw-semibold text-decoration-none shadow-sm" id="navSignInBtn">
                            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" viewBox="0 0 16 16">
                                <path d="M8 8a3 3 0 1 0 0-6 3 3 0 0 0 0 6zm2-3a2 2 0 1 1-4 0 2 2 0 0 1 4 0zm4 8c0 1-1 1-1 1H3s-1 0-1-1 1-4 6-4 6 3 6 4zm-1-.004c-.001-.246-.154-.986-.832-1.664C11.516 10.68 10.289 10 8 10c-2.29 0-3.516.68-4.168 1.332-.678.678-.83 1.418-.832 1.664h10z"/>
                            </svg>
                            <span>Sign In</span>
                        </a>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</nav>

<!-- Flash Messages Alert Bar -->
<div class="container mt-2">
    <c:if test="${not empty sessionScope.flashSuccess}">
        <div class="alert alert-success alert-dismissible fade show shadow-sm py-2 px-3 small" role="alert">
            <strong>Success:</strong> ${sessionScope.flashSuccess}
            <button type="button" class="btn-close py-2" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
        <c:remove var="flashSuccess" scope="session" />
    </c:if>
    <c:if test="${not empty sessionScope.flashError}">
        <div class="alert alert-danger alert-dismissible fade show shadow-sm py-2 px-3 small" role="alert">
            <strong>Error:</strong> ${sessionScope.flashError}
            <button type="button" class="btn-close py-2" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
        <c:remove var="flashError" scope="session" />
    </c:if>
    <c:if test="${not empty sessionScope.flashInfo}">
        <div class="alert alert-info alert-dismissible fade show shadow-sm py-2 px-3 small" role="alert">
            <strong>Info:</strong> ${sessionScope.flashInfo}
            <button type="button" class="btn-close py-2" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
        <c:remove var="flashInfo" scope="session" />
    </c:if>
</div>