<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
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
              <ul class="menu-sub active">
                <li class="menu-item active">
                  <a href="ui-accordion.html" class="menu-link active">
                    <div data-i18n="Accordion">Đặt Hàng</div>
                  </a>
                </li>
                <li class="menu-item">
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
                  <!--Nav-->

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

            <!-- Content wrapper -->
          
                <div class="container" style="padding-top: 20px;"> 
                  <!-- Responsive Table -->
             <div class="card col-sm-12" style="height: 90vh; overflow: hidden;">
  <h5 class="card-header">Thêm mới phiếu nhập Hàng</h5>

 <form class="row" action="ThemPhieuNhap" method="POST"> 
  <!-- Cột trái: Card thông tin -->
  <div class="col-md-2">
    <div class="card mb-2">
      <h5 class="card-header">Thông Tin</h5>
      <div class="card-body">
        <!-- Các input như bạn đã có -->
        <div class="mb-3">
          <label for="receiptId" class="form-label">ID Phiếu Nhập</label>
          <input type="text" name="importReceiptID" class="form-control" id="receiptId" />
        </div>
       
        <div class="mb-3">
          <label for="warehouse" class="form-label">Nhà cung cấp</label>
          <select class="form-select" id="warehouse" name="SupplierID">
            <option selected disabled>Chọn nhà cung cấp</option>
             <c:forEach var="lsup" items="${listSup}">
                      <option value="${lsup.supplierID}">${lsup.supplierName}</option>
                      </c:forEach>
          </select>
        </div>
        <div class="mb-3">
            <label for="receiptId" class="form-label">Mã Nhân Viên</label>
          <input type="text" name="EmployeeID" class="form-control" id="receiptId" placeholder="PN001" />
        </div>
        <div class="mb-3">
          <label for="warehouse" class="form-label">Kho Nhập</label>
          <select class="form-select" id="warehouse" name="shopID">
            <option selected disabled>Chọn kho</option>
             <c:forEach var="ls" items="${listShop}">
                      <option value="${ls.shopID}">${ls.shopName}</option>
                      </c:forEach>
          </select>
        </div>
        <div class="mb-3">
          <label for="receiptId" class="form-label">ID Phiếu Nhập</label>
          <input type="date" id="importDate" name="Date" class="form-control" required />
        </div>
        
        <div class="mb-3">
            <label for="value" class="form-label">Giá Trị</label>
  <input type="hidden" name="Total" id="grandTotalInput" />
  <span id="grandTotal" class="fw-bold text-primary">0</span> VNĐ
</div>
        
        
        
        <div class="mb-3">
          <label for="warehouse" class="form-label">Loại Phiếu Nhập</label>
          <select class="form-select" id="warehouse" name="code">
            <option selected disabled>Chọn Phiếu</option>
             <c:forEach var="ltype" items="${listType}">
                      <option value="${ltype.typeID}">${ltype.typeName}</option>
                      </c:forEach>
          </select>
        </div>
        <div>
          <label for="note" class="form-label">Ghi chú</label>
          <textarea class="form-control" id="note" name="note" rows="3"></textarea>
        </div>
      </div>
    </div>
  </div>
  <!--Cot phai  -->
<div class="col-md-10" id="horizontal-example">
  <div class="card mb-2">
    <h5 class="card-header">Chi Tiết Phiếu Nhập</h5>
    <div class="card-body" id="horizontal-example">

      <!-- ✅ Bọc bảng trong div có cuộn ngang -->
      <div style="overflow-x: auto;">
        <table class="table table-bordered" id="productTable" style="min-width: 1000px;">
          <thead>
            <tr>
              <th>Mã Detail</th>
              <th>Mã Nhập</th>
              <th>Mã Sản Phẩm</th>
              <th>Số lượng</th>
              <th>Đơn Giá</th>
              <th>Thành Tiền</th>
              <th>Ghi Chú</th>
              <th>Hành động</th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <td><input type="text" name="importReceiptDetailID[]" class="form-control" required /></td>
              <td><input type="number" name="importReceiptID[]" class="form-control" required /></td>
              <td><input type="text" name="productID[]" class="form-control" required /></td>
              <td><input type="number" name="quantity[]" class="form-control" required /></td>
              <td><input type="number" name="price[]" class="form-control" required /></td>
              <td><input type="number" name="total[]" class="form-control" readonly /></td>
              <td><input type="text" name="note[]" class="form-control" required /></td>
              <td><button type="button" class="btn btn-danger btn-sm remove-row">Xóa</button></td>
            </tr>
          </tbody>
        </table>
      </div>

      <!-- Nút thêm dòng -->
      <button type="button" class="btn btn-secondary mb-3" id="addRowBtn">+ Thêm dòng</button>

      <!-- Nút submit -->
      <button type="submit" class="btn btn-primary">Lưu phiếu nhập</button>
   
    </div>
  </div>
</div>

<!-- JavaScript: Thêm/Xóa dòng và tính Thành tiền -->
<script>
  // Thêm dòng mới
  document.getElementById("addRowBtn").addEventListener("click", function () {
    const tableBody = document.querySelector("#productTable tbody");
    const newRow = document.createElement("tr");
    newRow.innerHTML = `
      <td><input type="text" name="importReceiptDetailID[]" class="form-control" required /></td>
              <td><input type="number" name="importReceiptID[]" class="form-control" required /></td>
              <td><input type="text" name="productID[]" class="form-control" required /></td>
              <td><input type="number" name="quantity[]" class="form-control" required /></td>
              <td><input type="number" name="price[]" class="form-control" required /></td>
              <td><input type="number" name="total[]" class="form-control" readonly /></td>
              <td><input type="text" name="note[]" class="form-control" required /></td>
              <td><button type="button" class="btn btn-danger btn-sm remove-row">Xóa</button></td>
    `;
    tableBody.appendChild(newRow);
  });

  // Tính thành tiền + tổng cộng mỗi khi người dùng nhập số
document.addEventListener("input", function (e) {
  if (e.target.name === "quantity[]" || e.target.name === "price[]") {
    const row = e.target.closest("tr");
    const qty = parseFloat(row.querySelector('input[name="quantity[]"]').value) || 0;
    const price = parseFloat(row.querySelector('input[name="price[]"]').value) || 0;
    const total = qty * price;
    row.querySelector('input[name="total[]"]').value = total.toFixed(2);

    calculateGrandTotal();
  }
});

  // Xóa dòng
  document.addEventListener("click", function (e) {
    if (e.target.classList.contains("remove-row")) {
      e.target.closest("tr").remove();
      calculateGrandTotal();
    }
  });

  // Hàm tính tổng cộng
  function calculateGrandTotal() {
    const totals = document.querySelectorAll('input[name="total[]"]');
    let sum = 0;
    totals.forEach(input => {
      sum += parseFloat(input.value) || 0;
    });
    document.getElementById("grandTotal").textContent = sum.toLocaleString("vi-VN", {
      style: "currency",
      currency: "VND"
    });
  }
</script>

  </div>
</form>

   





    
                </div>
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
