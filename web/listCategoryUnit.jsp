<%-- 
    Document   : listCategoryUnit
    Created on : Jul 16, 2025, 12:08:11 PM
    Author     : Thai Anh
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html>
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
                            <h4 class="fw-bold py-3 mb-4">Hệ thống / Loại Sản phẩm</h4>
                            <!-- Tương tự như phần khung đã có -->
                            <div class="row">
                                <!-- Bảng danh mục sản phẩm -->
                                <div class="col-md-6">
                                    <div class="card">
                                        <div class="card-header">
                                            <h5>Danh mục sản phẩm
                                                <button class="btn btn-outline-primary" data-bs-toggle="modal" data-bs-target="#addCategoryModal">Thêm mới</button>
                                            </h5>
                                        </div>
                                        <div class="card-body">
                                            <table class="table table-bordered">
                                                <thead>
                                                    <tr><th>Mã</th><th>Tên danh mục</th><th>Mô tả</th><th>Trạng thái</th></tr>
                                                </thead>
                                                <tbody>
                                                    <c:forEach var="c" items="${categoryList}">
                                                        <tr>
                                                            <td>${c.categoryID}</td>
                                                            <td>${c.categoryName}</td>
                                                            <td>${c.description}</td>
                                                            <td>
                                                                <c:choose>
                                                                    <c:when test="${c.status}"><span class="badge bg-success">Hiển thị</span></c:when>
                                                                    <c:otherwise><span class="badge bg-secondary">Ẩn</span></c:otherwise>
                                                                </c:choose>
                                                            </td>
                                                            <td>
                                                                <button type="button" class="btn btn-sm btn-warning"
                                                                        onclick="openEditCategoryModal('${c.categoryID}', '${fn:escapeXml(c.categoryName)}', '${fn:escapeXml(c.description)}')">
                                                                    Sửa
                                                                </button>
                                                                <button type="submit" class="btn btn-sm btn-danger"
                                                                        onclick="return confirm('Bạn có chắc muốn xóa danh mục này?');"
                                                                        form="deleteCategoryForm${c.categoryID}">
                                                                    Xóa
                                                                </button>
                                                                <form id="deleteCategoryForm${c.categoryID}" method="post" style="display: none;">
                                                                    <input type="hidden" name="action" value="deleteCategory" />
                                                                    <input type="hidden" name="categoryID" value="${c.categoryID}" />
                                                                </form>

                                                            </td>


                                                        </tr>
                                                    </c:forEach>
                                                </tbody>
                                            </table>
                                            <!-- Modal thêm danh mục sản phẩm -->
                                            <div class="modal fade" id="addCategoryModal" tabindex="-1" aria-labelledby="addCategoryModalLabel" aria-hidden="true">
                                                <div class="modal-dialog">
                                                    <form method="post" action="ListCategoryUnitServlet">
                                                        <div class="modal-content">
                                                            <div class="modal-header">
                                                                <h5 class="modal-title" id="addCategoryModalLabel">Thêm danh mục sản phẩm</h5>
                                                                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Đóng"></button>
                                                            </div>
                                                            <input type="hidden" name="action" value="addCategory" />
                                                            <div class="modal-body">
                                                                <div class="mb-3">
                                                                    <label for="categoryName" class="form-label">Tên danh mục</label>
                                                                    <input type="text" class="form-control" id="categoryName" name="categoryName" required>
                                                                </div>
                                                                <div class="mb-3">
                                                                    <label for="categoryDescription" class="form-label">Mô tả</label>
                                                                    <textarea class="form-control" id="categoryDescription" name="description" rows="3"></textarea>
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
                                            <!-- Modal sửa Danh mục -->
                                            <div class="modal fade" id="editCategoryModal" tabindex="-1" aria-hidden="true">
                                                <div class="modal-dialog">
                                                    <form method="post">
                                                        <input type="hidden" name="action" value="updateCategory">
                                                        <input type="hidden" id="editCategoryID" name="categoryID">
                                                        <div class="modal-content">
                                                            <div class="modal-header">
                                                                <h5 class="modal-title">Sửa Danh mục</h5>
                                                                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                                                            </div>
                                                            <div class="modal-body">
                                                                <div class="mb-3">
                                                                    <label for="editCategoryName" class="form-label">Tên danh mục</label>
                                                                    <input type="text" class="form-control" id="editCategoryName" name="categoryName" required>
                                                                </div>
                                                                <div class="mb-3">
                                                                    <label for="editCategoryDesc" class="form-label">Mô tả</label>
                                                                    <textarea class="form-control" id="editCategoryDesc" name="description"></textarea>
                                                                </div>
                                                            </div>
                                                            <div class="modal-footer">
                                                                <button type="submit" class="btn btn-primary">Cập nhật</button>
                                                                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                                                            </div>
                                                        </div>
                                                    </form>
                                                </div>
                                            </div>


                                        </div>
                                    </div>
                                </div>

                                <!-- Bảng đơn vị tính -->
                                <div class="col-md-6">
                                    <div class="card">
                                        <div class="card-header">
                                            <h5>Đơn vị tính
                                                <button class="btn btn-outline-primary" data-bs-toggle="modal" data-bs-target="#addUnitModal">Thêm mới</button>

                                            </h5>
                                        </div>
                                        <div class="card-body">
                                            <table class="table table-bordered">
                                                <thead>
                                                    <tr><th>Mã</th><th>Đơn vị</th><th>Trạng thái</th></tr>
                                                </thead>
                                                <tbody>
                                                    <c:forEach var="u" items="${unitList}">
                                                        <tr>
                                                            <td>${u.unitID}</td>
                                                            <td>${u.description}</td>
                                                            <td>
                                                                <c:choose>
                                                                    <c:when test="${u.status == 1}"><span class="badge bg-success">Hiển thị</span></c:when>
                                                                    <c:otherwise><span class="badge bg-secondary">Ẩn</span></c:otherwise>
                                                                </c:choose>

                                                            </td>
                                                            <td>
                                                                <button type="button" class="btn btn-sm btn-warning"
                                                                        onclick="openEditUnitModal('${u.unitID}', '${fn:escapeXml(u.description)}')">
                                                                    Sửa
                                                                </button>
                                                                <button type="submit" class="btn btn-sm btn-danger"
                                                                        onclick="return confirm('Bạn có chắc muốn xóa đơn vị này?');"
                                                                        form="deleteUnitForm${u.unitID}">
                                                                    Xóa
                                                                </button>

                                                                <form id="deleteUnitForm${u.unitID}" method="post" style="display: none;">
                                                                    <input type="hidden" name="action" value="deleteUnit" />
                                                                    <input type="hidden" name="unitID" value="${u.unitID}" />
                                                                </form>
                                                            </td>
                                                        </tr>
                                                    </c:forEach>
                                                </tbody>
                                            </table>
                                            <!-- Modal thêm đơn vị tính -->
                                            <div class="modal fade" id="addUnitModal" tabindex="-1" aria-labelledby="addUnitModalLabel" aria-hidden="true">
                                                <div class="modal-dialog">
                                                    <form method="post" action="ListCategoryUnitServlet">
                                                        <div class="modal-content">
                                                            <div class="modal-header">
                                                                <input type="hidden" name="action" value="addUnit" />
                                                                <h5 class="modal-title" id="addUnitModalLabel">Thêm đơn vị tính</h5>
                                                                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Đóng"></button>
                                                            </div>
                                                            <div class="modal-body">
                                                                <div class="mb-3">
                                                                    <label for="unitDescription" class="form-label">Tên đơn vị</label>
                                                                    <input type="text" class="form-control" id="unitDescription" name="description" required>
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
                                            <!-- Modal sửa Đơn vị tính -->
                                            <div class="modal fade" id="editUnitModal" tabindex="-1" aria-hidden="true">
                                                <div class="modal-dialog">
                                                    <form method="post">
                                                        <input type="hidden" name="action" value="updateUnit">
                                                        <input type="hidden" id="editUnitID" name="unitID">
                                                        <div class="modal-content">
                                                            <div class="modal-header">
                                                                <h5 class="modal-title">Sửa Đơn vị tính</h5>
                                                                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                                                            </div>
                                                            <div class="modal-body">
                                                                <div class="mb-3">
                                                                    <label for="editUnitDesc" class="form-label">Tên đơn vị</label>
                                                                    <input type="text" class="form-control" id="editUnitDesc" name="description" required>
                                                                </div>
                                                            </div>
                                                            <div class="modal-footer">
                                                                <button type="submit" class="btn btn-primary">Cập nhật</button>
                                                                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                                                            </div>
                                                        </div>
                                                    </form>
                                                </div>
                                            </div>

                                        </div>
                                    </div>
                                </div>
                            </div>

                        </div>
                    </div>
                    <jsp:include page="footer.jsp" />
                </div>

            </div>
        </div>

        <script>
            function openEditCategoryModal(id, name, desc) {
                document.getElementById('editCategoryID').value = id;
                document.getElementById('editCategoryName').value = name;
                document.getElementById('editCategoryDesc').value = desc;
                new bootstrap.Modal(document.getElementById('editCategoryModal')).show();
            }

            function openEditUnitModal(id, desc) {
                document.getElementById('editUnitID').value = id;
                document.getElementById('editUnitDesc').value = desc;
                new bootstrap.Modal(document.getElementById('editUnitModal')).show();
            }
        </script>
        <script src="assets/vendor/libs/jquery/jquery.js"></script>
        <script src="assets/vendor/libs/popper/popper.js"></script>
        <script src="assets/vendor/js/bootstrap.js"></script>
        <script src="assets/vendor/libs/perfect-scrollbar/perfect-scrollbar.js"></script>
        <script src="assets/vendor/js/menu.js"></script> <script src="assets/js/main.js"></script> </body>
</html>
