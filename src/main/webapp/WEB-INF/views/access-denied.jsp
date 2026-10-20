<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Access Denied | FashionStore</title>

    <!-- Google Fonts -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Outfit:wght@500;600;700;800&family=Plus+Jakarta+Sans:wght@400;500;600;700&display=swap" rel="stylesheet">

    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">

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

    <main class="container my-5 py-5 text-center">
        <div class="row justify-content-center">
            <div class="col-md-7 col-lg-6 col-xl-5">
                <div class="card border-0 shadow-lg rounded-4 p-4 p-md-5">
                    <div class="mb-3">
                        <span class="badge bg-danger-subtle text-danger border border-danger-subtle px-3 py-2 fs-6 rounded-pill">
                            HTTP 403 — Restricted Area
                        </span>
                    </div>

                    <h2 class="fw-bolder text-dark mb-2">Administrator Access Required</h2>
                    <p class="text-muted mb-4">
                        You do not have permission to access the FashionStore Admin Management Dashboard.
                    </p>

                    <c:if test="${not empty sessionScope.user}">
                        <div class="bg-light border rounded-3 p-3 text-start small mb-4">
                            <div><strong>Signed in as:</strong> ${sessionScope.user.fullName} (${sessionScope.user.email})</div>
                            <div><strong>Current Role:</strong> <span class="badge bg-secondary">${sessionScope.user.role}</span></div>
                            <div class="text-danger mt-1">Only accounts with <code>ADMIN</code> privileges are permitted in this section.</div>
                        </div>
                    </c:if>

                    <div class="d-grid gap-2">
                        <a href="${pageContext.request.contextPath}/login?redirect=/admin" class="btn btn-dark fw-bold py-3 shadow-sm">
                            Sign In with an Admin Account
                        </a>

                        <a href="${pageContext.request.contextPath}/admin-setup" class="btn btn-outline-warning text-dark fw-bold py-2">
                            Authorize Account with Security Key
                        </a>

                        <a href="${pageContext.request.contextPath}/home" class="btn btn-outline-secondary py-2 mt-2">
                            Return to Store Home
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
