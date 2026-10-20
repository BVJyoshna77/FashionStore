
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<header class="navbar">
    <div class="container navbar-container">

        <!-- Fashion Store Logo -->
        <div class="logo">
            <a href="<c:url value='/home' />">Fashion Store</a>
        </div>

        <!-- Search Bar -->
        <form class="search-bar"
              action="<c:url value='/products' />"
              method="get">

            <input
                type="text"
                name="keyword"
                placeholder="Search for fashion products..."
                aria-label="Search for fashion products">

            <button type="submit">Search</button>
        </form>

        <!-- Navigation Links -->
        <nav class="nav-links">
            <a href="<c:url value='/home' />">Home</a>
            <a href="<c:url value='/products' />">Products</a>
            <a href="<c:url value='/cart' />">Cart</a>
            <a href="<c:url value='/login' />">Login</a>
        </nav>

    </div>
</header>
