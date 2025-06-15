<%-- 
    Document   : AddTransferReceipt
    Created on : Jun 11, 2025, 6:00:00 AM
    Author     : ADMIN
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en"
      class="light-style layout-menu-fixed"
      dir="ltr"
      data-theme="theme-default"
      data-assets-path="../assets/"
      data-template="vertical-menu-template-free">
    <head>
        <meta charset="utf-8" />
        <meta
            name="viewport"
            content="width=device-width, initial-scale=1.0, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0"
            />

        <title>Basic Inputs - Forms | Sneat - Bootstrap 5 HTML Admin Template - Pro</title>

        <meta name="description" content="" />

        <!-- Favicon -->
        <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/assets/img/favicon/favicon.ico" />

        <!-- Fonts -->
        <link rel="preconnect" href="https://fonts.googleapis.com" />
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
        <link
            href="https://fonts.googleapis.com/css2?family=Public+Sans:ital,wght@0,300;0,400;0,500;0,600;0,700;1,300;1,400;1,500;1,600;1,700&display=swap"
            rel="stylesheet"
            />

        <!-- Icons. Uncomment required icon fonts -->
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/vendor/fonts/boxicons.css" />

        <!-- Core CSS -->
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/vendor/css/core.css" class="template-customizer-core-css" />
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/vendor/css/theme-default.css" class="template-customizer-theme-css" />
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/demo.css" />

        <!-- Vendors CSS -->
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/vendor/libs/perfect-scrollbar/perfect-scrollbar.css" />

        <!-- Page CSS -->

        <!-- Helpers -->
        <script src="${pageContext.request.contextPath}/assets/vendor/js/helpers.js"></script>

        <!--! Template customizer & Theme config files MUST be included after core stylesheets and helpers.js in the <head> section -->
        <!--? Config:  Mandatory theme config file contain global vars & default theme options, Set your preferred theme option in this file.  -->
        <script src="${pageContext.request.contextPath}/assets/js/config.js"></script>
    </head>
    <body>
        <!-- Form controls -->
        <div class="row">
            <div class="col-12">
                <div class="card mb-4">
                    <h5 class="card-header">ADD NEW TRANSFER</h5>
                    <div class="card-body">
                        <form action="TransferReceipt" method="POST" class="row g-3 align-items-end">
                            <div class="col-md-3">
                                <label for="FromShop" class="form-label">From Shop</label>
                                <select name="FromShopID" class="form-select" onchange="this.form.submit()">
                                    <option>Select from Shop</option>
                                    <c:forEach var="c" items="${listShop}">
                                        <option value="${c.shopID}" ${c.shopID == select ? 'selected' : ''}>${c.shopName}</option>
                                    </c:forEach>
                                </select>
                            </div>

                            <div class="col-md-3">
                                <label for="ToShop" class="form-label">To Shop</label>
                                <select name="ToShopID" class="form-select" onchange="this.form.submit()">
                                    <option selected>Select To Shop</option>
                                    <c:forEach var="c" items="${listShop}">
                                        <option value="${c.shopID}" ${c.shopID == toShopSelect ? 'selected' : ''}>${c.shopName}</option>
                                    </c:forEach>
                                </select>
                            </div>

                            <div class="col-md-4">
                                <label for="Note" class="form-label">Note</label>
                                <input class="form-control" type="text" name="Note" value="${Note}" placeholder="Note" onchange="this.form.submit()">
                            </div>

                            <!-- Các hidden để giữ lại thông tin -->
                            <input type="hidden" name="service" value="addTransferReceipt" />
                        </form>
                    </div>
                </div>
            </div>


            <div class="col-md-6">

                <div class="card mb-4">
                    <h6 class="pb-1 mb-4 text-muted">List Product</h6>

                    <div class="row row-cols-1 row-cols-md-3 g-4 mb-5">
                        <c:choose>
                            <c:when test="${select != null}">
                                <c:forEach var="i" items="${vectorI}">
                                    <div class="col">
                                        <div class="card h-100">
                                            <c:forEach var="p" items="${vectorP}">
                                                <c:if test="${i.product.productID == p.productID}">
                                                    <img class="card-img-top" src="${p.imageUrl}" alt="${p.productName}" style="object-fit:cover; " />

                                                    <div class="card-body">
                                                        <h5 class="card-title">${p.productName}</h5>
                                                        <p class="card-text">
                                                            ${p.description}
                                                        </p>
                                                    </div>
                                                </c:if>
                                            </c:forEach>
                                            <form action="TransferReceipt" method="POST">
                                                <div>
                                                    <button type="submit" name="addProduct" value="Add" class="btn btn-outline-primary w-100">Add</button>
                                                    <input type="hidden" name="productID" value="${i.product.productID}">
                                                    <input type="hidden" name="service" value="addTransferReceipt">
                                                    <input type="hidden" name="FromShopID" value="${select}">
                                                    <input type="hidden" name="ToShopID" value="${toShopSelect}">
                                                    <input type="hidden" name="Note" value="${Note}">
                                                </div>    
                                            </form>
                                        </div>

                                    </div>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <c:forEach var="p" items="${vectorP}">
                                    <div class="col">
                                        <div class="card h-100">

                                            <img class="card-img-top" src="${p.imageUrl}" alt="${p.productName}" style="object-fit:cover; "  />

                                            <div class="card-body">
                                                <h5 class="card-title">${p.productName}</h5>
                                                <p class="card-text">
                                                    ${p.description}
                                                </p>
                                            </div>

                                        </div>
                                    </div>
                                </c:forEach>
                            </c:otherwise>
                        </c:choose>
                    </div>

                </div>
            </div> <div class="col-12">
                <div class="card mb-4">
                    <h5 class="card-header">List Transfer</h5>
                    <div class="table-responsive text-nowrap">
                        <form action="TransferReceipt" method="POST">
                            <table class="table table-bordered table-hover text-center align-middle">
                                <thead class="table-light">
                                    <tr>
                                        <th>TransferReceiptDetailID</th>
                                        <th>TransferReceiptID</th>
                                        <th>ProductID</th>
                                        <th>Quantity</th>
                                        <th>Action</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="d" items="${ListAddToCartTransfer}">
                                        <tr>
                                            <td><strong>${d.transferReceiptDetailID}</strong></td>
                                            <td>${d.transferReceiptID}</td>
                                            <td>${d.productID}</td>
                                            <td>
                                                <input type="text" name="Quantity" value="${d.quantity}" 
                                                       class="form-control form-control-sm text-center" style="width: 80px; margin: auto;">
                                                <input type="hidden" name="TransferReceiptDetailID" value="${d.transferReceiptDetailID}">
                                                <input type="hidden" name="updateQuantity" value="updateQuantity">
                                                <input type="hidden" name="service" value="addTransferReceipt">
                                                <input type="hidden" name="FromShopID" value="${select}">
                                                <input type="hidden" name="ToShopID" value="${toShopSelect}">
                                                <input type="hidden" name="Note" value="${Note}">
                                            </td>
                                            <td>
                                                <button type="submit" name="remove" value="remove" class="btn btn-sm btn-outline-danger">
                                                    Remove
                                                </button>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                            <div class="d-flex justify-content-end mt-3">
                                <button type="submit" class="btn btn-primary" name="submit" value="submit">
                                    Create Transfer
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>



        </div>






        <!-- Core JS -->
        <!-- build:js assets/vendor/js/core.js -->
        <script src="${pageContext.request.contextPath}/assets/vendor/libs/jquery/jquery.js"></script>
        <script src="${pageContext.request.contextPath}/assets/vendor/libs/popper/popper.js"></script>
        <script src="${pageContext.request.contextPath}/assets/vendor/js/bootstrap.js"></script>
        <script src="${pageContext.request.contextPath}/assets/vendor/libs/perfect-scrollbar/perfect-scrollbar.js"></script>

        <script src="${pageContext.request.contextPath}/assets/vendor/js/menu.js"></script>
        <!-- endbuild -->

        <!-- Vendors JS -->

        <!-- Main JS -->
        <script src="${pageContext.request.contextPath}/assets/js/main.js"></script>

        <!-- Page JS -->

        <script src="${pageContext.request.contextPath}/assets/js/form-basic-inputs.js"></script>

        <!-- Place this tag in your head or just before your close body tag. -->
        <script async defer src="https://buttons.github.io/buttons.js"></script>

    </body>
</html>
