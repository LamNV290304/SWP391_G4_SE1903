<%-- 
    Document   : UpdateProduct
    Created on : May 26, 2025, 9:22:58 PM
    Author     : Admin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <form action="ProductURL" method="POST">
            <input type="hidden" name="service" value="Update">
            <c:forEach var="i" items="${vector}">
                <table border="1">

                <tbody>
                    <tr>
                        <td>ProductID</td>
                        <td><input type="text" name="ProductID" id="" value="${i.productID}"></td>
                    </tr>
                    <tr>
                        <td>ProductName</td>
                        <td><input type="text" name="ProductName" id="" value="${i.productName}"></td>
                    </tr>
                    <tr>
                        <td>CategoryID</td>
                        <td><input type="text" name="CategoryID" id="" value="${i.categoryID}"></td>
                    </tr>
                    <tr>
                        <td>UnitID</td>
                        <td><input type="text" name="UnitID" id="" value="${i.unitID}"></td>
                    </tr>
                    <tr>
                        <td>Price</td>
                        <td><input type="text" name="Price" id="" value="${i.price}"></td>
                    </tr>
                    <tr>
                        <td>Description</td>
                        <td><input type="text" name="Description" id="" value="${i.description}"></td>
                    </tr>
                    <tr>
                        <td>Status</td>
                        <td><input type="radio" name="Status" value="1" id="" ${i.status == true ? "checked" :""}>Còn hàng
                        <input type="radio" name="Status" value="0" ${i.status == false ? "checked" :""} id="">Hết hàng</td>

                    </tr>
                    <tr>
                        <td>CreatedDate</td>
                        <td><input type="text" name="CreatedDate" id="" value="${i.createdDate}"></td>
                    </tr>
                    <tr>
                        <td>CreatedBy</td>
                        <td><input type="text" name="CreatedBy" id="" value="${i.createdBy}"></td>
                    </tr>
                    <tr>
                    <td><input type="submit" value="Update"
                               name="submit"></td>
                    <td><input type="reset" value="Clear"></td>
                </tr>
                </tbody>
            </table>
            </c:forEach>

        </form>
    </body>
</html>
