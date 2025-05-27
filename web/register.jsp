<%-- 
    Document   : register
    Created on : May 24, 2025, 1:44:49 AM
    Author     : Admin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html
    lang="en"
    class="light-style customizer-hide"
    dir="ltr"
    data-theme="theme-default"
    data-assets-path="./assets/"
    data-template="vertical-menu-template-free"
    >
    <head>
        <meta charset="utf-8" />
        <meta
            name="viewport"
            content="width=device-width, initial-scale=1.0, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0"
            />

        <title>Register Basic - Pages | Sneat - Bootstrap 5 HTML Admin Template - Pro</title>

        <meta name="description" content="" />

        <!-- Favicon -->
        <link rel="icon" type="image/x-icon" href="./assets/img/favicon/favicon.ico" />

        <!-- Fonts -->
        <link rel="preconnect" href="https://fonts.googleapis.com" />
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
        <link
            href="https://fonts.googleapis.com/css2?family=Public+Sans:ital,wght@0,300;0,400;0,500;0,600;0,700;1,300;1,400;1,500;1,600;1,700&display=swap"
            rel="stylesheet"
            />

        <!-- Icons. Uncomment required icon fonts -->
        <link rel="stylesheet" href="./assets/vendor/fonts/boxicons.css" />

        <!-- Core CSS -->
        <link rel="stylesheet" href="./assets/vendor/css/core.css" class="template-customizer-core-css" />
        <link rel="stylesheet" href="./assets/vendor/css/theme-default.css" class="template-customizer-theme-css" />
        <link rel="stylesheet" href="./assets/css/demo.css" />

        <!-- Vendors CSS -->
        <link rel="stylesheet" href="./assets/vendor/libs/perfect-scrollbar/perfect-scrollbar.css" />

        <!-- Page CSS -->
        <!-- Page -->
        <link rel="stylesheet" href="./assets/vendor/css/pages/page-auth.css" />
        <!-- Helpers -->
        <script src="./assets/vendor/js/helpers.js"></script>

        <!--! Template customizer & Theme config files MUST be included after core stylesheets and helpers.js in the <head> section -->
        <!--? Config:  Mandatory theme config file contain global vars & default theme options, Set your preferred theme option in this file.  -->
        <script src="./assets/js/config.js"></script>
        <style>
            body.custom-bg {
                background-color: #3399FF;
            }
        </style>
    </head>

    <body class="custom-bg">
        <!-- Content -->

        <div class="container-xxl">
            <div class="authentication-wrapper authentication-basic container-p-y">
                <div class="authentication-inner">
                    <!-- Register Card -->
                    <div class="card">
                        <div class="card-body">
                            <h4 class="mb-2">Make your business easy 🚀</h4>

                            <div class="">
                                <div class="card mb-4">
                                    <div class="card-header d-flex align-items-center justify-content-between">
                                        <h5 class="mb-0">Register</h5>
                                    </div>
                                    <div class="card-body">
                                        <c:if test="${not empty error}">
                                            <div class="alert alert-danger" role="alert">
                                                ${error}
                                            </div>
                                        </c:if>
                                        <form method="post" action="Register">
                                            <div class="row mb-3">
                                                <label class="col-sm-2 col-form-label" for="username">Username</label>
                                                <div class="col-sm-10">
                                                    <input type="text" class="form-control" id="username" name="username" placeholder="Your username" required />
                                                </div>
                                            </div>

                                            <div class="row mb-3">
                                                <label class="col-sm-2 col-form-label" for="password">Password</label>
                                                <div class="col-sm-10">
                                                    <input type="password" class="form-control" id="password" name="password" placeholder="Your password" required minlength="8"/>
                                                </div>
                                            </div>

                                            <div class="row mb-3">
                                                <label class="col-sm-2 col-form-label" for="password">Confirm Password</label>
                                                <div class="col-sm-10">
                                                    <input type="password" class="form-control" id="password" name="re-password" placeholder="Confirm your password" required minlength="8"/>
                                                </div>
                                            </div>

                                            <div class="row mb-3">
                                                <label class="col-sm-2 col-form-label" for="fullname">Fullname</label>
                                                <div class="col-sm-10">
                                                    <input type="text" class="form-control" id="fullname" name="fullname" placeholder="John Doe" required />
                                                </div>
                                            </div>

                                            <div class="row mb-3">
                                                <label class="col-sm-2 col-form-label" for="email">Email</label>
                                                <div class="col-sm-10">
                                                    <input type="email" class="form-control" id="email" name="email" placeholder="john@example.com" required />
                                                </div>
                                            </div>

                                            <div class="row mb-3">
                                                <label class="col-sm-2 col-form-label" for="phone">Phone</label>
                                                <div class="col-sm-10">
                                                    <input type="text" class="form-control" id="phone" name="phone" placeholder="0658 799 894" required />
                                                </div>
                                            </div>

                                            <div class="row mb-3">
                                                <label class="col-sm-2 col-form-label" for="shopName">Shop Name</label>
                                                <div class="col-sm-10">
                                                    <input type="text" class="form-control" id="shopName" name="shopName" placeholder="Your Shop Name" required />
                                                </div>
                                            </div>

                                            <div class="row justify-content-end">
                                                <div class="col-sm-10">
                                                    <button type="submit" class="btn btn-primary d-grid w-100">Sign up</button>
                                                </div>
                                            </div>
                                        </form>
                                    </div>
                                </div>
                            </div>



                            <p class="text-center">
                                <span>Already have an account?</span>
                                <a href="login.jsp">
                                    <span>Sign in instead</span>
                                </a>
                            </p>
                        </div>
                    </div>
                    <!-- Register Card -->
                </div>
            </div>
        </div>

        <!-- Core JS -->
        <!-- build:js assets/vendor/js/core.js -->
        <script src="./assets/vendor/libs/jquery/jquery.js"></script>
        <script src="./assets/vendor/libs/popper/popper.js"></script>
        <script src="./assets/vendor/js/bootstrap.js"></script>
        <script src="./assets/vendor/libs/perfect-scrollbar/perfect-scrollbar.js"></script>

        <script src="./assets/vendor/js/menu.js"></script>
        <!-- endbuild -->

        <!-- Vendors JS -->

        <!-- Main JS -->
        <script src="./assets/js/main.js"></script>

        <!-- Page JS -->

        <!-- Place this tag in your head or just before your close body tag. -->
        <script async defer src="https://buttons.github.io/buttons.js"></script>
    </body>
</html>
