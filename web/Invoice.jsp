<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8" />
        <title>Danh sách Hóa đơn</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet"/>
    </head>
    <body class="container py-4">

        <hr class="my-5"/>
        <!-- Form thêm hóa đơn -->
        <div class="card mb-4">
            <h5 class="card-header">Thêm hóa đơn mới</h5>
            <div class="card-body">
                <form method="post" action="InvoiceServlet" class="row g-3">
                    <input type="hidden" name="action" value="add" />
                    <div class="col-md-2">
                        <input type="text" name="invoiceID" class="form-control" placeholder="Mã HĐ" required/>
                    </div>
                    <div class="col-md-2">
                        <input type="text" name="customerID" class="form-control" placeholder="Mã KH" required/>
                    </div>
                    <div class="col-md-2">
                        <input type="text" name="employeeID" class="form-control" placeholder="Mã NV" required/>
                    </div>
                    <div class="col-md-2">
                        <input type="text" name="shopID" class="form-control" placeholder="Mã Shop" required/>
                    </div>
                    <div class="col-md-2">
                        <input type="number" name="totalAmount" class="form-control" placeholder="Tổng tiền" required/>
                    </div>
                    <div class="col-md-2">
                        <select name="status" class="form-select" required>
                            <option value="true">Đã thanh toán</option>
                            <option value="false">Chưa thanh toán</option>
                        </select>
                    </div>
                    <div class="col-md-4">
                        <input type="text" name="note" class="form-control" placeholder="Ghi chú"/>
                    </div>
                    <div class="col-md-auto">
                        <button type="submit" class="btn btn-success">Thêm</button>
                    </div>
                </form>
            </div>
        </div>

        <div class="card">
            <h5 class="card-header">Danh sách Hóa đơn</h5>
            <div class="table-responsive text-nowrap">
                <table class="table table-dark table-hover mb-0 align-middle">
                    <thead>
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
                    </thead>
                    <tbody class="table-border-bottom-0">
                        <c:forEach var="inv" items="${invoiceList}">
                            <tr>
                                <c:choose>
                                    <c:when test="${param.editId == inv.invoiceID}">
                                <form method="post" action="InvoiceServlet">
                                    <input type="hidden" name="action" value="update" />
                                    <td>
                                        ${inv.invoiceID}
                                        <input type="hidden" name="invoiceID" value="${inv.invoiceID}" />
                                    </td>
                                    <td><input type="text" name="customerID" class="form-control form-control-sm" value="${inv.customerID}" required/></td>
                                    <td><input type="text" name="employeeID" class="form-control form-control-sm" value="${inv.employeeID}" required/></td>
                                    <td><input type="text" name="shopID" class="form-control form-control-sm" value="${inv.shopID}" required/></td>
                                    <td><fmt:formatDate value="${inv.invoiceDate}" pattern="dd/MM/yyyy HH:mm:ss"/></td>
                                    <td>
                                        <input type="number" step="1" name="totalAmount" class="form-control form-control-sm" value="${inv.totalAmount}" required/>
                                    </td>
                                    <td>
                                        <select name="status" class="form-select form-select-sm" required>
                                            <option value="true" <c:if test="${inv.status}">selected</c:if>>Đã thanh toán</option>
                                            <option value="false" <c:if test="${!inv.status}">selected</c:if>>Chưa thanh toán</option>
                                            </select>
                                        </td>
                                        <td><input type="text" name="note" class="form-control form-control-sm" value="${inv.note}"/></td>
                                    <td>
                                        <button type="submit" class="btn btn-sm btn-success mb-1">Lưu</button>
                                        <a href="InvoiceServlet" class="btn btn-sm btn-secondary mb-1">Hủy</a>
                                    </td>
                                </form>
                            </c:when>
                            <c:otherwise>
                                <td><strong>${inv.invoiceID}</strong></td>
                                <td>${inv.customerID}</td>
                                <td>${inv.employeeID}</td>
                                <td>${inv.shopID}</td>
                                <td><fmt:formatDate value="${inv.invoiceDate}" pattern="dd/MM/yyyy HH:mm:ss"/></td>
                                <td>

                                    <fmt:formatNumber value="${inv.totalAmount}" pattern="#,##0 '₫'"/>

                                </td>
                                <td>
                                    <c:choose>
                                        <c:when test="${inv.status}">
                                            <span class="badge bg-success">Đã thanh toán</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge bg-warning text-dark">Chưa thanh toán</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>${inv.note}</td>
                                <td>
                                    <div class="dropdown">
                                        <button class="btn btn-sm btn-outline-light dropdown-toggle" type="button"
                                                data-bs-toggle="dropdown" aria-expanded="false">
                                            Hành động
                                        </button>
                                        <ul class="dropdown-menu">
                                            <li><a class="dropdown-item" href="InvoiceServlet?editId=${inv.invoiceID}">Cập nhật</a></li>
                                        </ul>
                                    </div>

                                    <!-- Form xóa tách riêng -->
                                    <form method="post" action="InvoiceServlet">
                                        <input type="hidden" name="action" value="delete"/>
                                        <input type="hidden" name="invoiceID" value="${inv.invoiceID}"/>
                                        <button type="submit" class="btn btn-sm btn-danger" onclick="return confirm('Bạn có chắc muốn xóa hóa đơn này không?');">
                                            Xóa
                                        </button>
                                    </form>
                                </td>
                            </c:otherwise>
                        </c:choose>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
