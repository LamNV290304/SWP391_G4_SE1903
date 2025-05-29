<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Danh sách sản phẩm</title>
        <style>
            body {
                font-family: Arial, sans-serif;
                background-color: #f0f8ff;
                margin: 20px;
                color: #333;
            }

            h2 {
                color: #1e3a8a;
            }

            form {
                margin-bottom: 20px;
            }

            input[type="submit"] {
                background-color: #2563eb;
                color: white;
                border: none;
                padding: 8px 16px;
                border-radius: 4px;
                cursor: pointer;
                transition: background-color 0.3s;
            }

            input[type="submit"]:hover {
                background-color: #1d4ed8;
            }

            table {
                width: 100%;
                border-collapse: collapse;
                background-color: white;
                box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            }

            th, td {
                padding: 10px;
                text-align: left;
                border-bottom: 1px solid #ddd;
            }

            th {
                background-color: #3b82f6;
                color: white;
            }

            tr:hover {
                background-color: #e0f2fe;
            }

            td input[type="submit"] {
                padding: 5px 10px;
                font-size: 14px;
            }
        </style>
    </head>
    <body>
        <h2>Danh sách sản phẩm</h2>
        <form action="ProductURL" method="post">
            <input type="submit" value="Insert" name="service" />
        </form>

        <table>
            <thead>
                <tr>
                    <th>ProductID</th>
                    <th>ProductName</th>
                    <th>CategoryID</th>
                    <th>UnitID</th>
                    <th>Price</th>
                    <th>Description</th>
                    <th>Status</th>
                    <th>CreatedDate</th>
                    <th>CreatedBy</th>
                    <th>Update</th>
                    <th>Delete</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="p" items="${vector}">
                    <form action="ProductURL" method="post">
                        <tr>
                            <td>${p.productID}</td>
                            <td>${p.productName}</td>
                            <td>${p.categoryID}</td>
                            <td>${p.unitID}</td>
                            <td>${p.price}</td>
                            <td>${p.description}</td>
                            <td>${p.status == true ? "Còn hàng" : "Hết hàng"}</td>
                            <td>${p.createdDate}</td>
                            <td>${p.createdBy}</td>
                            <input type="hidden" name="pId" value="${p.productID}" />
                            <td><input type="submit" name="service" value="Update" /></td>
                            <td><input type="submit" name="service" value="Delete" /></td>
                        </tr>
                    </form>
                </c:forEach>
            </tbody>
        </table>
    </body>
</html>
