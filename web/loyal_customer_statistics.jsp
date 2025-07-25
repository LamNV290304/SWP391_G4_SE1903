<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<c:if test="${empty sessionScope.Employee}">
    <c:redirect url="loginEmployee.jsp"/>
</c:if>
<html lang="en" class="light-style layout-menu-fixed" dir="ltr" data-theme="theme-default"
      data-assets-path="${pageContext.request.contextPath}/assets/"
      data-template="vertical-menu-template-free">
    <head>
        <meta charset="UTF-8">
        <title>Thống kê Khách hàng thân thiết - Sneat</title>
        <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/assets/img/favicon/favicon.ico" />
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/vendor/fonts/boxicons.css" />
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/vendor/css/core.css" />
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/vendor/css/theme-default.css" />
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
                            <h4 class="fw-bold py-3 mb-4"><span class="text-muted fw-light">Thống kê /</span> Khách hàng thân thiết</h4>

                            <div class="card mb-4">
                                <h5 class="card-header">Tìm kiếm Khách hàng thân thiết</h5>
                                <div class="card-body">
                                    <form action="LoyalCustomerServlet" method="get" class="row g-3 align-items-end">
                                        <input type="hidden" name="action" value="search" />
                                        <div class="col-md-3">
                                            <label for="startDate" class="form-label">Từ ngày:</label>
                                            <input type="date" class="form-control" id="startDate" name="startDate" value="${startDate}" />
                                        </div>
                                        <div class="col-md-3">
                                            <label for="endDate" class="form-label">Đến ngày:</label>
                                            <input type="date" class="form-control" id="endDate" name="endDate" value="${endDate}" />
                                        </div>

                                        <div class="col-md-3 mb-3">
                                            <label for="selectedMonth" class="form-label">Tháng:</label>
                                            <select class="form-select" id="selectedMonth" name="selectedMonth">
                                                <option value="">-- Chọn tháng --</option>
                                                <c:forEach begin="1" end="12" var="monthNum">
                                                    <option value="${monthNum}" <c:if test="${param.selectedMonth == monthNum}">selected</c:if>>
                                                        Tháng ${monthNum}
                                                    </option>
                                                </c:forEach>
                                            </select>
                                        </div>

                                        <div class="col-md-3">
                                            <label for="searchKeyword" class="form-label">Tên hoặc SĐT:</label>
                                            <input type="text" class="form-control" id="searchKeyword" name="searchKeyword" value="${searchKeyword}" placeholder="Nhập tên hoặc số điện thoại" />
                                        </div>
                                        <div class="col-12 text-end">
                                            <button type="submit" class="btn btn-primary">Tìm kiếm</button>
                                            <a href="LoyalCustomerServlet">Đặt lại</a>
                                        </div>

                                    </form>
                                </div>
                            </div>

                            <div class="card">
                                <h5 class="card-header">Danh sách khách hàng thân thiết</h5>
                                <div class="table-responsive text-nowrap">
                                    <table class="table table-hover">
                                        <thead>
                                            <tr>
                                                <th>Mã KH</th>
                                                <th>Tên khách hàng</th>
                                                <th>SĐT</th>
                                                <th>Email</th>
                                                <th>Số lần mua</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach var="c" items="${topCustomers}">
                                                <tr>
                                                    <td>${c.customerId}</td>
                                                    <td>${c.customerName}</td>
                                                    <td>${c.phone}</td>
                                                    <td>${c.email}</td>
                                                    <td><fmt:formatNumber value="${c.purchaseCount}" pattern="#,##0" /></td>
                                                </tr>
                                            </c:forEach>
                                            <c:if test="${empty topCustomers}">
                                                <tr><td colspan="5" class="text-center">Không tìm thấy khách hàng nào.</td></tr>
                                            </c:if>
                                        </tbody>
                                    </table>
                                </div>
                                <div class="card-footer d-flex justify-content-center">
                                    <nav aria-label="Page navigation">
                                        <c:if test="${totalPages > 1}">
                                            <ul class="pagination">
                                                <li class="page-item <c:if test='${currentPage == 1}'>disabled</c:if>">
                                                        <a class="page-link"
                                                           href="LoyalCustomerServlet?action=search&startDate=${startDate}&endDate=${endDate}&searchKeyword=${searchKeyword}&page=${currentPage - 1}">
                                                        <i class="tf-icon bx bx-chevron-left"></i>
                                                    </a>
                                                </li>

                                                <c:forEach begin="1" end="${totalPages}" var="i">
                                                    <li class="page-item <c:if test='${i == currentPage}'>active</c:if>">
                                                            <a class="page-link"
                                                               href="LoyalCustomerServlet?action=search&startDate=${startDate}&endDate=${endDate}&searchKeyword=${searchKeyword}&page=${i}">${i}</a>
                                                    </li>
                                                </c:forEach>

                                                <li class="page-item <c:if test='${currentPage == totalPages}'>disabled</c:if>">
                                                        <a class="page-link"
                                                           href="LoyalCustomerServlet?action=search&startDate=${startDate}&endDate=${endDate}&searchKeyword=${searchKeyword}&page=${currentPage + 1}">
                                                        <i class="tf-icon bx bx-chevron-right"></i>
                                                    </a>
                                                </li>
                                            </ul>
                                        </c:if>
                                    </nav>
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

        <!-- Scripts -->
        <script src="${pageContext.request.contextPath}/assets/vendor/libs/jquery/jquery.js"></script>
        <script src="${pageContext.request.contextPath}/assets/vendor/libs/popper/popper.js"></script>
        <script src="${pageContext.request.contextPath}/assets/vendor/js/bootstrap.js"></script>
        <script src="${pageContext.request.contextPath}/assets/vendor/libs/perfect-scrollbar/perfect-scrollbar.js"></script>
        <script src="${pageContext.request.contextPath}/assets/vendor/js/menu.js"></script>
        <script src="${pageContext.request.contextPath}/assets/js/main.js"></script>
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
    </body>
</html>
