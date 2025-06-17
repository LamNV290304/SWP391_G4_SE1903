<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en" class="light-style layout-menu-fixed" dir="ltr" data-theme="theme-default" data-assets-path="./assets/" data-template="vertical-menu-template-free">
    <head>
        <meta charset="UTF-8">
        <title>Danh sách nhân viên</title>
        <link rel="stylesheet" href="./assets/vendor/css/core.css" />
        <link rel="stylesheet" href="./assets/vendor/css/theme-default.css" />
        <link rel="stylesheet" href="./assets/css/demo.css" />
        <link rel="stylesheet" href="./assets/vendor/fonts/boxicons.css" />
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

                            <!-- Bộ lọc chính -->
                            <form method="get" class="row g-3 align-items-end mb-2">
                                <div class="col-md-3">
                                    <label for="shopFilter" class="form-label">Lọc theo cửa hàng</label>
                                    <select class="form-select" id="shopFilter" name="shopId">
                                        <option value="">Tất cả cửa hàng</option>
                                        <c:forEach var="shop" items="${shopList}">
                                            <option value="${shop.shopID}" ${param.shopId == shop.shopID ? 'selected' : ''}>${shop.shopName}</option>
                                        </c:forEach>
                                    </select>
                                </div>

                                <div class="col-md-3">
                                    <label for="roleFilter" class="form-label">Lọc theo chức vụ</label>
                                    <select class="form-select" id="roleFilter" name="roleId">
                                        <option value="">Tất cả chức vụ</option>
                                        <c:forEach var="role" items="${roleList}">
                                            <option value="${role.id}" ${param.roleId == role.id ? 'selected' : ''}>${role.name}</option>
                                        </c:forEach>
                                    </select>
                                </div>

                                <div class="col-md-2">
                                    <label for="statusFilter" class="form-label">Lọc theo trạng thái</label>
                                    <select class="form-select" id="statusFilter" name="status">
                                        <option value="">Tất cả</option>
                                        <option value="1" ${param.status == '1' ? 'selected' : ''}>Hoạt động</option>
                                        <option value="0" ${param.status == '0' ? 'selected' : ''}>Đã khoá</option>
                                    </select>
                                </div>

                                <div class="col-md-2">
                                    <label for="sort" class="form-label">Sắp xếp theo</label>
                                    <select class="form-select" id="sort" name="sort">
                                        <option value="">Mặc định</option>
                                        <option value="name_asc" ${param.sort == 'name_asc' ? 'selected' : ''}>Tên A → Z</option>
                                        <option value="name_desc" ${param.sort == 'name_desc' ? 'selected' : ''}>Tên Z → A</option>
                                    </select>
                                </div>

                                <div class="col-md-2 d-flex align-items-end">
                                    <button type="submit" class="btn btn-primary w-100">Lọc</button>
                                </div>
                            </form>

                            <!-- Thanh tìm kiếm riêng dòng -->
                            <form method="get" class="mb-4">
                                <input type="hidden" name="shopId" value="${param.shopId}">
                                <input type="hidden" name="roleId" value="${param.roleId}">
                                <input type="hidden" name="status" value="${param.status}">
                                <input type="hidden" name="sort" value="${param.sort}">
                                <div class="row">
                                    <div class="col-md-6">
                                        <input type="text" class="form-control" id="keyword" name="keyword" placeholder="Nhập tên hoặc email để tìm kiếm..." value="${param.keyword}" />
                                    </div>
                                    <div class="col-md-2">
                                        <button type="submit" class="btn btn-outline-secondary">Tìm kiếm</button>
                                    </div>
                                </div>
                            </form>

                            <div class="card">
                                <h5 class="card-header">Danh sách nhân viên</h5>
                                <div class="table-responsive text-nowrap">
                                    <table class="table">
                                        <thead>
                                            <tr>
                                                <th>Tên nhân viên</th>
                                                <th>Email</th>
                                                <th>Số điện thoại</th>
                                                <th>Trạng thái</th>
                                                <th>Ngày tạo</th>
                                                <th>Shop</th>
                                                <th>Chức vụ</th>
                                                <th>Hành động</th>
                                            </tr>
                                        </thead>
                                        <tbody class="table-border-bottom-0">
                                            <c:forEach var="e" items="${employeeList}">
                                                <tr>
                                                    <td><i class="bx bx-user me-2"></i> ${e.fullName}</td>
                                                    <td>${e.email}</td>
                                                    <td>${e.phone}</td>
                                                    <td>
                                                        <c:choose>
                                                            <c:when test="${e.status}">
                                                                <span class="badge bg-label-success">Hoạt động</span>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <span class="badge bg-label-danger">Đã khoá</span>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </td>
                                                    <td>${e.createdDate}</td>
                                                    <td>${e.shopName}</td>
                                                    <td>${e.role}</td>
                                                    <td>
                                                        <div class="dropdown">
                                                            <button type="button" class="btn p-0 dropdown-toggle hide-arrow" data-bs-toggle="dropdown">
                                                                <i class="bx bx-dots-vertical-rounded"></i>
                                                            </button>
                                                            <div class="dropdown-menu">
                                                                <a class="dropdown-item" href="ShowDetailEmployee?id=${e.id}"><i class="bx bx-edit-alt me-1"></i> Chi tiết</a>
                                                                <a class="dropdown-item" href="employee-lock?id=${e.id}"><i class="bx bx-trash me-1"></i> Khoá tài khoản</a>
                                                            </div>
                                                        </div>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                            </div>

                            <!-- Pagination -->
                            <nav aria-label="Page navigation" class="mt-4">
                                <ul class="pagination justify-content-center">
                                    <c:if test="${currentPage > 1}">
                                        <li class="page-item">
                                            <a class="page-link"
                                               href="?page=${currentPage - 1}&shopId=${param.shopId}&roleId=${param.roleId}&status=${param.status}&sort=${param.sort}&keyword=${param.keyword}">Trước</a>
                                        </li>
                                    </c:if>

                                    <c:forEach var="i" begin="1" end="${totalPages}">
                                        <li class="page-item ${i == currentPage ? 'active' : ''}">
                                            <a class="page-link"
                                               href="?page=${i}&shopId=${param.shopId}&roleId=${param.roleId}&status=${param.status}&sort=${param.sort}&keyword=${param.keyword}">${i}</a>
                                        </li>
                                    </c:forEach>

                                    <c:if test="${currentPage < totalPages}">
                                        <li class="page-item">
                                            <a class="page-link"
                                               href="?page=${currentPage + 1}&shopId=${param.shopId}&roleId=${param.roleId}&status=${param.status}&sort=${param.sort}&keyword=${param.keyword}">Tiếp</a>
                                        </li>
                                    </c:if>
                                </ul>
                            </nav>
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
