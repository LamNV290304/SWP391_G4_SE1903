<%-- 
    Document   : resetPassword
    Created on : Jul 15, 2025, 1:37:11 AM
    Author     : Admin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en" class="light-style customizer-hide" dir="ltr"
      data-theme="theme-default"
      data-assets-path="./assets/"
      data-template="vertical-menu-template-free">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport"
              content="width=device-width, initial-scale=1.0, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0">
        <title>Đặt lại mật khẩu - Sneat Template</title>

        <!-- Favicon & Fonts -->
        <link rel="icon" type="image/x-icon" href="./assets/img/favicon/favicon.ico" />
        <link rel="preconnect" href="https://fonts.googleapis.com" />
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
        <link href="https://fonts.googleapis.com/css2?family=Public+Sans:wght@400;600&display=swap" rel="stylesheet" />

        <!-- Icons + CSS -->
        <link rel="stylesheet" href="./assets/vendor/fonts/boxicons.css" />
        <link rel="stylesheet" href="./assets/vendor/css/core.css" />
        <link rel="stylesheet" href="./assets/vendor/css/theme-default.css" />
        <link rel="stylesheet" href="./assets/css/demo.css" />
        <link rel="stylesheet" href="./assets/vendor/css/pages/page-auth.css" />

        <script src="./assets/vendor/js/helpers.js"></script>
        <script src="./assets/js/config.js"></script>
    </head>

    <body>
        <div class="container-xxl">
            <div class="authentication-wrapper authentication-basic container-p-y">
                <div class="authentication-inner py-4">
                    <div class="card">
                        <div class="card-body">
                            <h4 class="mb-2">Tạo mật khẩu mới 🔑</h4>
                            <p class="mb-4">Hãy đặt một mật khẩu mạnh và dễ nhớ với bạn</p>

                            <!-- Hiển thị thông báo lỗi nếu có -->
                            <c:if test="${not empty error}">
                                <div class="alert alert-danger" role="alert">
                                    <c:out value="${error}" />
                                </div>
                            </c:if>

                            <form action="ResetPassword" method="POST">
                                <!-- Hidden fields -->
                                <input type="hidden" name="email" value="${email}" />
                                <input type="hidden" name="databaseName" value="${databaseName}" />
                                <div class="mb-3">
                                    <label for="newPassword" class="form-label">Mật khẩu mới</label>
                                    <input type="password" class="form-control" id="newPassword" name="newPassword"
                                           placeholder="••••••••" required minlength="6"/>
                                </div>

                                <div class="mb-3">
                                    <label for="confirmPassword" class="form-label">Xác nhận mật khẩu</label>
                                    <input type="password" class="form-control" id="confirmPassword" name="confirmPassword"
                                           placeholder="••••••••" required minlength="6"/>
                                </div>

                                <button type="submit" class="btn btn-primary d-grid w-100">Cập nhật mật khẩu</button>
                            </form>

                            <div class="text-center mt-3">
                                <a href="login.jsp" class="d-flex align-items-center justify-content-center">
                                    <i class="bx bx-chevron-left scaleX-n1-rtl bx-sm"></i>
                                    Quay lại đăng nhập
                                </a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Core JS -->
        <script src="./assets/vendor/libs/jquery/jquery.js"></script>
        <script src="./assets/vendor/libs/popper/popper.js"></script>
        <script src="./assets/vendor/js/bootstrap.js"></script>
        <script src="./assets/vendor/libs/perfect-scrollbar/perfect-scrollbar.js"></script>
        <script src="./assets/vendor/js/menu.js"></script>
        <script src="./assets/js/main.js"></script>

    </body>
</html>
