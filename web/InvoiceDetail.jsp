<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Hóa đơn bán hàng - ${selectedInvoice.invoiceID}</title>
        <%-- Liên kết Bootstrap CSS để đồng bộ giao diện --%>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
        <style>
            body {
                font-family: Arial, sans-serif;
                margin: 20px;
                padding: 0;
                background-color: #f4f4f4; /* Nền nhẹ nhàng */
            }
            .invoice-container {
                max-width: 800px; /* Giới hạn chiều rộng tờ hóa đơn */
                margin: 20px auto; /* Căn giữa */
                padding: 30px;
                background-color: #fff;
                border: 1px solid #ddd;
                box-shadow: 0 0 10px rgba(0,0,0,0.1); /* Tạo hiệu ứng tờ giấy */
            }
            .invoice-header, .invoice-info, .invoice-details, .invoice-footer {
                margin-bottom: 20px;
            }
            .invoice-header {
                text-align: center;
                margin-bottom: 30px;
            }
            .invoice-header h1 {
                font-size: 2.5em;
                margin-bottom: 5px;
                color: #333;
            }
            .invoice-header p {
                margin: 0;
                font-size: 0.9em;
                color: #666;
            }
            .invoice-title {
                text-align: center;
                font-size: 2em;
                font-weight: bold;
                margin: 30px 0;
                color: #007bff; /* Màu xanh nổi bật */
            }
            .invoice-info p {
                margin-bottom: 5px;
            }
            .invoice-details table {
                width: 100%;
                border-collapse: collapse;
                margin-bottom: 20px;
            }
            .invoice-details th, .invoice-details td {
                border: 1px solid #eee;
                padding: 8px;
                text-align: left;
            }
            .invoice-details th {
                background-color: #f2f2f2;
            }
            .invoice-total {
                text-align: right;
                font-size: 1.2em;
                font-weight: bold;
                padding-top: 10px;
                border-top: 2px solid #333; /* Dòng ngang cuối bảng */
            }
            .form-section {
                margin-top: 40px;
                padding-top: 20px;
                border-top: 1px dashed #ccc; /* Dòng phân cách nhẹ nhàng */
            }
            .text-muted-invoice {
                color: #777;
                font-style: italic;
            }
        </style>
    </head>
    <body>
        <div class="invoice-container">
            <div class="invoice-header">
                <c:if test="${not empty selectedShop}">
                    <h1>${selectedShop.shopName}</h1>
                    <p>${selectedShop.address} | Điện thoại: ${selectedShop.phone} | Email: ${selectedShop.email}</p>
                </c:if>

            </div>

            <h2 class="invoice-title">HÓA ĐƠN BÁN HÀNG</h2>

            <div class="invoice-info">
                <div class="row">
                    <div class="col-md-6">
                        <p><strong>Mã Hóa đơn:</strong> #${selectedInvoice.invoiceID}</p>
                        <p><strong>Ngày lập:</strong> <fmt:formatDate value="${selectedInvoice.invoiceDate}" pattern="dd/MM/yyyy HH:mm:ss" /></p>
                        <p><strong>Nhân viên:</strong>
                            <c:choose>
                                <c:when test="${not empty selectedEmployee}">${selectedEmployee.fullName} (${selectedEmployee.id})</c:when>
                                <c:otherwise>${selectedInvoice.employeeID} <span class="text-muted-invoice"></span></c:otherwise>
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
                                    <span class="text-muted-invoice">Khách hàng không xác định / Khách lẻ</span>
                                </c:otherwise>
                            </c:choose>
                        </p>
                        <p><strong>Điện thoại KH:</strong>
                            <c:choose>
                                <c:when test="${not empty selectedCustomer && not empty selectedCustomer.phone}">
                                    ${selectedCustomer.phone}
                                </c:when>
                                <c:otherwise>
                                    <span class="text-muted-invoice">N/A</span>
                                </c:otherwise>
                            </c:choose>
                        </p>
                        <p><strong>Email KH:</strong>
                            <c:choose>
                                <c:when test="${not empty selectedCustomer && not empty selectedCustomer.email}">
                                    ${selectedCustomer.email}
                                </c:when>
                                <c:otherwise>
                                    <span class="text-muted-invoice">N/A</span>
                                </c:otherwise>
                            </c:choose>
                        </p>
                    </div>
                </div>
            </div>

            <div class="invoice-details">
                <table class="table">
                    <thead>
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
                                <td class="text-end"><fmt:formatNumber value="${detail.unitPrice}" pattern="#,##0" /></td>
                                <td class="text-end">${detail.discount}%</td>
                                <td class="text-end"><fmt:formatNumber value="${detail.totalPrice}" pattern="#,##0" /> VNĐ</td>
                            </tr>
                            <c:set var="totalItemsAmount" value="${totalItemsAmount + detail.totalPrice}"/>
                        </c:forEach>
                        <c:if test="${empty invoiceDetails}">
                            <tr>
                                <td colspan="6" class="text-center text-muted-invoice">Chưa có mặt hàng nào trong hóa đơn này.</td>
                            </tr>
                        </c:if>
                    </tbody>
                    <tfoot>
                        <tr>
                            <td colspan="5" class="invoice-total">Tổng cộng:</td>
                            <td class="invoice-total text-end">
                                <fmt:formatNumber value="${totalItemsAmount}" pattern="#,##0" /> VNĐ
                            </td>
                        </tr>
                    </tfoot>
                </table>
            </div>

            <div class="invoice-footer text-center">
                <p>Cảm ơn quý khách và hẹn gặp lại!</p>
            </div>
        </div>

        <div class="container form-section">
            <h3 class="mb-3 text-center">Quản lý chi tiết hóa đơn</h3>

           
            <c:if test="${not empty errorMessage}">
                <div class="alert alert-danger" role="alert">
                    ${errorMessage}
                </div>
            </c:if>
            <c:if test="${not empty successMessage}">
                <div class="alert alert-success" role="alert">
                    ${successMessage}
                </div>
            </c:if>

            <h4 class="mb-3">Thêm mặt hàng mới vào hóa đơn</h4>
            <div class="card mb-4">
                <div class="card-body">
                    <form action="InvoiceServlet" method="post">
                        <input type="hidden" name="action" value="addDetail" />
                        <input type="hidden" name="invoiceID" value="${selectedInvoice.invoiceID}" />

                        <input type="hidden" name="shopID" value="${selectedInvoice.shopID}" />

                        <div class="mb-3">
                            <label for="productID" class="form-label">Mã sản phẩm:</label>
                            
                            <select class="form-select" id="productID" name="productID" required>
                                <option value="">-- Chọn sản phẩm --</option>
                               
                                <c:forEach var="invItem" items="${inventories}">
                                  
                                    <c:if test="${invItem.quantity > 0}">
                                        <option value="${invItem.product.productID}">
                                            ${invItem.product.productID} - ${invItem.product.productName} (Tồn: ${invItem.quantity})
                                        </option>
                                    </c:if>
                                </c:forEach>
                            </select>
                            <!--                            <small class="form-text text-muted">Chọn sản phẩm từ danh sách có sẵn.</small>-->
                        </div>
                        <div class="mb-3">
                            <label for="quantity" class="form-label">Số lượng:</label>
                            <input type="number" class="form-control" id="quantity" name="quantity" placeholder="Số lượng" min="1" required />
                        </div>
                        <div class="mb-3">
                            <label for="unitPrice" class="form-label">Đơn giá:</label>
                            <input type="number" class="form-control" id="unitPrice" name="unitPrice" placeholder="Đơn giá" step="any" min="0" required />
                            <small class="form-text text-muted">Đơn giá bán của sản phẩm.</small>
                        </div>
                        <div class="mb-3">
                            <label for="discount" class="form-label">Giảm giá (%):</label>
                            <input type="number" class="form-control" id="discount" name="discount" placeholder="Giảm giá (%)" step="any" min="0" max="100" value="0" />
                            <small class="form-text text-muted">Phần trăm giảm giá cho mặt hàng này (0-100).</small>
                        </div>
                        <button type="submit" class="btn btn-primary">Thêm mặt hàng</button>
                    </form>
                </div>
            </div>

            <h4 class="mb-3">Danh sách các mặt hàng đã có</h4>
            <table class="table table-bordered">
                <thead>
                    <tr>
                        <th>Mã SP</th>
                        <th>Số lượng</th>
                        <th>Giá bán</th>
                        <th>Giảm giá (%)</th>
                        <th>Thành tiền</th>
                        <!--                        <th>Hành động</th>-->
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="detail" items="${invoiceDetails}">
                        <tr>
                            <c:choose>
                                <c:when test="${editDetailID eq detail.invoiceDetailID}">
                            <form method="post" action="InvoiceServlet" style="margin:0;">
                                <input type="hidden" name="action" value="updateDetail" />
                                <input type="hidden" name="invoiceDetailID" value="${detail.invoiceDetailID}" />
                                <input type="hidden" name="invoiceID" value="${selectedInvoice.invoiceID}" />

                                <td>
                                    <%-- Dropdown cho sản phẩm khi chỉnh sửa --%>
                                    <select class="form-select form-control-sm" name="productID" required>
                                        <c:forEach var="prod" items="${products}">
                                            <option value="${prod.productID}" ${detail.productID == prod.productID ? 'selected' : ''}>
                                                ${prod.productName}
                                            </option>
                                        </c:forEach>
                                    </select>
                                </td>
                                <td><input type="number" class="form-control form-control-sm" name="quantity" value="${detail.quantity}" min="1" required /></td>
                                <td><input type="number" class="form-control form-control-sm" name="unitPrice" value="${detail.unitPrice}" step="any" min="0" required /></td>
                                <td><input type="number" class="form-control form-control-sm" name="discount" step="any" value="${detail.discount}" min="0" max="100" /></td>
                                <td><fmt:formatNumber value="${detail.totalPrice}" pattern="#,##0" /> VNĐ</td>
                                <td>
                                    <button type="submit" class="btn btn-success btn-sm">Lưu</button>
                                    <a href="InvoiceServlet?action=listDetail&invoiceID=${selectedInvoice.invoiceID}" class="btn btn-secondary btn-sm">Hủy</a>
                                </td>
                            </form>
                        </c:when>
                        <c:otherwise>
                            
                            <td>
                                <c:set var="productNameDisplay" value="${detail.productID}"/>
                                <c:forEach var="prod" items="${products}">
                                    <c:if test="${prod.productID == detail.productID}">
                                        <c:set var="productNameDisplay" value="${prod.productID} - ${prod.productName}"/>
                                    </c:if>
                                </c:forEach>
                                ${productNameDisplay}
                            </td>
                            <td>${detail.quantity}</td>
                            <td><fmt:formatNumber value="${detail.unitPrice}" pattern="#,##0" /> VNĐ</td>
                            <td>${detail.discount} %</td>
                            <td><fmt:formatNumber value="${detail.totalPrice}" pattern="#,##0" /> VNĐ</td>
                            <!--                            <td>
                                                            <a href="InvoiceServlet?action=listDetail&invoiceID=${selectedInvoice.invoiceID}&editDetailID=${detail.invoiceDetailID}" class="btn btn-info btn-sm">Sửa</a>
                                                         
                                                        </td>-->
                        </c:otherwise>
                    </c:choose>
                    </tr>
                </c:forEach>
                <c:if test="${empty invoiceDetails && empty editDetailID}">
                    <tr>
                        <td colspan="6" class="text-center">Chưa có mặt hàng nào trong hóa đơn này.</td>
                    </tr>
                </c:if>
                </tbody>
            </table>
            <p class="text-center mt-4"><a href="InvoiceServlet" class="btn btn-secondary">Quay lại danh sách Hóa đơn</a></p>
        </div>

        <%-- Bootstrap Bundle with Popper --%>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
    </body>
</html>