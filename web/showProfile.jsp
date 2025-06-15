<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="en" class="light-style layout-menu-fixed" data-theme="theme-default"
      data-assets-path="./assets/" data-template="vertical-menu-template-free">

    <head>
        <meta charset="UTF-8" />
        <meta name="viewport"
              content="width=device-width, initial-scale=1.0, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0" />
        <title>User Profile | Sneat</title>

        <!-- Favicon & Fonts -->
        <link rel="icon" type="image/x-icon" href="./assets/img/favicon/favicon.ico" />
        <link rel="stylesheet" href="./assets/vendor/fonts/boxicons.css" />

        <!-- Core CSS -->
        <link rel="stylesheet" href="./assets/vendor/css/core.css" class="template-customizer-core-css" />
        <link rel="stylesheet" href="./assets/vendor/css/theme-default.css" class="template-customizer-theme-css" />
        <link rel="stylesheet" href="./assets/css/demo.css" />
        <link rel="stylesheet" href="./assets/vendor/libs/perfect-scrollbar/perfect-scrollbar.css" />

        <script src="./assets/vendor/js/helpers.js"></script>
        <script src="./assets/js/config.js"></script>
    </head>

    <body>
        <div class="layout-wrapper layout-content-navbar">
            <div class="layout-container">

                <!-- Sidebar -->
                <jsp:include page="sidebar.jsp" />

                <!-- Page Content -->
                <div class="layout-page">
                    <!-- Navbar -->
                    <jsp:include page="navBar.jsp" />

                    <!-- Content Wrapper -->
                    <div class="content-wrapper">
                        <div class="container-xxl flex-grow-1 container-p-y">
                            <h4 class="fw-bold py-3 mb-4"><span class="text-muted fw-light">Account /</span> Profile</h4>

                            <div class="card mb-4">
                                <div class="card-body d-flex align-items-center">
                                    <img src="./assets/img/avatars/1.png" alt="User avatar" class="rounded-circle me-3" width="80" height="80">
                                    <div>
                                        <h5 class="mb-1">Nguyễn Việt Lâm</h5>
                                        <span class="badge bg-label-primary">Quản trị viên</span>
                                    </div>
                                    <div class="ms-auto">
                                        <button type="button" class="btn btn-outline-primary" data-bs-toggle="modal" data-bs-target="#editProfileModal">
                                            Edit Profile
                                        </button>
                                    </div>
                                </div>
                            </div>

                            <div class="card">
                                <h5 class="card-header">Thông tin chi tiết</h5>
                                <div class="card-body">
                                    <ul class="list-unstyled mb-0">
                                        <li class="mb-3"><strong>Email:</strong> lam.nguyen@gmail.com</li>
                                        <li class="mb-3"><strong>Số điện thoại:</strong> 0987654321</li>
                                        <li class="mb-3"><strong>Tên đăng nhập:</strong> lamnv</li>
                                        <li class="mb-3"><strong>Trạng thái:</strong>
                                            <span class="badge bg-success">Hoạt động</span>
                                        </li>
                                        <li class="mb-3"><strong>Ngày tạo:</strong> 2024-12-15</li>
                                    </ul>
                                </div>
                            </div>
                        </div>
                        <jsp:include page="footer.jsp" />
                    </div>
                </div>
            </div>
        </div>

        <!-- Edit Profile Modal -->
        <div class="modal fade" id="editProfileModal" tabindex="-1" aria-labelledby="editProfileModalLabel" aria-hidden="true">
          <div class="modal-dialog">
            <form action="updateProfile" method="post" class="modal-content">
              <div class="modal-header">
                <h5 class="modal-title" id="editProfileModalLabel">Chỉnh sửa hồ sơ</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Đóng"></button>
              </div>
              <div class="modal-body">
                <div class="mb-3">
                  <label class="form-label">Họ và tên</label>
                  <input type="text" name="fullName" class="form-control" value="Nguyễn Việt Lâm" required>
                </div>
                <div class="mb-3">
                  <label class="form-label">Email</label>
                  <input type="email" name="email" class="form-control" value="lam.nguyen@gmail.com" required>
                </div>
                <div class="mb-3">
                  <label class="form-label">Số điện thoại</label>
                  <input type="text" name="phone" class="form-control" value="0987654321" required>
                </div>
                <input type="hidden" name="username" value="lamnv">
              </div>
              <div class="modal-footer">
                <button type="button" class="btn btn-outline-secondary" data-bs-dismiss="modal">Hủy</button>
                <button type="submit" class="btn btn-primary">Lưu thay đổi</button>
              </div>
            </form>
          </div>
        </div>

        <!-- Scripts -->
        <script src="./assets/vendor/libs/jquery/jquery.js"></script>
        <script src="./assets/vendor/libs/popper/popper.js"></script>
        <script src="./assets/vendor/js/bootstrap.js"></script>
        <script src="./assets/vendor/libs/perfect-scrollbar/perfect-scrollbar.js"></script>
        <script src="./assets/vendor/js/menu.js"></script>
        <script src="./assets/js/main.js"></script>

    </body>
</html>
