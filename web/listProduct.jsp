<%-- 
    Document   : listProduct
    Created on : Jul 15, 2025, 8:40:24 PM
    Author     : Thai Anh
--%>

<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<c:if test="${empty sessionScope.Employee}">
    <c:redirect url="loginEmployee.jsp"/>
</c:if>
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
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">

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
                                    <button class="btn btn-success" onclick="openAddModal()">Thêm sản phẩm</button>
                                    <!-- Modal -->
                                    <div class="modal fade" id="productModal" tabindex="-1" aria-labelledby="productModalLabel" aria-hidden="true">
                                        <div class="modal-dialog modal-lg">
                                            <div class="modal-content">
                                                <form id="productForm" method="post" action="ListProductServlet" enctype="multipart/form-data">
                                                    <div class="modal-header">
                                                        <h5 class="modal-title" id="productModalLabel">Thêm Sản phẩm</h5>
                                                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Đóng"></button>
                                                    </div>
                                                    <div class="modal-body">

                                                        <!-- Hidden field for productID (for editing) -->
                                                        <input type="hidden" name="productID" id="productID">

                                                        <div class="mb-3">
                                                            <label for="productName" class="form-label">Tên sản phẩm</label>
                                                            <input type="text" class="form-control" id="productName" name="productName" required>
                                                        </div>

                                                        <div class="mb-3">
                                                            <label for="categoryID" class="form-label">Loại sản phẩm</label>
                                                            <select name="categoryID" class="form-select">
                                                                <c:forEach var="c" items="${categoryList}">
                                                                    <option value="${c.categoryID}" ${param.categoryID == c.categoryID ? "selected" : ""}>
                                                                        ${c.categoryName}
                                                                    </option>
                                                                </c:forEach>
                                                            </select>
                                                        </div>

                                                        <div class="mb-3">
                                                            <label for="unitID" class="form-label">Đơn vị tính</label>
                                                            <select name="unitID" class="form-select" required>
                                                                <c:forEach var="u" items="${unitList}">
                                                                    <option value="${u.unitID}" ${param.unitID == u.unitID ? "selected" : ""}>
                                                                        ${u.description}
                                                                    </option>
                                                                </c:forEach>
                                                            </select>
                                                        </div>

                                                        <div class="row">
                                                            <div class="col-md-6 mb-3">
                                                                <label for="importPrice" class="form-label">Giá nhập</label>
                                                                <input type="number" step="0.01" class="form-control" id="importPrice" name="importPrice" required>
                                                            </div>
                                                            <div class="col-md-6 mb-3">
                                                                <label for="sellingPrice" class="form-label">Giá bán</label>
                                                                <input type="number" step="0.01" class="form-control" id="sellingPrice" name="sellingPrice" required>
                                                            </div>
                                                        </div>

                                                        <div class="mb-3">
                                                            <label for="description" class="form-label">Mô tả</label>
                                                            <textarea class="form-control" id="description" name="description"></textarea>
                                                        </div>

                                                        <div class="mb-3">
                                                            <label for="status" class="form-label">Trạng thái</label>
                                                            <select class="form-select" id="status" name="status">
                                                                <option value="true">Đang bán</option>
                                                                <option value="false">Ngừng bán</option>
                                                            </select>
                                                        </div>

                                                        <!-- Thêm image nếu cần -->

                                                        <div class="mb-3">
                                                            <label for="image" class="form-label">Hình ảnh sản phẩm</label>
                                                            <input type="file" class="form-control" id="image" name="image" accept="image/*">
                                                        </div>

                                                        <!-- Hiển thị hình ảnh hiện tại nếu có -->
                                                        <div class="mb-3">
                                                            <label class="form-label">Xem trước ảnh:</label><br>
                                                            <img id="previewImage" src="#" alt="Chưa có ảnh" style="max-height: 150px; display: none;" class="border rounded mt-1">
                                                        </div>

                                                    </div>
                                                    <div class="modal-footer">
                                                        <input type="hidden" name="action" id="action" value="add">
                                                        <button type="submit" class="btn btn-primary">Lưu</button>
                                                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                                                    </div>

                                                    <!-- Hidden action -->

                                                </form>
                                            </div>
                                        </div>
                                    </div>

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
                                                    <button class="btn btn-sm btn-outline-primary"
                                                            data-bs-toggle="modal" 
                                                            data-bs-target="#editModal"
                                                            data-id="${p.productID}"
                                                            data-name="${p.productName}"
                                                            data-import="${p.importPrice}"
                                                            data-sell="${p.sellingPrice}"
                                                            data-category="${p.categoryID}"
                                                            data-unit="${p.unitID}"
                                                            data-image="${p.imageUrl}">
                                                        <i class="bx bx-edit-alt"></i>
                                                    </button>


                                                    <button class="btn btn-sm btn-outline-danger"
                                                            data-bs-toggle="modal" data-bs-target="#deleteModal"
                                                            data-id="${p.productID}"
                                                            data-name="${p.productName}">
                                                        <i class="bx bx-trash"></i>
                                                    </button>
                                                </td>
                                            </tr>
                                        </c:forEach>

                                        <!-- Modal Sửa Duy Nhất -->
                                        <div class="modal fade" id="editModal" tabindex="-1" aria-hidden="true">
                                            <div class="modal-dialog modal-lg">
                                                <form method="post" action="ListProductServlet" enctype="multipart/form-data">
                                                    <div class="modal-content">
                                                        <div class="modal-header">
                                                            <h5 class="modal-title">Cập nhật sản phẩm</h5>
                                                            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                                                        </div>
                                                        <div class="modal-body row">
                                                            <input type="hidden" name="productID" id="edit-id" />
                                                            <!-- Các input -->
                                                            <div class="mb-3 col-md-6">
                                                                <label class="form-label">Tên sản phẩm</label>
                                                                <input class="form-control" type="text" id="edit-name" name="productName" required />
                                                            </div>
                                                            <div class="mb-3 col-md-6">
                                                                <label class="form-label">Giá nhập</label>
                                                                <input class="form-control" type="number" id="edit-import" name="importPrice" min="0" required />
                                                            </div>
                                                            <div class="mb-3 col-md-6">
                                                                <label class="form-label">Giá bán</label>
                                                                <input class="form-control" type="number" id="edit-sell" name="sellingPrice" min="0" required />
                                                            </div>
                                                            <div class="mb-3 col-md-6">
                                                                <label class="form-label">Danh mục</label>
                                                                <select name="categoryID" id="edit-category" class="form-select" required>
                                                                    <c:forEach var="c" items="${categoryList}">
                                                                        <option value="${c.categoryID}">${c.categoryName}</option>
                                                                    </c:forEach>
                                                                </select>
                                                            </div>
                                                            <div class="mb-3 col-md-6">
                                                                <label class="form-label">Đơn vị tính</label>
                                                                <select name="unitID" id="edit-unit" class="form-select" required>
                                                                    <c:forEach var="u" items="${unitList}">
                                                                        <option value="${u.unitID}">${u.description}</option>
                                                                    </c:forEach>
                                                                </select>
                                                            </div>
                                                            <div class="mb-3 col-md-6">
                                                                <label class="form-label">Ảnh</label>
                                                                <input class="form-control" type="file" name="image" />
                                                                <img id="preview-edit-image" src="#" style="max-height: 100px; margin-top: 10px;" />
                                                            </div>
                                                        </div>
                                                        <div class="modal-footer">
                                                            <button type="submit" name="action" value="update" class="btn btn-primary">Cập nhật</button>
                                                        </div>
                                                    </div>
                                                </form>
                                            </div>
                                        </div>
                                        <!-- Modal Xóa Duy Nhất -->
                                        <div class="modal fade" id="deleteModal" tabindex="-1" aria-hidden="true">
                                            <div class="modal-dialog">
                                                <form method="post" action="ListProductServlet">
                                                    <div class="modal-content">
                                                        <div class="modal-header">
                                                            <h5 class="modal-title">Xác nhận xóa</h5>
                                                            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                                                        </div>
                                                        <div class="modal-body">
                                                            Bạn có chắc chắn muốn xóa sản phẩm "<strong id="delete-name"></strong>"?
                                                            <input type="hidden" name="productID" id="delete-id" />
                                                        </div>
                                                        <div class="modal-footer">
                                                            <button type="submit" name="action" value="delete" class="btn btn-danger">Xóa</button>
                                                        </div>
                                                    </div>
                                                </form>
                                            </div>
                                        </div>

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
        <script>
            document.getElementById('image').addEventListener('change', function (event) {
                const [file] = event.target.files;
                if (file) {
                    const preview = document.getElementById('previewImage');
                    preview.src = URL.createObjectURL(file);
                    preview.style.display = 'block';
                }
            });
            function openAddModal() {
                document.getElementById('productForm').reset();
                document.getElementById('action').value = 'add';
                new bootstrap.Modal(document.getElementById('productModal')).show();
            }
        </script>
        <script src="assets/vendor/libs/jquery/jquery.js"></script>
        <script src="assets/vendor/libs/popper/popper.js"></script>
        <script src="assets/vendor/js/bootstrap.js"></script>
        <script src="assets/vendor/libs/perfect-scrollbar/perfect-scrollbar.js"></script>
        <script src="assets/vendor/js/menu.js"></script>
        <script src="assets/js/main.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
        <script>
                document.addEventListener('DOMContentLoaded', function () {
                    const editModalEl = document.getElementById('editModal');
                    const deleteModalEl = document.getElementById('deleteModal');

                    // Listen for opening the Edit modal
                    editModalEl.addEventListener('show.bs.modal', function (event) {
                        const button = event.relatedTarget;
                        if (!button)
                            return;

                        document.getElementById('edit-id').value = button.getAttribute('data-id');
                        document.getElementById('edit-name').value = button.getAttribute('data-name');
                        document.getElementById('edit-import').value = button.getAttribute('data-import');
                        document.getElementById('edit-sell').value = button.getAttribute('data-sell');
                        document.getElementById('edit-category').value = button.getAttribute('data-category');
                        document.getElementById('edit-unit').value = button.getAttribute('data-unit');

                        const image = button.getAttribute('data-image');
                        if (image) {
                            document.getElementById('preview-edit-image').src = 'images/' + image;
                            document.getElementById('preview-edit-image').style.display = 'block';
                        } else {
                            document.getElementById('preview-edit-image').style.display = 'none';
                        }
                    });

                    // Listen for opening the Delete modal
                    deleteModalEl.addEventListener('show.bs.modal', function (event) {
                        const button = event.relatedTarget;
                        if (!button)
                            return;

                        document.getElementById('delete-id').value = button.getAttribute('data-id');
                        document.getElementById('delete-name').innerText = button.getAttribute('data-name');
                    });
                });

        </script>

    </body>
</html>
