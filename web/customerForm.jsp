<%-- customerForm.jsp --%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="en"
      class="light-style layout-menu-fixed"
      dir="ltr"
      data-theme="theme-default"
      data-assets-path="${pageContext.request.contextPath}/assets/"
      data-template="vertical-menu-template-free">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
        <title>Thêm Hóa đơn mới - Thông tin Khách hàng</title>

        <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/assets/img/favicon/favicon.ico" />

        <link rel="preconnect" href="https://fonts.googleapis.com" />
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
        <link href="https://fonts.googleapis.com/css2?family=Public+Sans:wght@300;400;500;600;700&display=swap" rel="stylesheet" />

        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/vendor/fonts/boxicons.css" />

        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/vendor/css/core.css" class="template-customizer-core-css" />
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/vendor/css/theme-default.css" class="template-customizer-theme-css" />
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/demo.css" />

        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/vendor/libs/perfect-scrollbar/perfect-scrollbar.css" />

        <script src="${pageContext.request.contextPath}/assets/vendor/js/helpers.js"></script>

        <script src="${pageContext.request.contextPath}/assets/js/config.js"></script>
    </head>
    <body>
        <div class="layout-wrapper layout-content-navbar">
            <div class="layout-container">
                <jsp:include page="sidebar.jsp" />
                <div class="layout-page">
                    <jsp:include page="navBar.jsp" />
                    <div class="content-wrapper">
                        <div class="container-xxl flex-grow-1 container-p-y">
                            <h4 class="fw-bold py-3 mb-4">
                                <span class="text-muted fw-light">Hóa đơn /</span> Nhập thông tin Khách hàng
                            </h4>

                            <%-- Hiển thị thông báo lỗi hoặc thành công --%>
                            <c:if test="${not empty errorMessage}">
                                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                                    ${errorMessage}
                                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                                </div>
                                <c:remove var="errorMessage" scope="request"/> <%-- Xóa attribute sau khi hiển thị --%>
                            </c:if>
                            <c:if test="${not empty successMessage}">
                                <div class="alert alert-success alert-dismissible fade show" role="alert">
                                    ${successMessage}
                                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                                </div>
                                <c:remove var="successMessage" scope="request"/> <%-- Xóa attribute sau khi hiển thị --%>
                            </c:if>

                            <div class="card mb-4">
                                <h5 class="card-header">Thông tin Khách hàng</h5>
                                <div class="card-body">
                                    <form action="${pageContext.request.contextPath}/CustomerServlet" method="post">
                                        <input type="hidden" name="action" value="${customer != null ? 'updateCustomer' : 'addCustomer'}"/>
                                        <c:if test="${not empty returnInvoiceID}">
                                            <input type="hidden" name="returnInvoiceID" value="${returnInvoiceID}"/>
                                        </c:if>
                                        <c:if test="${customer != null}">
                                            <input type="hidden" name="customerID" value="${customer.customerID}"/>
                                        </c:if>

                                        <div class="mb-3">
                                            <label for="customerName" class="form-label">Tên Khách hàng:</label>
                                            <input type="text" class="form-control" id="customerName" name="customerName"
                                                   placeholder="Nhập tên khách hàng (có thể bỏ trống)"
                                                   value="${customer != null ? customer.customerName : (requestScope.customerName != null ? requestScope.customerName : '')}"/>
                                            <small class="form-text text-muted">Bỏ trống để sử dụng khách vãng lai.</small>
                                        </div>
                                        <div class="mb-3">
                                            <label for="customerPhone" class="form-label">Số điện thoại:</label>
                                            <input type="text" class="form-control" id="customerPhone" name="customerPhone"
                                                   placeholder="Nhập số điện thoại (có thể bỏ trống)"
                                                   value="${customer != null ? customer.phone : (requestScope.customerPhone != null ? requestScope.customerPhone : '')}"/>
                                            <small class="form-text text-muted">Nếu số điện thoại đã tồn tại, hệ thống sẽ sử dụng thông tin khách hàng hiện có.</small>
                                        </div>
                                        <div class="mb-3">
                                            <label for="customerAddress" class="form-label">Địa chỉ:</label>
                                            <input type="text" class="form-control" id="customerAddress" name="customerAddress"
                                                   placeholder="Nhập địa chỉ (có thể bỏ trống)"
                                                   value="${customer != null ? customer.address : (requestScope.customerAddress != null ? requestScope.customerAddress : '')}"/>
                                        </div>
                                        <div class="mb-3">
                                            <label for="customerEmail" class="form-label">Email:</label>
                                            <input type="email" class="form-control" id="customerEmail" name="customerEmail"
                                                   placeholder="Nhập email (có thể bỏ trống)"
                                                   value="${customer != null ? customer.email : (requestScope.customerEmail != null ? requestScope.customerEmail : '')}"/>
                                        </div>

                                        <button type="submit" class="btn btn-primary me-2">
                                            <i class='bx bx-right-arrow-alt me-1'></i> Thêm khách hàng
                                        </button>
                                        <a href="${pageContext.request.contextPath}/CustomerServlet?action=list" class="btn btn-secondary"> <%-- Đổi link hủy về trang danh sách khách hàng --%>
                                            <i class='bx bx-x me-1'></i> Hủy bỏ
                                        </a>
                                    </form>
                                </div>
                            </div>
                        </div>
                        <jsp:include page="footer.jsp" />
                        <div class="content-backdrop fade"></div>
                    </div>
                </div>
            </div>

            <div class="layout-overlay layout-menu-toggle"></div>
        </div>
        <script src="${pageContext.request.contextPath}/assets/vendor/libs/jquery/jquery.js"></script>
        <script src="${pageContext.request.contextPath}/assets/vendor/libs/popper/popper.js"></script>
        <script src="${pageContext.request.contextPath}/assets/vendor/js/bootstrap.js"></script>
        <script src="${pageContext.request.contextPath}/assets/vendor/libs/perfect-scrollbar/perfect-scrollbar.js"></script>

        <script src="${pageContext.request.contextPath}/assets/vendor/js/menu.js"></script>

        <script src="${pageContext.request.contextPath}/assets/js/main.js"></script>

        <script async defer src="https://buttons.github.io/buttons.js"></script>
    </body>
</html>