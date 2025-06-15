<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<!--
Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Html.html to edit this template
-->
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
                <!--sidebar-->
                <jsp:include page="sidebar.jsp" />
               
                <div class="layout-page">
                  <!--Nav-->
                   <jsp:include page="navBar.jsp" />
           <!-- / Navbar -->
           <div>
               <div class="container">
            <h2 class="my-4 text-center">Chi tiết Hóa đơn: ${selectedInvoice.invoiceID}</h2>

            <%-- Hiển thị thông báo lỗi hoặc thành công --%>
            <c:if test="${not empty errorMessage}">
                <div class="alert alert-danger" role="alert">
                    ${errorMessage}
                </div>
            </c:if>
            <c:if test="${not empty successMessage}">
                <div class="alert alert-success" role="alert">
                    ${successMessage}
                </div>
            </c:if>

            <div class="row">
                <div class="col-md-6">
                    <div class="card mb-4">
                        <h5 class="card-header">Thông tin Hóa đơn</h5>
                        <div class="card-body">
                            <p><strong>Mã Hóa đơn:</strong> ${selectedInvoice.invoiceID}</p>
                            <p><strong>Ngày tạo:</strong> <fmt:formatDate value="${selectedInvoice.invoiceDate}" pattern="dd/MM/yyyy HH:mm:ss" /></p>
                            <p><strong>Tổng tiền:</strong> <fmt:formatNumber value="${selectedInvoice.totalAmount}" pattern="#,##0" /> VNĐ</p>
                            <p><strong>Trạng thái:</strong>
                                <c:choose>
                                    <c:when test="${selectedInvoice.status}">Đã thanh toán</c:when>
                                    <c:otherwise>Chưa thanh toán</c:otherwise>
                                </c:choose>
                            </p>
                            <p><strong>Ghi chú:</strong> ${selectedInvoice.note}</p>
                        </div>
                    </div>
                </div>
                <div class="col-md-6">
                    <div class="card mb-4">
                        <h5 class="card-header">Thông tin Khách hàng & Nhân viên</h5>
                        <div class="card-body">
                            <p><strong>Mã Khách hàng:</strong> ${selectedInvoice.customerID}</p>
                            <%-- Nếu có đối tượng Customer trong request scope, hiển thị tên --%>
                            <c:if test="${not empty selectedCustomer}">
                                <p><strong>Tên Khách hàng:</strong> ${selectedCustomer.customerName}</p>
                                <p><strong>Email Khách hàng:</strong> ${selectedCustomer.email}</p>
                                <p><strong>SĐT Khách hàng:</strong> ${selectedCustomer.phone}</p>
                            </c:if>
                            <p><strong>Mã Nhân viên:</strong> ${selectedInvoice.employeeID}</p>
                            <%-- Nếu có đối tượng Employee trong request scope, hiển thị tên --%>
                            <c:if test="${not empty selectedEmployee}">
                                <p><strong>Tên Nhân viên:</strong> ${selectedEmployee.employeeName}</p>
                                <p><strong>SĐT Nhân viên:</strong> ${selectedEmployee.phone}</p>
                            </c:if>
                        </div>
                    </div>
                    <div class="card mb-4">
                        <h5 class="card-header">Thông tin Cửa hàng</h5>
                        <div class="card-body">
                            <p><strong>Mã Cửa hàng:</strong> ${selectedInvoice.shopID}</p>
                            <c:if test="${not empty selectedShop}">
                                <p><strong>Tên Cửa hàng:</strong> ${selectedShop.shopName}</p>
                                <p><strong>Địa chỉ:</strong> ${selectedShop.address}</p>
                                <p><strong>Điện thoại:</strong> ${selectedShop.phone}</p>
                            </c:if>
                        </div>
                    </div>
                </div>
            </div>

            <hr class="my-4"/>

            <h3 class="mb-3 text-center">Các mặt hàng trong Hóa đơn</h3>
            <table class="table table-bordered">
                <thead>
                    <tr>
                        <th>Mã SP</th>
                        <th>Số lượng</th>
                        <th>Giá bán</th>
                        <th>Giảm giá (%)</th>
                        <th>Thành tiền</th>
                        <th>Hành động</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="detail" items="${invoiceDetails}">
                        <tr>
                            <c:choose>
                                <c:when test="${editDetailID eq detail.invoiceDetailID}">
                                    <%-- CHẾ ĐỘ CHỈNH SỬA: Hiển thị Input fields và nút Lưu/Hủy --%>
                            <form method="post" action="InvoiceServlet" style="margin:0;">
                                <input type="hidden" name="action" value="updateDetail" />
                                <input type="hidden" name="invoiceDetailID" value="${detail.invoiceDetailID}" />
                                <input type="hidden" name="invoiceID" value="${selectedInvoice.invoiceID}" />
                                <%-- productID có thể là dropdown hoặc input nếu không có list sản phẩm --%>
                                <td><input type="text" class="form-control form-control-sm" name="productID" value="${detail.productID}" required /></td>
                                <td><input type="number" class="form-control form-control-sm" name="quantity" value="${detail.quantity}" min="1" required /></td>
                                <td><input type="number" class="form-control form-control-sm" name="unitPrice" value="${detail.unitPrice}" step="any" min="0" required /></td>
                                <td><input type="number" class="form-control form-control-sm" name="discount" step="any" value="${detail.discount}" min="0" max="100" /></td>
                                <td><fmt:formatNumber value="${detail.totalPrice}" pattern="#,##0" /> VNĐ</td>
                                <td>
                                    <button type="submit" class="btn btn-success btn-sm">Lưu</button>
                                    <a href="InvoiceServlet?action=listDetail&invoiceID=${selectedInvoice.invoiceID}" class="btn btn-secondary btn-sm">Hủy</a>
                                </td>
                            </form>
                        </c:when>
                        <c:otherwise>
                            <%-- CHẾ ĐỘ XEM: Hiển thị Text và nút Sửa/Xóa --%>
                            <td>${detail.productID}</td>
                            <td>${detail.quantity}</td>
                            <td><fmt:formatNumber value="${detail.unitPrice}" pattern="#,##0" /> VNĐ</td>
                            <td>${detail.discount} %</td>
                            <td><fmt:formatNumber value="${detail.totalPrice}" pattern="#,##0" /> VNĐ</td>
                            <td>
                                <a href="InvoiceServlet?action=listDetail&invoiceID=${selectedInvoice.invoiceID}&editDetailID=${detail.invoiceDetailID}" class="btn btn-info btn-sm">Sửa</a>
                                <form method="post" action="InvoiceServlet" style="display:inline;">
                                    <input type="hidden" name="action" value="deleteDetail" />
                                    <input type="hidden" name="invoiceDetailID" value="${detail.invoiceDetailID}" />
                                    <input type="hidden" name="invoiceID" value="${selectedInvoice.invoiceID}" />
                                    <button type="submit" class="btn btn-danger btn-sm" onclick="return confirm('Bạn có chắc muốn xóa chi tiết hóa đơn này?');">Xóa</button>
                                </form>
                            </td>
                        </c:otherwise>
                    </c:choose>
                    </tr>
                </c:forEach>
                <c:if test="${empty invoiceDetails}">
                    <tr>
                        <td colspan="6" class="text-center">Chưa có mặt hàng nào trong hóa đơn này.</td>
                    </tr>
                </c:if>
                </tbody>
            </table>

            <hr class="my-4"/>

            <h3 class="mb-3 text-center">Thêm mặt hàng vào hóa đơn</h3>
            <div class="card mb-4">
                <div class="card-body">
                    <form action="InvoiceServlet" method="post">
                        <input type="hidden" name="action" value="addDetail" />
                        <input type="hidden" name="invoiceID" value="${selectedInvoice.invoiceID}" />

                        <div class="mb-3">
                            <label for="productID" class="form-label">Mã sản phẩm:</label>
                            <input type="text" class="form-control" id="productID" name="productID" placeholder="Mã SP" required />
                        </div>
                        <%-- Mã shop cho chi tiết hóa đơn thường không cần thiết ở đây nếu shop của hóa đơn đã được xác định.
                             Tuy nhiên, nếu một hóa đơn có thể có các mặt hàng từ các shop khác nhau, bạn có thể giữ lại.
                             Nếu không, có thể bỏ qua hoặc lấy từ selectedInvoice.shopID.
                             Tạm thời tôi sẽ bỏ nó khỏi form, bạn có thể thêm lại nếu cần logic phức tạp hơn.
                        --%>
                        <%-- <div class="mb-3">
                            <label for="shopID" class="form-label">Mã shop:</label>
                            <input type="text" class="form-control" id="shopID" name="shopID" placeholder="Mã shop" value="${selectedInvoice.shopID}" required />
                        </div> --%>
                        <div class="mb-3">
                            <label for="quantity" class="form-label">Số lượng:</label>
                            <input type="number" class="form-control" id="quantity" name="quantity" placeholder="Số lượng" min="1" required />
                        </div>
                        <div class="mb-3">
                            <label for="unitPrice" class="form-label">Đơn giá:</label>
                            <input type="number" class="form-control" id="unitPrice" name="unitPrice" placeholder="Đơn giá" step="any" min="0" required />
                        </div>
                        <div class="mb-3">
                            <label for="discount" class="form-label">Giảm giá (%):</label>
                            <input type="number" class="form-control" id="discount" name="discount" placeholder="Giảm giá (%)" step="any" min="0" max="100" value="0" />
                        </div>
                        <button type="submit" class="btn btn-primary">Thêm mặt hàng</button>
                    </form>
                </div>
            </div>

            <p class="text-center mt-4"><a href="InvoiceServlet" class="btn btn-secondary">Quay lại danh sách Hóa đơn</a></p>
        </div>

        <%-- Bootstrap Bundle with Popper --%>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
               
               
           </div>
            <!-- Content wrapper -->
          <div class="content-wrapper">
              
              
              
            <!-- Content -->
                </div>
           <jsp:include page="footer.jsp" />
            </div>
        
        </div>


        <script src="assets/vendor/libs/jquery/jquery.js"></script>
<script src="assets/vendor/libs/popper/popper.js"></script>
<script src="assets/vendor/js/bootstrap.js"></script>
<script src="assets/vendor/libs/perfect-scrollbar/perfect-scrollbar.js"></script>
<script src="assets/vendor/js/menu.js"></script> <!-- X? lï¿½ toggle -->
<script src="assets/js/main.js"></script> <!-- Main logic -->

    
        </body>
</html>