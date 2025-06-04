<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Danh sách Hóa đơn</title>
    </head>
    <body>

        <h2>Quản lý Hóa đơn</h2>

        <!-- Form thêm hóa đơn -->
        <h3>Thêm hóa đơn mới</h3>
        <form method="post" action="InvoiceServlet">
            <input type="hidden" name="action" value="add" />
            Mã HĐ: <input type="text" name="invoiceID" required/><br/>
            Mã KH: <input type="text" name="customerID" required/><br/>
            Mã NV: <input type="text" name="employeeID" required/><br/>
            Mã Shop: <input type="text" name="shopID" required/><br/>
            <!-- Tổng tiền do servlet tính, không cho nhập -->
            <input type="hidden" name="totalAmount" value="0"/>
            Trạng thái:
            <select name="status" required>
                <option value="true">Đã thanh toán</option>
                <option value="false">Chưa thanh toán</option>
            </select><br/>
            Ghi chú: <input type="text" name="note"/><br/>
            <input type="submit" value="Thêm"/>
        </form>

        <hr/>

        <!-- Danh sách hóa đơn -->
        <h3>Danh sách Hóa đơn</h3>
        <table border="1" cellpadding="5" cellspacing="0">
            <tr>
                <th>Mã HĐ</th>
                <th>Khách hàng</th>
                <th>Nhân viên</th>
                <th>Shop</th>
                <th>Ngày tạo</th>
                <th>Tổng tiền</th>
                <th>Trạng thái</th>
                <th>Ghi chú</th>
                <th>Thao tác</th>
            </tr>

            <c:forEach var="inv" items="${invoiceList}">
                <tr>
                    <c:choose>
                        <c:when test="${param.editId == inv.invoiceID}">
                        <form method="post" action="InvoiceServlet">
                            <input type="hidden" name="action" value="update"/>
                            <td>${inv.invoiceID}
                                <input type="hidden" name="invoiceID" value="${inv.invoiceID}"/>
                            </td>
                            <td><input type="text" name="customerID" value="${inv.customerID}" required/></td>
                            <td><input type="text" name="employeeID" value="${inv.employeeID}" required/></td>
                            <td><input type="text" name="shopID" value="${inv.shopID}" required/></td>
                            <td><fmt:formatDate value="${inv.invoiceDate}" pattern="dd/MM/yyyy HH:mm:ss"/></td>
                            <td><fmt:formatNumber value="${inv.totalAmount}" pattern="#,##0"/></td>
                            <td>
                                <select name="status">
                                    <option value="true" <c:if test="${inv.status}">selected</c:if>>Đã thanh toán</option>
                                    <option value="false" <c:if test="${!inv.status}">selected</c:if>>Chưa thanh toán</option>
                                    </select>
                                </td>
                                <td><input type="text" name="note" value="${inv.note}"/></td>
                            <td>
                                <input type="submit" value="Lưu"/>
                                <a href="InvoiceServlet">Hủy</a>
                            </td>
                        </form>
                    </c:when>

                    <c:otherwise>
                        <td>${inv.invoiceID}</td>
                        <td>${inv.customerID}</td>
                        <td>${inv.employeeID}</td>
                        <td>${inv.shopID}</td>
                        <td><fmt:formatDate value="${inv.invoiceDate}" pattern="dd/MM/yyyy HH:mm:ss"/></td>
                        <td><fmt:formatNumber value="${inv.totalAmount}" pattern="#,##0"/></td>
                        <td>
                            <c:choose>
                                <c:when test="${inv.status}">Đã thanh toán</c:when>
                                <c:otherwise>Chưa thanh toán</c:otherwise>
                            </c:choose>
                        </td>
                        <td>${inv.note}</td>
                        <td>
                            <a href="InvoiceServlet?editId=${inv.invoiceID}">Sửa</a> |
                            <a href="InvoiceServlet?action=listDetail&invoiceID=${inv.invoiceID}">Xem chi tiết</a>
                            <form method="post" action="InvoiceServlet" style="display:inline;">
                                <input type="hidden" name="action" value="delete"/>
                                <input type="hidden" name="invoiceID" value="${inv.invoiceID}"/>
                                <input type="submit" value="Xóa" onclick="return confirm('Bạn có chắc muốn xóa?');"/>
                            </form>
                        </td>
                    </c:otherwise>
                </c:choose>
            </tr>
        </c:forEach>
    </table>

    <!-- Chi tiết hóa đơn và form thêm chi tiết chỉ hiện khi có selectedInvoice -->
    <c:if test="${not empty selectedInvoice}">
        <h3>Chi tiết hóa đơn: ${selectedInvoice.invoiceID}</h3>
        <table>
            <thead>
                <tr>
                    <th>Mã HĐ</th>
                    <th>Mã SP</th>
                    <th>Số lượng</th>
                    <th>Giá bán</th>
                    <th>Giảm giá (%)</th>
                    <th>Thành tiền</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="detail" items="${invoiceDetails}">
                    <tr>
                        <td>${detail.invoiceID}</td>
                        <td>${detail.productID}</td>
                        <td>${detail.quantity}</td>
                        <td>${detail.unitPrice}</td>
                        <td>${detail.discount}</td>
                        <td>${detail.totalPrice}</td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

        <h4>Thêm chi tiết hóa đơn</h4>
        <form action="InvoiceServlet" method="post">
            <input type="hidden" name="action" value="addDetail"/>
            <input type="hidden" name="invoiceID" value="${selectedInvoice.invoiceID}"/>
            <input type="text" name="productID" placeholder="Mã SP" required/>
            <input type="text" name="shopID" placeholder="Mã shop" required/>
            <input type="number" name="quantity" placeholder="Số lượng" required/>
            <input type="number" step="0.01" name="unitPrice" placeholder="Đơn giá" required/>
            <input type="number" step="0.01" name="discount" placeholder="Giảm giá (%)"/>
            <button type="submit">Thêm</button>
        </form>
    </c:if>

</body>
</html>
