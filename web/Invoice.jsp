<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8" />
        <title>Danh sách Hóa đơn</title>
        <%-- Đảm bảo bạn đã liên kết với thư viện Bootstrap CSS ở đây --%>
        <%-- Ví dụ (thay thế bằng đường dẫn CDN hoặc file local của bạn): --%>
        <%-- <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous"> --%>
        <%-- Hoặc nếu bạn có file local: <link rel="stylesheet" href="path/to/bootstrap.min.css"> --%>
        <style>
            /* Một số style cơ bản nếu không có Bootstrap */
            body {
                font-family: Arial, sans-serif;
                margin: 20px;
            }
            .card {
                border: 1px solid #ccc;
                border-radius: 0.25rem;
                margin-bottom: 1rem;
            }
            .card-header {
                padding: 0.75rem 1.25rem;
                margin-bottom: 0;
                background-color: #f8f9fa;
                border-bottom: 1px solid #ccc;
            }
            .card-body {
                padding: 1.25rem;
            }
            .mb-3 {
                margin-bottom: 1rem !important;
            }
            .form-label {
                display: inline-block;
                margin-bottom: 0.5rem;
            }
            .form-control, .form-select {
                display: block;
                width: 100%;
                padding: 0.375rem 0.75rem;
                font-size: 1rem;
                line-height: 1.5;
                color: #495057;
                background-color: #fff;
                background-clip: padding-box;
                border: 1px solid #ced4da;
                border-radius: 0.25rem;
                transition: border-color 0.15s ease-in-out, box-shadow 0.15s ease-in-out;
            }
            .form-control-plaintext {
                display: block;
                width: 100%;
                padding: 0.375rem 0; /* No horizontal padding */
                margin-bottom: 0;
                line-height: 1.5;
                color: #212529;
                background-color: transparent;
                border: solid transparent;
                border-width: 1px 0;
            }
            .btn {
                display: inline-block;
                font-weight: 400;
                color: #212529;
                text-align: center;
                vertical-align: middle;
                user-select: none;
                background-color: transparent;
                border: 1px solid transparent;
                padding: 0.375rem 0.75rem;
                font-size: 1rem;
                line-height: 1.5;
                border-radius: 0.25rem;
                transition: color 0.15s ease-in-out, background-color 0.15s ease-in-out, border-color 0.15s ease-in-out, box-shadow 0.15s ease-in-out;
            }
            .btn-primary {
                color: #fff;
                background-color: #0d6efd;
                border-color: #0d6efd;
            }
            .btn-danger {
                color: #fff;
                background-color: #dc3545;
                border-color: #dc3545;
            }
            .btn-info { /* cho nút Sửa */
                color: #fff;
                background-color: #0dcaf0;
                border-color: #0dcaf0;
            }
            .btn-success { /* cho nút Lưu */
                color: #fff;
                background-color: #198754;
                border-color: #198754;
            }
            .table {
                width: 100%;
                margin-bottom: 1rem;
                color: #212529;
                border-collapse: collapse;
            }
            .table th, .table td {
                padding: 0.75rem;
                vertical-align: top;
                border-top: 1px solid #dee2e6;
            }
            .table thead th {
                vertical-align: bottom;
                border-bottom: 2px solid #dee2e6;
            }
            .table-bordered {
                border: 1px solid #dee2e6;
            }
            .table-bordered th, .table-bordered td {
                border: 1px solid #dee2e6;
            }
            .pagination-container {
                display: flex;
                gap: 10px; /* Thêm khoảng cách giữa các phần tử phân trang */
                margin-top: 20px;
            }
        </style>
    </head>
    <body>

        <div class="container">
            <h2 class="my-4">Quản lý Hóa đơn</h2>

            <div class="card mb-4">
                <h5 class="card-header">Tìm kiếm Hóa đơn</h5>
                <div class="card-body">
                    <form method="post" action="InvoiceServlet">
                        <input type="hidden" name="action" value="search" />
                        <div class="mb-3">
                            <label for="searchInvoiceID" class="form-label">Tìm theo Mã HĐ:</label>
                            <input type="text" class="form-control" id="searchInvoiceID" name="invoiceID" placeholder="Nhập mã hóa đơn" value="${data}" />
                        </div>
                        <button type="submit" class="btn btn-primary">Tìm kiếm</button>
                    </form>
                </div>
            </div>

            <div class="card mb-4">
                <h5 class="card-header">Thêm hóa đơn mới</h5>
                <div class="card-body">
                    <form method="post" action="InvoiceServlet">
                        <input type="hidden" name="action" value="add" />
                        <div class="mb-3">
                            <label for="addInvoiceID" class="form-label">Mã HĐ:</label>
                            <input type="text" class="form-control" id="addInvoiceID" name="invoiceID" required />
                        </div>
                        <div class="mb-3">
                            <label for="addCustomerID" class="form-label">Mã KH:</label>
                            <input type="text" class="form-control" id="addCustomerID" name="customerID" required />
                        </div>
                        <div class="mb-3">
                            <label for="addEmployeeID" class="form-label">Mã NV:</label>
                            <input type="text" class="form-control" id="addEmployeeID" name="employeeID" required />
                        </div>
                        <div class="mb-3">
                            <label for="addShopID" class="form-label">Mã Shop:</label>
                            <input type="text" class="form-control" id="addShopID" name="shopID" required />
                        </div>
                        <input type="hidden" name="totalAmount" value="0" />
                        <div class="mb-3">
                            <label for="addStatus" class="form-label">Trạng thái:</label>
                            <select class="form-select" id="addStatus" name="status" required>
                                <option value="true">Đã thanh toán</option>
                                <option value="false">Chưa thanh toán</option>
                            </select>
                        </div>
                        <div class="mb-3">
                            <label for="addNote" class="form-label">Ghi chú:</label>
                            <input type="text" class="form-control" id="addNote" name="note" />
                        </div>
                        <button type="submit" class="btn btn-primary">Thêm</button>
                    </form>
                </div>
            </div>

            <hr class="my-4"/>

            <h3 class="mb-3">Danh sách Hóa đơn</h3>
            <table class="table table-bordered">
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
                <tbody>
                    <c:forEach var="inv" items="${invoiceList}">
                        <tr>
                            <c:choose>
                                <c:when test="${param.editId == inv.invoiceID}">
                            <form method="post" action="InvoiceServlet" style="margin:0;">
                                <input type="hidden" name="action" value="update" />
                                <input type="hidden" name="invoiceID" value="${inv.invoiceID}" />
                                <input type="hidden" name="totalAmount" value="${inv.totalAmount}" />
                                <%-- Row for editing --%>
                                <td>${inv.invoiceID}</td>
                                <td><input type="text" class="form-control" name="customerID" value="${inv.customerID}" required /></td>
                                <td><input type="text" class="form-control" name="employeeID" value="${inv.employeeID}" required /></td>
                                <td><input type="text" class="form-control" name="shopID" value="${inv.shopID}" required /></td>
                                <td><fmt:formatDate value="${inv.invoiceDate}" pattern="dd/MM/yyyy HH:mm:ss" /></td>
                                <td><fmt:formatNumber value="${inv.totalAmount}" pattern="#,##0" /></td>
                                <td>
                                    <select class="form-select" name="status" required>
                                        <option value="true" <c:if test="${inv.status}">selected</c:if>>Đã thanh toán</option>
                                        <option value="false" <c:if test="${!inv.status}">selected</c:if>>Chưa thanh toán</option>
                                        </select>
                                    </td>
                                    <td><input type="text" class="form-control" name="note" value="${inv.note}" /></td>
                                <td>
                                    <button type="submit" class="btn btn-success btn-sm">Lưu</button>
                                    <a href="InvoiceServlet" class="btn btn-secondary btn-sm">Hủy</a>
                                </td>
                            </form>
                        </c:when>
                        <c:otherwise>
                            <td>${inv.invoiceID}</td>
                            <td>${inv.customerID}</td>
                            <td>${inv.employeeID}</td>
                            <td>${inv.shopID}</td>
                            <td><fmt:formatDate value="${inv.invoiceDate}" pattern="dd/MM/yyyy HH:mm:ss" /></td>
                            <td><fmt:formatNumber value="${inv.totalAmount}" pattern="#,##0" /> VNĐ</td>
                            <td>
                                <c:choose>
                                    <c:when test="${inv.status}">Đã thanh toán</c:when>
                                    <c:otherwise>Chưa thanh toán</c:otherwise>
                                </c:choose>
                            </td>
                            <td>${inv.note}</td>
                            <td>
                                <a href="InvoiceServlet?editId=${inv.invoiceID}" class="btn btn-info btn-sm">Sửa</a>
                                <a href="InvoiceServlet?action=listDetail&invoiceID=${inv.invoiceID}" class="btn btn-primary btn-sm">Xem chi tiết</a>
                                <form method="post" action="InvoiceServlet" style="display:inline;">
                                    <input type="hidden" name="action" value="delete" />
                                    <input type="hidden" name="invoiceID" value="${inv.invoiceID}" />
                                    <button type="submit" class="btn btn-danger btn-sm" onclick="return confirm('Bạn có chắc muốn xóa?');">Xóa</button>
                                </form>
                            </td>
                        </c:otherwise>
                    </c:choose>
                    </tr>
                </c:forEach>
                </tbody>
            </table>

            <c:if test="${not empty errorMessage}">
                <div class="alert alert-danger" role="alert">
                    ${errorMessage}
                </div>
            </c:if>

            <div class="pagination-container">
                <c:if test="${totalPages > 1}">
                    <c:if test="${currentPage > 1}">
                        <a href="InvoiceServlet?page=${currentPage - 1}" class="btn btn-outline-primary">&laquo; Trang trước</a>
                    </c:if>

                    <c:forEach begin="1" end="${totalPages}" var="i">
                        <c:choose>
                            <c:when test="${i == currentPage}">
                                <span class="btn btn-primary disabled">${i}</span>
                            </c:when>
                            <c:otherwise>
                                <a href="InvoiceServlet?page=${i}" class="btn btn-outline-primary">${i}</a>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>

                    <c:if test="${currentPage < totalPages}">
                        <a href="InvoiceServlet?page=${currentPage + 1}" class="btn btn-outline-primary">Trang sau &raquo;</a>
                    </c:if>
                </c:if>
            </div>
        </div>

        <%-- Đảm bảo bạn đã liên kết với thư viện Bootstrap JS nếu cần các hiệu ứng JS --%>
        <%-- Ví dụ: <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script> --%>
        <script>
            // Lưu vị trí cuộn trang trước khi rời đi
            window.addEventListener('beforeunload', function () {
                sessionStorage.setItem('scrollPos', window.scrollY);
            });

            // Khôi phục vị trí cuộn khi trang tải lại
            window.addEventListener('load', function () {
                const scrollPos = sessionStorage.getItem('scrollPos');
                if (scrollPos) {
                    window.scrollTo(0, parseInt(scrollPos));
                    sessionStorage.removeItem('scrollPos'); // Xóa sau khi dùng để không ảnh hưởng lần tải sau
                }
            });
        </script>
    </body>
</html>