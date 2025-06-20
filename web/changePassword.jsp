<%-- 
    Document   : changePassword
    Created on : Jun 20, 2025, 4:50:08 PM
    Author     : Admin
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en" class="light-style layout-menu-fixed" dir="ltr"
      data-theme="theme-default" data-assets-path="./assets/" data-template="vertical-menu-template-free">

<head>
    <meta charset="UTF-8">
    <title>Đổi mật khẩu</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <link href="https://fonts.googleapis.com/css2?family=Roboto&display=swap&subset=vietnamese" rel="stylesheet" />
    <link rel="icon" type="image/x-icon" href="img/logoSale.png" />
    <link rel="stylesheet" href="./assets/vendor/fonts/boxicons.css" />
    <link rel="stylesheet" href="./assets/vendor/css/core.css" class="template-customizer-core-css" />
    <link rel="stylesheet" href="./assets/vendor/css/theme-default.css" class="template-customizer-theme-css" />
    <link rel="stylesheet" href="./assets/css/demo.css" />
    <link rel="stylesheet" href="./assets/vendor/libs/perfect-scrollbar/perfect-scrollbar.css" />
    <link rel="stylesheet" href="./assets/css/custom.css" />
    <script src="./assets/vendor/js/helpers.js"></script>
    <script src="./assets/js/config.js"></script>
</head>

<body>
    <div class="layout-wrapper layout-content-navbar">
        <div class="layout-container">

            <!-- Sidebar -->
            <jsp:include page="sidebar.jsp" />

            <!-- Layout page -->
            <div class="layout-page">

                <!-- Navbar -->
                <jsp:include page="navBar.jsp" />

                <!-- Content wrapper -->
                <div class="content-wrapper">
                    <div class="container-xxl flex-grow-1 container-p-y">
                        <h4 class="fw-bold py-3 mb-4"><span class="text-muted fw-light">Tài khoản /</span> Đổi mật khẩu</h4>

                        <c:if test="${not empty sessionScope.successMessage}">
                            <div class="alert alert-success">${sessionScope.successMessage}</div>
                            <c:remove var="successMessage" scope="session"/>
                        </c:if>
                        <c:if test="${not empty sessionScope.errorMessage}">
                            <div class="alert alert-danger">${sessionScope.errorMessage}</div>
                            <c:remove var="errorMessage" scope="session"/>
                        </c:if>

                        <div class="card">
                            <h5 class="card-header">Đổi mật khẩu</h5>
                            <div class="card-body">
                                <form action="changePassword" method="post">
                                    <div class="mb-3">
                                        <label for="currentPassword" class="form-label">Mật khẩu hiện tại</label>
                                        <input type="password" class="form-control" id="currentPassword" name="currentPassword" required />
                                    </div>
                                    <div class="mb-3">
                                        <label for="newPassword" class="form-label">Mật khẩu mới</label>
                                        <input type="password" class="form-control" id="newPassword" name="newPassword" required minlength="8" />
                                    </div>
                                    <div class="mb-3">
                                        <label for="confirmPassword" class="form-label">Nhập lại mật khẩu mới</label>
                                        <input type="password" class="form-control" id="confirmPassword" name="confirmPassword" required minlength="8" />
                                    </div>
                                    <button type="submit" class="btn btn-primary">Đổi mật khẩu</button>
                                </form>
                            </div>
                        </div>
                    </div>

                    <!-- Footer -->
                    <jsp:include page="footer.jsp" />
                </div>
            </div>
        </div>
    </div>

    <script src="./assets/vendor/libs/jquery/jquery.js"></script>
    <script src="./assets/vendor/libs/popper/popper.js"></script>
    <script src="./assets/vendor/js/bootstrap.js"></script>
    <script src="./assets/vendor/libs/perfect-scrollbar/perfect-scrollbar.js"></script>
    <script src="./assets/vendor/js/menu.js"></script>
    <script src="./assets/js/main.js"></script>
</body>
</html>
