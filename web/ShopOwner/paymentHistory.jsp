<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="vi" class="light-style layout-menu-fixed">
    <head>
        <title>Lịch sử thanh toán</title>
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
                                <span class="text-muted fw-light">Tài khoản /</span> Lịch sử thanh toán
                            </h4>

                            <!-- Bộ lọc -->
                            <form method="get" class="mb-3 d-flex align-items-center justify-content-end gap-2">
                                <input type="hidden" name="shopOwnerId" value="${shopOwner.id}" />
                                <input type="hidden" name="page" value="1" />

                                <label class="form-label mb-0 fw-semibold">Từ ngày:</label>
                                <input type="date" name="fromDate" class="form-control w-auto" value="${fromDate}"/>

                                <label class="form-label mb-0 fw-semibold">Đến ngày:</label>
                                <input type="date" name="toDate" class="form-control w-auto" value="${toDate}"/>

                                <label class="form-label mb-0 fw-semibold">Gói:</label>
                                <select name="packageId" class="form-select w-auto">
                                    <option value="">Tất cả</option>
                                    <c:forEach var="pkg" items="${packageList}">
                                        <option value="${pkg.id}" ${selectedPackageId == pkg.id ? 'selected' : ''}>${pkg.name}</option>
                                    </c:forEach>
                                </select>

                                <button type="submit" class="btn btn-primary">Lọc</button>
                            </form>

                            <!-- Bảng thanh toán -->
                            <div class="card">
                                <div class="table-responsive text-nowrap">
                                    <table class="table">
                                        <thead class="table-light">
                                            <tr>
                                                <th>Gói dịch vụ</th>
                                                <th>
                                                    Ngày thanh toán
                                                </th>
                                                <th>
                                                    Ngày hết hạn
                                                </th>
                                                <th>Số tiền</th>
                                                <th>Trạng thái</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:choose>
                                                <c:when test="${empty payments}">
                                                    <tr>
                                                        <td colspan="5" class="text-center text-muted fw-semibold py-4">
                                                            🔔 Bạn chưa có lịch sử thanh toán nào.
                                                        </td>
                                                    </tr>
                                                </c:when>
                                                <c:otherwise>
                                                    <c:forEach var="payment" items="${payments}">
                                                        <tr>
                                                            <td>${payment.packageName}</td>
                                                            <td><fmt:formatDate value="${payment.paymentDate}" pattern="dd/MM/yyyy" /></td>
                                                            <td><fmt:formatDate value="${payment.expireAt}" pattern="dd/MM/yyyy" /></td>

                                                            <td><fmt:formatNumber value="${payment.amount}" type="currency" currencySymbol="₫" /></td>
                                                            <td>
                                                                <span class="badge bg-label-${payment.status == 'Thành công' ? 'success' : 'danger'}">
                                                                    ${payment.status}
                                                                </span>
                                                            </td>
                                                        </tr>
                                                    </c:forEach>
                                                </c:otherwise>
                                            </c:choose>
                                        </tbody>
                                    </table>
                                </div>

                                <!-- Dòng tổng số tiền -->
                                <c:if test="${sessionScope.shopOwner.id == 1}">

                                    <div class="d-flex justify-content-end align-items-center px-4 py-3">
                                        <span class="fw-semibold">Tổng số tiền đã thanh toán:</span>
                                        <span class="fw-bold text-primary ms-2">
                                            <fmt:formatNumber value="${totalAmount}" type="currency" currencySymbol="₫"/>
                                        </span>
                                    </div>
                                </div>
                            </c:if>

                            <!-- Phân trang -->
                            <nav class="mt-3" aria-label="Page navigation">
                                <ul class="pagination justify-content-center">
                                    <c:if test="${currentPage > 1}">
                                        <li class="page-item">
                                            <a class="page-link" href="?shopOwnerId=${id}&page=${currentPage - 1}&packageId=${selectedPackageId}">«</a>
                                        </li>
                                    </c:if>
                                    <c:forEach var="i" begin="1" end="${totalPages}">
                                        <li class="page-item ${i == currentPage ? 'active' : ''}">
                                            <a class="page-link" href="?shopOwnerId=${id}&page=${i}&packageId=${selectedPackageId}">${i}</a>
                                        </li>
                                    </c:forEach>
                                    <c:if test="${currentPage < totalPages}">
                                        <li class="page-item">
                                            <a class="page-link" href="?shopOwnerId=${id}&page=${currentPage + 1}&packageId=${selectedPackageId}">»</a>
                                        </li>
                                    </c:if>
                                </ul>
                            </nav>

                            <div class="card">
                                <div class="card-body">
                                    <h5 class="card-title">Thông tin Shop</h5>
                                    <div class="row mb-3">
                                        <label class="col-sm-2 col-form-label">Tên Shop:</label>
                                        <div class="col-sm-10">
                                            <p class="form-control-plaintext fw-semibold">${shop.shopName}</p>
                                        </div>
                                    </div>
                                    <div class="row mb-3">
                                        <label class="col-sm-2 col-form-label">Tên khách hàng:</label>
                                        <div class="col-sm-10">
                                            <p class="form-control-plaintext">${shop.fullname}</p>
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
                                        <label class="col-sm-2 col-form-label">Mã số thuế:</label>
                                        <div class="col-sm-10">
                                            <p class="form-control-plaintext">${shop.taxNumber}</p>
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
