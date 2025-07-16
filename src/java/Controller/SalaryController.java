/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import Context.DBContext;
import Dal.EmployeeDAO;
import Dal.SalaryDAO;
import Dal.SalaryDetailDAO;
import Dal.SalarySettingDAO;
import Dal.ShiftDAO;
import Dal.WorkScheduleDAO;
import Models.Employee;
import Models.Salary;
import Models.SalaryDetail;
import Models.SalarySetting;
import Models.Shift;
import Models.WorkSchedule;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.sql.Date;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

/**
 *
 * @author ADMIN
 */
@WebServlet(name = "SalaryController", urlPatterns = {"/SalaryController"})
public class SalaryController extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    DBContext connection = new DBContext("Test");
    SalaryDAO dao = new SalaryDAO(connection.getConnection());
    SalaryDetailDAO salaryDetailDAO = new SalaryDetailDAO(connection.getConnection());
    EmployeeDAO employeeDAO = new EmployeeDAO(connection.getConnection());
    SalarySettingDAO salarySettingDAO = new SalarySettingDAO(connection.getConnection());
    WorkScheduleDAO workScheduleDAO = new WorkScheduleDAO(connection.getConnection());
    ShiftDAO shiftDAO = new ShiftDAO(connection.getConnection());

    Vector<SalarySetting> listSalarySetting = salarySettingDAO.getAllSalarySetting("SELECT * FROM SalarySetting");

    List<Employee> employees = employeeDAO.getAllEmployee();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Set encoding để xử lý tiếng Việt
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String service = request.getParameter("service");
        if (service == null) {
            service = "list";
        }

        try {

            switch (service) {
                case "list":
                    listSalary(request, response);
                    break;

                case "create":
                    createSalary(request, response);
                    break;

                case "delete":
                    deleteSalary(request, response);
                    break;

                case "detail":
                    showDetail(request, response);
                    break;
                case "deleteSalaryDetail":
                    deleteSalaryDetail(request, response);
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi hệ thống: " + e.getMessage());
        }
    }

    private void deleteSalaryDetail(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        try {
            String detailIdStr = request.getParameter("detailId");
            String salaryIdStr = request.getParameter("salaryId");

            if (detailIdStr == null || detailIdStr.trim().isEmpty()) {
                out.print("{\"success\":false,\"message\":\"ID chi tiết lương không hợp lệ\"}");
                return;
            }

            if (salaryIdStr == null || salaryIdStr.trim().isEmpty()) {
                out.print("{\"success\":false,\"message\":\"ID bảng lương không hợp lệ\"}");
                return;
            }

            int detailId = Integer.parseInt(detailIdStr);
            int salaryId = Integer.parseInt(salaryIdStr);

            // Kiểm tra xem salary detail có tồn tại không
            SalaryDetail existingDetail = salaryDetailDAO.getSalaryDetailByID(detailId);
            if (existingDetail == null) {
                out.print("{\"success\":false,\"message\":\"Không tìm thấy chi tiết lương\"}");
                return;
            }

            // Kiểm tra xem bảng lương có thể chỉnh sửa không
            Salary salary = dao.searchSalary(salaryId);
            if (salary != null && "Đã trả".equals(salary.getStatus())) {
                out.print("{\"success\":false,\"message\":\"Không thể xóa chi tiết của bảng lương đã trả\"}");
                return;
            }

            // Xóa salary detail
            int result = salaryDetailDAO.deleteSalaryDetail(detailId);

            if (result > 0) {
                // Cập nhật lại tổng lương sau khi xóa
                updateTotalSalaryAfterDetailChange(salaryId);

                out.print("{\"success\":true,\"message\":\"Xóa chi tiết lương thành công\"}");
            } else {
                out.print("{\"success\":false,\"message\":\"Xóa không thành công\"}");
            }

        } catch (NumberFormatException e) {
            out.print("{\"success\":false,\"message\":\"ID phải là số nguyên hợp lệ\"}");
        } catch (Exception e) {
            e.printStackTrace();
            out.print("{\"success\":false,\"message\":\"Lỗi hệ thống: " + e.getMessage() + "\"}");
        } finally {
            out.flush();
        }
    }

// Thêm method helper để cập nhật tổng lương:
    private void updateTotalSalaryAfterDetailChange(int salaryId) {
        try {
            Vector<SalaryDetail> details = salaryDetailDAO.getSalaryDetailBySalaryID(salaryId);
            BigDecimal newTotal = BigDecimal.ZERO;

            for (SalaryDetail detail : details) {
                newTotal = newTotal.add(detail.getBasicSalary());
            }

            dao.updateTotalSalary(salaryId, newTotal);
        } catch (Exception e) {
            log("Error updating total salary after detail change: " + e.getMessage());
            e.printStackTrace();
        }
    }

//    Tính tổng giờ làm việc của nhân viên trong kỳ dựa trên WorkSchedule
    private BigDecimal getWorkHoursOfEmployeeInPeriod(int employeeId, Date startDate, Date endDate) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String startDateStr = sdf.format(startDate);
            String endDateStr = sdf.format(endDate);

            // Lấy tất cả WorkSchedule của nhân viên trong kỳ
            Vector<WorkSchedule> schedules = workScheduleDAO.getWorkScheduleByDateAndEmployee(
                    employeeId, startDateStr, endDateStr);

            double totalHours = 0.0;

            for (WorkSchedule schedule : schedules) {
                // Lấy thông tin ca làm việc
                Shift shift = shiftDAO.searchShift(schedule.getShiftID());
                if (shift != null) {
                    // Tính số giờ của ca làm việc
                    LocalTime startTime = shift.getStartTime();
                    LocalTime endTime = shift.getEndTime();

                    // Tính số giờ
                    long startSeconds = startTime.toSecondOfDay();
                    long endSeconds = endTime.toSecondOfDay();

                    // Xử lý ca đêm (khi endTime < startTime)
                    if (endSeconds < startSeconds) {
                        endSeconds += 24 * 3600; // Thêm 24 giờ
                    }

                    double shiftHours = (endSeconds - startSeconds) / 3600.0;
                    totalHours += shiftHours;
                }
            }

            return BigDecimal.valueOf(totalHours);

        } catch (Exception e) {
            e.printStackTrace();
            return BigDecimal.ZERO;
        }
    }

//    Tính số ca làm việc của nhân viên trong kỳ
    private int getWorkShiftsOfEmployeeInPeriod(int employeeId, Date startDate, Date endDate) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String startDateStr = sdf.format(startDate);
            String endDateStr = sdf.format(endDate);

            Vector<WorkSchedule> schedules = workScheduleDAO.getWorkScheduleByDateAndEmployee(
                    employeeId, startDateStr, endDateStr);

            return schedules.size();

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

//    Tính lương chi tiết của nhân viên dựa trên SalarySetting và WorkSchedule
    private BigDecimal calculateEmployeeSalary(int employeeId, Date startDate, Date endDate, String salaryPeriod) {
        try {
            SalarySetting salarySetting = salarySettingDAO.getSalarySettingByEmployee(employeeId);

            if (salarySetting == null) {
                log("No salary setting found for employee " + employeeId);
                return BigDecimal.ZERO;
            }

            String salaryType = salarySetting.getSalaryType();
            BigDecimal amount = salarySetting.getAmount();

            switch (salaryType) {
                case "PerShift":
                    // Tính theo số ca làm việc
                    int totalShifts = getWorkShiftsOfEmployeeInPeriod(employeeId, startDate, endDate);
                    return amount.multiply(BigDecimal.valueOf(totalShifts));

                case "PerHour":
                    // Tính theo số giờ làm việc
                    BigDecimal totalHours = getWorkHoursOfEmployeeInPeriod(employeeId, startDate, endDate);
                    return amount.multiply(totalHours);

                case "FixedMonthly":
                    // Xử lý lương cố định tháng - không phụ thuộc vào số ngày/giờ làm việc thực tế
                    if ("Hàng tuần".equals(salaryPeriod)) {
                        // Chia lương tháng cho 4 tuần (lương cố định theo tuần)
                        return amount.divide(BigDecimal.valueOf(4), 2, BigDecimal.ROUND_HALF_UP);
                    } else {
                        // Kỳ tháng: trả toàn bộ lương cố định tháng
                        // Không cần quan tâm đến số ngày thực tế vì đây là lương cố định
                        return amount;
                    }

                default:
                    log("Unknown salary type: " + salaryType + " for employee " + employeeId);
                    return BigDecimal.ZERO;
            }

        } catch (Exception e) {
            log("Error calculating salary for employee " + employeeId + ": " + e.getMessage());
            e.printStackTrace();
            return BigDecimal.ZERO;
        }
    }

//    Tạo mô tả chi tiết cho cách tính lương
    private String generateSalaryDescription(int employeeId, Date startDate, Date endDate, String salaryPeriod) {
        try {
            SalarySetting salarySetting = salarySettingDAO.getSalarySettingByEmployee(employeeId);

            if (salarySetting == null) {
                return "Chưa thiết lập lương";
            }

            String salaryType = salarySetting.getSalaryType();
            BigDecimal amount = salarySetting.getAmount();
            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

            switch (salaryType) {
                case "PerShift":
                    int totalShifts = getWorkShiftsOfEmployeeInPeriod(employeeId, startDate, endDate);
                    return String.format("%d ca × %s", totalShifts,
                            currencyFormat.format(amount) + "/ca");

                case "PerHour":
                    BigDecimal totalHours = getWorkHoursOfEmployeeInPeriod(employeeId, startDate, endDate);
                    return String.format("%.1f giờ × %s", totalHours.doubleValue(),
                            currencyFormat.format(amount) + "/giờ");

                case "FixedMonthly":
                    if ("Hàng tuần".equals(salaryPeriod)) {
                        return String.format("Lương cố định tháng %s ÷ 4 tuần",
                                currencyFormat.format(amount));
                    } else {
                        return String.format("Lương cố định tháng: %s (toàn bộ)",
                                currencyFormat.format(amount));
                    }

                default:
                    return "Loại lương không xác định";
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "Lỗi tính toán";
        }
    }

    public static List<String> getSurroundingDateRanges(int totalMonths, int type) {
        List<String> DateRanges = new ArrayList<>();
        LocalDate now = LocalDate.now();
        int half = totalMonths / 2;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        if (type == 0) {
            for (int i = -half; i <= half; i++) {
                LocalDate month = now.plusMonths(i);
                LocalDate start = month.with(TemporalAdjusters.firstDayOfMonth());
                LocalDate end = month.with(TemporalAdjusters.lastDayOfMonth());
                String range = start.format(formatter) + " - " + end.format(formatter);
                DateRanges.add(range);
            }
        } else {
            for (int i = -half; i <= half; i++) {
                LocalDate weekRef = now.plusWeeks(i);

                // Tuần bắt đầu từ thứ Hai và kết thúc Chủ Nhật
                LocalDate startOfWeek = weekRef.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
                LocalDate endOfWeek = weekRef.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

                String range = startOfWeek.format(formatter) + " - " + endOfWeek.format(formatter);
                DateRanges.add(range);
            }
        }

        return DateRanges;
    }

    private void listSalary(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<String> ListMonth = getSurroundingDateRanges(9, 0);
            List<String> ListWeeks = getSurroundingDateRanges(9, 1);

            Vector<Salary> list = dao.getAllSalary("SELECT * FROM Salary ORDER BY SalaryID DESC");

            request.setAttribute("listWeeks", ListWeeks);
            request.setAttribute("listMonth", ListMonth);
            request.setAttribute("vectorE", employees);
            request.setAttribute("salaryList", list);
            request.getRequestDispatcher("TransferReceiptJSP/ListSalary.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("message", "Lỗi khi tải danh sách bảng lương: " + e.getMessage());
            request.setAttribute("messageType", "error");
            request.getRequestDispatcher("TransferReceiptJSP/ListSalary.jsp").forward(request, response);
        }
    }

    private void createSalary(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        try {
            // Lấy thông tin từ form
            String salaryPeriod = request.getParameter("salaryPeriod");
            String selectedPeriod = request.getParameter("selectedPeriod");
            String applyScope = request.getParameter("applyScope");
            String[] selectedEmployeeIds = request.getParameterValues("selectedEmployees");

            // Validation
            if (salaryPeriod == null || selectedPeriod == null || selectedPeriod.trim().isEmpty()) {
                throw new Exception("Thiếu thông tin đầu vào");
            }

            // Parse selectedPeriod
            String[] dateRange = selectedPeriod.split(" - ");
            if (dateRange.length != 2) {
                throw new Exception("Định dạng kỳ làm việc không hợp lệ");
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate startDate = LocalDate.parse(dateRange[0].trim(), formatter);
            LocalDate endDate = LocalDate.parse(dateRange[1].trim(), formatter);

            Date workStart = Date.valueOf(startDate);
            Date workEnd = Date.valueOf(endDate);

            if (workStart.after(workEnd)) {
                throw new Exception("Ngày bắt đầu không thể sau ngày kết thúc");
            }

            // Tạo Salary object
            String salaryName = generateSalaryName(salaryPeriod, workStart, workEnd);
            HttpSession session = request.getSession();
            String createdBy = (session.getAttribute("username") != null)
                    ? session.getAttribute("username").toString()
                    : "admin";

            Salary salary = new Salary();
            salary.setSalaryName(salaryName);
            salary.setSalaryPeriod(salaryPeriod);
            salary.setWorkPeriodStart(workStart);
            salary.setWorkPeriodEnd(workEnd);
            salary.setStatus("Tạm tính");
            salary.setCreatedBy(createdBy);
            salary.setTotalSalary(BigDecimal.ZERO);

            // Insert và lấy ID
            int salaryID = dao.insertSalaryAndReturnID(salary);
            if (salaryID <= 0) {
                throw new Exception("Không thể tạo bảng lương");
            }

            // Chuẩn bị danh sách nhân viên (loại trừ admin ngay từ đầu)
            List<Employee> employeesToProcess = new ArrayList<>();
            if ("all".equalsIgnoreCase(applyScope)) {
                List<Employee> allEmployees = employeeDAO.getAllEmployee();
                for (Employee emp : allEmployees) {
                    // Loại trừ admin (ID = 1) ngay từ đầu
                    if (emp.getId() != 1) {
                        employeesToProcess.add(emp);
                    }
                }
            } else if ("custom".equalsIgnoreCase(applyScope) && selectedEmployeeIds != null && selectedEmployeeIds.length > 0) {
                for (String idStr : selectedEmployeeIds) {
                    try {
                        int empId = Integer.parseInt(idStr);
                        // Loại trừ admin ngay cả khi được chọn thủ công
                        if (empId != 1) {
                            Employee emp = employeeDAO.getEmployeeByID(empId);
                            if (emp != null) {
                                employeesToProcess.add(emp);
                            }
                        }
                    } catch (NumberFormatException e) {
                        log("Invalid employee ID: " + idStr);
                    }
                }
            }

            if (employeesToProcess.isEmpty()) {
                throw new Exception("Không có nhân viên nào được chọn để tính lương (admin đã được loại trừ tự động)");
            }

            log("Bắt đầu tính lương cho " + employeesToProcess.size() + " nhân viên (đã loại trừ admin)");

            // Tính lương cho từng nhân viên
            BigDecimal totalSalary = BigDecimal.ZERO;
            int processedCount = 0;
            int skippedCount = 0;

            for (Employee emp : employeesToProcess) {
                try {
                    // Kiểm tra cơ bản trước khi tính lương

                    // 1. Kiểm tra có salary setting không
                    SalarySetting salarySetting = salarySettingDAO.getSalarySettingByEmployee(emp.getId());
                    if (salarySetting == null) {
                        log("Employee " + emp.getId() + " (" + emp.getFullname() + "): Không có cấu hình lương - bỏ qua");
                        skippedCount++;
                        continue;
                    }

                    // 2. Kiểm tra có ca làm việc trong kỳ không (chỉ áp dụng cho lương theo ca/giờ)
                    String salaryType = salarySetting.getSalaryType();

                    if ("PerShift".equals(salaryType) || "PerHour".equals(salaryType)) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        String startDateStr = sdf.format(workStart);
                        String endDateStr = sdf.format(workEnd);

                        Vector<WorkSchedule> schedules = workScheduleDAO.getWorkScheduleByDateAndEmployee(
                                emp.getId(), startDateStr, endDateStr);

                        if (schedules == null || schedules.isEmpty()) {
                            log("Employee " + emp.getId() + " (" + emp.getFullname() + "): Lương " + salaryType + " nhưng không có ca làm việc trong kỳ - bỏ qua");
                            skippedCount++;
                            continue;
                        }
                    }
                    // Lương cố định tháng (FixedMonthly) không cần kiểm tra ca làm việc

                    // 3. Tính lương dựa trên WorkSchedule và SalarySetting
                    BigDecimal basicSalary = calculateEmployeeSalary(emp.getId(), workStart, workEnd, salaryPeriod);

                    // 4. Kiểm tra lương phải lớn hơn 0
                    if (basicSalary.compareTo(BigDecimal.ZERO) <= 0) {
                        log("Employee " + emp.getId() + " (" + emp.getFullname() + "): Lương tính được bằng 0 - bỏ qua");
                        skippedCount++;
                        continue;
                    }

                    // 5. Tạo SalaryDetail chỉ khi đã pass tất cả kiểm tra
                    SalaryDetail detail = new SalaryDetail();
                    detail.setSalaryID(salaryID);
                    detail.setEmployeeID(emp.getId());
                    detail.setBasicSalary(basicSalary);

                    // Insert vào DB
                    salaryDetailDAO.insertSalaryDetail(detail);
                    totalSalary = totalSalary.add(basicSalary);
                    processedCount++;

                    // Log thông tin chi tiết
                    String salaryDescription = generateSalaryDescription(emp.getId(), workStart, workEnd, salaryPeriod);
                    log("Employee " + emp.getId() + " (" + emp.getFullname() + "): " + salaryDescription + " = " + basicSalary);

                } catch (Exception e) {
                    log("Error processing salary for employee " + emp.getId() + ": " + e.getMessage());
                    skippedCount++;
                }
            }

            if (processedCount == 0) {
                throw new Exception("Không có nhân viên nào đủ điều kiện để tính lương. Vui lòng kiểm tra:\n"
                        + "- Nhân viên có cấu hình lương chưa?\n"
                        + "- Nhân viên có ca làm việc trong kỳ chưa?");
            }

            // Cập nhật tổng lương
            dao.updateTotalSalary(salaryID, totalSalary);

            // Thông báo kết quả
            String message = String.format("Tạo bảng lương thành công! Đã xử lý %d/%d nhân viên.",
                    processedCount, employeesToProcess.size());
            if (skippedCount > 0) {
                message += String.format(" (%d nhân viên bị bỏ qua do không đủ điều kiện)", skippedCount);
            }

            session.setAttribute("message", message);
            session.setAttribute("messageType", "success");
            response.sendRedirect("SalaryController?service=list");

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("message", "Lỗi khi tạo bảng lương: " + e.getMessage());
            request.setAttribute("messageType", "error");
            listSalary(request, response);
        }
    }

    private String generateSalaryName(String salaryPeriod, Date start, Date end) {
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.setTime(start);
        int month = cal.get(java.util.Calendar.MONTH) + 1;
        int year = cal.get(java.util.Calendar.YEAR);

        if ("Hàng tháng".equals(salaryPeriod)) {
            return "Bảng lương tháng " + month + "/" + year;
        } else if ("Hàng tuần".equals(salaryPeriod)) {
            int week = cal.get(java.util.Calendar.WEEK_OF_MONTH);
            return "Bảng lương tuần " + week + " tháng " + month + "/" + year;
        } else {
            return "Bảng lương " + salaryPeriod;
        }
    }

    private void deleteSalary(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        try {
            String idStr = request.getParameter("id");
            if (idStr == null || idStr.trim().isEmpty()) {
                out.print("{\"success\":false,\"message\":\"ID không hợp lệ\"}");
                return;
            }

            int id = Integer.parseInt(idStr);

            // Kiểm tra xem bảng lương có tồn tại không
            Salary existingSalary = dao.searchSalary(id);
            if (existingSalary == null) {
                out.print("{\"success\":false,\"message\":\"Không tìm thấy bảng lương\"}");
                return;
            }

            // Kiểm tra xem bảng lương có thể xóa không (có thể thêm logic kiểm tra)
            if ("Đã trả".equals(existingSalary.getStatus())) {
                out.print("{\"success\":false,\"message\":\"Không thể xóa bảng lương đã trả\"}");
                return;
            }

            // Xóa các salary detail trước
            Vector<SalaryDetail> details = salaryDetailDAO.getSalaryDetailBySalaryID(id);
            for (SalaryDetail detail : details) {
                salaryDetailDAO.deleteSalaryDetail(detail.getSalaryDetailID());
            }

            // Sau đó xóa salary
            int result = dao.deleteSalary(id);

            if (result > 0) {
                out.print("{\"success\":true,\"message\":\"Xóa thành công\"}");
            } else {
                out.print("{\"success\":false,\"message\":\"Xóa không thành công\"}");
            }

        } catch (NumberFormatException e) {
            out.print("{\"success\":false,\"message\":\"ID phải là số nguyên\"}");
        } catch (Exception e) {
            e.printStackTrace();
            out.print("{\"success\":false,\"message\":\"Lỗi hệ thống: " + e.getMessage() + "\"}");
        } finally {
            out.flush();
        }
    }

    private void showDetail(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String idStr = request.getParameter("id");
            if (idStr == null || idStr.trim().isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Thiếu ID bảng lương");
                return;
            }

            int id = Integer.parseInt(idStr);

            Salary salary = dao.searchSalary(id);
            if (salary != null) {
                // Lấy chi tiết bảng lương
                Vector<SalaryDetail> salaryDetails = salaryDetailDAO.getSalaryDetailBySalaryID(id);
                log("id: " + salaryDetails.get(1).getEmployeeID());
                // Truyền danh sách nhân viên và cấu hình lương
                request.setAttribute("salary", salary);
                request.setAttribute("salaryDetails", salaryDetails);
                request.setAttribute("vectorE", employees);
                request.setAttribute("vectorSS", listSalarySetting);

                request.getRequestDispatcher("TransferReceiptJSP/ListSalaryDetail.jsp").forward(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Không tìm thấy bảng lương với ID: " + id);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID phải là số nguyên hợp lệ");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi hệ thống: " + e.getMessage());
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Salary Controller - Quản lý bảng lương";
    }// </editor-fold>
}
