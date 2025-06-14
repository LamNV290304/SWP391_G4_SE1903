<%-- 
    Document   : AddTransfer
    Created on : May 30, 2025, 9:23:44 AM
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
        <form action="TransferReceipt" method="POST">
            <div class="row">
                <!-- LEFT SIDE: Select Product -->
                <div class="col-md-6">
                    <div class="card mb-4">
                        <h5 class="card-header">Update Transfer</h5>
                        <div class="card-body">
                            <div class="mb-3">
                                <label for="ProductID" class="form-label">ProductName</label>

                                <c:forEach var="c" items="${vectorP}">
                                    <c:if test="${c.productID == p.productID}">
                                        <input class="form-control" type="text" name="ProductName" value="${c.productName}" readonly>
                                        <input type="hidden" name="ProductID" value="${p.productID}">
                                    </c:if>
                                </c:forEach>
                            </div>

                            <div class="mb-3">
                                <label for="FromInventoryID" class="form-label">FromInventoryID</label>
                                <select name="FromInventoryID" class="form-select" >
                                    <option selected>Select from Shop</option>
                                    <c:forEach var="c" items="${vectorI}">
                                        <option value="${c.inventoryID}" ${c.inventoryID == p.fromInventoryID ? 'selected' : ''}>
                                            ${c.shop.shopName}
                                        </option>
                                    </c:forEach>
                                </select>

                            </div>
                            
                            <div class="mb-3">
                                <label for="toInventoryID" class="form-label">ToInventoryID</label>
                                <select name="toInventoryID" class="form-select" >
                                    <option selected>Select from Shop</option>
                                    <c:forEach var="c" items="${vectorI}">
                                        <option value="${c.inventoryID}" ${c.inventoryID == p.toInventoryID ? 'selected' : ''}>
                                            ${c.shop.shopName}
                                        </option>
                                    </c:forEach>
                                </select>

                            </div>

                            <div class="mb-3">
                                <label for="ToInventoryID" class="form-label">ToInventoryID</label>
                                <select name="ToInventoryID" class="form-select">
                                    <option >Select to Shop</option>
                                    <c:forEach var="c" items="${vectorI}" >
                                        <option value="${c.inventoryID}" ${c.inventoryID == p.toInventoryID ? 'selected' : ''}>
                                            ${c.shop.shopName}
                                        </option>
                                    </c:forEach>
                                </select>
                            </div>

                            <div class="mb-3">
                                <label for="Quantity" class="form-label">Quantity</label>
                                <input class="form-control" type="text" name="Quantity" value="${p.quantity}">
                            </div>

                            <div class="mb-3">
                                <label for="Note" class="form-label">Note</label>
                                <input class="form-control" type="text" name="Note" value="${p.note}">
                            </div>


                            <div>
                                <input type="submit" name="submit" value="Update Transfer" class="btn btn-primary">
                                <input type="hidden" name="TransferReceiptID" value="${p.transferReceiptID}">
                                <input type="hidden" name="service" value="updateTransferReceipt">

                            </div>
                        </div>
                    </div>
                </div>

            </div>
        </form>

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
