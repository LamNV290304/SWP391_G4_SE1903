<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<c:if test="${empty sessionScope.Employee}">
    <c:redirect url="loginEmployee.jsp"/>
</c:if>
<html lang="en"
      class="light-style layout-menu-fixed"
      dir="ltr"
      data-theme="theme-default"
      data-assets-path="${pageContext.request.contextPath}/assets/"
      data-template="vertical-menu-template-free">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>${statisticTitle}</title>
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
                                <div class="alert alert-danger" role="alert">${errorMessage}</div>
                            </c:if>

                            <div class="card mb-4">
                                <h5 class="card-header">Lọc thống kê</h5>
                                <div class="card-body">
                                    <form id="reportForm" action="StatisticServlet" method="GET">

                                        <div class="row align-items-end">

                                            <c:choose>
                                                <c:when test="${statisticType eq 'cashierSummary'}">
                                                    <input type="hidden" name="action" value="cashierStatistic" />
                                                </c:when>
                                                <c:when test="${statisticType eq 'managerSummary'}">
                                                    <input type="hidden" name="action" value="cashierStatistic" />
                                                </c:when>
                                                <c:otherwise>
                                                    <input type="hidden" name="action" value="saleStatistic" />
                                                </c:otherwise>
                                            </c:choose>



                                            <c:if test="${userRole eq 'Admin'}">
                                                <div class="col-md-3 mb-3">
                                                    <label for="shopSelect" class="form-label">Chọn cửa hàng:</label>
                                                    <select class="form-select" id="shopSelect" name="shopId">
                                                        <option value="all" ${selectedShopId == 'all' || selectedShopId == null ? 'selected' : ''}>
                                                            Tất cả cửa hàng
                                                        </option>

                                                        <c:forEach var="shop" items="${allShops}">
                                                            <option value="${shop.shopID}" ${selectedShopId != null && selectedShopId eq shop.shopID.toString() ? 'selected' : ''}>
                                                                ${shop.shopName}
                                                            </option>

                                                        </c:forEach>
                                                    </select>
                                                </div>
                                            </c:if>


                                            <c:if test="${userRole eq 'Admin' || userRole eq 'Manager' || userRole eq 'Cashier'}">
                                                <div class="col-md-3 mb-3">
                                                    <label for="employeeSelect" class="form-label">Chọn nhân viên:</label>
                                                    <select class="form-select" id="employeeSelect" name="employeeId">
                                                        <option value="all" ${selectedEmployeeId == 'all' || selectedEmployeeId == null ? 'selected' : ''}>Tất cả</option>
                                                        <c:forEach var="employee" items="${filterableEmployees}">
                                                            <option value="${employee.id}" ${selectedEmployeeId eq String.valueOf(employee.id) ? 'selected' : ''}>
                                                                ${employee.fullname}
                                                            </option>
                                                        </c:forEach>
                                                    </select>
                                                </div>
                                            </c:if>


                                            <c:if test="${userRole eq 'Sale'}">
                                                <input type="hidden" name="employeeId" value="${selectedEmployeeId}" />
                                            </c:if>

                                            <div class="col-md-3 mb-3">
                                                <label for="selectedMonth" class="form-label">Tháng:</label>
                                                <select class="form-select" id="selectedMonth" name="selectedMonth">
                                                    <option value="">-- Chọn tháng --</option> <%-- Option để không chọn tháng --%>
                                                    <c:forEach begin="1" end="12" var="monthNum">
                                                        <option value="${monthNum}" ${param.selectedMonth == monthNum ? 'selected' : ''}>
                                                            Tháng ${monthNum}
                                                        </option>
                                                    </c:forEach>
                                                </select>
                                            </div>

                                            <div class="col-md-3 mb-3">
                                                <label for="startDate" class="form-label">Từ ngày:</label>
                                                <input type="date" class="form-control" id="startDate" name="startDate"
                                                       value="${not empty startDate ? startDate : ''}"
                                                       max="<%= new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date()) %>">
                                            </div>
                                            <div class="col-md-3 mb-3">
                                                <label for="endDate" class="form-label">Đến ngày:</label>
                                                <input type="date" class="form-control" id="endDate" name="endDate"
                                                       value="${not empty endDate ? endDate : ''}"
                                                       max="<%= new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date()) %>">
                                            </div>

                                            <div class="col-md-auto mb-3">
                                                <button type="submit" class="btn btn-primary me-2">Lọc</button>

                                                <a href="StatisticServlet?action=<c:out value="${statisticType eq 'cashierSummary' ? 'cashierStatistic' : 'saleStatistic'}" />" class="btn btn-outline-secondary">Xem tổng cộng</a>
                                            </div>
                                        </div>
                                    </form>


                                    <form action="StatisticServlet" method="get" id="exportForm">
                                        <input type="hidden" name="action" value="export" />

                                        <c:choose>
                                            <c:when test="${statisticType eq 'cashierSummary' || statisticType eq 'managerSummary'}">
                                                <input type="hidden" name="statisticType" value="cashierSummary" />
                                            </c:when>
                                            <c:when test="${statisticType eq 'saleProductDetail'}">
                                                <input type="hidden" name="statisticType" value="saleProductDetail" />
                                            </c:when>
                                            <c:when test="${statisticType eq 'all'}">
                                                <input type="hidden" name="statisticType" value="all" />
                                            </c:when>
                                        </c:choose>

                                        <input type="hidden" name="selectedEmployeeId" value="${not empty selectedEmployeeId ? selectedEmployeeId : ''}" />

                                        <c:if test="${not empty selectedShopId}">
                                            <input type="hidden" name="shopId" value="${selectedShopId}" />
                                        </c:if>
                                        <c:if test="${shopId eq 'all'}">
                                            <input type="hidden" name="shopId" value="all" />
                                        </c:if>

                                        <input type="hidden" name="selectedMonth" value="${not empty selectedMonth ? selectedMonth : ''}" />
                                        <input type="hidden" name="startDate" value="${not empty startDate ? startDate : ''}" />
                                        <input type="hidden" name="endDate" value="${not empty endDate ? endDate : ''}" />

                                        <button type="submit" class="btn btn-outline-primary mt-2">Xuất Excel</button>
                                    </form>
                                </div>
                            </div>

                            <div class="card mb-4">
                                <h5 class="card-header">${statisticTitle}</h5>
                                <div class="table-responsive text-nowrap">
                                    <table class="table card-table">

                                        <c:choose>
                                            <c:when test="${statisticType eq 'cashierSummary' or statisticType eq 'managerSummary'}">
                                                <thead>
                                                    <tr>
                                                        <th>STT</th>
                                                        <th>Mã NV</th>
                                                        <th>Tên nhân viên</th>
                                                        <th>Tổng doanh thu</th>
                                                        <th>Tổng đơn hàng</th>
                                                        <th>Doanh thu trung bình/Đơn hàng</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <c:choose>
                                                        <c:when test="${not empty salesStatistics}">
                                                            <c:forEach var="stat" items="${salesStatistics}" varStatus="loop">
                                                                <tr>
                                                                    <td>${loop.index + 1}</td>
                                                                    <td><strong>${stat.employeeID}</strong></td>
                                                                    <td>${stat.fullName}</td>
                                                                    <td><fmt:formatNumber value="${stat.totalRevenue}" pattern="#,##0" /> VNĐ</td>
                                                                    <td>${stat.totalOrders}</td>
                                                                    <td><fmt:formatNumber value="${stat.averageRevenuePerOrder}" pattern="#,##0" /> VNĐ</td>
                                                                </tr>
                                                            </c:forEach>
                                                            <c:if test="${not empty overallTotalRevenue and not empty overallTotalOrders}">
                                                                <tr>
                                                                    <td colspan="3" class="text-end"><strong>Tổng cộng:</strong></td>
                                                                    <td><strong><fmt:formatNumber value="${overallTotalRevenue}" pattern="#,##0" /> VNĐ</strong></td>
                                                                    <td><strong>${overallTotalOrders}</strong></td>
                                                                    <td></td>
                                                                </tr>
                                                            </c:if>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <tr>
                                                                <td colspan="6" class="text-center">Không có dữ liệu thống kê doanh số.</td>
                                                            </tr>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </tbody>
                                            </c:when>

                                            <c:when test="${statisticType eq 'saleProductDetail'}">
                                                <thead>
                                                    <tr>
                                                        <th>STT</th>
                                                        <th>Mã sản phẩm</th>
                                                        <th>Tên sản phẩm</th>
                                                        <th>Số lượng đã bán</th>
                                                        <th>Giá đơn vị trung bình</th>
                                                        <th>Thành tiền</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <c:choose>
                                                        <c:when test="${not empty productSaleStatistics}">
                                                            <c:forEach var="productDetail" items="${productSaleStatistics}" varStatus="loop">
                                                                <tr>
                                                                    <td>${loop.index + 1}</td>
                                                                    <td><strong>${productDetail.productID}</strong></td>
                                                                    <td>${productDetail.productName}</td>
                                                                    <td>${productDetail.quantitySold}</td>
                                                                    <td><fmt:formatNumber value="${productDetail.unitPrice}" pattern="#,##0" /> VNĐ</td>
                                                                    <td><fmt:formatNumber value="${productDetail.amount}" pattern="#,##0" /> VNĐ</td>
                                                                </tr>
                                                            </c:forEach>

                                                            <c:if test="${not empty totalSoldRevenue and not empty totalQuantitySold}">
                                                                <tr>
                                                                    <td colspan="3" class="text-end"><strong>Tổng cộng:</strong></td>
                                                                    <td><strong>${totalQuantitySold}</strong></td>
                                                                    <td></td>
                                                                    <td><strong><fmt:formatNumber value="${totalSoldRevenue}" pattern="#,##0" /> VNĐ</strong></td>
                                                                </tr>
                                                            </c:if>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <tr>
                                                                <td colspan="6" class="text-center">Không có dữ liệu chi tiết sản phẩm đã bán.</td>
                                                            </tr>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </tbody>
                                            </c:when>

                                            <c:otherwise>

                                                <tbody>
                                                    <tr>
                                                        <td colspan="6" class="text-center">Vui lòng chọn loại thống kê để hiển thị.</td>
                                                    </tr>
                                                </tbody>
                                            </c:otherwise>
                                        </c:choose>
                                    </table>
                                </div>
                                <nav aria-label="Page navigation">
                                    <ul class="pagination justify-content-center">

                                        <c:if test="${currentPage > 1}">
                                            <li class="page-item">
                                                <a class="page-link"
                                                   href="StatisticServlet?action=${param.action}&page=${currentPage - 1}&employeeId=${selectedEmployeeId}&shopId=${selectedShopId}&startDate=${startDate}&endDate=${endDate}&selectedMonth=${param.selectedMonth}">
                                                    «
                                                </a>
                                            </li>
                                        </c:if>

                                        <c:forEach begin="1" end="${totalPages}" var="i">
                                            <li class="page-item ${i == currentPage ? 'active' : ''}">
                                                <a class="page-link"
                                                   href="StatisticServlet?action=${param.action}&page=${i}&employeeId=${selectedEmployeeId}&shopId=${selectedShopId}&startDate=${startDate}&endDate=${endDate}&selectedMonth=${param.selectedMonth}">
                                                    ${i}
                                                </a>
                                            </li>
                                        </c:forEach>

                                        <c:if test="${currentPage < totalPages}">
                                            <li class="page-item">
                                                <a class="page-link"
                                                   href="StatisticServlet?action=${param.action}&page=${currentPage + 1}&employeeId=${selectedEmployeeId}&shopId=${selectedShopId}&startDate=${startDate}&endDate=${endDate}&selectedMonth=${param.selectedMonth}">
                                                    »
                                                </a>
                                            </li>
                                        </c:if>

                                    </ul>
                                </nav>



                            </div>
                        </div>

                        <jsp:include page="footer.jsp" />
                        <div class="content-backdrop fade"></div>
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
                document.addEventListener('DOMContentLoaded', function () {
                    const selectedMonthSelect = document.getElementById('selectedMonth');
                    const startDateInput = document.getElementById('startDate');
                    const endDateInput = document.getElementById('endDate');


                    selectedMonthSelect.addEventListener('change', function () {
                        if (this.value !== "") {
                            startDateInput.value = '';
                            endDateInput.value = '';
                        }
                    });

                    startDateInput.addEventListener('change', function () {
                        if (this.value !== "") {
                            selectedMonthSelect.value = '';
                        }
                    });

                    endDateInput.addEventListener('change', function () {
                        if (this.value !== "") {
                            selectedMonthSelect.value = '';
                        }
                    });
                });
            </script>
        </div>
    </body>
</html>