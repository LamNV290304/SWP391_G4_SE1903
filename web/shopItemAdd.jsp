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
        <meta charset="UTF-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
        <title>Thêm Đồ dùng Cửa hàng - Sneat</title>

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
                            <h4 class="fw-bold py-3 mb-4"><span class="text-muted fw-light">Quản lý /</span> Thêm Đồ dùng Cửa hàng mới</h4>

                       
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
                                <h5 class="card-header">Thêm Đồ dùng mới</h5>
                                <div class="card-body">
                                    <form action="ShopItemServlet" method="post">
                                        <input type="hidden" name="action" value="add">

                                        <div class="row mb-3">
                                            <label class="col-sm-2 col-form-label" for="itemName">Tên Đồ dùng:</label>
                                            <div class="col-sm-10">
                                                <input type="text" class="form-control" id="itemName" name="itemName" value="${itemToEdit.itemName}" required>
                                            </div>
                                        </div>
                                 
                                        <div class="row mb-3">
                                            <label class="col-sm-2 col-form-label" for="categoryId">Danh mục:</label>
                                            <div class="col-sm-10">
                                                <select class="form-select" id="categoryId" name="categoryId" required>
                                                    <option value="">-- Chọn danh mục --</option>
                                                    <c:forEach var="category" items="${categories}">
                                                        <option value="${category.categoryId}"
                                                                <c:if test="${category.categoryId == itemToEdit.categoryId}">selected</c:if>>
                                                            ${category.categoryName}
                                                        </option>
                                                    </c:forEach>
                                                </select>
                                            </div>
                                        </div>
                                        <div class="row mb-3">
                                            <label class="col-sm-2 col-form-label" for="quantity">Số lượng:</label>
                                            <div class="col-sm-10">
                                                <input type="number" class="form-control" id="quantity" name="quantity" value="${itemToEdit.quantity}" min="0" required>
                                            </div>
                                        </div>

                                        <div class="row mb-3">
                                            <label class="col-sm-2 col-form-label" for="unitId">Đơn vị:</label>
                                            <div class="col-sm-10">
                                                <select class="form-select" id="unitId" name="unitId" required>
                                                    <option value="">-- Chọn đơn vị --</option>
                                                    <c:forEach var="unit" items="${units}">
                                                        <option value="${unit.unitID}"
                                                                <c:if test="${unit.unitID == itemToEdit.unitId}">selected</c:if>>
                                                            ${unit.description}
                                                        </option>
                                                    </c:forEach>
                                                </select>
                                            </div>
                                        </div>

                                        <div class="row mb-3">
                                            <label class="col-sm-2 col-form-label" for="price">Giá:</label>
                                            <div class="col-sm-10">
                                                <input type="number" step="0.01" class="form-control" id="price" name="price" value="${itemToEdit.price}" min="0">
                                            </div>
                                        </div>

                                

                                        <div class="row mb-3">
                                            <label class="col-sm-2 col-form-label" for="shopId">Cửa hàng:</label>
                                            <div class="col-sm-10">
                                                <select class="form-select" id="shopId" name="shopId" required>
                                                    <option value="">-- Chọn cửa hàng--</option>
                                                    <c:forEach var="shop" items="${shops}">
                                                        <option value="${shop.shopID}"
                                                                <c:if test="${shop.shopID == itemToEdit.shopId}">selected</c:if>>
                                                            ${shop.shopName}
                                                        </option>
                                                    </c:forEach>
                                                </select>
                                            </div>
                                        </div>

                                        <div class="row mb-3">
                                            <label class="col-sm-2 col-form-label" for="notes">Ghi chú:</label>
                                            <div class="col-sm-10">
                                                <textarea class="form-control" id="notes" name="notes">${itemToEdit.notes}</textarea>
                                            </div>
                                        </div>

                                        <div class="row justify-content-end">
                                            <div class="col-sm-10">
                                                <button type="submit" class="btn btn-primary me-2">Thêm Đồ dùng</button>
                                                <a href="ShopItemServlet?action=list" class="btn btn-secondary">Hủy</a>
                                            </div>
                                        </div>
                                    </form>
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