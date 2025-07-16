<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html
    lang="vi"
    class="light-style layout-menu-fixed"
    dir="ltr"
    data-theme="theme-default"
    data-assets-path="./assets/"
    data-template="vertical-menu-template-free"
    >
    <head>
        <title>SaleShape</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link href="https://fonts.googleapis.com/css2?family=Roboto&display=swap&subset=vietnamese" rel="stylesheet">

        <link href="https://fonts.googleapis.com/css2?family=Roboto&display=swap" rel="stylesheet" />
        <link rel="icon" type="image/x-icon" href="img/logoSale.png" />
        <link rel="preconnect" href="https://fonts.googleapis.com" />
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
        <link
            href="https://fonts.googleapis.com/css2?family=Public+Sans:ital,wght@0,300;0,400;0,500;0,600;0,700;1,300;1,400;1,500;1,600;1,700&display=swap"
            rel="stylesheet"
            />

        <link rel="stylesheet" href="./assets/css/custom.css" />

        <link rel="stylesheet" href="./assets/vendor/fonts/boxicons.css" />

        <link rel="stylesheet" href="./assets/vendor/css/core.css" class="template-customizer-core-css" />
        <link rel="stylesheet" href="./assets/vendor/css/theme-default.css" class="template-customizer-theme-css" />
        <link rel="stylesheet" href="./assets/css/demo.css" />

        <link rel="stylesheet" href="./assets/vendor/libs/perfect-scrollbar/perfect-scrollbar.css" />

        <link rel="stylesheet" href="./assets/vendor/libs/apex-charts/apex-charts.css" />

        <script src="./assets/vendor/js/helpers.js"></script>

        <script src="./assets/js/config.js"></script>
    </head>
    <body>
        <div  class="layout-wrapper layout-content-navbar">
            <div class="layout-container">
                <jsp:include page="sidebar.jsp" />

                <div class="layout-page">
                    <jsp:include page="navBar.jsp" />
                    <div class="content-wrapper">
                        <button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#addFormModal">
  Thêm Phiếu Mới
</button>
                        <div class="modal fade" id="addFormModal" tabindex="-1" aria-labelledby="addFormLabel" aria-hidden="true">
  <div class="modal-dialog modal-lg"> <!-- modal-lg nếu bạn muốn rộng -->
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="addFormLabel">Thêm Phiếu Nhập</h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Đóng"></button>
      </div>
      <div class="modal-body">
        <form action="AddImportReceiptServlet" method="post">
          <div class="mb-3">
            <label class="form-label">Mã phiếu</label>
            <input type="text" name="receiptCode" class="form-control" required />
          </div>
          <div class="mb-3">
            <label class="form-label">Ngày nhập</label>
            <input type="date" name="importDate" class="form-control" required />
          </div>
          <!-- Thêm các trường khác tại đây -->
          <div class="text-end">
            <button type="submit" class="btn btn-success">Lưu</button>
            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
          </div>
        </form>
      </div>
    </div>
  </div>
</div>

                    </div>
                    <jsp:include page="footer.jsp" />
                </div>

            </div>
        </div>


        <script src="assets/vendor/libs/jquery/jquery.js"></script>
        <script src="assets/vendor/libs/popper/popper.js"></script>
        <script src="assets/vendor/js/bootstrap.js"></script>
        <script src="assets/vendor/libs/perfect-scrollbar/perfect-scrollbar.js"></script>
        <script src="assets/vendor/js/menu.js"></script> <script src="assets/js/main.js"></script> </body>
</html>