<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Email Verification | FashionStore</title>

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
        .otp-input {
            letter-spacing: 12px;
            font-size: 2rem;
            font-weight: 700;
            text-align: center;
            font-family: 'Courier New', Courier, monospace;
            padding: 12px;
            border-radius: 12px;
            border: 2px solid #cbd5e1;
            transition: all 0.2s ease-in-out;
        }
        .otp-input:focus {
            border-color: #f59e0b;
            box-shadow: 0 0 0 4px rgba(245, 158, 11, 0.2);
            outline: none;
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
                        <div class="mb-2">
                            <span class="badge bg-warning text-dark px-3 py-1 rounded-pill fw-semibold text-uppercase small">
                                Step 2 of 2
                            </span>
                        </div>
                        <h3 class="fw-bold mb-1">Verify Your Email</h3>
                        <p class="text-light opacity-75 small mb-0">Enter the 6-digit code sent to your Gmail inbox</p>
                    </div>

                    <div class="card-body p-4 p-md-5">
                        <c:if test="${not empty error}">
                            <div class="alert alert-danger py-2 px-3 small rounded-3 mb-4" role="alert">
                                ${error}
                            </div>
                        </c:if>

                        <div class="text-center mb-4">
                            <p class="small text-muted mb-1">A verification code has been dispatched to:</p>
                            <p class="fw-bold text-dark fs-6 mb-2">${maskedEmail}</p>
                            <div class="d-flex justify-content-center align-items-center gap-2 small">
                                <span class="badge bg-light text-secondary border">Expires in 5 min</span>
                                <c:if test="${remainingAttempts < 5}">
                                    <span class="badge bg-warning-subtle text-warning-emphasis border border-warning-subtle">
                                        ${remainingAttempts} attempt<c:if test="${remainingAttempts != 1}">s</c:if> left
                                    </span>
                                </c:if>
                            </div>
                        </div>

                        <!-- OTP Submission Form -->
                        <form action="${pageContext.request.contextPath}/verify-otp" method="post" id="otpForm">
                            <input type="hidden" name="email" value="${pendingEmail}">

                            <div class="mb-4">
                                <label for="otpCode" class="form-label small fw-bold text-uppercase text-muted d-block text-center">
                                    6-Digit Verification Code
                                </label>
                                <input type="text"
                                       class="form-control otp-input"
                                       id="otpCode"
                                       name="otp"
                                       maxlength="6"
                                       pattern="[0-9]{6}"
                                       inputmode="numeric"
                                       autocomplete="one-time-code"
                                       placeholder="••••••"
                                       required
                                       autofocus
                                       oninput="this.value = this.value.replace(/[^0-9]/g, '')">
                            </div>

                            <div class="d-grid gap-2">
                                <button type="submit" class="btn btn-dark btn-lg fw-bold py-3 shadow-sm" id="verifyBtn">
                                    Verify & Create Account
                                </button>
                            </div>
                        </form>

                        <!-- Resend OTP Form -->
                        <div class="mt-4 pt-3 border-top text-center">
                            <p class="small text-muted mb-2">Didn't receive the email? Check spam or resend:</p>
                            <form action="${pageContext.request.contextPath}/resend-otp" method="post" id="resendForm">
                                <input type="hidden" name="email" value="${pendingEmail}">
                                <button type="submit"
                                        class="btn btn-outline-secondary btn-sm fw-semibold px-4 py-2"
                                        id="resendBtn"
                                        <c:if test="${cooldownSeconds > 0}">disabled</c:if>>
                                    <c:choose>
                                        <c:when test="${cooldownSeconds > 0}">
                                            Resend Code in (<span id="cooldownTimer">${cooldownSeconds}</span>s)
                                        </c:when>
                                        <c:otherwise>
                                            Resend Code
                                        </c:otherwise>
                                    </c:choose>
                                </button>
                            </form>
                        </div>

                        <div class="text-center mt-3 pt-2 small text-muted">
                            Entered the wrong email? 
                            <a href="${pageContext.request.contextPath}/register" class="fw-bold text-dark text-decoration-none">
                                Register again
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </main>

    <!-- Bootstrap JS Bundle -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>

    <!-- Resend Countdown Script -->
    <script>
        (function() {
            var cooldownSeconds = parseInt("${cooldownSeconds != null ? cooldownSeconds : 0}", 10);
            var resendBtn = document.getElementById("resendBtn");
            var timerSpan = document.getElementById("cooldownTimer");

            if (cooldownSeconds > 0 && resendBtn && timerSpan) {
                var interval = setInterval(function() {
                    cooldownSeconds--;
                    if (cooldownSeconds <= 0) {
                        clearInterval(interval);
                        resendBtn.disabled = false;
                        resendBtn.innerHTML = "Resend Code";
                    } else {
                        timerSpan.textContent = cooldownSeconds;
                    }
                }, 1000);
            }
        })();
    </script>
</body>
</html>
