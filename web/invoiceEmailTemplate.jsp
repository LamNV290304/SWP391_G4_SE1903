<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Hóa Đơn Của Bạn</title>
        <style>
            /* CSS nhúng trực tiếp vào HTML (Inline CSS) hoặc trong thẻ <style> */
            /* Email client hỗ trợ CSS kém, nên tốt nhất là inline hoặc tối thiểu như thế này */
            body {
                font-family: Arial, sans-serif;
                font-size: 14px;
                color: #333;
                line-height: 1.6;
            }
            .container {
                width: 80%; /* hoặc fixed width như 600px */
                margin: 20px auto;
                border: 1px solid #eee;
                padding: 20px;
                box-shadow: 0 0 10px rgba(0,0,0,0.1);
            }
            h1, h2 {
                color: #0056b3;
                text-align: center;
            }
            table {
                width: 100%;
                border-collapse: collapse;
                margin-top: 20px;
            }
            th, td {
                border: 1px solid #ddd;
                padding: 8px;
                text-align: left;
            }
            th {
                background-color: #f2f2f2;
                font-weight: bold;
            }
            .text-right {
                text-align: right;
            }
            .footer {
                margin-top: 30px;
                text-align: center;
                font-size: 12px;
                color: #777;
            }
            .shop-info {
                text-align: center;
                margin-bottom: 20px;
            }
            .total-row {
                background-color: #e0f2f7; /* Màu nền nhẹ cho tổng cộng */
                font-weight: bold;
            }
        </style>
    </head>
    <body>
        <div class="container">
            <div class="shop-info">
                <c:if test="${not empty selectedShop}">
                    <h1>${selectedShop.shopName}</h1>
                    <p>${selectedShop.address} | Điện thoại: ${selectedShop.phone} | Email: ${selectedShop.email}</p>
                </c:if>
                <hr>
            </div>

            <h2>HÓA ĐƠN BÁN HÀNG</h2>
            <p><strong>Mã Hóa đơn:</strong> #${selectedInvoice.invoiceID}</p>
            <p><strong>Ngày lập:</strong> <fmt:formatDate value="${selectedInvoice.invoiceDate}" pattern="dd/MM/yyyy HH:mm:ss"/></p>
            <p><strong>Nhân viên:</strong>
                <c:choose>
                    <c:when test="${not empty selectedEmployee}">${selectedEmployee.fullName} (${selectedEmployee.id})</c:when>
                    <c:otherwise>${selectedInvoice.employeeID} <span class="text-muted-invoice"></span></c:otherwise>
                </c:choose>
            </p>
            <p><strong>Khách hàng:</strong>
                <c:choose>
                    <c:when test="${not empty selectedCustomer}">${selectedCustomer.customerName}</c:when>
                    <c:otherwise>Khách lẻ</c:otherwise>
                </c:choose>
            </p>
            <p><strong>Email KH:</strong> ${selectedCustomer.email}</p>

            <table>
                <thead>
                    <tr>
                        <th>STT</th>
                        <th>Tên sản phẩm</th>
                        <th class="text-right">SL</th>
                        <th class="text-right">Đơn giá</th>
                        <th class="text-right">Giảm giá (%)</th>
                        <th class="text-right">Thành tiền</th>
                    </tr>
                </thead>
                <tbody>
                    <c:set var="totalItemsAmount" value="${0}"/>
                    <c:forEach var="detail" items="${invoiceDetails}" varStatus="status">
                        <tr>
                            <td>${status.index + 1}</td>
                            <td>
                                <c:set var="productNameDisplay" value="Sản phẩm không xác định"/>
                                <c:forEach var="prod" items="${products}">
                                    <c:if test="${prod.productID == detail.productID}">
                                        <c:set var="productNameDisplay" value="${prod.productName}"/>
                                    </c:if>
                                </c:forEach>
                                ${productNameDisplay}
                            </td>
                            <td class="text-right">${detail.quantity}</td>
                            <td class="text-right"><fmt:formatNumber value="${detail.unitPrice}" type="number" pattern="#,##0"/> VNĐ</td>
                            <td class="text-right"><fmt:formatNumber value="${detail.discount}" type="number" pattern="#0"/>%</td>
                            <td class="text-right"><fmt:formatNumber value="${detail.totalPrice}" type="number" pattern="#,##0"/> VNĐ</td>
                        </tr>
                        <c:set var="totalItemsAmount" value="${totalItemsAmount + detail.totalPrice}"/>
                    </c:forEach>
                    <c:if test="${empty invoiceDetails}">
                        <tr><td colspan="6" class="text-center" style="font-style: italic; color: #777;">Chưa có mặt hàng nào trong hóa đơn này.</td></tr>
                    </c:if>
                </tbody>
                <tfoot>
                    <tr class="total-row">
                        <td colspan="5" class="text-right"><strong>Tổng cộng:</strong></td>
                        <td class="text-right"><strong><fmt:formatNumber value="${totalItemsAmount}" type="number" pattern="#,##0"/> VNĐ</strong></td>
                    </tr>
                </tfoot>
            </table>

            <div class="footer">
                <p>Cảm ơn quý khách đã mua sắm tại cửa hàng của chúng tôi!</p>
                <p>Mọi thắc mắc xin liên hệ ${selectedShop.phone} hoặc ${selectedShop.email}.</p>
            </div>
        </div>
    </body>
</html>