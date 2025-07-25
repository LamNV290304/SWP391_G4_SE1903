<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
        <title>Chương trình khuyến mãi - Quản lý</title>

        <!-- Sneat Core CSS -->
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/vendor/css/core.css" />
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/vendor/css/theme-default.css" />
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/demo.css" />
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/vendor/libs/perfect-scrollbar/perfect-scrollbar.css" />

        <!-- JS Helpers -->
        <script src="${pageContext.request.contextPath}/assets/vendor/js/helpers.js"></script>
    </head>

    <body>
        <div class="layout-wrapper layout-content-navbar">
            <div class="layout-container">
                <!-- Sidebar -->
                <jsp:include page="sidebar.jsp" />

                <!-- Main layout -->
                <div class="layout-page">
                    <!-- Navbar -->
                    <jsp:include page="navBar.jsp" />

                    <!-- Content wrapper -->
                    <div class="content-wrapper">
                        <!-- Content -->
                        <div class="container-xxl flex-grow-1 container-p-y">
                            <h4 class="fw-bold py-3 mb-4">
                                <span class="text-muted fw-light">Quản lý /</span> Chương trình khuyến mãi
                            </h4>
                            <form method="get" action="PromotionServlet" class="row g-3 mb-4">
                                <input type="hidden" name="action" value="list" />

                                <div class="col-md-4">
                                    <label for="searchKeyword" class="form-label">Tên khuyến mãi:</label>
                                    <input type="text" class="form-control" name="searchKeyword" id="searchKeyword" value="${searchKeyword}" />
                                </div>

                                <div class="col-md-3">
                                    <label for="month" class="form-label">Tháng:</label>
                                    <select name="month" class="form-select" id="month">
                                        <option value="">Tất cả</option>
                                        <c:forEach begin="1" end="12" var="m">
                                            <option value="${m}" <c:if test="${selectedMonth == m}">selected</c:if>>Tháng ${m}</option>
                                        </c:forEach>
                                    </select>
                                </div>

                                <div class="col-md-2 align-self-end">
                                    <button type="submit" class="btn btn-primary">Lọc</button>
                                </div>
                            </form>
                            <div class="card">
                                <div class="card-header d-flex justify-content-between align-items-center">
                                    <h5 class="mb-0">Danh sách chương trình</h5>
                                    <a href="PromotionServlet?action=new" class="btn btn-primary">Thêm mới</a>
                                </div>


                                <div class="table-responsive text-nowrap">
                                    <table class="table table-hover">
                                        <thead>
                                            <tr>
                                                <th>STT</th>
                                                <th>Tên chương trình</th>
                                                <th>Danh mục</th> 
                                                <th>Tỷ lệ giảm (%)</th> 
                                                <th>Từ ngày</th>
                                                <th>Đến ngày</th>
                                                <th>Trạng thái</th>
                                                <th>Hành động</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach var="p" items="${promotions}" varStatus="loop">
                                                <tr>
                                                    <td>${loop.index + 1}</td>
                                                    <td>${p.promotionName}</td>
                                                    <td>${p.categoryName}</td> <!-- mới -->
                                                    <td><fmt:formatNumber value="${p.discountRate}" pattern="#0.##" /></td>
                                                    <td><fmt:formatDate value="${p.startDate}" pattern="dd/MM/yyyy" /></td>
                                                    <td><fmt:formatDate value="${p.endDate}" pattern="dd/MM/yyyy" /></td>
                                                    <td>
                                                        <span class="badge bg-${p.status ? 'success' : 'secondary'}">
                                                            ${p.status ? 'Đang hoạt động' : 'Không hoạt động'}
                                                        </span>
                                                    </td>
                                                    <td>
                                                        <a href="PromotionServlet?action=edit&id=${p.promotionId}" class="btn btn-sm btn-warning">Sửa</a>
                                                        <a href="PromotionServlet?action=delete&id=${p.promotionId}" class="btn btn-sm btn-danger" onclick="return confirm('Xác nhận xoá?');">Xoá</a>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                            <c:if test="${empty promotions}">
                                                <tr>
                                                    <td colspan="8" class="text-center text-muted">Không có chương trình nào.</td>
                                                </tr>
                                            </c:if>
                                        </tbody>
                                    </table>
                                    <div class="mt-3 text-center">
                                        <nav>
                                            <ul class="pagination justify-content-center">
                                                <c:if test="${currentPage > 1}">
                                                    <li class="page-item">
                                                        <a class="page-link" href="PromotionServlet?action=list&page=${currentPage - 1}&searchKeyword=${searchKeyword}&month=${selectedMonth}">«</a>
                                                    </li>
                                                </c:if>

                                                <c:forEach begin="1" end="${totalPages}" var="i">
                                                    <li class="page-item ${i == currentPage ? 'active' : ''}">
                                                        <a class="page-link" href="PromotionServlet?action=list&page=${i}&searchKeyword=${searchKeyword}&month=${selectedMonth}">${i}</a>
                                                    </li>
                                                </c:forEach>

                                                <c:if test="${currentPage < totalPages}">
                                                    <li class="page-item">
                                                        <a class="page-link" href="PromotionServlet?action=list&page=${currentPage + 1}&searchKeyword=${searchKeyword}&month=${selectedMonth}">»</a>
                                                    </li>
                                                </c:if>
                                            </ul>
                                        </nav>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <!-- /Content -->

                        <!-- Footer -->
                        <jsp:include page="footer.jsp" />
                        <div class="content-backdrop fade"></div>
                    </div>
                    <!-- /Content wrapper -->
                </div>
                <!-- /Layout page -->
            </div>
        </div>

        <!-- Core JS -->
        <script src="${pageContext.request.contextPath}/assets/vendor/libs/jquery/jquery.js"></script>
        <script src="${pageContext.request.contextPath}/assets/vendor/libs/popper/popper.js"></script>
        <script src="${pageContext.request.contextPath}/assets/vendor/js/bootstrap.js"></script>
        <script src="${pageContext.request.contextPath}/assets/vendor/libs/perfect-scrollbar/perfect-scrollbar.js"></script>
        <script src="${pageContext.request.contextPath}/assets/vendor/js/menu.js"></script>
        <script src="${pageContext.request.contextPath}/assets/js/main.js"></script>
    </body>
</html>
