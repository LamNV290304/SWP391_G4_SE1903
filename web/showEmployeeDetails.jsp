<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en" class="light-style layout-menu-fixed" dir="ltr" data-theme="theme-default" data-assets-path="./assets/" data-template="vertical-menu-template-free">
    <head>
        <meta charset="UTF-8">
        <title>Chi tiết nhân viên</title>
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

                            <div class="card mb-4">
                                <h5 class="card-header">Chi tiết nhân viên</h5>
                                <div class="card-body">
                                    <div class="row mb-3">
                                        <label class="col-sm-3 col-form-label">Tên nhân viên:</label>
                                        <div class="col-sm-9">
                                            <p class="form-control-plaintext">${employee.fullName}</p>
                                        </div>
                                    </div>

                                    <div class="row mb-3">
                                        <label class="col-sm-3 col-form-label">Email:</label>
                                        <div class="col-sm-9">
                                            <p class="form-control-plaintext">${employee.email}</p>
                                        </div>
                                    </div>

                                    <div class="row mb-3">
                                        <label class="col-sm-3 col-form-label">Số điện thoại:</label>
                                        <div class="col-sm-9">
                                            <p class="form-control-plaintext">${employee.phone}</p>
                                        </div>
                                    </div>

                                    <div class="row mb-3">
                                        <label class="col-sm-3 col-form-label">Trạng thái:</label>
                                        <div class="col-sm-9">
                                            <c:choose>
                                                <c:when test="${employee.status}">
                                                    <span class="badge bg-label-success">Hoạt động</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="badge bg-label-danger">Đã khoá</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </div>
                                    </div>

                                    <div class="row mb-3">
                                        <label class="col-sm-3 col-form-label">Ngày tạo:</label>
                                        <div class="col-sm-9">
                                            <p class="form-control-plaintext">${employee.createdDate}</p>
                                        </div>
                                    </div>

                                    <div class="row mb-3">
                                        <label class="col-sm-3 col-form-label">Cửa hàng:</label>
                                        <div class="col-sm-9">
                                            <p class="form-control-plaintext">${employee.shopName}</p>
                                        </div>
                                    </div>

                                    <div class="row mb-3">
                                        <label class="col-sm-3 col-form-label">Chức vụ:</label>
                                        <div class="col-sm-9">
                                            <p class="form-control-plaintext">${employee.role}</p>
                                        </div>
                                    </div>

                                    <div class="mt-4 d-flex justify-content-between">
                                        <a href="ShowEmployeeList" class="btn btn-outline-secondary"><i class="bx bx-arrow-back"></i> Quay lại danh sách</a>
                                        <!-- Nút mở Modal -->
                                        <button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#editEmployeeModal">
                                            <i class="bx bx-edit-alt"></i> Chỉnh sửa
                                        </button>
                                    </div>
                                </div>
                            </div>

                        </div>

                        <!-- Footer -->
                        <jsp:include page="footer.jsp" />
                    </div>
                </div>
            </div>
        </div>

        <!-- Modal chỉnh sửa nhân viên -->
        <div class="modal fade" id="editEmployeeModal" tabindex="-1" aria-labelledby="editEmployeeModalLabel" aria-hidden="true">
            <div class="modal-dialog modal-lg">
                <form method="post" action="ShowDetailEmployee">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title" id="editEmployeeModalLabel">Chỉnh sửa thông tin nhân viên</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Đóng"></button>
                        </div>
                        <div class="modal-body">
                            <input type="hidden" name="id" value="${employee.id}">

                            <div class="mb-3">
                                <label class="form-label">Tên nhân viên</label>
                                <input type="text" class="form-control" name="fullName" value="${employee.fullName}" required>
                            </div>

                            <div class="mb-3">
                                <label class="form-label">Email</label>
                                <input type="email" class="form-control" name="email" value="${employee.email}" required>
                            </div>

                            <div class="mb-3">
                                <label class="form-label">Số điện thoại</label>
                                <input type="text" class="form-control" name="phone" value="${employee.phone}" required>
                            </div>

                            <div class="mb-3">
                                <label class="form-label">Trạng thái</label>
                                <select class="form-select" name="status" required>
                                    <option value="1" ${employee.status ? 'selected' : ''}>Hoạt động</option>
                                    <option value="0" ${!employee.status ? 'selected' : ''}>Đã khoá</option>
                                </select>
                            </div>

                            <div class="mb-3">
                                <label class="form-label">Chức vụ</label>
                                <select class="form-select" name="roleId" required>
                                    <c:forEach var="role" items="${roleList}">
                                        <option value="${role.id}" ${employee.role == role.name ? 'selected' : ''}>${role.name}</option>
                                    </c:forEach>
                                </select>
                            </div>

                            <div class="mb-3">
                                <label class="form-label">Cửa hàng</label>
                                <select class="form-select" name="shopId" required>
                                    <c:forEach var="shop" items="${shopList}">
                                        <option value="${shop.shopID}" ${employee.shopName == shop.shopName ? 'selected' : ''}>${shop.shopName}</option>
                                    </c:forEach>
                                </select>
                            </div>

                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-outline-secondary" data-bs-dismiss="modal">Hủy</button>
                            <button type="submit" class="btn btn-primary">Lưu thay đổi</button>
                        </div>
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
 