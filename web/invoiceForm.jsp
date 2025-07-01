<%--
    Document : invoiceForm
    Created on : Jun 17, 2025, 4:44:48 PM
    Author   : duckh
--%>

<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="en"
      class="light-style layout-menu-fixed"
      dir="ltr"
      data-theme="theme-default"
      data-assets-path="${pageContext.request.contextPath}/assets/"
      data-template="vertical-menu-template-free">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
        <title>Tạo Hóa đơn Mới - Sneat</title> 

        <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/assets/img/favicon/favicon.ico" />

        <link rel="preconnect" href="https://fonts.googleapis.com" />
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
        <link href="https://fonts.googleapis.com/css2?family=Public+Sans:wght@300;400;500;600;700&display=swap" rel="stylesheet" />

        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/vendor/fonts/boxicons.css" />

        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/vendor/css/core.css" class="template-customizer-core-css" />
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/vendor/css/theme-default.css" class="template-customizer-theme-css" />
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/demo.css" />

        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/vendor/libs/perfect-scrollbar/perfect-scrollbar.css" />

        <script src="${pageContext.request.contextPath}/assets/vendor/js/helpers.js"></script>

        <script src="${pageContext.request.contextPath}/assets/js/config.js"></script>
    </head>
    <body>
        <div class="layout-wrapper layout-content-navbar">
            <div class="layout-container">
                <jsp:include page="sidebar.jsp" />
                <div class="layout-page">
                    <jsp:include page="navBar.jsp" />
                    <div class="content-wrapper">
                        <div class="container-xxl flex-grow-1 container-p-y">
                            <h4 class="fw-bold py-3 mb-4">
                                <span class="text-muted fw-light">Hóa đơn /</span> Tạo Hóa Đơn Mới
                            </h4>
                           
                            <c:if test="${not empty selectedInvoice}">
                                <p class="mb-4">Hóa đơn lập ngày: <fmt:formatDate value="${selectedInvoice.invoiceDate}" pattern="dd/MM/yyyy HH:mm:ss" /></p>
                            </c:if>

                            <c:if test="${not empty errorMessage}">
                                <div class="alert alert-danger" role="alert">
                                    ${errorMessage}
                                </div>
                            </c:if>
                            <c:if test="${not empty successMessage}">
                                <div class="alert alert-success" role="alert">
                                    ${successMessage}
                                </div>
                            </c:if>

                            <div class="card mb-4">
                                <h5 class="card-header">Thông tin khách hàng</h5>
                                <div class="card-body">
                                   
                                    <form action="InvoiceServlet" method="post">
                                        <input type="hidden" name="action" value="checkCustomerPhone" /> 
                                        <input type="hidden" name="invoiceID" value="${selectedInvoice.invoiceID}" /> 

                                        <div class="row mb-3">
                                            <div class="col-md-6">
                                                <label for="customerPhone" class="form-label">Số điện thoại khách hàng</label>
                                                <div class="input-group">
                                                    <input type="text" id="customerPhone" name="customerPhone" class="form-control" placeholder="Nhập số điện thoại"
                                                           value="${customerPhone != null ? customerPhone : ''}" />
                                                    <button type="submit" class="btn btn-info">Kiểm tra SĐT</button>
                                                </div>
                                           
                                                <small class="form-text text-muted">
                                                    <c:if test="${not empty phoneCheckMessage}">
                                                        ${phoneCheckMessage}
                                                    </c:if>
                                                </small>
                                              
                                                <input type="hidden" name="customerID" value="${customerID != null ? customerID : defaultCustomerId}" /> 
                                            </div>
                                        </div>

                                        <div class="row mb-3">
                                            <div class="col-md-6">
                                                <label for="customerName" class="form-label">Tên khách hàng</label>
                                                <input type="text" id="customerName" name="customerName" class="form-control" placeholder="Tên khách hàng"
                                                       value="${customerName != null ? customerName : 'Khách vãng lai'}"
                                                       <c:if test="${customerFieldsReadonly}">readonly</c:if> />
                                                </div>
                                                <div class="col-md-6">
                                                    <label for="customerEmail" class="form-label">Email khách hàng</label>
                                                    <input type="email" id="customerEmail" name="customerEmail" class="form-control" placeholder="Email khách hàng"
                                                           value="${customerEmail != null ? customerEmail : ''}"
                                                    <c:if test="${customerFieldsReadonly}">readonly</c:if> />
                                                </div>
                                            </div>
                                            <div class="row mb-3">
                                                <div class="col-md-12">
                                                    <label for="customerAddress" class="form-label">Địa chỉ</label>
                                                    <input type="text" id="customerAddress" name="customerAddress" class="form-control" placeholder="Địa chỉ khách hàng"
                                                           value="${customerAddress != null ? customerAddress : ''}"
                                                    <c:if test="${customerFieldsReadonly}">readonly</c:if> />
                                                </div>
                                            </div>

                                        <c:if test="${showAddCustomerButton}">
                                            <p>Current Invoice ID: ${selectedInvoice.invoiceID}</p>
                                            <a href="CustomerServlet?action=showCreateForm&phone=${customerPhone}&invoiceID=${selectedInvoice.invoiceID}" class="btn btn-success me-2">Thêm khách hàng mới</a>
                                        </c:if>




                                    </form>
                                </div>
                            </div>

                            <div class="card mb-4">
                                <h5 class="card-header">Thêm mặt hàng mới vào hóa đơn</h5>
                                <div class="card-body">
                                    <form action="InvoiceServlet" method="post">
                                        <input type="hidden" name="action" value="selectProductForPrice" id="selectProductAction"/>
                                        <input type="hidden" name="invoiceID" value="${selectedInvoice.invoiceID}" />
                                        <input type="hidden" name="shopID" value="${selectedInvoice.shopID}" />
                                  
                                        <input type="hidden" name="customerID" value="${customerID != null ? customerID : defaultCustomerId}" /> 

                                        <div class="mb-3">
                                            <label for="productID" class="form-label">Mã sản phẩm:</label>
                                            <select class="form-select" id="productID" name="productID" required onchange="this.form.submit()">
                                                <option value="">-- Chọn sản phẩm --</option>
                                                <c:forEach var="invItem" items="${inventories}">
                                                    <c:if test="${invItem.quantity > 0}">
                                                        <option value="${invItem.product.productID}"
                                                                ${param.productID == invItem.product.productID ? 'selected' : ''}>
                                                            ${invItem.product.productID} - ${invItem.product.productName} (Tồn: ${invItem.quantity})
                                                        </option>
                                                    </c:if>
                                                </c:forEach>
                                            </select>
                                        </div>
                                        <div class="mb-3">
                                            <label for="quantity" class="form-label">Số lượng:</label>
                                            <input type="number" class="form-control" id="quantity" name="quantity" placeholder="Số lượng" min="1" required
                                                   value="${param.quantity != null ? param.quantity : ''}" />
                                        </div>
                                        <div class="mb-3">
                                            <label for="unitPrice" class="form-label">Đơn giá:</label>
                                            <input type="number" class="form-control" id="unitPrice" name="unitPrice" placeholder="Đơn giá" step="any" min="0" required
                                                   value="<fmt:formatNumber value="${not empty selectedUnitPrice ? selectedUnitPrice : (param.unitPrice != null ? param.unitPrice : '')}" pattern="#0"/>" />
                                            <small class="form-text text-muted">Đơn giá bán của sản phẩm.</small>
                                        </div>
                                        <div class="mb-3">
                                            <label for="discount" class="form-label">Giảm giá (%):</label>
                                            <input type="number" class="form-control" id="discount" name="discount" placeholder="Giảm giá (%)" step="any" min="0" max="100" value="${param.discount != null ? param.discount : '0'}" />
                                            <small class="form-text text-muted">Phần trăm giảm giá cho mặt hàng này (0-100).</small>
                                        </div>
                                        <button type="submit" class="btn btn-primary me-2" onclick="document.getElementById('selectProductAction').value = 'addDetail';">
                                            <i class='bx bx-plus me-1'></i> Thêm mặt hàng
                                        </button>
                                    </form>
                                </div>
                            </div>

                            <div class="card">
                                <h5 class="card-header">Danh sách các mặt hàng đã có</h5>
                                <div class="table-responsive text-nowrap">
                                    <table class="table table-hover">
                                        <thead>
                                            <tr>
                                                <th>Mã SP</th>
                                                <th>Số lượng</th>
                                                <th>Giá bán</th>
                                                <th>Giảm giá (%)</th>
                                                <th>Thành tiền</th>
                                                <th>Hành động</th>
                                            </tr>
                                        </thead>
                                        <tbody class="table-border-bottom-0">
                                            <c:forEach var="detail" items="${invoiceDetails}">
                                                <tr>
                                                    <c:choose>
                                                        <c:when test="${editDetailID eq detail.invoiceDetailID}">
                                                    <form method="post" action="InvoiceServlet" style="margin:0;">
                                                        <input type="hidden" name="action" value="updateDetail" />
                                                        <input type="hidden" name="invoiceDetailID" value="${detail.invoiceDetailID}" />
                                                        <input type="hidden" name="invoiceID" value="${selectedInvoice.invoiceID}" />
                                             
                                                        <input type="hidden" name="customerID" value="${customerID != null ? customerID : defaultCustomerId}" /> 

                                                        <td>
                                                            <select class="form-select form-select-sm" name="productID" required>
                                                                <c:forEach var="prod" items="${products}">
                                                                    <option value="${prod.productID}" ${detail.productID == prod.productID ? 'selected' : ''}>
                                                                        ${prod.productName}
                                                                    </option>
                                                                </c:forEach>
                                                            </select>
                                                        </td>
                                                        <td><input type="number" class="form-control form-control-sm" name="quantity" value="${detail.quantity}" min="1" required /></td>
                                                        <td><input type="number" class="form-control form-control-sm" name="unitPrice" value="${detail.unitPrice}" step="any" min="0" required /></td>
                                                        <td><input type="number" class="form-control form-control-sm" name="discount" step="any" value="${detail.discount}" min="0" max="100" /></td>
                                                        <td><fmt:formatNumber value="${detail.totalPrice}" pattern="#,##0" /> VNĐ</td>
                                                        <td>
                                                            <button type="submit" class="btn btn-success btn-sm me-1">
                                                                <i class='bx bx-save me-1'></i> Lưu
                                                            </button>
                                                            <a href="InvoiceServlet?action=manageInvoiceDetails&invoiceID=${selectedInvoice.invoiceID}" class="btn btn-secondary btn-sm">
                                                                <i class='bx bx-x me-1'></i> Hủy
                                                            </a>
                                                        </td>
                                                    </form>
                                                </c:when>
                                                <c:otherwise>
                                                    <td>
                                                        <c:set var="productNameDisplay" value="${detail.productID}"/>
                                                        <c:forEach var="prod" items="${products}">
                                                            <c:if test="${prod.productID == detail.productID}">
                                                                <c:set var="productNameDisplay" value="${prod.productID} - ${prod.productName}"/>
                                                            </c:if>
                                                        </c:forEach>
                                                        ${productNameDisplay}
                                                    </td>
                                                    <td>${detail.quantity}</td>
                                                    <td><fmt:formatNumber value="${detail.unitPrice}" pattern="#,##0" /> VNĐ</td>
                                                    <td>${detail.discount} %</td>
                                                    <td><fmt:formatNumber value="${detail.totalPrice}" pattern="#,##0" /> VNĐ</td>
                                                    <td>
                                                        <a href="InvoiceServlet?action=manageInvoiceDetails&invoiceID=${selectedInvoice.invoiceID}&editDetailID=${detail.invoiceDetailID}" class="btn btn-info btn-sm me-1">
                                                            <i class='bx bx-edit-alt me-1'></i> Sửa
                                                        </a>
                                                  
<!--                                                        <a href="InvoiceServlet?action=deleteDetail&invoiceID=${selectedInvoice.invoiceID}&invoiceDetailID=${detail.invoiceDetailID}&customerID=${customerID != null ? customerID : defaultCustomerId}" class="btn btn-danger btn-sm" onclick="return confirm('Bạn có chắc chắn muốn xóa mặt hàng này?');">
                                                            <i class='bx bx-trash me-1'></i> Xóa
                                                        </a>-->
                                                    </td>
                                                </c:otherwise>
                                            </c:choose>
                                            </tr>
                                        </c:forEach>
                                        <c:if test="${empty invoiceDetails && empty editDetailID}">
                                            <tr>
                                                <td colspan="6" class="text-center">Chưa có mặt hàng nào trong hóa đơn này.</td>
                                            </tr>
                                        </c:if>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                            <div class="mt-4 text-center">
                                <a href="InvoiceServlet?action=list" class="btn btn-secondary">
                                    <i class='bx bx-arrow-back me-1'></i> Quay lại danh sách Hóa đơn
                                </a>
                           
                                <form action="InvoiceServlet" method="post" style="display: inline-block;">
                                    <input type="hidden" name="action" value="finalizeInvoice" />
                                    <input type="hidden" name="invoiceID" value="${selectedInvoice.invoiceID}" />
                                    <input type="hidden" name="customerID" value="${customerID != null ? customerID : defaultCustomerId}" /> 
                                    <button type="submit" class="btn btn-success" onclick="return confirm('Bạn có chắc chắn muốn hoàn tất hóa đơn này?');">
                                        <i class='bx bx-check me-1'></i> Hoàn tất Hóa đơn
                                    </button>
                                </form>
                            </div>
                        </div>
                        <jsp:include page="footer.jsp" /> 
                        <div class="content-backdrop fade"></div>
                    </div>
                </div>
            </div>

            <div class="layout-overlay layout-menu-toggle"></div>
        </div>
        <script src="${pageContext.request.contextPath}/assets/vendor/libs/jquery/jquery.js"></script>
        <script src="${pageContext.request.contextPath}/assets/vendor/libs/popper/popper.js"></script>
        <script src="${pageContext.request.contextPath}/assets/vendor/js/bootstrap.js"></script>
        <script src="${pageContext.request.contextPath}/assets/vendor/libs/perfect-scrollbar/perfect-scrollbar.js"></script>
        <script src="${pageContext.request.contextPath}/assets/vendor/js/menu.js"></script>
        <script src="${pageContext.request.contextPath}/assets/js/main.js"></script>
        <script async defer src="https://buttons.github.io/buttons.js"></script>
    </body>
</html>