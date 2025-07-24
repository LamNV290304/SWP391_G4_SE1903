<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

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
        <meta charset="UTF-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
        <title>Quản lý Danh mục Đồ dùng - Sneat</title>

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
                            <h4 class="fw-bold py-3 mb-4"><span class="text-muted fw-light">Quản lý /</span> Danh mục Đồ dùng</h4>

                  
                            <c:if test="${not empty sessionScope.successMessage}">
                                <div class="alert alert-success alert-dismissible" role="alert">
                                    ${sessionScope.successMessage}
                                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                                </div>
                                <c:remove var="successMessage" scope="session"/>
                            </c:if>
                            <c:if test="${not empty sessionScope.errorMessage}">
                                <div class="alert alert-danger alert-dismissible" role="alert">
                                    ${sessionScope.errorMessage}
                                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                                </div>
                                <c:remove var="errorMessage" scope="session"/>
                            </c:if>
                      
                            <c:if test="${not empty requestScope.errorMessage}">
                                <div class="alert alert-danger alert-dismissible" role="alert">
                                    ${requestScope.errorMessage}
                                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                                </div>
                                <c:remove var="errorMessage" scope="request"/>
                            </c:if>

                            <div class="card mb-4">
                                <h5 class="card-header">
                                    <c:choose>
                               
                                        <c:when test="${categoryToEdit.categoryId != null && categoryToEdit.categoryId != 0}">
                                            Chỉnh sửa Danh mục: ${categoryToEdit.categoryName}
                                        </c:when>
                                        <c:otherwise>
                                            Thêm Danh mục mới
                                        </c:otherwise>
                                    </c:choose>
                                </h5>
                                <div class="card-body">
                                    <form action="ItemCategoryServlet" method="post">
                                        <c:if test="${categoryToEdit.categoryId != null && categoryToEdit.categoryId != 0}">
                                            <input type="hidden" name="action" value="update">
                                            <input type="hidden" name="categoryId" value="${categoryToEdit.categoryId}">
                                        </c:if>
                                        <c:if test="${categoryToEdit.categoryId == null || categoryToEdit.categoryId == 0}">
                                            <input type="hidden" name="action" value="add">
                                        </c:if>

                                        <div class="row mb-3">
                                            <label class="col-sm-2 col-form-label" for="categoryName">Tên Danh mục:</label>
                                            <div class="col-sm-10">
                                                <input type="text" class="form-control" id="categoryName" name="categoryName"
                                                       value="${categoryToEdit.categoryName != null ? categoryToEdit.categoryName : ''}"
                                                       placeholder="Nhập tên danh mục" required>
                                            </div>
                                        </div>
                                        <div class="row mb-3">
                                            <label class="col-sm-2 col-form-label" for="description">Mô tả:</label>
                                            <div class="col-sm-10">
                                                <textarea class="form-control" id="description" name="description" rows="3"
                                                          placeholder="Mô tả về danh mục">${categoryToEdit.description != null ? categoryToEdit.description : ''}</textarea>
                                            </div>
                                        </div>

                                        <div class="row justify-content-end">
                                            <div class="col-sm-10">
                                                <button type="submit" class="btn btn-primary me-2">
                                                    <c:if test="${categoryToEdit.categoryId != null && categoryToEdit.categoryId != 0}">Cập nhật Danh mục</c:if>
                                                    <c:if test="${categoryToEdit.categoryId == null || categoryToEdit.categoryId == 0}">Thêm Danh mục</c:if>
                                                </button>
                                                <%-- Nút hủy chỉ hiện khi đang ở chế độ chỉnh sửa --%>
                                                <c:if test="${categoryToEdit.categoryId != null && categoryToEdit.categoryId != 0}">
                                                    <a href="ItemCategoryServlet?action=list" class="btn btn-secondary">Hủy</a>
                                                </c:if>
                                            </div>
                                        </div>
                                    </form>
                                </div>
                            </div>

                            <div class="card">
                                <h5 class="card-header">Danh sách Danh mục</h5>
                                <div class="mb-3 mx-3 mt-3 text-end">
                                    <%-- Nút thêm mới, chỉ chuyển trang nếu đang ở chế độ chỉnh sửa --%>
                                    <c:if test="${categoryToEdit.categoryId != null && categoryToEdit.categoryId != 0}">
                                        <a href="ItemCategoryServlet?action=list" class="btn btn-primary">
                                            <i class='bx bx-plus me-1'></i>Thêm Danh mục mới
                                        </a>
                                    </c:if>
                           
                                    <c:if test="${categoryToEdit.categoryId == null || categoryToEdit.categoryId == 0}">
                                       
                                    </c:if>
                                </div>
                                <div class="table-responsive text-nowrap">
                                    <table class="table card-table">
                                        <thead>
                                            <tr>
                                                <th>ID</th>
                                                <th>Tên Danh mục</th>
                                                <th>Mô tả</th>
                                                <th>Thao tác</th>
                                            </tr>
                                        </thead>
                                        <tbody class="table-border-bottom-0">
                                        <c:if test="${empty categories}">
                                            <tr>
                                                <td colspan="4" class="text-center">Không có danh mục nào để hiển thị.</td>
                                            </tr>
                                        </c:if>
                                        <c:forEach var="category" items="${categories}">
                                            <tr>
                                                <td><strong>${category.categoryId}</strong></td>
                                                <td>${category.categoryName}</td>
                                                <td>${category.description}</td>
                                                <td>
                                                    <div class="dropdown">
                                                        <button type="button" class="btn p-0 dropdown-toggle hide-arrow" data-bs-toggle="dropdown">
                                                            <i class="bx bx-dots-vertical-rounded"></i>
                                                        </button>
                                                        <div class="dropdown-menu">
                                                            <a class="dropdown-item" href="ItemCategoryServlet?action=edit&id=${category.categoryId}">
                                                                <i class="bx bx-edit-alt me-1"></i> Chỉnh sửa
                                                            </a>
                                                            <form method="post" action="ItemCategoryServlet" class="d-inline">
                                                                <input type="hidden" name="action" value="delete" />
                                                                <input type="hidden" name="id" value="${category.categoryId}" /> <%-- id thay vì categoryId --%>
                                                                <button type="submit" class="dropdown-item" onclick="return confirm('Bạn có chắc muốn xóa danh mục này? Điều này có thể ảnh hưởng đến các đồ dùng liên quan.');">
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
                            </div>
                        </div>
                        <%-- Include Footer --%>
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