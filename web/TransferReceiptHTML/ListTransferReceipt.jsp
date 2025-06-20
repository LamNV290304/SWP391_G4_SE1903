<%-- 
    Document   : ListTransfer
    Created on : May 28, 2025, 5:32:34 AM
    Author     : ADMIN
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html
    lang="en"
    class="light-style layout-menu-fixed"
    dir="ltr"
    data-theme="theme-default"
    data-assets-path="../assets/"
    data-template="vertical-menu-template-free"
    >
    <head>
        <meta charset="utf-8" />
        <meta
            name="viewport"
            content="width=device-width, initial-scale=1.0, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0"
            />

        <title>Tables - Basic Tables | Sneat - Bootstrap 5 HTML Admin Template - Pro</title>

        <meta name="description" content="" />

        <!-- Favicon -->
        <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/assets/img/favicon/favicon.ico" />

        <!-- Fonts -->
        <link rel="preconnect" href="https://fonts.googleapis.com" />
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
        <link
            href="https://fonts.googleapis.com/css2?family=Public+Sans:ital,wght@0,300;0,400;0,500;0,600;0,700;1,300;1,400;1,500;1,600;1,700&display=swap"
            rel="stylesheet"
            />

        <!-- Icons. Uncomment required icon fonts -->
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/vendor/fonts/boxicons.css" />

        <!-- Core CSS -->
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/vendor/css/core.css" class="template-customizer-core-css" />
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/vendor/css/theme-default.css" class="template-customizer-theme-css" />
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/demo.css" />

        <!-- Vendors CSS -->
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/vendor/libs/perfect-scrollbar/perfect-scrollbar.css" />

        <!-- Page CSS -->

        <!-- Helpers -->
        <script src="${pageContext.request.contextPath}/assets/vendor/js/helpers.js"></script>

        <!--! Template customizer & Theme config files MUST be included after core stylesheets and helpers.js in the <head> section -->
        <!--? Config:  Mandatory theme config file contain global vars & default theme options, Set your preferred theme option in this file.  -->
        <script src="${pageContext.request.contextPath}/assets/js/config.js"></script>
    </head>

    <body>
        <div class="layout-wrapper layout-content-navbar">
            <div class="layout-container">
                <!--menu-->
                <aside id="layout-menu" class="layout-menu menu-vertical menu bg-menu-theme">
                    <div class="app-brand demo">
                        <a href="Home.jsp" class="app-brand-link">
                            <span class="app-brand-logo demo">
                                <img src="img/logoSale.png" alt="Logo" style="height: 80px;">
                            </span>
                            <span class="app-brand-text demo menu-text fw-bolder ms-2">Sale</span>
                        </a>
                        <a href="javascript:void(0);" class="layout-menu-toggle menu-link text-large ms-auto d-block d-xl-none">
                            <i class="bx bx-chevron-left bx-sm align-middle"></i>
                        </a>
                    </div>
                    <div class="menu-inner-shadow"></div>
                    <ul class="menu-inner py-1">
                        <!-- Dashboard -->
                        <li class="menu-item">
                            <a href="index.html" class="menu-link">
                                <i class="menu-icon tf-icons bx bx-home-circle"></i>
                                <div data-i18n="Analytics">Trang Chủ</div>
                            </a>
                        </li>

                        <!-- Account -->
                        <li class="menu-item">
                            <a href="javascript:void(0);" class="menu-link menu-toggle">
                                <i class="menu-icon tf-icons bx bx-layout">

                                </i>
                                <div data-i18n="Layouts">Tài Khoản</div>
                            </a>

                            <ul class="menu-sub">
                                <li class="menu-item">
                                    <a href="layouts-without-menu.html" class="menu-link">
                                        <div data-i18n="Without menu">Thông tin tài Khoản</div>
                                    </a>
                                </li>
                                <li class="menu-item">
                                    <a href="layouts-without-navbar.html" class="menu-link">
                                        <div data-i18n="Without navbar">Hợp Đồng</div>
                                    </a>
                                </li>
                                <li class="menu-item">
                                    <a href="layouts-container.html" class="menu-link">
                                        <div data-i18n="Container">Bảo Mật</div>
                                    </a>
                                </li>
                                <li class="menu-item">
                                    <a href="layouts-fluid.html" class="menu-link">
                                        <div data-i18n="Fluid">Quyền Hạn</div>
                                    </a>
                                </li>
                                <li class="menu-item">
                                    <a href="layouts-blank.html" class="menu-link">
                                        <div data-i18n="Blank">Khác</div>
                                    </a>
                                </li>
                            </ul>
                        </li>
                        <!--Nhân Viên-->
                        <li class="menu-header small text-uppercase">
                            <span class="menu-header-text">Quản Lý Nhân Sự</span>
                        </li>
                        <li class="menu-item">
                            <a href="javascript:void(0);" class="menu-link menu-toggle">
                                <i class="menu-icon tf-icons bx bx-dock-top"></i>
                                <div data-i18n="Account Settings">Nhân Sự</div>
                            </a>
                            <ul class="menu-sub">
                                <li class="menu-item">
                                    <a href="pages-account-settings-account.html" class="menu-link">
                                        <div data-i18n="Account">Thông Tin nhân Viên</div>
                                    </a>
                                </li>
                                <li class="menu-item">
                                    <a href="pages-account-settings-notifications.html" class="menu-link">
                                        <div data-i18n="Notifications">Bộ phận Quản Lý </div>
                                    </a>
                                </li>
                                <li class="menu-item">
                                    <a href="pages-account-settings-connections.html" class="menu-link">
                                        <div data-i18n="Connections">Bộ Phận Phục Vụ</div>
                                    </a>
                                </li>
                            </ul>
                        </li>
                        <li class="menu-item">
                            <a href="javascript:void(0);" class="menu-link menu-toggle">
                                <i class="menu-icon tf-icons bx bx-lock-open-alt"></i>
                                <div data-i18n="Authentications">Lịch Làm việc</div>
                            </a>
                            <ul class="menu-sub">
                                <li class="menu-item">
                                    <a href="auth-login-basic.html" class="menu-link" target="_blank">
                                        <div data-i18n="Basic">Lịch làm việc theo ca</div>
                                    </a>
                                </li>
                                <li class="menu-item">
                                    <a href="auth-register-basic.html" class="menu-link" target="_blank">
                                        <div data-i18n="Basic">Chấm Công</div>
                                    </a>
                                </li>
                                <li class="menu-item">
                                    <a href="auth-forgot-password-basic.html" class="menu-link" target="_blank">
                                        <div data-i18n="Basic">Báo Cáo</div>
                                    </a>
                                </li>
                            </ul>
                        </li>
                        <li class="menu-item">
                            <a href="javascript:void(0);" class="menu-link menu-toggle">
                                <i class="menu-icon tf-icons bx bx-cube-alt"></i>
                                <div data-i18n="Misc">Lương </div>
                            </a>
                            <ul class="menu-sub">
                                <li class="menu-item">
                                    <a href="pages-misc-error.html" class="menu-link">
                                        <div data-i18n="Error">Báo Cáo Lương</div>
                                    </a>
                                </li>
                                <li class="menu-item">
                                    <a href="pages-misc-under-maintenance.html" class="menu-link">
                                        <div data-i18n="Under Maintenance">Phụ Lương </div>
                                    </a>
                                </li>
                                <li class="menu-item">
                                    <a href="pages-misc-under-maintenance.html" class="menu-link">
                                        <div data-i18n="Under Maintenance">Thiết lập lương</div>
                                    </a>
                                </li>
                            </ul>
                        </li>
                        <!-- Kho -->
                        <li class="menu-header small text-uppercase"><span class="menu-header-text">Quản lý Kho</span></li>
                        <!-- Quản Lý Kho -->
                        <li class="menu-item">
                            <a href="cards-basic.html" class="menu-link">
                                <i class="menu-icon tf-icons bx bx-collection"></i>
                                <div data-i18n="Basic">-Báo Cáo Kho Hàng-</div>
                            </a>
                        </li>
                        <!-- Hàng Hóa -->
                        <li class="menu-item active">
                            <a href="javascript:void(0)" class="menu-link menu-toggle op ">
                                <i class="menu-icon tf-icons bx bx-box"></i>
                                <div data-i18n="User interface">Nhập Hàng</div>
                            </a>
                            <ul class="menu-sub ">
                                <li class="menu-item ">
                                    <a href="ui-accordion.html" class="menu-link active">
                                        <div data-i18n="Accordion">Đặt Hàng</div>
                                    </a>
                                </li>
                                <li class="menu-item active">
                                    <a href="ui-alerts.html" class="menu-link">
                                        <div data-i18n="Alerts">Nhập Mua hàng</div>
                                    </a>
                                </li>
                                <li class="menu-item">
                                    <a href="ui-badges.html" class="menu-link">
                                        <div data-i18n="Badges">Nhập điều chuyển</div>
                                    </a>
                                </li>

                            </ul>
                        </li>

                        <!-- Quản Lý Kho -->
                        <li class="menu-item">
                            <a href="javascript:void(0)" class="menu-link menu-toggle">
                                <i class="menu-icon tf-icons bx bx-copy"></i>
                                <div data-i18n="Extended UI">Xuất Hàng</div>
                            </a>
                            <ul class="menu-sub">
                                <li class="menu-item">
                                    <a href="extended-ui-perfect-scrollbar.html" class="menu-link">
                                        <div data-i18n="Perfect Scrollbar">Xuất Bán Hàng</div>
                                    </a>
                                </li>
                                <li class="menu-item">
                                    <a href="extended-ui-text-divider.html" class="menu-link">
                                        <div data-i18n="Text Divider">Xuất Điều Chuyển</div>
                                    </a>
                                </li>
                                <li class="menu-item">
                                    <a href="extended-ui-text-divider.html" class="menu-link">
                                        <div data-i18n="Text Divider">Xuất Bán Buôn</div>
                                    </a>
                                </li>
                                <li class="menu-item">
                                    <a href="extended-ui-text-divider.html" class="menu-link">
                                        <div data-i18n="Text Divider">Xuất Hủy</div>
                                    </a>
                                </li>

                            </ul>
                        </li>

                        <li class="menu-item">
                            <a href="icons-boxicons.html" class="menu-link">
                                <i class="menu-icon tf-icons bx bx-crown"></i>
                                <div data-i18n="Boxicons">Kiểm Kê</div>
                            </a>
                        </li>
                        <li class="menu-item">
                            <a href="icons-boxicons.html" class="menu-link">
                                <i class="menu-icon tf-icons bx bx-crown"></i>
                                <div data-i18n="Boxicons">Đang uplateting</div>
                            </a>
                        </li>
                        <!-- Forms & Tables -->
                        <li class="menu-header small text-uppercase"><span class="menu-header-text">Bán Hàng</span></li>
                        <!-- Forms -->
                        <li class="menu-item">
                            <a href="javascript:void(0);" class="menu-link menu-toggle">
                                <i class="menu-icon tf-icons bx bx-detail"></i>
                                <div data-i18n="Form Elements">Menu</div>
                            </a>
                            <ul class="menu-sub">
                                <li class="menu-item">
                                    <a href="forms-basic-inputs.html" class="menu-link">
                                        <div data-i18n="Basic Inputs">Chương Trình</div>
                                    </a>
                                </li>
                                <li class="menu-item">
                                    <a href="forms-input-groups.html" class="menu-link">
                                        <div data-i18n="Input groups">Danh Mục</div>
                                    </a>
                                </li>
                            </ul>
                        </li>
                        <li class="menu-item">
                            <a href="javascript:void(0);" class="menu-link menu-toggle">
                                <i class="menu-icon tf-icons bx bx-detail"></i>
                                <div data-i18n="Form Layouts">Bán Hàng</div>
                            </a>
                            <ul class="menu-sub">
                                <li class="menu-item">
                                    <a href="form-layouts-vertical.html" class="menu-link">
                                        <div data-i18n="Vertical Form">Khách Hàng </div>
                                    </a>
                                </li>
                                <li class="menu-item">
                                    <a href="form-layouts-horizontal.html" class="menu-link">
                                        <div data-i18n="Horizontal Form">Báo Cáo</div>
                                    </a>
                                </li>
                            </ul>
                        </li>
                        <!-- Danh Mục -->
                        <li class="menu-item">
                            <a href="tables-basic.html" class="menu-link">
                                <i class="menu-icon tf-icons bx bx-table"></i>
                                <div data-i18n="Tables">Danh Mục</div>
                            </a>
                        </li>
                        <!-- Misc -->
                        <li class="menu-header small text-uppercase"><span class="menu-header-text">Misc</span></li>
                        <li class="menu-item">
                            <a
                                href="https://github.com/themeselection/sneat-html-admin-template-free/issues"
                                target="_blank"
                                class="menu-link"
                                >
                                <i class="menu-icon tf-icons bx bx-support"></i>
                                <div data-i18n="Support">Support</div>
                            </a>
                        </li>
                        <li class="menu-item">
                            <a
                                href="https://themeselection.com/demo/sneat-bootstrap-html-admin-template/documentation/"
                                target="_blank"
                                class="menu-link"
                                >
                                <i class="menu-icon tf-icons bx bx-file"></i>
                                <div data-i18n="Documentation">Documentation</div>
                            </a>
                        </li>
                    </ul>
                </aside>
                <div class="layout-page">
                    <nav
                        class="layout-navbar container-xxl navbar navbar-expand-xl navbar-detached align-items-center bg-navbar-theme"
                        id="layout-navbar"
                        >
                        <div class="layout-menu-toggle navbar-nav align-items-xl-center me-3 me-xl-0 d-xl-none">
                            <a class="nav-item nav-link px-0 me-xl-4" href="javascript:void(0)">
                                <i class="bx bx-menu bx-sm"></i>
                            </a>
                        </div>

                        <div class="navbar-nav-right d-flex align-items-center" id="navbar-collapse">
                            <!-- titletitle -->
                            <div class="navbar-nav w-100 d-flex justify-content-center align-items-center">
                                <div class="Brand-Logo fs-4 fw-bold">
                                    Tên thương Hiệu
                                </div>
                            </div>
                            <!-- /title -->

                            <ul class="navbar-nav flex-row align-items-center ms-auto">
                                <!-- Place this tag where you want the button to render. -->
                                <li class="nav-item lh-1 me-3">
                                    <a
                                        class="github-button"
                                        href="https://github.com/themeselection/sneat-html-admin-template-free"
                                        data-icon="octicon-star"
                                        data-size="large"
                                        data-show-count="true"
                                        aria-label="Star themeselection/sneat-html-admin-template-free on GitHub"
                                        >Mess</a
                                    >
                                </li>

                                <!-- User -->
                                <li class="nav-item navbar-dropdown dropdown-user dropdown">
                                    <a class="nav-link dropdown-toggle hide-arrow" href="javascript:void(0);" data-bs-toggle="dropdown">
                                        <div class="avatar avatar-online">
                                            <img src="img/logoSale.png" alt class="w-px-40 h-auto rounded-circle" />
                                        </div>
                                    </a>
                                    <ul class="dropdown-menu dropdown-menu-end">
                                        <li>
                                            <a class="dropdown-item" href="#">
                                                <div class="d-flex">
                                                    <div class="flex-shrink-0 me-3">




                                                        <div class="avatar avatar-online">
                                                            <img src="img/logoSale.png" alt class="w-px-40 h-auto rounded-circle" />
                                                        </div>
                                                    </div>
                                                    <div class="flex-grow-1">
                                                        <span class="fw-semibold d-block">AnhĐVT</span>
                                                        <small class="text-muted">Admin</small>
                                                    </div>
                                                </div>
                                            </a>
                                        </li>
                                        <li>
                                            <div class="dropdown-divider"></div>
                                        </li>
                                        <li>
                                            <a class="dropdown-item" href="#">
                                                <i class="bx bx-user me-2"></i>
                                                <span class="align-middle">Thông tin của tôi</span>
                                            </a>
                                        </li>
                                        <li>
                                            <a class="dropdown-item" href="#">
                                                <i class="bx bx-cog me-2"></i>
                                                <span class="align-middle">Cài đặt</span>
                                            </a>
                                        </li>
                                        <li>
                                            <a class="dropdown-item" href="#">
                                                <span class="d-flex align-items-center align-middle">
                                                    <i class="flex-shrink-0 bx bx-credit-card me-2"></i>
                                                    <span class="flex-grow-1 align-middle">HeHe</span>
                                                    <span class="flex-shrink-0 badge badge-center rounded-pill bg-danger w-px-20 h-px-20">4</span>
                                                </span>
                                            </a>
                                        </li>
                                        <li>
                                            <div class="dropdown-divider"></div>
                                        </li>
                                        <li>
                                            <a class="dropdown-item" href="auth-login-basic.html">
                                                <i class="bx bx-power-off me-2"></i>
                                                <span class="align-middle">Đăng Xuất</span>
                                            </a>
                                        </li>
                                    </ul>
                                </li>
                                <!--/ User -->
                            </ul>
                        </div>
                        <!--menu in navbar-->

                    </nav>
                    <!-- / Navbar -->
                    <div class="content-wrapper">
                        <div class="container-xxl flex-grow-1 container-p-y">

                            <h4 class="fw-bold py-3 mb-4"><span class="text-muted fw-light"></span> Transfer Receipt</h4>


                            <div class="mb-3 d-flex justify-content-between align-items-center flex-wrap">
                                <!-- Search bên trái -->
                                <form action="TransferReceipt" method="POST" class="d-flex align-items-center mb-2 mb-md-0">
                                    <div class="input-group">
                                        <span class="input-group-text"><i class="bx bx-search"></i></span>
                                        <input
                                            type="text"
                                            class="form-control"
                                            placeholder="Search..."
                                            name="search"
                                            value="${param.search != null ? param.search : ''}" />
                                    </div>
                                    <input type="hidden" name="submit" value="listProcessTransferReceipt">
                                    <input type="hidden" name="service" value="listProcessTransferReceipt">
                                </form>

                                <!-- Nút bên phải -->
                                <div class="d-flex gap-2">
                                    <a href="TransferReceipt?service=listCompleteTransferReceipt" class="btn btn-outline-secondary">
                                        <i class="bx bx-check-circle me-1"></i> Completed TransferReceipt
                                    </a>
                                    <a href="TransferReceipt?service=addTransferReceipt" class="btn btn-primary">
                                        <i class="bx bx-plus me-1"></i> Add new
                                    </a>
                                </div>
                            </div>

                            <!-- Basic Bootstrap Table -->
                            <div class="card">
                                <h5 class="card-header">List Transfer</h5>
                                <div class="table-responsive text-nowrap">
                                    <table class="table">
                                        <thead>
                                            <tr>
                                                <th>ID</th>
                                                <th>From</th>
                                                <th>To</th>
                                                <th>Date</th>
                                                <th>Note</th>
                                                <th>Status</th>
                                                <th>Actions</th>
                                            </tr>
                                        </thead>
                                        <tbody class="table-border-bottom-0">
                                            <c:forEach var="p" items="${data}">
                                                <tr>
                                                    <td><i class="fab fa-angular fa-lg text-danger me-3"></i> <strong>${p.transferReceiptID}</strong></td>

                                                    <td>
                                                        <c:forEach var="c" items="${vectorS}">
                                                            <div value="${c.shopID}">
                                                                <c:if test="${p.fromShopID == c.shopID}">${c.shopName}</c:if>
                                                                </div>
                                                        </c:forEach>
                                                    </td>
                                                    <td>
                                                        <c:forEach var="c" items="${vectorS}">
                                                            <div value="${c.shopID}">
                                                                <c:if test="${p.toShopID == c.shopID}">${c.shopName}</c:if>
                                                                </div>
                                                        </c:forEach>
                                                    </td>
                                                    <td>${p.transferDate}</td>
                                                    <td>${p.note}</td>
                                                    <td>
                                                        <c:choose>
                                                            <c:when test="${p.status == 1}">
                                                                Accepted
                                                            </c:when>
                                                            <c:when test="${p.status == 2}">
                                                                Rejected
                                                            </c:when>
                                                            <c:otherwise>
                                                                Process
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </td>

                                                    <td>
                                                        <c:if test="${p.status == 0}">
                                                            <div class="dropdown">
                                                                <button type="button" class="btn p-0 dropdown-toggle hide-arrow" data-bs-toggle="dropdown">
                                                                    <i class="bx bx-dots-vertical-rounded"></i>
                                                                </button>
                                                                <div class="dropdown-menu">
                                                                    
                                                                    <a class="dropdown-item" href="TransferReceipt?service=Detail&TransferReceiptID=${p.transferReceiptID}"
                                                                       ><i class="bx bx-edit-alt me-1" ></i> Detail</a
                                                                    >
                                                                    <a class="dropdown-item" href="TransferReceipt?service=deleteTransferReceipt&TransferReceiptID=${p.transferReceiptID}"
                                                                       ><i class="bx bx-trash me-1"></i> Delete</a
                                                                    >
                                                                </div>
                                                            </div>

                                                        </c:if>
                                                    </td>
                                                    <td>
                                                        <c:if test="${p.status == 0}">
                                                            <form action="TransferReceipt" method="Get">
                                                                <input type="hidden" name="service" value="updateStatus">
                                                                <input type="hidden" name="setStatus" value="accept">
                                                                <input type="hidden" name="TransferReceiptID" value="${p.transferReceiptID}">
                                                                <button type="submit" class="btn btn-success btn-sm">✔</button>
                                                            </form>
                                                        </c:if>
                                                    </td>
                                                    <td>
                                                        <c:if test="${p.status == 0}">
                                                            <form action="TransferReceipt" method="Get">
                                                                <input type="hidden" name="service" value="updateStatus">
                                                                <input type="hidden" name="setStatus" value="reject">
                                                                <input type="hidden" name="TransferReceiptID" value="${p.transferReceiptID}">
                                                                <button type="submit" class="btn btn-danger btn-sm">✖</button>
                                                            </form>
                                                        </c:if>
                                                    </td>

                                                </tr>
                                            </c:forEach>


                                        </tbody>
                                    </table>
                                </div>
                            </div>
                            <!-- Pagination -->
                            <nav aria-label="Page navigation" class="mt-4">
                                <ul class="pagination justify-content-center">
                                    <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                                        <a class="page-link" href="TransferReceipt?service=listProcessTransferReceipt&selectPage=${currentPage - 1}&search=${currentSearch}&submit=Search">&laquo;</a>
                                    </li>

                                    <c:forEach var="i" begin="1" end="${page}">
                                        <li class="page-item ${i == currentPage ? 'active' : ''}">
                                            <a class="page-link" href="TransferReceipt?service=listProcessTransferReceipt&selectPage=${i}&search=${currentSearch}&submit=Search">${i}</a>
                                        </li>
                                    </c:forEach>

                                    <li class="page-item ${currentPage == page ? 'disabled' : ''}">
                                        <a class="page-link" href="TransferReceipt?service=listProcessTransferReceipt&selectPage=${currentPage + 1}&search=${currentSearch}&submit=Search">&raquo;</a>
                                    </li>


                                </ul>
                            </nav>

                        </div>
                    </div>
                </div>
            </div>
        </div>


        <!--/ Basic Bootstrap Table -->

        <hr class="my-5" />

        <!-- Core JS -->
        <!-- build:js assets/vendor/js/core.js -->
        <script src="${pageContext.request.contextPath}/assets/vendor/libs/jquery/jquery.js"></script>
        <script src="${pageContext.request.contextPath}/assets/vendor/libs/popper/popper.js"></script>
        <script src="${pageContext.request.contextPath}/assets/vendor/js/bootstrap.js"></script>
        <script src="${pageContext.request.contextPath}/assets/vendor/libs/perfect-scrollbar/perfect-scrollbar.js"></script>

        <script src="${pageContext.request.contextPath}/assets/vendor/js/menu.js"></script>
        <!-- endbuild -->

        <!-- Vendors JS -->

        <!-- Main JS -->
        <script src="${pageContext.request.contextPath}/assets/js/main.js"></script>

        <!-- Page JS -->

        <!-- Place this tag in your head or just before your close body tag. -->
        <script async defer src="https://buttons.github.io/buttons.js"></script>
    </body>
</html>

