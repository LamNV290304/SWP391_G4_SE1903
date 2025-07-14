<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
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
        <meta charset="UTF-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
        <title>Báo cáo & Thống kê Đồ dùng Cửa hàng - Sneat</title>

        <link rel="icon" type="image/x-xicon" href="${pageContext.request.contextPath}/assets/img/favicon/favicon.ico" />
        <link rel="preconnect" href="https://fonts.googleapis.com" />
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
        <link href="https://fonts.googleapis.com/css2?family=Public+Sans:wght@300;400;500;600;700&display=swap" rel="stylesheet" />
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/vendor/fonts/boxicons.css" />
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/vendor/css/core.css" class="template-customizer-core-css" />
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/vendor/css/theme-default.css" class="template-customizer-theme-css" />
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/demo.css" />
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/vendor/libs/perfect-scrollbar/perfect-scrollbar.css" />

        <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>

        <script src="${pageContext.request.contextPath}/assets/vendor/js/helpers.js"></script>
        <script src="${pageContext.request.contextPath}/assets/js/config.js"></script>

        <style>
            /* Đảm bảo biểu đồ có chiều cao cố định để không bị quá nhỏ */
            .chart-container {
                position: relative;
                height: 300px; /* Điều chỉnh chiều cao theo ý muốn */
                width: 100%;
            }
        </style>
    </head>
    <body>
        <div class="layout-wrapper layout-content-navbar">
            <div class="layout-container">
                <%-- Include Sidebar --%>
                <jsp:include page="sidebar.jsp" />

                <div class="layout-page">
                    <%-- Include Navbar --%>
                    <jsp:include page="navBar.jsp" />

                    <div class="content-wrapper">
                        <div class="container-xxl flex-grow-1 container-p-y">
                            <h4 class="fw-bold py-3 mb-4"><span class="text-muted fw-light">Báo cáo /</span> Thống kê Đồ dùng Cửa hàng</h4>

                            <%-- Thêm khối này để hiển thị thông báo lỗi từ Servlet --%>
                            <c:if test="${not empty sessionScope.errorMessage}">
                                <div class="alert alert-danger" role="alert">
                                    ${sessionScope.errorMessage}
                                    <%-- Xóa thông báo lỗi khỏi session sau khi hiển thị --%>
                                    <c:remove var="errorMessage" scope="session"/>
                                </div>
                            </c:if>

                            <div class="card mb-4">
                                <h5 class="card-header">Tìm kiếm Báo cáo</h5>
                                <div class="card-body">
                                    <form action="ReportItemServlet" method="get">
                                        <div class="row g-3">
                                            <div class="col-md-6">
                                                <label for="categoryName" class="form-label">Tên Danh mục</label>
                                                <input type="text" id="categoryName" name="categoryName" class="form-control" placeholder="Nhập tên danh mục" value="${param.categoryName}">
                                            </div>
                                            <div class="col-md-6">
                                                <label for="shopName" class="form-label">Tên Cửa hàng</label>
                                                <input type="text" id="shopName" name="shopName" class="form-control" placeholder="Nhập tên cửa hàng" value="${param.shopName}">
                                            </div>
                                            <div class="col-12 mt-4">
                                                <button type="submit" class="btn btn-primary">Tìm kiếm & Lọc</button>
                                                <a href="ReportItemServlet" class="btn btn-secondary ms-2">Đặt lại bộ lọc</a>
                                            </div>
                                        </div>
                                    </form>
                                </div>
                            </div>

                            <div class="row mb-4">
                                <div class="col-md-6">
                                    <div class="card h-100">
                                        <div class="card-header d-flex align-items-center justify-content-between">
                                            <h5 class="card-title m-0 me-2">Số lượng Đồ dùng theo Danh mục</h5>
                                        </div>
                                        <div class="card-body">
                                            <div class="chart-container">
                                                <canvas id="categoryChart"></canvas>
                                            </div>
                                            <c:if test="${empty categoryCounts}">
                                                <p class="text-center text-muted mt-3">Không có dữ liệu để hiển thị biểu đồ danh mục.</p>
                                            </c:if>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <div class="card h-100">
                                        <div class="card-header d-flex align-items-center justify-content-between">
                                            <h5 class="card-title m-0 me-2">Tổng Giá trị Đồ dùng theo Cửa hàng</h5>
                                        </div>
                                        <div class="card-body">
                                            <div class="chart-container">
                                                <canvas id="shopValueChart"></canvas>
                                            </div>
                                            <c:if test="${empty shopTotalValues}">
                                                <p class="text-center text-muted mt-3">Không có dữ liệu để hiển thị biểu đồ giá trị cửa hàng.</p>
                                            </c:if>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <%-- Báo cáo số lượng theo Danh mục (Bảng lớn) --%>
                            <div class="card mb-4">
                                <h5 class="card-header">Chi tiết Số lượng Đồ dùng theo Danh mục</h5>
                                <div class="table-responsive text-nowrap">
                                    <table class="table card-table">
                                        <thead>
                                            <tr>
                                                <th>ID Danh mục</th>
                                                <th>Tên Danh mục</th>
                                                <th>Tổng số Đồ dùng</th>
                                            </tr>
                                        </thead>
                                        <tbody class="table-border-bottom-0">
                                            <c:if test="${empty categoryCounts}">
                                                <tr>
                                                    <td colspan="3">Không có dữ liệu báo cáo số lượng theo danh mục.</td>
                                                </tr>
                                            </c:if>
                                            <c:forEach var="entry" items="${categoryCounts}">
                                                <tr>
                                                    <td>${entry.categoryId}</td>
                                                    <td>${entry.categoryName}</td>
                                                    <td><span class="badge bg-label-primary me-1">${entry.itemCount}</span></td>
                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                            </div>

                            <%-- Báo cáo tổng giá trị theo Cửa hàng (Bảng lớn) --%>
                            <div class="card mb-4">
                                <h5 class="card-header">Chi tiết Tổng Giá trị Đồ dùng theo Cửa hàng</h5>
                                <div class="table-responsive text-nowrap">
                                    <table class="table card-table">
                                        <thead>
                                            <tr>
                                                <th>ID Cửa hàng</th>
                                                <th>Tên Cửa hàng</th>
                                                <th>Tổng giá trị (VNĐ)</th>
                                            </tr>
                                        </thead>
                                        <tbody class="table-border-bottom-0">
                                            <c:if test="${empty shopTotalValues}">
                                                <tr>
                                                    <td colspan="3">Không có dữ liệu báo cáo tổng giá trị theo cửa hàng.</td>
                                                </tr>
                                            </c:if>
                                            <c:forEach var="entry" items="${shopTotalValues}">
                                                <tr>
                                                    <td>${entry.shopId}</td>
                                                    <td>${entry.shopName}</td>
                                                    <td>
                                                        <span class="badge bg-label-success me-1">
                                                            <fmt:formatNumber value="${entry.totalValue}" type="currency" currencySymbol="VNĐ" maxFractionDigits="0"/>
                                                        </span>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                            </div>

                        </div>
                        <%-- Include Footer --%>
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

        <script>
            document.addEventListener('DOMContentLoaded', function () {
                // Dữ liệu cho biểu đồ số lượng theo Danh mục
                var categoryLabels = [];
                var categoryCountsData = [];
                <c:forEach var="entry" items="${categoryCounts}">
                    categoryLabels.push('${entry.categoryName}');
                    categoryCountsData.push(${entry.itemCount});
                </c:forEach>

                if (categoryCountsData.length > 0) {
                    var ctxCategory = document.getElementById('categoryChart').getContext('2d');
                    new Chart(ctxCategory, {
                        type: 'bar', // Biểu đồ cột
                        data: {
                            labels: categoryLabels,
                            datasets: [{
                                label: 'Số lượng Đồ dùng',
                                data: categoryCountsData,
                                backgroundColor: [
                                    'rgba(255, 99, 132, 0.6)',
                                    'rgba(54, 162, 235, 0.6)',
                                    'rgba(255, 206, 86, 0.6)',
                                    'rgba(75, 192, 192, 0.6)',
                                    'rgba(153, 102, 255, 0.6)',
                                    'rgba(255, 159, 64, 0.6)'
                                ],
                                borderColor: [
                                    'rgba(255, 99, 132, 1)',
                                    'rgba(54, 162, 235, 1)',
                                    'rgba(255, 206, 86, 1)',
                                    'rgba(75, 192, 192, 1)',
                                    'rgba(153, 102, 255, 1)',
                                    'rgba(255, 159, 64, 1)'
                                ],
                                borderWidth: 1
                            }]
                        },
                        options: {
                            responsive: true,
                            maintainAspectRatio: false, // Cho phép biểu đồ co giãn theo kích thước div
                            scales: {
                                y: {
                                    beginAtZero: true,
                                    title: {
                                        display: true,
                                        text: 'Số lượng'
                                    }
                                },
                                x: {
                                    title: {
                                        display: true,
                                        text: 'Danh mục'
                                    }
                                }
                            },
                            plugins: {
                                legend: {
                                    display: false
                                }
                            }
                        }
                    });
                }

                // Dữ liệu cho biểu đồ tổng giá trị theo Cửa hàng
                var shopLabels = [];
                var shopTotalValuesData = [];
                <c:forEach var="entry" items="${shopTotalValues}">
                    shopLabels.push('${entry.shopName}');
                    shopTotalValuesData.push(${entry.totalValue});
                </c:forEach>

                if (shopTotalValuesData.length > 0) {
                    var ctxShop = document.getElementById('shopValueChart').getContext('2d');
                    new Chart(ctxShop, {
                        type: 'pie', // Biểu đồ tròn
                        data: {
                            labels: shopLabels,
                            datasets: [{
                                label: 'Tổng giá trị (VNĐ)',
                                data: shopTotalValuesData,
                                backgroundColor: [
                                    'rgba(255, 99, 132, 0.6)',
                                    'rgba(54, 162, 235, 0.6)',
                                    'rgba(255, 206, 86, 0.6)',
                                    'rgba(75, 192, 192, 0.6)',
                                    'rgba(153, 102, 255, 0.6)',
                                    'rgba(255, 159, 64, 0.6)',
                                    'rgba(199, 199, 199, 0.6)',
                                    'rgba(83, 102, 255, 0.6)'
                                ],
                                borderColor: [
                                    'rgba(255, 99, 132, 1)',
                                    'rgba(54, 162, 235, 1)',
                                    'rgba(255, 206, 86, 1)',
                                    'rgba(75, 192, 192, 1)',
                                    'rgba(153, 102, 255, 1)',
                                    'rgba(255, 159, 64, 1)',
                                    'rgba(199, 199, 199, 1)',
                                    'rgba(83, 102, 255, 1)'
                                ],
                                borderWidth: 1
                            }]
                        },
                        options: {
                            responsive: true,
                            maintainAspectRatio: false, // Cho phép biểu đồ co giãn theo kích thước div
                            plugins: {
                                legend: {
                                    position: 'top',
                                },
                                tooltip: {
                                    callbacks: {
                                        label: function (context) {
                                            let label = context.label || '';
                                            if (label) {
                                                label += ': ';
                                            }
                                            if (context.parsed !== null) {
                                                label += new Intl.NumberFormat('vi-VN', {style: 'currency', currency: 'VND'}).format(context.parsed);
                                            }
                                            return label;
                                        }
                                    }
                                }
                            }
                        }
                    });
                }
            });
        </script>

    </body>
</html>