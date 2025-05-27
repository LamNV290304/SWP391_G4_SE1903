<%-- 
    Document   : Product
    Created on : May 26, 2025, 6:25:20 PM
    Author     : Admin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Danh sách sản phẩm</title>
        <script>
function sendPostRequest() {
    var form = document.createElement("form");
    form.method = "POST";
    form.action = "ProductURL"; // Đổi thành URL của Servlet của bạn

    var input = document.createElement("input");
    input.type = "hidden";
    input.name = "service";
    input.value = "Update";

    form.appendChild(input);
    document.body.appendChild(form);
    form.submit();
}
</script>
    </head>
    <body>
        <h2>Danh sách sản phẩm</h2>
        <form action="ProductURL" method="Post">
            <input type="submit" value="Insert" name="service" />
        </form>
        
            <table border="1">
                <tbody>
                    <tr>
                        <td>ProductID</td>
                        <td>ProductName</td>
                        <td>CategoryID</td>
                        <td>UnitID</td>
                        <td>Price</td>
                        <td>Description</td>
                        <td>Status</td>
                        <td>CreatedDate</td>
                        <td>CreatedBy</td>
                        <td>Update</td>
                        <td>Delete</td>
                    </tr>
                    <c:forEach var="p" items="${vector}">
                        <form action="ProductURL" method="post">
                        <tr>
                            <td>${p.productID}</td>
                            <td>${p.productName}</td>
                            <td>${p.categoryID}</td>
                            <td>${p.unitID}</td>
                            <td>${p.price}</td>
                            <td>${p.description}</td>
                            <td>${p.status == true ? "Còn hàng":"Hết hàng" }</td>
                            <td>${p.createdDate}</td>
                            <td>${p.createdBy}</td>
                        <input type="hidden" name="pId" value="${p.productID}">
                        
                            <td><input type="submit" name="service" value="Update"></td>
                            <td><input type="submit" name="service" value="Delete"></td>

                        </tr>
                        </form>
                    </c:forEach>
                </tbody>
            </table>


        
    </body>
</html>
