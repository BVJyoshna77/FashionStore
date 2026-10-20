<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Security Setup | FashionStore</title>

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
            background-color: #0f172a;
            color: #f8fafc;
        }
        h1, h2, h3, h4 { font-family: 'Outfit', sans-serif; }
    </style>
</head>
<body>

    <!-- Header Navigation -->
    <jsp:include page="navbar.jsp" />

    <main class="container my-5">
        <div class="row justify-content-center">
            <div class="col-md-8 col-lg-6">
                <div class="card border-0 shadow-lg rounded-4 overflow-hidden bg-dark text-white border border-secondary">
                    <div class="p-4 text-center border-bottom border-secondary bg-black bg-opacity-40">
                        <h3 class="fw-bold mb-1">Administrator Authorization</h3>
                        <p class="text-light opacity-75 small mb-0">Securely assign or create an ADMIN account</p>
                    </div>

                    <div class="card-body p-4 p-md-5">
                        <c:if test="${not empty error}">
                            <div class="alert alert-danger py-2 px-3 small rounded-3 mb-4" role="alert">
                                ${error}
                            </div>
                        </c:if>

                        <form action="${pageContext.request.contextPath}/admin-setup" method="post">
                            <div class="mb-3">
                                <label class="form-label small fw-bold text-uppercase text-light">Target User Email *</label>
                                <input type="email" class="form-control bg-dark text-white border-secondary" name="email" value="${email != null ? email : (sessionScope.user != null ? sessionScope.user.email : '')}" placeholder="user@example.com" required>
                                <div class="form-text text-light opacity-60 small">
                                    If this user already exists, their role will be upgraded to <strong>ADMIN</strong> immediately.
                                </div>
                            </div>

                            <div class="mb-3">
                                <label class="form-label small fw-bold text-uppercase text-light">Full Name (for new accounts)</label>
                                <input type="text" class="form-control bg-dark text-white border-secondary" name="fullName" value="${fullName}" placeholder="Administrator Name">
                            </div>

                            <div class="mb-3">
                                <label class="form-label small fw-bold text-uppercase text-light">Password (new or reset)</label>
                                <input type="password" class="form-control bg-dark text-white border-secondary" name="password" placeholder="Min. 6 characters">
                            </div>

                            <div class="mb-4">
                                <label class="form-label small fw-bold text-uppercase text-warning">
                                    Master Admin Secret Key *
                                </label>
                                <input type="password" class="form-control bg-dark text-warning border-warning" name="secretKey" placeholder="Enter Master Admin Secret Key" required>
                            </div>

                            <div class="d-grid mt-4">
                                <button type="submit" class="btn btn-warning btn-lg fw-bold text-dark py-3 shadow">
                                    Authorize & Assign Admin Access
                                </button>
                            </div>
                        </form>

                        <div class="text-center mt-4 pt-3 border-top border-secondary small text-light opacity-75">
                            <a href="${pageContext.request.contextPath}/home" class="text-light text-decoration-none">
                                Return to Store Home
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </main>

    <!-- Footer Included -->
    <jsp:include page="partials/footer.jsp" />

</body>
</html>
