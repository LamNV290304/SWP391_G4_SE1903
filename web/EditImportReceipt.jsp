<%-- 
    Document   : EditImportReceipt
    Created on : Jun 24, 2025, 3:07:16 PM
    Author     : Thai Anh
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

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
                <div class="container" style="padding-top: 20px;"> 
                  <!-- Responsive Table -->
             <div class="card col-sm-12" style="height: 90vh; overflow: hidden;">
  <h5 class="card-header">Thêm mới phiếu nhập Hàng</h5>

 <form class="row" action="AddImportReceipt" method="POST"> 
  <!-- Cột trái: Card thông tin -->
  <div class="col-md-3" style="max-height: 80vh; overflow-y: auto;">
    <div class="card mb-2">
      <h5 class="card-header">Thông Tin</h5>
      <div class="card-body">
        <!-- Các input như bạn đã có -->
        <input type="hidden" name="ReceiptID" class="form-control"  value="${receipt.importReceiptID}"  />
        <div class="mb-3">
          <label for="warehouse" class="form-label">Nhà cung cấp</label>
           <select name="SupplierID" class="form-control" required>
        <c:forEach var="s" items="${listSup}">
            <option value="${s.supplierID}" 
                <c:if test="${s.supplierID == receipt.supplierID}">selected</c:if>>
                ${s.supplierName}
            </option>
        </c:forEach>
    </select>
        </div>
        
        
        <div class="mb-3">
            <label for="receiptId" class="form-label">Mã Nhân Viên</label>
            <input type="text" name="EmployeeID" class="form-control" id="receiptId" value="${receipt.employeeID}" />
        </div>
        <div class="mb-3">
          <label for="warehouse" class="form-label">Kho Nhập</label>
          <select name="shopID" class="form-control" required>
        <c:forEach var="s" items="${listShop}">
            <option value="${s.shopID}" 
                <c:if test="${s.shopID == receipt.shopID}">selected</c:if>>
                ${s.shopName}
            </option>
        </c:forEach>
    </select>
        </div>
        <div class="mb-3">
          <label for="receiptId" class="form-label">Ngày nhập hàng</label>
          <input type="date" id="importDate" name="Date" class="form-control" required />
        </div>
        
        <div class="mb-3">
            <label for="value" class="form-label">Giá Trị</label>
  <input type="hidden" name="Total" id="grandTotalInput" />
  <span id="grandTotal" class="fw-bold text-primary">0</span> VNĐ
</div>
        
        
        
        <div class="mb-3">
          <label for="warehouse" class="form-label">Loại Phiếu Nhập</label>
<select class="form-select" name="code" required>
   <option disabled selected>Chọn Phiếu</option>
   <c:forEach var="ltype" items="${listType}">
     <option value="${ltype.typeID}">
       ${ltype.typeID} - ${ltype.typeName}
     </option>
   </c:forEach>
</select>


        </div>
        <div>
          <label for="note" class="form-label">Ghi chú</label>
         <textarea name="note" class="form-control">${receipt.note}</textarea>
        </div>
      </div>
    </div>
  </div>
  <!--Cot phai  -->
<div class="col-md-9" id="horizontal-example">
  <div class="card h-100 d-flex flex-column">
    <h5 class="card-header">Chi Tiết Phiếu Nhập </h5>
    <div class="card-body" id="horizontal-example">

      <!-- ✅ Bọc bảng trong div có cuộn ngang -->
      <div class="card-body py-2 px-3 overflow-auto" style="max-height: 60vh;">
          <div class="table-responsive">
               <table class="table table-bordered" id="productTable" style="min-width: 1000px;">
          <thead>
            <tr>
              <th>Mã Sản Phẩm</th>
              <th>Số lượng</th>
              <th>Đơn Giá</th>
              <th>Thành Tiền</th>
              <th>Ghi Chú</th>
              <th>Hành động</th>
            </tr>
          </thead>
          <tbody>
            <c:forEach var="d" items="${details}">
<tr>
  <td>
    <select class="form-select" name="productID[]">
      <c:forEach var="prod" items="${listProduct}">
        <option value="${prod.productID}" <c:if test="${prod.productID == d.productID}">selected</c:if>>
          ${prod.productName}
        </option>
      </c:forEach>
    </select>
  </td>
  <td><input type="number" name="quantity[]" class="form-control" value="${d.quantity}" required /></td>
  <td><input type="number" name="price[]" class="form-control" value="${d.price}" required /></td>
  <td><input type="number" name="total[]" class="form-control" value="${d.quantity * d.price}" readonly /></td>
  <td><input type="text" name="note[]" class="form-control" value="${d.note}" required /></td>
  <td><button type="button" class="btn btn-danger btn-sm remove-row">Xóa</button></td>
</tr>
</c:forEach>

          </tbody>
        </table>
          </div>
       
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


// Khi thêm dòng mới, cũng tự động gán luôn importReceiptID
document.getElementById("addRowBtn").addEventListener("click", function () {
  const tableBody = document.querySelector("#productTable tbody");
  const newRow = document.createElement("tr");
  newRow.innerHTML = `
 <td> 
          <select class="form-select" name="productID[]">
    <option selected disabled>Chọn Sản Phẩm</option>
    <c:forEach var="prod" items="${listProduct}">
      <option value="${prod.productID}">${prod.productName}</option>
    </c:forEach>
  </select></td>
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

  // Cập nhật nội dung hiển thị
  document.getElementById("grandTotal").textContent = sum.toLocaleString("vi-VN", {
    style: "currency",
    currency: "VND"
  });

  // ✅ Cập nhật giá trị input hidden
  document.getElementById("grandTotalInput").value = sum.toFixed(2);
}
</script>
<script>
  const productOptions = `
    <c:forEach var="ltype" items="${listProduct}">
      <option value="${ltype.productID}">${ltype.productName}</option>
    </c:forEach>
  `;
</script>
  </div>
</form>

   





    
                </div>
              </div>
              <!--/ Responsive Table -->
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

