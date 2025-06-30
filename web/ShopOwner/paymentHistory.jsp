<%-- 
    Document   : paymentHistory
    Created on : Jun 23, 2025, 11:33:14 PM
    Author     : Admin
--%>
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
                            <h4 class="fw-bold mb-4">
                                <span class="text-muted fw-light">Tài khoản /</span> Lịch sử thanh toán
                            </h4>

                            <!-- Lọc gói -->
                            <form method="get" class="mb-3 d-flex align-items-center justify-content-end gap-2">
                                <input type="hidden" name="sort" value="${sort}" />
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
                            <form method="get" class="mb-3 d-flex align-items-center justify-content-end">
                                <input type="hidden" name="sort" value="${sort}" />
                                <input type="hidden" name="page" value="1" />
                                <label class="form-label me-2 mb-0 fw-semibold">Lọc theo gói:</label>
                                <select name="packageId" class="form-select w-auto" onchange="this.form.submit()">
                                    <option value="">Tất cả</option>
                                    <c:forEach var="pkg" items="${packageList}">
                                        <option value="${pkg.id}" ${selectedPackageId == pkg.id ? 'selected' : ''}>${pkg.name}</option>
                                    </c:forEach>
                                </select>
                            </form>

                            <div class="card">
                                <div class="table-responsive text-nowrap">
                                    <table class="table">
                                        <thead class="table-light">
                                            <tr>
                                                <th>
                                                    <a href="?page=${currentPage}&sort=packageName:${nextSort['packageName']},paymentDate:${sortMap['paymentDate']},expireAt:${sortMap['expireAt']}&packageId=${selectedPackageId}" class="text-dark text-decoration-none">
                                                        Gói dịch vụ
                                                        <c:if test="${sortField == 'packageName'}">
                                                            <i class="bx bx-chevron-${sortDir == 'asc' ? 'up' : 'down'}"></i>
                                                        </c:if>
                                                    </a>
                                                </th>
                                                <th>
                                                    <a href="?page=${currentPage}&sort=paymentDate:${nextSort['paymentDate']},packageName:${sortMap['packageName']},expireAt:${sortMap['expireAt']}&packageId=${selectedPackageId}" class="text-dark text-decoration-none">
                                                        Ngày thanh toán
                                                        <c:if test="${sortField == 'paymentDate'}">
                                                            <i class="bx bx-chevron-${sortDir == 'asc' ? 'up' : 'down'}"></i>
                                                        </c:if>
                                                    </a>
                                                </th>
                                                <th>
                                                    <a href="?page=${currentPage}&sort=expireAt:${nextSort['expireAt']},paymentDate:${sortMap['paymentDate']},packageName:${sortMap['packageName']}&packageId=${selectedPackageId}" class="text-dark text-decoration-none">
                                                        Ngày hết hạn
                                                        <c:if test="${sortField == 'expireAt'}">
                                                            <i class="bx bx-chevron-${sortDir == 'asc' ? 'up' : 'down'}"></i>
                                                        </c:if>
                                                    </a>
                                                </th>
                                                <th>Số tiền</th>
                                                <th>Trạng thái</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:choose>
                                                <c:when test="${empty payments}">
                                                    <tr>
                                                        <td colspan="5" class="text-center text-muted fw-semibold py-4">🔔 Bạn chưa có lịch sử thanh toán nào.</td>
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
                            </div>

                            <!-- Pagination -->
                            <nav class="mt-3" aria-label="Page navigation">
                                <ul class="pagination justify-content-center">
                                    <c:if test="${currentPage > 1}">
                                        <li class="page-item">
                                            <a class="page-link" href="?page=${currentPage - 1}&sort=${sort}&packageId=${selectedPackageId}">«</a>
                                        </li>
                                    </c:if>
                                    <c:forEach var="i" begin="1" end="${totalPages}">
                                        <li class="page-item ${i == currentPage ? 'active' : ''}">
                                            <a class="page-link" href="?page=${i}&sort=${sort}&packageId=${selectedPackageId}">${i}</a>
                                        </li>
                                    </c:forEach>
                                    <c:if test="${currentPage < totalPages}">
                                        <li class="page-item">
                                            <a class="page-link" href="?page=${currentPage + 1}&sort=${sort}&packageId=${selectedPackageId}">»</a>
                                        </li>
                                    </c:if>
                                </ul>
                            </nav>
                        </div>
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





