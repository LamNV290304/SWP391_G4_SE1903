<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
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
                    <jsp:include page="navBar.jsp" />
                  
           <!-- / Navbar -->

            <!-- Content wrapper -->
          
                <div class="container" style="padding-top: 20px;"> 
                  <!-- Responsive Table -->
             <div class="card col-sm-12" style="height: 90vh; overflow: hidden;">
                 <div>
                     <h5 class="card-header">Phiếu Nhập Mua Hàng
                         <a href="AddImportReceipt" >
                             <button type="button" class="btn btn-outline-info">Thêm mới phiếu nhập mua hàng</button>
                         </a>
                         
                     </h5>
                     
                 </div>
  
  
  <div class="table-responsive text-nowrap" style="height: calc(80vh - 80px); overflow-y: auto;">
    <table class="table">
                    <thead style="position: sticky ; top: 0; background-color: white; z-index: 20;">
                      <tr class="text-nowrap">
                        <th>#</th>
                        <th>Mã Phiếu</th>
                        <th>Ngày Muốn Nhận</th>
                        <th>Kho Hàng</th>
                        <th>Giá Trị Mặt hàng</th>
                        <th>Nhà cung cấp</th>
                        <th>Nhân Viên Nhận</th>
                        <th>Ghi chú</th>
                        <th>Hành động</th>
                      </tr>
                    </thead>
                    <%! int i = 1; %> 
                    <tbody id="vertical-example">
                           <c:forEach var="ir" items="${listIR}">
                      <tr>
                        <th scope="row"><%out.println( i); i++;%>
                          </th>
                        <td>${ir.importReceiptID}</td>
                        <td>${ir.receiptDate}</td>
                        <td>${ir.shopID}</td>
                        <td>${ir.totalAmount}</td>
                        <td>${ir.supplierID}</td>
                        <td>${ir.employeeID}</td>
                        <td>${ir.note}</td>
                        <td>
                          <div class="dropdown">
                            <button type="button" class="btn p-0 dropdown-toggle hide-arrow" data-bs-toggle="dropdown">
                              <i class="bx bx-dots-vertical-rounded"></i>
                            </button>
                            <div class="dropdown-menu">
                                <form class="dropdown-item" action="AddImportReceipt" >
                                    <input type="hidden" name="action" value="edit" />
                                    <input type="hidden" name="receiptId" value="${ir.importReceiptID}" />
                                    <button class="btn btn-secondary" type="submit"><i>Edit</i></button>
                                </form>
                                <form class="dropdown-item" action="AddImportReceipt" method="GET">
                                    <input type="hidden" name="action" value="delete" />
                                    <input type="hidden" name="receiptId" value="${ir.importReceiptID}" />
                                    <button class="btn btn-secondary" type="submit"><i>Delete</i></button>
                                </form>
                            </div>
                          </div>
                        </td>
                      </tr>
                      </c:forEach>
                    </tbody>
                  </table>
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
