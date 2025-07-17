<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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
      <link rel="icon" type="image/x-icon" href="img/logoSale.png" />
        <!-- Fonts -->
        <link rel="preconnect" href="https://fonts.googleapis.com" />
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
        <link
            href="https://fonts.googleapis.com/css2?family=Public+Sans:ital,wght@0,300;0,400;0,500;0,600;0,700;1,300;1,400;1,500;1,600;1,700&display=swap"
            rel="stylesheet"
            />

        <!-- Icons. Uncomment required icon fonts -->
        <link rel="stylesheet" href="./assets/css/custom.css" />

        <link rel="stylesheet" href="./assets/vendor/fonts/boxicons.css" />

        <!-- Core CSS -->
        <link rel="stylesheet" href="./assets/vendor/css/core.css" class="template-customizer-core-css" />
        <link rel="stylesheet" href="./assets/vendor/css/theme-default.css" class="template-customizer-theme-css" />
        <link rel="stylesheet" href="./assets/css/demo.css" />

        <!-- Vendors CSS -->
        <link rel="stylesheet" href="./assets/vendor/libs/perfect-scrollbar/perfect-scrollbar.css" />

        <link rel="stylesheet" href="./assets/vendor/libs/apex-charts/apex-charts.css" />

        <!-- Page CSS -->

        <!-- Helpers -->
        <script src="./assets/vendor/js/helpers.js"></script>

        <!--! Template customizer & Theme config files MUST be included after core stylesheets and helpers.js in the <head> section -->
        <!--? Config:  Mandatory theme config file contain global vars & default theme options, Set your preferred theme option in this file.  -->
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
                            <h4 class="fw-bold py-3 mb-4">Thêm Phiếu Chi</h4>

                            <c:if test="${not empty successMessage}">
                                <div class="alert alert-success" role="alert">${successMessage}</div>
                            </c:if>

                            <form action="AddPaymentVoucherServlet" method="post">
                                <div class="row">

                                    <!-- Cửa hàng -->
                                    <div class="mb-3 col-md-6">
                                        <label class="form-label">Cửa hàng</label>
                                        <select name="shopID" class="form-select" required>
                                            <c:forEach var="s" items="${listShop}">
                                                <option value="${s.shopID}">${s.shopName}</option>
                                            </c:forEach>
                                        </select>
                                    </div>

                                    <!-- Nhân viên -->
                                    <div class="mb-3 col-md-6">
                                        <label class="form-label">Nhân viên</label>
                                        <select name="employeeID" class="form-select" required>
                                            <c:forEach var="e" items="${listEmp}">
                                                <option value="${e.id}">${e.fullname}</option>
                                            </c:forEach>
                                        </select>
                                    </div>

                                    <!-- Nhà cung cấp -->
                                    <div class="mb-3 col-md-6">
                                        <label class="form-label">Nhà cung cấp (nếu có)</label>
                                        <select name="supplierID" class="form-select">
                                            <option value="">-- Không chọn --</option>
                                            <c:forEach var="sup" items="${listSupplier}">
                                                <option value="${sup.supplierID}">${sup.supplierName}</option>
                                            </c:forEach>
                                        </select>
                                    </div>

                                    <!-- Ngày chi -->
                                    <div class="mb-3 col-md-6">
                                        <label class="form-label">Ngày chi</label>
                                        <input type="datetime-local" name="paymentDate" class="form-control" required />
                                    </div>

                                    <!-- Số tiền -->
                                    <div class="mb-3 col-md-6">
                                        <label class="form-label">Số tiền</label>
                                        <input type="number" name="amount" step="0.01" class="form-control" required />
                                    </div>

                                    <!-- Loại phiếu chi -->
                                    <div class="mb-3 col-md-6">
                                        <label class="form-label">Loại phiếu</label>
                                        <select name="typeID" class="form-select" required>
                                            <c:forEach var="type" items="${listType}">
                                                <option value="${type.typeID}">${type.typeName}</option>
                                            </c:forEach>
                                        </select>
                                    </div>

                                    <!-- Phương thức thanh toán -->
                                    <div class="mb-3 col-md-6">
                                        <label class="form-label">Phương thức thanh toán</label>
                                        <select name="paymentMethodID" class="form-select" required>
                                            <c:forEach var="pm" items="${listPayment}">
                                                <option value="${pm.paymentMethodID}">${pm.methodName}</option>
                                            </c:forEach>
                                        </select>
                                    </div>

                                    <!-- Ghi chú -->
                                    <div class="mb-3 col-md-12">
                                        <label class="form-label">Ghi chú</label>
                                        <textarea name="note" class="form-control" rows="3" placeholder="Nhập nội dung chi..."></textarea>
                                    </div>

                                    <!-- Trạng thái -->
                                    <div class="mb-3 col-md-6">
                                        <label class="form-label">Trạng thái</label>
                                        <select name="status" class="form-select">
                                            <option value="true">Kích hoạt</option>
                                            <option value="false">Ẩn</option>
                                        </select>
                                    </div>

                                </div>

                                <button type="submit" class="btn btn-primary">Lưu phiếu chi</button>
                                <a href="PaymentVoucherServlet" class="btn btn-outline-secondary">Hủy</a>
                            </form>
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
            <script src="assets/vendor/js/menu.js"></script> <!-- Xử lý toggle -->
            <script src="assets/js/main.js"></script> 
</html>