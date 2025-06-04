<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Product Management</title>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap/5.3.0/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
        .product-image {
            width: 60px;
            height: 60px;
            object-fit: cover;
            border-radius: 8px;
        }
        .status-active {
            background-color: #d1edff;
            color: #0969da;
        }
        .status-inactive {
            background-color: #f1f3f4;
            color: #656d76;
        }
        .search-section {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 2rem 0;
            margin-bottom: 2rem;
        }
        .card-stats {
            border-left: 4px solid #007bff;
        }
        .table-hover tbody tr:hover {
            background-color: #f8f9fa;
        }
    </style>
</head>
<body class="bg-light">
    <!-- Navigation -->
    <nav class="navbar navbar-expand-lg navbar-dark bg-primary">
        <div class="container-fluid">
            <a class="navbar-brand" href="#">
                <i class="fas fa-boxes me-2"></i>Product Management
            </a>
            <div class="navbar-nav ms-auto">
                <a class="nav-link" href="products?action=new">
                    <i class="fas fa-plus me-1"></i>Add Product
                </a>
            </div>
        </div>
    </nav>

    <!-- Search Section -->
    <div class="search-section">
        <div class="container">
            <div class="row justify-content-center">
                <div class="col-lg-8">
                    <form method="GET" action="products">
                        <div class="row g-3">
                            <div class="col-md-4">
                                <div class="form-floating">
                                    <input type="text" class="form-control" id="search" name="search" 
                                           value="${search}" placeholder="Search products...">
                                    <label for="search">Search Products</label>
                                </div>
                            </div>
                            <div class="col-md-4">
                                <div class="form-floating">
                                    <select class="form-select" id="category" name="category">
                                        <option value="">All Categories</option>
                                        <c:forEach var="category" items="${categories}">
                                            <option value="${category.categoryID}" 
                                                    ${categoryFilter == category.categoryID ? 'selected' : ''}>
                                                ${category.categoryName}
                                            </option>
                                        </c:forEach>
                                    </select>
                                    <label for="category">Category</label>
                                </div>
                            </div>
                            <div class="col-md-4">
                                <button type="submit" class="btn btn-light w-100 h-100">
                                    <i class="fas fa-search me-2"></i>Search
                                </button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <div class="container-fluid">
        <!-- Alert Messages -->
        <c:if test="${param.success != null}">
            <div class="alert alert-success alert-dismissible fade show" role="alert">
                <i class="fas fa-check-circle me-2"></i>${param.success}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>
        
        <c:if test="${param.error != null}">
            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                <i class="fas fa-exclamation-circle me-2"></i>${param.error}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>

        <!-- Stats Cards -->
        <div class="row mb-4">
            <div class="col-md-3">
                <div class="card card-stats h-100">
                    <div class="card-body">
                        <div class="d-flex justify-content-between">
                            <div>
                                <p class="card-category text-muted mb-1">Total Products</p>
                                <h3 class="card-title mb-0">${products.size()}</h3>
                            </div>
                            <div class="text-primary">
                                <i class="fas fa-boxes fa-2x"></i>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-md-3">
                <div class="card card-stats h-100">
                    <div class="card-body">
                        <div class="d-flex justify-content-between">
                            <div>
                                <p class="card-category text-muted mb-1">Categories</p>
                                <h3 class="card-title mb-0">${categories.size()}</h3>
                            </div>
                            <div class="text-success">
                                <i class="fas fa-tags fa-2x"></i>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-md-3">
                <div class="card card-stats h-100">
                    <div class="card-body">
                        <div class="d-flex justify-content-between">
                            <div>
                                <p class="card-category text-muted mb-1">Current Page</p>
                                <h3 class="card-title mb-0">${currentPage}/${totalPages}</h3>
                            </div>
                            <div class="text-info">
                                <i class="fas fa-file-alt fa-2x"></i>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-md-3">
                <div class="card card-stats h-100">
                    <div class="card-body text-center">
                        <a href="products?action=new" class="btn btn-primary btn-lg">
                            <i class="fas fa-plus me-2"></i>Add New Product
                        </a>
                    </div>
                </div>
            </div>
        </div>

        <!-- Products Table -->
        <div class="card shadow">
            <div class="card-header bg-white">
                <div class="d-flex justify-content-between align-items-center">
                    <h5 class="mb-0">
                        <i class="fas fa-list me-2"></i>Products List
                    </h5>
                    <div class="d-flex gap-2">
                        <span class="badge bg-secondary">
                            Total: ${products.size()} products
                        </span>
                    </div>
                </div>
            </div>
            
            <div class="card-body p-0">
                <div class="table-responsive">
                    <table class="table table-hover mb-0">
                        <thead class="table-dark">
                            <tr>
                                <th>Image</th>
                                <th>Product ID</th>
                                <th>Product Name</th>
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
                                        <td colspan="9" class="text-center py-4">
                                            <i class="fas fa-inbox fa-3x text-muted mb-3"></i>
                                            <p class="text-muted">No products found</p>
                                            <a href="products?action=new" class="btn btn-primary">
                                                <i class="fas fa-plus me-2"></i>Add First Product
                                            </a>
                                        </td>
                                    </tr>
                                </c:when>
                                <c:otherwise>
                                    <c:forEach var="product" items="${products}">
                                        <tr>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${not empty product.imageUrl}">
                                                        <img src="${product.imageUrl}" alt="${product.productName}" 
                                                             class="product-image">
                                                    </c:when>
                                                    <c:otherwise>
                                                        <div class="product-image bg-light d-flex align-items-center justify-content-center">
                                                            <i class="fas fa-image text-muted"></i>
                                                        </div>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>
                                                <strong>${product.productID}</strong>
                                            </td>
                                            <td>
                                                <div>
                                                    <strong>${product.productName}</strong>
                                                    <c:if test="${not empty product.description}">
                                                        <br><small class="text-muted">${product.description}</small>
                                                    </c:if>
                                                </div>
                                            </td>
                                            <td>
                                                <c:forEach var="category" items="${categories}">
                                                    <c:if test="${category.categoryID == product.categoryID}">
                                                        <span class="badge bg-primary">${category.categoryName}</span>
                                                    </c:if>
                                                </c:forEach>
                                            </td>
                                            <td>${product.unitID}</td>
                                            <td>
                                                <span class="text-success">
                                                    <fmt:formatNumber value="${product.importPrice}" type="currency" currencySymbol="$"/>
                                                </span>
                                            </td>
                                            <td>
                                                <span class="text-primary">
                                                    <fmt:formatNumber value="${product.sellingPrice}" type="currency" currencySymbol="$"/>
                                                </span>
                                            </td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${product.status}">
                                                        <span class="badge status-active">
                                                            <i class="fas fa-check me-1"></i>Active
                                                        </span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="badge status-inactive">
                                                            <i class="fas fa-times me-1"></i>Inactive
                                                        </span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>
                                                <div class="btn-group" role="group">
                                                    <a href="products?action=edit&id=${product.productID}" 
                                                       class="btn btn-outline-primary btn-sm" title="Edit">
                                                        <i class="fas fa-edit"></i>
                                                    </a>
                                                    <button type="button" class="btn btn-outline-danger btn-sm" 
                                                            onclick="confirmDelete('${product.productID}', '${product.productName}')" 
                                                            title="Delete">
                                                        <i class="fas fa-trash"></i>
                                                    </button>
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
        </div>

        <!-- Pagination -->
        <c:if test="${totalPages > 1}">
            <nav aria-label="Product pagination" class="mt-4">
                <ul class="pagination justify-content-center">
                    <c:if test="${currentPage > 1}">
                        <li class="page-item">
                            <a class="page-link" href="products?page=${currentPage-1}&search=${search}&category=${categoryFilter}">
                                <i class="fas fa-chevron-left"></i> Previous
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
                                Next <i class="fas fa-chevron-right"></i>
                            </a>
                        </li>
                    </c:if>
                </ul>
            </nav>
        </c:if>
    </div>

    <!-- Delete Confirmation Modal -->
    <div class="modal fade" id="deleteModal" tabindex="-1">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header bg-danger text-white">
                    <h5 class="modal-title">
                        <i class="fas fa-exclamation-triangle me-2"></i>Confirm Delete
                    </h5>
                    <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <p>Are you sure you want to delete the product:</p>
                    <p><strong id="productToDelete"></strong></p>
                    <p class="text-muted">This action cannot be undone.</p>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">
                        <i class="fas fa-times me-2"></i>Cancel
                    </button>
                    <a id="deleteLink" href="#" class="btn btn-danger">
                        <i class="fas fa-trash me-2"></i>Delete Product
                    </a>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap/5.3.0/js/bootstrap.bundle.min.js"></script>
    <script>
        function confirmDelete(productId, productName) {
            document.getElementById('productToDelete').textContent = productName;
            document.getElementById('deleteLink').href = 'products?action=delete&id=' + productId;
            new bootstrap.Modal(document.getElementById('deleteModal')).show();
        }

        
        setTimeout(function() {
            const alerts = document.querySelectorAll('.alert');
            alerts.forEach(function(alert) {
                const bsAlert = new bootstrap.Alert(alert);
                bsAlert.close();
            });
        }, 5000);
    </script>
</body>
</html>