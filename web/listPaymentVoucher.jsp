<%-- 
    Document   : listPaymentVoucher
    Created on : Jul 3, 2025, 9:53:15 AM
    Author     : Thai Anh
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<c:if test="${empty sessionScope.Employee}">
    <c:redirect url="loginEmployee.jsp"/>
</c:if>
<html lang="vi" class="light-style layout-menu-fixed" dir="ltr" data-theme="theme-default">
    <head>
        <title>Danh sách phiếu chi</title>
        <meta charset="UTF-8">
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
        <link rel="stylesheet" href="assets/css/custom.css" />
        <link rel="stylesheet" href="assets/vendor/css/core.css" />
    </head>
    <body>
        <div class="layout-wrapper layout-content-navbar">
            <div class="layout-container">
                <jsp:include page="sidebar.jsp"/>
                <div class="layout-page">
                    <jsp:include page="navBar.jsp"/>
                    <div class="content-wrapper">
                        <div class="container-xxl flex-grow-1 container-p-y">
                            <h4 class="fw-bold py-3 mb-4"><span class="text-muted fw-light">Tài chính /</span> Phiếu chi</h4>

                            <div class="card">
                                <div class="card-body">
                                    <form method="get" action="PaymentVoucherServlet" class="row gy-3 gx-4 align-items-end mb-4">
                                        <div class="col-md-2">
                                            <label class="form-label">Cửa hàng</label>
                                            <select name="shopID" class="form-select">
                                                <option value="">Tất cả</option>
                                                <c:forEach var="s" items="${shops}">
                                                    <option value="${s.shopID}" <c:if test="${paramShopID == s.shopID}">selected</c:if>>${s.shopName}</option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                        <div class="col-md-2">
                                            <label class="form-label">Nhân viên</label>
                                            <select name="employeeID" class="form-select">
                                                <option value="">Tất cả</option>
                                                <c:forEach var="e" items="${employees}">
                                                    <option value="${e.id}" <c:if test="${paramEmployeeID == e.id}">selected</c:if>>${e.fullname}</option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                        <div class="col-md-2">
                                            <label class="form-label">Loại phiếu</label>
                                            <select name="typeID" class="form-select">
                                                <option value="">Tất cả</option>
                                                <c:forEach var="t" items="${types}">
                                                    <option value="${t.typeID}" <c:if test="${paramTypeID == t.typeID}">selected</c:if>>${t.typeName}</option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                        <div class="col-md-2">
                                            <label class="form-label">PT Thanh toán</label>
                                            <select name="paymentMethodID" class="form-select">
                                                <option value="">Tất cả</option>
                                                <c:forEach var="pm" items="${paymentMethods}">
                                                    <option value="${pm.paymentMethodID}" <c:if test="${paramPaymentMethodID == pm.paymentMethodID}">selected</c:if>>${pm.methodName}</option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                        <div class="col-md-2">
                                            <label class="form-label">Số tiền từ</label>
                                            <input type="number" step="0.01" name="minAmount" class="form-control" value="${paramMinAmount}" />
                                        </div>
                                        <div class="col-md-2">
                                            <label class="form-label">Đến số tiền</label>
                                            <input type="number" step="0.01" name="maxAmount" class="form-control" value="${paramMaxAmount}" />
                                        </div>
                                        <div class="col-md-2">
                                            <label class="form-label">Từ ngày</label>
                                            <input type="date" name="fromDate" class="form-control" value="${paramFromDate}" />
                                        </div>
                                        <div class="col-md-2">
                                            <label class="form-label">Đến ngày</label>
                                            <input type="date" name="toDate" class="form-control" value="${paramToDate}" />
                                        </div>
                                        <div class="col-md-2 text-end">
                                            <button type="submit" class="btn btn-primary">
                                                <i class="bx bx-filter-alt me-1"></i> Lọc kết quả
                                            </button>
                                        </div>
                                    </form>
                                    <div class="col-md-2 text-end">
                                        <form method="post" action="AddPaymentVoucherServlet" >
                                            <button type="submit" class="btn btn-outline-primary"> Thêm mới phiếu chi
                                            </button>
                                        </form>
                                    </div>
                                        <form action="PaymentVoucherServlet" method="post" enctype="multipart/form-data">
    <input type="file" name="excelFile" accept=".xls,.xlsx" required />
    <input type="hidden" name="action" value="importExcel" />
    <button type="submit">Nhập Excel</button>
</form>

                                    <div class="table-responsive text-nowrap mt-3">
                                        <table class="table table-bordered">
                                            <thead>
                                                <tr>
                                                    <th>Mã phiếu</th>
                                                    <th>Ngày chi</th>
                                                    <th>Cửa hàng</th>
                                                    <th>Nhân viên</th>
                                                    <th>Nhà cung cấp</th>
                                                    <th>Số tiền</th>
                                                    <th>Ghi chú</th>
                                                    <th>Trạng thái</th>
                                                    <th>Loại</th>
                                                    <th>PT thanh toán</th>
                                                    <th>Hành động</th>

                                                </tr>
                                            </thead>
                                            <tbody>
                                                <c:forEach var="pv" items="${paymentVouchers}">
                                                    <tr>
                                                        <td>${pv.paymentVoucherID}</td>
                                                        <td><fmt:formatDate value="${pv.paymentDate}" pattern="dd/MM/yyyy"/></td>
                                                        <td>
                                                            <c:forEach var="s" items="${shops}">
                                                                <c:if test="${s.shopID == pv.shopID}">${s.shopName}</c:if>
                                                            </c:forEach>
                                                        </td>
                                                        <td>
                                                            <c:forEach var="e" items="${employees}">
                                                                <c:if test="${e.id == pv.employeeID}">${e.fullname}</c:if>
                                                            </c:forEach>
                                                        </td>
                                                        <td>
                                                            <c:choose>
                                                                <c:when test="${pv.supplierID != null}">
                                                                    <c:forEach var="sup" items="${suppliers}">
                                                                        <c:if test="${sup.supplierID == pv.supplierID}">${sup.supplierName}</c:if>
                                                                    </c:forEach>
                                                                </c:when>
                                                                <c:otherwise>—</c:otherwise>
                                                            </c:choose>
                                                        </td>
                                                        <td><fmt:formatNumber value="${pv.amount}" pattern="#,##0₫" /></td>
                                                        <td>${pv.note}</td>
                                                        <td>
                                                            <c:choose>
                                                                <c:when test="${pv.status}"><span class="badge bg-success">Đã chi</span></c:when>
                                                                <c:otherwise><span class="badge bg-danger">Hủy</span></c:otherwise>
                                                            </c:choose>
                                                        </td>
                                                        <td>
                                                            <c:forEach var="t" items="${types}">
                                                                <c:if test="${t.typeID == pv.typeID}">${t.typeName}</c:if>
                                                            </c:forEach>
                                                        </td>
                                                        <td>
                                                            <c:forEach var="pm" items="${paymentMethods}">
                                                                <c:if test="${pm.paymentMethodID == pv.paymentMethodID}">${pm.methodName}</c:if>
                                                            </c:forEach>
                                                        </td>
                                                        <td>
                                                            <button type="button" class="btn btn-sm btn-warning edit-btn" 
                                                                    data-bs-toggle="modal" data-bs-target="#editModal"
                                                                    data-id="${pv.paymentVoucherID}"
                                                                    data-shop="${pv.shopID}"
                                                                    data-employee="${pv.employeeID}"
                                                                    data-supplier="${pv.supplierID}"
                                                                    data-date="${pv.paymentDate.time}"
                                                                    data-amount="${pv.amount}"
                                                                    data-note="${pv.note}"
                                                                    data-status="${pv.status}"
                                                                    data-type="${pv.typeID}"
                                                                    data-method="${pv.paymentMethodID}">
                                                                Sửa
                                                            </button>
                                                            <form method="post" action="PaymentVoucherServlet" style="display:inline;" 
                                                                  onsubmit="return confirm('Bạn có chắc muốn xoá phiếu chi này?');">
                                                                <input type="hidden" name="action" value="delete" />
                                                                <input type="hidden" name="paymentVoucherID" value="${pv.paymentVoucherID}" />
                                                                <button type="submit" class="btn btn-sm btn-danger">Xoá</button>
                                                            </form>
                                                        </td>

                                                    </tr>
                                                </c:forEach>
                                            </tbody>
                                             <tfoot>
        <tr>
            <td colspan="5" class="text-end fw-bold">Tổng cộng:</td>
            <td class="fw-bold text-danger">
                <fmt:formatNumber value="${totalCost}" pattern="#,##0₫"/>
            </td>
            <td colspan="5"></td>
        </tr>
    </tfoot>
                                        </table>

                                        <div class="modal fade" id="editModal" tabindex="-1" aria-labelledby="editModalLabel" aria-hidden="true">
                                            <div class="modal-dialog modal-lg">
                                                <form method="post" action="PaymentVoucherServlet">
                                                    <input type="hidden" name="action" value="update" />
                                                    <div class="modal-content">
                                                        <div class="modal-header">
                                                            <h5 class="modal-title">Cập nhật phiếu chi</h5>
                                                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Đóng"></button>
                                                        </div>
                                                        <div class="modal-body row g-3">
                                                            <input type="hidden" name="paymentVoucherID" id="edit-id"/>

                                                            <!-- Shop -->
                                                            <div class="col-md-6">
                                                                <label class="form-label">Cửa hàng</label>
                                                                <select name="shopID" id="edit-shop" class="form-select">
                                                                    <c:forEach var="s" items="${shops}">
                                                                        <option value="${s.shopID}">${s.shopName}</option>
                                                                    </c:forEach>
                                                                </select>
                                                            </div>

                                                            <!-- Employee -->
                                                            <div class="col-md-6">
                                                                <label class="form-label">Nhân viên</label>
                                                                <select name="employeeID" id="edit-employee" class="form-select">
                                                                    <c:forEach var="e" items="${employees}">
                                                                        <option value="${e.id}">${e.fullname}</option>
                                                                    </c:forEach>
                                                                </select>
                                                            </div>

                                                            <!-- Supplier -->
                                                            <div class="col-md-6">
                                                                <label class="form-label">Nhà cung cấp</label>
                                                                <select name="supplierID" id="edit-supplier" class="form-select">
                                                                    <option value="">—</option>
                                                                    <c:forEach var="sup" items="${suppliers}">
                                                                        <option value="${sup.supplierID}">${sup.supplierName}</option>
                                                                    </c:forEach>
                                                                </select>
                                                            </div>

                                                            <!-- Payment Date -->
                                                            <div class="col-md-6">
                                                                <label class="form-label">Ngày chi</label>
                                                                <input type="date" name="paymentDate" id="edit-date" class="form-control"/>
                                                            </div>

                                                            <!-- Amount -->
                                                            <div class="col-md-6">
                                                                <label class="form-label">Số tiền</label>
                                                                <input type="number" step="0.01" name="amount" id="edit-amount" class="form-control"/>
                                                            </div>

                                                            <!-- Note -->
                                                            <div class="col-md-6">
                                                                <label class="form-label">Ghi chú</label>
                                                                <input type="text" name="note" id="edit-note" class="form-control"/>
                                                            </div>

                                                            <!-- Status -->
                                                            <div class="col-md-6">
                                                                <label class="form-label">Trạng thái</label>
                                                                <select name="status" id="edit-status" class="form-select">
                                                                    <option value="true">Đã chi</option>
                                                                    <option value="false">Hủy</option>
                                                                </select>
                                                            </div>

                                                            <!-- Type -->
                                                            <div class="col-md-6">
                                                                <label class="form-label">Loại phiếu</label>
                                                                <select name="typeID" id="edit-type" class="form-select">
                                                                    <c:forEach var="t" items="${types}">
                                                                        <option value="${t.typeID}">${t.typeName}</option>
                                                                    </c:forEach>
                                                                </select>
                                                            </div>

                                                            <!-- Payment Method -->
                                                            <div class="col-md-6">
                                                                <label class="form-label">PT Thanh toán</label>
                                                                <select name="paymentMethodID" id="edit-method" class="form-select">
                                                                    <c:forEach var="pm" items="${paymentMethods}">
                                                                        <option value="${pm.paymentMethodID}">${pm.methodName}</option>
                                                                    </c:forEach>
                                                                </select>
                                                            </div>
                                                        </div>
                                                        <div class="modal-footer">
                                                            <button type="submit" class="btn btn-primary">Lưu thay đổi</button>
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
                        <jsp:include page="footer.jsp"/>
                    </div>
                </div>
            </div>
        </div>
        <script>
            document.querySelectorAll('.edit-btn').forEach(btn => {
                btn.addEventListener('click', () => {
                    document.getElementById('edit-id').value = btn.dataset.id;
                    document.getElementById('edit-shop').value = btn.dataset.shop;
                    document.getElementById('edit-employee').value = btn.dataset.employee;
                    document.getElementById('edit-supplier').value = btn.dataset.supplier || '';

                    const date = new Date(parseInt(btn.dataset.date));
                    document.getElementById('edit-date').value = date.toISOString().substring(0, 10);

                    document.getElementById('edit-amount').value = btn.dataset.amount;
                    document.getElementById('edit-note').value = btn.dataset.note;
                    document.getElementById('edit-status').value = btn.dataset.status;
                    document.getElementById('edit-type').value = btn.dataset.type;
                    document.getElementById('edit-method').value = btn.dataset.method;
                });
            });
        </script>

        <!-- Luôn đặt các script ở cuối <body> theo thứ tự sau -->
        <script src="assets/vendor/libs/jquery/jquery.js"></script>
        <script src="assets/vendor/libs/popper/popper.js"></script>
        <script src="assets/vendor/js/bootstrap.js"></script>
        <script src="assets/vendor/libs/perfect-scrollbar/perfect-scrollbar.js"></script>

        <script src="assets/vendor/js/menu.js"></script> <!-- ✅ Phải đặt trước -->
        <script src="assets/js/main.js"></script>       <!-- ✅ Đặt sau -->

    </body>
</html>
