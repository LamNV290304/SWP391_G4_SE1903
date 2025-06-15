<%-- 
    Document   : showEmployeeDetails
    Created on : Jun 10, 2025, 10:20:19 AM
    Author     : Admin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<!-- =========================================================
* Sneat - Bootstrap 5 HTML Admin Template - Pro | v1.0.0
==============================================================

* Product Page: https://themeselection.com/products/sneat-bootstrap-html-admin-template/
* Created by: ThemeSelection
* License: You must have a valid license purchased in order to legally use the theme for your project.
* Copyright ThemeSelection (https://themeselection.com)

=========================================================
-->
<!-- beautify ignore:start -->
<html
    lang="en"
    class="light-style layout-menu-fixed"
    dir="ltr"
    data-theme="theme-default"
    data-assets-path="./assets/"
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

        <link rel="stylesheet" href="https://cdn.datatables.net/1.13.6/css/dataTables.bootstrap5.min.css">
        <!-- Favicon -->
        <link rel="icon" type="image/x-icon" href="./assets/img/favicon/favicon.ico" />

        <!-- Fonts -->
        <link rel="preconnect" href="https://fonts.googleapis.com" />
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
        <link
            href="https://fonts.googleapis.com/css2?family=Public+Sans:ital,wght@0,300;0,400;0,500;0,600;0,700;1,300;1,400;1,500;1,600;1,700&display=swap"
            rel="stylesheet"
            />

        <!-- Icons. Uncomment required icon fonts -->
        <link rel="stylesheet" href="./assets/vendor/fonts/boxicons.css" />

        <!-- Core CSS -->
        <link rel="stylesheet" href="./assets/vendor/css/core.css" class="template-customizer-core-css" />
        <link rel="stylesheet" href="./assets/vendor/css/theme-default.css" class="template-customizer-theme-css" />
        <link rel="stylesheet" href="./assets/css/demo.css" />

        <!-- Vendors CSS -->
        <link rel="stylesheet" href="./assets/vendor/libs/perfect-scrollbar/perfect-scrollbar.css" />

        <!-- Page CSS -->

        <!-- Helpers -->
        <script src="./assets/vendor/js/helpers.js"></script>

        <!--! Template customizer & Theme config files MUST be included after core stylesheets and helpers.js in the <head> section -->
        <!--? Config:  Mandatory theme config file contain global vars & default theme options, Set your preferred theme option in this file.  -->
        <script src="./assets/js/config.js"></script>
    </head>

    <body>
        <!-- Layout wrapper -->
        <div class="layout-wrapper layout-content-navbar">
            <div class="layout-container">
                <jsp:include page="sidebar.jsp" />
                <!-- Layout container -->
                <div class="layout-page">
                    <jsp:include page="navBar.jsp" />
                    <!-- Content wrapper -->
                    <div class="content-wrapper">
                        <!-- Content -->
                        <div class="col-lg-4 col-md-6">
                            <div class="mt-3 ms-3">
                                <!-- Button trigger modal -->
                                <button
                                    type="button"
                                    class="btn btn-primary ms-2"
                                    data-bs-toggle="modal"
                                    data-bs-target="#basicModal"
                                    >
                                    Launch modal
                                </button>

                                <!-- Modal -->
                                <div class="modal fade" id="basicModal" tabindex="-1" aria-hidden="true">
                                    <div class="modal-dialog" role="document">
                                        <div class="modal-content">
                                            <div class="modal-header">
                                                <h5 class="modal-title" id="exampleModalLabel1">Modal title</h5>
                                                <button
                                                    type="button"
                                                    class="btn-close"
                                                    data-bs-dismiss="modal"
                                                    aria-label="Close"
                                                    ></button>
                                            </div>
                                            <div class="modal-body">
                                                <div class="row">
                                                    <div class="col mb-3">
                                                        <label for="nameBasic" class="form-label">Name</label>
                                                        <input type="text" id="nameBasic" class="form-control" placeholder="Enter Name" />
                                                    </div>
                                                </div>
                                                <div class="row g-2">
                                                    <div class="col mb-0">
                                                        <label for="emailBasic" class="form-label">Email</label>
                                                        <input type="text" id="emailBasic" class="form-control" placeholder="xxxx@xxx.xx" />
                                                    </div>
                                                    <div class="col mb-0">
                                                        <label for="dobBasic" class="form-label">DOB</label>
                                                        <input type="text" id="dobBasic" class="form-control" placeholder="DD / MM / YY" />
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="modal-footer">
                                                <button type="button" class="btn btn-outline-secondary" data-bs-dismiss="modal">
                                                    Close
                                                </button>
                                                <button type="submit" class="btn btn-primary">Save changes</button>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="container-xxl flex-grow-1 container-p-y">
                            <div class="row mb-3">

                                <div class="col-md-4">
                                    <form method="get" action="showEmployeeDetails.jsp">
                                        <div class="input-group">
                                            <input type="text" name="search" class="form-control" placeholder="Tìm kiếm tên hoặc username..." value="${param.search}">
                                            <button class="btn btn-outline-secondary" type="submit">Tìm</button>
                                        </div>
                                    </form>
                                </div>
                                <div class="col-md-3">
                                    <form method="get" action="showEmployeeDetails.jsp">
                                        <select class="form-select" name="sortBy" onchange="this.form.submit()">
                                            <option value="EmployeeID" ${param.sortBy == 'EmployeeID' ? 'selected' : ''}>Mã nhân viên</option>
                                            <option value="Username" ${param.sortBy == 'Username' ? 'selected' : ''}>Username</option>
                                            <option value="Fullname" ${param.sortBy == 'Fullname' ? 'selected' : ''}>Họ tên</option>
                                            <option value="CreateDate" ${param.sortBy == 'CreateDate' ? 'selected' : ''}>Ngày tạo</option>
                                        </select>
                                        <input type="hidden" name="search" value="${param.search}">
                                    </form>
                                </div>
                                <div class="col-md-2">
                                    <form method="get" action="showEmployeeDetails.jsp">
                                        <select class="form-select" name="sortDirection" onchange="this.form.submit()">
                                            <option value="ASC" ${param.sortDirection == 'ASC' ? 'selected' : ''}>Tăng dần</option>
                                            <option value="DESC" ${param.sortDirection == 'DESC' ? 'selected' : ''}>Giảm dần</option>
                                        </select>
                                        <input type="hidden" name="search" value="${param.search}">
                                        <input type="hidden" name="sortBy" value="${param.sortBy}">
                                    </form>
                                </div>
                            </div>
                            <!-- Basic Bootstrap Table -->
                            <div class="card">
                                <div class="table-responsive text-nowrap">
                                    <table id="projectTable" class="table table-striped">
                                        <thead>
                                            <tr>
                                                <th>ID</th>
                                                <th>Full name</th>
                                                <th>Email</th>
                                                <th>Phone</th>
                                                <th>Role</th>
                                                <th>Shop</th>
                                                <th>Create At</th>
                                                <th>Status</th>
                                                <th>Actions</th>
                                            </tr>
                                        </thead>
                                        <tbody class="table-border-bottom-0">

                                            <tr>
                                                <td></td>
                                                <td></td>
                                                <td></td>
                                                <td></td>
                                                <td></td>
                                                <td></td>
                                                <td></td>
                                                <td></td>                                                
                                                <td>
                                                    <div class="dropdown">
                                                        <button type="button" class="btn p-0 dropdown-toggle hide-arrow" data-bs-toggle="dropdown">
                                                            <i class="bx bx-dots-vertical-rounded"></i>
                                                        </button>
                                                        <div class="dropdown-menu">
                                                            <a class="dropdown-item" href="javascript:void(0);"
                                                               ><i class="bx bx-edit-alt me-2"></i> Edit</a
                                                            >
                                                            <a class="dropdown-item" href="javascript:void(0);"
                                                               ><i class="bx bx-trash me-2"></i> Delete</a
                                                            >
                                                        </div>
                                                    </div>
                                                </td>
                                            </tr>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                            <!--/ Basic Bootstrap Table -->

                            <nav aria-label="Page navigation" class="mt-3">
                                <ul class="pagination justify-content-center">

                                    <c:if test="${currentPage > 1}">
                                        <li class="page-item">
                                            <a class="page-link" href="?page=${currentPage - 1}">&laquo; Previous</a>
                                        </li>
                                    </c:if>

                                    <c:forEach var="i" begin="1" end="${totalPages}">
                                        <li class="page-item ${i == currentPage ? 'active' : ''}">
                                            <a class="page-link" href="?page=${i}">${i}</a>
                                        </li>
                                    </c:forEach>

                                    <c:if test="${currentPage < totalPages}">
                                        <li class="page-item">
                                            <a class="page-link" href="?page=${currentPage + 1}">Next &raquo;</a>
                                        </li>
                                    </c:if>

                                </ul>
                            </nav>

                            <hr class="my-5" />
                            <div class="content-backdrop fade"></div>
                        </div>
                        <!-- Content wrapper -->
                    </div>
                    <!-- / Layout page -->
                    <jsp:include page="footer.jsp" />
                </div>
                <!-- Overlay -->
            </div>

            <!-- Core JS -->
            <!-- build:js assets/vendor/js/core.js -->
            <script src="./assets/vendor/libs/jquery/jquery.js"></script>
            <script src="./assets/vendor/libs/popper/popper.js"></script>
            <script src="./assets/vendor/js/bootstrap.js"></script>
            <script src="./assets/vendor/libs/perfect-scrollbar/perfect-scrollbar.js"></script>

            <script src="./assets/vendor/js/menu.js"></script>
            <!-- endbuild -->

            <!-- Vendors JS -->

            <!-- Main JS -->
            <script src="./assets/js/main.js"></script>

            <!-- Page JS -->

            <!-- Place this tag in your head or just before your close body tag. -->
            <script async defer src="https://buttons.github.io/buttons.js"></script>
    </body>
</html>
