<%-- 
    Document   : ListWorkSchedule
    Created on : Jun 27, 2025, 3:20:31 PM
    Author     : ADMIN
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html
    lang="en"
    class="light-style layout-menu-fixed"
    dir="ltr"
    data-theme="theme-default"
    data-assets-path="../assets/"
    data-template="vertical-menu-template-free"
    >
    <head>
        <jsp:include page="LinkCSS.jsp" />
    </head>
    <body>
        <div class="layout-wrapper layout-content-navbar">
            <div class="layout-container">
                <!--menu-->
                <jsp:include page="menu.jsp" />
                <div class="layout-page">
                    <jsp:include page="navbar.jsp" />
                    <div class="content-wrapper">
                        <div class="container-xxl flex-grow-1 container-p-y">
                            <h4 class="fw-bold py-3 mb-4"><span class="text-muted fw-light"></span> Work Schedule</h4>

                            <!-- Bordered Table -->
                            <div class="card">
                                <h5 class="card-header">Work Schedule</h5>
                                <div class="card-body">
                                    <div class="table-responsive text-nowrap">
                                        <table class="table table-bordered">
                                            <thead>
                                                <tr>
                                                    <th>Nhân viên</th>
                                                    <th>Shop</th>
                                                    <th>Ca Làm việc</th>
                                                    <th>Ngày</th>
                                                    <th>Status</th>
                                                    <th>Note</th>
                                                    <th>Action</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <c:forEach var="w" items="${data}">
                                                    <tr>
                                                        <c:forEach var="e" items="${ListEmployee}">
                                                            <c:if test="${w.employeeID == e.id}">
                                                                <td>
                                                                    <i class="fab fa-angular fa-lg text-danger me-3"></i> <strong>${e.fullname}</strong>
                                                                </td>
                                                            </c:if>
                                                        </c:forEach>
                                                        <c:forEach var="s" items="${ListShop}">
                                                            <c:if test="${w.shopID == s.shopID}">
                                                                <td>${s.shopName}</td>
                                                            </c:if>
                                                        </c:forEach>
                                                        <c:forEach var="s" items="${ListShift}">
                                                            <c:if test="${w.shiftID == s.shiftID}">
                                                                <td>${s.shiftName}</td>
                                                            </c:if>
                                                        </c:forEach>
                                                        <td>${w.workDate}</td>
                                                        <td><span class="badge bg-label-primary me-1">${w.status} Active</span></td>
                                                        <td>${w.note}</td>
                                                        <td>
                                                            <div class="dropdown">
                                                                <button
                                                                    type="button"
                                                                    class="btn p-0 dropdown-toggle hide-arrow"
                                                                    data-bs-toggle="dropdown"
                                                                    >
                                                                    <i class="bx bx-dots-vertical-rounded"></i>
                                                                </button>
                                                                <div class="dropdown-menu">
                                                                    <a class="dropdown-item" href="javascript:void(0);"
                                                                       ><i class="bx bx-edit-alt me-1"></i> Edit</a
                                                                    >
                                                                    <a class="dropdown-item" href="javascript:void(0);"
                                                                       ><i class="bx bx-trash me-1"></i> Delete</a
                                                                    >
                                                                </div>
                                                            </div>
                                                        </td>
                                                    </tr>
                                                </c:forEach>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            </div>
                            <!--/ Bordered Table -->
                        </div>
                    </div>

                </div>

            </div>

        </div>
        <jsp:include page="LinkJS.jsp" />
    </body>
</html>
