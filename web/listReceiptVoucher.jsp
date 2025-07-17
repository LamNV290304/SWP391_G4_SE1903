<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

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
                            <h4 class="fw-bold py-3 mb-4"><span class="text-muted fw-light">Tài chính /</span> Phiếu thu</h4>

                            <!-- Bảng danh sách phiếu thu -->
                            <div class="card">
                                <div class="card-body">
                                    <div class="table-responsive text-nowrap" style="max-height: 500px; overflow-y: auto;">
                                        <form method="get" action="ReceiptVoucherServlet" class="row gy-3 gx-4 align-items-end mb-4">
                                            <div class="col-md-2">
                                                <label class="form-label">Cửa hàng</label>
                                                <select name="shopID" class="form-select">
                                                    <option value="">Tất cả</option>
                                                    <c:forEach var="s" items="${shops}">
                                                        <option value="${s.shopID}" 
                                                                <c:if test="${paramShopID != null && paramShopID == s.shopID}">selected</c:if>>
                                                            ${s.shopName}
                                                        </option>
                                                    </c:forEach>
                                                </select>
                                            </div>

                                            <div class="col-md-2">
                                                <label class="form-label">Nhân viên</label>
                                                <select name="employeeID" class="form-select">
                                                    <option value="">Tất cả</option>
                                                    <c:forEach var="e" items="${employees}">
                                                        <option value="${e.id}"
                                                                <c:if test="${paramEmployeeID != null && paramEmployeeID == e.id}">selected</c:if>>
                                                            ${e.fullname}
                                                        </option>
                                                    </c:forEach>
                                                </select>
                                            </div>

                                            <div class="col-md-2">
                                                <label class="form-label">Loại phiếu</label>
                                                <select name="typeID" class="form-select">
                                                    <option value="">Tất cả</option>
                                                    <c:forEach var="t" items="${types}">
                                                        <option value="${t.typeID}" 
                                                                <c:if test="${paramTypeID != null && paramTypeID == t.typeID}">selected</c:if>>
                                                            ${t.typeName}
                                                        </option>
                                                    </c:forEach>
                                                </select>
                                            </div>

                                            <div class="col-md-2">
                                                <label class="form-label">Phương thức thanh toán</label>
                                                <select name="paymentMethodID" class="form-select">
                                                    <option value="">Tất cả</option>
                                                    <c:forEach var="pm" items="${paymentMethods}">
                                                        <option value="${pm.paymentMethodID}" 
                                                                <c:if test="${paramPaymentMethodID != null && paramPaymentMethodID == pm.paymentMethodID}">selected</c:if>>
                                                            ${pm.methodName}
                                                        </option>
                                                    </c:forEach>
                                                </select>
                                            </div>

                                            <div class="col-md-2">
                                                <label class="form-label">Số tiền từ</label>
                                                <input type="number" step="0.01" name="minAmount" class="form-control" placeholder="VND"
                                                       value="${paramMinAmount != null ? paramMinAmount : ''}" />
                                            </div>

                                            <div class="col-md-2">
                                                <label class="form-label">Đến số tiền</label>
                                                <input type="number" step="0.01" name="maxAmount" class="form-control" placeholder="VND"
                                                       value="${paramMaxAmount != null ? paramMaxAmount : ''}" />
                                            </div>

                                            <div class="col-md-2">
                                                <label class="form-label">Từ ngày</label>
                                                <input type="date" name="fromDate" class="form-control"
                                                       value="${paramFromDate != null ? paramFromDate : ''}" />
                                            </div>

                                            <div class="col-md-2">
                                                <label class="form-label">Đến ngày</label>
                                                <input type="date" name="toDate" class="form-control"
                                                       value="${paramToDate != null ? paramToDate : ''}" />
                                            </div>

                                            <div class="col-md-2 text-end">
                                                <button type="submit" class="btn btn-primary">
                                                    <i class="bx bx-filter-alt me-1"></i> Lọc kết quả
                                                </button>
                                            </div>
                                        </form>
                                        <div class="col-md-2 text-end">
                                            <form method="post" action="AddReceiptPayment" >
                                                <button type="submit" class="btn btn-primary">
                                                    <i class="bx bx-filter-alt me-1"></i> Thêm mới phiếu thu
                                                </button>
                                            </form>
                                        </div>

                                        <div class="table-responsive text-nowrap" style="max-height: 500px; overflow-y: auto;">
                                            <table class="table table-bordered table-fixed-header">
                                                <thead>
                                                    <tr class="text-nowrap">
                                                        <th>Mã phiếu</th>
                                                        <th>Ngày thu</th>
                                                        <th>Cửa hàng</th>
                                                        <th>Nhân viên</th>
                                                        <th>Khách hàng</th>
                                                        <th>Số tiền</th>
                                                        <th>Ghi chú</th>
                                                        <th>Trạng thái</th>
                                                        <th>Loại</th>
                                                        <th>PT thanh toán</th>
                                                        <th>Hành Động</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <c:forEach var="rv" items="${receiptVouchers}">
                                                        <tr>
                                                            <td>${rv.receiptVoucherID}</td>
                                                            <td><fmt:formatDate value="${rv.receiptDate}" pattern="dd/MM/yyyy"/></td>
                                                            <td><c:forEach var="s" items="${shops}">
                                                                    <c:if test="${s.shopID == rv.shopID}">
                                                                        ${s.shopName}
                                                                    </c:if>
                                                                </c:forEach></td>
                                                            <td>
                                                                <c:forEach var="emp" items="${employees}">
                                                                    <c:if test="${emp.id == rv.employeeID}">
                                                                        ${emp.fullname}
                                                                    </c:if>
                                                                </c:forEach></td>
                                                            <td>
                                                                <c:choose>
                                                                    <c:when test="${rv.customerID != null}">
                                                                        <c:forEach var="c" items="${customers}">
                                                                            <c:if test="${c.customerID == rv.customerID}">
                                                                                ${c.customerName}
                                                                            </c:if>
                                                                        </c:forEach>
                                                                    </c:when>
                                                                    <c:otherwise>—</c:otherwise>
                                                                </c:choose></td>
                                                            <td><fmt:formatNumber value="${rv.amount}" type="currency" currencySymbol="₫"/></td>
                                                            <td>${rv.note}</td>
                                                            <td>
                                                                <c:choose>
                                                                    <c:when test="${rv.status}"><span class="badge bg-success">Đã thu</span></c:when>
                                                                    <c:otherwise><span class="badge bg-danger">Hủy</span></c:otherwise>
                                                                </c:choose>
                                                            </td>
                                                            <td>
                                                                <c:forEach var="t" items="${types}">
                                                                    <c:if test="${t.typeID == rv.typeID}">
                                                                        ${t.typeName}
                                                                    </c:if>
                                                                </c:forEach></td>
                                                            <td><c:forEach var="pm" items="${paymentMethods}">
                                                                    <c:if test="${pm.paymentMethodID == rv.paymentMethodID}">
                                                                        ${pm.methodName}
                                                                    </c:if>
                                                                </c:forEach></td>
                                                            <td>
                                                                <button type="button" class="btn btn-sm btn-warning edit-btn"
                                                                        data-bs-toggle="modal" data-bs-target="#editModal"
                                                                        data-id="${rv.receiptVoucherID}"
                                                                        data-shop="${rv.shopID}"
                                                                        data-employee="${rv.employeeID}"
                                                                        data-customer="${rv.customerID}"
                                                                        data-date="${rv.receiptDate.time}"
                                                                        data-amount="${rv.amount}"
                                                                        data-note="${rv.note}"
                                                                        data-status="${rv.status}"
                                                                        data-type="${rv.typeID}"
                                                                        data-method="${rv.paymentMethodID}">
                                                                    Sửa
                                                                </button>
                                                                <!-- Nút Xóa -->
                                                                <form method="post" action="ReceiptVoucherServlet" style="display:inline;"  onsubmit="return confirm('Bạn có chắc muốn xóa phiếu thu này?');">
                                                                    <input type="hidden" name="action" value="delete"/>
                                                                    <input type="hidden" name="receiptVoucherID" value="${rv.receiptVoucherID}"/>
                                                                    <button type="submit" class="btn btn-sm btn-danger" title="Xóa">Xóa
                                                                    </button>
                                                                </form>
                                                            </td>

                                                        </tr>
                                                    </c:forEach>
                                                </tbody>
                                            </table>
                                            <div class="modal fade" id="editModal" tabindex="-1" aria-labelledby="editModalLabel" aria-hidden="true">
                                                <div class="modal-dialog modal-lg">
                                                    <form method="post" action="ReceiptVoucherServlet">
                                                        <div class="modal-content">
                                                            <div class="modal-header">
                                                                <h5 class="modal-title">Cập nhật phiếu thu</h5>
                                                                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Đóng"></button>
                                                            </div>
                                                            <div class="modal-body row g-3">
                                                                <input type="hidden" name="receiptVoucherID" id="edit-id"/>

                                                                <div class="col-md-6">
                                                                    <label class="form-label">Cửa hàng</label>
                                                                    <select name="shopID" id="edit-shop" class="form-select">
                                                                        <c:forEach var="s" items="${shops}">
                                                                            <option value="${s.shopID}">${s.shopName}</option>
                                                                        </c:forEach>
                                                                    </select>
                                                                </div>

                                                                <div class="col-md-6">
                                                                    <label class="form-label">Nhân viên</label>
                                                                    <select name="employeeID" id="edit-employee" class="form-select">
                                                                        <c:forEach var="e" items="${employees}">
                                                                            <option value="${e.id}">${e.fullname}</option>
                                                                        </c:forEach>
                                                                    </select>
                                                                </div>

                                                                <div class="col-md-6">
                                                                    <label class="form-label">Khách hàng</label>
                                                                    <select name="customerID" id="edit-customer" class="form-select">
                                                                        <option value="">—</option>
                                                                        <c:forEach var="c" items="${customers}">
                                                                            <option value="${c.customerID}">${c.customerName}</option>
                                                                        </c:forEach>
                                                                    </select>
                                                                </div>

                                                                <div class="col-md-6">
                                                                    <label class="form-label">Ngày thu</label>
                                                                    <input type="date" name="receiptDate" id="edit-date" class="form-control"/>
                                                                </div>

                                                                <div class="col-md-6">
                                                                    <label class="form-label">Số tiền</label>
                                                                    <input type="number" step="0.01" name="amount" id="edit-amount" class="form-control"/>
                                                                </div>

                                                                <div class="col-md-6">
                                                                    <label class="form-label">Ghi chú</label>
                                                                    <input type="text" name="note" id="edit-note" class="form-control"/>
                                                                </div>

                                                                <div class="col-md-6">
                                                                    <label class="form-label">Trạng thái</label>
                                                                    <select name="status" id="edit-status" class="form-select">
                                                                        <option value="true">Đã thu</option>
                                                                        <option value="false">Hủy</option>
                                                                    </select>
                                                                </div>

                                                                <div class="col-md-6">
                                                                    <label class="form-label">Loại phiếu</label>
                                                                    <select name="typeID" id="edit-type" class="form-select">
                                                                        <c:forEach var="t" items="${types}">
                                                                            <option value="${t.typeID}">${t.typeName}</option>
                                                                        </c:forEach>
                                                                    </select>
                                                                </div>

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
                                                                <button type="submit" class="btn btn-primary" name="action" value="update">Lưu thay đổi</button>
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
                        <jsp:include page="footer.jsp" />
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
                    document.getElementById('edit-customer').value = btn.dataset.customer || '';
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

        <script src="assets/vendor/libs/jquery/jquery.js"></script>
        <script src="assets/vendor/libs/popper/popper.js"></script>
        <script src="assets/vendor/js/bootstrap.js"></script>
        <script src="assets/vendor/libs/perfect-scrollbar/perfect-scrollbar.js"></script>
        <script src="assets/vendor/js/menu.js"></script> <script src="assets/js/main.js"></script> </body>
</html>