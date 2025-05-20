<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Store - Dashboard</title>
    <style>
        * {
            margin: 0; padding: 0; box-sizing: border-box;
            font-family: Arial, sans-serif;
        }
        body {
            display: flex;
            background-color: #f4f4f4;
        }
        /* Sidebar */
        .sidebar {
            width: 250px;
            background-color: white;
            height: 100vh;
            padding: 20px;
            border-right: 1px solid #ddd;
        }
        .sidebar h2 {
            font-size: 20px;
            margin-bottom: 20px;
        }
        .sidebar .menu {
            display: flex;
            flex-wrap: wrap;
            gap: 15px;
        }
        .sidebar .menu-item {
            width: 48%;
            text-align: center;
            padding: 10px;
            background-color: #f7f9fc;
            border-radius: 10px;
            font-size: 13px;
            border: 1px solid #ddd;
        }

        /* Main content */
        .main {
            flex: 1;
            padding: 20px;
        }
        .header {
            display: flex;
            justify-content: space-between;
            align-items: center;
        }
        .stats {
            display: flex;
            margin-top: 20px;
            gap: 20px;
        }
        .stat-box {
            flex: 1;
            background: linear-gradient(to right, #6a11cb, #2575fc);
            color: white;
            padding: 20px;
            border-radius: 12px;
            position: relative;
        }
        .small-box {
            flex: 1;
            background-color: white;
            padding: 15px;
            border-radius: 12px;
            box-shadow: 0 2px 8px rgba(0,0,0,0.05);
        }

        /* Charts */
        .charts {
            display: grid;
            grid-template-columns: repeat(3, 1fr);
            grid-gap: 20px;
            margin-top: 30px;
        }
        .chart-box {
            background-color: white;
            padding: 15px;
            border-radius: 10px;
            box-shadow: 0 2px 8px rgba(0,0,0,0.05);
        }

        .chart-box h4 {
            margin-bottom: 10px;
        }

        canvas {
            width: 100% !important;
            height: 200px !important;
        }
    </style>
</head>
<body>
    <!-- Sidebar -->s
    <div class="sidebar">
        <h2>Dashboard</h2>
        <div class="menu">
            <div class="menu-item">HomePage</div>
            <div class="menu-item">Cửa Hàng</div>
            <div class="menu-item">Sản Phâm</div>
            <div class="menu-item">Chương trình</div>
            <div class="menu-item">Kho Cửa Hàng</div>
            <div class="menu-item">Thiết bị</div>
            <div class="menu-item">Nhân viên</div>
            <div class="menu-item">Báo cáo</div>
            <div class="menu-item">Kế toán</div>
            <div class="menu-item">Góp ý</div>
        </div>
    </div>

    <!-- Main content -->
    <div class="main">
        <div class="header">
            <h1>Tên cửa hàng</h1>
            <div>Xin chào, <b>quanly@.....com</b></div>
        </div>

        <!-- Stats -->
        <div class="stats">
            <div class="stat-box">
                <h2>Doanh thu (NET)</h2>
                <p style="font-size: 28px;">Số tiên doanh thu theo ngày</p>
                <small></small>
            </div>
            <div class="small-box">
                <h3>Số hóa đơn</h3>
                <p>... hóa đơn</p>
                <small>Trung bình .... / hóa đơn</small>
            </div>
            <div class="small-box">
                <h3>Số khách</h3>
                <p>0 khách</p>
                <small>Trung bình 0 đ / khách</small>
            </div>
        </div>

        <!-- Charts -->
        <div class="charts">
            <div class="chart-box">
                <h4>Doanh thu 7 ngày gần nhất</h4>
                <canvas id="chart1"></canvas>
            </div>
            <div class="chart-box">
                <h4>Top 5 mặt hàng bán chạy</h4>
                <ol>
                    <li>..... - ......... đ</li>
                </ol>
            </div>
            <div class="chart-box">
                <h4>Top 5 chương trình khuyến mãi</h4>
                <canvas id="chart2"></canvas>
            </div>
            <div class="chart-box">
                <h4>Top 5 nguồn đơn</h4>
                <canvas id="chart3"></canvas>
            </div>
            <div class="chart-box">
                <h4>Top 5 phương thức thanh toán</h4>
                <canvas id="chart4"></canvas>
            </div>
            <div class="chart-box">
                <h4>Top 5 cửa hàng có doanh thu cao</h4>
                <canvas id="chart5"></canvas>
            </div>
        </div>
    </div>

    <!-- Chart.js -->
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    <script>
        const ctx1 = document.getElementById('chart1').getContext('2d');
        new Chart(ctx1, {
            type: 'bar',
            data: {
                labels: ['T2','T3','T4','T5','T6','T7','CN'],
                datasets: [{
                    label: 'Doanh thu',
                    data: [2, 4, 3, 3, 5, 4, 6],
                    backgroundColor: '#3b82f6'
                }]
            }
        });
    </script>
</body>
</html>
