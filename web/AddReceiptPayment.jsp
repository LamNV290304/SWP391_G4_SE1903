<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<c:if test="${empty sessionScope.Employee}">
    <c:redirect url="loginEmployee.jsp"/>
</c:if>
<html
    lang="vi"
    class="light-style layout-menu-fixed"
    dir="ltr"
    data-theme="theme-default"
    data-assets-path="./assets/"
    data-template="vertical-menu-template-free"
    >
    <head>
        <title>SaleShape</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link href="https://fonts.googleapis.com/css2?family=Roboto&display=swap&subset=vietnamese" rel="stylesheet">

        <link href="https://fonts.googleapis.com/css2?family=Roboto&display=swap" rel="stylesheet" />
        <link rel="icon" type="image/x-icon" href="img/logoSale.png" />
        <link rel="preconnect" href="https://fonts.googleapis.com" />
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
        <link
            href="https://fonts.googleapis.com/css2?family=Public+Sans:ital,wght@0,300;0,400;0,500;0,600;0,700;1,300;1,400;1,500;1,600;1,700&display=swap"
            rel="stylesheet"
            />

        <link rel="stylesheet" href="./assets/css/custom.css" />

        <link rel="stylesheet" href="./assets/vendor/fonts/boxicons.css" />

        <link rel="stylesheet" href="./assets/vendor/css/core.css" class="template-customizer-core-css" />
        <link rel="stylesheet" href="./assets/vendor/css/theme-default.css" class="template-customizer-theme-css" />
        <link rel="stylesheet" href="./assets/css/demo.css" />

        <link rel="stylesheet" href="./assets/vendor/libs/perfect-scrollbar/perfect-scrollbar.css" />

        <link rel="stylesheet" href="./assets/vendor/libs/apex-charts/apex-charts.css" />

        <script src="./assets/vendor/js/helpers.js"></script>

        <script src="./assets/js/config.js"></script>
    </head>
    <body>
        <div  class="layout-wrapper layout-content-navbar">
            <div class="layout-container">
                <jsp:include page="sidebar.jsp" />

                <div class="layout-page">
                    <jsp:include page="navBar.jsp" />
                    <div class="content-wrapper">
                        <div class="container-xxl container-p-y">
                            <h4 class="fw-bold py-3 mb-4">Thêm Phiếu Thu</h4>

                            <form action="AddReceiptPayment?action=add" method="post">
                                <div class="row">
                                    <div class="mb-3 col-md-6">
                                        <label class="form-label">Cửa hàng</label>
                                        <select name="shopID" class="form-select">
                                            <option value="">Tất cả</option>
                                            <c:forEach var="s" items="${listShop}">
                                                <option value="${s.shopID}" 
                                                        <c:if test="${paramShopID == s.shopID}">selected</c:if>>
                                                    ${s.shopName}
                                                </option>
                                            </c:forEach>
                                        </select>
                                    </div>

                                    <div class="mb-3 col-md-6">
                                        <label class="form-label">Nhân viên</label>
                                        <input type="text" name="EmployeeID" class="form-control" value="${sessionScope.Employee.id}" readonly />
                                    </div>

                                    <div class="mb-3 col-md-6">
                                        <label class="form-label">Khách hàng (nếu có)</label>
                                        <select name="customerID" class="form-control">
                                            <option value="">-- Không chọn --</option>
                                            <c:forEach var="cus" items="${listCustomer}">
                                                <option value="${cus.customerID}">${cus.customerName}</option>
                                            </c:forEach>
                                        </select>
                                    </div>

                                    <div class="mb-3 col-md-6">
                                        <label class="form-label">Ngày thu</label>
                                        <input type="datetime-local" name="receiptDate" class="form-control" required>
                                    </div>

                                    <div class="mb-3 col-md-6">
                                        <label class="form-label">Số tiền</label>
                                        <input type="number" step="0.01" name="amount" class="form-control" required>
                                    </div>

                                    <div class="mb-3 col-md-6">
                                        <label class="form-label">Loại phiếu</label>
                                        <select name="typeID" class="form-control">
                                            <c:forEach var="type" items="${listType}">
                                                <option value="${type.typeID}">${type.typeName}</option>
                                            </c:forEach>
                                        </select>
                                    </div>

                                    <div class="mb-3 col-md-6">
                                        <label class="form-label">Phương thức thanh toán</label>
                                        <select name="paymentMethodID" class="form-control">
                                            <c:forEach var="pm" items="${listPayment}">
                                                <option value="${pm.paymentMethodID}">${pm.methodName}</option>
                                            </c:forEach>
                                        </select>
                                    </div>

                                    <div class="mb-3 col-md-12">
                                        <label class="form-label">Ghi chú</label>
                                        <textarea name="note" class="form-control" rows="3"></textarea>
                                    </div>

                                    <div class="mb-3 col-md-6">
                                        <label class="form-label">Trạng thái</label>
                                        <select name="status" class="form-control">
                                            <option value="true">Kích hoạt</option>
                                            <option value="false">Ẩn</option>
                                        </select>
                                    </div>
                                </div>

                                <button type="submit" class="btn btn-primary">Lưu</button>
                                <a href="ReceiptVoucherServlet" class="btn btn-secondary">Hủy</a>
                            </form>
                        </div>

                        <script src="assets/vendor/js/bootstrap.js"></script>
                    </div>
                    <jsp:include page="footer.jsp" />
                </div>

            </div>
        </div>


        <script src="assets/vendor/libs/jquery/jquery.js"></script>
        <script src="assets/vendor/libs/popper/popper.js"></script>
        <script src="assets/vendor/js/bootstrap.js"></script>
        <script src="assets/vendor/libs/perfect-scrollbar/perfect-scrollbar.js"></script>
        <script src="assets/vendor/js/menu.js"></script> <script src="assets/js/main.js"></script> </body>
</html>