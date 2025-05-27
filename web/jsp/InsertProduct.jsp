<%-- 
    Document   : InsertProduct
    Created on : May 26, 2025, 8:22:31 PM
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
            <input type="hidden" name="service" value="Insert">
            <table border="1">

                <tbody>
                    <tr>
                        <td>ProductID</td>
                        <td><input type="text" name="ProductID" id=""></td>
                    </tr>
                    <tr>
                        <td>ProductName</td>
                        <td><input type="text" name="ProductName" id=""></td>
                    </tr>
                    <tr>
                        <td>CategoryID</td>
                        <td><input type="text" name="CategoryID" id=""></td>
                    </tr>
                    <tr>
                        <td>UnitID</td>
                        <td><input type="text" name="UnitID" id=""></td>
                    </tr>
                    <tr>
                        <td>Price</td>
                        <td><input type="text" name="Price" id=""></td>
                    </tr>
                    <tr>
                        <td>Description</td>
                        <td><input type="text" name="Description" id=""></td>
                    </tr>
                    <tr>
                        <td>Status</td>
                        <td><input type="radio" name="Status" value="1" id="">Còn hàng
                        <input type="radio" name="Status" value="0" id="">Hết hàng</td>

                    </tr>
                    <tr>
                        <td>CreatedDate</td>
                        <td><input type="text" name="CreatedDate" id=""></td>
                    </tr>
                    <tr>
                        <td>CreatedBy</td>
                        <td><input type="text" name="CreatedBy" id=""></td>
                    </tr>
                    <tr>
                    <td><input type="submit" value="InsertProduct"
                               name="submit"></td>
                    <td><input type="reset" value="Clear"></td>
                </tr>
                </tbody>
            </table>

        </form>


    </body>
</html>
