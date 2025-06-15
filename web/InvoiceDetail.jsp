<%@ page contentType="text/html;charset=UTF-8" %>
<meta charset="UTF-8">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Chi tiết Hóa đơn</title>
    </head>
    <body>
        <h2>Chi tiết hóa đơn: ${selectedInvoice.invoiceID}</h2>



        <p><strong>Cửa hàng:</strong> ${selectedShop.shopName}</p>
        <p><strong>Địa chỉ:</strong> ${selectedShop.address}</p> <%-- Sửa từ shopAddress thành address --%>
        <p><strong>Điện thoại:</strong> ${selectedShop.phone}</p> <%-- Sửa từ shopPhone thành phone --%>



        <br> 


        <p><strong>Mã khách hàng:</strong> ${selectedInvoice.customerID}</p>


        <br> <%-- Thêm một dòng cách giữa thông tin khách hàng và bảng chi tiết --%>

        <table border="1" cellpadding="5" cellspacing="0">
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
                    <%-- Xác định xem dòng hiện tại có phải là dòng đang được chỉnh sửa hay không --%>


                    <tr>
                        <c:choose>
                            <c:when test="${editDetailID eq detail.invoiceDetailID}">
                                <%-- CHẾ ĐỘ CHỈNH SỬA: Hiển thị Input fields và nút Lưu/Hủy --%>
                        <form method="post" action="InvoiceServlet" style="margin:0;">
                            <input type="hidden" name="action" value="updateDetail" />
                            <input type="hidden" name="invoiceDetailID" value="${detail.invoiceDetailID}" />
                            <input type="hidden" name="invoiceID" value="${selectedInvoice.invoiceID}" />
                            <input type="hidden" name="shopID" value="${detail.shopID}" /> <%-- Giữ nguyên shopID cho detail --%>

                            <td><input type="text" name="productID" value="${detail.productID}" /></td>
                            <td><input type="number" name="quantity" value="${detail.quantity}" min="0" /></td>
                            <td><input type="number" name="unitPrice" value="${detail.unitPrice}" step="any" /></td>
                            <td><input type="number" name="discount" step="1" value="${detail.discount}" /></td>
                            <td><fmt:formatNumber value="${detail.totalPrice}" pattern="#,##0" /> VNĐ</td> <%-- Tổng tiền vẫn là hiển thị --%>
                            <td>
                                <input type="submit" value="Lưu" />
                                <%-- Nút Hủy: chuyển hướng lại trang chi tiết hóa đơn mà không có param editDetailID --%>
                                <a href="InvoiceServlet?action=listDetail&invoiceID=${selectedInvoice.invoiceID}">Hủy</a>
                            </td>
                        </form>
                    </c:when>
                    <c:otherwise>
                        <%-- CHẾ ĐỘ XEM: Hiển thị Text và nút Sửa --%>
                        <td>${detail.productID}</td>
                        <td>${detail.quantity}</td>
                        <td><fmt:formatNumber value="${detail.unitPrice}" pattern="#,##0" /> VNĐ</td>
                        <td>${detail.discount} %</td>
                        <td><fmt:formatNumber value="${detail.totalPrice}" pattern="#,##0" /> VNĐ</td>
                        <td>
                            <%-- Nút Sửa: Gửi lại yêu cầu với param editDetailID để servlet biết dòng nào cần chỉnh sửa --%>
                            <a href="InvoiceServlet?action=listDetail&invoiceID=${selectedInvoice.invoiceID}&editDetailID=${detail.invoiceDetailID}">Sửa</a>
                        </td>
                    </c:otherwise>
                </c:choose>
            </tr>
        </c:forEach>
    </tbody>
</table>

<h4>Thêm chi tiết hóa đơn</h4>
<form action="InvoiceServlet" method="post">
    <input type="hidden" name="action" value="addDetail" />
    <input type="hidden" name="invoiceID" value="${selectedInvoice.invoiceID}" />
    <input type="text" name="productID" placeholder="Mã SP" required />
    <input type="text" name="shopID" placeholder="Mã shop" required />
    <input type="number" name="quantity" placeholder="Số lượng" required />
    <input type="number" step="1" name="unitPrice" placeholder="Đơn giá" required />
    <input type="number" step="1" name="discount" placeholder="Giảm giá (%)" />
    <button type="submit">Thêm</button>
</form>

<p><a href="InvoiceServlet">Quay lại danh sách Hóa đơn</a></p>
</body>
</html>