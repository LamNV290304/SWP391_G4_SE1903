<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html lang="vi" class="light-style layout-menu-fixed" dir="ltr" data-theme="theme-default" data-assets-path="../assets/" data-template="vertical-menu-template-free">
    <head>
        <jsp:include page="LinkCSS.jsp" />
        <title>Chi tiết bảng lương</title>
    </head>
    <body>
        <div class="layout-wrapper layout-content-navbar">
            <div class="layout-container">
                <jsp:include page="../sidebar.jsp" />
                <div class="layout-page">
                    <jsp:include page="../navBar.jsp" />
                    <div class="content-wrapper">
                        <div class="container-xxl flex-grow-1 container-p-y">
                            <div class="d-flex justify-content-between align-items-center mb-4">
                                <h4 class="fw-bold">Chi tiết bảng lương: ${salary.salaryName}</h4>
                                <a href="SalaryController?service=list" class="btn btn-outline-primary">
                                    <i class="bx bx-arrow-back"></i> Quay lại danh sách
                                </a>
                            </div>

                            <!-- Hiển thị thông báo -->
                            <c:if test="${not empty sessionScope.message}">
                                <div class="alert alert-${sessionScope.messageType} alert-dismissible" role="alert">
                                    <i class="bx bx-check-circle me-2"></i>
                                    ${sessionScope.message}
                                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                                </div>
                                <c:remove var="message" scope="session"/>
                                <c:remove var="messageType" scope="session"/>
                            </c:if>

                            <!-- Thông tin bảng lương -->
                            <div class="row mb-4">
                                <div class="col-md-6">
                                    <div class="card h-100">
                                        <div class="card-body">
                                            <h5 class="card-title"><i class="bx bx-id-card me-2"></i>Thông tin cơ bản</h5>
                                            <p><strong>Mã:</strong> BL${salary.salaryID}</p>
                                            <p><strong>Tên:</strong> ${salary.salaryName}</p>
                                            <p><strong>Kỳ hạn trả:</strong> ${salary.salaryPeriod}</p>
                                            <p><strong>Kỳ làm việc:</strong>
                                                <fmt:formatDate value="${salary.workPeriodStart}" pattern="dd/MM/yyyy" /> -
                                                <fmt:formatDate value="${salary.workPeriodEnd}" pattern="dd/MM/yyyy" />
                                            </p>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <div class="card h-100">
                                        <div class="card-body">
                                            <h5 class="card-title"><i class="bx bx-bar-chart me-2"></i>Thống kê</h5>
                                            <p><strong>Ngày tạo:</strong> <fmt:formatDate value="${salary.createdDate}" pattern="dd/MM/yyyy HH:mm:ss" /></p>
                                            <p><strong>Người lập bảng:</strong> ${salary.createdBy}</p>
                                            <p><strong>Trạng thái:</strong>
                                                <c:choose>
                                                    <c:when test="${salary.status == 'Đã trả'}">
                                                        <span class="badge bg-success">${salary.status}</span>
                                                    </c:when>
                                                    <c:when test="${salary.status == 'Đã duyệt'}">
                                                        <span class="badge bg-info">${salary.status}</span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="badge bg-warning text-dark">${salary.status}</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </p>
                                            <p><strong>Tổng số nhân viên:</strong> ${fn:length(salaryDetails)}</p>
                                            <p><strong>Tổng lương:</strong>
                                                <span class="text-success fw-bold">
                                                    <fmt:formatNumber value="${salary.totalSalary}" type="currency" currencySymbol="" />₫
                                                </span>
                                            </p>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <!-- Bảng chi tiết nhân viên -->
                            <div class="card">
                                <div class="card-header d-flex justify-content-between align-items-center">
                                    <h5 class="mb-0"><i class="bx bx-group me-2"></i>Chi tiết lương nhân viên</h5>
                                    <span class="badge bg-label-primary">${fn:length(salaryDetails)} nhân viên</span>
                                </div>
                                <div class="table-responsive">
                                    <table class="table table-hover">
                                        <thead class="table-light">
                                            <tr>
                                                <th>STT</th>
                                                <th>Tên nhân viên</th>
                                                <th>Đơn giá</th>
                                                <th>Tổng lương</th>
                                                    <c:if test="${salary.status != 'Đã trả'}">
                                                    <th>Thao tác</th>
                                                    </c:if>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:choose>
                                                <c:when test="${empty salaryDetails}">
                                                    <tr>
                                                        <td colspan="${salary.status != 'Đã trả' ? '6' : '5'}" class="text-center py-4">
                                                            <i class="bx bx-info-circle me-2"></i>Không có dữ liệu chi tiết
                                                        </td>
                                                    </tr>
                                                </c:when>
                                                <c:otherwise>
                                                    <c:forEach var="detail" items="${salaryDetails}" varStatus="loop">
                                                        <tr>
                                                            <td>${loop.index + 1}</td>

                                                            <!-- Tên nhân viên -->
                                                            <td>
                                                                <c:forEach var="e" items="${vectorE}">
                                                                    <c:if test="${e.id == detail.employeeID}">
                                                                        ${e.fullname}
                                                                    </c:if>
                                                                </c:forEach>
                                                            </td>

                                                            <c:forEach var="s" items="${vectorSS}">
                                                                <c:if test="${s.employeeID == detail.employeeID}">
                                                                    <!-- Loại lương -->
                                                                    <td>
                                                                        <fmt:formatNumber value="${s.amount}" type="currency" currencySymbol="" pattern="#,#00"/>₫
                                                                        <c:choose>
                                                                            <c:when test="${s.salaryType == 'PerShift'}">
                                                                                <small class="text-muted">/ca</small>
                                                                            </c:when>
                                                                            <c:when test="${s.salaryType == 'PerHour'}">
                                                                                <small class="text-muted">/giờ</small>
                                                                            </c:when>
                                                                            <c:when test="${s.salaryType == 'FixedMonthly'}">
                                                                                <small class="text-muted">/tháng</small>
                                                                            </c:when>
                                                                        </c:choose>
                                                                    </td>
                                                                </c:if>
                                                            </c:forEach>

                                                            <!-- Tổng lương -->
                                                            <td class="text-success fw-bold">
                                                                <fmt:formatNumber value="${detail.basicSalary}" type="currency" currencySymbol="" />₫
                                                            </td>

                                                            <!-- Thao tác xóa (chỉ hiển thị nếu chưa trả lương) -->
                                                            <c:if test="${salary.status != 'Đã trả'}">
                                                                <td>
                                                                    <button class="btn btn-sm btn-danger" 
                                                                            onclick="deleteSalaryDetail(${detail.salaryDetailID}, ${salary.salaryID}, '${detail.employeeID}')">
                                                                        <i class="bx bx-trash"></i> Xóa
                                                                    </button>
                                                                </td>
                                                            </c:if>
                                                        </tr>
                                                    </c:forEach>
                                                </c:otherwise>
                                            </c:choose>
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
            function showLoading() {
                const div = document.createElement('div');
                div.className = 'loading-overlay';
                div.innerHTML = '<div class="spinner-border text-primary"></div><p>Đang xử lý...</p>';
                Object.assign(div.style, {
                    position: 'fixed', top: 0, left: 0, right: 0, bottom: 0,
                    background: 'rgba(0,0,0,0.5)', display: 'flex', alignItems: 'center',
                    justifyContent: 'center', zIndex: 9999, flexDirection: 'column',
                    color: 'white'
                });
                document.body.appendChild(div);
            }

            function deleteSalaryDetail(detailId, salaryId, employeeId) {
                // Tìm tên nhân viên để hiển thị trong confirm dialog
                let employeeName = 'Nhân viên ID: ' + employeeId;
                const row = event.target.closest('tr');
                if (row) {
                    const nameCell = row.querySelector('td:nth-child(2)');
                    if (nameCell) {
                        employeeName = nameCell.textContent.trim();
                    }
                }

                if (confirm(`Xác nhận xóa chi tiết lương của "${employeeName}"?\n\nHành động này không thể hoàn tác.`)) {
                    showLoading();

                    fetch('SalaryController?service=deleteSalaryDetail', {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/x-www-form-urlencoded',
                        },
                        body: 'detailId=' + encodeURIComponent(detailId) +
                                '&salaryId=' + encodeURIComponent(salaryId)
                    })
                            .then(response => response.json())
                            .then(data => {
                                // Ẩn loading
                                const loadingDiv = document.querySelector('.loading-overlay');
                                if (loadingDiv) {
                                    loadingDiv.remove();
                                }

                                if (data.success) {
                                    // Reload trang để cập nhật dữ liệu
                                    location.reload();
                                } else {
                                    alert('Lỗi: ' + (data.message || 'Xóa không thành công'));
                                }
                            })
                            .catch(error => {
                                // Ẩn loading
                                const loadingDiv = document.querySelector('.loading-overlay');
                                if (loadingDiv) {
                                    loadingDiv.remove();
                                }

                                console.error('Error:', error);
                                alert('Có lỗi xảy ra khi xóa chi tiết lương');
                            });
                }
            }
        </script>
    </body>
</html>