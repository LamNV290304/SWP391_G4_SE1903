<%-- 
    Document   : showShopDetails
    Created on : Jul 16, 2025, 4:03:47 AM
    Author     : Admin
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi" class="light-style layout-menu-fixed">
    <head>
        <title>Chi tiết Shop</title>
        <link href="https://fonts.googleapis.com/css2?family=Roboto&display=swap&subset=vietnamese" rel="stylesheet" />
        <link rel="icon" type="image/x-icon" href="img/logoSale.png" />
        <link rel="stylesheet" href="./assets/vendor/fonts/boxicons.css" />
        <link rel="stylesheet" href="./assets/vendor/css/core.css" />
        <link rel="stylesheet" href="./assets/vendor/css/theme-default.css" />
        <link rel="stylesheet" href="./assets/css/demo.css" />
        <link rel="stylesheet" href="./assets/css/custom.css" />
        <link rel="stylesheet" href="./assets/vendor/libs/perfect-scrollbar/perfect-scrollbar.css" />
        <link rel="stylesheet" href="./assets/vendor/libs/apex-charts/apex-charts.css" />
        <script src="./assets/vendor/js/helpers.js"></script>
        <script src="./assets/js/config.js"></script>
    </head>
    <body>
        <div class="layout-wrapper layout-content-navbar">
            <div class="layout-container">
                <jsp:include page="sidebar.jsp" />
                <div class="layout-page">
                    <jsp:include page="navbar.jsp" />
                    <div class="content-wrapper">
                        <div class="container-xxl flex-grow-1 container-p-y">
                            <h4 class="fw-bold mb-4">
                                <span class="text-muted fw-light">Shop /</span> Chi tiết
                            </h4>

                            <div class="card">
                                <div class="card-body">
                                    <h5 class="card-title">Thông tin Shop</h5>
                                    <div class="row mb-3">
                                        <label class="col-sm-2 col-form-label">Shop ID:</label>
                                        <div class="col-sm-10">
                                            <p class="form-control-plaintext fw-semibold">${shop.shopID}</p>
                                        </div>
                                    </div>
                                    <div class="row mb-3">
                                        <label class="col-sm-2 col-form-label">Tên Shop:</label>
                                        <div class="col-sm-10">
                                            <p class="form-control-plaintext fw-semibold">${shop.shopName}</p>
                                        </div>
                                    </div>
                                    <div class="row mb-3">
                                        <label class="col-sm-2 col-form-label">Địa chỉ:</label>
                                        <div class="col-sm-10">
                                            <p class="form-control-plaintext">${shop.address}</p>
                                        </div>
                                    </div>
                                    <div class="row mb-3">
                                        <label class="col-sm-2 col-form-label">Số điện thoại:</label>
                                        <div class="col-sm-10">
                                            <p class="form-control-plaintext">${shop.phone}</p>
                                        </div>
                                    </div>
                                    <div class="row mb-3">
                                        <label class="col-sm-2 col-form-label">Email:</label>
                                        <div class="col-sm-10">
                                            <p class="form-control-plaintext">${shop.email}</p>
                                        </div>
                                    </div>
                                    <div class="row mb-3">
                                        <label class="col-sm-2 col-form-label">Trạng thái:</label>
                                        <div class="col-sm-10">
                                            <span class="badge bg-label-${shop.status == true ? 'success' : 'danger'}">
                                                ${shop.status == true ? 'Hoạt động' : 'Ngừng hoạt động'}
                                            </span>
                                        </div>
                                    </div>

                                    <a href="ShowRevenueShop?shopOwnerId=${id}" class="btn btn-secondary">← Quay lại</a>
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

