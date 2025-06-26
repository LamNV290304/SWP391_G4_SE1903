<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
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

        <title>${product != null ? 'Edit Product' : 'Add New Product'} | Sneat - Product Management</title>

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

        <!-- Custom CSS -->
        <style>
            .image-preview {
                width: 150px;
                height: 150px;
                border: 2px dashed #d9dee3;
                border-radius: 8px;
                display: flex;
                align-items: center;
                justify-content: center;
                background-color: rgba(67, 89, 113, 0.1);
                position: relative;
                transition: all 0.3s ease;
            }

            .image-preview:hover {
                border-color: #696cff;
                background-color: rgba(105, 108, 255, 0.05);
            }

            .image-preview img {
                width: 100%;
                height: 100%;
                object-fit: cover;
                border-radius: 6px;
            }

            .upload-placeholder {
                text-align: center;
                color: #a1acb8;
            }

            .profit-indicator {
                padding: 0.5rem 0.75rem;
                border-radius: 6px;
                margin-top: 0.5rem;
                font-size: 0.875rem;
                font-weight: 500;
            }

            .profit-positive {
                background-color: rgba(113, 221, 55, 0.16);
                color: #71dd37;
                border: 1px solid rgba(113, 221, 55, 0.4);
            }

            .profit-negative {
                background-color: rgba(255, 62, 29, 0.16);
                color: #ff3e1d;
                border: 1px solid rgba(255, 62, 29, 0.4);
            }

            .required-field::after {
                content: ' *';
                color: #ff3e1d;
            }

            .input-group-text {
                background-color: rgba(67, 89, 113, 0.04);
                border-color: #d9dee3;
                color: #697a8d;
            }

            .form-control:focus, .form-select:focus {
                border-color: #696cff;
                box-shadow: 0 0 0 0.2rem rgba(105, 108, 255, 0.25);
            }

            .alert {
                border: none;
                padding: 1rem 1.25rem;
                margin-bottom: 1.5rem;
            }

            .alert-success {
                background-color: rgba(113, 221, 55, 0.16);
                color: #71dd37;
            }

            .alert-danger {
                background-color: rgba(255, 62, 29, 0.16);
                color: #ff3e1d;
            }

            .breadcrumb-item + .breadcrumb-item::before {
                color: #a1acb8;
            }
        </style>

        <!-- Helpers -->
        <script src="../assets/vendor/js/helpers.js"></script>
        <script src="../assets/js/config.js"></script>
    </head>

    <body>
        <!-- Layout wrapper -->
        <div class="layout-wrapper layout-content-navbar">
            <div class="layout-container">
                <!-- Menu -->                <jsp:include page="sidebar.jsp" />

                 
                <!-- / Menu -->

                <!-- Layout container -->
                <div class="layout-page">
                                        <jsp:include page="navBar.jsp" />

                    <!-- / Navbar -->

                    <!-- Content wrapper -->
                    <div class="content-wrapper">
                        <!-- Content -->
                        <div class="container-xxl flex-grow-1 container-p-y">
                            <!-- Breadcrumb -->
                            <nav aria-label="breadcrumb">
                                <ol class="breadcrumb">
                                    <li class="breadcrumb-item">
                                        <a href="index.html">Dashboard</a>
                                    </li>
                                    <li class="breadcrumb-item">
                                        <a href="products">Products</a>
                                    </li>
                                    <li class="breadcrumb-item active">${product != null ? 'Edit Product' : 'Add Product'}</li>
                                </ol>
                            </nav>

                            <h4 class="fw-bold py-3 mb-4">
                                <span class="text-muted fw-light">Products /</span> ${product != null ? 'Edit Product' : 'Add New Product'}
                            </h4>

                            <!-- Success Messages -->
                            <c:if test="${not empty param.success}">
                                <div class="alert alert-success alert-dismissible fade show" role="alert">
                                    <strong>Success!</strong> ${param.success}
                                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                                </div>
                            </c:if>

                            <!-- Error Messages -->
                            <c:if test="${not empty error}">
                                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                                    <strong>Error!</strong> ${error}
                                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                                </div>
                            </c:if>

                            <!-- Product Form -->
                            <div class="row">
                                <div class="col-12">
                                    <div class="card">
                                        <div class="card-header d-flex justify-content-between align-items-center">
                                            <h5 class="mb-0">
                                                <i class="bx ${product != null ? 'bx-edit' : 'bx-plus'} me-2"></i>
                                                ${product != null ? 'Edit Product Information' : 'Create New Product'}
                                            </h5>
                                            <small class="text-muted float-end">
                                                <c:if test="${product != null}">
                                                    ID: ${product.productID}
                                                </c:if>
                                            </small>
                                        </div>
                                        <div class="card-body">
                                            <form action="products" method="post" enctype="multipart/form-data">
                                                <input type="hidden" name="action" value="${product != null ? 'update' : 'create'}">
                                                <c:if test="${product != null}">
                                                    <input type="hidden" name="productId" value="${product.productID}">
                                                </c:if>

                                                <div class="row">
                                                    <!-- Left Column - Basic Information -->
                                                    <div class="col-xl-6">
                                                        <h6 class="text-muted fw-bold mb-3">Basic Information</h6>

                                                     

                                                        <!-- Product Name -->
                                                        <div class="mb-3">
                                                            <label for="productName" class="form-label required-field">Product Name</label>
                                                            <div class="input-group input-group-merge">
                                                                <span class="input-group-text"><i class="bx bx-package"></i></span>
                                                                <input 
                                                                    type="text" 
                                                                    class="form-control" 
                                                                    id="productName" 
                                                                    name="productName" 
                                                                    value="${not empty formData ? paramValues.productName[0] : product.productName}" 
                                                                    placeholder="Enter product name" 
                                                                    required
                                                                    >
                                                            </div>
                                                        </div>

                                                        <!-- Category -->
                                                        <div class="mb-3">
                                                            <label for="categoryId" class="form-label required-field">Category</label>
                                                            <div class="input-group input-group-merge">
                                                                <span class="input-group-text"><i class="bx bx-category"></i></span>
                                                                <select class="form-select" id="categoryId" name="categoryId" required>
                                                                    <option value="">Select Category</option>
                                                                    <c:forEach var="category" items="${categories}">
                                                                        <c:set var="selectedCategory" value="${not empty formData ? paramValues.categoryId[0] : product.categoryID}"/>
                                                                        <option value="${category.categoryID}" 
                                                                                ${selectedCategory == category.categoryID ? 'selected' : ''}>
                                                                            ${category.categoryName}
                                                                        </option>
                                                                    </c:forEach>
                                                                </select>
                                                            </div>
                                                        </div>

                                                        <!-- Unit -->
                                                        <div class="mb-3">
                                                            <label for="unitId" class="form-label required-field">Unit</label>
                                                            <div class="input-group input-group-merge">
                                                                <span class="input-group-text"><i class="bx bx-cube"></i></span>
                                                                <select class="form-select" id="unitId" name="unitId" required>
                                                                    <option value="">Select Unit</option>
                                                                    <c:forEach var="unit" items="${units}">
                                                                        <c:set var="selectedUnit" value="${not empty formData ? paramValues.unitId[0] : product.unitID}"/>
                                                                        <option value="${unit.unitID}" 
                                                                                ${selectedUnit == unit.unitID ? 'selected' : ''}>
                                                                            ${unit.description}
                                                                        </option>
                                                                    </c:forEach>
                                                                </select>
                                                            </div>
                                                        </div>

                                                        <!-- Description -->
                                                        <div class="mb-3">
                                                            <label for="description" class="form-label">Description</label>
                                                            <textarea 
                                                                class="form-control" 
                                                                id="description" 
                                                                name="description" 
                                                                rows="4" 
                                                                placeholder="Enter product description"
                                                                >${not empty formData ? paramValues.description[0] : product.description}</textarea>
                                                        </div>

                                                        <!-- Status -->
                                                        <div class="mb-3">
                                                            <div class="form-check form-switch">
                                                                <input 
                                                                    class="form-check-input" 
                                                                    type="checkbox" 
                                                                    id="status" 
                                                                    name="status" 
                                                                    ${(not empty formData and paramValues.status[0] == 'on') or (empty formData and product.status) ? 'checked' : ''}
                                                                    >
                                                                <label class="form-check-label" for="status">
                                                                    Active Product
                                                                </label>
                                                            </div>
                                                            <div class="form-text">Enable this product for sale</div>
                                                        </div>
                                                    </div>

                                                    <!-- Right Column - Pricing & Image -->
                                                    <div class="col-xl-6">
                                                        <h6 class="text-muted fw-bold mb-3">Pricing & Media</h6>

                                                        <!-- Image Upload -->
                                                        <div class="mb-4">
                                                            <label class="form-label">Product Image</label>
                                                            <div class="d-flex align-items-start gap-3">
                                                                <div class="image-preview">
                                                                    <c:choose>
                                                                        <c:when test="${not empty product.imageUrl}">
                                                                            <img src="${product.imageUrl}" alt="Product Image">
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                            <div class="upload-placeholder">
                                                                                <i class="bx bx-camera fs-2 mb-2"></i>
                                                                                <div class="small">No Image</div>
                                                                            </div>
                                                                        </c:otherwise>
                                                                    </c:choose>
                                                                </div>
                                                                <div class="flex-grow-1">
                                                                    <input type="file" class="form-control" id="image" name="image" accept="image/*">
                                                                    <div class="form-text">
                                                                        <i class="bx bx-info-circle me-1"></i>
                                                                        Supported: JPG, PNG, GIF (Max 5MB)
                                                                    </div>
                                                                    <c:if test="${not empty product.imageUrl}">
                                                                        <div class="mt-2">
                                                                            <span class="badge bg-label-success">
                                                                                <i class="bx bx-check me-1"></i>Image uploaded
                                                                            </span>
                                                                        </div>
                                                                    </c:if>
                                                                </div>
                                                            </div>
                                                        </div>

                                                        <!-- Import Price -->
                                                        <div class="mb-3">
                                                            <label for="importPrice" class="form-label required-field">Import Price</label>
                                                            <div class="input-group input-group-merge">
                                                                <span class="input-group-text">$</span>
                                                                <input 
                                                                    type="number" 
                                                                    class="form-control" 
                                                                    id="importPrice" 
                                                                    name="importPrice" 
                                                                    value="${not empty formData ? paramValues.importPrice[0] : product.importPrice}" 
                                                                    step="0.01" 
                                                                    min="0" 
                                                                    placeholder="0.00" 
                                                                    required
                                                                    >
                                                            </div>
                                                        </div>

                                                        <!-- Selling Price -->
                                                        <div class="mb-3">
                                                            <label for="sellingPrice" class="form-label required-field">Selling Price</label>
                                                            <div class="input-group input-group-merge">
                                                                <span class="input-group-text">$</span>
                                                                <input 
                                                                    type="number" 
                                                                    class="form-control" 
                                                                    id="sellingPrice" 
                                                                    name="sellingPrice" 
                                                                    value="${not empty formData ? paramValues.sellingPrice[0] : product.sellingPrice}" 
                                                                    step="0.01" 
                                                                    min="0" 
                                                                    placeholder="0.00" 
                                                                    required
                                                                    >
                                                            </div>

                                                            <!-- Profit Calculation -->
                                                            <c:if test="${product != null && product.importPrice != null && product.sellingPrice != null}">
                                                                <c:set var="profit" value="${product.sellingPrice - product.importPrice}"/>
                                                                <c:set var="profitClass" value="${profit >= 0 ? 'profit-positive' : 'profit-negative'}"/>
                                                                <div class="profit-indicator ${profitClass}">
                                                                    <i class="bx ${profit >= 0 ? 'bx-trending-up' : 'bx-trending-down'} me-1"></i>
                                                                    Profit: $<fmt:formatNumber value="${profit}" pattern="#,##0.00"/>
                                                                    <c:if test="${product.importPrice > 0}">
                                                                        <c:set var="profitPercentage" value="${(profit / product.importPrice) * 100}"/>
                                                                        (<fmt:formatNumber value="${profitPercentage}" pattern="#,##0.00"/>%)
                                                                    </c:if>
                                                                </div>
                                                            </c:if>
                                                        </div>
                                                    </div>
                                                </div>

                                                <!-- Form Actions -->
                                                <div class="row mt-4">
                                                    <div class="col-12">
                                                        <div class="d-flex justify-content-between">
                                                            <a href="products" class="btn btn-label-secondary">
                                                                <i class="bx bx-arrow-back me-2"></i>Cancel
                                                            </a>
                                                            <div>
                                                                <c:if test="${product != null}">
                                                                    <a href="products?action=delete&id=${product.productID}" 
                                                                       class="btn btn-label-danger me-2"
                                                                       onclick="return confirm('Are you sure you want to delete this product?')">
                                                                        <i class="bx bx-trash me-2"></i>Delete
                                                                    </a>
                                                                </c:if>
                                                                <button type="submit" class="btn btn-primary">
                                                                    <i class="bx ${product != null ? 'bx-save' : 'bx-plus'} me-2"></i>
                                                                    ${product != null ? 'Update Product' : 'Create Product'}
                                                                </button>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </form>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <!-- / Content -->

                        <!-- Footer -->
                            <jsp:include page="footer.jsp" />
                        <!-- / Footer -->

                        <div class="content-backdrop fade"></div>
                    </div>
                    <!-- / Content wrapper -->
                </div>
                <!-- / Layout page -->
            </div>
            <!-- / Layout container -->
        </div>
        <!-- / Layout wrapper -->

        <!-- Core JS -->
        <!-- build:js assets/vendor/js/core.js -->
       
            <script src="assets/vendor/libs/popper/popper.js"></script>
            <script src="assets/vendor/js/bootstrap.js"></script>
            <script src="assets/vendor/libs/perfect-scrollbar/perfect-scrollbar.js"></script>
            <script src="assets/vendor/js/menu.js"></script> <!-- X? lï¿½ toggle -->
            <script src="assets/js/main.js"></script> <!-- Main logic -->
        <!-- endbuild -->

        <!-- Vendors JS -->

        <!-- Main JS -->
        <script src="../assets/js/main.js"></script>

        <!-- Page JS -->

        <!-- Optional Custom JS -->
    </body>
</html>