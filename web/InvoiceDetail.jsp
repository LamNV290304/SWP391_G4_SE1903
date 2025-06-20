<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Chi Tiết Hóa Đơn #${requestScope.selectedInvoice.invoiceID}</title>
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
        <style>
            body {
                font-family: Arial, sans-serif;
                background-color: #f4f7f6;
                padding: 20px;
            }
            .container {
                background-color: #ffffff;
                padding: 30px;
                border-radius: 8px;
                box-shadow: 0 4px 8px rgba(0,0,0,0.05);
                max-width: 800px; /* Giới hạn chiều rộng để trông giống tờ hóa đơn hơn */
                margin: 20px auto; /* Căn giữa container */
            }
            .invoice-header {
                text-align: center;
                margin-bottom: 30px;
            }
            .invoice-header h2 {
                margin-bottom: 5px;
                color: #333;
                font-size: 28px;
            }
            .invoice-header p {
                margin: 0;
                color: #555;
                font-size: 14px;
            }
            .invoice-title {
                text-align: center;
                margin-bottom: 30px;
            }
            .invoice-title h1 {
                font-size: 36px;
                color: #007bff; /* Màu sắc nổi bật cho tiêu đề hóa đơn */
                border-bottom: 2px solid #007bff;
                padding-bottom: 10px;
                display: inline-block; /* Để đường gạch chân chỉ rộng bằng chữ */
            }

            .invoice-info-section {
                display: flex;
                justify-content: space-between;
                margin-bottom: 20px;
                font-size: 15px;
            }
            .invoice-info-section div {
                flex: 1;
                padding: 5px 15px;
            }
            .invoice-info-section .left-info {
                text-align: left;
            }
            .invoice-info-section .right-info {
                text-align: right;
            }
            .invoice-info-section p {
                margin-bottom: 5px;
            }

            .table th {
                background-color: #f2f2f2;
                font-size: 15px;
            }
            .table td {
                font-size: 14px;
            }
            .alert {
                margin-top: 20px;
            }
            .action-buttons {
                margin-top: 30px;
                text-align: center;
            }
            .action-buttons .btn {
                margin: 5px;
            }
            .text-muted-invoice {
                color: #777;
                font-style: italic;
            }
            .total-amount-footer {
                font-size: 18px;
                font-weight: bold;
                text-align: right;
                margin-top: 20px;
            }
            .total-amount-footer span {
                color: #dc3545; /* Màu đỏ cho tổng tiền */
            }
            @media print {
                body {
                    background-color: #fff;
                    margin: 0;
                    padding: 0;
                    font-size: 12pt; /* Điều chỉnh font size cho bản in */
                }
                .container {
                    box-shadow: none;
                    border: none;
                    margin: 0;
                    padding: 0;
                    max-width: 100%; /* Full width khi in */
                }
                .no-print {
                    display: none;
                }
                .invoice-header, .invoice-title {
                    margin-bottom: 15px; /* Giảm margin khi in */
                }
                .invoice-info-section {
                    margin-bottom: 10px; /* Giảm margin khi in */
                    font-size: 11pt;
                }
                .table th, .table td {
                    font-size: 11pt;
                }
                .total-amount-footer {
                    font-size: 14pt;
                }
            }
        </style>
    </head>
    <body>
        <div class="container">
            <%-- Phần thông tin cửa hàng - nằm giữa trên đỉnh bảng hóa đơn --%>
            <div class="invoice-header">
                <h2>
                    <c:choose>
                        <c:when test="${not empty selectedShop && not empty selectedShop.shopName}">
                            ${selectedShop.shopName}
                        </c:when>
                        <c:otherwise>
                            Tên Cửa Hàng Của Bạn
                        </c:otherwise>
                    </c:choose>
                </h2>
                <p>
                    Địa chỉ:
                    <c:choose>
                        <c:when test="${not empty selectedShop && not empty selectedShop.address}">
                            ${selectedShop.address}
                        </c:when>
                        <c:otherwise>
                            Địa chỉ: Số X, Đường Y, Phường Z, Quận K, TP. Hồ Chí Minh
                        </c:otherwise>
                    </c:choose>
                </p>
                <p>
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

            <div class="invoice-title">
                <h1>Hóa Đơn Bán Hàng</h1>
            </div>

            <%-- Display Messages --%>
            <c:if test="${not empty requestScope.successMessage}">
                <div class="alert alert-success" role="alert">
                    ${requestScope.successMessage}
                </div>
            </c:if>
            <c:if test="${not empty requestScope.errorMessage}">
                <div class="alert alert-danger" role="alert">
                    ${requestScope.errorMessage}
                </div>
            </c:if>

            <c:set var="invoice" value="${requestScope.selectedInvoice}"/>
            <c:set var="customer" value="${requestScope.selectedCustomer}"/>
            <c:set var="employees" value="${requestScope.employees}"/>
            <c:set var="selectedEmployee" value="${requestScope.selectedEmployee}"/>
            <c:set var="shop" value="${requestScope.selectedShop}"/> <%-- Dòng này lấy shop từ requestScope --%>
            <c:set var="invoiceDetails" value="${requestScope.invoiceDetails}"/>
            <c:set var="products" value="${requestScope.products}"/>

            <c:choose>
                <c:when test="${selectedInvoice == null}">
                    <div class="invoice-container text-center">
                        <div class="alert alert-danger" role="alert">
                            <h3>Lỗi: Không tìm thấy thông tin hóa đơn này!</h3>
                            <p>Có vẻ như hóa đơn bạn đang cố gắng xem không tồn tại hoặc đã bị xóa.</p>
                            <p>Vui lòng quay lại danh sách hóa đơn và chọn một hóa đơn khác.</p>
                        </div>
                        <a href="InvoiceServlet?action=list" class="btn btn-secondary mt-3 no-print">Quay lại danh sách Hóa đơn</a>
                    </div>
                </c:when>
                <c:otherwise>
                    <%-- Thông tin hóa đơn và khách hàng/nhân viên được chia thành 2 cột --%>
                    <div class="invoice-info-section mb-4">
                        <div class="left-info">
                            <p><strong>Mã Hóa đơn:</strong> #${invoice.invoiceID}</p>
                            <p><strong>Ngày lập:</strong> <fmt:formatDate value="${invoice.invoiceDate}" pattern="dd/MM/yyyy HH:mm:ss"/></p>
                            <p>
                                <strong>Nhân viên:</strong>
                                <c:choose>
                                    <c:when test="${not empty selectedEmployee}">
                                        ${selectedEmployee.employeeName}
                                    </c:when>
                                    <c:otherwise>
                                        <c:set var="employeeName" value="N/A"/>
                                        <c:forEach var="emp" items="${employees}">
                                            <c:if test="${emp.employeeID == invoice.employeeID}">
                                                <c:set var="employeeName" value="${emp.employeeName}"/>
                                            </c:if>
                                        </c:forEach>
                                        ${employeeName} <span class="text-muted-invoice">(ID: ${invoice.employeeID})</span>
                                    </c:otherwise>
                                </c:choose>
                            </p>
                        </div>
                        <div class="right-info">
                            <p>
                                <strong>Khách hàng:</strong>
                                <c:choose>
                                    <c:when test="${not empty customer}">
                                        ${customer.customerName}
                                    </c:when>
                                    <c:otherwise>
                                        <span class="text-muted-invoice">Khách hàng không xác định / Khách lẻ</span>
                                    </c:otherwise>
                                </c:choose>
                            </p>
                            <c:if test="${not empty customer}">
                                <p><strong>Điện thoại KH:</strong> ${customer.phone}</p>
                                <c:if test="${not empty customer.email}"><p><strong>Email KH:</strong> ${customer.email}</p></c:if>
                            </c:if>
                            <p>
                                <strong>Cửa hàng:</strong>
                                <c:choose>
                                    <c:when test="${not empty shop}">
                                        ${shop.shopName}
                                    </c:when>
                                    <c:otherwise>
                                        <span class="text-muted-invoice">Không có thông tin cửa hàng (ID: ${invoice.shopID})</span>
                                    </c:otherwise>
                                </c:choose>
                            </p>
                        </div>
                    </div>
                    <table class="table table-bordered table-striped">
                        <thead>
                            <tr>
                                <th>STT</th>
                                <th>Sản phẩm</th>
                                <th>Đơn giá</th>
                                <th>Số lượng</th>
                                <th>Giảm Giá (%)</th>
                                <th>Thành tiền</th>
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
                                <td colspan="5" class="text-right"><strong>Tổng tiền:</strong></td>
                                <td><strong><fmt:formatNumber value="${invoice.totalAmount}" type="currency" currencySymbol="đ" maxFractionDigits="0"/></strong></td>
                            </tr>
                     
                            <c:if test="${not empty invoice.note}">
                                <tr>
                                    <td colspan="6" class="text-left"><strong>Ghi chú:</strong> ${invoice.note}</td>
                                </tr>
                            </c:if>
                        </tfoot>
                    </table>

                    <div class="action-buttons no-print">
                        <a href="InvoiceServlet?action=list" class="btn btn-info">Quay lại danh sách Hóa đơn</a>
                        <a href="#" onclick="window.print(); return false;" class="btn btn-secondary">In Hóa Đơn</a>

                        <c:if test="${not empty customer && not empty customer.email}">
                            <a href="InvoiceServlet?action=sendInvoiceEmail&invoiceID=${invoice.invoiceID}" class="btn btn-success"
                               onclick="return confirm('Bạn có chắc chắn muốn gửi hóa đơn này qua email không?');">Gửi Gmail Hóa đơn</a>
                        </c:if>

                        <c:if test="${!invoice.status}">
                            <a href="InvoiceServlet?action=manageInvoiceDetails&invoiceID=${invoice.invoiceID}" class="btn btn-primary">Quản lý chi tiết Hóa đơn</a>
                        </c:if>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>

        <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.5.4/dist/umd/popper.min.js"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
    </body>
</html>