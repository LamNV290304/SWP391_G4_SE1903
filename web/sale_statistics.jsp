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
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Thống kê Doanh số Bán hàng</title>
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
                            <h4 class="fw-bold py-3 mb-4"><span class="text-muted fw-light">Thống kê /</span> ${statisticTitle}</h4>

                            <c:if test="${not empty errorMessage}">
                                <div class="alert alert-danger" role="alert">${errorMessage}</div>
                            </c:if>

                            <div class="card mb-4">
                                <h5 class="card-header">Lọc thống kê</h5>
                                <div class="card-body">
                                    <form id="reportForm" action="StatisticServlet" method="GET">
                                        <div class="row align-items-end">
                                            <div class="col-md-3 mb-3">
                                                <label for="shopSelect" class="form-label">Chọn cửa hàng:</label>
                                                <select class="form-select" id="shopSelect" name="shopId">
                                                    <option value="" ${selectedShopId == null ? 'selected' : ''}>Tất cả cửa hàng</option>
                                                    <c:forEach var="shop" items="${shops}">
                                                        <option value="${shop.shopID}" ${selectedShopId != null && selectedShopId == shop.shopID ? 'selected' : ''}>
                                                            ${shop.shopName}
                                                        </option>
                                                    </c:forEach>
                                                </select>
                                            </div>
                                            <div class="col-md-3 mb-3">
                                                <label for="startDate" class="form-label">Từ ngày:</label>
                                                <input type="date" class="form-control" id="startDate" name="startDate"
                                                       value="${not empty startDate ? startDate : ''}"
                                                       max="<%= new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date()) %>">
                                            </div>
                                            <div class="col-md-3 mb-3">
                                                <label for="endDate" class="form-label">Đến ngày:</label>
                                                <input type="date" class="form-control" id="endDate" name="endDate"
                                                       value="${not empty endDate ? endDate : ''}"
                                                       max="<%= new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date()) %>">
                                            </div>
                                            <div class="col-md-3 mb-3">
                                                <button type="submit" class="btn btn-primary me-2">Lọc</button>
                                                <a href="StatisticServlet" class="btn btn-outline-secondary">Xem tổng cộng</a>
                                            </div>

                                            <input type="hidden" name="page" value="${currentPage}" />
                                            <input type="hidden" name="recordsPerPage" value="${recordsPerPage}" />
                                        </div>
                                    </form>
                                </div>
                            </div>

                            <c:if test="${not empty salesStatistics}">
                                <div class="row">
                                    <div class="col-md-6 mb-2">
                                        <div class="card h-100">
                                            <div class="card-header py-1 px-3"><span class="fw-light small">📊 Doanh thu theo nhân viên</span></div>
                                            <div class="card-body p-1" style="max-height:300px;">
                                                <canvas id="revenueChart" style="height: 120px;"></canvas>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="col-md-6 mb-2">
                                        <div class="card h-100">
                                            <div class="card-header py-1 px-3"><span class="fw-light small">🧾 Tỷ lệ đơn hàng</span></div>
                                            <div class="card-body d-flex align-items-center justify-content-between p-2" style="height: 240px;">
                                                <canvas id="orderPieChart" style="width: 65%; height: 100%;"></canvas>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </c:if>

                            <div class="card mb-4">
                                <h5 class="card-header">${statisticTitle}</h5>
                                <div class="table-responsive text-nowrap">
                                    <table class="table card-table">
                                        <thead>
                                            <tr>
                                                <th>Mã NV</th>
                                                <th>Tên nhân viên</th>
                                                <th>Tổng doanh thu</th>
                                                <th>Tổng số đơn hàng</th>
                                                <th>Doanh thu trung bình/Đơn hàng</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:choose>
                                                <c:when test="${not empty salesStatistics}">
                                                    <c:forEach var="stat" items="${salesStatistics}">
                                                        <tr>
                                                            <td><strong>${stat.employeeID}</strong></td>
                                                            <td>${stat.fullName}</td>
                                                            <td><fmt:formatNumber value="${stat.totalRevenue}" pattern="#,##0" /> VNĐ</td>
                                                            <td>${stat.totalOrders}</td>
                                                            <td><fmt:formatNumber value="${stat.averageRevenuePerOrder}" pattern="#,##0" /> VNĐ</td>
                                                        </tr>
                                                    </c:forEach>
                                                </c:when>
                                                <c:otherwise>
                                                    <tr>
                                                        <td colspan="5" class="text-center">Không có dữ liệu thống kê.</td>
                                                    </tr>
                                                </c:otherwise>
                                            </c:choose>
                                        </tbody>
                                    </table>
                                </div>
                                <%-- Phần phân trang --%>
                                <div class="card-footer d-flex justify-content-center flex-wrap align-items-center">
                                    <nav aria-label="Page navigation" class="mb-2 mb-md-0">
                                        <c:if test="${totalPages > 1}">
                                            <ul class="pagination mb-0">
                                                <c:url var="baseLink" value="StatisticServlet">
                                                    <%-- Giữ lại các tham số lọc hiện tại --%>
                                                    <c:if test="${not empty param.startDate}">
                                                        <c:param name="startDate" value="${param.startDate}" />
                                                    </c:if>
                                                    <c:if test="${not empty param.endDate}">
                                                        <c:param name="endDate" value="${param.endDate}" />
                                                    </c:if>
                                                    <c:if test="${not empty param.shopId}">
                                                        <c:param name="shopId" value="${param.shopId}" />
                                                    </c:if>
                                                    <c:if test="${not empty recordsPerPage}">
                                                        <c:param name="recordsPerPage" value="${recordsPerPage}" />
                                                    </c:if>
                                                </c:url>

                                                <li class="page-item <c:if test="${currentPage == 1}">disabled</c:if>">
                                                    <a class="page-link" href="<c:url value="${baseLink}"><c:param name="page" value="${currentPage - 1}"/></c:url>">
                                                            <i class="tf-icon bx bx-chevrons-left"></i>
                                                        </a>
                                                    </li>
                                                <c:set var="numPagesToShow" value="5" />
                                                <c:set var="halfPagesToShow" value="${numPagesToShow / 2}" />

                                                <c:set var="startPage" value="${currentPage - halfPagesToShow}" />
                                                <c:set var="endPage" value="${currentPage + halfPagesToShow}" />

                                                <c:if test="${startPage < 1}">
                                                    <c:set var="startPage" value="1" />
                                                    <c:set var="endPage" value="${numPagesToShow > totalPages ? totalPages : numPagesToShow}" />
                                                </c:if>

                                                <c:if test="${endPage > totalPages}">
                                                    <c:set var="endPage" value="${totalPages}" />
                                                    <c:set var="startPage" value="${totalPages - numPagesToShow + 1}" />
                                                    <c:if test="${startPage < 1}">
                                                        <c:set var="startPage" value="1" />
                                                    </c:if>
                                                </c:if>

                                                <c:forEach begin="${startPage}" end="${endPage}" var="i">
                                                    <li class="page-item <c:if test="${i == currentPage}">active</c:if>">
                                                        <c:url var="pageLink" value="${baseLink}">
                                                            <c:param name="page" value="${i}" />
                                                        </c:url>
                                                        <a class="page-link" href="${pageLink}">${i}</a>
                                                    </li>
                                                </c:forEach>

                                                <li class="page-item <c:if test="${currentPage == totalPages}">disabled</c:if>">
                                                    <a class="page-link" href="<c:url value="${baseLink}"><c:param name="page" value="${currentPage + 1}"/></c:url>">
                                                            <i class="tf-icon bx bx-chevrons-right"></i>
                                                        </a>
                                                    </li>
                                                </ul>
                                        </c:if>
                                    </nav>
                             
                                </div>
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

        <script src="https://cdn.jsdelivr.net/npm/chart.js@4.4.2/dist/chart.umd.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/chartjs-plugin-datalabels@2.2.0/dist/chartjs-plugin-datalabels.min.js"></script>

        <c:if test="${not empty salesStatistics}">
            <script>
                Chart.register(ChartDataLabels);

                const employeeNames = [
                <c:forEach var="stat" items="${salesStatistics}" varStatus="loop">
                "${stat.fullName}"<c:if test="${!loop.last}">,</c:if>
                </c:forEach>
                ];
                const revenues = [
                <c:forEach var="stat" items="${salesStatistics}" varStatus="loop">
                    ${stat.totalRevenue}<c:if test="${!loop.last}">,</c:if>
                </c:forEach>
                ];
                const orders = [
                <c:forEach var="stat" items="${salesStatistics}" varStatus="loop">
                    ${stat.totalOrders * 1}<c:if test="${!loop.last}">,</c:if>
                </c:forEach>
                ];

                new Chart(document.getElementById('revenueChart').getContext('2d'), {
                    type: 'bar',
                    data: {
                        labels: employeeNames,
                        datasets: [{
                                label: 'Doanh thu (VNĐ)',
                                data: revenues,
                                backgroundColor: '#5A8DEE'
                            }]
                    },
                    options: {
                        responsive: true,
                        plugins: {
                            legend: {display: false},
                            tooltip: {
                                callbacks: {
                                    label: ctx => ctx.formattedValue.replace(/\B(?=(\d{3})+(?!\d))/g, ",") + ' VNĐ'
                                }
                            },
                            datalabels: {
                                formatter: function (value, context) {
                                    return value.toLocaleString('vi-VN') + ' VNĐ';
                                },
                                color: '#fff',
                                font: {
                                    weight: 'bold',
                                    size: 11
                                },
                                anchor: 'center',
                                align: 'center'
                            }
                        },
                        scales: {
                            y: {
                                ticks: {
                                    callback: value => value.toLocaleString('vi-VN') + ' VNĐ'
                                }
                            }
                        }
                    }
                });

                new Chart(document.getElementById('orderPieChart').getContext('2d'), {
                    type: 'pie',
                    data: {
                        labels: employeeNames,
                        datasets: [{
                                data: orders,
                                backgroundColor: ['#5A8DEE', '#39DA8A', '#FF5B5C', '#FDAC41', '#00CFDD']
                            }]
                    },
                    options: {
                        responsive: true,
                        plugins: {
                            legend: {
                                position: 'right',
                                labels: {
                                    boxWidth: 12,
                                    padding: 15
                                }
                            },
                            tooltip: {
                                callbacks: {
                                    label: function (ctx) {
                                        const value = Number(ctx.raw);
                                        const total = ctx.dataset.data.reduce((acc, val) => acc + Number(val), 0);
                                        const percentage = ((value / total) * 100).toFixed(1);
                                        const label = ctx.label || ctx.chart.data.labels[ctx.dataIndex];
                                        return `${label}: ${value} đơn (${percentage}%)`;
                                    }
                                }
                            },
                            datalabels: {
                                formatter: (value, ctx) => {
                                    let sum = 0;
                                    let dataArr = ctx.chart.data.datasets[0].data;
                                    dataArr.forEach(data => {
                                        sum += Number(data);
                                    });
                                    let percentage = (value * 100 / sum).toFixed(1) + "%";
                                    return percentage;
                                },
                                color: '#fff',
                                font: {
                                    weight: 'bold',
                                    size: 14
                                },
                                anchor: 'end',
                                align: 'start',
                                offset: 5,
                                display: function (ctx) {
                                    return ctx.dataset.data[ctx.dataIndex] > (ctx.chart.data.datasets[0].data.reduce((a, b) => a + b, 0) * 0.03);
                                }
                            }
                        }
                    }
                });
            </script>
        </c:if>
    </body>
</html>