<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Login</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@300;400;600;700&display=swap" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-icons/1.10.5/font/bootstrap-icons.min.css" rel="stylesheet">
    <link rel="stylesheet" href="css/LoginStyle.css">
</head>
<body>

<!--    <div class="banner">
        <img src="images/banner.png" alt="Banner">
    </div>-->

    <div class="form-container">
        <form method="post" action="login">
            <h2 class="login-title">Đăng nhập tài khoản</h2>

            <c:if test="${not empty error}">
                <div class="alert alert-danger alert-size" role="alert">
                    ${error}
                </div>
            </c:if>

            <div class="mb-3">
                <input type="text" class="form-control" name="username" placeholder="Tên đăng nhập hoặc Email"
                       value="${param.username}" required>
            </div>

            <div class="mb-3">
                <input type="password" class="form-control" name="password" placeholder="Mật khẩu" required>
            </div>

            <button type="submit" class="btn btn-login">Đăng nhập</button>
        </form>

        <div class="forgot-link">
            <span>Hoặc <a href="findAccount.jsp">Quên mật khẩu?</a></span>
        </div>
    </div>

</body>
</html>
