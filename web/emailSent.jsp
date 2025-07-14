<%-- 
    Document   : emailSent
    Created on : Jul 15, 2025, 1:27:49 AM
    Author     : Admin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
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
    <title>Email Sent - Forgot Password | Sneat Template</title>

    <!-- Favicon -->
    <link rel="icon" type="image/x-icon" href="./assets/img/favicon/favicon.ico" />

    <!-- Fonts -->
    <link rel="preconnect" href="https://fonts.googleapis.com" />
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
    <link
        href="https://fonts.googleapis.com/css2?family=Public+Sans:wght@400;600&display=swap"
        rel="stylesheet"
    />

    <!-- Icons -->
    <link rel="stylesheet" href="./assets/vendor/fonts/boxicons.css" />

    <!-- Core CSS -->
    <link rel="stylesheet" href="./assets/vendor/css/core.css" class="template-customizer-core-css" />
    <link rel="stylesheet" href="./assets/vendor/css/theme-default.css" class="template-customizer-theme-css" />
    <link rel="stylesheet" href="./assets/css/demo.css" />

    <!-- Page CSS -->
    <link rel="stylesheet" href="./assets/vendor/css/pages/page-auth.css" />

    <!-- Helpers -->
    <script src="./assets/vendor/js/helpers.js"></script>
    <script src="./assets/js/config.js"></script>
</head>

<body>
    <div class="container-xxl">
        <div class="authentication-wrapper authentication-basic container-p-y">
            <div class="authentication-inner py-4">
                <!-- Card -->
                <div class="card">
                    <div class="card-body text-center">
                        <i class='bx bx-envelope-send bx-lg text-primary mb-3'></i>
                        <h4 class="mb-2">Đã gửi email xác thực 💌</h4>
                        <p class="mb-4">
                            Vui lòng kiểm tra hòm thư của bạn. Nếu không thấy email, hãy thử kiểm tra cả mục spam.
                        </p>

                        <a href="forgotPassword.jsp" class="btn btn-outline-primary w-100">
                            Gửi lại email khác
                        </a>
                    </div>
                </div>
                <!-- /Card -->
            </div>
        </div>
    </div>

    <!-- Core JS -->
    <script src="./assets/vendor/libs/jquery/jquery.js"></script>
    <script src="./assets/vendor/libs/popper/popper.js"></script>
    <script src="./assets/vendor/js/bootstrap.js"></script>
    <script src="./assets/vendor/libs/perfect-scrollbar/perfect-scrollbar.js"></script>
    <script src="./assets/vendor/js/menu.js"></script>

    <!-- Main JS -->
    <script src="./assets/js/main.js"></script>
</body>
</html>
