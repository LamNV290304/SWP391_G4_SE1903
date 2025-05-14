<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Register</title>
        <!-- Bootstrap CSS -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@300;400;600;700&display=swap" rel="stylesheet">
        <link href="css/Header_Footer.css" rel="stylesheet">
        <link rel="stylesheet" href="css/register.css">
    </head>
    <body>
        <%@ include file="Header.jsp" %>

        <div class="container mb-5">
            <div class="row justify-content-center mt-5">
                <div class="col-md-6">
                    <div class="card shadow-sm">
                        <div class="card-body">
                            <h3 class="text-center mb-4">Tạo tài khoản</h3>

                            <form method="POST" action="register">
                                <c:if test="${requestScope.error != null}">
                                    <div class="alert alert-danger">
                                        ${requestScope.error}
                                    </div>
                                </c:if>

                                <div class="row">
                                    <!-- Cột 1 -->
                                    <div class="col-md-6">
                                        <div class="mb-3">
                                            <label for="name" class="form-label">Họ và tên</label>
                                            <input type="text" class="form-control" id="name" name="name" placeholder="Nhập họ và tên" required>
                                        </div>

                                        <div class="mb-3">
                                            <label for="password" class="form-label">Mật khẩu</label>
                                            <input type="password" class="form-control" id="password" name="password" placeholder="Nhập mật khẩu" required>
                                        </div>

                                        <div class="mb-3">
                                            <label for="phone" class="form-label">Số điện thoại</label>
                                            <input type="text" class="form-control" id="phone" name="phone" placeholder="Nhập số điện thoại" required>
                                        </div>
                                    </div>

                                    <!-- Cột 2 -->
                                    <div class="col-md-6">
                                        <div class="mb-3">
                                            <label for="email" class="form-label">Email</label>
                                            <input type="email" class="form-control" id="email" name="email" placeholder="Nhập email của bạn" required>
                                        </div>

                                        <div class="mb-3">
                                            <label for="confirm-password" class="form-label">Xác nhận mật khẩu</label>
                                            <input type="password" class="form-control" id="confirm-password" name="confirmPassword" placeholder="Xác nhận lại mật khẩu" required>
                                        </div>

                                        <div class="mb-3">
                                            <label for="address" class="form-label">Địa chỉ</label>
                                            <input type="text" class="form-control" id="address" name="address" placeholder="Nhập địa chỉ của bạn" required>
                                        </div>
                                    </div>
                                </div>

                                <div class="mb-3">
                                    <label for="roleId" class="form-label">Vai trò</label>
                                    <select class="form-select" id="roleId" name="roleId" required>
                                        <option value="" disabled selected>Chọn vai trò</option>
                                        <option value="1">Admin</option>
                                        <option value="2" selected>User</option>
                                        <option value="3">Nhân viên</option>
                                        <!-- Thêm vai trò khác nếu cần -->
                                    </select>
                                </div>

                                <div class="text-center">
                                    <button type="submit" class="btn btn-primary w-100">Đăng ký</button>
                                </div>
                            </form>


                            <div class="text-center mt-3">
                                <p>Đã có tài khoản? <a href="Login.jsp"> Đăng nhập ở đây</a></p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <%@ include file="Footer.jsp" %>
    </body>
</html>
