<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<c:if test="${empty sessionScope.Employee}">
    <c:redirect url="loginEmployee.jsp"/>
</c:if>
<html
    lang="vi"
    class="light-style layout-menu-fixed"
    dir="ltr"
    data-theme="theme-default"
    data-assets-path="./assets/"
    data-template="vertical-menu-template-free"
    >
    <head>
        <title>SaleShape</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link href="https://fonts.googleapis.com/css2?family=Roboto&display=swap&subset=vietnamese" rel="stylesheet">

        <link href="https://fonts.googleapis.com/css2?family=Roboto&display=swap" rel="stylesheet" />
        <link rel="icon" type="image/x-icon" href="img/logoSale.png" />
        <link rel="preconnect" href="https://fonts.googleapis.com" />
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
        <link
            href="https://fonts.googleapis.com/css2?family=Public+Sans:ital,wght@0,300;0,400;0,500;0,600;0,700;1,300;1,400;1,500;1,600;1,700&display=swap"
            rel="stylesheet"
            />

        <link rel="stylesheet" href="./assets/css/custom.css" />

        <link rel="stylesheet" href="./assets/vendor/fonts/boxicons.css" />

        <link rel="stylesheet" href="./assets/vendor/css/core.css" class="template-customizer-core-css" />
        <link rel="stylesheet" href="./assets/vendor/css/theme-default.css" class="template-customizer-theme-css" />
        <link rel="stylesheet" href="./assets/css/demo.css" />

        <link rel="stylesheet" href="./assets/vendor/libs/perfect-scrollbar/perfect-scrollbar.css" />

        <link rel="stylesheet" href="./assets/vendor/libs/apex-charts/apex-charts.css" />

        <script src="./assets/vendor/js/helpers.js"></script>

        <script src="./assets/js/config.js"></script>
    </head>
    <body>
        <div  class="layout-wrapper layout-content-navbar">
            <div class="layout-container">
                <jsp:include page="sidebar.jsp" />

                <div class="layout-page">
                    <jsp:include page="navBar.jsp" />
                    <div class="content-wrapper">
                        <div class="container-xxl flex-grow-1 container-p-y">
                            <div class="card">
                                <h5 class="card-header">Danh sách cửa hàng</h5>
                                <div class="table-responsive text-nowrap">
                                    <button type="button" class="btn btn-primary mb-3" data-bs-toggle="modal" data-bs-target="#addShopModal">
                                        Thêm cửa hàng
                                    </button>

                                    <!-- Modal Thêm Cửa Hàng -->
                                    <div class="modal fade" id="addShopModal" tabindex="-1" aria-labelledby="addShopModalLabel" aria-hidden="true">
                                        <div class="modal-dialog modal-lg">
                                            <div class="modal-content">
                                                <form action="ListShopServlet" method="post">
                                                    <div class="modal-header">
                                                        <h5 class="modal-title" id="addShopModalLabel">Thêm cửa hàng mới</h5>
                                                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Đóng"></button>
                                                    </div>
                                                    <div class="modal-body">
                                                        <div class="mb-3">
                                                            <label for="shopName" class="form-label">Tên cửa hàng</label>
                                                            <input type="text" class="form-control" id="shopName" name="shopName" required>
                                                        </div>
                                                        <div class="mb-3">
                                                            <label for="shopCode" class="form-label">Mã cửa hàng</label>
                                                            <input type="text" class="form-control" id="shopCode" name="shopCode" required>
                                                        </div>
                                                        <div class="mb-3">
                                                            <label for="address" class="form-label">Địa chỉ</label>
                                                            <input type="text" class="form-control" id="address" name="address" required>
                                                        </div>
                                                        <div class="mb-3">
                                                            <label for="phone" class="form-label">Số điện thoại</label>
                                                            <input type="text" class="form-control" id="phone" name="phone" required>
                                                        </div>
                                                        <div class="mb-3">
                                                            <label for="createdDate" class="form-label">Ngày tạo</label>
                                                            <input type="date" class="form-control" id="createdDate" name="createdDate" required>
                                                        </div>
                                                        <div class="mb-3">
                                                            <label for="status" class="form-label">Trạng thái</label>
                                                            <select class="form-select" id="status" name="status">
                                                                <option value="1">Đang hoạt động</option>
                                                                <option value="0">Ngưng hoạt động</option>
                                                            </select>
                                                        </div>
                                                    </div>
                                                    <div class="modal-footer">
                                                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                                                        <button type="submit" class="btn btn-primary">Lưu</button>
                                                    </div>
                                                </form>
                                            </div>
                                        </div>
                                    </div>


                                    <table class="table">
                                        <thead>
                                            <tr>
                                                <th>Mã</th>
                                                <th>Tên cửa hàng</th>
                                                <th>Địa chỉ</th>
                                                <th>Phone</th>
                                                <th>Ngày Tạo</th>
                                                <th>Trạng Thái</th>
                                                <th>Thao tác</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach var="s" items="${listShop}">
                                                <tr>
                                                    <td>${s.shopID}</td>
                                                    <td>${s.shopName}</td>
                                                    <td>${s.address}</td>
                                                    <td>${s.phone}</td>
                                                    <td><fmt:formatDate value="${s.createdDate}" pattern="dd-MM-yyyy" /></td>

                                                    <td>
                                                        <c:choose>
                                                            <c:when test="${s.status}"><span class="badge bg-success">Hoạt động</span></c:when>
                                                            <c:otherwise><span class="badge bg-secondary">Ngừng</span></c:otherwise>
                                                        </c:choose>
                                                    </td>
                                                    <td>
                                                        <button type="button" class="btn btn-warning btn-sm"
                                                                data-bs-toggle="modal" data-bs-target="#editShopModal${s.shopID}">
                                                            Sửa
                                                        </button>

                                                        <!-- Nút xóa -->
                                                        <button type="button" class="btn btn-danger btn-sm"
                                                                data-bs-toggle="modal" data-bs-target="#deleteShopModal${s.shopID}">
                                                            Xóa
                                                        </button>
                                                    </td>
                                                </tr>
                                                <!-- Modal Sửa -->
                                    <div class="modal fade" id="editShopModal${s.shopID}" tabindex="-1" aria-labelledby="editShopModalLabel${s.shopID}" aria-hidden="true">
                                        <div class="modal-dialog modal-lg">
                                            <div class="modal-content">
                                                <form action="ListShopServlet" method="post">
                                                    <input type="hidden" name="shopID" value="${s.shopID}">
                                                    <div class="modal-header">
                                                        <h5 class="modal-title" id="editShopModalLabel${s.shopID}">Sửa cửa hàng</h5>
                                                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                                                    </div>
                                                    <div class="modal-body">
                                                        <div class="mb-3">
                                                            <label class="form-label">Tên cửa hàng</label>
                                                            <input type="text" class="form-control" name="shopName" value="${s.shopName}" required>
                                                        </div>
                                                        <div class="mb-3">
                                                            <label class="form-label">Mã cửa hàng</label>
                                                            <input type="text" class="form-control" name="shopCode" value="${s.shopID}" readonly>
                                                        </div>
                                                        <div class="mb-3">
                                                            <label class="form-label">Địa chỉ</label>
                                                            <input type="text" class="form-control" name="address" value="${s.address}" required>
                                                        </div>
                                                        <div class="mb-3">
                                                            <label class="form-label">Số điện thoại</label>
                                                            <input type="text" class="form-control" name="phone" value="${s.phone}" required>
                                                        </div>
                                                        <div class="mb-3">
                                                            <label class="form-label">Ngày tạo</label>
                                                            <input type="date" class="form-control" name="createdDate" value="${s.createdDate}" required>
                                                        </div>
                                                        <div class="mb-3">
                                                            <label class="form-label">Trạng thái</label>
                                                            <select class="form-select" name="status">
                                                                <option value="true" ${s.status ? 'selected' : ''}>Đang hoạt động</option>
                                                                s<option value="false" ${!s.status ? 'selected' : ''}>Ngưng hoạt động</option>
                        
                                                            </select>
                                                        </div>
                                                    </div>
                                                    <div class="modal-footer">
                                                        <button type="submit" class="btn btn-primary">Cập nhật</button>
                                                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Đóng</button>
                                                    </div>
                                                </form>
                                            </div>
                                        </div>
                                    </div>
                                             <!-- Modal Xóa -->
                                    <div class="modal fade" id="deleteShopModal${s.shopID}" tabindex="-1" aria-labelledby="deleteShopModalLabel${s.shopID}" aria-hidden="true">
                                        <div class="modal-dialog">
                                            <div class="modal-content">
                                                <form action="ListShopServlet" method="post">
                                                    <input type="hidden" name="shopID" value="${s.shopID}">
                                                    <div class="modal-header">
                                                        <h5 class="modal-title" id="deleteShopModalLabel${s.shopID}">Xác nhận xóa</h5>
                                                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                                                    </div>
                                                    <div class="modal-body">
                                                        Bạn có chắc muốn xóa cửa hàng <strong>${s.shopName}</strong>?
                                                    </div>
                                                    <div class="modal-footer">
                                                        <button type="submit" class="btn btn-danger">Xóa</button>
                                                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                                                    </div>
                                                </form>
                                            </div>
                                        </div>
                                    </div>

                                            </c:forEach>
                                        </tbody>
                                    </table>
                                    
                                   

                                </div>
                            </div>
                        </div>
                        <jsp:include page="footer.jsp" />
                    </div>
                </div>

            </div>
        </div>


        <script src="assets/vendor/libs/jquery/jquery.js"></script>
        <script src="assets/vendor/libs/popper/popper.js"></script>
        <script src="assets/vendor/js/bootstrap.js"></script>
        <script src="assets/vendor/libs/perfect-scrollbar/perfect-scrollbar.js"></script>
        <script src="assets/vendor/js/menu.js"></script> <script src="assets/js/main.js"></script> </body>
</html>