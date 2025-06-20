<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
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
        <meta charset="UTF-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
        <title>Danh sách Hóa đơn</title>

        <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/assets/img/favicon/favicon.ico" />
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/vendor/fonts/boxicons.css" />
        <link href="https://fonts.googleapis.com/css2?family=Public+Sans:wght@300;400;500;600;700&display=swap" rel="stylesheet" />
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/vendor/css/core.css" />
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/vendor/css/theme-default.css" />
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/demo.css" />
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/vendor/libs/perfect-scrollbar/perfect-scrollbar.css" />
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" />

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
                        <div class="container mt-4">

                            <h2 class="text-center mb-4">Quản lý Hóa đơn</h2>

                            <div class="card mb-4">
                                <h5 class="card-header">Tìm kiếm Hóa đơn</h5>
                                <div class="card-body">
                                    <form method="get" action="InvoiceServlet">
                                        <input type="hidden" name="action" value="search" />
                                        <div class="mb-3">
                                            <label for="searchQuery" class="form-label">Tìm theo Mã HĐ hoặc Tên Khách hàng:</label>
                                            <input type="text" class="form-control" id="searchQuery" name="invoiceID"
                                                   placeholder="Nhập mã hóa đơn hoặc tên khách hàng"
                                                   value="${not empty requestScope.searchQuery ? requestScope.searchQuery : ''}" />
                                        </div>
                                        <button type="submit" class="btn btn-primary">Tìm kiếm</button>
                                    </form>
                                    <%-- Hiển thị thông báo lỗi hoặc thành công ngay dưới form tìm kiếm --%>
                                    <c:if test="${not empty errorMessage}">
                                        <div class="alert alert-danger mt-3" role="alert">
                                            ${errorMessage}
                                        </div>
                                    </c:if>
                                    <c:if test="${not empty successMessage}">
                                        <div class="alert alert-success mt-3" role="alert">
                                            ${successMessage}
                                        </div>
                                    </c:if>
                                </div>
                            </div>

                            <div class="mb-3 text-end">
                                <a href="InvoiceServlet?action=showAddForm" class="btn btn-success">Thêm hóa đơn mới</a>
                            </div>

                            <h3 class="text-center mb-3">Danh sách Hóa đơn</h3>
                            <div class="table-responsive">
                                <table class="table table-bordered">
                                    <thead>
                                        <tr class="text-nowrap">
                                            <th>Mã HĐ</th>
                                            <th>Khách hàng</th>
                                            <th>Shop</th>
                                            <th>Ngày tạo</th>
                                            <th>Tổng tiền</th>
                                            <th>Trạng thái</th>
                                            <th>Ghi chú</th>
                                            <th>Thao tác</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="inv" items="${invoiceList}">
                                            <tr>
                                                <td>${inv.invoiceID}</td>
                                                <td>${inv.customerName}</td>
                                                <td>${inv.shopName}</td>
                                                <td><fmt:formatDate value="${inv.invoiceDate}" pattern="dd/MM/yyyy HH:mm:ss" /></td>
                                                <td><fmt:formatNumber value="${inv.totalAmount}" pattern="#,##0" /> VNĐ</td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${inv.status}">
                                                            <span class="badge bg-success">Đã thanh toán <i class='bx bx-check-circle'></i></span>
                                                            </c:when>
                                                            <c:otherwise>
                                                            <form action="InvoiceServlet" method="post" style="display:inline-block;">
                                                                <input type="hidden" name="action" value="markAsPaid" />
                                                                <input type="hidden" name="invoiceID" value="${inv.invoiceID}" />
                                                                <span class="badge bg-warning text-dark me-2">Chưa thanh toán</span>
                                                                <button type="submit" class="btn btn-success btn-sm" title="Đánh dấu đã thanh toán">
                                                                    <i class='bx bx-check'></i>
                                                                </button>
                                                            </form>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td>${inv.note}</td>
                                                <td>
                                                    <a href="InvoiceServlet?action=listDetail&invoiceID=${inv.invoiceID}" class="btn btn-primary btn-sm me-1">Chi tiết</a>
                                                    <form method="post" action="InvoiceServlet" style="display:inline;">
                                                        <input type="hidden" name="action" value="delete" />
                                                        <input type="hidden" name="invoiceID" value="${inv.invoiceID}" />
                                                        <button type="submit" class="btn btn-danger btn-sm" onclick="return confirm('Bạn có chắc muốn xóa hóa đơn này?');"
                                                                <c:if test="${inv.status}">disabled</c:if>>
                                                                    Xóa
                                                                </button>
                                                        </form>
                                                    </td>
                                                </tr>
                                        </c:forEach>
                                        <c:if test="${empty invoiceList}">
                                            <tr>
                                                <td colspan="8" class="text-center">Không có hóa đơn nào để hiển thị.</td>
                                            </tr>
                                        </c:if>
                                    </tbody>
                                </table>
                            </div>

                            <div class="pagination-container">
                                <c:if test="${totalPages > 1}">
                                    <%-- Khởi tạo baseLink một lần --%>
                                    <c:url var="baseLink" value="InvoiceServlet">
                                        <%-- Điều kiện: Nếu có searchQuery, thì action là search và truyền lại searchQuery --%>
                                        <c:if test="${not empty searchQuery}">
                                            <c:param name="action" value="search" />
                                            <c:param name="searchQuery" value="${searchQuery}" />
                                        </c:if>
                                        <%-- Ngược lại (không có searchQuery), thì action là list --%>
                                        <c:if test="${empty searchQuery}">
                                            <c:param name="action" value="list" />
                                        </c:if>
                                    </c:url>

                                    <c:if test="${currentPage > 1}">
                                        <a href="${baseLink}&page=${currentPage - 1}" class="btn btn-outline-primary">&laquo; Trang trước</a>
                                    </c:if>
                                    <c:forEach begin="1" end="${totalPages}" var="i">
                                        <c:choose>
                                            <c:when test="${i == currentPage}">
                                                <span class="btn btn-primary disabled">${i}</span>
                                            </c:when>
                                            <c:otherwise>
                                                <a href="${baseLink}&page=${i}" class="btn btn-outline-primary">${i}</a>
                                            </c:otherwise>
                                        </c:choose>
                                    </c:forEach>
                                    <c:if test="${currentPage < totalPages}">
                                        <a href="${baseLink}&page=${currentPage + 1}" class="btn btn-outline-primary">Trang sau &raquo;</a>
                                    </c:if>
                                </c:if>
                            </div>

                        </div>
                    </div>
                </div>
            </div>

            <%-- Bootstrap Bundle with Popper --%>
            <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>

            <script src="${pageContext.request.contextPath}/assets/vendor/libs/jquery/jquery.js"></script>
            <script src="${pageContext.request.contextPath}/assets/vendor/libs/popper/popper.js"></script>
            <script src="${pageContext.request.contextPath}/assets/vendor/js/bootstrap.js"></script>
            <script src="${pageContext.request.contextPath}/assets/vendor/libs/perfect-scrollbar/perfect-scrollbar.js"></script>
            <script src="${pageContext.request.contextPath}/assets/vendor/js/menu.js"></script>
            <script src="${pageContext.request.contextPath}/assets/js/main.js"></script>
            <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>

    </body>
</html>