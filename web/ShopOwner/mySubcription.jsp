<%-- 
    Document   : mySubcription
    Created on : Jun 28, 2025, 1:27:35 PM
    Author     : Admin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="vi" class="light-style layout-menu-fixed"
      dir="ltr" data-theme="theme-default"
      data-assets-path="./assets/" data-template="vertical-menu-template-free">
    <head>
        <title>Gói Của Tôi</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">

        <!-- Fonts & Style -->
        <link href="https://fonts.googleapis.com/css2?family=Roboto&display=swap&subset=vietnamese" rel="stylesheet" />
        <link rel="icon" type="image/x-icon" href="img/logoSale.png" />
        <link rel="stylesheet" href="./assets/vendor/fonts/boxicons.css" />
        <link rel="stylesheet" href="./assets/vendor/css/core.css" />
        <link rel="stylesheet" href="./assets/vendor/css/theme-default.css" />
        <link rel="stylesheet" href="./assets/css/demo.css" />
        <link rel="stylesheet" href="./assets/css/custom.css" />
        <link rel="stylesheet" href="./assets/vendor/libs/perfect-scrollbar/perfect-scrollbar.css" />
        <link rel="stylesheet" href="./assets/vendor/libs/apex-charts/apex-charts.css" />

        <!-- Scripts -->
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
                            <h4 class="fw-bold py-3 mb-4">
                                <span class="text-muted fw-light">Thông tin /</span> Gói dịch vụ của tôi
                            </h4>

                            <c:choose>
                                <c:when test="${not empty subscription}">
                                    <div class="card">
                                        <div class="card-body text-start">
                                            <h5 class="card-title mb-3 text-primary">${subscription.packageName}</h5>
                                            <p><strong>Tên cửa hàng:</strong> ${subscription.shopName}</p>
                                            <p><strong>Thời gian bắt đầu:</strong> <fmt:formatDate value="${subscription.startDate}" pattern="dd/MM/yyyy" /></p>
                                            <p><strong>Thời gian kết thúc:</strong> <fmt:formatDate value="${subscription.endDate}" pattern="dd/MM/yyyy" /></p>
                                            <p><strong>Giá:</strong> <fmt:formatNumber value="${subscription.packagePrice}" type="currency" currencySymbol="₫"/></p>
                                            <p><strong>Mô tả:</strong> ${subscription.packageDescription}</p>
                                        </div>
                                        <div class="card-footer d-flex justify-content-end gap-2">
                                            <form method="post" action="ExtendSubscription">
                                                <input type="hidden" name="subscriptionId" value="${subscription.id}" />
                                                <input type="hidden" name="packageId" value="${subscription.packageId}" />
                                                <button type="submit" class="btn btn-outline-danger">Gia hạn</button>
                                            </form>
                                            <form method="get" action="ShowServicePackage" onsubmit="return confirmChangePackage();">
                                                <button type="submit" class="btn btn-outline-primary">Thay đổi gói</button>
                                            </form>
                                        </div>
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <div class="alert alert-warning text-center" role="alert">
                                        Bạn hiện chưa đăng ký gói dịch vụ nào. <a href="ShowServicePackage" class="alert-link">Chọn gói ngay</a> để bắt đầu trải nghiệm!
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </div>
                        <jsp:include page="footer.jsp" />
                    </div>
                </div>
            </div>
        </div>

        <!-- JS -->
        <script src="./assets/vendor/libs/jquery/jquery.js"></script>
        <script src="./assets/vendor/libs/popper/popper.js"></script>
        <script src="./assets/vendor/js/bootstrap.js"></script>
        <script src="./assets/vendor/libs/perfect-scrollbar/perfect-scrollbar.js"></script>
        <script src="./assets/vendor/js/menu.js"></script>
        <script src="./assets/js/main.js"></script>
        <script>
            function confirmChangePackage() {
                return confirm("Thao tác này sẽ huỷ gói dịch vụ hiện tại của bạn. Bạn có chắc chắn muốn tiếp tục?");
            }
        </script>
    </body>
</html>

