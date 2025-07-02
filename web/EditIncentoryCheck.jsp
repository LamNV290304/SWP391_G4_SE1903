<%-- 
    Document   : AddInventoryCheck
    Created on : Jun 20, 2025, 4:51:30 PM
    Author     : Thai Anh
--%>

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
                <jsp:include page="sidebar.jsp" />
               
                <div class="layout-page">
                  <!--Nav-->

                  <jsp:include page="navBar.jsp" />
           <!-- / Navbar -->

            <!-- Content wrapper -->  
                <div class="container-xxl flex-grow-1 container-p-y">
  <form class="row g-3" action="AddInventoryCheck" method="POST">
    
    <!-- Cột trái -->
    <div class="col-md-2">
      <div class="card h-100">
        <h5 class="card-header">Thông Tin Phiếu Kiểm Kê</h5>
        <div class="card-body">
          <div class="mb-3">
            <label for="receiptId" class="form-label">Mã Nhân Viên</label>
            <input type="text" name="EmployeeID" class="form-control" id="receiptId" placeholder="PN001" />
          </div>
          <div class="mb-3">
            <label for="warehouse" class="form-label">Kho Kiểm kê</label>
            <select class="form-select" id="warehouse" name="shopID">
              <option selected disabled>Chọn kho</option>
              <c:forEach var="ls" items="${listShop}">
                <option value="${ls.shopID}">${ls.shopName}</option>
              </c:forEach>
            </select>
          </div>
          <div class="mb-3">
            <label for="importDate" class="form-label">Ngày kiểm kê (cuối ngày)</label>
            <input type="date" id="importDate" name="Date" class="form-control" required />
          </div>
          <div class="mb-3">
            <label for="note" class="form-label">Ghi chú</label>
            <textarea class="form-control" id="note" name="note" rows="3"></textarea>
          </div>
        </div>
      </div>
    </div>

    <!-- Cột phải -->
    <div class="col-md-10">
      <div class="card h-100">
        <h5 class="card-header">Chi Tiết Hàng Kiểm Kê</h5>
        <div class="card-body">
         <div class="table-responsive" style="max-height: 55vh; overflow: auto;">
  <table class="table table-striped table-hover" id="productTable">
    <thead >
      <tr>
        <th style="width: 30%">Tên Sản Phẩm</th>
        <th style="width: 15%">Hệ thống</th>
        <th style="width: 15%">Thực tế</th>
        <th style="width: 15%">Chênh lệch</th>
        <th style="width: 15%">Ghi Chú</th>
        <th style="width: 10%">Hành động</th>
      </tr>
    </thead>
    <tbody>
        <!--
      <c:forEach var="prod" items="${listIvt}">
        <tr>
          <td>
            <input type="hidden" name="productID[]" value="${prod.product.productID}" />
            ${prod.product.productName}
          </td>
          <td>
            <input type="number" name="systemQuantity[]" class="form-control system-qty" 
         value="${prod.quantity != null ? prod.quantity : 0}" readonly />
          </td>
<td>
  <input type="number" name="actualQuantity[]" class="form-control actual-qty" required />
</td>
<td>
  <input type="number" name="total[]" class="form-control difference-qty" readonly />
</td>

          <td><input type="text" name="note[]" class="form-control" /></td>
          <td><button type="button" class="btn btn-icon btn-danger btn-sm remove-row">
            <i class="bx bx-trash"></i>
          </button></td>
        </tr>
      </c:forEach>
        -->
    </tbody>
  </table>
</div>

          <!-- Nút submit -->           
          <div class="mt-4">
            <button type="submit" class="btn btn-primary w-100">Lưu phiếu kiểm kê</button>
          </div>
        </div>
      </div>
    </div>
<script>
document.addEventListener("input", function (e) {
  if (e.target.classList.contains("actual-qty")) {
    const row = e.target.closest("tr");

    const actual = parseFloat(row.querySelector(".actual-qty").value) || 0;
    const system = parseFloat(row.querySelector(".system-qty").value) || 0;
    const diff = actual - system;

    row.querySelector(".difference-qty").value = diff;
  }
});
window.addEventListener('DOMContentLoaded', function () {
    const today = new Date();
    const formatted = today.toISOString().split('T')[0]; // YYYY-MM-DD
    document.getElementById('importDate').value = formatted;
  });



  // Xóa dòng
  document.addEventListener("click", function (e) {
    if (e.target.classList.contains("remove-row")) {
      e.target.closest("tr").remove();
      calculateGrandTotal();
    }
  });
const productOptions = `
    <c:forEach var="ltype" items="${listProduct}">
      <option value="${ltype.productID}">${ltype.productName}</option>
    </c:forEach>
  `;
  // Hàm tính tổng cộng
 function calculateGrandTotal() {
  const totals = document.querySelectorAll('input[name="total[]"]');
  let sum = 0;
  totals.forEach(input => {
    sum += parseFloat(input.value) || 0;
  });

  // Cập nhật nội dung hiển thị
  document.getElementById("grandTotal").textContent = sum.toLocaleString("vi-VN", {
    style: "currency",
    currency: "VND"
  });

  // ✅ Cập nhật giá trị input hidden
  document.getElementById("grandTotalInput").value = sum.toFixed(2);
}
document.getElementById("warehouse").addEventListener("change", function () {
  const shopID = parseInt(this.value);
  const filtered = allInventories.filter(i => i.shopID === shopID);
  const tbody = document.querySelector("#productTable tbody");
  tbody.innerHTML = "";

  filtered.forEach(item => {
    const row = document.createElement("tr");

    // Tên sản phẩm + hidden productID
    const tdName = document.createElement("td");
    const inputHidden = document.createElement("input");
    inputHidden.type = "hidden";
    inputHidden.name = "productID[]";
    inputHidden.value = item.productID;
    const spanName = document.createElement("span");
    spanName.className = "product-name";
    spanName.textContent = item.productName;
    tdName.appendChild(inputHidden);
    tdName.appendChild(spanName);
    row.appendChild(tdName);

    // Hệ thống
    const tdSystem = document.createElement("td");
    const inputSystem = document.createElement("input");
    inputSystem.type = "number";
    inputSystem.name = "systemQuantity[]";
    inputSystem.className = "form-control system-qty";
    inputSystem.readOnly = true;
    inputSystem.value = item.quantity != null ? item.quantity : 0;
    tdSystem.appendChild(inputSystem);
    row.appendChild(tdSystem);

    // Thực tế
    const tdActual = document.createElement("td");
    const inputActual = document.createElement("input");
    inputActual.type = "number";
    inputActual.name = "actualQuantity[]";
    inputActual.className = "form-control actual-qty";
    inputActual.required = true;
    tdActual.appendChild(inputActual);
    row.appendChild(tdActual);

    // Chênh lệch
    const tdDiff = document.createElement("td");
    const inputDiff = document.createElement("input");
    inputDiff.type = "number";
    inputDiff.name = "total[]";
    inputDiff.className = "form-control difference-qty";
    inputDiff.readOnly = true;
    tdDiff.appendChild(inputDiff);
    row.appendChild(tdDiff);

    // Ghi chú
    const tdNote = document.createElement("td");
    const inputNote = document.createElement("input");
    inputNote.type = "text";
    inputNote.name = "note[]";
    inputNote.className = "form-control";
    tdNote.appendChild(inputNote);
    row.appendChild(tdNote);

    // Xóa dòng
    const tdAction = document.createElement("td");
    const btnRemove = document.createElement("button");
    btnRemove.type = "button";
    btnRemove.className = "btn btn-icon btn-danger btn-sm remove-row";
    btnRemove.innerHTML = '<i class="bx bx-trash"></i>';
    tdAction.appendChild(btnRemove);
    row.appendChild(tdAction);

    tbody.appendChild(row);
  });
});


</script>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<script>
 const allInventories = [
    <c:forEach var="ivt" items="${listIvt}" varStatus="loop">
      {
        shopID: ${ivt.shop.shopID},
        productID: ${ivt.product.productID},
        productName: "${fn:replace(fn:escapeXml(ivt.product.productName), '"', '\\"')}",
        quantity: ${ivt.quantity}
      }<c:if test="${!loop.last}">,</c:if>
    </c:forEach>
  ];
</script>
  </form>
</div>
                </div>
              </div>
              <!--/ Responsive Table -->
                </div>
        


        <script src="assets/vendor/libs/jquery/jquery.js"></script>
<script src="assets/vendor/libs/popper/popper.js"></script>
<script src="assets/vendor/js/bootstrap.js"></script>
<script src="assets/vendor/libs/perfect-scrollbar/perfect-scrollbar.js"></script>
<script src="assets/vendor/js/menu.js"></script> <!-- Xử lý toggle -->
<script src="assets/js/main.js"></script> <!-- Main logic -->

    </body>
</html>

