<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="vi" class="light-style layout-menu-fixed"
      dir="ltr" data-theme="theme-default"
      data-assets-path="./assets/" data-template="vertical-menu-template-free">
    <head>
        <title>SaleShape</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">

        <!-- Fonts & Styles -->
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
                                <span class="text-muted fw-light">Đăng ký /</span> Gói dịch vụ
                            </h4>

                            <!-- Thông báo -->
                            <c:if test="${not empty param.error}">
                                <div class="alert alert-danger">${param.error}</div>
                            </c:if>
                            <c:if test="${not empty param.success}">
                                <div class="alert alert-success">${param.success}</div>
                            </c:if>

                            <!-- Nút thêm gói mới -->
                            <c:if test="${sessionScope.shopOwner.id == 1}">
                                <div class="mb-3 text-end">
                                    <button class="btn btn-success" data-bs-toggle="modal" data-bs-target="#addPackageModal">
                                        + Thêm gói mới
                                    </button>
                                </div>
                            </c:if>
                            <c:if test="${not empty sessionScope.flash_success}">
                                <div class="alert alert-success">${sessionScope.flash_success}</div>
                                <c:remove var="flash_success" scope="session" />
                            </c:if>
                            <c:if test="${not empty sessionScope.flash_success}">
                                <div class="alert alert-danger">${sessionScope.flash_fail}</div>
                                <c:remove var="flash_fail" scope="session" />
                            </c:if>

                            <!-- Danh sách gói -->
                            <div class="row">
                                <c:forEach var="pkg" items="${packages}">
                                    <div class="col-md-6 col-lg-4 mb-4">
                                        <div class="card h-100">
                                            <div class="card-body text-center">
                                                <h5 class="card-title">${pkg.name}</h5>
                                                <p class="text-muted mb-1">Thời hạn: <strong>${pkg.durationInDays}</strong> ngày</p>
                                                <p class="text-muted">Giá: <strong><fmt:formatNumber value="${pkg.price}" type="currency" currencySymbol="₫"/></strong></p>
                                                <p class="mb-3">${pkg.description}</p>

                                                <!-- Admin: chỉnh sửa -->
                                                <c:if test="${sessionScope.shopOwner.id == 1}">
                                                    <button class="btn btn-warning" data-bs-toggle="modal" data-bs-target="#editModal${pkg.id}">
                                                        Chỉnh sửa gói
                                                    </button>
                                                </c:if>

                                                <!-- Người dùng thường: chọn gói -->
                                                <c:if test="${sessionScope.shopOwner.id != 1}">
                                                    <form method="post" action="RegisterPackage"
                                                          onsubmit="${hasActivePackage ? 'return confirmChangePackage();' : ''}">
                                                        <input type="hidden" name="packageId" value="${pkg.id}" />
                                                        <button type="submit" class="btn btn-primary">Chọn gói</button>
                                                    </form>
                                                </c:if>
                                            </div>
                                        </div>
                                    </div>

                                    <!-- Modal chỉnh sửa -->
                                    <div class="modal fade" id="editModal${pkg.id}" tabindex="-1" aria-labelledby="editModalLabel${pkg.id}" aria-hidden="true">
                                        <div class="modal-dialog">
                                            <form action="EditPackage" method="post" class="modal-content">
                                                <div class="modal-header">
                                                    <h5 class="modal-title" id="editModalLabel${pkg.id}">Chỉnh sửa: ${pkg.name}</h5>
                                                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                                                </div>
                                                <div class="modal-body">
                                                    <input type="hidden" name="id" value="${pkg.id}" />
                                                    <div class="mb-3">
                                                        <label class="form-label">Tên gói</label>
                                                        <input type="text" name="name" class="form-control" value="${pkg.name}" required />
                                                    </div>
                                                    <div class="mb-3">
                                                        <label class="form-label">Thời hạn (ngày)</label>
                                                        <input type="number" name="durationInDays" class="form-control" value="${pkg.durationInDays}" required />
                                                    </div>
                                                    <div class="mb-3">
                                                        <label class="form-label">Giá</label>
                                                        <input type="number" name="price" class="form-control" value="${pkg.price}" required />
                                                    </div>
                                                    <div class="mb-3">
                                                        <label class="form-label">Mô tả</label>
                                                        <textarea name="description" class="form-control" rows="3">${pkg.description}</textarea>
                                                    </div>
                                                </div>
                                                <div class="modal-footer">
                                                    <button type="submit" class="btn btn-primary">Lưu thay đổi</button>
                                                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                                                </div>
                                            </form>
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>
                        </div>

                        <!-- Modal thêm mới -->
                        <c:if test="${sessionScope.shopOwner.id == 1}">
                            <div class="modal fade" id="addPackageModal" tabindex="-1" aria-labelledby="addPackageLabel" aria-hidden="true">
                                <div class="modal-dialog">
                                    <form action="CreatePackage" method="post" class="modal-content">
                                        <div class="modal-header">
                                            <h5 class="modal-title" id="addPackageLabel">Thêm gói dịch vụ mới</h5>
                                            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                                        </div>
                                        <div class="modal-body">
                                            <div class="mb-3">
                                                <label class="form-label">Tên gói</label>
                                                <input type="text" name="name" class="form-control" required />
                                            </div>
                                            <div class="mb-3">
                                                <label class="form-label">Thời hạn (ngày)</label>
                                                <input type="number" name="durationInDays" class="form-control" required />
                                            </div>
                                            <div class="mb-3">
                                                <label class="form-label">Giá</label>
                                                <input type="number" name="price" class="form-control" required />
                                            </div>
                                            <div class="mb-3">
                                                <label class="form-label">Mô tả</label>
                                                <textarea name="description" class="form-control" rows="3"></textarea>
                                            </div>
                                        </div>
                                        <div class="modal-footer">
                                            <button type="submit" class="btn btn-primary">Thêm mới</button>
                                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                                        </div>
                                    </form>
                                </div>
                            </div>
                        </c:if>

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
                return confirm("Bạn đã có gói dịch vụ đang sử dụng.\nViệc chọn gói mới sẽ hủy kích hoạt gói hiện tại.\nBạn có chắc chắn muốn tiếp tục?");
            }
        </script>
    </body>
</html>
