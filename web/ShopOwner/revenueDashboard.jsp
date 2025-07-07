<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <title>SaleShape - Dashboard</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" href="./assets/vendor/fonts/boxicons.css">
        <link rel="stylesheet" href="./assets/vendor/css/core.css">
        <link rel="stylesheet" href="./assets/vendor/css/theme-default.css">
        <link rel="stylesheet" href="./assets/css/demo.css">
        <link rel="stylesheet" href="./assets/vendor/libs/apex-charts/apex-charts.css">

        <!-- Fonts & Styles -->
        <link href="https://fonts.googleapis.com/css2?family=Roboto&display=swap&subset=vietnamese" rel="stylesheet" />
        <link rel="icon" type="image/x-icon" href="img/logoSale.png" />
        <link rel="stylesheet" href="./assets/css/custom.css" />
        <link rel="stylesheet" href="./assets/vendor/libs/perfect-scrollbar/perfect-scrollbar.css" />

        <!-- Scripts -->
        <script src="./assets/vendor/js/helpers.js"></script>
        <script src="./assets/js/config.js"></script>
    </head>
    <body>
        <div class="layout-wrapper layout-content-navbar">
            <div class="layout-container">
                <!-- Sidebar -->
                <jsp:include page="sidebar.jsp" />

                <div class="layout-page">
                    <!-- Navbar -->
                    <jsp:include page="navbar.jsp" />

                    <!-- Main content -->
                    <div class="content-wrapper">
                        <div class="container-xxl flex-grow-1 container-p-y">
                            <h4 class="fw-bold py-3 mb-4">Doanh thu</h4>

                            <!-- Filter form -->
                            <form method="get" action="RevenueDashboard" class="mb-4">
                                <label>Từ tháng:</label>
                                <input type="month" name="fromMonth" value="${fromMonth}" required>
                                <label class="ms-3">Đến tháng:</label>
                                <input type="month" name="toMonth" value="${toMonth}" required>
                                <button type="submit" class="btn btn-primary btn-sm ms-2">Lọc</button>
                            </form>

                            <!-- Overview cards -->
                            <div class="row">
                                <div class="col-md-3 mb-4"><div class="card text-center"><div class="card-body"><h6 class="text-muted">Tổng doanh thu</h6><h3 class="text-success"><fmt:formatNumber value="${totalRevenue}" type="currency" currencySymbol="₫"/></h3></div></div></div>
                                <div class="col-md-3 mb-4"><div class="card text-center"><div class="card-body"><h6 class="text-muted">Giao dịch thành công</h6><h3 class="text-primary">${totalTransactions}</h3></div></div></div>
                                <div class="col-md-3 mb-4"><div class="card text-center"><div class="card-body"><h6 class="text-muted">Shop đang hoạt động</h6><h3 class="text-warning">${activeShops}</h3></div></div></div>
                                <div class="col-md-3 mb-4"><div class="card text-center"><div class="card-body"><h6 class="text-muted">Tăng trưởng tháng</h6><h3 class="text-info">${growthRate}%</h3></div></div></div>
                            </div>

                            <!-- Charts -->
                            <div class="row">
                                <div class="col-lg-8 mb-4">
                                    <div class="card">
                                        <div class="card-header"><h5 class="mb-0">Doanh thu theo tháng</h5></div>
                                        <div class="card-body"><div id="revenueChart"></div></div>
                                    </div>
                                </div>
                                <div class="col-lg-4 mb-4">
                                    <div class="card">
                                        <div class="card-header"><h5 class="mb-0">Tỉ lệ gói được chọn</h5></div>
                                        <div class="card-body"><div id="packagePieChart"></div></div>
                                    </div>
                                </div>
                            </div>
                            <!-- Footer -->
                            <jsp:include page="footer.jsp" />
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Scripts -->
        <script src="./assets/vendor/libs/jquery/jquery.js"></script>
        <script src="./assets/vendor/libs/popper/popper.js"></script>
        <script src="./assets/vendor/js/bootstrap.js"></script>
        <script src="./assets/vendor/libs/perfect-scrollbar/perfect-scrollbar.js"></script>
        <script src="./assets/vendor/js/menu.js"></script>
        <script src="./assets/js/main.js"></script>
        <script src="./assets/vendor/libs/apex-charts/apexcharts.js"></script>
        <script>
            const revenueChart = new ApexCharts(document.querySelector("#revenueChart"), {
                chart: {type: 'bar', height: 250},
                series: [{name: 'Doanh thu', data: ${monthlyRevenue}}],
                xaxis: {categories: ${monthLabels}},
                colors: ['#696cff']
            });
            revenueChart.render();

            const pieChart = new ApexCharts(document.querySelector("#packagePieChart"), {
                chart: {type: 'donut', height: 250},
                labels: ${packageLabels},
                series: ${packageRatios},
                colors: ['#03c3ec', '#ffab00', '#696cff', '#8e44ad', '#00b894', '#ff7675']
            });
            pieChart.render();
        </script>
    </body>
</html>
