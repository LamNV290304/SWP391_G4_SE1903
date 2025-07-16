<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html lang="vi"
      class="light-style layout-menu-fixed"
      dir="ltr"
      data-theme="theme-default"
      data-assets-path="../assets/"
      data-template="vertical-menu-template-free">

    <head>
        <jsp:include page="LinkCSS.jsp"/>
        <title>Danh sách bảng lương</title>
        <style>
            .employee-list {
                max-height: 300px;
                overflow-y: auto;
                border: 1px solid #dee2e6;
                border-radius: 0.375rem;
                background: white;
            }
            .employee-item {
                display: flex;
                align-items: center;
                padding: 12px 16px;
                border-bottom: 1px solid #f1f3f4;
                cursor: pointer;
                transition: background-color 0.2s;
            }
            .employee-item:hover {
                background-color: #f8f9fa;
            }
            .employee-item:last-child {
                border-bottom: none;
            }
            .employee-info {
                flex: 1;
            }
            .employee-name {
                font-weight: 500;
                color: #333;
                margin-bottom: 2px;
            }
            .employee-code {
                font-size: 12px;
                color: #6c757d;
            }
            .employee-checkbox {
                margin-left: 12px;
            }
            .search-container {
                position: relative;
                margin-bottom: 16px;
            }
            .search-input {
                padding-left: 40px;
            }
            .search-icon {
                position: absolute;
                left: 12px;
                top: 50%;
                transform: translateY(-50%);
                color: #6c757d;
            }
            .employee-table-view {
                border: 1px solid #dee2e6;
                border-radius: 0.375rem;
                overflow: hidden;
            }
            .employee-table-view .table {
                margin-bottom: 0;
            }
            .employee-table-view .table th {
                background-color: #f8f9fa;
                border-bottom: 1px solid #dee2e6;
                padding: 12px;
                font-weight: 500;
                color: #495057;
            }
            .employee-table-view .table td {
                padding: 12px;
                vertical-align: middle;
                border-bottom: 1px solid #f1f3f4;
            }
            .employee-table-view .table tbody tr:hover {
                background-color: #f8f9fa;
            }
            .view-toggle {
                display: flex;
                gap: 8px;
                margin-bottom: 16px;
            }
            .view-toggle .btn {
                padding: 6px 12px;
                font-size: 12px;
            }
        </style>
    </head>
    <body>
        <div class="layout-wrapper layout-content-navbar">
            <div class="layout-container">
                <jsp:include page="../sidebar.jsp" />
                <div class="layout-page">
                    <jsp:include page="../navBar.jsp" />
                    <div class="content-wrapper">
                        <div class="container-xxl flex-grow-1 container-p-y">
                            <h4 class="fw-bold py-3 mb-4">
                                <span class="text-muted fw-light">Bảng lương /</span> Danh sách
                            </h4>

                            <c:if test="${not empty sessionScope.message}">
                                <div class="alert alert-${sessionScope.messageType} alert-dismissible" role="alert">
                                    <i class="bx bx-check-circle me-2"></i>
                                    ${sessionScope.message}
                                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                                </div>
                                <c:remove var="message" scope="session"/>
                                <c:remove var="messageType" scope="session"/>
                            </c:if>

                            <div class="card">
                                <div class="card-header d-flex justify-content-between align-items-center">
                                    <div>
                                        <h5 class="mb-1">Danh sách bảng lương</h5>
                                        <small class="text-muted">Quản lý và theo dõi bảng lương nhân viên</small>
                                    </div>
                                    <button class="btn btn-primary btn-sm" data-bs-toggle="modal" data-bs-target="#createSalaryModal">
                                        <i class="bx bx-plus me-1"></i> Thêm mới
                                    </button>
                                </div>
                                <div class="table-responsive text-nowrap">
                                    <table class="table table-hover">
                                        <thead class="table-light">
                                            <tr>
                                                <th></th>
                                                <th>Mã</th>
                                                <th>Tên bảng lương</th>
                                                <th>Kỳ hạn</th>
                                                <th>Kỳ làm việc</th>
                                                <th>Tổng lương</th>
                                                <th>Trạng thái</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:choose>
                                                <c:when test="${empty salaryList}">
                                                    <tr><td colspan="7" class="text-center py-4">Không có dữ liệu</td></tr>
                                                </c:when>
                                                <c:otherwise>
                                                    <c:forEach var="salary" items="${salaryList}" varStatus="loop">
                                                        <tr class="salary-row" onclick="toggleDetail(this, ${salary.salaryID})">
                                                            <td><i class="bx bx-chevron-down toggle-icon" id="icon-${salary.salaryID}"></i></td>
                                                            <td><span class="fw-bold text-primary">${salary.salaryID}</span></td>
                                                            <td>${salary.salaryName}<br><small class="text-muted">Bởi ${salary.createdBy}</small></td>
                                                            <td><span class="badge bg-label-info">${salary.salaryPeriod}</span></td>
                                                            <td>
                                                                <fmt:formatDate value="${salary.workPeriodStart}" pattern="dd/MM/yyyy"/> -
                                                                <fmt:formatDate value="${salary.workPeriodEnd}" pattern="dd/MM/yyyy"/><br>
                                                                <small class="text-muted"><fmt:formatDate value="${salary.createdDate}" pattern="dd/MM/yyyy"/></small>
                                                            </td>
                                                            <td><strong class="text-success">
                                                                    <fmt:formatNumber value="${salary.totalSalary}" type="currency" currencySymbol=""/>₫
                                                                </strong></td>
                                                            <td>
                                                                <c:choose>
                                                                    <c:when test="${salary.status eq 'Đã trả'}">
                                                                        <span class="badge bg-success"><i class="bx bx-check"></i> ${salary.status}</span>
                                                                    </c:when>
                                                                    <c:when test="${salary.status eq 'Đã duyệt'}">
                                                                        <span class="badge bg-info"><i class="bx bx-check-double"></i> ${salary.status}</span>
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <span class="badge bg-label-primary"><i class="bx bx-time"></i> ${salary.status}</span>
                                                                    </c:otherwise>
                                                                </c:choose>
                                                            </td>
                                                        </tr>
                                                        <tr class="salary-detail-row" id="detail-${salary.salaryID}" style="display: none;">
                                                            <td colspan="7">
                                                                <div class="p-3 bg-light rounded border">
                                                                    <div class="d-flex justify-content-between align-items-center">
                                                                        <div>
                                                                            <strong>Mã:</strong> BL${salary.salaryID} |
                                                                            <strong>Ngày tạo:</strong> <fmt:formatDate value="${salary.createdDate}" pattern="dd/MM/yyyy HH:mm"/> |
                                                                            <strong>Người tạo:</strong> ${salary.createdBy}
                                                                        </div>
                                                                        <div class="d-flex gap-2">
                                                                            <a href="SalaryController?service=detail&id=${salary.salaryID}" class="btn btn-sm btn-info text-white" onclick="event.stopPropagation(); showLoading();">
                                                                                <i class="bx bx-show"></i> Chi tiết
                                                                            </a>
                                                                            <c:if test="${salary.status != 'Đã trả'}">
                                                                                <button class="btn btn-sm btn-danger" onclick="event.stopPropagation(); deleteSalary(${salary.salaryID}, '${salary.salaryName}')">
                                                                                    <i class="bx bx-trash"></i> Xóa
                                                                                </button>
                                                                            </c:if>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                            </td>
                                                        </tr>
                                                    </c:forEach>
                                                </c:otherwise>
                                            </c:choose>
                                        </tbody>
                                    </table>
                                </div>
                            </div>

                            <!-- Modal thêm bảng lương -->
                            <div class="modal fade" id="createSalaryModal" tabindex="-1" aria-labelledby="createSalaryModalLabel" aria-hidden="true">
                                <div class="modal-dialog modal-xl">
                                    <form method="post" action="SalaryController" onsubmit="return validateForm()">
                                        <input type="hidden" name="service" value="create" />
                                        <div class="modal-content">
                                            <div class="modal-header">
                                                <h5 class="modal-title"><i class="bx bx-plus-circle me-2"></i> Thêm bảng tính lương</h5>
                                                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                            </div>
                                            <div class="modal-body">
                                                <div class="row g-3">
                                                    <div class="row-md-6">
                                                        <label class="form-label">Kỳ hạn trả lương *</label>
                                                        <select name="salaryPeriod" class="form-select" required>
                                                            <option value="Hàng tháng" selected>Hàng tháng</option>
                                                            <option value="Hàng tuần">Hàng tuần</option>
                                                        </select>
                                                    </div>
                                                    <div class="row-md-6">
                                                        <label class="form-label">Kỳ làm việc *</label>
                                                        <div class="dropdown" id="monthlyPeriodDropdown">
                                                            <input type="text" class="form-control dropdown-toggle" id="periodSelectMonth" value="${listMonth[4]}"
                                                                   data-bs-toggle="dropdown" aria-expanded="false" 
                                                                   placeholder="Chọn kỳ làm việc" readonly required />
                                                            <ul class="dropdown-menu w-100" aria-labelledby="periodSelectMonth">
                                                                <c:forEach var="month" items="${listMonth}" varStatus="loop">
                                                                    <li>
                                                                        <a class="dropdown-item month-option ${loop.index == (listMonth.size()/2) ? 'active' : ''}" 
                                                                           href="#" data-period="${month}">
                                                                            ${month}
                                                                        </a>
                                                                    </li>
                                                                </c:forEach>
                                                            </ul>
                                                        </div>

                                                        <div class="dropdown" id="weekPeriodDropdown" style="display: none;">
                                                            <input type="text" class="form-control dropdown-toggle" id="periodSelectWeek" value="${listWeeks[4]}"
                                                                   data-bs-toggle="dropdown" aria-expanded="false" 
                                                                   placeholder="Chọn kỳ làm việc" readonly required />
                                                            <ul class="dropdown-menu w-100" aria-labelledby="periodSelectWeek">
                                                                <c:forEach var="week" items="${listWeeks}" varStatus="check">
                                                                    <li>
                                                                        <a class="dropdown-item week-option ${check.index == (listWeeks.size()/2) ? 'active' : ''}" 
                                                                           href="#" data-period="${week}">
                                                                            ${week}
                                                                        </a>
                                                                    </li>
                                                                </c:forEach>
                                                            </ul>
                                                        </div>

                                                        <!-- Hidden field để gửi về controller -->
                                                        <input type="hidden" name="selectedPeriod" id="selectedPeriod" value="${listMonth[4]}" />
                                                    </div>

                                                    <div class="col-md-12">
                                                        <label class="form-label">Phạm vi áp dụng</label>
                                                        <div>
                                                            <div class="form-check form-check-inline">
                                                                <input class="form-check-input" type="radio" name="applyScope" id="scopeAll" value="all" checked>
                                                                <label class="form-check-label" for="scopeAll">Tất cả nhân viên</label>
                                                            </div>
                                                            <div class="form-check form-check-inline">
                                                                <input class="form-check-input" type="radio" name="applyScope" id="scopeCustom" value="custom">
                                                                <label class="form-check-label" for="scopeCustom">Tùy chọn</label>
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <div class="col-md-12" id="employeeSelectArea" style="display:none;">
                                                        <div class="search-container">
                                                            <i class="bx bx-search search-icon"></i>
                                                            <input type="text" class="form-control search-input" placeholder="Tìm theo mã, tên nhân viên" onkeyup="filterEmployee(this.value)"/>
                                                        </div>



                                                        <!-- Table View -->
                                                        <div class="employee-table-view" id="employeeTableView" >
                                                            <div class="table-responsive" style="max-height: 300px; overflow-y: auto;">
                                                                <table class="table">
                                                                    <thead>
                                                                        <tr>
                                                                            <th width="50">
                                                                                <input type="checkbox" id="selectAllEmployees" onchange="toggleAllEmployees(this)">
                                                                            </th>
                                                                            <th>Mã nhân viên</th>
                                                                            <th>Tên nhân viên</th>
                                                                        </tr>
                                                                    </thead>
                                                                    <tbody id="employeeTableBody">
                                                                        <c:forEach var="emp" items="${vectorE}">
                                                                            <tr data-id="${emp.id}" data-name="${emp.fullname}">
                                                                                <td><input type="checkbox" name="selectedEmployees" class="employee-checkbox" value="${emp.id}"/></td>
                                                                                <td>${emp.id}</td>
                                                                                <td>${emp.fullname}</td>
                                                                            </tr>
                                                                        </c:forEach>
                                                                    </tbody>
                                                                </table>
                                                            </div>
                                                        </div>

                                                        <div class="text-center text-muted mt-3" id="noEmployeeMsg" style="display: none;">
                                                            <i class="bx bx-user-x"></i> Không tìm thấy nhân viên phù hợp
                                                        </div>

                                                        <div class="mt-2">
                                                            <small class="text-muted">
                                                                <span id="selectedCount">0</span> nhân viên được chọn
                                                            </small>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="modal-footer">
                                                <button type="button" class="btn btn-outline-secondary" data-bs-dismiss="modal">
                                                    <i class="bx bx-x"></i> Bỏ qua
                                                </button>
                                                <button type="submit" class="btn btn-success">
                                                    <i class="bx bx-check"></i> Tạo bảng lương
                                                </button>
                                            </div>
                                        </div>
                                    </form>
                                </div>
                            </div>
                        </div>

                    </div>
                </div>
            </div>
        </div>
        <jsp:include page="LinkJS.jsp"/>
        <script>
            let currentView = 'list';
            const selectedIDs = new Set();

            document.getElementById('createSalaryModal').addEventListener('show.bs.modal', function () {
                // Reset form
                const form = this.querySelector('form');
                form.reset();

                // Reset các dropdown
                const salaryPeriod = form.querySelector('[name="salaryPeriod"]');
                salaryPeriod.value = 'Hàng tháng';

                document.getElementById('monthlyPeriodDropdown').style.display = 'block';
                document.getElementById('weekPeriodDropdown').style.display = 'none';
                document.getElementById('periodSelectMonth').value = '${listMonth[4]}';
                document.getElementById('selectedPeriod').value = '${listMonth[4]}';

                // Reset checkbox chọn nhân viên
                selectedIDs.clear();
                document.querySelectorAll('.employee-checkbox').forEach(cb => cb.checked = false);
                document.getElementById('selectAllEmployees').checked = false;

                // Reset số nhân viên đã chọn
                updateSelectedCount();

                // Reset phạm vi áp dụng
                document.getElementById('scopeAll').checked = true;
                document.getElementById('employeeSelectArea').style.display = 'none';

                // Reset ô tìm kiếm nhân viên (nếu có)
                const searchInput = form.querySelector('.search-input');
                if (searchInput)
                    searchInput.value = '';
                filterEmployee('');
            });

            // Xử lý thay đổi phạm vi áp dụng
            document.querySelectorAll('input[name="applyScope"]').forEach(el => {
                el.addEventListener('change', function () {
                    const show = document.getElementById('scopeCustom').checked;
                    document.getElementById('employeeSelectArea').style.display = show ? 'block' : 'none';
                    updateSelectedCount();
                });
            });

            // Xử lý thay đổi kỳ hạn trả lương
            document.querySelector('[name="salaryPeriod"]').addEventListener('change', function () {
                const isMonthly = this.value === 'Hàng tháng';
                const monthDropdown = document.getElementById('monthlyPeriodDropdown');
                const weekDropdown = document.getElementById('weekPeriodDropdown');
                const selectedPeriod = document.getElementById('selectedPeriod');

                if (isMonthly) {
                    monthDropdown.style.display = 'block';
                    weekDropdown.style.display = 'none';
                    document.getElementById('periodSelectMonth').value = '${listMonth[4]}';
                    selectedPeriod.value = '${listMonth[4]}';
                } else {
                    monthDropdown.style.display = 'none';
                    weekDropdown.style.display = 'block';
                    document.getElementById('periodSelectWeek').value = '${listWeeks[4]}';
                    selectedPeriod.value = '${listWeeks[4]}';
                }
            });

            // Xử lý chọn kỳ làm việc từ dropdown
            document.querySelectorAll('.month-option, .week-option').forEach(item => {
                item.addEventListener('click', function (e) {
                    e.preventDefault();
                    const period = this.getAttribute('data-period');
                    const isMonth = this.classList.contains('month-option');

                    if (isMonth) {
                        document.getElementById('periodSelectMonth').value = period;
                    } else {
                        document.getElementById('periodSelectWeek').value = period;
                    }
                    document.getElementById('selectedPeriod').value = period;

                    // Update active state
                    const siblings = isMonth ? document.querySelectorAll('.month-option') : document.querySelectorAll('.week-option');
                    siblings.forEach(i => i.classList.remove('active'));
                    this.classList.add('active');
                });
            });

            // Khởi tạo giá trị mặc định
            document.addEventListener('DOMContentLoaded', function () {
                document.getElementById('selectedPeriod').value = '${listMonth[4]}';

                // Thiết lập event listener cho checkbox
                document.querySelectorAll('.employee-checkbox').forEach(cb => {
                    cb.addEventListener('change', function () {
                        if (this.checked) {
                            selectedIDs.add(this.value);
                        } else {
                            selectedIDs.delete(this.value);
                        }
                        updateSelectedCount();
                    });
                });
            });



            function filterEmployee(keyword) {
                keyword = keyword.toLowerCase();
                let hasMatch = false;


                const rows = document.querySelectorAll('#employeeTableBody tr');
                rows.forEach(row => {
                    const id = row.getAttribute('data-id').toLowerCase();
                    const name = row.getAttribute('data-name').toLowerCase();
                    const show = id.includes(keyword) || name.includes(keyword);
                    row.style.display = show ? '' : 'none';
                    if (show)
                        hasMatch = true;
                });


                document.getElementById('noEmployeeMsg').style.display = hasMatch ? 'none' : 'block';
                restoreCheckboxStates();
            }

            function restoreCheckboxStates() {
                document.querySelectorAll('.employee-checkbox').forEach(cb => {
                    cb.checked = selectedIDs.has(cb.value);
                });
                updateSelectedCount();
            }

            function toggleAllEmployees(checkbox) {
                const checkboxes = document.querySelectorAll('.employee-checkbox');
                checkboxes.forEach(cb => {
                    const isVisible = currentView === 'list'
                            ? cb.closest('.employee-item').style.display !== 'none'
                            : cb.closest('tr').style.display !== 'none';

                    if (isVisible) {
                        cb.checked = checkbox.checked;
                        if (checkbox.checked) {
                            selectedIDs.add(cb.value);
                        } else {
                            selectedIDs.delete(cb.value);
                        }
                    }
                });
                updateSelectedCount();
            }

            function updateSelectedCount() {
                const count = selectedIDs.size;
                const countElement = document.getElementById('selectedCount');
                if (countElement) {
                    countElement.textContent = count;
                }
            }

            function toggleDetail(row, id) {
                const detailRow = document.getElementById('detail-' + id);
                const icon = document.getElementById('icon-' + id);
                const isVisible = detailRow.style.display === 'table-row';

                // Ẩn tất cả trước
                document.querySelectorAll('.salary-detail-row').forEach(r => r.style.display = 'none');
                document.querySelectorAll('.toggle-icon').forEach(i => i.className = 'bx bx-chevron-down toggle-icon');

                // Nếu chưa hiển thị → hiển thị
                if (!isVisible) {
                    detailRow.style.display = 'table-row';
                    icon.className = 'bx bx-chevron-up toggle-icon';
                }
            }
            function showLoading() {
                const div = document.createElement('div');
                div.className = 'loading-overlay';
                div.innerHTML = '<div class="spinner-border text-primary"></div><p>Đang tải...</p>';
                Object.assign(div.style, {
                    position: 'fixed', top: 0, left: 0, right: 0, bottom: 0,
                    background: 'rgba(0,0,0,0.5)', display: 'flex', alignItems: 'center',
                    justifyContent: 'center', zIndex: 9999, flexDirection: 'column',
                    color: 'white'
                });
                document.body.appendChild(div);
            }

            function deleteSalary(id, name) {
                if (confirm(`Xóa bảng lương "${name}"?`)) {
                    showLoading();
                    fetch('SalaryController?service=delete', {
                        method: 'POST',
                        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                        body: 'id=' + encodeURIComponent(id)
                    })
                            .then(res => res.json())
                            .then(data => {
                                location.reload();
                            })
                            .catch(() => alert('Xóa thất bại'));
                }
            }

            function validateForm() {
                const applyScope = document.querySelector('input[name="applyScope"]:checked').value;
                const selectedPeriod = document.getElementById('selectedPeriod').value;

                if (!selectedPeriod || selectedPeriod.trim() === '') {
                    alert('Vui lòng chọn kỳ làm việc.');
                    return false;
                }

                if (applyScope === 'custom' && selectedIDs.size === 0) {
                    alert('Vui lòng chọn ít nhất một nhân viên.');
                    return false;
                }

                // Hiển thị loading
                showLoading();
                return true;
            }
        </script>
    </body>
</html>