<%-- 
    Document   : ListTransferReceiptDetail
    Created on : Jun 12, 2025, 12:35:47 PM
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
                            <h4 class="fw-bold py-3 mb-4"><span class="text-muted fw-light"></span> Transfer Receipt Detail</h4>
                            <!-- Basic Bootstrap Table -->
                            <div class="card">
                                <h5 class="card-header">List Transfer Detail</h5>
                                <div class="table-responsive text-nowrap">
                                    <table class="table">
                                        <thead>
                                            <tr>
                                                <th>DetailID</th>
                                                <th>TransferID</th>
                                                <th>Product</th>
                                                <th>Quantity</th>
                                            </tr>
                                        </thead>
                                        <tbody class="table-border-bottom-0">
                                            <c:forEach var="d" items="${listDetail}">
                                                <tr>
                                                    <td><i class="fab fa-angular fa-lg text-danger me-3"></i> <strong>${d.transferReceiptDetailID}</strong></td>
                                                    <td><i class="fab fa-angular fa-lg text-danger me-3"></i> <strong>${d.transferReceiptID}</strong></td>
                                                    <c:forEach var="p" items="${vectorP}">
                                                        <c:if test="${p.productID == d.productID}">
                                                            <td><i class="fab fa-angular fa-lg text-danger me-3"></i> <strong>${p.productName}</strong></td>
                                                        </c:if>
                                                    </c:forEach>
                                                    <td><i class="fab fa-angular fa-lg text-danger me-3"></i> <strong>${d.quantity}</strong></td>
                                                </tr>
                                            </c:forEach>


                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="container-xxl flex-grow-1 container-p-y">
            <h4 class="fw-bold py-3 mb-4"><span class="text-muted fw-light"></span> Transfer Receipt Detail</h4>
            <!-- Basic Bootstrap Table -->
            <div class="card">
                <h5 class="card-header">List Transfer Detail</h5>
                <div class="table-responsive text-nowrap">
                    <table class="table">
                        <thead>
                            <tr>
                                <th>DetailID</th>
                                <th>TransferID</th>
                                <th>Product</th>
                                <th>Quantity</th>
                            </tr>
                        </thead>
                        <tbody class="table-border-bottom-0">
                            <c:forEach var="d" items="${listDetail}">
                                <tr>
                                    <td><i class="fab fa-angular fa-lg text-danger me-3"></i> <strong>${d.transferReceiptDetailID}</strong></td>
                                    <td><i class="fab fa-angular fa-lg text-danger me-3"></i> <strong>${d.transferReceiptID}</strong></td>
                                    <c:forEach var="p" items="${vectorP}">
                                        <c:if test="${p.productID == d.productID}">
                                            <td><i class="fab fa-angular fa-lg text-danger me-3"></i> <strong>${p.productName}</strong></td>
                                        </c:if>
                                    </c:forEach>
                                    <td><i class="fab fa-angular fa-lg text-danger me-3"></i> <strong>${d.quantity}</strong></td>
                                </tr>
                            </c:forEach>


                        </tbody>
                    </table>
                </div>
            </div>
        </div>





        <jsp:include page="LinkJS.jsp" />
    </body>
</html>
