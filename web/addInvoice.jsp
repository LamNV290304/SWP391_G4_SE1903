<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Thêm Hóa đơn mới</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
        <style>
            body {
                font-family: Arial, sans-serif;
                margin: 20px;
                padding: 0;
            }
            .container {
                max-width: 600px; /* Giới hạn chiều rộng của form */
                margin: 0 auto;
                padding: 20px;
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
                text-decoration: none;
            }
            .btn-primary {
                color: #fff;
                background-color: #0d6efd;
                border-color: #0d6efd;
            }
            .btn-secondary {
                color: #fff;
                background-color: #6c757d;
                border-color: #6c757d;
            }
            .alert {
                padding: 1rem;
                margin-bottom: 1rem;
                border: 1px solid transparent;
                border-radius: 0.25rem;
            }
            .alert-danger {
                color: #842029;
                background-color: #f8d7da;
                border-color: #f5c2c7;
            }
        </style>
    </head>
    <body>
        <div class="container">
            <h2 class="my-4 text-center">Thêm Hóa đơn mới</h2>

            <%-- Hiển thị thông báo lỗi nếu có --%>
            <c:if test="${not empty errorMessage}">
                <div class="alert alert-danger" role="alert">
                    ${errorMessage}
                </div>
            </c:if>

            <div class="card">
                <div class="card-body">
                    <form method="post" action="InvoiceServlet">
                        <input type="hidden" name="action" value="add" />

                        <div class="mb-3">
                         
                            <input type="hidden" class="form-control" id="invoiceID" name="invoiceID" required placeholder="Nhập mã hóa đơn (ví dụ: INV001)" />
                        </div>

                        <div class="mb-3">
                            <label for="customerID" class="form-label">Mã Khách hàng:</label>
                            <input type="text" class="form-control" id="customerID" name="customerID" required placeholder="Nhập mã khách hàng" />
                        </div>

                        <div class="mb-3">
                            <label for="employeeID" class="form-label">Nhân viên:</label>
<!--                            <select class="form-select" id="employeeID" name="employeeID" required>
                                <option value="">-- Chọn nhân viên --</option>
                                <c:forEach var="emp" items="${employees}">
                                    <option value="${emp.id}">${emp.fullName}</option>
                                </c:forEach>
                            </select>-->
                            <input type="text" class="form-control" id="employeeID" name="employeeID" required placeholder="Nhập mã nhân viên" />
                        </div>

                        <div class="mb-3">
                            <label for="shopID" class="form-label">Mã Cửa hàng:</label>
                            <input type="text" class="form-control" id="shopID" name="shopID" required placeholder="Nhập mã cửa hàng" />
                        </div>

                        <%-- totalAmount không cần nhập, sẽ được tính toán từ các chi tiết hóa đơn sau --%>
                        <input type="hidden" name="totalAmount" value="0" /> 

                        <div class="mb-3">
                            <label for="status" class="form-label">Trạng thái:</label>
                            <select class="form-select" id="status" name="status" required>
                                <option value="false" selected>Chưa thanh toán</option>
                                <option value="true">Đã thanh toán</option>
                            </select>
                            <small class="form-text text-muted">Thường mặc định là "Chưa thanh toán" khi mới tạo.</small>
                        </div>

                        <div class="mb-3">
                            <label for="note" class="form-label">Ghi chú (tùy chọn):</label>
                            <textarea class="form-control" id="note" name="note" rows="3" placeholder="Ghi chú về hóa đơn này"></textarea>
                        </div>

                        <button type="submit" class="btn btn-primary">Thêm Hóa đơn</button>
                        <a href="InvoiceServlet?action=list" class="btn btn-secondary">Hủy</a>
                    </form>
                </div>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
    </body>
</html>