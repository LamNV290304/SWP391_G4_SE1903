<%@page contentType="text/html" pageEncoding="UTF-8"%>
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
        <div class="layout-wrapper layout-content-navbar">
            <div class="layout-container">
                <jsp:include page="sidebar.jsp" />
                <div class="layout-page">
                    <jsp:include page="navBar.jsp" />
                    <div class="content-wrapper">
                        <div class="container-xxl flex-grow-1 container-p-y">
                            <h4 class="fw-bold py-3 mb-4">
                                <span class="text-muted fw-light">Hóa Đơn /</span> Chi Tiết Hóa Đơn
                            </h4>

                            <div class="card">
                                <div class="card-body">
                                    <div>
                                        <div class="text-center mb-4"> <c:if test="${not empty selectedShop}">
                                                <h1 class="mb-1">${selectedShop.shopName}</h1>
                                                <p class="text-muted">${selectedShop.address} | Điện thoại: ${selectedShop.phone} | Email: ${selectedShop.email}</p>
                                            </c:if>
                                        </div>

                                        <h2 class="text-center mb-5 text-primary fw-bold">HÓA ĐƠN BÁN HÀNG</h2> <div class="row mb-4">
                                            <div class="col-md-6">
                                                <p><strong>Mã Hóa đơn:</strong> #${selectedInvoice.invoiceID}</p>
                                                <p><strong>Ngày lập:</strong> <fmt:formatDate value="${selectedInvoice.invoiceDate}" pattern="dd/MM/yyyy HH:mm:ss" /></p>
                                                <p><strong>Nhân viên:</strong>
                                                    <c:choose>
                                                        <c:when test="${not empty selectedEmployee}">${selectedEmployee.fullName} (${selectedEmployee.id})</c:when>
                                                        <c:otherwise>${selectedInvoice.employeeID} <span class="text-muted">(Không tìm thấy)</span></c:otherwise>
                                                    </c:choose>
                                                </p>
                                            </div>
                                            <div class="col-md-6 text-end">
                                                <p><strong>Khách hàng:</strong>
                                                    <c:choose>
                                                        <c:when test="${not empty selectedCustomer}">
                                                            ${selectedCustomer.customerName}
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span class="text-muted">Khách hàng không xác định / Khách lẻ</span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </p>
                                                <p><strong>Điện thoại KH:</strong>
                                                    <c:choose>
                                                        <c:when test="${not empty selectedCustomer && not empty selectedCustomer.phone}">
                                                            ${selectedCustomer.phone}
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span class="text-muted">N/A</span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </p>
                                                <p><strong>Email KH:</strong>
                                                    <c:choose>
                                                        <c:when test="${not empty selectedCustomer && not empty selectedCustomer.email}">
                                                            ${selectedCustomer.email}
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span class="text-muted">N/A</span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </p>
                                            </div>
                                        </div>

                                        <div class="table-responsive mb-4"> <table class="table table-bordered"> <thead>
                                                    <tr>
                                                        <th>STT</th>
                                                        <th>Tên sản phẩm</th>
                                                        <th class="text-end">Số lượng</th>
                                                        <th class="text-end">Đơn giá</th>
                                                        <th class="text-end">Giảm giá</th>
                                                        <th class="text-end">Thành tiền</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <c:set var="totalItemsAmount" value="0"/>
                                                    <c:forEach var="detail" items="${invoiceDetails}" varStatus="loop">
                                                        <tr>
                                                            <td>${loop.index + 1}</td>
                                                            <td>
                                                                <c:set var="productName" value="Sản phẩm không xác định"/>
                                                                <c:forEach var="invItem" items="${inventories}">
                                                                    <c:if test="${invItem.product.productID == detail.productID}">
                                                                        <c:set var="productName" value="${invItem.product.productName}"/>
                                                                    </c:if>
                                                                </c:forEach>
                                                                ${productName}
                                                            </td>
                                                            <td class="text-end">${detail.quantity}</td>
                                                            <td class="text-end"><fmt:formatNumber value="${detail.unitPrice}" pattern="#,##0" /> VNĐ</td>
                                                            <td class="text-end">${detail.discount}%</td>
                                                            <td class="text-end"><fmt:formatNumber value="${detail.totalPrice}" pattern="#,##0" /> VNĐ</td>
                                                        </tr>
                                                        <c:set var="totalItemsAmount" value="${totalItemsAmount + detail.totalPrice}"/>
                                                    </c:forEach>
                                                    <c:if test="${empty invoiceDetails}">
                                                        <tr>
                                                            <td colspan="6" class="text-center text-muted">Chưa có mặt hàng nào trong hóa đơn này.</td>
                                                        </tr>
                                                    </c:if>
                                                </tbody>
                                                <tfoot>
                                                    <tr>
                                                        <td colspan="5" class="text-end fs-5 fw-bold pt-3 border-top-2">Tổng cộng:</td> <td class="text-end fs-5 fw-bold pt-3 border-top-2">
                                                            <fmt:formatNumber value="${totalItemsAmount}" pattern="#,##0" /> VNĐ
                                                        </td>
                                                    </tr>
                                                </tfoot>
                                            </table>
                                        </div>

                                        <div class="text-center mt-4">
                                            <p class="text-muted">Cảm ơn quý khách và hẹn gặp lại!</p>
                                        </div>
                                    </div>
                                    <div class="d-flex justify-content-center gap-3 mt-4"> <a href="InvoiceServlet?action=list" class="btn btn-secondary">Quay lại danh sách Hóa đơn</a>
                                        <a href="InvoiceServlet?action=manageInvoiceDetails&invoiceID=${selectedInvoice.invoiceID}" class="btn btn-primary">Quản lý chi tiết Hóa đơn</a>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <jsp:include page="footer.jsp" />
                        <div class="content-backdrop fade"></div>
                    </div>
                    </div>
                </div>

            <div class="layout-overlay layout-menu-toggle"></div>
        </div>
        <script src="assets/vendor/libs/jquery/jquery.js"></script>
        <script src="assets/vendor/libs/popper/popper.js"></script>
        <script src="assets/vendor/js/bootstrap.js"></script>
        <script src="assets/vendor/libs/perfect-scrollbar/perfect-scrollbar.js"></script>
        <script src="assets/vendor/js/menu.js"></script>
        <script src="assets/js/main.js"></script>

        <script async defer src="https://buttons.github.io/buttons.js"></script>
    </body>
</html>