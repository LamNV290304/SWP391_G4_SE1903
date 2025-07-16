<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html lang="vi" class="light-style layout-menu-fixed" dir="ltr" data-theme="theme-default" data-assets-path="../assets/" data-template="vertical-menu-template-free">

    <head>
        <jsp:include page="LinkCSS.jsp" />
        <title>Thiết Lập Lương</title>
    </head>
    <body>
        <div class="layout-wrapper layout-content-navbar">
            <div class="layout-container">
                <jsp:include page="menu.jsp" />
                <div class="layout-page">
                    <jsp:include page="navbar.jsp" />
                    <div class="content-wrapper">
                        <div class="container-xxl flex-grow-1 container-p-y">
                            <div class="d-flex justify-content-between align-items-center mb-4">
                                <div class="d-flex align-items-center gap-3 flex-wrap">
                                    <h4 class="fw-bold mb-0">Danh sách thiết lập lương</h4>

                                    <form action="SalarySetting" method="POST" class="d-flex align-items-center mb-2 mb-md-0">
                                         
                                        <div class="input-group" style="width: 250px;">
                                            <span class="input-group-text"><i class="bx bx-search"></i></span>
                                            <input
                                                type="text"
                                                class="form-control"
                                                placeholder="Search..."
                                                name="search"
                                                value="${param.search != null ? param.search : ''}" />
                                        </div>      
                                        <input type="hidden" name="service" value="search" />

                                    </form>
                                </div>
                                <div class="d-flex align-items-center gap-2 flex-nowrap">
                                    <button class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#addSalaryModal">
                                        <i class="bx bx-plus"></i> Thêm thiết lập
                                    </button>
                                </div>
                            </div>

                            <c:if test="${not empty message}">
                                <div class="alert alert-${messageType} alert-dismissible" role="alert">
                                    ${message}
                                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                                </div>
                            </c:if>

                            <div class="card">
                                <div class="table-responsive">
                                    <table class="table table-hover">
                                        <thead class="table-light">
                                            <tr>
                                                <th>STT</th>
                                                <th>Nhân viên</th>
                                                <th>Loại lương</th>
                                                <th>Mức lương</th>
                                                <th>Thao tác</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach var="setting" items="${salarySettings}" varStatus="status">
                                                <tr>
                                                    <td>${status.index + 1}</td>
                                                    <td>
                                                        <div class="d-flex align-items-center">
                                                            <div>
                                                                <div class="fw-bold">${setting.fullName}</div>
                                                                <small class="text-muted">ID: ${setting.employeeID}</small>
                                                            </div>
                                                        </div>
                                                    </td>
                                                    <td>
                                                        <c:choose>
                                                            <c:when test="${setting.salaryType == 'PerShift'}">
                                                                <span class="badge bg-label-primary">Theo ca</span>
                                                            </c:when>
                                                            <c:when test="${setting.salaryType == 'PerHour'}">
                                                                <span class="badge bg-label-success">Theo giờ</span>
                                                            </c:when>
                                                            <c:when test="${setting.salaryType == 'FixedMonthly'}">
                                                                <span class="badge bg-label-info">Tháng</span>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <span class="badge bg-label-warning">${setting.salaryType}</span>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </td>
                                                    <td>
                                                        <span class="text-success fw-bold">
                                                            <fmt:formatNumber value="${setting.amount}" type="currency" currencySymbol="" pattern="#,#00"/>₫
                                                        </span>
                                                        <c:choose>
                                                            <c:when test="${setting.salaryType == 'PerShift'}">
                                                                <small class="text-muted">/ca</small>
                                                            </c:when>
                                                            <c:when test="${setting.salaryType == 'PerHour'}">
                                                                <small class="text-muted">/giờ</small>
                                                            </c:when>
                                                            <c:when test="${setting.salaryType == 'FixedMonthly'}">
                                                                <small class="text-muted">/tháng</small>
                                                            </c:when>
                                                        </c:choose>
                                                    </td>
                                                    <td>
                                                        <div class="dropdown">
                                                            <button class="btn p-0 dropdown-toggle hide-arrow" data-bs-toggle="dropdown">
                                                                <i class="bx bx-dots-vertical-rounded"></i>
                                                            </button>
                                                            <div class="dropdown-menu">
                                                                <a class="dropdown-item" href="#" data-bs-toggle="modal" data-bs-target="#editSalaryModal"
                                                                   onclick="editSalarySetting(${setting.salarySettingID}, ${setting.employeeID}, '${setting.salaryType}', ${setting.amount})">
                                                                    <i class="bx bx-edit-alt me-1"></i> Chỉnh sửa
                                                                </a>
                                                                <a class="dropdown-item" href="#" onclick="deleteSalarySetting(${setting.salarySettingID}, '${setting.fullName}')">
                                                                    <i class="bx bx-trash me-1"></i> Xoá
                                                                </a>
                                                            </div>
                                                        </div>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                            <c:if test="${empty salarySettings}">
                                                <tr>
                                                    <td colspan="5" class="text-center py-4 text-muted">
                                                        <i class="bx bx-info-circle"></i> Không có thiết lập lương
                                                    </td>
                                                </tr>
                                            </c:if>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>


                    </div>
                </div>
            </div>
        </div>

        <jsp:include page="LinkJS.jsp" />
        <script>
            function editSalarySetting(id, empId, type, amount) {
                document.getElementById('editSalarySettingId').value = id;
                document.getElementById('editEmployeeId').value = empId;
                document.getElementById('editSalaryType').value = type;
                document.getElementById('editAmount').value = amount;
            }

            function deleteSalarySetting(id, name) {
                if (confirm(`Bạn có chắc chắn muốn xoá thiết lập cho "${name}"?`)) {
                    window.location.href = `SalarySetting?service=delete&salarySettingId=${id}`;
                }
            }
        </script>

        <!-- Modal thêm -->
        <div class="modal fade" id="addSalaryModal" tabindex="-1">
            <div class="modal-dialog">
                <form method="post" action="SalarySetting">
                    <input type="hidden" name="service" value="add">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title"><i class="bx bx-plus-circle"></i> Thêm thiết lập lương</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                        </div>
                        <div class="modal-body">
                            <div class="mb-3">
                                <label class="form-label">Nhân viên *</label>
                                <select class="form-select" name="employeeId" required>
                                    <option value="">-- Chọn nhân viên --</option>
                                    <c:forEach var="emp" items="${employees}">
                                        <option value="${emp.id}">${emp.fullName} (${emp.username})</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="mb-3">
                                <label class="form-label">Loại lương *</label>
                                <select class="form-select" name="salaryType" required>
                                    <option value="PerShift">Theo ca</option>
                                    <option value="PerHour">Theo giờ</option>
                                    <option value="FixedMonthly">Cố định tháng</option>
                                </select>
                            </div>
                            <div class="mb-3">
                                <label class="form-label">Mức lương (VNĐ) *</label>
                                <input type="number" class="form-control" name="amount" required min="0" step="1000">
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-outline-secondary" data-bs-dismiss="modal">Huỷ</button>
                            <button type="submit" class="btn btn-success">Lưu</button>
                        </div>
                    </div>
                </form>
            </div>
        </div>

        <!-- Modal sửa -->
        <div class="modal fade" id="editSalaryModal" tabindex="-1">
            <div class="modal-dialog">
                <form method="post" action="SalarySetting">
                    <input type="hidden" name="service" value="update">
                    <input type="hidden" name="salarySettingId" id="editSalarySettingId">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title"><i class="bx bx-edit"></i> Chỉnh sửa thiết lập lương</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                        </div>
                        <div class="modal-body">
                            <div class="mb-3">
                                <label class="form-label">Nhân viên *</label>
                                <select class="form-select" name="employeeId" id="editEmployeeId" required>
                                    <c:forEach var="emp" items="${employees}">
                                        <option value="${emp.id}">${emp.fullName} (${emp.username})</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="mb-3">
                                <label class="form-label">Loại lương *</label>
                                <select class="form-select" name="salaryType" id="editSalaryType" required>
                                    <option value="PerShift">Theo ca</option>
                                    <option value="PerHour">Theo giờ</option>
                                    <option value="FixedMonthly">Cố định tháng</option>
                                </select>
                            </div>
                            <div class="mb-3">
                                <label class="form-label">Mức lương (VNĐ) *</label>
                                <input type="number" class="form-control" name="amount" id="editAmount" required min="0" step="1000">
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-outline-secondary" data-bs-dismiss="modal">Huỷ</button>
                            <button type="submit" class="btn btn-success">Cập nhật</button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </body>
</html>
