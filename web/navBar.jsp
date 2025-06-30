<%-- 
    Document   : navBar
    Created on : Jun 11, 2025, 10:48:17 PM
    Author     : Thai Anh
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!--Nav bar-->

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
                ${sessionScope.shopName}
            </div>
        </div>
        <!-- /title -->

        <ul class="navbar-nav flex-row align-items-center ms-auto">
            <!-- Notification -->
            <li class="nav-item dropdown-notifications dropdown me-3">
                <a class="nav-link dropdown-toggle hide-arrow" href="javascript:void(0);" data-bs-toggle="dropdown">
                    <i class="bx bx-bell bx-sm"></i>

                    <c:if test="${sizeNoti > 0}">
                        <span class="badge bg-danger rounded-pill badge-notifications">
                            ${sizeNoti}
                        </span>
                    </c:if>
                </a>
                <ul class="dropdown-menu dropdown-menu-end">
                    <li class="dropdown-header">
                        <div class="d-flex justify-content-between">
                            <h6 class="dropdown-title mb-0">Thông báo</h6>

                            <c:if test="${sizeNoti > 0}">
                                <span class="badge rounded-pill bg-label-primary">
                                    ${sizeNoti}
                                </span>
                            </c:if>
                        </div>
                    </li>
                    <li>
                        <div class="dropdown-divider"></div>
                    </li>
                    <c:forEach var="n" items="${vectorNoti}">
                        <li class="dropdown-notifications-item
                            ${n.isRead == 0 ? 'bg-light border-start border-primary border-3' : ''}">
                            <a class="dropdown-item px-3 py-2 ${n.isRead == 0 ? 'fw-bold' : ''}" href="NotiController?service=SetIsRead&NotiID=${n.notiID}&link=${n.link}">

                                <div class="d-flex">
                                    <!-- Avatar/logo -->
                                    <div class="flex-shrink-0 me-3">
                                        <div class="avatar">
                                            <img src="img/logoSale.png" alt class="w-px-40 h-auto rounded-circle">
                                        </div>
                                    </div>

                                    <!-- Nội dung -->
                                    <div class="flex-grow-1">
                                        <!-- Tiêu đề -->
                                        <p class="mb-1 ${n.isRead == 0 ? 'text-dark fw-semibold' : 'text-dark'}" style="font-size: 1rem;">
                                            ${n.title}
                                        </p>

                                        <!-- Nội dung thông báo -->
                                        <p class="mb-2 ${n.isRead == 0 ? 'text-secondary' : 'text-muted'}" style="font-size: 0.95rem;">
                                            ${n.message}
                                        </p>

                                        <!-- Thời gian -->
                                        <small class="${n.isRead == 0 ? 'text-dark' : 'text-muted'}" style="font-size: 0.8rem;">
                                            5 phút trước
                                        </small>
                                    </div>
                                </div>
                            </a>
                        </li>
                    </c:forEach>




                    <li>
                        <div class="dropdown-divider"></div>
                    </li>
                    <li class="dropdown-footer">
                        <a class="dropdown-item text-center" href="#">Xem tất cả thông báo</a>
                    </li>
                </ul>
            </li>
            <!-- /Notification -->

            <!-- User -->
            <li class="nav-item navbar-dropdown dropdown-user dropdown">
                <a class="nav-link dropdown-toggle hide-arrow" href="javascript:void(0);" data-bs-toggle="dropdown">
                    <div class="avatar avatar-online">
                        <img src="img/logoSale.png" alt class="w-px-40 h-auto rounded-circle" />
                    </div>
                </a>
                <ul class="dropdown-menu dropdown-menu-end">
                    <li>
                        <a class="dropdown-item" href="ShowProfile">
                            <i class="bx bx-user me-2"></i>
                            <span class="align-middle">Thông tin tài khoản</span>
                        </a>
                    </li>
                    <li>
                        <a class="dropdown-item" href="changePassword.jsp">
                            <i class="bx bx-cog me-2"></i>
                            <span class="align-middle"> Đổi mật khẩu </span>
                        </a>
                    </li>
                    <li>
                        <div class="dropdown-divider"></div>
                    </li>
                    <li>
                        <a class="dropdown-item" href="Logout">
                            <i class="bx bx-power-off me-2"></i>
                            <span class="align-middle">Đăng Xuất</span>
                        </a>
                    </li>
                </ul>
            </li>
            <!--/ User -->
        </ul>
    </div>
</nav>
