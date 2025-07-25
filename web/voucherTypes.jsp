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
                            <h4 class="fw-bold py-3 mb-4">Hệ thống / Loại phiếu thu & chi</h4>

                            <div class="row">
                                <!-- Bảng loại phiếu thu -->
                                <div class="col-md-6">
                                    <div class="card">
                                        <div class="card-header"><h5>Loại phiếu thu
                                                <button type="button" class="btn btn-outline-primary" data-bs-toggle="modal" data-bs-target="#addReceiptModal">Thêm loại thu mới</button>
                                            </h5></div>
                                        <!-- Modal Thêm Loại Phiếu Thu -->
                                        <div class="modal fade" id="addReceiptModal" tabindex="-1" aria-labelledby="addReceiptLabel" aria-hidden="true">
                                            <div class="modal-dialog">
                                                <div class="modal-content">
                                                    <form action="TypeVoucherServlet" method="post">
                                                        <div class="modal-header">
                                                            <h5 class="modal-title" id="addReceiptLabel">Thêm Loại Phiếu Thu</h5>
                                                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Đóng"></button>
                                                        </div>
                                                        <div class="modal-body">
                                                            <input type="hidden" name="action" value="addReceipt" />
                                                            <div class="mb-3">
                                                                <label class="form-label">Tên loại phiếu thu</label>
                                                                <input type="text" name="nameTypeReceipt" class="form-control" required />
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

                                        <div class="card-body">
                                            <table class="table table-bordered">
                                                <thead>
                                                    <tr>
                                                        <th>Mã loại</th>
                                                        <th>Tên loại phiếu</th>
                                                        <th>Hành động</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <c:forEach var="r" items="${typeReceiptList}">
                                                        <tr>
                                                            <td>${r.typeID}</td>
                                                            <td>${r.typeName}</td>
                                                            <td>
                                                                <form action="TypeVoucherServlet" method="post" style="display:inline-block">
                                                                    <input type="hidden" name="action" value="editReceipt" />
                                                                    <input type="hidden" name="id" value="${r.typeID}" />
                                                                    <!-- Nút Sửa -->
                                                                    <button type="button" class="btn btn-sm btn-outline-primary" data-bs-toggle="modal" data-bs-target="#editReceiptModal${r.typeID}">
                                                                        <i class="bx bx-edit"></i>
                                                                    </button>
                                                                </form>
                                                                <!-- Modal Sửa Phiếu Thu -->
                                                                <div class="modal fade" id="editReceiptModal${r.typeID}" tabindex="-1" aria-labelledby="editReceiptLabel${r.typeID}" aria-hidden="true">
                                                                    <div class="modal-dialog">
                                                                        <div class="modal-content">
                                                                            <form action="TypeVoucherServlet" method="post">
                                                                                <div class="modal-header">
                                                                                    <h5 class="modal-title" id="editReceiptLabel${r.typeID}">Sửa Loại Phiếu Thu</h5>
                                                                                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Đóng"></button>
                                                                                </div>
                                                                                <div class="modal-body">
                                                                                    <input type="hidden" name="action" value="editReceipt" />
                                                                                    <input type="hidden" name="id" value="${r.typeID}" />
                                                                                    <div class="mb-3">
                                                                                        <label for="nameTypeReceipt" class="form-label">Tên loại phiếu</label>
                                                                                        <input type="text" class="form-control" name="nameTypeReceipt" value="${r.typeName}" required />
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

                                                                <form action="TypeVoucherServlet" method="post" style="display:inline-block" onsubmit="return confirm('Xóa loại phiếu thu này?')">
                                                                    <input type="hidden" name="action" value="deleteReceipt" />
                                                                    <input type="hidden" name="id" value="${r.typeID}" />
                                                                    <button class="btn btn-sm btn-outline-danger"><i class="bx bx-trash"></i></button>
                                                                </form>
                                                            </td>
                                                        </tr>
                                                    </c:forEach>
                                                </tbody>
                                            </table>

                                        </div>
                                    </div>
                                </div>

                                <!-- Bảng loại phiếu chi -->
                                <div class="col-md-6">
                                    <div class="card">
                                        <div class="card-header"><h5>Loại phiếu chi
                                                <button type="button" class="btn btn-outline-primary" data-bs-toggle="modal" data-bs-target="#addPaymentModal">Thêm loại chi mới</button>
                                            </h5></div>
                                        <!-- Modal Thêm Loại Phiếu Chi -->
                                        <div class="modal fade" id="addPaymentModal" tabindex="-1" aria-labelledby="addPaymentLabel" aria-hidden="true">
                                            <div class="modal-dialog">
                                                <div class="modal-content">
                                                    <form action="TypeVoucherServlet" method="post">
                                                        <div class="modal-header">
                                                            <h5 class="modal-title" id="addPaymentLabel">Thêm Loại Phiếu Chi</h5>
                                                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Đóng"></button>
                                                        </div>
                                                        <div class="modal-body">
                                                            <input type="hidden" name="action" value="addPayment" />
                                                            <div class="mb-3">
                                                                <label class="form-label">Tên loại phiếu chi</label>
                                                                <input type="text" name="nameTypePayment" class="form-control" required />
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

                                        <div class="card-body">
                                            <table class="table table-bordered">
                                                <thead>
                                                    <tr>
                                                        <th>Mã loại</th>
                                                        <th>Tên loại phiếu</th>
                                                        <th>Hành động</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <c:forEach var="p" items="${typePaymentList}">
                                                        <tr>
                                                            <td>${p.typeID}</td>
                                                            <td>${p.typeName}</td>
                                                            <td>
                                                                <form action="TypeVoucherServlet" method="post" style="display:inline-block">
                                                                    <input type="hidden" name="action" value="editPayment" />
                                                                    <input type="hidden" name="id" value="${p.typeID}" />
                                                                    <button type="button" class="btn btn-sm btn-outline-primary" data-bs-toggle="modal" data-bs-target="#editPaymentModal${p.typeID}">
                                                                        <i class="bx bx-edit"></i>
                                                                    </button>
                                                                </form>
                                                                <!-- Modal Sửa Phiếu Chi -->
                                                                <div class="modal fade" id="editPaymentModal${p.typeID}" tabindex="-1" aria-labelledby="editPaymentLabel${p.typeID}" aria-hidden="true">
                                                                    <div class="modal-dialog">
                                                                        <div class="modal-content">
                                                                            <form action="TypeVoucherServlet" method="post">
                                                                                <div class="modal-header">
                                                                                    <h5 class="modal-title" id="editPaymentLabel${p.typeID}">Sửa Loại Phiếu Chi</h5>
                                                                                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Đóng"></button>
                                                                                </div>
                                                                                <div class="modal-body">
                                                                                    <input type="hidden" name="action" value="editPayment" />
                                                                                    <input type="hidden" name="id" value="${p.typeID}" />
                                                                                    <div class="mb-3">
                                                                                        <label for="nameTypePayment" class="form-label">Tên loại phiếu</label>
                                                                                        <input type="text" class="form-control" name="nameTypePayment" value="${p.typeName}" required />
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

                                                                <form action="TypeVoucherServlet" method="post" style="display:inline-block" onsubmit="return confirm('Xóa loại phiếu chi này?')">
                                                                    <input type="hidden" name="action" value="deletePayment" />
                                                                    <input type="hidden" name="id" value="${p.typeID}" />
                                                                    <button class="btn btn-sm btn-outline-danger"><i class="bx bx-trash"></i></button>
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
                        </div>
                    </div>
                    <jsp:include page="footer.jsp" />
                </div>

            </div>
        </div>


        <script src="assets/vendor/libs/jquery/jquery.js"></script>
        <script src="assets/vendor/libs/popper/popper.js"></script>
        <script src="assets/vendor/js/bootstrap.js"></script>
        <script src="assets/vendor/libs/perfect-scrollbar/perfect-scrollbar.js"></script>
        <script src="assets/vendor/js/menu.js"></script> <script src="assets/js/main.js"></script> </body>
</html>