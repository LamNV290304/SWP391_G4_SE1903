<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<!--
Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Html.html to edit this template
-->
<c:if test="${empty sessionScope.Employee}">
    <c:redirect url="loginEmployee.jsp"/>
</c:if>
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
                                <div class="col-md-3"style="max-height: 80vh; overflow-y: auto;">
                                    <div class="card mb-2">
                                        <h5 class="card-header">Thông Tin</h5>
                                        <div class="card-body">
                                            <!-- Các input như bạn đã có -->

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
                                <div class="col-md-9" id="horizontal-example">
                                    <div class="card h-100 d-flex flex-column">
                                        <h5 class="card-header">Chi Tiết Phiếu Nhập </h5>
                                        <div class="card-body" id="horizontal-example">

                                            <!-- ✅ Bọc bảng trong div có cuộn ngang -->
                                            <div class="card-body py-2 px-3 overflow-auto" style="max-height: 60vh;">
                                                <div class="table-responsive">
                                                    <table class="table table-bordered table-sm" id="productTable" style="min-width: 100%; " >
                                                        <thead>
                                                            <tr>
                                                                <th style="width: 20%;">Mã Sản phẩm</th>
                                                                <th style="width: 20%;">Tên Sản phẩm</th> 
                                                                <th style="width: 13%;">Số lượng</th>
                                                                <th style="width: 15%;">Đơn Giá</th>
                                                                <th style="width: 15%;">Thành Tiền</th>
                                                                <th style="width: 22%;">Ghi Chú</th>
                                                                <th style="width: 15%;">Hành động</th>
                                                            </tr>
                                                        </thead>
                                                        <tbody>
                                                            <tr>
                                                                <td>
                                                                    <input type="text" name="productID[]" class="form-control product-id" placeholder="Nhập mã sản phẩm" required />
                                                                </td>
                                                                <td>
  <div class="d-flex align-items-center gap-2">
    <input type="text" class="form-control product-name" name="productName[]" readonly />
    <a href="ListProductServlet" target="_blank" class="btn btn-outline-primary btn-sm add-product-btn d-none">➕</a>
  </div>
</td>


                                                                <td><input type="number" name="quantity[]" class="form-control" required /></td>
                                                                <td><input type="number" name="price[]" class="form-control" required /></td>
                                                                <td><input type="text" name="total[]" class="form-control text-end" readonly /></td>
                                                                <td><input type="text" name="note[]" class="form-control" required /></td>
                                                                <td><button type="button" class="btn btn-danger btn-sm remove-row">Xóa</button></td>
                                                            </tr>

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
<td><input type="text" name="productID[]" class="form-control product-id" placeholder="Nhập mã sản phẩm" required /></td>
<td>
  <div class="d-flex align-items-center gap-2">
    <input type="text" class="form-control product-name" name="productName[]" readonly />
    <a href="/AddProduct" target="_blank" class="btn btn-outline-primary btn-sm add-product-btn d-none">➕</a>
  </div>
</td>
<td><input type="number" name="quantity[]" class="form-control" required /></td>
<td><input type="number" name="price[]" class="form-control" required /></td>
<td><input type="text" name="total[]" class="form-control text-end" readonly /></td>
<td><input type="text" name="note[]" class="form-control" required /></td>
<td><button type="button" class="btn btn-danger btn-sm remove-row">Xóa</button></td>
`;

                                    tableBody.appendChild(newRow);
                                    });
                                    // Tính thành tiền + tổng cộng mỗi khi người dùng nhập số
                                    document.addEventListener("input", function (e) {
  if (e.target.classList.contains("product-id")) {
    const row = e.target.closest("tr");
    const productId = e.target.value.trim();
    const nameInput = row.querySelector(".product-name");
    const addBtn = row.querySelector(".add-product-btn");

    if (productMap[productId]) {
      nameInput.value = productMap[productId];
      nameInput.readOnly = true;
      addBtn.classList.add("d-none");
    } else {
      nameInput.value = "";
      nameInput.readOnly = true;
      addBtn.classList.remove("d-none");

      // Gán đường dẫn có sẵn mã để
    }
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
                                    const raw = input.value.replace(/\D/g, '');
                                    sum += parseFloat(raw) || 0;
                                    });
                                    document.getElementById("grandTotal").textContent = formatCurrency(sum);
                                    document.getElementById("grandTotalInput").value = sum;
                                    }


                                    // Cập nhật nội dung hiển thị

                                </script>
                                <script>
                                    const productOptions = `
                                    <c:forEach var="ltype" items="${listProduct}">
                                      <option value="${ltype.productID}">${ltype.productName}</option>
                                    </c:forEach>
                                  `;
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


    </div>

    <script>
        function formatCurrency(value) {
        return Number(value).toLocaleString("vi-VN", {
        style: "currency",
                currency: "VND"
        });
        }

        // Format tất cả input khi load trang
        window.addEventListener('DOMContentLoaded', () => {
        document.querySelectorAll('input[name="total[]"]').forEach(input => {
        const raw = input.value.replace(/\D/g, '');
        input.value = formatCurrency(raw);
        });
        });
    </script>
    <script>
        const productMap = {
        <c:forEach var="prod" items="${listProduct}">
        "${prod.productID}": "${prod.productName}",
        </c:forEach>
        };
    </script>
    <script>
        // Tính thành tiền khi nhập số lượng hoặc đơn giá
  document.addEventListener("input", function (e) {
    if (e.target.name === "quantity[]" || e.target.name === "price[]") {
      const row = e.target.closest("tr");
      const quantity = parseFloat(row.querySelector('input[name="quantity[]"]').value) || 0;
      const price = parseFloat(row.querySelector('input[name="price[]"]').value) || 0;
      const total = quantity * price;

      const totalField = row.querySelector('input[name="total[]"]');
      totalField.value = formatCurrency(total);
      
      calculateGrandTotal(); // Cập nhật tổng cộng
    }
  });
    </script>

    <script src="assets/vendor/libs/jquery/jquery.js"></script>
    <script src="assets/vendor/libs/popper/popper.js"></script>
    <script src="assets/vendor/js/bootstrap.js"></script>
    <script src="assets/vendor/libs/perfect-scrollbar/perfect-scrollbar.js"></script>
    <script src="assets/vendor/js/menu.js"></script> <!-- Xử lý toggle -->
    <script src="assets/js/main.js"></script> <!-- Main logic -->
    <script>
        // Tự động cập nhật tên sản phẩm khi chọn mã sản phẩm
        document.addEventListener("change", function (e) {
        if (e.target.classList.contains("product-select")) {
        const selectedOption = e.target.options[e.target.selectedIndex];
        const productName = selectedOption.getAttribute("data-name") || "";
        const row = e.target.closest("tr");
        row.querySelector(".product-name").value = productName;
        }
        });
    </script>
</body>
</html>
