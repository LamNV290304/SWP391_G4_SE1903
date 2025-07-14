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
        <title>Quản lý Đồ dùng - Sneat</title>

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
                <%-- Include Sidebar --%>
                <jsp:include page="sidebar.jsp" />

                <div class="layout-page">
                    <%-- Include Navbar --%>
                    <jsp:include page="navBar.jsp" />

                    <div class="content-wrapper">
                        <div class="container-xxl flex-grow-1 container-p-y">
                            <h4 class="fw-bold py-3 mb-4"><span class="text-muted fw-light">Quản lý /</span> Đồ dùng Cửa hàng</h4>

                            <div class="card mb-4">
                                <h5 class="card-header">Tìm kiếm & Lọc Đồ dùng</h5>
                                <div class="card-body">
                                    <%-- Search by Date --%>
                                    <h6 class="mb-3">Tìm kiếm theo ngày giao dịch</h6>
                                    <form action="ShopItemServlet" method="get" class="row g-3 align-items-end mb-4">
                                        <input type="hidden" name="action" value="searchByDate"/>

                                        <div class="col-md-4">
                                            <label for="startDate" class="form-label">Từ ngày:</label>
                                            <input type="date" id="startDate" name="startDate" class="form-control" value="${param.startDate != null ? param.startDate : ''}"/>
                                        </div>
                                        <div class="col-md-4">
                                            <label for="endDate" class="form-label">Đến ngày:</label>
                                            <input type="date" id="endDate" name="endDate" class="form-control" value="${param.endDate != null ? param.endDate : ''}"/>
                                        </div>
                                        <div class="col-md-4">
                                            <button type="submit" class="btn btn-primary me-2">
                                                <i class='bx bx-search me-1'></i> Tìm kiếm
                                            </button>
                                            <a href="ShopItemServlet?action=list" class="btn btn-secondary">
                                                <i class='bx bx-reset me-1'></i> Xem tất cả
                                            </a>
                                        </div>
                                    </form>

                                    <%-- Search by Keyword (Name/Description) --%>
                                    <h6 class="mb-3">Tìm kiếm theo Tên đồ dùng hoặc ID</h6>
                                    <form method="get" action="ShopItemServlet" class="mb-4">
                                        <input type="hidden" name="action" value="search" />
                                        <div class="mb-3">
                                            <label for="searchQuery" class="form-label">Tìm kiếm:</label>
                                            <input type="text" class="form-control" id="searchQuery" name="searchQuery"
                                                   placeholder="Nhập tên hoặc mô tả đồ dùng"
                                                   value="${not empty requestScope.searchQuery ? requestScope.searchQuery : ''}" />
                                        </div>
                                        <button type="submit" class="btn btn-primary">
                                            <i class='bx bx-search me-1'></i> Tìm kiếm
                                        </button>
                                    </form>

                                    <%-- Message Display (from session scope, cleared after display) --%>
                                    <c:if test="${not empty sessionScope.successMessage}">
                                        <div class="alert alert-success alert-dismissible mt-3" role="alert">
                                            ${sessionScope.successMessage}
                                            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                                        </div>
                                        <c:remove var="successMessage" scope="session"/>
                                    </c:if>
                                    <c:if test="${not empty sessionScope.errorMessage}">
                                        <div class="alert alert-danger alert-dismissible mt-3" role="alert">
                                            ${sessionScope.errorMessage}
                                            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                                        </div>
                                        <c:remove var="errorMessage" scope="session"/>
                                    </c:if>
                                </div>
                            </div>

                            <div class="mb-3 text-end">
                                <a href="ShopItemServlet?action=add" class="btn btn-primary">
                                    <i class='bx bx-plus me-1'></i>Thêm đồ dùng mới
                                </a>
                            </div>

                            <div class="card">
                                <h5 class="card-header">Danh sách Đồ dùng</h5>
                                <div class="table-responsive text-nowrap">
                                    <table class="table card-table">
                                        <thead>
                                            <tr>
                                                <th>ID</th>
                                                <th>Tên Đồ dùng</th>
                                                <th>Mô tả</th>
                                                <th>Danh mục</th>
                                                <th>Số lượng</th>
                                                
                                                <th>Giá</th>
                                                <th>Thời gian giao dịch</th>
                                                <th>Cửa hàng</th>
                                                <th>Ghi chú</th>
                                                <th>Thao tác</th>
                                            </tr>
                                        </thead>
                                        <tbody class="table-border-bottom-0">
                                            <c:if test="${empty shopItems}">
                                                <tr>
                                                    <td colspan="11" class="text-center">Không có đồ dùng nào để hiển thị.</td>
                                                </tr>
                                            </c:if>
                                            <c:forEach var="item" items="${shopItems}">
                                                <tr>
                                                    <td><strong>${item.itemId}</strong></td>
                                                    <td>${item.itemName}</td>
                                                    <td>${item.description}</td>
                                                    <td>
                                                        <c:choose>
                                                            <c:when test="${not empty item.category}">
                                                                ${item.category.categoryName}
                                                            </c:when>
                                                            <c:otherwise>(Chưa có)</c:otherwise>
                                                        </c:choose>
                                                    </td>
                                                    <td>${item.quantity}</td>
                                                
                                                    <td>
                                                        <c:if test="${item.price != null}">
                                                            <fmt:formatNumber value="${item.price}" type="currency" currencySymbol="đ" maxFractionDigits="0"/>
                                                        </c:if>
                                                    </td>
                                                    <td>
                                                        <c:if test="${not empty item.itemDate}">
                                                            <fmt:formatDate value="${item.itemDate}" pattern="yyyy-MM-dd HH:mm"/>
                                                        </c:if>
                                                        <c:if test="${empty item.itemDate}">
                                                            (Chưa có)
                                                        </c:if>
                                                    </td>
                                                    <td>
                                                        <c:choose>
                                                            <c:when test="${not empty item.shop}">
                                                                ${item.shop.shopName}
                                                            </c:when>
                                                           
                                                        </c:choose>
                                                    </td>
                                                    <td>${item.notes}</td>
                                                    <td>
                                                        <div class="dropdown">
                                                            <button type="button" class="btn p-0 dropdown-toggle hide-arrow" data-bs-toggle="dropdown">
                                                                <i class="bx bx-dots-vertical-rounded"></i>
                                                            </button>
                                                            <div class="dropdown-menu">
                                                                <a class="dropdown-item" href="ShopItemServlet?action=edit&id=${item.itemId}">
                                                                    <i class="bx bx-edit-alt me-1"></i> Chỉnh sửa
                                                                </a>
                                                                <form method="post" action="ShopItemServlet" class="d-inline">
                                                                    <input type="hidden" name="action" value="delete" />
                                                                    <input type="hidden" name="id" value="${item.itemId}" />
                                                                    <button type="submit" class="dropdown-item" onclick="return confirm('Bạn có chắc muốn xóa đồ dùng này?');">
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
                                <div class="card-footer d-flex justify-content-center">
                                    <nav aria-label="Page navigation">
                                        <c:if test="${totalPages > 1}">
                                            <ul class="pagination mb-0">
                                                <c:url var="baseLink" value="ShopItemServlet">
                                               
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
                                                    <%-- Default action to list if no specific action is present (e.g., first load) --%>
                                                    <c:if test="${empty param.action && empty param.startDate && empty param.endDate && empty param.searchQuery}">
                                                        <c:param name="action" value="list" />
                                                    </c:if>
                                                </c:url>

                                                <li class="page-item <c:if test="${currentPage == 1}">disabled</c:if>">
                                                    <a class="page-link" href="${currentPage > 1 ? baseLink : '#'}**&**page=${currentPage - 1}">
                                                        <i class="tf-icon bx bx-chevrons-left"></i>
                                                    </a>
                                                </li>

                                                <c:set var="numPagesToShow" value="5" />
                                                <c:set var="halfPagesToShow" value="${numPagesToShow / 2}" />

                                                <c:set var="startPage" value="${currentPage - halfPagesToShow}" />
                                                <c:set var="endPage" value="${currentPage + halfPagesToShow}" />

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
                                                    <a class="page-link" href="${currentPage < totalPages ? baseLink : '#'}**&**page=${currentPage + 1}">
                                                        <i class="tf-icon bx bx-chevrons-right"></i>
                                                    </a>
                                                </li>
                                            </ul>
                                        </c:if>
                                    </nav>
                                </div>
                            </div>
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

    </body>
</html> 