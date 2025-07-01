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
        <title>Thống kê Doanh số Bán hàng</title>

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
                            <h4 class="fw-bold py-3 mb-4"><span class="text-muted fw-light">Thống kê /</span> ${statisticTitle}</h4>

                            <c:if test="${not empty errorMessage}">
                                <div class="alert alert-danger" role="alert">
                                    ${errorMessage}
                                </div>
                            </c:if>
                            <div class="card mb-4">
                                <h5 class="card-header">Lọc thống kê theo thời gian</h5>
                                <div class="card-body">
                                    <form action="${pageContext.request.contextPath}/StatisticServlet" method="GET">
                                        <div class="row align-items-end">
                                            <div class="col-md-5 mb-3">
                                                <label for="startDate" class="form-label">Từ ngày:</label>
                                                <input type="date" class="form-control" id="startDate" name="startDate" 
                                                       value="${not empty startDate ? startDate : ''}" 
                                                       max="<%= new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date()) %>"
                                                       required>
                                            </div>
                                            <div class="col-md-5 mb-3">
                                                <label for="endDate" class="form-label">Đến ngày:</label>
                                                <input type="date" class="form-control" id="endDate" name="endDate" 
                                                       value="${not empty endDate ? endDate : ''}"
                                                       max="<%= new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date()) %>"
                                                       required>
                                            </div>
                                            <div class="col-md-2 mb-3">
                                                <button type="submit" class="btn btn-primary d-grid w-100">Lọc</button>
                                            </div>
                                        </div>
                                    </form>
                                    <div class="mt-3">
                                        <a href="${pageContext.request.contextPath}/StatisticServlet" class="btn btn-outline-secondary">Xem thống kê tổng cộng</a>
                                    </div>
                                </div>
                            </div>

                            <div class="card">
                                <h5 class="card-header">${statisticTitle}</h5>
                                <div class="table-responsive text-nowrap">
                                    <table class="table card-table">
                                        <thead>
                                            <tr>
                                                <th>Mã NV</th>
                                                <th>Tên nhân viên</th>
                                                <th>Tổng doanh thu</th>
                                                <th>Tổng số đơn hàng</th>
                                            </tr>
                                        </thead>
                                        <tbody class="table-border-bottom-0">
                                            <c:choose>
                                                <c:when test="${not empty salesStatistics}">
                                                    <c:forEach var="stat" items="${salesStatistics}">
                                                        <tr>
                                                            <td><i class="fab fa-angular fa-lg text-danger me-3"></i> <strong>${stat.employeeID}</strong></td>
                                                            <td>${stat.fullName}</td>
                                                            <td>${String.format("%,.0f", stat.totalRevenue)} VNĐ</td>
                                                            <td>${stat.totalOrders}</td>
                                                        </tr>
                                                    </c:forEach>
                                                </c:when>
                                                <c:otherwise>
                                                    <tr>
                                                        <td colspan="4" class="text-center">Không có dữ liệu thống kê doanh số.</td>
                                                    </tr>
                                                </c:otherwise>
                                            </c:choose>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                            <br>
                         
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