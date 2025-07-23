<%-- 
    Document   : ExportReceipt
    Created on : Jun 12, 2025, 11:43:28 AM
    Author     : Thai Anh
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<!--
Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Html.html to edit this template
-->
<html
    lang="en"
    class="light-style layout-menu-fixed"
    dir="ltr"
    data-theme="theme-default"
    data-assets-path="./assets/"
    data-template="vertical-menu-template-free"
    >
    <head>
        <title>SaleShape</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="icon" type="image/x-icon" href="img/logoSale.png" />
        <!-- Fonts -->
        <link rel="preconnect" href="https://fonts.googleapis.com" />
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
        <link
            href="https://fonts.googleapis.com/css2?family=Public+Sans:ital,wght@0,300;0,400;0,500;0,600;0,700;1,300;1,400;1,500;1,600;1,700&display=swap"
            rel="stylesheet"
            />

        <!-- Icons. Uncomment required icon fonts -->
        <link rel="stylesheet" href="./assets/css/custom.css" />

        <link rel="stylesheet" href="./assets/vendor/fonts/boxicons.css" />

        <!-- Core CSS -->
        <link rel="stylesheet" href="./assets/vendor/css/core.css" class="template-customizer-core-css" />
        <link rel="stylesheet" href="./assets/vendor/css/theme-default.css" class="template-customizer-theme-css" />
        <link rel="stylesheet" href="./assets/css/demo.css" />

        <!-- Vendors CSS -->
        <link rel="stylesheet" href="./assets/vendor/libs/perfect-scrollbar/perfect-scrollbar.css" />

        <link rel="stylesheet" href="./assets/vendor/libs/apex-charts/apex-charts.css" />

        <!-- Page CSS -->

        <!-- Helpers -->
        <script src="./assets/vendor/js/helpers.js"></script>

        <!--! Template customizer & Theme config files MUST be included after core stylesheets and helpers.js in the <head> section -->
        <!--? Config:  Mandatory theme config file contain global vars & default theme options, Set your preferred theme option in this file.  -->
        <script src="./assets/js/config.js"></script>
    </head>
    <body>
        <div  class="layout-wrapper layout-content-navbar">
            <div class="layout-container">
                <!--menu-->
                <jsp:include page="sidebar.jsp" />

                <div class="layout-page">
                    <jsp:include page="navBar.jsp" />

                    <!-- / Navbar -->

                    <!-- Content wrapper -->

                    <div class="container" style="padding-top: 20px;"> 
                        <!-- Responsive Table -->
                        <div class="card col-sm-12" style="overflow: hidden;">
                            <h5 class="card-header">Phiếu Xuất Hàng
                                <a href="AddExportReceipt" >
                                    <button type="button" class="btn btn-outline-info">Thêm mới phiếu xuất hàng</button>
                                </a>
                            </h5>
                            <c:if test="${not empty successMessage}">
                                <div class="alert alert-success alert-dismissible fade show mt-3" role="alert">
                                    ${successMessage}
                                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                                </div>
                            </c:if>
                            <div class="table-responsive text-nowrap" style="height: calc(80vh - 80px); overflow-y: auto;">
                                <form action="ExportReceiptServlet" method="GET" class="card-header-tabs row mb-4 " 
                                      style="padding: 0px 50px 0px 5%;">
                                    <div class="col-lg-2 col-md-3 col-sm-6 mb-2">
                                        <label class="form-label">Nhân viên</label>
                                        <select class="form-select form-select-sm" name="employeeId">
                                            <option value="">Tất cả</option>
                                            <c:forEach var="emp" items="${listEmp}">
                                                <option value="${emp.id}" ${param.employeeId == emp.id ? 'selected' : ''}>
                                                    ${emp.fullname}
                                                </option>
                                            </c:forEach>
                                        </select>
                                    </div>

                                    <div class="col-lg-2 col-md-3 col-sm-6 mb-2">
                                        <label class="form-label">Kho xuất</label>
                                        <select class="form-select form-select-sm" name="shopId">
                                            <option value="">Tất cả</option>
                                            <c:forEach var="shop" items="${listShop}">
                                                <option value="${shop.shopID}" ${param.shopId == shop.shopID ? 'selected' : ''}>
                                                    ${shop.shopName}
                                                </option>
                                            </c:forEach>
                                        </select>
                                    </div>

                                    <div class="col-lg-2 col-md-3 col-sm-6 mb-2">
                                        <label class="form-label">Loại phiếu</label>
                                        <select class="form-select form-select-sm" name="typeId">
                                            <option value="">Tất cả</option>
                                            <c:forEach var="type" items="${Types}">
                                                <option value="${type.typeID}" ${param.typeId == type.typeID ? 'selected' : ''}>
                                                    ${type.typeName}
                                                </option>
                                            </c:forEach>
                                        </select>
                                    </div>

                                    <div class="col-lg-2 col-md-3 col-sm-6 mb-2">
                                        <label class="form-label">Từ ngày</label>
                                        <input type="date" name="fromDate" class="form-control form-control-sm"
                                               value="${not empty param.fromDate ? fn:substring(param.fromDate, 0, 10) : ''}">
                                    </div>

                                    <div class="col-lg-2 col-md-3 col-sm-6 mb-2">
                                        <label class="form-label">Đến ngày</label>
                                        <input type="date" name="toDate" class="form-control form-control-sm"
                                               value="${not empty param.toDate ? fn:substring(param.toDate, 0, 10) : ''}">
                                    </div>

                                    <div class="col-lg-2 col-md-3 col-sm-6 mb-2 d-flex align-items-end">
                                        <button type="submit" class="btn btn-sm btn-primary w-100">
                                            <i class="bx bx-filter-alt"></i> Lọc
                                        </button>
                                    </div>
                                </form>


                                <table class="table">
                                    <thead style="position: sticky ; top: 0; background-color: white; z-index: 20;">
                                        <tr class="text-nowrap">
                                            <th>#</th>
                                            <th>Mã Phiếu</th>
                                            <th>Ngày Xuất</th>
                                            <th>Kho Xuất</th>
                                            <th>Giá Trị </th>
                                            <th>Nhân Viên Xuất</th>
                                            <th>Loại Xuất</th>
                                            <th>Ghi chú</th>
                                            <th>Hành động</th>
                                        </tr>
                                    </thead>
                                    <tbody id="vertical-example">
                                        <c:forEach var="ir" items="${listIE}" varStatus="loop">
                                            <tr>
                                                <th scope="row">${loop.index + 1}</th>
                                                <td>${ir.exportReceiptID}</td>
                                                <td>${ir.receiptDate}</td>
                                                <td><c:forEach var="shop" items="${listShop}">
                                                        <c:if test="${shop.shopID == ir.shopID}">
                                                            ${shop.shopName}
                                                        </c:if>
                                                    </c:forEach></td>

                                                <td>${ir.totalAmount}</td>
                                                <td><c:forEach var="emp" items="${listEmp}">
                                                        <c:if test="${emp.id == ir.employeeID}">
                                                            ${emp.fullname}
                                                        </c:if>
                                                    </c:forEach></td>
                                                <td><c:forEach var="type" items="${Types}">
                                                        <c:if test="${type.typeID== ir.typeID}">
                                                            ${type.typeName}
                                                        </c:if>
                                                    </c:forEach></td>
                                                <td>${ir.note}</td>
                                                <td>
                                                    <div class="dropdown">
                                                        <button type="button" class="btn p-0 dropdown-toggle hide-arrow" data-bs-toggle="dropdown">
                                                            <i class="bx bx-dots-vertical-rounded"></i>
                                                        </button>
                                                        <div class="dropdown-menu">

                                                            <form class="dropdown-item" action="ExportReceiptServlet" method="POST">
                                                                <input type="hidden" name="action" value="edit" />
                                                                <input type="hidden" name="receiptId" value="${ir.exportReceiptID}" />
                                                                <button class="btn btn-secondary" type="submit"><i>Edit</i></button>
                                                            </form>
                                                            <form class="dropdown-item" action="ExportReceiptServlet" method="POST">
                                                                <input type="hidden" name="action" value="delete" />
                                                                <input type="hidden" name="receiptId" value="${ir.exportReceiptID}" />
                                                                <button class="btn btn-secondary" type="submit"><i>Delete</i></button>
                                                            </form>
                                                        </div>
                                                    </div>
                                                </td>
                                            </tr>
                                        </c:forEach>

                                    </tbody>
                                </table>
                            </div>
                            <jsp:include page="footer.jsp" />
                        </div>
                        <!--/ Responsive Table -->

                    </div>

                </div>


            </div>


            <script src="assets/vendor/libs/jquery/jquery.js"></script>
            <script src="assets/vendor/libs/popper/popper.js"></script>
            <script src="assets/vendor/js/bootstrap.js"></script>
            <script src="assets/vendor/libs/perfect-scrollbar/perfect-scrollbar.js"></script>
            <script src="assets/vendor/js/menu.js"></script> <!-- Xử lý toggle -->
            <script src="assets/js/main.js"></script> <!-- Main logic -->

    </body>
</html>

