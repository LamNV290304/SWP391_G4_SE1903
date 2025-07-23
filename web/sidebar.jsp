<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<aside id="layout-menu" class="layout-menu menu-vertical menu bg-menu-theme">
    <div class="app-brand demo">
        <a href="Home.jsp" class="app-brand-link">
            <span class="app-brand-logo demo">
                <img src="img/logoSale.png" alt="Logo" style="height: 80px;">
            </span>
            <span class="app-brand-text demo menu-text fw-bolder ms-2">SaleShape</span>
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
                <div data-i18n="Analytics">Home Page</div>
            </a>
        </li>
        <!--Nhï¿½n Viï¿½n-->
        <li class="menu-header small text-uppercase">
            <span class="menu-header-text">Quản lý nhân sư</span>
        </li>
        <li class="menu-item">
            <a href="javascript:void(0);" class="menu-link menu-toggle">
                <i class="menu-icon tf-icons bx bx-dock-top"></i>
                <div data-i18n="Account Settings">Nhân Viên</div>
            </a>
            <ul class="menu-sub">
                <li class="menu-item">
                    <a href="ShowEmployeeList" class="menu-link">
                        <div data-i18n="Account">Thông tin nhân viên</div>
                    </a>
                </li>
                <li class="menu-item">
                    <a href="AddEmployee" class="menu-link">
                        <div data-i18n="Account">Thêm nhân viên</div>
                    </a>
                </li>
            </ul>
        </li>
        <li class="menu-item">
            <a href="javascript:void(0);" class="menu-link menu-toggle">
                <i class="menu-icon tf-icons bx bx-lock-open-alt"></i>
                <div data-i18n="Authentications">Lịch Làm Việc</div>
            </a>
            <ul class="menu-sub">
                <li class="menu-item">
                    <a href="WorkSchedule" class="menu-link" >
                        <div>Lịch Làm Việc</div>
                    </a>
                </li>
                <li class="menu-item">
                    <a href="WorkSchedule?service=workSchedule&view=shift" class="menu-link" >
                        <div>Lịch Làm Việc Theo Ca</div>
                    </a>
                </li>
            </ul>
        </li>
        <li class="menu-item">
            <a href="javascript:void(0);" class="menu-link menu-toggle">
                <i class="menu-icon tf-icons bx bx-cube-alt"></i>
                <div data-i18n="Misc">Lương</div>
            </a>
            <ul class="menu-sub">
                <li class="menu-item">
                    <a href="SalaryController" class="menu-link">
                        <div>Báo Cáo Lương </div>
                    </a>
                </li>
                <li class="menu-item">
                    <a href="SalarySettingController" class="menu-link">
                        <div>Thiết Lập Lương</div>
                    </a>
                </li>
            </ul>
        </li>
        <!-- Kho -->
        <li class="menu-header small text-uppercase"><span class="menu-header-text">Quản Lý Kho</span></li>
        <!-- Qu?n Lï¿½ Kho -->
        <li class="menu-item">
            <a href="ListShopServlet" class="menu-link">
                <i class="menu-icon tf-icons bx bx-collection"></i>
                <div >Điểm bán hàng-</div>
            </a>
        </li>
        <!-- Hï¿½ng Hï¿½a -->
        <li class="menu-item">
            <a href="ListProductServlet" class="menu-link menu-toggle">
                <i class="menu-icon tf-icons bx bx-box"></i>
                <div >Thông tin Hàng Hóa</div>
            </a>
            <ul class="menu-sub">
                <li class="menu-item">
                    <a href="ListProductServlet" class="menu-link">
                        <div >Hàng hóa theo thương hiệu</div>
                    </a>
                </li>


                <li class="menu-item">
                    <a href="ListCategoryUnitServlet" class="menu-link">
                        <div >Thiết lập hàng hóa</div>
                    </a>
                </li>

            </ul>
        </li>

        <!-- Qu?n Lï¿½ Kho -->
        <li class="menu-item">
            <a href="javascript:void(0)" class="menu-link menu-toggle">
                <i class="menu-icon tf-icons bx bx-copy"></i>
                <div data-i18n="Extended UI">Quản lý kho</div>
            </a>
            <ul class="menu-sub">
                <li class="menu-item">
                    <a href="listInventoryHome" class="menu-link">
                        <div >Thông tin tồn kho</div>
                    </a>
                </li>
                <li class="menu-item">
                    <a href="ImportReceiptServlet" class="menu-link">
                        <div >Nhập Hàng</div>
                    </a>
                </li>
                <li class="menu-item">
                    <a href="ExportReceiptServlet" class="menu-link">
                        <div>Xuất Hàng</div>
                    </a>
                </li>
                <li class="menu-item">
                    <a href="TypeReceiptServlet" class="menu-link">
                        <div >Thiết lập kho hàng</div>
                    </a>
                </li>
            </ul>
        </li>

        <li class="menu-item">
            <a href="InventoryCheckServlet" class="menu-link">
                <i class="menu-icon tf-icons bx bx-crown"></i>
                <div >Kiểm kê</div>
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
                        <div data-i18n="Basic Inputs">Chương Trinh</div>
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

                    <a href="InvoiceServlet" class="menu-link">
                        <div data-i18n="Vertical Form">Danh sách hóa đơn </div>

                    </a>
                </li>
                <li class="menu-item">
                    <a href="StatisticServlet" class="menu-link">
                        <div data-i18n="Horizontal Form">Thống kê nhân viên bán hàng</div>
                    </a>
                </li>
            </ul>
        </li>
        <li class="menu-header small text-uppercase"><span class="menu-header-text">Đồ dùng cửa hàng</span></li>
        <!-- Forms -->
        <li class="menu-item">
            <a href="javascript:void(0);" class="menu-link menu-toggle">
                <i class="menu-icon tf-icons bx bx-detail"></i>
                <div data-i18n="Form Elements">Đồ dùng</div>
            </a>
            <ul class="menu-sub">
                <li class="menu-item">
                    <a href="ShopItemServlet" class="menu-link">
                        <div data-i18n="Basic Inputs">Quản lí đồ dùng</div>
                    </a>
                </li>
                <li class="menu-item">
                    <a href="ItemCategoryServlet" class="menu-link">
                        <div data-i18n="Basic Inputs">Danh mục đồ dùng</div>
                    </a>
                </li>
                <li class="menu-item">
                    <a href="ReportItemServlet" class="menu-link">
                        <div data-i18n="Basic Inputs">Thống kê</div>
                    </a>
                </li>

            </ul>
        </li>
        <li class="menu-item">
            <a href="javascript:void(0);" class="menu-link menu-toggle">
                <i class="menu-icon tf-icons bx bx-detail"></i>
                <div data-i18n="Form Layouts">Quản lý chi tiêu</div>
            </a>
            <ul class="menu-sub">
                <li class="menu-item">
                    <a href="ReceiptVoucherServlet" class="menu-link">
                        <div >Phiếu Thu </div>
                    </a>

                    <a href="PaymentVoucherServlet" class="menu-link">
                        <div >Phiếu Chi </div>

                    </a>
                    <a href="TypeVoucherServlet" class="menu-link">
                        <div >Thiết lập thu chi</div>

                    </a>
                </li>
            </ul>
        </li>
        <!-- Danh M?c -->
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