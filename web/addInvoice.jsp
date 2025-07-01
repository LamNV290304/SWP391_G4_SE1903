<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
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
        <title>Thêm Hóa đơn mới - Sneat</title>

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
                            <h4 class="fw-bold py-3 mb-4"><span class="text-muted fw-light">Hóa đơn /</span> Thêm mới</h4>

                            <c:if test="${not empty errorMessage}">
                                <div class="alert alert-danger" role="alert">
                                    ${errorMessage}
                                </div>
                            </c:if>

                            <div class="card mb-4">
                                <h5 class="card-header">Thông tin Hóa đơn mới</h5>
                                <div class="card-body">
                                    <form method="post" action="InvoiceServlet">
                                        <input type="hidden" name="action" value="add" />

                                        <div class="mb-3">
                                            <label for="employeeID" class="form-label">Nhân viên:</label>
                                            <select class="form-select" id="employeeID" name="employeeID" required>
                                                <option value="">-- Chọn nhân viên --</option>
                                                <c:forEach var="employee" items="${employees}">
                                                    
                                                    <option value="${employee.id}">
                                                       
                                                        ${employee.fullname} 
                                                      
                                                        <c:if test="${not empty employee.role && not empty employee.role.roleName}">
                                                            (Chức vụ: ${employee.role.roleName})
                                                        </c:if>
                                                    </option>
                                                </c:forEach>
                                            </select>
                                        </div>

                                        <div class="mb-3">
                                            <label for="shopID" class="form-label">Cửa hàng:</label>
                                            <select class="form-select" id="shopID" name="shopID" required>
                                                <option value="">-- Chọn cửa hàng --</option>
                                                <c:forEach var="shop" items="${allShops}">
                                                    <option value="${shop.shopID}">${shop.shopName}</option>
                                                </c:forEach>
                                            </select>
                                        </div>

                                        <input type="hidden" name="totalAmount" value="0" />
                                        <div class="mb-3">
                                            <label for="note" class="form-label">Ghi chú (tùy chọn):</label>
                                            <textarea class="form-control" id="note" name="note" rows="3" placeholder="Ghi chú về hóa đơn này"></textarea>
                                        </div>

                                        <button type="submit" class="btn btn-primary me-2">
                                            <i class='bx bx-check me-1'></i> Tiếp tục tạo hóa đơn
                                        </button>
                                        <a href="InvoiceServlet?action=list" class="btn btn-secondary">
                                            <i class='bx bx-arrow-back me-1'></i> Hủy
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