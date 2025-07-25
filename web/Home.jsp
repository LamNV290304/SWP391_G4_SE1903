<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<c:if test="${empty sessionScope.Employee}">
    <c:redirect url="loginEmployee.jsp"/>
</c:if>
<html
    lang="vi"
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
        <link href="https://fonts.googleapis.com/css2?family=Roboto&display=swap&subset=vietnamese" rel="stylesheet">

        <link href="https://fonts.googleapis.com/css2?family=Roboto&display=swap" rel="stylesheet" />
        <link rel="icon" type="image/x-icon" href="img/logoSale.png" />
        <link rel="preconnect" href="https://fonts.googleapis.com" />
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
        <link
            href="https://fonts.googleapis.com/css2?family=Public+Sans:ital,wght@0,300;0,400;0,500;0,600;0,700;1,300;1,400;1,500;1,600;1,700&display=swap"
            rel="stylesheet"
            />

        <link rel="stylesheet" href="./assets/css/custom.css" />

        <link rel="stylesheet" href="./assets/vendor/fonts/boxicons.css" />

        <link rel="stylesheet" href="./assets/vendor/css/core.css" class="template-customizer-core-css" />
        <link rel="stylesheet" href="./assets/vendor/css/theme-default.css" class="template-customizer-theme-css" />
        <link rel="stylesheet" href="./assets/css/demo.css" />

        <link rel="stylesheet" href="./assets/vendor/libs/perfect-scrollbar/perfect-scrollbar.css" />

        <link rel="stylesheet" href="./assets/vendor/libs/apex-charts/apex-charts.css" />

        <script src="./assets/vendor/js/helpers.js"></script>

        <script src="./assets/js/config.js"></script>
    </head>
    <body>
        <div  class="layout-wrapper layout-content-navbar">
            <div class="layout-container">
                <jsp:include page="sidebar.jsp" />

                <div class="layout-page">
                    <jsp:include page="navBar.jsp" />
                    <div class="content-wrapper">
                        <!-- Bộ lọc ngày -->
<div class="container-xxl flex-grow-1 container-p-y">
  <div class="d-flex justify-content-between align-items-center mb-4">
    <form action="Home" method="get" class="d-flex align-items-center">
  <input type="date" name="from" class="form-control me-2" required
         value="${param.from != null ? param.from : fromDate}">
  <input type="date" name="to" class="form-control me-2" required
         value="${param.to != null ? param.to : toDate}">
  <select name="shopId" class="form-select me-2" style="min-width: 200px;" required>
    <c:forEach var="shop" items="${shops}">
      <option value="${shop.shopID}"
  <c:if test="${param.shopId == shop.shopID}">selected</c:if>>
  ${shop.shopName}
</option>
    </c:forEach>
  </select>
  <button type="submit" class="btn btn-primary">Lọc</button>
</form>

  </div>

  <!-- Thống kê -->
  <div class="row">
    <div class="col-md-3">
      <div class="card text-white bg-primary mb-3">
        <div class="card-body">
          <h5 class="card-title text-light">Doanh thu </h5>
          <p class="card-text fs-4"><fmt:formatNumber value="${totalSalesAmount}" type="number"/> đ</p>
          <small class="text-white-50">So với ngày hôm qua: ${revenueChange}%</small>
        </div>
      </div>
    </div>

    <div class="col-md-3">
      <div class="card text-white bg mb-3">
        <div class="card-body">
          <h5 class="card-title">Số hóa đơn</h5>
          <p class="card-text fs-4 text-black">${invoiceCount}</p>
          <small class="text-black-50">Trung bình ${avgPerInvoice} đ / hóa đơn</small>
        </div>
      </div>
    </div>

    <div class="col-md-3">
      <div class="card text-white bg-success mb-3">
        <div class="card-body">
          <h5 class="card-title">Số khách</h5>
          <p class="card-text fs-4">${customerCount}</p>
          <small class="text-50">Trung bình ${avgPerCustomer} đ / khách</small>
        </div>
      </div>
    </div>

    <div class="col-md-3">
      <div class="card text-white bg-secondary mb-3">
        <div class="card-body">
          <h5 class="card-title">Giảm giá</h5>
          <p class="card-text fs-4"><fmt:formatNumber value="${discount}" type="number"/> đ</p>
        </div>
      </div>
    </div>
  </div>
</div>
<!-- TỔNG TỒN KHO -->
<div class="row mb-4">
  <div class="col-md-6">
    <div class="card p-3 shadow-sm">
      <div class="d-flex justify-content-between align-items-center mb-2">
        <h5 class="mb-0">Tổng tồn kho</h5>
        <a href="#" class="text-primary small">Chi tiết</a>
      </div>
      <h3 class="text-danger">
        <fmt:formatNumber value="${totalInventory}" type="number" /> đ
      </h3>
      <div class="d-flex justify-content-between mt-3">
        <span class="text-danger">${outOfStock} HH <small>Hết hàng</small></span>
        <span class="text-warning">${belowThreshold} HH <small>Dưới ngưỡng</small></span>
        <span class="text-purple">${aboveThreshold} HH <small>Vượt ngưỡng</small></span>
      </div>
    </div>
  </div>

  <!-- CHI PHÍ NGUYÊN VẬT LIỆU -->
  <div class="col-md-6">
    <div class="card p-3 shadow-sm">
      <div class="d-flex justify-content-between align-items-center mb-2">
        <h5 class="mb-0">Chi phí nhập theo tháng ${param.to != null ? param.to : toDate}</h5>
        <a href="#" class="text-primary small">Chi tiết</a>
      </div>
      <h3 class="text-primary">
        <fmt:formatNumber value="${materialCost}" type="number" /> đ
        <small class="text-warning">(${materialCostPercent}% doanh thu)</small>
      </h3>
    </div>
  </div>
</div>
                    </div>
                    <jsp:include page="footer.jsp" />
                </div>

            </div>
        </div>


        <script src="assets/vendor/libs/jquery/jquery.js"></script>
        <script src="assets/vendor/libs/popper/popper.js"></script>
        <script src="assets/vendor/js/bootstrap.js"></script>
        <script src="assets/vendor/libs/perfect-scrollbar/perfect-scrollbar.js"></script>
        <script src="assets/vendor/js/menu.js"></script> <script src="assets/js/main.js"></script> </body>
</html>