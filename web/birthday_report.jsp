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
        <title>Thống kê Sinh nhật khách hàng - Sneat</title>
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
                            <h4 class="fw-bold py-3 mb-4"><span class="text-muted fw-light">Thống kê /</span> Sinh nhật khách hàng</h4>

                            <div class="card mb-4">
                                <h5 class="card-header">Lọc khách hàng theo tháng sinh</h5>
                                <div class="card-body">
                                    <form action="BirthdayReportServlet" method="get" class="row g-3 align-items-end">
                                        <div class="col-md-4">  
                                            <label for="month" class="form-label">Tháng sinh:</label>
                                            <select class="form-select" id="month" name="month">
                                                <option value="">-- Chọn tháng --</option>
                                                <c:forEach begin="1" end="12" var="i">
                                                    <option value="${i}" <c:if test="${selectedMonth == i}">selected</c:if>>Tháng ${i}</option>
                                                </c:forEach>

                                            </select>
                                        </div>
                                        <div class="col-md-4">
                                            <label for="searchKeyword" class="form-label">Tên hoặc SĐT:</label>
                                            <input type="text" class="form-control" id="searchKeyword" name="searchKeyword"
                                                   value="${searchKeyword}" placeholder="Nhập tên hoặc số điện thoại" />
                                        </div>
                                        <div class="col-md-4 text-end">
                                            <button type="submit" class="btn btn-primary">Lọc</button>
                                            <a href="BirthdayReportServlet" class="btn btn-outline-secondary">Đặt lại</a>
                                        </div>
                                    </form>
                                </div>
                            </div>

                            <div class="card">
                                <h5 class="card-header">Danh sách khách hàng sinh nhật</h5>
                                <c:if test="${not empty successMessage}">
                                    <div class="alert alert-success alert-dismissible fade show m-3" role="alert">
                                        ${successMessage}
                                        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                                    </div>
                                </c:if>

                                <c:if test="${not empty error}">
                                    <div class="alert alert-danger alert-dismissible fade show m-3" role="alert">
                                        <c:out value="${error}" escapeXml="false"/>
                                        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                                    </div>
                                </c:if>
                                <form action="BirthdayReportServlet" method="post"> 
                                    <input type="hidden" name="month" value="${selectedMonth}" />
                                    <input type="hidden" name="searchKeyword" value="${searchKeyword}" />
                                    <div class="table-responsive text-nowrap">
                                        <table class="table table-hover">
                                            <thead>
                                                <tr>
                                                    <th><input type="checkbox" id="selectAll" /></th>
                                                    <th>Mã KH</th>
                                                    <th>Tên khách hàng</th>
                                                    <th>SĐT</th>
                                                    <th>Email</th>
                                                    <th>Ngày sinh</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <c:forEach var="cus" items="${customers}">
                                                    <tr>
                                                        <td><input type="checkbox" name="selectedIds" value="${cus.customerID}" class="customer-checkbox" /></td>
                                                        <td>${cus.customerID}</td>
                                                        <td>${cus.customerName}</td>
                                                        <td>${cus.phone}</td>
                                                        <td>${cus.email}</td>
                                                        <td><fmt:formatDate value="${cus.birthday}" pattern="dd/MM/yyyy"/></td>
                                                    </tr>
                                                </c:forEach>
                                                <c:if test="${empty customers}">
                                                    <tr>
                                                        <td colspan="6" class="text-center">Không tìm thấy khách hàng nào.</td>
                                                    </tr>
                                                </c:if>
                                            </tbody>
                                        </table>
                                    </div>

                                    <div class="p-3 text-end">
                                        <button type="submit" class="btn btn-success"
                                                onclick="return confirm('Bạn có chắc muốn gửi email cho các khách hàng đã chọn?')">
                                            Gửi Email
                                        </button>
                                    </div>
                                </form>
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

                                                    document.getElementById("selectAll").addEventListener("change", function () {
                                                        const checkboxes = document.querySelectorAll(".customer-checkbox");
                                                        for (const cb of checkboxes) {
                                                            cb.checked = this.checked;
                                                        }
                                                    });
            </script>
    </body>
</html>
