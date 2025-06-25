<%-- 
    Document   : navbar.jsp
    Created on : Jun 22, 2025, 2:33:02 PM
    Author     : Admin
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
                <!-- Place this tag where you want the button to render. -->

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
                      <a class="dropdown-item" href="${pageContext.request.contextPath}/ShopOwner/changePasswordCentral.jsp">
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

