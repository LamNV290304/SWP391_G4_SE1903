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
        <meta charset="utf-8" />
        <meta
            name="viewport"
            content="width=device-width, initial-scale=1.0, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0"
            />

        <title>Tables - Basic Tables | Sneat - Bootstrap 5 HTML Admin Template - Pro</title>

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

        <nav
            class="layout-navbar container-xxl navbar navbar-expand-xl navbar-detached align-items-center bg-navbar-theme"
            id="layout-navbar"
            >
            <div class="layout-menu-toggle navbar-nav align-items-xl-center me-3 me-xl-0 d-xl-none">
                <a class="nav-item nav-link px-0 me-xl-4" href="javascript:void(0)">
                    <i class="bx bx-menu bx-sm"></i>
                </a>
            </div>

            <div class="navbar-nav-right d-flex align-items-center" id="navbar-collapse">
                <form action="TransferReceipt" method="POST">
                    <!-- Search -->
                    <div class="navbar-nav align-items-center">
                        <div class="nav-item d-flex align-items-center">
                            <i class="bx bx-search fs-4 lh-0"></i>
                            <input
                                type="text"
                                class="form-control border-0 shadow-none"
                                placeholder="Search..."
                                aria-label="Search..."
                                name="search"
                                value="${param.search != null ? param.search : ''}"

                                />
                            <input type="hidden" name="submit" value="listProcessTransferReceipt">
                            <input type="hidden" name="service" value="listProcessTransferReceipt">
                        </div>
                    </div>
                    <!-- /Search -->
                </form>

            </div>
        </nav>

        <div class="container-xxl flex-grow-1 container-p-y">
            <h4 class="fw-bold py-3 mb-4"><span class="text-muted fw-light"></span> Transfer Receipt</h4>
            <div class="mb-3 d-flex justify-content-end gap-2">
                <a href="TransferReceipt?service=listCompleteTransferReceipt" class="btn btn-outline-secondary">
                    <i class="bx bx-check-circle me-1"></i> Completed TransferReceipt
                </a>
                <a href="TransferReceipt?service=addTransferReceipt" class="btn btn-primary">
                    <i class="bx bx-plus me-1"></i> Add new
                </a>

                
            </div>
            <!-- Basic Bootstrap Table -->
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
                                <th>Actions</th>
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

                                    <td>
                                        <c:if test="${p.status == 0}">
                                            <div class="dropdown">
                                                <button type="button" class="btn p-0 dropdown-toggle hide-arrow" data-bs-toggle="dropdown">
                                                    <i class="bx bx-dots-vertical-rounded"></i>
                                                </button>
                                                <div class="dropdown-menu">
                                                    <a class="dropdown-item" href="TransferReceipt?service=updateTransferReceipt&TransferReceiptID=${p.transferReceiptID}"
                                                       ><i class="bx bx-edit-alt me-1" ></i> Edit</a
                                                    >
                                                    <a class="dropdown-item" href="TransferReceipt?service=Detail&TransferReceiptID=${p.transferReceiptID}"
                                                       ><i class="bx bx-edit-alt me-1" ></i> Detail</a
                                                    >
                                                    <a class="dropdown-item" href="TransferReceipt?service=deleteTransferReceipt&TransferReceiptID=${p.transferReceiptID}"
                                                       ><i class="bx bx-trash me-1"></i> Delete</a
                                                    >
                                                </div>
                                            </div>

                                        </c:if>
                                    </td>
                                    <td>
                                        <c:if test="${p.status == 0}">
                                            <form action="TransferReceipt" method="Get">
                                                <input type="hidden" name="service" value="updateStatus">
                                                <input type="hidden" name="setStatus" value="accept">
                                                <input type="hidden" name="TransferReceiptID" value="${p.transferReceiptID}">
                                                <button type="submit" class="btn btn-success btn-sm">✔</button>
                                            </form>
                                        </c:if>
                                    </td>
                                    <td>
                                        <c:if test="${p.status == 0}">
                                            <form action="TransferReceipt" method="Get">
                                                <input type="hidden" name="service" value="updateStatus">
                                                <input type="hidden" name="setStatus" value="reject">
                                                <input type="hidden" name="TransferReceiptID" value="${p.transferReceiptID}">
                                                <button type="submit" class="btn btn-danger btn-sm">✖</button>
                                            </form>
                                        </c:if>
                                    </td>

                                </tr>
                            </c:forEach>


                        </tbody>
                    </table>
                </div>
            </div>
        </div>
        <!--/ Basic Bootstrap Table -->

        <hr class="my-5" />

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

        <!-- Place this tag in your head or just before your close body tag. -->
        <script async defer src="https://buttons.github.io/buttons.js"></script>
    </body>
</html>

