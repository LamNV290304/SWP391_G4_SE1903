<%-- 
    Document   : createEmployee
    Created on : Jun 12, 2025, 2:51:50 PM
    Author     : Admin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en" class="light-style layout-menu-fixed" dir="ltr"
      data-theme="theme-default" data-assets-path="./assets/"
      data-template="vertical-menu-template-free">

    <head>
        <meta charset="UTF-8" />
        <meta name="viewport"
              content="width=device-width, initial-scale=1.0, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0" />
        <title>Thêm Nhân Viên | Sneat</title>

        <!-- Favicon & Fonts -->
        <link rel="icon" type="image/x-icon" href="./assets/img/favicon/favicon.ico" />
        <link rel="stylesheet" href="./assets/vendor/fonts/boxicons.css" />
        <link rel="stylesheet" href="./assets/vendor/css/core.css" />
        <link rel="stylesheet" href="./assets/vendor/css/theme-default.css" />
        <link rel="stylesheet" href="./assets/css/demo.css" />
        <link rel="stylesheet" href="./assets/vendor/libs/perfect-scrollbar/perfect-scrollbar.css" />
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
                            <h4 class="fw-bold py-3 mb-4"><span class="text-muted fw-light">Employee /</span> Thêm mới</h4>

                            <!-- Form -->
                            <div class="card mb-4">
                                <div class="card-body">
                                    <form action="createEmployee" method="post">
                                        <div class="row mb-3">
                                            <div class="col-md-6">
                                                <label for="fullname" class="form-label">Họ và tên</label>
                                                <input type="text" id="fullname" name="fullname" class="form-control" required />
                                            </div>
                                            <div class="col-md-6">
                                                <label for="username" class="form-label">Tên đăng nhập</label>
                                                <input type="text" id="username" name="username" class="form-control" required />
                                            </div>
                                        </div>

                                        <div class="row mb-3">
                                            <div class="col-md-6">
                                                <label for="password" class="form-label">Mật khẩu</label>
                                                <input type="password" id="password" name="password" class="form-control" required />
                                            </div>
                                            <div class="col-md-6">
                                                <label for="email" class="form-label">Email</label>
                                                <input type="email" id="email" name="email" class="form-control" required />
                                            </div>
                                        </div>

                                        <div class="row mb-3">
                                            <div class="col-md-6">
                                                <label for="phone" class="form-label">Số điện thoại</label>
                                                <input type="text" id="phone" name="phone" class="form-control" />
                                            </div>
                                            <div class="col-md-6">
                                                <label for="roleId" class="form-label">Vai trò</label>
                                                <select id="roleId" name="roleId" class="form-select" required>
                                                    <option value="1">Quản trị viên</option>
                                                    <option value="2">Nhân viên</option>
                                                </select>
                                            </div>
                                        </div>

                                        <div class="mb-3">
                                            <label for="shopId" class="form-label">Mã cửa hàng</label>
                                            <input type="number" id="shopId" name="shopId" class="form-control" required />
                                        </div>

                                        <button type="submit" class="btn btn-primary">Tạo mới</button>
                                    </form>
                                </div>
                            </div>
                        </div>

                        <jsp:include page="footer.jsp" />
                    </div>
                </div>
            </div>
        </div>

        <!-- Scripts -->
        <script src="./assets/vendor/libs/jquery/jquery.js"></script>
        <script src="./assets/vendor/libs/popper/popper.js"></script>
        <script src="./assets/vendor/js/bootstrap.js"></script>
        <script src="./assets/vendor/libs/perfect-scrollbar/perfect-scrollbar.js"></script>
        <script src="./assets/vendor/js/menu.js"></script>
        <script src="./assets/js/main.js"></script>
    </body>
</html>

