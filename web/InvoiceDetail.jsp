<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html
    lang="vi"
    class="light-style layout-menu-fixed"
    dir="ltr"
    data-theme="theme-default"
    data-assets-path="./assets/" <%-- Đảm bảo đường dẫn này đúng với cấu hình của bạn --%>
    data-template="vertical-menu-template-free"
    >
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0"/>
        <title>Chi Tiết Hóa Đơn #${requestScope.selectedInvoice.invoiceID} - SaleShape</title>

        <meta name="description" content="" />

        <link rel="icon" type="image/x-icon" href="img/logoSale.png" /> <%-- Sử dụng icon của bạn --%>

        <link rel="preconnect" href="https://fonts.googleapis.com" />
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
        <link
            href="https://fonts.googleapis.com/css2?family=Public+Sans:ital,wght@0,300;0,400;0,500;0,600;0,700;1,300;1,400;1,500;1,600;1,700&display=swap"
            rel="stylesheet"
            />
        <link href="https://fonts.googleapis.com/css2?family=Roboto&display=swap&subset=vietnamese" rel="stylesheet">


        <link rel="stylesheet" href="./assets/vendor/fonts/boxicons.css" />

        <link rel="stylesheet" href="./assets/vendor/css/core.css" class="template-customizer-core-css" />
        <link rel="stylesheet" href="./assets/vendor/css/theme-default.css" class="template-customizer-theme-css" />
        <link rel="stylesheet" href="./assets/css/demo.css" />
        <link rel="stylesheet" href="./assets/css/custom.css" /> <%-- custom.css của bạn --%>


        <link rel="stylesheet" href="./assets/vendor/libs/perfect-scrollbar/perfect-scrollbar.css" />
        <link rel="stylesheet" href="./assets/vendor/libs/apex-charts/apex-charts.css" /> <%-- Nếu cần, thêm các CSS của ApexCharts --%>

        <style>
            /* Print styles - Vẫn giữ lại để điều chỉnh giao diện khi in */
            @media print {
                .layout-menu, /* Ẩn sidebar */
                .layout-navbar, /* Ẩn navbar */
                .layout-footer, /* Ẩn footer */
                .buy-now, /* Ẩn nút mua template nếu có */
                .customizer-toggler, /* Ẩn nút customizer */
                .layout-overlay,
                .drag-target,
                .action-buttons /* Ẩn các nút hành động trên hóa đơn */ {
                    display: none !important;
                }

                .content-wrapper {
                    padding: 0 !important; /* Loại bỏ padding trên content wrapper */
                }

                .container-xxl {
                    padding: 0 !important; /* Loại bỏ padding trên container chính */
                    max-width: 100% !important; /* Cho phép hóa đơn chiếm toàn bộ chiều rộng khi in */
                }

                .card { /* Sử dụng .card thay cho .invoice-card */
                    box-shadow: none !important;
                    border: none !important;
                    margin: 0 !important;
                    padding: 0 !important;
                    max-width: 100% !important; /* Full width khi in */
                }
                .invoice-header, .invoice-title {
                    margin-bottom: 10px !important; /* Giảm margin khi in */
                }
                .invoice-info-section {
                    margin-bottom: 8px !important; /* Giảm margin khi in */
                    font-size: 9pt !important;
                }
                .table th, .table td {
                    font-size: 9pt !important;
                }
                .total-amount-footer {
                    font-size: 12pt !important;
                    margin-top: 10px !important;
                    padding-top: 5px !important;
                }
            }
        </style>

        <script src="./assets/vendor/js/helpers.js"></script>

        <script src="./assets/js/config.js"></script>
    </head>

    <body>
        <div class="layout-wrapper layout-content-navbar">
            <div class="layout-container">
                <%-- Sidebar --%>
                <jsp:include page="sidebar.jsp" />

                <div class="layout-page">
                    <%-- Navbar --%>
                    <jsp:include page="navBar.jsp" />

                    <div class="content-wrapper">
                        <div class="container-xxl flex-grow-1 container-p-y">
                            <%-- Main content card --%>
                            <div class="card p-4 p-md-5 my-4 mx-auto" style="max-width: 900px;">
                                <%-- Phần thông tin cửa hàng - nằm giữa trên đỉnh bảng hóa đơn --%>
                                <div class="invoice-header text-center mb-5 pb-3 border-bottom">
                                    <h2 class="mb-2 text-heading fs-3">
                                        <c:choose>
                                            <c:when test="${not empty selectedShop && not empty selectedShop.shopName}">
                                                ${selectedShop.shopName}
                                            </c:when>
                                            <c:otherwise>
                                                Tên Cửa Hàng Của Bạn
                                            </c:otherwise>
                                        </c:choose>
                                    </h2>
                                    <p class="mb-0 text-muted fs-7">
                                        Địa chỉ:
                                        <c:choose>
                                            <c:when test="${not empty selectedShop && not empty selectedShop.address}">
                                                ${selectedShop.address}
                                            </c:when>
                                            <c:otherwise>
                                                Số X, Đường Y, Phường Z, Quận K, TP. Hồ Chí Minh
                                            </c:otherwise>
                                        </c:choose>
                                    </p>
                                    <p class="mb-0 text-muted fs-7">
                                        Điện thoại:
                                        <c:choose>
                                            <c:when test="${not empty selectedShop && not empty selectedShop.phone}">
                                                ${selectedShop.phone}
                                            </c:when>
                                            <c:otherwise>
                                                09xx.xxx.xxx
                                            </c:otherwise>
                                        </c:choose>
                                        | Email:
                                        <c:choose>
                                            <c:when test="${not empty selectedShop && not empty selectedShop.email}">
                                                ${selectedShop.email}
                                            </c:when>
                                            <c:otherwise>
                                                info@cuahang.com
                                            </c:otherwise>
                                        </c:choose>
                                    </p>
                                </div>

                                <div class="invoice-title text-center mb-5">
                                    <h1 class="fs-1 text-primary d-inline-block pb-2 border-bottom border-primary">Hóa Đơn Bán Hàng</h1>
                                </div>

                                <%-- Display Messages --%>
                                <c:if test="${not empty requestScope.successMessage}">
                                    <div class="alert alert-success alert-dismissible fade show mt-4" role="alert">
                                        ${requestScope.successMessage}
                                        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                                    </div>
                                </c:if>
                                <c:if test="${not empty requestScope.errorMessage}">
                                    <div class="alert alert-danger alert-dismissible fade show mt-4" role="alert">
                                        ${requestScope.errorMessage}
                                        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                                    </div>
                                </c:if>

                                <c:set var="invoice" value="${requestScope.selectedInvoice}"/>
                                <c:set var="customer" value="${requestScope.selectedCustomer}"/>
                                <c:set var="employees" value="${requestScope.employees}"/>
                                <c:set var="selectedEmployee" value="${requestScope.selectedEmployee}"/>
                                <c:set var="shop" value="${requestScope.selectedShop}"/>
                                <c:set var="invoiceDetails" value="${requestScope.invoiceDetails}"/>
                                <c:set var="products" value="${requestScope.products}"/>

                                <c:choose>
                                    <c:when test="${selectedInvoice == null}">
                                        <div class="text-center">
                                            <div class="alert alert-danger" role="alert">
                                                <h3 class="alert-heading">Lỗi: Không tìm thấy thông tin hóa đơn này!</h3>
                                                <p>Có vẻ như hóa đơn bạn đang cố gắng xem không tồn tại hoặc đã bị xóa.</p>
                                                <p class="mb-0">Vui lòng quay lại danh sách hóa đơn và chọn một hóa đơn khác.</p>
                                            </div>
                                            <a href="InvoiceServlet?action=list" class="btn btn-secondary mt-3 no-print">Quay lại danh sách Hóa đơn</a>
                                        </div>
                                    </c:when>
                                    <c:otherwise>
                                        <%-- Thông tin hóa đơn và khách hàng/nhân viên được chia thành 2 cột --%>
                                        <div class="invoice-info-section d-flex justify-content-between flex-wrap mb-4 fs-6">
                                            <div class="flex-grow-1 p-0 pe-md-4 text-start">
                                                <p class="mb-1"><strong>Mã Hóa đơn:</strong> #${invoice.invoiceID}</p>
                                                <p class="mb-1"><strong>Ngày lập:</strong> <fmt:formatDate value="${invoice.invoiceDate}" pattern="dd/MM/yyyy HH:mm:ss"/></p>
                                                <p class="mb-1">
                                                    <strong>Nhân viên:</strong>
                                                    <c:choose>
                                                        <c:when test="${not empty selectedEmployee}">
                                                            ${selectedEmployee.fullname}
                                                        </c:when>
                                                        <c:otherwise>
                                                            <c:set var="employeeName" value="N/A"/>
                                                            <c:forEach var="emp" items="${employees}">
                                                                <c:if test="${emp.id == invoice.employeeID}">
                                                                    <c:set var="employeeName" value="${emp.fullname}"/>
                                                                </c:if>
                                                            </c:forEach>
                                                            ${employeeName} <span class="text-muted fst-italic">(ID: ${invoice.employeeID})</span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </p>
                                            </div>
                                            <div class="flex-grow-1 p-0 ps-md-4 text-end">
                                                <p class="mb-1">
                                                    <strong>Khách hàng:</strong>
                                                    <c:choose>
                                                        <c:when test="${not empty customer}">
                                                            ${customer.customerName}
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span class="text-muted fst-italic">Khách hàng không xác định / Khách lẻ</span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </p>
                                                <c:if test="${not empty customer}">
                                                    <p class="mb-1"><strong>Điện thoại KH:</strong> ${customer.phone}</p>
                                                    <c:if test="${not empty customer.email}"><p class="mb-1"><strong>Email KH:</strong> ${customer.email}</p></c:if>
                                                </c:if>
                                            
                                            </div>
                                        </div>
                                        <div class="table-responsive">
                                            <table class="table table-bordered">
                                                <thead>
                                                    <tr>
                                                        <th class="text-nowrap">STT</th>
                                                        <th class="text-nowrap">Sản phẩm</th>
                                                        <th class="text-nowrap">Đơn giá</th>
                                                        <th class="text-nowrap">Số lượng</th>
                                                        <th class="text-nowrap">Giảm Giá (%)</th>
                                                        <th class="text-nowrap">Thành tiền</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <c:set var="grandTotal" value="0"/>
                                                    <c:if test="${empty invoiceDetails}">
                                                        <tr>
                                                            <td colspan="6" class="text-center text-muted">Chưa có sản phẩm nào trong hóa đơn này.</td>
                                                        </tr>
                                                    </c:if>
                                                    <c:forEach var="detail" items="${invoiceDetails}" varStatus="loop">
                                                        <tr>
                                                            <td>${loop.index + 1}</td>
                                                            <td>
                                                                <c:set var="productNameDisplay" value="Không tìm thấy sản phẩm (${detail.productID})"/>
                                                                <c:forEach var="prod" items="${products}">
                                                                    <c:if test="${prod.productID == detail.productID}">
                                                                        <c:set var="productNameDisplay" value="${prod.productName}"/>
                                                                    </c:if>
                                                                </c:forEach>
                                                                ${productNameDisplay}
                                                            </td>
                                                            <td><fmt:formatNumber value="${detail.unitPrice}" type="currency" currencySymbol="đ" maxFractionDigits="0"/></td>
                                                            <td>${detail.quantity}</td>
                                                            <td><fmt:formatNumber value="${detail.discount}" type="number" maxFractionDigits="2"/></td>
                                                            <td><fmt:formatNumber value="${detail.totalPrice}" type="currency" currencySymbol="đ" maxFractionDigits="0"/></td>
                                                        </tr>
                                                        <c:set var="grandTotal" value="${grandTotal + detail.totalPrice}"/>
                                                    </c:forEach>
                                                </tbody>
                                                <tfoot>
                                                    <tr>
                                                        <td colspan="5" class="text-end pt-3 border-top border-dashed"><strong>Tổng tiền:</strong></td>
                                                        <td class="pt-3 border-top border-dashed"><strong><fmt:formatNumber value="${invoice.totalAmount}" type="currency" currencySymbol="đ" maxFractionDigits="0"/></strong></td>
                                                    </tr>
                                                    <c:if test="${not empty invoice.note}">
                                                        <tr>
                                                            <td colspan="6" class="text-start pt-2"><strong>Ghi chú:</strong> ${invoice.note}</td>
                                                        </tr>
                                                    </c:if>
                                                </tfoot>
                                            </table>
                                        </div>

                                        <div class="action-buttons no-print text-center mt-5">
                                            <a href="InvoiceServlet?action=list" class="btn btn-label-secondary m-1">
                                                <i class='bx bx-arrow-back me-1'></i> Quay lại danh sách Hóa đơn
                                            </a>
                                            <a href="#" onclick="window.print(); return false;" class="btn btn-label-primary m-1">
                                                <i class='bx bx-printer me-1'></i> In Hóa Đơn
                                            </a>

                                            <c:if test="${not empty customer && not empty customer.email}">
                                                <a href="InvoiceServlet?action=sendInvoiceEmail&invoiceID=${invoice.invoiceID}" class="btn btn-label-success m-1"
                                                   onclick="return confirm('Bạn có chắc chắn muốn gửi hóa đơn này qua email không?');">
                                                    <i class='bx bx-mail-send me-1'></i> Gửi Email Hóa đơn
                                                </a>
                                            </c:if>

                                            <c:if test="${!invoice.status}">
                                                <a href="InvoiceServlet?action=manageInvoiceDetails&invoiceID=${invoice.invoiceID}" class="btn btn-label-warning m-1">
                                                    <i class='bx bx-edit-alt me-1'></i> Quản lý chi tiết Hóa đơn
                                                </a>
                                            </c:if>
                                        </div>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                        <jsp:include page="footer.jsp" /> <%-- Footer --%>
                    </div>
                </div>
            </div>
            <div class="layout-overlay layout-menu-toggle"></div>
        </div>
        <script src="./assets/vendor/libs/jquery/jquery.js"></script>
        <script src="./assets/vendor/libs/popper/popper.js"></script>
        <script src="./assets/vendor/js/bootstrap.js"></script>
        <script src="./assets/vendor/libs/perfect-scrollbar/perfect-scrollbar.js"></script>

        <script src="./assets/vendor/js/menu.js"></script>
        <script src="./assets/vendor/libs/apex-charts/apexcharts.js"></script> <%-- Nếu cần, thêm các JS của ApexCharts --%>

        <script src="./assets/js/main.js"></script>

        <script src="./assets/js/dashboards-analytics.js"></script> <%-- Nếu cần, thêm các JS cho dashboard/analytics --%>

        <script async defer src="https://buttons.github.io/buttons.js"></script>
    </body>
</html>