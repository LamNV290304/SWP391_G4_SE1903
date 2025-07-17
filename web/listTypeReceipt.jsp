<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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
                            <h4 class="fw-bold py-3 mb-4"><span class="text-muted fw-light">Hệ thống /</span> Loại phiếu xuất</h4>
                            <div class="card">
                                <div class="row">
                                    <!-- Bảng loại phiếu xuất -->
                                    <div class="col-md-6">
                                        <div class="card">
                                            <div class="card-header">
                                                <h5>Loại phiếu xuất
                                                    <button type="button" class="btn btn-outline-primary" data-bs-toggle="modal" data-bs-target="#addFormModal">Thêm loại xuất mới</button>
                                                </h5>
                                                <div class="modal fade" id="addFormModal" tabindex="-1" aria-labelledby="addFormLabel" aria-hidden="true">
                                                    <div class="modal-dialog modal-lg"> <!-- modal-lg nếu bạn muốn rộng -->
                                                        <div class="modal-content">
                                                            <div class="modal-header">
                                                                <h5 class="modal-title" id="addFormLabel">Thêm Loại Phiếu Xuất</h5>
                                                                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Đóng"></button>
                                                            </div>
                                                            <div class="modal-body">
                                                                <form action="TypeReceiptServlet" method="post">
                                                                    <div class="mb-3">
                                                                        <input type="hidden" name="action" value="addExportType" class="form-control"/>
                                                                        <label class="form-label">Tên loại phiếu xuất mới</label>
                                                                        <input type="text" name="nameTypeEx" class="form-control" required />
                                                                    </div>

                                                                    <!-- Thêm các trường khác tại đây -->
                                                                    <div class="text-end">
                                                                        <button type="submit" class="btn btn-success">Lưu</button>
                                                                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                                                                    </div>
                                                                </form>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>

                                            </div>
                                            <div class="card-body">
                                                <div class="table-responsive text-nowrap">
                                                    <table class="table table-bordered">
                                                        <thead>
                                                            <tr>
                                                                <th>Mã loại</th>
                                                                <th>Tên loại phiếu xuất</th>
                                                                <th>Hành động</th>
                                                            </tr>
                                                        </thead>
                                                        <tbody>
                                                            <c:forEach var="type" items="${typeExportList}">
                                                                <tr>
                                                                    <td>${type.typeID}</td>
                                                                    <td>${type.typeName}</td>
                                                                    <td class="d-flex">
                                                                        <!-- Form Sửa -->
                                                                        <form method="post" action="TypeReceiptServlet" class="me-1">
                                                                            <input type="hidden" name="action" value="editExport">
                                                                            <input type="hidden" name="id" value="${type.typeID}">
                                                                            <!-- Nút sửa -->
                                                                            <button type="button"
                                                                                    class="btn btn-sm btn-outline-primary"
                                                                                    onclick="editExportType(${type.typeID}, '${type.typeName}')"
                                                                                    data-bs-toggle="modal" data-bs-target="#editExportModal">
                                                                                <i class="bx bx-edit"></i>
                                                                            </button>
                                                                        </form>
                                                                        <!-- Modal sửa -->
                                                                        <div class="modal fade" id="editExportModal" tabindex="-1" aria-labelledby="editExportLabel" aria-hidden="true">
                                                                            <div class="modal-dialog modal-lg">
                                                                                <div class="modal-content">
                                                                                    <form action="TypeReceiptServlet" method="post">
                                                                                        <div class="modal-header">
                                                                                            <h5 class="modal-title" id="editExportLabel">Chỉnh sửa loại phiếu xuất</h5>
                                                                                            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                                                                                        </div>
                                                                                        <div class="modal-body">
                                                                                            <input type="hidden" name="action" value="editExport" />
                                                                                            <input type="hidden" name="id" id="editExportId" />
                                                                                            <label class="form-label">Tên loại:</label>
                                                                                            <input type="text" name="nameTypeEx" class="form-control" id="editExportName" required />
                                                                                        </div>
                                                                                        <div class="modal-footer">
                                                                                            <button type="submit" class="btn btn-success">Lưu</button>
                                                                                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                                                                                        </div>
                                                                                    </form>
                                                                                </div>
                                                                            </div>
                                                                        </div>
                                                                        <!-- Form Xóa -->
                                                                        <form method="post" action="TypeReceiptServlet" onsubmit="return confirm('Bạn có chắc chắn muốn xóa loại phiếu xuất này?');">
                                                                            <input type="hidden" name="action" value="deleteExport">
                                                                            <input type="hidden" name="id" value="${type.typeID}">
                                                                            <button type="submit" class="btn btn-sm btn-outline-danger">
                                                                                <i class="bx bx-trash"></i>
                                                                            </button>
                                                                        </form>
                                                                    </td>
                                                                </tr>
                                                            </c:forEach>
                                                        </tbody>
                                                    </table>
                                                </div>
                                            </div>
                                        </div>
                                    </div>

                                    <!-- Bảng loại phiếu nhập -->
                                    <div class="col-md-6">
                                        <div class="card">
                                            <div class="card-header">
                                                <h5>Loại phiếu nhập
                                                    <button type="button" class="btn btn-outline-primary" data-bs-toggle="modal" data-bs-target="#addFormModalImport">Thêm loại nhập mới</button>
                                                </h5>
                                                <div class="modal fade" id="addFormModalImport" tabindex="-1" aria-labelledby="addFormLabel" aria-hidden="true">
                                                    <div class="modal-dialog modal-lg"> <!-- modal-lg nếu bạn muốn rộng -->
                                                        <div class="modal-content">
                                                            <div class="modal-header">
                                                                <h5 class="modal-title" id="addFormLabel">Thêm Loại Phiếu Nhập</h5>
                                                                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Đóng"></button>
                                                            </div>
                                                            <div class="modal-body">
                                                                <form action="TypeReceiptServlet" method="post">
                                                                    <div class="mb-3">
                                                                        <input type="hidden" name="action" value="addImportType" class="form-control"/>
                                                                        <label class="form-label">Tên loại phiếu nhập mới</label>
                                                                        <input type="text" name="nameTypeIm" class="form-control" required />
                                                                    </div>

                                                                    <!-- Thêm các trường khác tại đây -->
                                                                    <div class="text-end">
                                                                        <button type="submit" class="btn btn-success">Lưu</button>
                                                                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                                                                    </div>
                                                                </form>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="card-body">
                                                <div class="table-responsive text-nowrap">
                                                    <table class="table table-bordered">
                                                        <thead>
                                                            <tr>
                                                                <th>Mã loại</th>
                                                                <th>Tên loại phiếu nhập</th>
                                                                <th>Hành động</th>
                                                            </tr>
                                                        </thead>
                                                        <tbody>
                                                            <c:forEach var="type" items="${typeImportList}">
                                                                <tr>
                                                                    <td>${type.typeID}</td>
                                                                    <td>${type.typeName}</td>
                                                                    <td class="d-flex">
                                                                        <!-- Form Sửa -->
                                                                        <form  action="TypeReceiptServlet" method="POST" class="me-1">
                                                                            <input type="hidden" name="action" value="editImport">
                                                                            <input type="hidden" name="id" value="${type.typeID}">
                                                                            <button type="button"
                                                                                    class="btn btn-sm btn-outline-primary editImportBtn"
                                                                                    data-id="${type.typeID}"
                                                                                    data-name="${type.typeName}">
                                                                                <i class="bx bx-edit"></i>
                                                                            </button>

                                                                        </form>
                                                                        <!-- Modal sửa loại phiếu nhập -->
                                                                        <div class="modal fade" id="editImportModal" tabindex="-1" aria-labelledby="editImportLabel" aria-hidden="true">
                                                                            <div class="modal-dialog modal-lg">
                                                                                <div class="modal-content">
                                                                                    <form action="TypeReceiptServlet" method="post">
                                                                                        <div class="modal-header">
                                                                                            <h5 class="modal-title" id="editImportLabel">Chỉnh sửa loại phiếu nhập</h5>
                                                                                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Đóng"></button>
                                                                                        </div>
                                                                                        <div class="modal-body">
                                                                                            <input type="hidden" name="action" value="editImport" />
                                                                                            <input type="hidden" name="id" id="editImportId" />
                                                                                            <div class="mb-3">
                                                                                                <label class="form-label">Tên loại phiếu nhập</label>
                                                                                                <input type="text" class="form-control" name="nameTypeIm" id="editImportName" required />
                                                                                            </div>
                                                                                        </div>
                                                                                        <div class="modal-footer">
                                                                                            <button type="submit" class="btn btn-success">Cập nhật</button>
                                                                                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                                                                                        </div>
                                                                                    </form>
                                                                                </div>
                                                                            </div>
                                                                        </div>

                                                                        <!-- Form Xóa -->
                                                                        <form method="post" action="TypeReceiptServlet" onsubmit="return confirm('Bạn có chắc chắn muốn xóa loại phiếu nhập này?');">
                                                                            <input type="hidden" name="action" value="deleteImport">
                                                                            <input type="hidden" name="id" value="${type.typeID}">
                                                                            <button type="submit" class="btn btn-sm btn-outline-danger">
                                                                                <i class="bx bx-trash"></i>
                                                                            </button>
                                                                        </form>
                                                                    </td>
                                                                </tr>
                                                            </c:forEach>
                                                        </tbody>
                                                    </table>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <!-- Bảng nhà cung cấp -->
                                    <div class="col-md-12 mt-4">
                                        <div class="card">
                                            <div class="card-header">
                                                <h5>Nhà cung cấp
                                                    <button type="button" class="btn btn-outline-primary" data-bs-toggle="modal" data-bs-target="#addFormSupplier">Thêm nhà cung cấp mới</button>
                                                </h5>
                                                <div class="modal fade" id="addFormSupplier" tabindex="-1" aria-labelledby="addFormLabel" aria-hidden="true">
                                                    <div class="modal-dialog modal-lg"> <!-- modal-lg nếu bạn muốn rộng -->
                                                        <div class="modal-content">
                                                            <div class="modal-header">
                                                                <h5 class="modal-title" id="addFormLabel">Thêm Nhà cung cấp mới</h5>
                                                                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Đóng"></button>
                                                            </div>
                                                            <div class="modal-body">
                                                                <form action="TypeReceiptServlet" method="post">
                                                                    <div class="mb-3">
                                                                        <input type="hidden" name="action" value="addSupplier" class="form-control"/>
                                                                        <label class="form-label">Tên nhà cung cấp mới</label>
                                                                        <input type="text" name="name" class="form-control" required />
                                                                    </div>
                                                                    <div class="mb-3">
                                                                        <label class="form-label">Số điện thoại</label>
                                                                        <input type="text" name="phone" class="form-control" required />
                                                                    </div>
                                                                    <div class="mb-3">
                                                                        <label class="form-label">Mail</label>
                                                                        <input type="text" name="email" class="form-control" required />
                                                                    </div>
                                                                    <div class="mb-3">
                                                                        <label class="form-label">Địa chỉ</label>
                                                                        <input type="text" name="address" class="form-control" required />
                                                                    </div>
                                                                    <div class="mb-3">
                                                                        <label for="receiptId" class="form-label">Ngày tạo</label>
                                                                        <input type="date" id="importDate" name="Date" class="form-control" required />
                                                                    </div>
                                                                    <div class="mb-3">
                                                                        <label class="form-label">createdBy</label>
                                                                        <input type="text" name="createdBy" class="form-control" required />
                                                                    </div>

                                                                    <!-- Thêm các trường khác tại đây -->
                                                                    <div class="text-end">
                                                                        <button type="submit" class="btn btn-success">Lưu</button>
                                                                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                                                                    </div>
                                                                </form>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>

                                            </div>
                                            <div class="card-body">
                                                <div class="table-responsive text-nowrap">
                                                    <table class="table table-bordered">
                                                        <thead>
                                                            <tr>
                                                                <th>Mã</th>
                                                                <th>Tên</th>
                                                                <th>Điện thoại</th>
                                                                <th>Email</th>
                                                                <th>Địa chỉ</th>
                                                                <th>Trạng thái</th>
                                                                <th>Ngày tạo</th>
                                                                <th>Người tạo</th>
                                                                <th>Hành động</th>
                                                            </tr>
                                                        </thead>
                                                        <tbody>
                                                            <c:forEach var="s" items="${supList}">
                                                                <tr>
                                                                    <td>${s.supplierID}</td>
                                                                    <td>${s.supplierName}</td>
                                                                    <td>${s.phone}</td>
                                                                    <td>${s.email}</td>
                                                                    <td>${s.address}</td>
                                                                    <td>
                                                                        <c:choose>
                                                                            <c:when test="${s.status}">Hoạt động</c:when>
                                                                            <c:otherwise>Ngừng</c:otherwise>
                                                                        </c:choose>
                                                                    </td>
                                                                    <td><fmt:formatDate value="${s.createdDate}" pattern="dd/MM/yyyy HH:mm"></fmt:formatDate></td>
                                                            <td>${s.createdBy}</td>
                                                            <td class="d-flex">
                                                                <!-- Nút Sửa -->
                                                                <form action="TypeReceiptServlet" method="post" class="me-1">
                                                                    <input type="hidden" name="action" value="editSupplier">
                                                                    <input type="hidden" name="id" value="${s.supplierID}">
                                                                    <button type="button"
                                                                            class="btn btn-sm btn-outline-primary me-1"
                                                                            onclick="editSupplier(
                                                                                            '${s.supplierID}',
                                                                                            '${fn:escapeXml(s.supplierName)}',
                                                                                            '${s.phone}',
                                                                                            '${s.email}',
                                                                                            '${fn:escapeXml(s.address)}',
                                                                                            '${s.status}')"
                                                                            data-bs-toggle="modal"
                                                                            data-bs-target="#editSupplierModal">
                                                                        <i class="bx bx-edit"></i>
                                                                    </button>

                                                                </form>

                                                                <!-- Nút Xóa -->
                                                                <form action="TypeReceiptServlet" method="post" onsubmit="return confirm('Xóa nhà cung cấp này?');">
                                                                    <input type="hidden" name="action" value="deleteSupplier">
                                                                    <input type="hidden" name="id" value="${s.supplierID}">
                                                                    <button type="submit" class="btn btn-sm btn-outline-danger">
                                                                        <i class="bx bx-trash"></i>
                                                                    </button>
                                                                </form>
                                                            </td>
                                                            </tr>
                                                        </c:forEach>
                                                        </tbody>
                                                    </table>
                                                    <!-- Modal sửa nhà cung cấp -->
                                                    <div class="modal fade" id="editSupplierModal" tabindex="-1" aria-labelledby="editSupplierLabel" aria-hidden="true">
                                                        <div class="modal-dialog modal-lg">
                                                            <div class="modal-content">
                                                                <form action="TypeReceiptServlet" method="post">
                                                                    <div class="modal-header">
                                                                        <h5 class="modal-title" id="editSupplierLabel">Chỉnh sửa nhà cung cấp</h5>
                                                                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                                                                    </div>
                                                                    <div class="modal-body row">
                                                                        <input type="hidden" name="action" value="editSupplier" />
                                                                        <input type="hidden" name="id" id="editSupplierID" />

                                                                        <div class="mb-3 col-md-6">
                                                                            <label class="form-label">Tên nhà cung cấp</label>
                                                                            <input type="text" name="supplierName" id="editSupplierName" class="form-control" required />
                                                                        </div>

                                                                        <div class="mb-3 col-md-6">
                                                                            <label class="form-label">Số điện thoại</label>
                                                                            <input type="text" name="phone" id="editSupplierPhone" class="form-control" required />
                                                                        </div>

                                                                        <div class="mb-3 col-md-6">
                                                                            <label class="form-label">Email</label>
                                                                            <input type="email" name="email" id="editSupplierEmail" class="form-control" />
                                                                        </div>

                                                                        <div class="mb-3 col-md-6">
                                                                            <label class="form-label">Địa chỉ</label>
                                                                            <input type="text" name="address" id="editSupplierAddress" class="form-control" />
                                                                        </div>

                                                                        <div class="mb-3 col-md-6">
                                                                            <label class="form-label">Trạng thái</label>
                                                                            <select name="status" id="editSupplierStatus" class="form-control">
                                                                                <option value="true">Hoạt động</option>
                                                                                <option value="false">Ngừng</option>
                                                                            </select>
                                                                        </div>
                                                                    </div>

                                                                    <div class="modal-footer">
                                                                        <button type="submit" class="btn btn-success">Lưu</button>
                                                                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
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

                            </div>
                        </div>
                        <jsp:include page="footer.jsp"/>
                    </div>
                </div>

            </div>
        </div>

        <script>
            document.addEventListener('DOMContentLoaded', function () {
                const editButtons = document.querySelectorAll('.editImportBtn');
                editButtons.forEach(btn => {
                    btn.addEventListener('click', function () {
                        const id = this.getAttribute('data-id');
                        const name = this.getAttribute('data-name');

                        // Gán vào input của modal
                        document.getElementById('editImportId').value = id;
                        document.getElementById('editImportName').value = name;

                        // Hiển thị modal
                        const editModal = new bootstrap.Modal(document.getElementById('editImportModal'));
                        editModal.show();
                    });
                });
            });
        </script>
        <script>
            function editExportType(id, name) {
                document.getElementById('editExportId').value = id;
                document.getElementById('editExportName').value = name;
            }
        </script>
        <script>
            function editSupplier(id, name, phone, email, address, status) {
                document.getElementById("editSupplierID").value = id;
                document.getElementById("editSupplierName").value = name;
                document.getElementById("editSupplierPhone").value = phone;
                document.getElementById("editSupplierEmail").value = email;
                document.getElementById("editSupplierAddress").value = address;
                document.getElementById("editSupplierStatus").value = status;
            }
        </script>

        <script src="assets/vendor/libs/jquery/jquery.js"></script>
        <script src="assets/vendor/libs/popper/popper.js"></script>
        <script src="assets/vendor/js/bootstrap.js"></script>
        <script src="assets/vendor/libs/perfect-scrollbar/perfect-scrollbar.js"></script>
        <script src="assets/vendor/js/menu.js"></script> <script src="assets/js/main.js"></script> 


    </body>
</html>