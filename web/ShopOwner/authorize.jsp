<%-- 
    Document   : authorize
    Created on : Jul 23, 2025, 2:36:55 PM
    Author     : Admin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<c:if test="${sessionScope.shopOwner.id != 1}">
    <c:redirect url="SaleSphere"/>
</c:if>

<!DOCTYPE html>
<html lang="vi" class="light-style layout-menu-fixed"
      dir="ltr" data-theme="theme-default"
      data-assets-path="./assets/" data-template="vertical-menu-template-free">
    <head>
        <meta charset="UTF-8">
        <title>Phân quyền hệ thống</title>

        <!-- Favicon & Fonts -->
        <link href="https://fonts.googleapis.com/css2?family=Roboto&display=swap&subset=vietnamese" rel="stylesheet" />
        <link rel="icon" type="image/x-icon" href="img/logoSale.png" />

        <!-- Core CSS -->
        <link rel="stylesheet" href="./assets/vendor/fonts/boxicons.css" />
        <link rel="stylesheet" href="./assets/vendor/css/core.css" />
        <link rel="stylesheet" href="./assets/vendor/css/theme-default.css" />
        <link rel="stylesheet" href="./assets/css/demo.css" />
        <link rel="stylesheet" href="./assets/css/custom.css" />
        <link rel="stylesheet" href="./assets/vendor/libs/perfect-scrollbar/perfect-scrollbar.css" />
        <link rel="stylesheet" href="./assets/vendor/libs/apex-charts/apex-charts.css" />

        <!-- Scripts -->
        <script src="./assets/vendor/js/helpers.js"></script>
        <script src="./assets/js/config.js"></script>
    </head>
    <body>
        <div class="layout-wrapper layout-content-navbar">
            <div class="layout-container">
                <!-- Sidebar -->
                <jsp:include page="sidebar.jsp"/>

                <!-- Main -->
                <div class="layout-page">
                    <!-- Navbar -->
                    <jsp:include page="navbar.jsp"/>

                    <!-- Content wrapper -->
                    <div class="content-wrapper">
                        <div class="container-xxl flex-grow-1 container-p-y">

                            <h4 class="fw-bold mb-4">🔐 Phân quyền truy cập theo vai trò</h4>

                            <!-- Thông báo -->
                            <c:if test="${not empty sessionScope.flash_success}">
                                <div class="alert alert-success">${sessionScope.flash_success}</div>
                                <c:remove var="flash_success" scope="session"/>
                            </c:if>
                            <c:if test="${not empty sessionScope.flash_fail}">
                                <div class="alert alert-danger">${sessionScope.flash_fail}</div>
                                <c:remove var="flash_fail" scope="session"/>
                            </c:if>

                            <!-- Form chọn vai trò -->
                            <form method="get" action="ShowListPermission" class="mb-4">
                                <label for="roleId" class="form-label fw-semibold">Chọn vai trò:</label>
                                <select name="roleId" id="roleId" class="form-select w-auto d-inline-block">
                                    <option value="">-- Chọn vai trò --</option>
                                    <c:forEach var="role" items="${roles}">
                                        <option value="${role.id}" ${role.id == selectedRoleId ? 'selected' : ''}>
                                            ${role.name}
                                        </option>
                                    </c:forEach>
                                </select>
                                <button type="submit" class="btn btn-primary ms-2">Xem quyền</button>
                            </form>

                            <!-- Form phân quyền -->
                            <c:if test="${not empty selectedRoleId}">
                                <form method="post" action="ShowListPermission">
                                    <input type="hidden" name="roleId" value="${selectedRoleId}"/>

                                    <div class="row">
                                        <c:forEach var="page" items="${pages}">
                                            <div class="col-md-4 mb-3">
                                                <div class="form-check">
                                                    <input class="form-check-input" type="checkbox"
                                                           id="page_${page.value.pageCode}"
                                                           name="pageIds"
                                                           value="${page.value.pageCode}"
                                                           <c:if test="${grantedPages[page.value.pageCode]}">checked</c:if> />
                                                    <label class="form-check-label fw-medium" for="page_${page.value.pageCode}">
                                                        ${page.value.displayName}
                                                    </label>
                                                </div>
                                            </div>
                                        </c:forEach>
                                    </div>

                                    <button type="submit" class="btn btn-success mt-3">💾 Lưu phân quyền</button>
                                </form>
                            </c:if>
                        </div>

                        <!-- Footer -->
                        <jsp:include page="footer.jsp"/>
                    </div>
                </div>
            </div>
        </div>

        <!-- Scripts -->
        <script src="./assets/vendor/libs/jquery/jquery.js"></script>
        <script src="./assets/vendor/libs/popper/popper.js"></script>
        <script src="./assets/vendor/js/bootstrap.js"></script>
        <script src="./assets/vendor/libs/perfect-scrollbar/perfect-scrollbar.js"></script>
        <script src="./assets/vendor/js/menu.js"></script>
        <script src="./assets/js/main.js"></script>
    </body>
</html>

