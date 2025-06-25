<%-- 
    Document   : showProfile
    Created on : Jun 22, 2025, 2:33:39 PM
    Author     : Admin
--%>

<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="en" class="light-style layout-menu-fixed" data-theme="theme-default"
      data-assets-path="./assets/" data-template="vertical-menu-template-free">

    <head>
        <meta charset="UTF-8" />
        <meta name="viewport"
              content="width=device-width, initial-scale=1.0, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0" />
        <title>Hồ sơ cửa hàng | Sneat</title>

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

                <!-- Content -->
                <div class="layout-page">

                    <!-- Navbar -->
                    <jsp:include page="navbar.jsp" />

                    <div class="content-wrapper">
                        <div class="container-xxl flex-grow-1 container-p-y">

                            <div class="card mb-4">
                                <div class="card-body d-flex align-items-center">
                                    <div>
                                        <h5 class="mb-1">${shopOwner.fullname}</h5>
                                        <span class="badge bg-label-success">Chủ cửa hàng</span>
                                    </div>
                                    <div class="ms-auto">
                                        <button type="button" class="btn btn-outline-primary"
                                                data-bs-toggle="modal" data-bs-target="#editShopProfileModal">
                                            Chỉnh sửa thông tin
                                        </button>
                                    </div>
                                </div>
                            </div>

                            <div class="card">
                                <h5 class="card-header">Thông tin cửa hàng</h5>
                                <div class="card-body">
                                    <ul class="list-unstyled mb-0">
                                        <li class="mb-3"><strong>Email:</strong> ${shopOwner.email}</li>
                                        <li class="mb-3"><strong>Số điện thoại:</strong> ${shopOwner.phone}</li>
                                        <li class="mb-3"><strong>Tên đăng nhập:</strong> ${shopOwner.username}</li>
                                        <li class="mb-3"><strong>Tên cửa hàng:</strong> ${shopOwner.shopName}</li>
                                        <li class="mb-3"><strong>Trạng thái:</strong>
                                            <c:choose>
                                                <c:when test="${shopOwner.status}">
                                                    <span class="badge bg-success">Đang hoạt động</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="badge bg-secondary">Đã khóa</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </li>
                                    </ul>
                                </div>
                            </div>
                        </div>

                        <jsp:include page="footer.jsp" />
                    </div>
                </div>
            </div>
        </div>

        <!-- Modal chỉnh sửa -->
        <div class="modal fade" id="editShopProfileModal" tabindex="-1" aria-labelledby="editShopProfileLabel" aria-hidden="true">
            <div class="modal-dialog">
                <form action="UpdateShopProfile" method="post" class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Cập nhật thông tin cửa hàng</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Đóng"></button>
                    </div>
                    <div class="modal-body">
                        <div class="mb-3">
                            <label class="form-label">Tên cửa hàng</label>
                            <input type="text" name="shopName" class="form-control" value="${shopOwner.shopName}" required>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Email</label>
                            <input type="email" name="email" class="form-control" value="${shopOwner.email}" required>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Số điện thoại</label>
                            <input type="text" name="phone" class="form-control" value="${shopOwner.phone}" required>
                        </div>
                        <input type="hidden" name="username" value="${shopOwner.username}">
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-outline-secondary" data-bs-dismiss="modal">Hủy</button>
                        <button type="submit" class="btn btn-primary">Lưu thay đổi</button>
                    </div>
                </form>
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

