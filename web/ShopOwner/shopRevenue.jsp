<%-- 
    Document   : shopRevenue
    Created on : Jul 16, 2025, 1:25:04 AM
    Author     : Admin
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="vi" class="light-style layout-menu-fixed">
    <head>
        <title>Doanh thu theo Shop</title>
        <%-- Include CSS/JS giống các file khác dùng SNEAT --%>
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
                                <span class="text-muted fw-light">Doanh thu /</span> Theo từng Shop
                            </h4>

                            <!-- Bộ lọc ngày -->
                            <form method="get" class="mb-3 d-flex align-items-center justify-content-end gap-2">
                                <input type="hidden" name="shopOwnerId" value="${shopOwner.id}" />
                                <label class="form-label mb-0 fw-semibold">Tên Shop:</label>
                                <input type="text" name="searchName" class="form-control w-auto" value="${searchName}" placeholder="Nhập tên shop..." />


                                <label class="form-label mb-0 fw-semibold">Từ ngày:</label>
                                <input type="date" name="fromDate" class="form-control w-auto" value="${param.fromDate}"/>

                                <label class="form-label mb-0 fw-semibold">Đến ngày:</label>
                                <input type="date" name="toDate" class="form-control w-auto" value="${param.toDate}"/>

                                <button type="submit" class="btn btn-primary">Tra cứu</button>
                            </form>

                            <!-- Bảng doanh thu -->
                            <div class="card">
                                <div class="table-responsive text-nowrap">
                                    <table class="table">
                                        <thead class="table-light">
                                            <tr>
                                                <th>Shop ID</th>
                                                <th>Tên Shop</th>
                                                <th>Tổng doanh thu</th>
                                                <th>Hành động</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:choose>
                                                <c:when test="${empty revenueList}">
                                                    <tr>
                                                        <td colspan="3" class="text-center text-muted fw-semibold py-4">
                                                            ⚠️ Không có dữ liệu nào trong khoảng thời gian đã chọn.
                                                        </td>
                                                    </tr>
                                                </c:when>
                                                <c:otherwise>
                                                    <c:set var="grandTotal" value="0" />
                                                    <c:forEach var="shop" items="${revenueList}">
                                                        <tr>
                                                            <td>${shop.shopId}</td>
                                                            <td>${shop.shopName}</td>
                                                            <td>
                                                                <fmt:formatNumber value="${shop.totalRevenue}" type="currency" currencySymbol="₫" />
                                                            </td>
                                                            <td>
                                                                <div class="dropdown">
                                                                    <button type="button" class="btn p-0 dropdown-toggle hide-arrow" data-bs-toggle="dropdown">
                                                                        <i class="bx bx-dots-vertical-rounded"></i>
                                                                    </button>
                                                                    <div class="dropdown-menu">
                                                                        <a href="ShowShopDetail?shopId=${shop.shopId}&Id=${shopOwner.id}" class="dropdown-item btn btn-sm btn-outline-primary">Xem thông tin của shop</a><br>
                                                                    </div>
                                                                </div>
                                                            </td>
                                                            <c:set var="grandTotal" value="${grandTotal + shop.totalRevenue}" />
                                                        </tr>
                                                    </c:forEach>
                                                </c:otherwise>
                                            </c:choose>
                                        </tbody>
                                    </table>
                                </div>

                                <!-- Tổng doanh thu -->
                                <c:if test="${not empty revenueList}">
                                    <div class="d-flex justify-content-end align-items-center px-4 py-3">
                                        <span class="fw-semibold">Tổng doanh thu toàn bộ:</span>
                                        <span class="fw-bold text-primary ms-2">
                                            <fmt:formatNumber value="${grandTotal}" type="currency" currencySymbol="₫"/>
                                        </span>
                                    </div>
                                </c:if>
                            </div>

                            <!-- Phân trang -->
                            <c:if test="${totalPages > 1}">
                                <nav class="mt-4" aria-label="Page navigation">
                                    <ul class="pagination justify-content-center">
                                        <c:if test="${currentPage > 1}">
                                            <li class="page-item">
                                                <a class="page-link"
                                                   href="ShowRevenueShop?shopOwnerId=${shopOwner.id}&page=${currentPage - 1}&fromDate=${param.fromDate}&toDate=${param.toDate}&searchName=${searchName}">«</a>
                                            </li>
                                        </c:if>

                                        <c:forEach var="i" begin="1" end="${totalPages}">
                                            <li class="page-item ${i == currentPage ? 'active' : ''}">
                                                <a class="page-link"
                                                   href="ShowRevenueShop?shopOwnerId=${shopOwner.id}&page=${i}&fromDate=${param.fromDate}&toDate=${param.toDate}&searchName=${searchName}">${i}</a>
                                            </li>
                                        </c:forEach>

                                        <c:if test="${currentPage < totalPages}">
                                            <li class="page-item">
                                                <a class="page-link"
                                                   href="ShowRevenueShop?shopOwnerId=${shopOwner.id}&page=${currentPage + 1}&fromDate=${param.fromDate}&toDate=${param.toDate}&searchName=${searchName}">»</a>
                                            </li>
                                        </c:if>
                                    </ul>
                                </nav>
                            </c:if>
                            <jsp:include page="footer.jsp"/>

                        </div>
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
