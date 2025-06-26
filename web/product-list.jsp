<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en" class="light-style layout-menu-fixed" dir="ltr" data-theme="theme-default" data-assets-path="../assets/" data-template="vertical-menu-template-free">
    <head>
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
        <div class="layout-wrapper layout-content-navbar">

            <div class="layout-container">
                <jsp:include page="sidebar.jsp" />

                <div class="layout-page">
                    <!-- Navbar -->
                    <jsp:include page="navBar.jsp" />


                    <!-- Content wrapper -->
                    <div class="content-wrapper">
                        <div class="container-xxl flex-grow-1 container-p-y">
                            <!-- Search Section -->
                            <div class="search-section">
                                <div class="card-body">
                                    <form method="GET" action="products" class="row g-3">
                                        <div class="col-md-5">
                                            <label for="search" class="form-label text-white">Search Products</label>
                                            <input type="text" class="form-control" id="search" name="search" 
                                                   value="${search}" placeholder="Enter product name...">
                                        </div>
                                        <div class="col-md-4">
                                            <label for="category" class="form-label text-white">Category</label>
                                            <select class="form-select" id="category" name="category">
                                                <option value="">All Categories</option>
                                                <c:forEach var="category" items="${categories}">
                                                    <option value="${category.categoryID}" 
                                                            ${categoryFilter == category.categoryID ? 'selected' : ''}>
                                                        ${category.categoryName}
                                                    </option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                        <div class="col-md-3">
                                            <label class="form-label text-white d-block">&nbsp;</label>
                                            <button type="submit" class="btn btn-light w-100">
                                                <i class="bx bx-search me-1"></i>Search
                                            </button>
                                        </div>
                                    </form>
                                </div>
                            </div>

                            <!-- Alert Messages -->
                            <c:if test="${param.success != null}">
                                <div class="alert alert-success alert-dismissible" role="alert">
                                    <i class="bx bx-check-circle me-2"></i>${param.success}
                                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                                </div>
                            </c:if>

                            <c:if test="${param.error != null}">
                                <div class="alert alert-danger alert-dismissible" role="alert">
                                    <i class="bx bx-error me-2"></i>${param.error}
                                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                                </div>
                            </c:if>

                            <!-- Stats Cards -->
                            <div class="row mb-4">
                                <div class="col-lg-3 col-md-6 col-6 mb-4">
                                    <div class="card stats-card border-primary">
                                        <div class="card-body">
                                            <div class="d-flex align-items-end justify-content-between">
                                                <div>
                                                    <small class="fw-semibold d-block mb-1 text-body">Total Products</small>
                                                    <h3 class="card-title mb-2 fw-semibold text-heading">${products.size()}</h3>
                                                </div>
                                                <div class="avatar flex-shrink-0">
                                                    <i class="bx bx-package bx-sm text-primary"></i>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-lg-3 col-md-6 col-6 mb-4">
                                    <div class="card stats-card border-success">
                                        <div class="card-body">
                                            <div class="d-flex align-items-end justify-content-between">
                                                <div>
                                                    <small class="fw-semibold d-block mb-1 text-body">Categories</small>
                                                    <h3 class="card-title mb-2 fw-semibold text-heading">${categories.size()}</h3>
                                                </div>
                                                <div class="avatar flex-shrink-0">
                                                    <i class="bx bx-category bx-sm text-success"></i>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-lg-3 col-md-6 col-6 mb-4">
                                    <div class="card stats-card border-info">
                                        <div class="card-body">
                                            <div class="d-flex align-items-end justify-content-between">
                                                <div>
                                                    <small class="fw-semibold d-block mb-1 text-body">Current Page</small>
                                                    <h3 class="card-title mb-2 fw-semibold text-heading">${currentPage} / ${totalPages}</h3>
                                                </div>
                                                <div class="avatar flex-shrink-0">
                                                    <i class="bx bx-file bx-sm text-info"></i>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-lg-3 col-md-6 col-6 mb-4">
                                    <div class="card stats-card border-warning">
                                        <div class="card-body text-center">
                                            <a href="products?action=new" class="btn btn-primary btn-lg w-100">
                                                <i class="bx bx-plus me-2"></i>Add New Product
                                            </a>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <!-- Products Table -->
                            <div class="card">
                                <div class="card-header d-flex justify-content-between align-items-center">
                                    <h5 class="card-title mb-0">
                                        <i class="bx bx-list-ul me-2"></i>Products List
                                    </h5>
                                    <div class="d-flex align-items-center">
                                        <span class="badge bg-label-secondary me-2">
                                            Total: ${products.size()} products
                                        </span>
                                    </div>
                                </div>

                                <div class="table-responsive text-nowrap">
                                    <table class="table table-hover">
                                        <thead>
                                            <tr>
                                                <th>Product</th>
                                                <th>ID</th>
                                                <th>Category</th>
                                                <th>Unit</th>
                                                <th>Import Price</th>
                                                <th>Selling Price</th>
                                                <th>Status</th>
                                                <th>Actions</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:choose>
                                                <c:when test="${empty products}">
                                                    <tr>
                                                        <td colspan="8">
                                                            <div class="empty-state">
                                                                <div class="empty-state-icon">
                                                                    <i class="bx bx-package bx-lg text-body"></i>
                                                                </div>
                                                                <h5 class="text-heading mb-2">No products found</h5>
                                                                <p class="text-body mb-4">Get started by adding your first product to the inventory.</p>
                                                                <a href="products?action=new" class="btn btn-primary">
                                                                    <i class="bx bx-plus me-2"></i>Add First Product
                                                                </a>
                                                            </div>
                                                        </td>
                                                    </tr>
                                                </c:when>
                                                <c:otherwise>
                                                    <c:forEach var="product" items="${products}">
                                                        <tr>
                                                            <td>
                                                                <div class="d-flex align-items-center">
                                                                    <c:choose>
                                                                        <c:when test="${not empty product.imageUrl}">
                                                                            <img src="${product.imageUrl}" alt="${product.productName}" 
                                                                                 class="product-image me-3">
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                            <div class="avatar me-3">
                                                                                <i class="bx bx-image text-body"></i>
                                                                            </div>
                                                                        </c:otherwise>
                                                                    </c:choose>
                                                                    <div>
                                                                        <h6 class="mb-0 text-heading">${product.productName}</h6>
                                                                        <c:if test="${not empty product.description}">
                                                                            <small class="text-body">${product.description}</small>
                                                                        </c:if>
                                                                    </div>
                                                                </div>
                                                            </td>
                                                            <td>
                                                                <span class="fw-medium text-heading">#${product.productID}</span>
                                                            </td>
                                                            <td>
                                                                <c:forEach var="category" items="${categories}">
                                                                    <c:if test="${category.categoryID == product.categoryID}">
                                                                        <span class="badge bg-label-primary">${category.categoryName}</span>
                                                                    </c:if>
                                                                </c:forEach>
                                                            </td>
                                                            <td>
                                                                <span class="text-body">${product.unitDescription}</span>
                                                            </td>
                                                            <td>
                                                                <span class="fw-semibold text-success">
                                                                    <fmt:formatNumber value="${product.importPrice}" type="currency" currencySymbol="$"/>
                                                                </span>
                                                            </td>
                                                            <td>
                                                                <span class="fw-semibold text-primary">
                                                                    <fmt:formatNumber value="${product.sellingPrice}" type="currency" currencySymbol="$"/>
                                                                </span>
                                                            </td>
                                                            <td>
                                                                <c:choose>
                                                                    <c:when test="${product.status}">
                                                                        <span class="badge bg-label-success">
                                                                            <i class="bx bx-check me-1"></i>Active
                                                                        </span>
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <span class="badge bg-label-secondary">
                                                                            <i class="bx bx-x me-1"></i>Inactive
                                                                        </span>
                                                                    </c:otherwise>
                                                                </c:choose>
                                                            </td>
                                                            <td>
                                                                <div class="dropdown">
                                                                    <button type="button" class="btn p-0 dropdown-toggle hide-arrow" data-bs-toggle="dropdown">
                                                                        <i class="bx bx-dots-vertical-rounded"></i>
                                                                    </button>
                                                                    <div class="dropdown-menu">
                                                                        <a class="dropdown-item" href="products?action=edit&id=${product.productID}">
                                                                            <i class="bx bx-edit-alt me-1"></i> Edit
                                                                        </a>
                                                                        <a class="dropdown-item" href="javascript:void(0);" 
                                                                           onclick="confirmDelete('${product.productID}', '${product.productName}')">
                                                                            <i class="bx bx-trash me-1"></i> Delete
                                                                        </a>
                                                                    </div>
                                                                </div>
                                                            </td>
                                                        </tr>
                                                    </c:forEach>
                                                </c:otherwise>
                                            </c:choose>
                                        </tbody>
                                    </table>
                                </div>
                            </div>

                            <!-- Pagination -->
                            <c:if test="${totalPages > 1}">
                                <nav aria-label="Product pagination" class="mt-4">
                                    <ul class="pagination justify-content-center">
                                        <c:if test="${currentPage > 1}">
                                            <li class="page-item">
                                                <a class="page-link" href="products?page=${currentPage-1}&search=${search}&category=${categoryFilter}">
                                                    <i class="bx bx-chevron-left"></i>
                                                </a>
                                            </li>
                                        </c:if>

                                        <c:forEach begin="1" end="${totalPages}" var="pageNum">
                                            <c:choose>
                                                <c:when test="${pageNum == currentPage}">
                                                    <li class="page-item active">
                                                        <span class="page-link">${pageNum}</span>
                                                    </li>
                                                </c:when>
                                                <c:otherwise>
                                                    <li class="page-item">
                                                        <a class="page-link" href="products?page=${pageNum}&search=${search}&category=${categoryFilter}">
                                                            ${pageNum}
                                                        </a>
                                                    </li>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:forEach>

                                        <c:if test="${currentPage < totalPages}">
                                            <li class="page-item">
                                                <a class="page-link" href="products?page=${currentPage+1}&search=${search}&category=${categoryFilter}">
                                                    <i class="bx bx-chevron-right"></i>
                                                </a>
                                            </li>
                                        </c:if>
                                    </ul>
                                </nav>
                            </c:if>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Delete Confirmation Modal -->
        <div class="modal fade" id="deleteModal" tabindex="-1" aria-hidden="true">
            <div class="modal-dialog modal-dialog-centered" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="modalCenterTitle">
                            <i class="bx bx-error-circle text-danger me-2"></i>Confirm Delete
                        </h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <div class="row">
                            <div class="col mb-3">
                                <p class="text-body">Are you sure you want to delete this product?</p>
                                <div class="alert alert-warning">
                                    <strong id="productToDelete"></strong>
                                </div>
                                <p class="text-body mb-0"><small>This action cannot be undone.</small></p>
                            </div>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-outline-secondary" data-bs-dismiss="modal">
                            <i class="bx bx-x me-1"></i>Cancel
                        </button>
                        <a id="deleteLink" href="#" class="btn btn-danger">
                            <i class="bx bx-trash me-1"></i>Delete Product
                        </a>
                    </div>
                </div>
            </div>
        </div>

        <!-- Core JS -->
        <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap/5.2.0/js/bootstrap.bundle.min.js"></script>
        <script src="assets/vendor/libs/jquery/jquery.js"></script>
        <script src="assets/vendor/libs/popper/popper.js"></script>
        <script src="assets/vendor/js/bootstrap.js"></script>
        <script src="assets/vendor/libs/perfect-scrollbar/perfect-scrollbar.js"></script>
        <script src="assets/vendor/js/menu.js"></script> <!-- X? lï¿½ toggle -->
        <script src="assets/js/main.js"></script> <!-- Main logic -->
        <!-- Page JS -->
        <script>
                                                                               function confirmDelete(productId, productName) {
                                                                                   document.getElementById('productToDelete').textContent = productName;
                                                                                   document.getElementById('deleteLink').href = 'products?action=delete&id=' + productId;
                                                                                   const deleteModal = new bootstrap.Modal(document.getElementById('deleteModal'));
                                                                                   deleteModal.show();
                                                                               }


        </script>
    </body>
</html>