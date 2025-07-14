<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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
        <link rel="icon" type="image/x-x-icon" href="${pageContext.request.contextPath}/assets/img/favicon/favicon.ico" />
        <link href="https://fonts.googleapis.com/css2?family=Public+Sans:wght@300;400;500;600;700&display=swap" rel="stylesheet" />
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/vendor/fonts/boxicons.css" />
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/vendor/css/core.css" />
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/vendor/css/theme-default.css" />
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
                                <h5 class="card-header">Lọc thống kê theo thời gian</h5>
                                <div class="card-body">
                                    <form action="${pageContext.request.contextPath}/StatisticServlet" method="GET">
                                        <div class="row align-items-end">
                                            <div class="col-md-5 mb-3">
                                                <label for="startDate" class="form-label">Từ ngày:</label>
                                                <input type="date" class="form-control" id="startDate" name="startDate"
                                                       value="${not empty startDate ? startDate : ''}"
                                                       max="<%= new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date()) %>" required>
                                            </div>
                                            <div class="col-md-5 mb-3">
                                                <label for="endDate" class="form-label">Đến ngày:</label>
                                                <input type="date" class="form-control" id="endDate" name="endDate"
                                                       value="${not empty endDate ? endDate : ''}"
                                                       max="<%= new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date()) %>" required>
                                            </div>
                                            <div class="col-md-2 mb-3">
                                                <button type="submit" class="btn btn-primary d-grid w-100">Lọc</button>
                                            </div>
                                        </div>
                                    </form>
                                    <div class="mt-3">
                                        <a href="${pageContext.request.contextPath}/StatisticServlet" class="btn btn-outline-secondary">Xem thống kê tổng cộng</a>
                                    </div>
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
                                                            <td>${String.format("%,.0f", stat.totalRevenue)} VNĐ</td>
                                                            <td>${stat.totalOrders}</td>
                                                            <td>${String.format("%,.0f", stat.averageRevenuePerOrder)} VNĐ</td>
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
                // Đăng ký plugin Datalabels TOÀN CỤC sau khi cả Chart.js và plugin đã được tải
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
                            // --- THÊM PHẦN CẤU HÌNH DATALABELS NÀY VÀO BIỂU ĐỒ CỘT ---
                            datalabels: {
                                formatter: function (value, context) {
                                    // Định dạng số có dấu phẩy ngăn cách hàng nghìn
                                    return value.toLocaleString('vi-VN') + ' VNĐ'; // Sử dụng 'vi-VN' cho định dạng tiếng Việt
                                },
                                color: '#fff', // Màu chữ trắng để dễ nhìn trên nền cột xanh
                                font: {
                                    weight: 'bold',
                                    size: 11 // Kích thước chữ
                                },
                                anchor: 'center', // Vị trí nhãn: 'center', 'end', 'start'
                                align: 'center'   // Căn chỉnh nhãn so với anchor
                            }
                            // --------------------------------------------------------
                        },
                        scales: {
                            y: {
                                ticks: {
                                    callback: value => value.toLocaleString('vi-VN') + ' VNĐ' // Đảm bảo cũng dùng toLocaleString cho trục Y
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
                            // Cấu hình datalabels cho biểu đồ tròn
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
                                    // Chỉ hiển thị nhãn nếu lát cắt lớn hơn 3% tổng số đơn hàng
                                    // Điều này giúp tránh chồng chéo nhãn cho các lát cắt quá nhỏ
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