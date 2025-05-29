<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Cập nhật sản phẩm</title>
        <style>
            body {
                font-family: Arial, sans-serif;
                background-color: #f0f8ff;
                padding: 20px;
            }

            h2 {
                color: #1e3a8a;
                margin-bottom: 20px;
            }

            form {
                background-color: #ffffff;
                padding: 20px;
                border-radius: 8px;
                box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
                max-width: 600px;
                margin: auto;
            }

            table {
                width: 100%;
                border-collapse: collapse;
            }

            td {
                padding: 10px;
                vertical-align: top;
            }

            td:first-child {
                font-weight: bold;
                color: #1e3a8a;
                width: 30%;
            }

            input[type="text"],
            input[type="number"] {
                width: 100%;
                padding: 8px;
                border: 1px solid #ccc;
                border-radius: 4px;
            }

            input[type="radio"] {
                margin-right: 5px;
            }

            input[type="submit"],
            input[type="reset"] {
                background-color: #2563eb;
                color: white;
                border: none;
                padding: 10px 20px;
                margin-top: 10px;
                border-radius: 4px;
                cursor: pointer;
                transition: background-color 0.3s;
            }

            input[type="submit"]:hover,
            input[type="reset"]:hover {
                background-color: #1d4ed8;
            }
        </style>
    </head>
    <body>
        <h2>Cập nhật sản phẩm</h2>
        <form action="ProductURL" method="POST">
            <input type="hidden" name="service" value="Update">
            <c:forEach var="i" items="${vector}">
                <table>
                    <tr>
                        <td>ProductID</td>
                        <td><input type="text" name="ProductID" value="${i.productID}" readonly></td>
                    </tr>
                    <tr>
                        <td>ProductName</td>
                        <td><input type="text" name="ProductName" value="${i.productName}"></td>
                    </tr>
                    <tr>
                        <td>CategoryID</td>
                        <td><input type="text" name="CategoryID" value="${i.categoryID}"></td>
                    </tr>
                    <tr>
                        <td>UnitID</td>
                        <td><input type="text" name="UnitID" value="${i.unitID}"></td>
                    </tr>
                    <tr>
                        <td>Price</td>
                        <td><input type="text" name="Price" value="${i.price}"></td>
                    </tr>
                    <tr>
                        <td>Description</td>
                        <td><input type="text" name="Description" value="${i.description}"></td>
                    </tr>
                    <tr>
                        <td>Status</td>
                        <td>
                            <label><input type="radio" name="Status" value="1" ${i.status == true ? "checked" :""}> Còn hàng</label>
                            <label><input type="radio" name="Status" value="0" ${i.status == false ? "checked" :""}> Hết hàng</label>
                        </td>
                    </tr>
                    <tr>
                        <td>CreatedDate</td>
                        <td><input type="text" name="CreatedDate" value="${i.createdDate}"></td>
                    </tr>
                    <tr>
                        <td>CreatedBy</td>
                        <td><input type="text" name="CreatedBy" value="${i.createdBy}"></td>
                    </tr>
                    <tr>
                        <td colspan="2" style="text-align: center;">
                            <input type="submit" value="Update" name="submit">
                            <input type="reset" value="Clear">
                        </td>
                    </tr>
                </table>
            </c:forEach>
        </form>
    </body>
</html>
