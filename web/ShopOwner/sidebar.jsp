<%-- 
    Document   : sidebar.jsp
    Created on : Jun 22, 2025, 2:32:43 PM
    Author     : Admin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<aside id="layout-menu" class="layout-menu menu-vertical menu bg-menu-theme">
    <div class="app-brand demo">
        <a href="${pageContext.request.contextPath}/ShowServicePackage" class="app-brand-link">
            <span class="app-brand-logo demo">
                <img src="${pageContext.request.contextPath}/ShopOwner/img/logoSale.png" alt="Logo" style="height: 80px;">
            </span>
            <span class="app-brand-text demo menu-text fw-bolder ms-2">SaleShape</span>
        </a>
        <a href="#" class="layout-menu-toggle menu-link text-large ms-auto d-block d-xl-none">
            <i class="bx bx-chevron-left bx-sm align-middle"></i>
        </a>
    </div>
    <div class="menu-inner-shadow"></div>
    <ul class="menu-inner py-1">
        <!-- Dashboard -->
        <li class="menu-item">
            <a href="${pageContext.request.contextPath}/ShowServicePackage" class="menu-link">
                <i class="menu-icon tf-icons bx bx-home-circle"></i>
                <div data-i18n="Analytics">Các gói dịch vụ</div>
            </a>
        </li>
        <li class="menu-item">
            <a href="javascript:void(0);" class="menu-link menu-toggle">
                <i class="menu-icon tf-icons bx bx-lock-open-alt"></i>
                <div data-i18n="Authentications">Lịch sử thanh toán</div>
            </a>
            <ul class="menu-sub">
                <li class="menu-item">
                    <a href="${pageContext.request.contextPath}/ShowPaymentHistory" class="menu-link" target="_blank">
                        <div data-i18n="Basic">Lịch sử thanh toán cá nhân</div>
                    </a>
                </li>
                <li class="menu-item">
                    <a href="#" class="menu-link" target="_blank">
                        <div data-i18n="Basic">Gói đã đăng kí</div>
                    </a>
                </li>
            </ul>
        </li>
        <li class="menu-item">
            <a href="javascript:void(0);" class="menu-link menu-toggle">
                <i class="menu-icon tf-icons bx bx-cube-alt"></i>
                <div data-i18n="Misc">Thanh toán</div>
            </a>
            <ul class="menu-sub">
                <li class="menu-item">
                    <a href="pages-misc-error.html" class="menu-link">
                        <div data-i18n="Basic">Báo Cáo Lương </div>
                    </a>
                </li>
            </ul>
        </li>
        <li class="menu-item">
            <a href="javascript:void(0);" class="menu-link menu-toggle">
                <i class="menu-icon tf-icons bx bx-cube-alt"></i>
                <div data-i18n="Misc">Thống kê</div>
            </a>
            <ul class="menu-sub">
                <li class="menu-item">
                    <a href="pages-misc-error.html" class="menu-link">
                        <div data-i18n="Basic">Báo Cáo Lương </div>
                    </a>
                </li>
            </ul>
        </li>
        <li class="menu-item">
            <a href="javascript:void(0);" class="menu-link menu-toggle">
                <i class="menu-icon tf-icons bx bx-cube-alt"></i>
                <div data-i18n="Misc">Quản lí khách hàng</div>
            </a>
            <ul class="menu-sub">
                <li class="menu-item">
                    <a href="pages-misc-error.html" class="menu-link">
                        <div data-i18n="Basic">Danh sách khách hàng</div>
                    </a>
                </li>
            </ul>
        </li>
    </ul>
</aside>
