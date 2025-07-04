<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${product != null ? 'Edit Product' : 'Add New Product'}</title>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap/5.3.0/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
        .header-section {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 2rem 0;
        }
        .form-section {
            background: #f8f9fa;
            min-height: calc(100vh - 200px);
            padding: 2rem 0;
        }
        .image-preview {
            width: 150px;
            height: 150px;
            border: 2px dashed #dee2e6;
            border-radius: 8px;
            display: flex;
            align-items: center;
            justify-content: center;
            background-color: #f8f9fa;
            cursor: pointer;
            transition: all 0.3s ease;
        }
        .image-preview:hover {
            border-color: #007bff;
            background-color: #e3f2fd;
        }
        .image-preview img {
            width: 100%;
            height: 100%;
            object-fit: cover;
            border-radius: 6px;
        }
        .profit-indicator {
            padding: 0.5rem;
            border-radius: 8px;
            margin-top: 0.5rem;
        }
        .profit-positive {
            background-color: #d1edff;
            color: #0969da;
            border: 1px solid #b6e3ff;
        }
        .profit-negative {
            background-color: #ffeaea;
            color: #d1242f;
            border: 1px solid #ffc1cc;
        }
        .required-field::after {
            content: ' *';
            color: #dc3545;
        }
        .card-form {
            box-shadow: 0 0.5rem 1rem rgba(0, 0, 0, 0.15);
            border: none;
        }
    </style>
</head>
<body class="bg-light">
    <!-- Navigation -->
    <nav class="navbar navbar-expand-lg navbar-dark bg-primary">
        <div class="container-fluid">
            <a class="navbar-brand" href="products">
                <i class="fas fa-boxes me-2"></i>Product Management
            </a>
            <div class="navbar-nav ms-auto">
                <a class="nav-link" href="products">
                    <i class="fas fa-list me-1"></i>Back to List
                </a>
            </div>
        </div>
    </nav>

    <!-- Header Section -->
    <div class="header-section">
        <div class="container">
            <div class="row">
                <div class="col-12 text-center">
                    <h1 class="mb-2">
                        <i class="fas ${product != null ? 'fa-edit' : 'fa-plus-circle'} me-3"></i>
                        ${product != null ? 'Edit Product' : 'Add New Product'}
                    </h1>
                    <p class="lead mb-0">
                        ${product != null ? 'Update product information' : 'Fill in the details to create a new product'}
                    </p>
                </div>
            </div>
        </div>
    </div>

    <!-- Form Section -->
    <div class="form-section">
        <div class="container">
            <!-- Error Messages -->
            <c:if test="${not empty error}">
                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                    <i class="fas fa-exclamation-circle me-2"></i>${error}
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
            </c:if>

            <div class="row justify-content-center">
                <div class="col-lg-10">
                    <div class="card card-form">
                        <div class="card-body p-4">
                            <form action="products" method="post" enctype="multipart/form-data" id="productForm">
                                <input type="hidden" name="action" value="${product != null ? 'update' : 'create'}">
                                
                                <div class="row">
                                    <!-- Left Column -->
                                    <div class="col-md-6">
                                        <!-- Product ID -->
                                        <div class="mb-3">
                                            <label for="productId" class="form-label required-field">Product ID</label>
                                            <input type="text" class="form-control" id="productId" name="productId" 
                                                   value="${product.productID}" 
                                                   ${product != null ? 'readonly' : ''} 
                                                   placeholder="Enter unique product ID" required>
                                            <div class="form-text">
                                                ${product != null ? 'Product ID cannot be changed' : 'Enter a unique identifier for the product'}
                                            </div>
                                        </div>

                                        <!-- Product Name -->
                                        <div class="mb-3">
                                            <label for="productName" class="form-label required-field">Product Name</label>
                                            <input type="text" class="form-control" id="productName" name="productName" 
                                                   value="${product.productName}" placeholder="Enter product name" required>
                                        </div>

                                        <!-- Category -->
                                        <div class="mb-3">
                                            <label for="categoryId" class="form-label required-field">Category</label>
                                            <select class="form-select" id="categoryId" name="categoryId" required>
                                                <option value="">Select Category</option>
                                                <c:forEach var="category" items="${categories}">
                                                    <option value="${category.categoryID}" 
                                                            ${product.categoryID == category.categoryID ? 'selected' : ''}>
                                                        ${category.categoryName}
                                                    </option>
                                                </c:forEach>
                                            </select>
                                        </div>

                                        <!-- Unit -->
                                        <div class="mb-3">
                                            <label for="unitId" class="form-label required-field">Unit</label>
                                            <select class="form-select" id="unitId" name="unitId" required>
                                                <option value="">Select Unit</option>
                                                <c:forEach var="unit" items="${units}">
                                                    <option value="${unit.unitID}" 
                                                            ${product.unitID == unit.unitID ? 'selected' : ''}>
                                                        ${unit.description}
                                                    </option>
                                                </c:forEach>
                                            </select>
                                        </div>

                                        <!-- Description -->
                                        <div class="mb-3">
                                            <label for="description" class="form-label">Description</label>
                                            <textarea class="form-control" id="description" name="description" 
                                                      rows="4" placeholder="Enter product description">${product.description}</textarea>
                                        </div>
                                    </div>

                                    <!-- Right Column -->
                                    <div class="col-md-6">
                                        <!-- Image Upload -->
                                        <div class="mb-3">
                                            <label class="form-label">Product Image</label>
                                            <div class="d-flex align-items-start gap-3">
                                                <div class="image-preview" onclick="document.getElementById('image').click()">
                                                    <c:choose>
                                                        <c:when test="${not empty product.imageUrl}">
                                                            <img src="${product.imageUrl}" alt="Product Image" id="imagePreview">
                                                        </c:when>
                                                        <c:otherwise>
                                                            <div class="text-center" id="imagePlaceholder">
                                                                <i class="fas fa-camera fa-2x text-muted mb-2"></i>
                                                                <div class="text-muted">Click to upload</div>
                                                            </div>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </div>
                                                <div class="flex-grow-1">
                                                    <input type="file" class="form-control" id="image" name="image" 
                                                           accept="image/*" style="display: none;" onchange="previewImage(this)">
                                                    <button type="button" class="btn btn-outline-primary mb-2" 
                                                            onclick="document.getElementById('image').click()">
                                                        <i class="fas fa-upload me-2"></i>Choose Image
                                                    </button>
                                                    <div class="form-text">
                                                        Supported formats: JPG, PNG, GIF (Max 5MB)
                                                    </div>
                                                    <c:if test="${not empty product.imageUrl}">
                                                        <button type="button" class="btn btn-outline-danger btn-sm mt-2" 
                                                                onclick="removeImage()">
                                                            <i class="fas fa-trash me-2"></i>Remove Image
                                                        </button>
                                                    </c:if>
                                                </div>
                                            </div>
                                        </div>

                                        <!-- Import Price -->
                                        <div class="mb-3">
                                            <label for="importPrice" class="form-label required-field">Import Price ($)</label>
                                            <div class="input-group">
                                                <span class="input-group-text">$</span>
                                                <input type="number" class="form-control" id="importPrice" name="importPrice" 
                                                       value="${product.importPrice}" step="0.01" min="0" 
                                                       placeholder="0.00" required onchange="calculateProfit()">
                                            </div>
                                        </div>

                                        <!-- Selling Price -->
                                        <div class="mb-3">
                                            <label for="sellingPrice" class="form-label required-field">Selling Price ($)</label>
                                            <div class="input-group">
                                                <span class="input-group-text">$</span>
                                                <input type="number" class="form-control" id="sellingPrice" name="sellingPrice" 
                                                       value="${product.sellingPrice}" step="0.01" min="0" 
                                                       placeholder="0.00" required onchange="calculateProfit()">
                                            </div>
                                        </div>

                                        <!-- Profit Calculator -->
                                        <div id="profitDisplay" class="profit-indicator" style="display: none;">
                                            <div class="d-flex justify-content-between align-items-center">
                                                <span><i class="fas fa-calculator me-2"></i>Profit Margin:</span>
                                                <span id="profitAmount" class="fw-bold"></span>
                                            </div>
                                            <div class="d-flex justify-content-between align-items-center mt-1">
                                                <span>Profit Percentage:</span>
                                                <span id="profitPercentage" class="fw-bold"></span>
                                            </div>
                                        </div>

                                        <!-- Status -->
                                        <div class="mb-3">
                                            <div class="form-check form-switch">
                                                <input class="form-check-input" type="checkbox" id="status" name="status" 
                                                       ${product == null || product.status ? 'checked' : ''}>
                                                <label class="form-check-label" for="status">
                                                    <span class="badge bg-success me-2" id="statusBadge">
                                                        <i class="fas fa-check me-1"></i>Active
                                                    </span>
                                                    Product Status
                                                </label>
                                            </div>
                                            <div class="form-text">Toggle to activate/deactivate the product</div>
                                        </div>
                                    </div>
                                </div>

                                <!-- Form Actions -->
                                <div class="row mt-4">
                                    <div class="col-12">
                                        <div class="d-flex justify-content-between">
                                            <a href="products" class="btn btn-outline-secondary btn-lg">
                                                <i class="fas fa-arrow-left me-2"></i>Cancel
                                            </a>
                                            <div>
                                                <button type="reset" class="btn btn-outline-warning btn-lg me-2">
                                                    <i class="fas fa-undo me-2"></i>Reset
                                                </button>
                                                <button type="submit" class="btn btn-primary btn-lg">
                                                    <i class="fas ${product != null ? 'fa-save' : 'fa-plus'} me-2"></i>
                                                    ${product != null ? 'Update Product' : 'Create Product'}
                                                 </button>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </form>
                        </div> <!-- /.card-body -->
                    </div> <!-- /.card -->
                </div> <!-- /.col-lg-10 -->
            </div> <!-- /.row -->
        </div> <!-- /.container -->
    </div> <!-- /.form-section -->

    <!-- Footer -->
    <footer class="bg-primary text-white text-center py-3 mt-auto">
        <div class="container">
            &copy; 2025 Product Management System
        </div>
    </footer>

    <!-- JavaScript -->
    <script>
        function previewImage(input) {
            const preview = document.getElementById("imagePreview");
            const placeholder = document.getElementById("imagePlaceholder");
            if (input.files && input.files[0]) {
                const reader = new FileReader();
                reader.onload = function(e) {
                    if (preview) {
                        preview.src = e.target.result;
                    } else {
                        const img = document.createElement("img");
                        img.id = "imagePreview";
                        img.src = e.target.result;
                        placeholder.replaceWith(img);
                    }
                };
                reader.readAsDataURL(input.files[0]);
            }
        }

        function removeImage() {
            const imageInput = document.getElementById("image");
            const preview = document.getElementById("imagePreview");
            if (preview) {
                preview.remove();
                const placeholder = document.createElement("div");
                placeholder.className = "text-center";
                placeholder.id = "imagePlaceholder";
                placeholder.innerHTML = `
                    <i class="fas fa-camera fa-2x text-muted mb-2"></i>
                    <div class="text-muted">Click to upload</div>
                `;
                document.querySelector(".image-preview").appendChild(placeholder);
                imageInput.value = "";
            }
        }

        function calculateProfit() {
            const importPrice = parseFloat(document.getElementById("importPrice").value) || 0;
            const sellingPrice = parseFloat(document.getElementById("sellingPrice").value) || 0;
            const profitAmount = sellingPrice - importPrice;
            const profitPercentage = importPrice > 0 ? (profitAmount / importPrice) * 100 : 0;

            const display = document.getElementById("profitDisplay");
            const amountEl = document.getElementById("profitAmount");
            const percentEl = document.getElementById("profitPercentage");

            if (!isNaN(profitAmount) && !isNaN(profitPercentage)) {
                display.style.display = "block";
                amountEl.innerText = `$${profitAmount.toFixed(2)}`;
                percentEl.innerText = `${profitPercentage.toFixed(2)}%`;

                if (profitAmount >= 0) {
                    display.classList.remove("profit-negative");
                    display.classList.add("profit-positive");
                } else {
                    display.classList.remove("profit-positive");
                    display.classList.add("profit-negative");
                }
            } else {
                display.style.display = "none";
            }
        }

        // Recalculate on page load if values are already present
        window.onload = () => {
            if (document.getElementById("importPrice").value && document.getElementById("sellingPrice").value) {
                calculateProfit();
            }
        };
    </script>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>