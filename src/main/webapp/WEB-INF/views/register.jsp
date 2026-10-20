<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Customer Registration | FashionStore</title>

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
            <div class="col-md-8 col-lg-6">
                <div class="card auth-card border-0">
                    <div class="bg-dark text-white p-4 text-center">
                        <h3 class="fw-bold mb-1">Create an Account</h3>
                        <p class="text-light opacity-75 small mb-0">Join FashionStore for personalized styles and exclusive deals</p>
                    </div>

                    <div class="card-body p-4 p-md-5">
                        <c:if test="${not empty error}">
                            <div class="alert alert-danger py-2 px-3 small rounded-3 mb-4" role="alert">
                                ${error}
                            </div>
                        </c:if>

                        <form action="${pageContext.request.contextPath}/register" method="post">
                            <div class="row g-3">
                                <div class="col-12">
                                    <label class="form-label small fw-bold text-uppercase text-muted">Full Name *</label>
                                    <input type="text" class="form-control" name="fullName" value="${fullName}" placeholder="Full Name" required>
                                </div>

                                <div class="col-md-6">
                                    <label class="form-label small fw-bold text-uppercase text-muted">Email Address *</label>
                                    <input type="email" class="form-control" name="email" value="${email}" placeholder="name@example.com" required>
                                </div>

                                <div class="col-md-6">
                                    <label class="form-label small fw-bold text-uppercase text-muted">Phone Number</label>
                                    <input type="tel" class="form-control" name="phone" value="${phone}" placeholder="10-digit mobile">
                                </div>

                                <div class="col-12">
                                    <label class="form-label small fw-bold text-uppercase text-muted">Gender</label>
                                    <select class="form-select" name="gender">
                                        <option value="Female" ${gender == 'Female' ? 'selected' : ''}>Female</option>
                                        <option value="Male" ${gender == 'Male' ? 'selected' : ''}>Male</option>
                                        <option value="Other" ${gender == 'Other' ? 'selected' : ''}>Other / Prefer not to say</option>
                                    </select>
                                </div>

                                <div class="col-12">
                                    <label class="form-label small fw-bold text-uppercase text-muted">Default Shipping Address</label>
                                    <textarea class="form-control" name="address" rows="2" placeholder="Street, Apartment, City, State, PIN">${address}</textarea>
                                </div>

                                <div class="col-md-6">
                                    <label class="form-label small fw-bold text-uppercase text-muted">Password *</label>
                                    <input type="password" class="form-control" name="password" placeholder="Min. 6 characters" required>
                                </div>

                                <div class="col-md-6">
                                    <label class="form-label small fw-bold text-uppercase text-muted">Confirm Password *</label>
                                    <input type="password" class="form-control" name="confirmPassword" placeholder="Repeat password" required>
                                </div>
                            </div>

                            <div class="d-grid mt-4">
                                <button type="submit" class="btn btn-dark btn-lg fw-bold py-3 shadow-sm">
                                    Register Account
                                </button>
                            </div>
                        </form>

                        <div class="text-center mt-4 pt-3 border-top small text-muted">
                            Already have an account? 
                            <a href="${pageContext.request.contextPath}/login" class="fw-bold text-dark text-decoration-none">
                                Sign in here
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
