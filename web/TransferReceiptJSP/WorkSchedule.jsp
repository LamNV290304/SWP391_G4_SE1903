<%-- 
    Document   : WorkSchedule
    Created on : Jun 27, 2025, 10:54:45 PM
    Author     : ADMIN
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:useBean id="now" class="java.util.Date" scope="page" />
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
        <style>
            .work-cell {
                position: relative;
                cursor: pointer;
                min-height: 60px;
                padding: 8px;
                vertical-align: top;
            }

            /* Style cho cell trống */
            .work-cell.empty::after {
                content: '+ Thêm ca';
                position: absolute;
                top: 50%;
                left: 50%;
                transform: translate(-50%, -50%);
                font-size: 0.9rem;
                color: #5f6368;
                opacity: 0;
                pointer-events: none;
                transition: 0.2s;
                background: #f8f9fa;
                padding: 4px 8px;
                border-radius: 4px;
                border: 1px dashed #dee2e6;
            }

            .work-cell.empty:hover::after {
                opacity: 1;
            }

            /* Style cho shift badge */
            .shift-badge {
                display: block;
                background: #e3f2fd;
                color: #1976d2;
                padding: 6px 8px;
                border-radius: 4px;
                font-size: 0.85rem;
                margin-bottom: 6px;
                position: relative;
                transition: all 0.2s;
                width: 100%;
                box-sizing: border-box;
                cursor: pointer;
            }

            .shift-badge:hover {
                background: #bbdefb;
                transform: translateY(-1px);
            }

            /* Style cho nút xóa shift */
            .shift-badge .remove-shift {
                display: none;
                position: absolute;
                top: -5px;
                right: -5px;
                background: #f44336;
                color: white;
                border-radius: 50%;
                width: 18px;
                height: 18px;
                font-size: 12px;
                line-height: 18px;
                text-align: center;
                cursor: pointer;
                z-index: 10;
            }

            .shift-badge:hover .remove-shift {
                display: block;
            }

            /* Style cho cell có shift */
            .work-cell.has-shift {
                background: #fafafa;
            }

            .work-cell.has-shift .add-shift-btn {
                display: block;
                background: #e8f4fd;
                color: #666;
                opacity: 0;
                transition: 0.2s;
                padding: 6px 8px;
                border-radius: 4px;
                font-size: 0.85rem;
                margin-bottom: 6px;
                border: 1px dashed #90caf9;
                cursor: pointer;
                text-align: center;
                width: 100%;
                box-sizing: border-box;
            }

            .work-cell.has-shift:hover .add-shift-btn {
                opacity: 1;
            }

            .work-cell.has-shift .add-shift-btn:hover {
                background: #bbdefb;
                color: #1976d2;
            }

            /* Container cho các shift */
            .shifts-container {
                display: flex;
                flex-direction: column;
                gap: 6px;
            }

            /* Style cho cột lương */
            .salary-cell {
                font-weight: bold;
                color: #28a745;
                text-align: center;
                vertical-align: middle;
                background-color: #f8f9fa;
                font-size: 0.9rem;
            }

            /* ===== STYLE CHO NGÀY ĐÃ QUA - READONLY ===== */
            .work-cell.past-date {
                background-color: #f8f9fa !important;
                opacity: 0.6;
                cursor: not-allowed !important;
                pointer-events: none;
            }

            .work-cell.past-date::after {
                display: none !important;
            }

            .work-cell.past-date .shift-badge {
                background: #e9ecef !important;
                color: #6c757d !important;
                cursor: default !important;
                pointer-events: none;
            }

            .work-cell.past-date .shift-badge:hover {
                background: #e9ecef !important;
                transform: none !important;
            }

            .work-cell.past-date .shift-badge .remove-shift {
                display: none !important;
            }

            .work-cell.past-date .add-shift-btn {
                display: none !important;
            }

            /* Header cho ngày đã qua */
            .past-date-header {
                background-color: #f8f9fa !important;
                color: #6c757d !important;
                position: relative;
            }

            .past-date-header::after {
                content: '(Đã qua)';
                font-size: 0.75rem;
                font-weight: normal;
                opacity: 0.7;
                margin-left: 5px;
            }
        </style>
        <!-- CSS bổ sung cho nút toggle -->
        <style>
            .btn-group .btn.active {
                pointer-events: none;
                cursor: default;
            }

            .btn-group .btn {
                border-radius: 0;
            }

            .btn-group .btn:first-child {
                border-top-left-radius: 0.375rem;
                border-bottom-left-radius: 0.375rem;
            }

            .btn-group .btn:last-child {
                border-top-right-radius: 0.375rem;
                border-bottom-right-radius: 0.375rem;
            }

            .btn-group .btn + .btn {
                border-left: 0;
            }

            .btn-group .btn-outline-primary + .btn-primary,
            .btn-group .btn-primary + .btn-outline-primary {
                border-left: 1px solid #0d6efd;
            }
        </style>
    </head>
    <body>
        <div class="layout-wrapper layout-content-navbar">
            <div class="layout-container">
                <!--menu-->
                <jsp:include page="../sidebar.jsp" />
                <div class="layout-page">
                    <jsp:include page="../navBar.jsp" />
                    <div class="content-wrapper">
                        <div class="container-xxl flex-grow-1 container-p-y">
                            <div class="d-flex justify-content-between align-items-center flex-wrap mb-3">
                                <!-- Bên trái: Tiêu đề + ô tìm kiếm -->
                                <div class="d-flex align-items-center gap-3 flex-wrap">
                                    <!-- Tiêu đề -->
                                    <h4 class="fw-bold mb-0">Work Schedule</h4>
                                    <form action="WorkSchedule" method="POST" class="d-flex align-items-center mb-2 mb-md-0">
                                        <!-- Thanh tìm kiếm nhỏ gọn -->
                                        <div class="input-group" style="width: 250px;">
                                            <span class="input-group-text"><i class="bx bx-search"></i></span>
                                            <input
                                                type="text"
                                                class="form-control"
                                                placeholder="Search..."
                                                name="search"
                                                value="${param.search != null ? param.search : ''}" />
                                        </div>
                                        <input type="hidden" name="submit" value="workSchedule">
                                        <input type="hidden" name="service" value="workSchedule">
                                    </form>
                                </div>
                                <!-- Nút chuyển đổi view -->
                                <div class="btn-group" role="group" aria-label="View toggle">
                                    <!-- Đang ở Shift view, hiển thị nút chuyển về Employee view -->
                                    <span  class="btn btn-sm btn-primary active">
                                        <i class="bx bx-time"></i> Employee View
                                    </span>
                                    <a href="WorkSchedule?service=workSchedule&view=shift" class="btn btn-sm btn-outline-primary">
                                        <i class="bx bx-user"></i> Shift View
                                    </a>


                                </div>
                                <!-- Bên phải: Điều hướng tuần -->
                                <div class="d-flex align-items-center gap-2 flex-nowrap">

                                    <!-- Nút tuần trước -->
                                    <a href="WorkSchedule?service=navigateWeek&direction=-1" class="btn btn-sm btn-outline-primary">
                                        <i class="bx bx-chevron-left"></i>
                                    </a>

                                    <!-- Thông tin tuần -->
                                    <div class="text-center">
                                        <div class="fw-semibold text-primary small">${weekInfo.weekText}</div>
                                        <div class="text-muted small">${weekInfo.weekRange}</div>
                                    </div>

                                    <!-- Nút tuần sau -->
                                    <a href="WorkSchedule?service=navigateWeek&direction=1" class="btn btn-sm btn-outline-primary">
                                        <i class="bx bx-chevron-right"></i>
                                    </a>

                                    <!-- Nút về tuần hiện tại -->
                                    <a href="WorkSchedule?service=goToCurrentWeek" class="btn btn-sm btn-primary">
                                        Tuần này
                                    </a>
                                </div>
                            </div>



                            <!-- Bordered Table -->
                            <div class="card">
                                <h5 class="card-header">Work Schedule</h5>
                                <div class="card-body">
                                    <div class="table-responsive text-nowrap">
                                        <table class="table table-bordered">
                                            <thead>
                                                <tr>
                                                    <th>Nhân viên</th>
                                                        <c:forEach var="day" items="${weekDays}">
                                                        <!-- Kiểm tra xem ngày có phải là quá khứ không -->
                                                        <c:set var="dayDate" value="${day.key}" />
                                                        <fmt:formatDate value="${now}" pattern="yyyy-MM-dd" var="todayString" />

                                                        <c:choose>
                                                            <c:when test="${dayDate < todayString}">
                                                                <th class="past-date-header" >${day.value}<br></th>
                                                                </c:when>
                                                                <c:otherwise>
                                                                <th>${day.value}</th>
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </c:forEach>
                                                    <th>Lương tuần</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <c:forEach var="e" items="${ListEmployee}">
                                                    <tr>
                                                        <td>
                                                            <i class="fab fa-angular fa-lg text-danger me-3"></i> <strong>${e.fullname}</strong>
                                                        </td>
                                                        <c:forEach var="day" items="${weekDays}">
                                                            <c:set var="hasShift" value="false" />
                                                            <c:set var="dayDate" value="${day.key}" />
                                                            <fmt:formatDate value="${now}" pattern="yyyy-MM-dd" var="todayString" />
                                                            <c:set var="isPastDate" value="${dayDate < todayString}" />

                                                            <c:choose>
                                                                <c:when test="${isPastDate}">
                                                                    <!-- Cell cho ngày đã qua - readonly -->
                                                                    <td class="work-cell past-date" data-employee="${e.id}" data-day="${day.key}">
                                                                        <c:forEach var="w" items="${data}">
                                                                            <c:if test="${w.workDate == day.key && w.employeeID == e.id}">
                                                                                <c:set var="hasShift" value="true" />
                                                                                <c:forEach var="s" items="${ListShift}">
                                                                                    <c:if test="${w.shiftID == s.shiftID}">
                                                                                        <span class="shift-badge" title="${s.description}">
                                                                                            ${s.shiftName}
                                                                                        </span>
                                                                                    </c:if>
                                                                                </c:forEach>
                                                                            </c:if>
                                                                        </c:forEach>
                                                                    </td>
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <!-- Cell cho ngày hiện tại và tương lai - có thể chỉnh sửa -->
                                                                    <td class="work-cell" data-employee="${e.id}" data-day="${day.key}" onclick="openAddShiftModal(this)">
                                                                        <c:forEach var="w" items="${data}">
                                                                            <c:if test="${w.workDate == day.key && w.employeeID == e.id}">
                                                                                <c:set var="hasShift" value="true" />
                                                                                <c:forEach var="s" items="${ListShift}">
                                                                                    <c:if test="${w.shiftID == s.shiftID}">
                                                                                        <span class="shift-badge" 
                                                                                              title="${s.description}"
                                                                                              data-work-schedule-id="${w.workScheduleID}"
                                                                                              data-shift-id="${s.shiftID}"
                                                                                              onclick="editShift(event, this)">
                                                                                            ${s.shiftName}
                                                                                            <span class="remove-shift" onclick="removeShift(event, ${w.workScheduleID})" title="Xóa ca">×</span>

                                                                                        </span>
                                                                                    </c:if>
                                                                                </c:forEach>
                                                                            </c:if>
                                                                        </c:forEach>
                                                                        <c:if test="${hasShift}">
                                                                            <div class="add-shift-btn" onclick="openAddShiftModal(this.parentElement)">+ Thêm ca</div>
                                                                            <script>
                                                                                document.currentScript.parentElement.classList.add('has-shift');
                                                                            </script>
                                                                        </c:if>
                                                                        <c:if test="${!hasShift}">
                                                                            <script>
                                                                                document.currentScript.parentElement.classList.add('empty');
                                                                            </script>
                                                                        </c:if>
                                                                    </td>
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </c:forEach>
                                                        <td class="salary-cell">
                                                            <c:choose>
                                                                <c:when test="${not empty employeeSalaries[e.id]}">
                                                                    ${employeeSalaries[e.id]}
                                                                </c:when>
                                                                <c:otherwise>
                                                                    0 ₫
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </td>
                                                    </tr>
                                                </c:forEach>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            </div>
                            <!--/ Bordered Table -->


                            <!-- Hiển thị thông báo -->
                            <c:if test="${not empty message}">
                                <div class="alert alert-${messageType == 'success' ? 'success' : (messageType == 'error' ? 'danger' : 'warning')} alert-dismissible fade show" role="alert">
                                    <strong>${messageType == 'success' ? 'Thành công!' : (messageType == 'error' ? 'Lỗi!' : 'Cảnh báo!')}</strong> ${message}
                                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                                </div>
                            </c:if>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Modal Thêm/Sửa Ca Làm -->
        <div class="modal fade" id="addShiftModal" tabindex="-1" aria-labelledby="addShiftLabel" aria-hidden="true">
            <div class="modal-dialog modal-lg">
                <form id="shiftForm" method="post" action="WorkSchedule">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title" id="addShiftLabel">Thêm Ca Làm</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                        </div>
                        <div class="modal-body">
                            <input type="hidden" name="employeeId" id="employeeId">
                            <input type="hidden" name="dayOfWeek" id="dayOfWeek">
                            <input type="hidden" name="workScheduleId" id="workScheduleId">

                            <div class="mb-3">
                                <label for="shiftTime" class="form-label">Chọn ca làm việc:</label>
                                <select name="ShiftID" id="shiftSelect" class="form-select">
                                    <c:forEach var="s" items="${ListShift}">
                                        <option value="${s.shiftID}">${s.shiftName}: ${s.description}</option>
                                    </c:forEach>
                                </select>
                            </div>

                            <!-- Phần Lặp lại hằng tuần -->
                            <div class="mb-3">
                                <div class="d-flex justify-content-between align-items-center mb-2">
                                    <label class="form-label mb-0">Lặp lại hằng tuần</label>
                                    <div class="form-check form-switch">
                                        <input class="form-check-input" type="checkbox" id="repeatWeekly" onchange="toggleRepeatSection()">
                                    </div>
                                </div>
                                <small class="text-muted">Lịch làm việc sẽ được tự động lặp lại vào các ngày trong tuần</small>
                            </div>

                            <!-- Phần chọn thứ -->
                            <div id="repeatSection" style="display: none;">
                                <div class="mb-3">
                                    <label class="form-label">Chọn các thứ:</label>
                                    <div class="d-flex flex-wrap gap-2 mb-2">
                                        <c:forEach var="day" items="${weekDays}" varStatus="status">
                                            <!-- Kiểm tra ngày đã qua để disable checkbox -->
                                            <c:set var="dayDate" value="${day.key}" />
                                            <fmt:formatDate value="${now}" pattern="yyyy-MM-dd" var="todayString" />
                                            <c:set var="isPastDate" value="${dayDate < todayString}" />

                                            <input type="checkbox" class="btn-check" id="day${status.index}" 
                                                   name="selectedDays" value="${day.key}" autocomplete="off"
                                                   ${isPastDate ? 'disabled' : ''}>
                                            <label class="btn ${isPastDate ? 'btn-outline-secondary disabled' : 'btn-outline-primary'} btn-sm" 
                                                   for="day${status.index}" ${isPastDate ? 'style="opacity: 0.5; pointer-events: none;"' : ''}>
                                                ${day.value}
                                                <c:if test="${isPastDate}">
                                                    <small class="d-block" style="font-size: 0.7rem;">(Đã qua)</small>
                                                </c:if>
                                            </label>
                                        </c:forEach>
                                    </div>
                                    <div class="d-flex gap-2">
                                        <button type="button" class="btn btn-outline-secondary btn-sm" onclick="selectAllFutureDays()">Chọn ngày tương lai</button>
                                        <button type="button" class="btn btn-outline-secondary btn-sm" onclick="clearAllDays()">Bỏ chọn</button>
                                    </div>
                                    <small class="text-muted mt-1 d-block" id="selectedDaysText">Lặp lại các ngày trong tuần</small>
                                </div>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-outline-danger" id="deleteBtn" style="display: none;" onclick="confirmDelete()">Xóa lịch</button>
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Bỏ qua</button>
                            <button type="submit" name="service" value="addWorkSchedule" id="submitBtn" class="btn btn-success">Xong</button>
                        </div>
                    </div>
                </form>
            </div>
        </div>



        <script>

            function openAddShiftModal(cell) {
                // Kiểm tra xem cell có phải là ngày đã qua không
                if (cell.classList.contains('past-date')) {
                    return; // Không làm gì nếu là ngày đã qua
                }

                const employeeId = cell.getAttribute('data-employee');
                const day = cell.getAttribute('data-day');

                // Reset form về trạng thái thêm mới
                resetModalForm();
                document.getElementById('employeeId').value = employeeId;
                document.getElementById('dayOfWeek').value = day;
                document.getElementById('addShiftLabel').textContent = 'Thêm lịch làm việc';
                document.getElementById('submitBtn').textContent = 'Thêm';
                document.querySelector('button[name="service"]').value = 'addWorkSchedule';


                // Hiển thị modal Bootstrap
                var myModal = new bootstrap.Modal(document.getElementById('addShiftModal'));
                myModal.show();
            }

            function editShift(event, shiftElement) {
                event.stopPropagation(); // Ngăn không cho trigger openAddShiftModal của cell

                const cell = shiftElement.closest('.work-cell');

                // Kiểm tra xem cell có phải là ngày đã qua không
                if (cell.classList.contains('past-date')) {
                    return; // Không cho phép chỉnh sửa ngày đã qua
                }

                const employeeId = cell.getAttribute('data-employee');
                const day = cell.getAttribute('data-day');
                const workScheduleId = shiftElement.getAttribute('data-work-schedule-id');
                const currentShiftId = shiftElement.getAttribute('data-shift-id');

                // Reset và cập nhật form với thông tin hiện tại
                resetModalForm();
                document.getElementById('employeeId').value = employeeId;
                document.getElementById('dayOfWeek').value = day;
                document.getElementById('workScheduleId').value = workScheduleId;

                // Chọn ca hiện tại trong dropdown
                const shiftSelect = document.getElementById('shiftSelect');
                shiftSelect.value = currentShiftId;

                // Cập nhật tiêu đề và nút
                document.getElementById('addShiftLabel').textContent = 'Cập nhật lịch làm việc';
                document.getElementById('submitBtn').textContent = 'Cập nhật';
                document.getElementById('deleteBtn').style.display = 'inline-block';
                document.querySelector('button[name="service"]').value = 'updateWorkSchedule';


                // Hiển thị modal Bootstrap
                var myModal = new bootstrap.Modal(document.getElementById('addShiftModal'));
                myModal.show();
            }



            function formatDate(date) {
                const year = date.getFullYear();
                const month = String(date.getMonth() + 1).padStart(2, '0');
                const day = String(date.getDate()).padStart(2, '0');
                return `${year}-${month}-${day}`;
                    }

                    function resetModalForm() {
                        // Reset tất cả form fields
                        document.getElementById('workScheduleId').value = '';
                        document.getElementById('shiftSelect').selectedIndex = 0;
                        document.getElementById('repeatWeekly').checked = false;
                        document.getElementById('deleteBtn').style.display = 'none';

                        // Reset checkbox days
                        document.querySelectorAll('input[name="selectedDays"]').forEach(checkbox => {
                            checkbox.checked = false;
                        });

                        // Hide repeat section
                        document.getElementById('repeatSection').style.display = 'none';
                        updateSelectedDaysText();
                    }



                    function toggleRepeatSection() {
                        const repeatCheckbox = document.getElementById('repeatWeekly');
                        const repeatSection = document.getElementById('repeatSection');

                        if (repeatCheckbox.checked) {
                            repeatSection.style.display = 'block';
                            // Tự động chọn ngày hiện tại nếu chưa có ngày nào được chọn
                            const currentDay = document.getElementById('dayOfWeek').value;
                            const selectedDays = document.querySelectorAll('input[name="selectedDays"]:checked');

                        } else {
                            repeatSection.style.display = 'none';
                            // Clear all selected days
                            clearAllDays();
                        }
                    }

                    function selectAllFutureDays() {
                        // Chỉ chọn các ngày tương lai (không bị disabled)
                        document.querySelectorAll('input[name="selectedDays"]:not(:disabled)').forEach(checkbox => {
                            checkbox.checked = true;
                        });
                        updateSelectedDaysText();
                    }

                    function clearAllDays() {
                        document.querySelectorAll('input[name="selectedDays"]').forEach(checkbox => {
                            checkbox.checked = false;
                        });
                        updateSelectedDaysText();
                    }

                    function updateSelectedDaysText() {
                        const selectedCheckboxes = document.querySelectorAll('input[name="selectedDays"]:checked');
                        const selectedDaysText = document.getElementById('selectedDaysText');

                        if (selectedCheckboxes.length === 0) {
                            selectedDaysText.textContent = 'Chưa chọn ngày nào';
                        } else {
                            selectedDaysText.textContent = 'Đã chọn lặp lại';
                        }
                    }



                    function confirmDelete() {

                        if (confirm('Bạn có chắc chắn muốn xóa lịch làm việc này?')) {
                            const workScheduleId = document.getElementById('workScheduleId').value;
                            window.location.href = 'WorkSchedule?service=removeWorkSchedule&workScheduleId=' + workScheduleId;
                        }

                    }



                    function removeShift(event, workScheduleId) {
                        event.stopPropagation(); // Ngăn không cho trigger openAddShiftModal

                        if (confirm('Bạn có chắc chắn muốn xóa ca làm này?')) {
                            // Gửi request xóa ca làm
                            window.location.href = 'WorkSchedule?service=removeWorkSchedule&workScheduleId=' + workScheduleId;
                        }
                    }

                    // Event listeners
                    document.addEventListener('DOMContentLoaded', function () {
                        // Ngăn event bubbling cho nút thêm ca trong cell có shift
                        document.querySelectorAll('.add-shift-btn').forEach(btn => {
                            btn.addEventListener('click', function (e) {
                                e.stopPropagation();
                            });
                        });

                        // Update selected days text khi thay đổi checkbox
                        document.querySelectorAll('input[name="selectedDays"]').forEach(checkbox => {
                            checkbox.addEventListener('change', updateSelectedDaysText);
                        });
                    });
        </script>

        <jsp:include page="LinkJS.jsp" />
    </body>
</html>