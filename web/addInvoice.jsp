<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

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
        <title>Thêm Hóa đơn mới - Sneat</title>

        <link rel="icon" type="image/x-x-icon" href="${pageContext.request.contextPath}/assets/img/favicon/favicon.ico" />

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
                            <h4 class="fw-bold py-3 mb-4"><span class="text-muted fw-light">Hóa đơn /</span> Thêm mới</h4>

                            <c:if test="${not empty errorMessage}">
                                <div class="alert alert-danger" role="alert">
                                    ${errorMessage}
                                </div>
                            </c:if>

                            <div class="card mb-4">
                                <h5 class="card-header">Thông tin Hóa đơn mới</h5>
                                <div class="card-body">

                                    <form id="addInvoiceForm" method="post" action="InvoiceServlet">
                                        <input type="hidden" name="action" value="add" />

                                        <%-- Shop Selection --%>
                                        <div class="mb-3">
                                            <label for="shopID" class="form-label">Cửa hàng:</label>
                                            <select class="form-select" id="shopID" name="shopID" required>
                                                <option value="">-- Chọn cửa hàng --</option>
                                                <c:forEach var="shop" items="${allShops}">
                                                    <option value="${shop.shopID}" ${param_shopID == shop.shopID ? 'selected' : ''}>${shop.shopName}</option>
                                                </c:forEach>
                                            </select>

                                            <button type="button" class="btn btn-sm btn-info mt-2" onclick="document.getElementById('updateEmployeeForm').submit()">Cập nhật danh sách nhân viên</button>
                                        </div>

                                        <div class="mb-3">
                                            <label for="employeeID" class="form-label">Nhân viên:</label>
                                            <select class="form-select" id="employeeID" name="employeeID" required>
                                                <option value="">-- Chọn nhân viên --</option>
                                                <c:forEach var="employee" items="${employees}">
                                                    <option value="${employee.id}" ${param_employeeID == employee.id ? 'selected' : ''}>
                                                        ${employee.fullname}
                                                    </option>
                                                </c:forEach>
                                            </select>
                                        </div>

                                        <div class="mb-3">
                                            <label for="note" class="form-label">Ghi chú (tùy chọn):</label>
                                            <textarea class="form-control" id="note" name="note" rows="3" placeholder="Ghi chú về hóa đơn này">${param_note}</textarea>
                                        </div>

                                        <c:set var="defaultVatRateId" value="-1"/>
                                        <c:forEach var="vatRate" items="${vatRatesList}">
                                            <c:if test="${vatRate.rate == 0.10}">
                                                <c:set var="defaultVatRateId" value="${vatRate.VATRateID}"/>
                                            </c:if>
                                        </c:forEach>
                                        <input type="hidden" name="vatRateID" value="${defaultVatRateId}" />


                                        <button type="submit" class="btn btn-primary me-2">
                                            <i class='bx bx-check me-1'></i> Tiếp tục tạo hóa đơn
                                        </button>
                                        <a href="InvoiceServlet?action=list" class="btn btn-secondary">
                                            <i class='bx bx-arrow-back me-1'></i> Hủy
                                        </a>
                                    </form>

                                    <form id="updateEmployeeForm" method="get" action="InvoiceServlet" style="display: none;">
                                        <input type="hidden" name="action" value="showAddForm" />
                                        <input type="hidden" name="shopID" id="hiddenShopIDForUpdate" />
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

        <script>
                                                // JavaScript đơn giản để cập nhật giá trị của hiddenShopIDForUpdate khi shopID thay đổi
                                                document.getElementById('shopID').addEventListener('change', function () {
                                                    document.getElementById('hiddenShopIDForUpdate').value = this.value;
                                                });
        </script>
    </body>
</html>