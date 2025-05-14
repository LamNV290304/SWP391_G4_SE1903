<%-- 
    Document   : Footer
    Created on : May 14, 2025, 8:26:49 PM
    Author     : Admin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="css/FooterStyle.css">
        <title>JSP Page</title>
    </head>
    <body class="d-flex flex-column min-vh-100">  <!-- Thêm class để đảm bảo footer luôn ở dưới đáy -->
        <footer class="bg-primary text-white mt-auto py-3"> <!-- Thêm mt-auto để footer dính dưới đáy -->
            <div class="container">
                <div class="row">
                    <div class="col-md-6 mb-3">
                        <h5 class="fw-bold">KiotClinic</h5>
                        <p>Giải pháp quản lý bán hàng toàn diện, hiện đại và hiệu quả.</p>
                    </div>
                    <div class="col-md-3 mb-3">
                        <h6 class="fw-bold">Liên hệ</h6>
                        <ul class="list-unstyled">
                            <li>Email: support@kiotclinic.vn</li>
                        </ul>
                    </div>
                    <div class="col-md-3 mb-3">
                        <h6 class="fw-bold">Liên kết</h6>
                        <ul class="list-unstyled">
                            <li><a href="about.jsp" class="text-white text-decoration-none">Về chúng tôi</a></li>
                        </ul>
                    </div>
                </div>
                <hr class="border-light">
                <div class="text-center">
                    &copy; 2025 KiotClinic. All rights reserved.
                </div>
            </div>
        </footer>
    </body>
</html>

