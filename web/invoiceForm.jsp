<%--
    Document   : invoiceForm
    Created on : Jun 17, 2025, 4:44:48 PM
    Author     : duckh
--%>

<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Quản lý Chi tiết Hóa đơn - #${selectedInvoice.invoiceID}</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
        <style>
            body {
                font-family: Arial, sans-serif;
                margin: 20px;
                padding: 0;
                background-color: #f4f4f4;
            }
            .form-section {
                max-width: 900px; /* Có thể rộng hơn một chút cho bảng quản lý */
                margin: 20px auto;
                padding: 30px;
                background-color: #fff;
                border: 1px solid #ddd;
                box-shadow: 0 0 10px rgba(0,0,0,0.1);
            }
            .text-muted-invoice {
                color: #777;
                font-style: italic;
            }
        </style>
    </head>
    <body>
        <div class="container form-section">
            <h3 class="mb-3 text-center">Quản lý chi tiết hóa đơn: #${selectedInvoice.invoiceID}</h3>
            <p class="text-center text-muted">Hóa đơn lập ngày: <fmt:formatDate value="${selectedInvoice.invoiceDate}" pattern="dd/MM/yyyy HH:mm:ss" /></p>

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
                        <input type="hidden" name="action" value="selectProductForPrice" id="selectProductAction"/>
                        <input type="hidden" name="invoiceID" value="${selectedInvoice.invoiceID}" />
                        <input type="hidden" name="shopID" value="${selectedInvoice.shopID}" />

                      <div class="mb-3">
                            <label for="productID" class="form-label">Mã sản phẩm:</label>
                            <select class="form-select" id="productID" name="productID" required onchange="this.form.submit()">
                                <option value="">-- Chọn sản phẩm --</option>
                                <c:forEach var="invItem" items="${inventories}">
                                    <c:if test="${invItem.quantity > 0}">
                                        <option value="${invItem.product.productID}"
                                                ${param.productID == invItem.product.productID ? 'selected' : ''}>
                                            ${invItem.product.productID} - ${invItem.product.productName} (Tồn: ${invItem.quantity})
                                        </option>
                                    </c:if>
                   </div>
                        <div class="mb-3">
                            <label for="quantity" class="form-label">Số lượng:</label>
                            <input type="number" class="form-control" id="quantity" name="quantity" placeholder="Số lượng" min="1" required
                                   value="${param.quantity != null ? param.quantity : ''}" />
                        </div>
                        <div class="mb-3">
                            <label for="unitPrice" class="form-label">Đơn giá:</label>
                            <input type="number" class="form-control" id="unitPrice" name="unitPrice" placeholder="Đơn giá" step="any" min="0" required
                                   value="${not empty selectedUnitPrice ? selectedUnitPrice : (param.unitPrice != null ? param.unitPrice : '')}" />
                            <small class="form-text text-muted">Đơn giá bán của sản phẩm.</small>
                        </div>
                        <div class="mb-3">
                            <label for="discount" class="form-label">Giảm giá (%):</label>
                            <input type="number" class="form-control" id="discount" name="discount" placeholder="Giảm giá (%)" step="any" min="0" max="100" value="${param.discount != null ? param.discount : '0'}" />
                            <small class="form-text text-muted">Phần trăm giảm giá cho mặt hàng này (0-100).</small>
                        </div>
                        <button type="submit" class="btn btn-primary" onclick="document.getElementById('selectProductAction').value = 'addDetail';">Thêm mặt hàng</button>
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
                        <th>Hành động</th>
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
                                    <a href="InvoiceServlet?action=manageInvoiceDetails&invoiceID=${selectedInvoice.invoiceID}" class="btn btn-secondary btn-sm">Hủy</a>
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
                            <td>
                                <a href="InvoiceServlet?action=manageInvoiceDetails&invoiceID=${selectedInvoice.invoiceID}&editDetailID=${detail.invoiceDetailID}" class="btn btn-info btn-sm">Sửa</a>
<!--                                <a href="InvoiceServlet?action=deleteDetail&invoiceID=${selectedInvoice.invoiceID}&invoiceDetailID=${detail.invoiceDetailID}" class="btn btn-danger btn-sm" onclick="return confirm('Bạn có chắc chắn muốn xóa mặt hàng này?');">Xóa</a>-->
                            </td>
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
            <p class="text-center mt-4"><a href="InvoiceServlet?action=listDetail&invoiceID=${selectedInvoice.invoiceID}" class="btn btn-secondary">Quay lại xem Hóa đơn</a></p>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
    </body>
</html>