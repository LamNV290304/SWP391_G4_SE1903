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
        <title>Danh sách Hóa đơn - Sneat</title>

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
                            <h4 class="fw-bold py-3 mb-4"><span class="text-muted fw-light">Quản lý /</span> Hóa đơn</h4>

                            <div class="card mb-4">
                                <h5 class="card-header">Tìm kiếm & Lọc Hóa đơn</h5>
                                <div class="card-body">
                                    <h6 class="mb-3">Tìm kiếm theo ngày</h6>
                                    <form action="InvoiceServlet" method="get" class="row g-3 align-items-end mb-4">
                                        <input type="hidden" name="action" value="searchByDate" />

                                        <div class="col-md-4">
                                            <label for="startDate" class="form-label">Từ ngày:</label>
                                            <input type="date" class="form-control" id="startDate" name="startDate"
                                                   value="${param.startDate}" />
                                        </div>
                                        <div class="col-md-4">
                                            <label for="endDate" class="form-label">Đến ngày:</label>
                                            <input type="date" class="form-control" id="endDate" name="endDate"
                                                   value="${param.endDate}" />
                                        </div>
                                        <div class="col-md-4">
                                            <button type="submit" class="btn btn-primary me-2">Tìm kiếm</button>
                                            <a href="InvoiceServlet?action=list" class="btn btn-secondary">Xem tất cả</a>
                                        </div>
                                    </form>

                                    <h6 class="mb-3">Tìm kiếm theo Mã HĐ hoặc Tên Khách hàng</h6>
                                    <form method="get" action="InvoiceServlet">
                                        <input type="hidden" name="action" value="search" />
                                        <div class="mb-3">
                                            <label for="searchQuery" class="form-label">Tìm kiếm:</label>
                                            <input type="text" class="form-control" id="searchQuery" name="searchQuery"
                                                   placeholder="Nhập mã hóa đơn hoặc tên khách hàng"
                                                   value="${not empty requestScope.searchQuery ? requestScope.searchQuery : ''}" />
                                        </div>
                                        <button type="submit" class="btn btn-primary">Tìm kiếm</button>
                                    </form>

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
                                <a href="InvoiceServlet?action=showAddForm" class="btn btn-primary">
                                    <i class='bx bx-plus me-1'></i>Thêm hóa đơn mới
                                </a>
                            </div>

                            <div class="card">
                                <h5 class="card-header">Danh sách Hóa đơn</h5>
                                <div class="table-responsive text-nowrap">
                                    <table class="table card-table">
                                        <thead>
                                            <tr>
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
                                        <tbody class="table-border-bottom-0">
                                            <c:forEach var="inv" items="${invoiceList}">
                                                <tr>
                                                    <td><i class="fab fa-angular fa-lg text-danger me-3"></i> <strong>${inv.invoiceID}</strong></td>
                                                    <td>${inv.customerName}</td>
                                                    <td>${inv.shopName}</td>
                                                    <td><fmt:formatDate value="${inv.invoiceDate}" pattern="dd/MM/yyyy HH:mm:ss" /></td>
                                                    <td><fmt:formatNumber value="${inv.totalAmount}" pattern="#,##0" /> VNĐ</td>
                                                    <td>
                                                        <c:choose>
                                                            <c:when test="${inv.status}">
                                                                <span class="badge bg-label-success me-1">Đã thanh toán</span>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <form action="InvoiceServlet" method="post" style="display:inline-block;">
                                                                    <input type="hidden" name="action" value="markAsPaid" />
                                                                    <input type="hidden" name="invoiceID" value="${inv.invoiceID}" />
                                                                    <span class="badge bg-label-warning me-2">Chưa thanh toán</span>
                                                                    <button type="submit" class="btn btn-sm btn-icon btn-success" title="Đánh dấu đã thanh toán">
                                                                        <i class='bx bx-check-circle'></i>
                                                                    </button>
                                                                </form>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </td>
                                                    <td>${inv.note}</td>
                                                    <td>
                                                        <div class="dropdown">
                                                            <button type="button" class="btn p-0 dropdown-toggle hide-arrow" data-bs-toggle="dropdown">
                                                                <i class="bx bx-dots-vertical-rounded"></i>
                                                            </button>
                                                            <div class="dropdown-menu">
                                                                <a class="dropdown-item" href="InvoiceServlet?action=listDetail&invoiceID=${inv.invoiceID}">
                                                                    <i class="bx bx-detail me-1"></i> Chi tiết
                                                                </a>
                                                                <form method="post" action="InvoiceServlet" class="d-inline">
                                                                    <input type="hidden" name="action" value="delete" />
                                                                    <input type="hidden" name="invoiceID" value="${inv.invoiceID}" />
                                                                    <button type="submit" class="dropdown-item" onclick="return confirm('Bạn có chắc muốn xóa hóa đơn này?');"
                                                                            <c:if test="${inv.status}">disabled</c:if>>
                                                                                <i class="bx bx-trash me-1"></i> Xóa
                                                                            </button>
                                                                    </form>
                                                                </div>
                                                            </div>
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
                                <div class="card-footer d-flex justify-content-center"> <%-- Thay đổi justify-content-end thành justify-content-center --%>
                                    <nav aria-label="Page navigation">
                                        <c:if test="${totalPages > 1}">
                                            <ul class="pagination mb-0">
                                                <c:url var="baseLink" value="InvoiceServlet">
                                                    <c:if test="${not empty param.action}">
                                                        <c:param name="action" value="${param.action}" />
                                                    </c:if>
                                                    <c:if test="${not empty param.startDate}">
                                                        <c:param name="startDate" value="${param.startDate}" />
                                                    </c:if>
                                                    <c:if test="${not empty param.endDate}">
                                                        <c:param name="endDate" value="${param.endDate}" />
                                                    </c:if>
                                                    <c:if test="${not empty param.searchQuery}">
                                                        <c:param name="searchQuery" value="${param.searchQuery}" />
                                                    </c:if>
                                                    <%-- Nếu không có action nào, mặc định là list --%>
                                                    <c:if test="${empty param.action && empty param.startDate && empty param.endDate && empty param.searchQuery}">
                                                        <c:param name="action" value="list" />
                                                    </c:if>
                                                </c:url>

                                                <li class="page-item <c:if test="${currentPage == 1}">disabled</c:if>">
                                                    <a class="page-link" href="${currentPage > 1 ? baseLink : '#'} &page=${currentPage - 1}"><i class="tf-icon bx bx-chevrons-left"></i></a>
                                                </li>

                                                <%-- Logic để hiện thị 5 trang xung quanh trang hiện tại --%>
                                                <c:set var="numPagesToShow" value="5" />
                                                <c:set var="halfPagesToShow" value="${numPagesToShow / 2}" />

                                                <c:set var="startPage" value="${currentPage - halfPagesToShow}" />
                                                <c:set var="endPage" value="${currentPage + halfPagesToShow}" />

                                                <%-- Điều chỉnh startPage và endPage để không vượt quá giới hạn và luôn hiển thị đủ số trang (nếu có) --%>
                                                <c:if test="${startPage < 1}">
                                                    <c:set var="startPage" value="1" />
                                                    <c:set var="endPage" value="${numPagesToShow > totalPages ? totalPages : numPagesToShow}" />
                                                </c:if>

                                                <c:if test="${endPage > totalPages}">
                                                    <c:set var="endPage" value="${totalPages}" />
                                                    <c:set var="startPage" value="${totalPages - numPagesToShow + 1}" />
                                                    <c:if test="${startPage < 1}">
                                                        <c:set var="startPage" value="1" />
                                                    </c:if>
                                                </c:if>

                                                <c:forEach begin="${startPage}" end="${endPage}" var="i">
                                                    <li class="page-item <c:if test="${i == currentPage}">active</c:if>">
                                                        <a class="page-link" href="${baseLink}&page=${i}">${i}</a>
                                                    </li>
                                                </c:forEach>

                                                <li class="page-item <c:if test="${currentPage == totalPages}">disabled</c:if>">
                                                    <a class="page-link" href="${currentPage < totalPages ? baseLink : '#'} &page=${currentPage + 1}"><i class="tf-icon bx bx-chevrons-right"></i></a>
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
        <script src="${pageContext.request.contextPath}/assets/vendor/libs/jquery/jquery.js"></script>
        <script src="${pageContext.request.contextPath}/assets/vendor/libs/popper/popper.js"></script>
        <script src="${pageContext.request.contextPath}/assets/vendor/js/bootstrap.js"></script>
        <script src="${pageContext.request.contextPath}/assets/vendor/libs/perfect-scrollbar/perfect-scrollbar.js"></script>
        <script src="${pageContext.request.contextPath}/assets/vendor/js/menu.js"></script>
        <script src="${pageContext.request.contextPath}/assets/js/main.js"></script>

        <script async defer src="https://buttons.github.io/buttons.js"></script>
    </body>
</html>