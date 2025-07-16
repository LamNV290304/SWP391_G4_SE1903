<%-- 
    Document   : listProduct
    Created on : Jul 15, 2025, 8:40:24 PM
    Author     : Thai Anh
--%>

<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi"
      class="light-style layout-menu-fixed"
      dir="ltr"
      data-theme="theme-default"
      data-assets-path="./assets/"
      data-template="vertical-menu-template-free">
    <head>
        <title>SaleShape - Danh sách sản phẩm</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="icon" type="image/x-icon" href="img/logoSale.png" />

        <!-- Fonts & Styles -->
        <link href="https://fonts.googleapis.com/css2?family=Public+Sans&display=swap" rel="stylesheet" />
        <link rel="stylesheet" href="./assets/vendor/fonts/boxicons.css" />
        <link rel="stylesheet" href="./assets/vendor/css/core.css" />
        <link rel="stylesheet" href="./assets/vendor/css/theme-default.css" />
        <link rel="stylesheet" href="./assets/css/demo.css" />
        <link rel="stylesheet" href="./assets/vendor/libs/perfect-scrollbar/perfect-scrollbar.css" />
        <script src="./assets/vendor/js/helpers.js"></script>
        <script src="./assets/js/config.js"></script>
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

                            <!-- Card -->
                            <div class="card">
                                <h5 class="card-header">Danh sách sản phẩm toàn thương hiệu </h5>
                                <div class="table-responsive text-nowrap">
                                    <form method="GET" action="ListProductServlet" class="row g-3 px-4 py-3">
                                        <div class="col-md-3">
                                            <input type="text" name="search" class="form-control" placeholder="Tìm theo tên" value="${param.search}">
                                        </div>
                                        <div class="col-md-2">
                                            <input type="number" step="0.01" name="minImportPrice" class="form-control" placeholder="Giá nhập từ" value="${param.minImportPrice}">
                                        </div>
                                        <div class="col-md-2">
                                            <input type="number" step="0.01" name="maxImportPrice" class="form-control" placeholder="Giá nhập đến" value="${param.maxImportPrice}">
                                        </div>
                                        <div class="col-md-2">
                                            <input type="number" step="0.01" name="minSellingPrice" class="form-control" placeholder="Giá bán từ" value="${param.minSellingPrice}">
                                        </div>
                                        <div class="col-md-2">
                                            <input type="number" step="0.01" name="maxSellingPrice" class="form-control" placeholder="Giá bán đến" value="${param.maxSellingPrice}">
                                        </div>
                                        <div class="col-md-2">
                                            <select name="status" class="form-select">
                                                <option value="">Tất cả trạng thái</option>
                                                <option value="true" ${param.status == 'true' ? 'selected' : ''}>Hiển thị</option>
                                                <option value="false" ${param.status == 'false' ? 'selected' : ''}>Ẩn</option>
                                            </select>
                                        </div>
                                        <div class="col-md-3">
                                            <select name="categoryID" class="form-select">
                                                <option value="">Tất cả danh mục</option>
                                                <c:forEach var="c" items="${categoryList}">
                                                    <option value="${c.categoryID}" ${param.categoryID == c.categoryID ? "selected" : ""}>
                                                        ${c.categoryName}
                                                    </option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                        <div class="col-md-3">
                                            <button type="submit" class="btn btn-primary">Lọc</button>
                                            <a href="ListProductServlet" class="btn btn-secondary">Reset</a>
                                        </div>
                                    </form>

                                    <table class="table">
                                        <thead>
                                            <tr>
                                                <th>Mã SP</th>
                                                <th>Tên SP</th>
                                                <th>Danh mục</th>
                                                <th>Đơn vị</th>
                                                <th>Giá nhập</th>
                                                <th>Giá bán</th>
                                                <th>Trạng thái</th>
                                                <th>Thao tác</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach var="p" items="${productList}">
                                                <tr>
                                                    <td>${p.productID}</td>
                                                    <td>${p.productName}</td>
                                                    <td>${p.categoryName}</td>
                                                    <td>${p.unitDescription}</td>
                                                    <td>${p.importPrice}</td>
                                                    <td>${p.sellingPrice}</td>
                                                    <td>
                                                        <c:choose>
                                                            <c:when test="${p.status}"><span class="badge bg-success">Hiển thị</span></c:when>
                                                            <c:otherwise><span class="badge bg-secondary">Ẩn</span></c:otherwise>
                                                        </c:choose>
                                                    </td>
                                                    <td>
                                                        <!-- Nút Sửa -->
                                                        <button class="btn btn-sm btn-outline-primary" data-bs-toggle="modal" data-bs-target="#editModal${p.productID}">
                                                            <i class="bx bx-edit-alt"></i>
                                                        </button>

                                                        <!-- Nút Xóa -->
                                                        <button class="btn btn-sm btn-outline-danger" data-bs-toggle="modal" data-bs-target="#deleteModal${p.productID}">
                                                            <i class="bx bx-trash"></i>
                                                        </button>
                                                    </td>
                                                </tr>
                                                <!-- Modal Sửa -->
                                            <div class="modal fade" id="editModal${p.productID}" tabindex="-1" aria-labelledby="editModalLabel${p.productID}" aria-hidden="true">
                                                <div class="modal-dialog modal-lg">
                                                    <form method="post" action="ListProductServlet">
                                                        <input type="hidden" name="action" value="update" />
                                                        <input type="hidden" name="productID" value="${p.productID}" />
                                                        <div class="modal-content">
                                                            <div class="modal-header">
                                                                <h5 class="modal-title" id="editModalLabel${p.productID}">Cập nhật sản phẩm</h5>
                                                                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Đóng"></button>
                                                            </div>
                                                            <div class="modal-body row g-3">
                                                                <div class="col-md-6">
                                                                    <label class="form-label">Tên sản phẩm</label>
                                                                    <input type="text" class="form-control" name="productName" value="${p.productName}" required>
                                                                </div>
                                                                <div class="col-md-6">
                                                                    <label class="form-label">Danh mục</label>
                                                                    <select name="categoryID" class="form-select">
                                                                        <c:forEach var="c" items="${categoryList}">
                                                                            <option value="${c.categoryID}" ${p.categoryID == c.categoryID ? "selected" : ""}>${c.categoryName}</option>
                                                                        </c:forEach>
                                                                    </select>
                                                                </div>
                                                                <div class="col-md-6">
                                                                    <label class="form-label">Đơn vị</label>
                                                                    <select name="unitID" class="form-select" required>
                                                                        <c:forEach var="u" items="${unitList}">
                                                                            <option value="${u.unitID}" ${p.unitID == u.unitID ? "selected" : ""}>
                                                                                ${u.description}
                                                                            </option>
                                                                        </c:forEach>
                                                                    </select>
                                                                </div>

                                                                <div class="col-md-6">
                                                                    <label class="form-label">Giá nhập</label>
                                                                    <input type="number" step="0.01" class="form-control" name="importPrice" value="${p.importPrice}">
                                                                </div>
                                                                <div class="col-md-6">
                                                                    <label class="form-label">Giá bán</label>
                                                                    <input type="number" step="0.01" class="form-control" name="sellingPrice" value="${p.sellingPrice}">
                                                                </div>
                                                                <div class="col-md-12">
                                                                    <label class="form-label">Mô tả</label>
                                                                    <textarea class="form-control" name="description">${p.description}</textarea>
                                                                </div>
                                                                <div class="col-md-6">
                                                                    <label class="form-label">Trạng thái</label>
                                                                    <select class="form-select" name="status">
                                                                        <option value="true" ${p.status ? "selected" : ""}>Hiển thị</option>
                                                                        <option value="false" ${!p.status ? "selected" : ""}>Ẩn</option>
                                                                    </select>
                                                                </div>
                                                            </div>
                                                            <div class="modal-footer">
                                                                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                                                                <button type="submit" class="btn btn-primary">Lưu</button>
                                                            </div>
                                                        </div>
                                                    </form>
                                                </div>
                                            </div>
                                            <!-- Modal Xóa -->
                                            <div class="modal fade" id="deleteModal${p.productID}" tabindex="-1" aria-labelledby="deleteModalLabel${p.productID}" aria-hidden="true">
                                                <div class="modal-dialog">
                                                    <form method="post" action="ListProductServlet">
                                                        <input type="hidden" name="action" value="delete" />
                                                        <input type="hidden" name="productID" value="${p.productID}" />
                                                        <div class="modal-content">
                                                            <div class="modal-header">
                                                                <h5 class="modal-title">Xóa sản phẩm</h5>
                                                                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Đóng"></button>
                                                            </div>
                                                            <div class="modal-body">
                                                                Bạn có chắc chắn muốn xóa sản phẩm <strong>${p.productName}</strong>?
                                                            </div>
                                                            <div class="modal-footer">
                                                                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                                                                <button type="submit" class="btn btn-danger">Xóa</button>
                                                            </div>
                                                        </div>
                                                    </form>
                                                </div>
                                            </div>

                                        </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                            </div>

                        </div>

                        <!-- Footer -->
                        <jsp:include page="footer.jsp" />

                    </div>
                </div>
            </div>
        </div>

        <!-- Scripts -->
        <script src="assets/vendor/libs/jquery/jquery.js"></script>
        <script src="assets/vendor/libs/popper/popper.js"></script>
        <script src="assets/vendor/js/bootstrap.js"></script>
        <script src="assets/vendor/libs/perfect-scrollbar/perfect-scrollbar.js"></script>
        <script src="assets/vendor/js/menu.js"></script>
        <script src="assets/js/main.js"></script>
    </body>
</html>
