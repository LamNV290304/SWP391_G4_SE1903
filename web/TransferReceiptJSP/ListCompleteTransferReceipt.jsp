<%-- 
    Document   : ListTransfer
    Created on : May 28, 2025, 5:32:34 AM
    Author     : ADMIN
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html
    lang="en"
    class="light-style layout-menu-fixed"
    dir="ltr"
    data-theme="theme-default"
    data-assets-path="../assets/"
    data-template="vertical-menu-template-free"
    >
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
                            <h4 class="fw-bold py-3 mb-4"><span class="text-muted fw-light"></span> Transfer Receipt</h4>

                            <div class="mb-3 d-flex justify-content-between align-items-center flex-wrap">
                                <!-- Search bên trái -->
                                <form action="TransferReceipt" method="POST" class="d-flex align-items-center mb-2 mb-md-0">
                                    <div class="input-group">
                                        <span class="input-group-text"><i class="bx bx-search"></i></span>
                                        <input
                                            type="text"
                                            class="form-control"
                                            placeholder="Search..."
                                            name="search"
                                            value="${param.search != null ? param.search : ''}" />
                                    </div>
                                    <input type="hidden" name="submit" value="listCompleteTransferReceipt">
                                    <input type="hidden" name="service" value="listCompleteTransferReceipt">
                                </form>

                                <!-- Nút bên phải -->
                                <div class="d-flex gap-2">
                                    <a href="TransferReceipt?service=listProcessTransferReceipt" class="btn btn-outline-secondary">
                                        <i class="bx bx-check-circle me-1"></i> List TransferReceipt
                                    </a>
                                    <a href="TransferReceipt?service=addTransferReceipt" class="btn btn-primary">
                                        <i class="bx bx-plus me-1"></i> Add new
                                    </a>
                                </div>
                            </div>


                            <div class="card">
                                <h5 class="card-header">List Transfer</h5>
                                <div class="table-responsive text-nowrap">
                                    <table class="table">
                                        <thead>
                                            <tr>
                                                <th>ID</th>
                                                <th>From</th>
                                                <th>To</th>
                                                <th>Date</th>
                                                <th>Note</th>
                                                <th>Status</th>
                                            </tr>
                                        </thead>
                                        <tbody class="table-border-bottom-0">
                                            <c:forEach var="p" items="${data}">
                                                <tr>
                                                    <td><i class="fab fa-angular fa-lg text-danger me-3"></i> <strong>${p.transferReceiptID}</strong></td>

                                                    <td>
                                                        <c:forEach var="c" items="${vectorS}">
                                                            <div value="${c.shopID}">
                                                                <c:if test="${p.fromShopID == c.shopID}">${c.shopName}</c:if>
                                                                </div>
                                                        </c:forEach>
                                                    </td>
                                                    <td>
                                                        <c:forEach var="c" items="${vectorS}">
                                                            <div value="${c.shopID}">
                                                                <c:if test="${p.toShopID == c.shopID}">${c.shopName}</c:if>
                                                                </div>
                                                        </c:forEach>
                                                    </td>
                                                    <td>${p.transferDate}</td>
                                                    <td>${p.note}</td>
                                                    <td>
                                                        <c:choose>
                                                            <c:when test="${p.status == 1}">
                                                                Accepted
                                                            </c:when>
                                                            <c:when test="${p.status == 2}">
                                                                Rejected
                                                            </c:when>
                                                            <c:otherwise>
                                                                Process
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </td>

                                                </tr>
                                            </c:forEach>


                                        </tbody>
                                    </table>
                                </div>

                            </div>
                            <!-- Pagination -->
                            <nav aria-label="Page navigation" class="mt-4">
                                <ul class="pagination justify-content-center">
                                    <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                                        <a class="page-link" href="TransferReceipt?service=listCompleteTransferReceipt&selectPage=${currentPage - 1}&search=${currentSearch}&submit=Search">&laquo;</a>
                                    </li>

                                    <c:forEach var="i" begin="1" end="${page}">
                                        <li class="page-item ${i == currentPage ? 'active' : ''}">
                                            <a class="page-link" href="TransferReceipt?service=listCompleteTransferReceipt&selectPage=${i}&search=${currentSearch}&submit=Search">${i}</a>
                                        </li>
                                    </c:forEach>

                                    <li class="page-item ${currentPage == page ? 'disabled' : ''}">
                                        <a class="page-link" href="TransferReceipt?service=listCompleteTransferReceipt&selectPage=${currentPage + 1}&search=${currentSearch}&submit=Search">&raquo;</a>
                                    </li>


                                </ul>
                            </nav>
                            <c:if test="${not empty messageStatus}">
                                <div class="d-flex justify-content-center">
                                    <div class="alert alert-info mt-3 w-auto px-4 py-2 text-center" role="alert">
                                        ${messageStatus}
                                    </div>
                                </div>
                            </c:if>
                        </div>
                    </div>
                </div>
            </div>
        </div>




        <jsp:include page="LinkJS.jsp" />
    </body>
</html>

