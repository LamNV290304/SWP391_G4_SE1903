<%-- 
    Document   : adminShopOwnerManagement
    Created on : Jul 3, 2025, 2:09:39 PM
    Author     : Admin
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:if test="${empty sessionScope.shopOwner}">
    <c:redirect url="SaleSphere"/>
</c:if>
<!DOCTYPE html>
<html lang="vi" class="light-style layout-menu-fixed">
    <head>
        <title>Quản lý chủ shop</title>
        <link href="https://fonts.googleapis.com/css2?family=Roboto&display=swap&subset=vietnamese" rel="stylesheet" />
        <link rel="icon" type="image/x-icon" href="img/logoSale.png" />
        <link rel="stylesheet" href="./assets/vendor/fonts/boxicons.css" />
        <link rel="stylesheet" href="./assets/vendor/css/core.css" />
        <link rel="stylesheet" href="./assets/vendor/css/theme-default.css" />
        <link rel="stylesheet" href="./assets/css/demo.css" />
        <link rel="stylesheet" href="./assets/css/custom.css" />
        <link rel="stylesheet" href="./assets/vendor/libs/perfect-scrollbar/perfect-scrollbar.css" />
        <script src="./assets/vendor/js/helpers.js"></script>
        <script src="./assets/js/config.js"></script>
    </head>
    <body>
        <div class="layout-wrapper layout-content-navbar">
            <div class="layout-container">
                <jsp:include page="sidebar.jsp"/>
                <div class="layout-page">
                    <jsp:include page="navbar.jsp"/>
                    <div class="content-wrapper">
                        <div class="container-xxl flex-grow-1 container-p-y">
                            <h4 class="fw-bold mb-4">
                                <span class="text-muted fw-light">Quản trị /</span> Quản lý chủ shop
                            </h4>

                            <!-- Bộ lọc -->
                            <form method="get" class="mb-3 d-flex align-items-center justify-content-end gap-2">
                                <input type="hidden" name="page" value="1" />
                                <label class="form-label mb-0 fw-semibold">Tìm kiếm:</label>
                                <input type="text" name="search" class="form-control w-auto" placeholder="Tên hoặc email" value="${search}" />
                                <button type="submit" class="btn btn-primary">Tìm</button>
                            </form>

                            <!-- Bảng dữ liệu -->
                            <div class="card">
                                <div class="table-responsive text-nowrap">
                                    <table class="table">
                                        <thead class="table-light">
                                            <tr>
                                                <th>ID</th>
                                                <th>Email</th>
                                                <th>Họ tên</th>
                                                <th>Database</th>
                                                <th>Ngày tạo</th>
                                                <th>Mã số thuế</th>
                                                <th>Tên shop</th>
                                                <th>SĐT</th>
                                                <th>Trạng thái</th>
                                                <th>Thao tác</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:choose>
                                                <c:when test="${empty shopOwners}">
                                                    <tr>
                                                        <td colspan="9" class="text-center text-muted fw-semibold py-4">
                                                            ⚠️ Không tìm thấy chủ shop nào phù hợp.
                                                        </td>
                                                    </tr>
                                                </c:when>
                                                <c:otherwise>
                                                    <c:forEach var="owner" items="${shopOwners}">
                                                        <tr>
                                                            <td>${owner.id}</td>
                                                            <td>${owner.email}</td>
                                                            <td>${owner.fullname}</td>
                                                            <td>${owner.databaseName}</td>
                                                            <td>
                                                                <fmt:formatDate value="${owner.createDate}" pattern="dd/MM/yyyy" />
                                                            </td>
                                                            <td>${owner.taxNumber}</td>
                                                            <td>${owner.shopName}</td>
                                                            <td>${owner.phone}</td>
                                                            <td>
                                                                <span class="badge bg-label-${owner.status ? 'success' : 'secondary'}">
                                                                    ${owner.status ? 'Đang hoạt động' : 'Đã khóa'}
                                                                </span>
                                                            </td>
                                                            <td>
                                                                <div class="dropdown">
                                                                    <button type="button" class="btn p-0 dropdown-toggle hide-arrow" data-bs-toggle="dropdown">
                                                                        <i class="bx bx-dots-vertical-rounded"></i>
                                                                    </button>
                                                                    <div class="dropdown-menu">
                                                                        <a href="ShowPaymentHistoryAdmin?shopOwnerId=${owner.id}" class="dropdown-item btn btn-sm btn-outline-primary">Xem Lịch sử giao dịch</a><br>
                                                                        <a href="ShowShopDetail?Id=${owner.id}" class="dropdown-item btn btn-sm btn-outline-primary">Xem thông tin của chủ sở hữu</a>
                                                                    </div>
                                                                </div>
                                                            </td>
                                                        </tr>
                                                    </c:forEach>
                                                </c:otherwise>
                                            </c:choose>
                                        </tbody>
                                    </table>
                                </div>
                            </div>

                            <!-- Phân trang -->
                            <nav class="mt-3" aria-label="Page navigation">
                                <ul class="pagination justify-content-center">
                                    <c:if test="${currentPage > 1}">
                                        <li class="page-item">
                                            <a class="page-link" href="?page=${currentPage - 1}&search=${search}">«</a>
                                        </li>
                                    </c:if>
                                    <c:forEach var="i" begin="1" end="${totalPages}">
                                        <li class="page-item ${i == currentPage ? 'active' : ''}">
                                            <a class="page-link" href="?page=${i}&search=${search}">${i}</a>
                                        </li>
                                    </c:forEach>
                                    <c:if test="${currentPage < totalPages}">
                                        <li class="page-item">
                                            <a class="page-link" href="?page=${currentPage + 1}&search=${search}">»</a>
                                        </li>
                                    </c:if>
                                </ul>
                            </nav>
                        </div>
                        <jsp:include page="footer.jsp"/>
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
