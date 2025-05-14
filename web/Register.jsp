<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Register</title>
        <!-- Bootstrap CSS -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@300;400;600;700&display=swap" rel="stylesheet">
        <link href="css/Header_Footer.css" rel="stylesheet">
        <link rel="stylesheet" href="css/register.css">
    </head>
    <body>
        <%@ include file="Header.jsp" %>

        <div class="container mb-5">
            <div class="row justify-content-center mt-5">
                <div class="col-md-6">
                    <div class="card shadow-sm">
                        <div class="card-body">
                            <h3 class="text-center mb-4">Create Your Account</h3>

                            <!-- Register Form -->
                            <form method="POST" action="register">
                                <c:if test="${requestScope.error != null && requestScope.error}">
                                    <div class="alert alert-danger">
                                        ${requestScope.error}
                                    </div>
                                </c:if>

                                <div class="mb-3">
                                    <label for="name" class="form-label">Full Name</label>
                                    <input type="text" class="form-control" id="name" name="name" placeholder="Enter your full name" required>
                                </div>

                                <div class="mb-3">
                                    <label for="email" class="form-label">Email address</label>
                                    <input type="email" class="form-control" id="email" name="email" placeholder="Enter your email" required>
                                </div>

                                <div class="mb-3">
                                    <label for="password" class="form-label">Password</label>
                                    <input type="password" class="form-control" id="password" name="password" placeholder="Create a password" required>
                                </div>

                                <div class="mb-3">
                                    <label for="confirm-password" class="form-label">Confirm Password</label>
                                    <input type="password" class="form-control" id="confirm-password" name="confirmPassword" placeholder="Confirm your password" required>
                                </div>

                                <div class="text-center">
                                    <button type="submit" class="btn btn-primary w-100">Register</button>
                                </div>
                            </form>

                            <div class="text-center mt-3">
                                <p>Already have an account? <a href="Login.jsp">Login here</a></p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <%@ include file="Footer.jsp" %>
    </body>
</html>
