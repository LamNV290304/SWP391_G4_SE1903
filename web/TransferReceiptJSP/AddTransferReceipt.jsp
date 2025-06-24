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

        <jsp:include page="LinkCSS.jsp" />
    </head>
    <body>
        <div class="layout-wrapper layout-content-navbar">
            <div class="layout-container">
                <jsp:include page="menu.jsp" />
                <div class="layout-page">
                    <jsp:include page="navbar.jsp" />

                    <div class="content-wrapper">

                        <div class="container-xxl flex-grow-1 container-p-y">
                            <div class="w-100 d-block text-start ps-3 mt-3 mb-3">
                                <a href="TransferReceipt?service=listProcessTransferReceipt" class="btn btn-outline-secondary">
                                    <i class="bx bx-arrow-back me-1"></i> Back to List
                                </a>
                            </div>

                            <div class="col-12">
                                <div class="card mb-4">
                                    <h5 class="card-header">ADD NEW TRANSFER</h5>
                                    <div class="card-body">
                                        <form action="TransferReceipt" method="POST" class="row g-3 align-items-end">
                                            <!-- From Shop -->
                                            <div class="col-md-3">
                                                <label for="FromShop" class="form-label">From Shop</label>
                                                <select name="FromShopID" class="form-select" onchange="this.form.submit()">
                                                    <option disabled ${ select == 0 ? 'selected' : ''}>Select from Shop</option>
                                                    <c:forEach var="c" items="${listShop}">
                                                        <option value="${c.shopID}" ${c.shopID == select ? 'selected' : ''}>${c.shopName}</option>
                                                    </c:forEach>
                                                </select>
                                            </div>

                                            <!-- To Shop -->
                                            <div class="col-md-3">
                                                <label for="ToShop" class="form-label">To Shop</label>
                                                <select name="ToShopID" class="form-select" onchange="this.form.submit()">
                                                    <option disabled ${ toShopSelect == 0 ? 'selected' : ''}>Select To Shop</option>
                                                    <c:forEach var="c" items="${listShop}">
                                                        <option value="${c.shopID}" ${c.shopID == toShopSelect ? 'selected' : ''}>${c.shopName}</option>
                                                    </c:forEach>
                                                </select>
                                            </div>

                                            <!-- Note -->
                                            <div class="col-md-4">
                                                <label for="Note" class="form-label">Note</label>
                                                <input class="form-control" type="text" name="Note" value="${Note}" placeholder="Note" onchange="this.form.submit()">
                                            </div>

                                            <!-- Hidden -->
                                            <input type="hidden" name="service" value="addTransferReceipt" />
                                        </form>

                                    </div>
                                </div>
                            </div>

                            <div class="row align-items-stretch">
                                <div class="col-md-6 d-flex">
                                    <div class="card mb-4 w-100 h-100" >

                                        <h5 class="card-header">List Product</h5>
                                        <c:if test="${select != 0}">
                                            <!-- Search bar -->
                                            <form action="TransferReceipt" method="POST" class="mb-3 d-flex justify-content-end">
                                                <input type="text" name="searchProduct" class="form-control w-25 me-2" placeholder="Search product..." value="${searchProduct}">
                                                <input type="hidden" name="service" value="addTransferReceipt">
                                                <input type="hidden" name="FromShopID" value="${select}">
                                                <input type="hidden" name="ToShopID" value="${toShopSelect}">
                                                <input type="hidden" name="Note" value="${Note}">
                                                <button type="submit" name="search" value="search" class="btn btn-primary">Search</button>
                                            </form>

                                            <div class="row row-cols-1 row-cols-md-3 g-4 mb-5">


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

                                            </div>
                                        </c:if>

                                    </div>
                                </div> 
                                <!-- BÊN PHẢI: List Transfer -->

                                <div class="col-md-6 d-flex">
                                    <div class="card mb-4 w-100 h-100 d-flex flex-column">
                                        <h5 class="card-header">List Transfer</h5>

                                        <!-- TABLE DỮ LIỆU -->
                                        <div class="table-responsive text-nowrap flex-grow-1">
                                            <table class="table table-bordered table-hover text-center align-middle mb-0">
                                                <thead class="table-light">
                                                    <tr>
                                                        <th>TransferReceiptID</th>
                                                        <th>ProductID</th>
                                                        <th>Quantity</th>
                                                        <th>Action</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <c:forEach var="d" items="${ListAddToCartTransfer}">

                                                        <tr>

                                                            <td>${d.transferReceiptID}</td>
                                                            <td>${d.productID}</td>

                                                            <td>
                                                                <form action="TransferReceipt" method="POST" class="d-flex flex-column flex-grow-1">
                                                                    <input type="text" name="Quantity" value="${d.quantity}" 
                                                                           class="form-control form-control-sm text-center" style="width: 80px; margin: auto;" >
                                                                    <input type="hidden" name="ProductID" value="${d.productID}">
                                                                    <input type="hidden" name="updateQuantity" value="updateQuantity">
                                                                    <input type="hidden" name="service" value="addTransferReceipt">
                                                                    <input type="hidden" name="FromShopID" value="${select}">
                                                                    <input type="hidden" name="ToShopID" value="${toShopSelect}">
                                                                    <input type="hidden" name="Note" value="${Note}">
                                                                </form>
                                                            </td>
                                                            <td>
                                                                <form action="TransferReceipt" method="POST" class="d-flex flex-column flex-grow-1">
                                                                    <button type="submit" name="remove" value="remove" class="btn btn-sm btn-outline-danger">
                                                                        Remove
                                                                    </button>
                                                                    <input type="hidden" name="ProductID" value="${d.productID}">
                                                                    <input type="hidden" name="service" value="addTransferReceipt">
                                                                    <input type="hidden" name="FromShopID" value="${select}">
                                                                    <input type="hidden" name="ToShopID" value="${toShopSelect}">
                                                                    <input type="hidden" name="Note" value="${Note}">
                                                                </form>
                                                            </td>
                                                        </tr>
                                                    </c:forEach>
                                                </tbody>
                                            </table>
                                        </div>

                                        <!-- NÚT CREATE -->
                                        <form action="TransferReceipt" method="POST" class="d-flex flex-column flex-grow-1">
                                            <div class="mt-auto d-flex justify-content-end p-3">
                                                <button type="submit" class="btn btn-primary" name="submit" value="submit">
                                                    Create Transfer
                                                </button>
                                                <input type="hidden" name="service" value="addTransferReceipt">
                                                <input type="hidden" name="FromShopID" value="${select}">
                                                <input type="hidden" name="ToShopID" value="${toShopSelect}">
                                                <input type="hidden" name="Note" value="${Note}">
                                            </div>
                                        </form>
                                    </div>
                                </div>

                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="w-100 d-block text-start ps-3 mt-3">
            <a href="TransferReceipt?service=listProcessTransferReceipt" class="btn btn-outline-secondary">
                <i class="bx bx-arrow-back me-1"></i> Back to List
            </a>
        </div>







        <jsp:include page="LinkJS.jsp" />

    </body>
</html>
