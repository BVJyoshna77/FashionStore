<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Customer Login | FashionStore</title>

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
            background: linear-gradient(rgba(15, 23, 42, 0.65), rgba(15, 23, 42, 0.78)),
                        url('${pageContext.request.contextPath}/images/hero-banner.jpg') center/cover no-repeat fixed;
            min-height: 100vh;
            display: flex;
            flex-direction: column;
            color: #1e293b;
        }
        h1, h2, h3, h4 { font-family: 'Outfit', sans-serif; }
        .auth-card {
            background-color: #ffffff;
            border-radius: 16px;
            box-shadow: 0 20px 45px rgba(0, 0, 0, 0.35);
            overflow: hidden;
        }
    </style>
</head>
<body>

    <!-- Header Navigation -->
    <jsp:include page="navbar.jsp" />

    <main class="container my-auto py-5">
        <div class="row justify-content-center">
            <div class="col-md-7 col-lg-5 col-xl-4">
                <div class="card auth-card border-0">
                    <div class="bg-dark text-white p-4 text-center">
                        <h3 class="fw-bold mb-1">Welcome Back</h3>
                        <p class="text-light opacity-75 small mb-0">Sign in to your FashionStore account</p>
                    </div>

                    <div class="card-body p-4 p-md-5">
                        <c:if test="${not empty sessionScope.user}">
                            <div class="alert alert-info py-2 px-3 small rounded-3 mb-4" role="alert">
                                Currently signed in as <strong>${sessionScope.user.fullName}</strong> (${sessionScope.user.email}). You can sign in to a different account below, or <a href="${pageContext.request.contextPath}/home" class="alert-link text-decoration-underline">go to Home</a>.
                            </div>
                        </c:if>

                        <c:if test="${not empty sessionScope.flashSuccess}">
                            <div class="alert alert-success py-2 px-3 small rounded-3 mb-4" role="alert">
                                <strong>Success:</strong> ${sessionScope.flashSuccess}
                            </div>
                            <c:remove var="flashSuccess" scope="session" />
                        </c:if>

                        <c:if test="${not empty error}">
                            <div class="alert alert-danger py-2 px-3 small rounded-3 mb-4" role="alert">
                                ${error}
                            </div>
                        </c:if>

                        <form action="${pageContext.request.contextPath}/login" method="post">
                            <input type="hidden" name="redirect" value="${redirect}">

                            <div class="mb-3">
                                <label for="emailInput" class="form-label small fw-bold text-uppercase text-muted">Email Address</label>
                                <input type="email" class="form-control form-control-lg fs-6" id="emailInput" name="email" value="${email}" placeholder="name@example.com" required autofocus>
                            </div>

                            <div class="mb-3">
                                <div class="d-flex justify-content-between align-items-center">
                                    <label for="passwordInput" class="form-label small fw-bold text-uppercase text-muted">Password</label>
                                    <a href="#" class="small text-decoration-none text-muted">Forgot?</a>
                                </div>
                                <input type="password" class="form-control form-control-lg fs-6" id="passwordInput" name="password" placeholder="Password" required>
                            </div>

                            <div class="d-grid mt-4">
                                <button type="submit" class="btn btn-dark btn-lg fw-bold py-3 shadow-sm">
                                    Sign In
                                </button>
                            </div>
                        </form>

                        <div class="text-center mt-4 pt-3 border-top small text-muted">
                            Don't have an account? 
                            <a href="${pageContext.request.contextPath}/register" class="fw-bold text-dark text-decoration-none">
                                Register now
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </main>

    <!-- Bootstrap JS Bundle -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>

</body>
</html>
