<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html
    lang="vi"
    class="light-style layout-menu-fixed"
    dir="ltr"
    data-theme="theme-default"
    data-assets-path="${pageContext.request.contextPath}/assets/"
    data-template="vertical-menu-template-free"
    >
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0"/>
        <title>Quản lý Khách hàng - SaleShape</title>

        <meta name="description" content="" />

        <link rel="icon" type="image/x-xicon" href="${pageContext.request.contextPath}/img/logoSale.png"/>

        <link rel="preconnect" href="https://fonts.googleapis.com" />
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
        <link
            href="https://fonts.googleapis.com/css2?family=Public+Sans:ital,wght@0,300;0,400;0,500;0,600;0,700;1,300;1,400;1,500;1,600;1,700&display=swap"
            rel="stylesheet"
            />
        <link href="https://fonts.googleapis.com/css2?family=Roboto&display=swap&subset=vietnamese" rel="stylesheet">


        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/vendor/fonts/boxicons.css" />

        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/vendor/css/core.css" class="template-customizer-core-css" />
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/vendor/css/theme-default.css" class="template-customizer-theme-css" />
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/demo.css" />
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/custom.css" />


        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/vendor/libs/perfect-scrollbar/perfect-scrollbar.css" />
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/vendor/libs/apex-charts/apex-charts.css" />
        <script src="${pageContext.request.contextPath}/assets/vendor/js/helpers.js"></script>

        <script src="${pageContext.request.contextPath}/assets/js/config.js"></script>
    </head>

    <body>
        <div class="layout-wrapper layout-content-navbar">
            <div class="layout-container">
                <%-- Sidebar --%>
                <jsp:include page="sidebar.jsp" />

                <div class="layout-page">
                    <%-- Navbar --%>
                    <jsp:include page="navBar.jsp" />

                    <div class="content-wrapper">
                        <div class="container-xxl flex-grow-1 container-p-y">
                            <div class="row">
                                <div class="col-lg-12">
                                    <h4 class="fw-bold py-3 mb-4">
                                        <span class="text-muted fw-light">Quản lý /</span> Khách hàng
                                    </h4>

                                    <div class="card">
                                        <h5 class="card-header">Danh sách Khách hàng</h5>
                                        <div class="card-body">
<!--                                            <a href="CustomerServlet?action=add" class="btn btn-primary mb-3">Thêm khách hàng mới</a>-->

                                            <form action="CustomerServlet" method="get" class="mb-3">
                                                <input type="hidden" name="action" value="list">
                                                <div class="input-group">
                                                    <input type="text" class="form-control" name="searchKeyword" placeholder="Tìm kiếm theo tên, SĐT, email..." value="${param.searchKeyword}">
                                                    <button class="btn btn-outline-secondary" type="submit">Tìm kiếm</button>
                                                </div>
                                            </form>

                                            <c:if test="${not empty requestScope.successMessage}">
                                                <div class="alert alert-success alert-dismissible fade show mt-3" role="alert">
                                                    ${requestScope.successMessage}
                                                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                                                </div>
                                            </c:if>
                                            <c:if test="${not empty requestScope.errorMessage}">
                                                <div class="alert alert-danger alert-dismissible fade show mt-3" role="alert">
                                                    ${requestScope.errorMessage}
                                                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                                                </div>
                                            </c:if>
                                            <c:if test="${not empty requestScope.customers}">
                                                <div class="table-responsive text-nowrap">
                                                    <table class="table table-hover">
                                                        <thead>
                                                            <tr>
                                                                <th>ID</th>
                                                                <th>Tên Khách hàng</th>
                                                                <th>Điện thoại</th>
                                                                <th>Email</th>
                                                                <th>Địa chỉ</th>
                                                                <th>Trạng thái</th>
                                                                <th>Ngày tạo</th>
                                                                <th>Người tạo</th>
                                                                <th>Hành động</th>
                                                            </tr>
                                                        </thead>
                                                        <tbody class="table-border-bottom-0">
                                                            <c:forEach var="customer" items="${requestScope.customers}">
                                                                <tr>
                                                                    <td><i class="fab fa-angular fa-lg text-danger me-3"></i> <strong>${customer.customerID}</strong></td>
                                                                    <td>${customer.customerName}</td>
                                                                    <td>${customer.phone}</td>
                                                                    <td>${customer.email}</td>
                                                                    <td>${customer.address}</td>
                                                                    <td>
                                                                        <span class="badge ${customer.status ? 'bg-label-success' : 'bg-label-secondary'} me-1">
                                                                            ${customer.status ? "Hoạt động" : "Vô hiệu hóa"}
                                                                        </span>
                                                                    </td>
                                                                    <td>${customer.createdDate}</td>
                                                                    <td>${customer.createdBy}</td>
                                                                    <td>
                                                                        <div class="dropdown">
                                                                            <button type="button" class="btn p-0 dropdown-toggle hide-arrow" data-bs-toggle="dropdown">
                                                                                <i class="bx bx-dots-vertical-rounded"></i>
                                                                            </button>
                                                                            <div class="dropdown-menu">
                                                                                <a class="dropdown-item" href="${pageContext.request.contextPath}/CustomerServlet?action=edit&id=${customer.customerID}">
                                                                                    <i class="bx bx-edit-alt me-1"></i> Sửa
                                                                                </a>
                                                                                <c:if test="${customer.status}">
                                                                                    <a class="dropdown-item" href="${pageContext.request.contextPath}/CustomerServlet?action=deactivate&id=${customer.customerID}" onclick="return confirm('Bạn có chắc chắn muốn vô hiệu hóa khách hàng này?');">
                                                                                        <i class="bx bx-archive-in me-1"></i> Vô hiệu hóa
                                                                                    </a>
                                                                                </c:if>
                                                                                <c:if test="${!customer.status}">
                                                                                    <a class="dropdown-item" href="${pageContext.request.contextPath}/CustomerServlet?action=activate&id=${customer.customerID}" onclick="return confirm('Bạn có chắc chắn muốn kích hoạt lại khách hàng này?');">
                                                                                        <i class="bx bx-check-circle me-1"></i> Kích hoạt
                                                                                    </a>
                                                                                </c:if>
                                                                                <form action="${pageContext.request.contextPath}/CustomerServlet" method="post" style="display:inline;"
                                                                                      onsubmit="return confirm('CẢNH BÁO: Bạn có chắc muốn XÓA VĨNH VIỄN khách hàng này? Hành động này không thể hoàn tác!');">
                                                                                    <input type="hidden" name="action" value="deleteCustomer"/>
                                                                                    <input type="hidden" name="id" value="${customer.customerID}"/>
                                                                                    <button type="submit" class="dropdown-item" style="background: none; border: none; width: 100%; text-align: left; padding-left: 1rem;">
                                                                                        <i class="bx bx-trash me-1"></i> Xóa
                                                                                    </button>
                                                                                </form>
                                                                            </div>
                                                                        </div>
                                                                    </td>
                                                                </tr>
                                                            </c:forEach>
                                                        </tbody>
                                                    </table>
                                                </div>
                                            </c:if>
                                            <c:if test="${empty requestScope.customers}">
                                                <p>Không có khách hàng nào để hiển thị.</p>
                                            </c:if>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <footer class="content-footer footer bg-footer-theme">
                            <div class="container-xxl d-flex flex-wrap justify-content-between py-2 flex-md-row flex-column">
                                <div>
                                    <a href="https://themeselection.com/license/" class="footer-link me-4" target="_blank">License</a>
                                    <a href="https://themeselection.com/" target="_blank" class="footer-link me-4">More Themes</a>
                                    <a href="https://themeselection.com/demo/sneat-bootstrap-html-admin-template/documentation/" target="_blank" class="footer-link me-4">Documentation</a>
                                    <a href="https://github.com/themeselection/sneat-html-admin-template-free/issues" target="_blank" class="footer-link me-4">Support</a>
                                </div>
                            </div>
                        </footer>
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
        <script src="${pageContext.request.contextPath}/assets/vendor/libs/apex-charts/apexcharts.js"></script>

        <script src="${pageContext.request.contextPath}/assets/js/main.js"></script>

        <script src="${pageContext.request.contextPath}/assets/js/dashboards-analytics.js"></script>

        <script async defer src="https://buttons.github.io/buttons.js"></script>
    </body>
</html>