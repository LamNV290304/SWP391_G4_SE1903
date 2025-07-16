package Controller;

import Context.DBContext;
import Dal.EmployeeDAO;
import Dal.SalarySettingDAO;
import Dal.ShiftDAO;
import Dal.ShopDAO;
import Dal.WorkScheduleDAO;
import Models.Employee;
import Models.SalarySetting;
import Models.Shift;
import Models.Shop;
import Models.WorkSchedule;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.text.NumberFormat;

@WebServlet(name = "WorkScheduleController", urlPatterns = {"/WorkSchedule"})
public class WorkScheduleController extends HttpServlet {

    DBContext connection = new DBContext("Test");
    WorkScheduleDAO workScheduleDAO = new WorkScheduleDAO(connection.getConnection());
    EmployeeDAO employeeDAO = new EmployeeDAO(connection.getConnection());
    ShiftDAO shiftDAO = new ShiftDAO(connection.getConnection());

    private static final String sqlList = "SELECT * FROM [dbo].[WorkSchedule]";

    List<Employee> employees = employeeDAO.getEmployee();
    Vector<Shift> listShift = shiftDAO.getAllShift("SELECT * FROM [dbo].[Shift]");

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException {

        String service = request.getParameter("service");

        if (service == null) {
            workSchedule(request, response);
        } else {
            switch (service) {
                case "workSchedule":
                    workSchedule(request, response);
                    break;
                case "addWorkSchedule":
                    addWorkSchedule(request, response);
                    break;
                case "updateWorkSchedule":
                    updateWorkSchedule(request, response);
                    break;
                case "removeWorkSchedule":
                    removeWorkSchedule(request, response);
                    break;
                case "navigateWeek":
                    navigateWeek(request, response);
                    break;
                case "goToCurrentWeek":
                    goToCurrentWeek(request, response);
                    break;
                

            }
        }
    }


    private double calculateWeeklySalary(int employeeId, LocalDate weekStart, LocalDate weekEnd) {
        try {
            // Get salary setting for employee
            SalarySettingDAO salarySettingDAO = new SalarySettingDAO(connection.getConnection());
            SalarySetting salarySetting = salarySettingDAO.getSalarySettingByEmployee(employeeId);

            if (salarySetting == null) {
                return 0.0; // No salary setting found
            }

            // Count work schedules for this employee in the week
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String startDateStr = sdf.format(java.sql.Date.valueOf(weekStart));
            String endDateStr = sdf.format(java.sql.Date.valueOf(weekEnd));

            Vector<WorkSchedule> weekSchedules = workScheduleDAO.getWorkScheduleByDate(startDateStr, endDateStr);

            // Filter schedules for this employee
            int workShiftsCount = 0;
            double totalHours = 0.0;

            for (WorkSchedule ws : weekSchedules) {
                if (ws.getEmployeeID() == employeeId) {
                    workShiftsCount++;

                    // Calculate hours if needed for PerHour salary type
                    if ("PerHour".equals(salarySetting.getSalaryType())) {
                        // Find shift details to calculate hours
                        for (Shift shift : listShift) {
                            if (shift.getShiftID() == ws.getShiftID()) {
                                // Calculate hours between start and end time
                                long startSeconds = shift.getStartTime().toSecondOfDay();
                                long endSeconds = shift.getEndTime().toSecondOfDay();
                                double hours = (endSeconds - startSeconds) / 3600.0; // Convert to hours
                                totalHours += hours;
                                break;
                            }
                        }
                    }
                }
            }

            // Calculate salary based on type
            BigDecimal amount = salarySetting.getAmount();
            double weeklySalary = 0.0;

            switch (salarySetting.getSalaryType()) {
                case "PerShift":
                    weeklySalary = amount.doubleValue() * workShiftsCount;
                    break;
                case "PerHour":
                    weeklySalary = amount.doubleValue() * totalHours;
                    break;
                case "FixedMonthly":
                    // For monthly salary, calculate weekly portion (monthly / 4.33 weeks average)
                    weeklySalary = amount.doubleValue() / 4.33;
                    break;
                default:
                    weeklySalary = 0.0;
            }

            return weeklySalary;

        } catch (Exception e) {
            e.printStackTrace();
            return 0.0;
        }
    }

    // Phương thức format tiền tệ
    private String formatCurrency(double amount) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        return formatter.format(amount);
    }

    // Phương thức điều hướng tuần
    private void navigateWeek(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int direction = Integer.parseInt(request.getParameter("direction")); // -1 hoặc 1

            // Lấy tuần hiện tại từ session, nếu không có thì dùng tuần hiện tại
            LocalDate currentWeekStart = (LocalDate) request.getSession().getAttribute("currentWeekStart");
            if (currentWeekStart == null) {
                currentWeekStart = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
            }

            // Tính tuần mới
            LocalDate newWeekStart = currentWeekStart.plusWeeks(direction);
            request.getSession().setAttribute("currentWeekStart", newWeekStart);

        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        // Redirect về trang chính để hiển thị tuần mới, preserve view
        String view = request.getParameter("view");
        if (view != null && !view.trim().isEmpty()) {
            response.sendRedirect("WorkSchedule?view=" + view);
        } else {
            response.sendRedirect("WorkSchedule");
        }
    }

    // Phương thức về tuần hiện tại
    private void goToCurrentWeek(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Xóa tuần đã lưu trong session để về tuần hiện tại
        request.getSession().removeAttribute("currentWeekStart");

        // Redirect về trang chính, preserve view
        String view = request.getParameter("view");
        if (view != null && !view.trim().isEmpty()) {
            response.sendRedirect("WorkSchedule?view=" + view);
        } else {
            response.sendRedirect("WorkSchedule");
        }
    }

    // Phương thức tạo thông tin tuần   
    private Map<String, Object> getWeekInfo(LocalDate weekStart) {
        Map<String, Object> weekInfo = new HashMap<>();

        // Tính tuần trong năm
        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        int weekOfYear = weekStart.get(weekFields.weekOfWeekBasedYear());
        int year = weekStart.getYear();

        // Ngày cuối tuần
        LocalDate weekEnd = weekStart.plusDays(6);

        // Format ngày
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // Tạo text hiển thị
        String weekText = String.format("Tuần %d - Tháng %d %d", weekOfYear, weekStart.getMonthValue(), year);
        String weekRange = String.format("%s - %s", weekStart.format(dateFormatter), weekEnd.format(dateFormatter));

        weekInfo.put("weekText", weekText);
        weekInfo.put("weekRange", weekRange);
        weekInfo.put("weekStart", weekStart);
        weekInfo.put("weekEnd", weekEnd);

        return weekInfo;
    }

    // Tạo danh sách ngày cho việc lặp lại lịch làm việc
    private List<Date> generateRepeatDates(String[] selectedDays, LocalDate baseDate)
            throws ParseException {
        List<Date> repeatDates = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        if (selectedDays == null || selectedDays.length == 0) {
            return repeatDates;
        }

        // Lấy ngày cuối tháng từ baseDate
        LocalDate endOfMonth = baseDate.with(TemporalAdjusters.lastDayOfMonth());

        // Tìm thứ Hai của tuần chứa baseDate
        LocalDate currentWeekStart = baseDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

        // Tạo Map để nhóm các ngày đã chọn theo thứ trong tuần
        Map<DayOfWeek, Boolean> selectedDaysOfWeek = new HashMap<>();

        for (String dateStr : selectedDays) {
            LocalDate date = LocalDate.parse(dateStr);
            DayOfWeek dayOfWeek = date.getDayOfWeek();
            selectedDaysOfWeek.put(dayOfWeek, true);
        }

        // Tạo lịch cho tất cả các tuần từ tuần hiện tại đến cuối tháng
        LocalDate currentWeek = currentWeekStart;
        while (currentWeek.isBefore(endOfMonth) || currentWeek.isEqual(endOfMonth)) {

            // Duyệt qua các ngày trong tuần hiện tại
            for (DayOfWeek dayOfWeek : selectedDaysOfWeek.keySet()) {
                LocalDate targetDate = currentWeek.with(dayOfWeek);

                // Chỉ tạo lịch nếu:
                // 1. Ngày nằm trong tháng hiện tại
                // 2. Ngày >= baseDate (từ ngày được chọn trở đi)
                // 3. Ngày >= ngày hiện tại (không tạo lịch cho quá khứ)
                if (!targetDate.isAfter(endOfMonth)
                        && !targetDate.isBefore(baseDate)
                        && !targetDate.isBefore(LocalDate.now())) {
                    repeatDates.add(sdf.parse(targetDate.toString()));
                }
            }

            // Chuyển sang tuần tiếp theo
            currentWeek = currentWeek.plusWeeks(1);
        }

        return repeatDates;
    }

    private void updateWorkSchedule(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException {
        try {
            int workScheduleId = Integer.parseInt(request.getParameter("workScheduleId"));
            int employeeID = Integer.parseInt(request.getParameter("employeeId"));
            String WorkDate = request.getParameter("dayOfWeek");
            int ShiftID = Integer.parseInt(request.getParameter("ShiftID"));

            // Kiểm tra xem có bật repeat weekly không
            String[] selectedDays = request.getParameterValues("selectedDays");
            boolean isRepeatWeekly = request.getParameter("repeatWeekly") != null;

            // Tìm shopID từ employee
            int shopID = 0;
            for (Employee e : employees) {
                if (e.getId() == employeeID) {
                    shopID = e.getShopId();
                    break;
                }
            }

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            LocalDate baseDate = LocalDate.parse(WorkDate);

            int successCount = 0;
            int totalCount = 0;

            if (isRepeatWeekly && selectedDays != null && selectedDays.length > 0) {
                // Cập nhật và tạo thêm lịch cho nhiều ngày trong tháng
                List<Date> repeatDates = generateRepeatDates(selectedDays, baseDate);

                boolean hasUpdatedOriginal = false;

                for (Date date : repeatDates) {
                    String dateStr = sdf.format(date);

                    // Nếu là ngày gốc, cập nhật thay vì tạo mới
                    if (dateStr.equals(WorkDate) && !hasUpdatedOriginal) {
                        WorkSchedule workSchedule = new WorkSchedule();
                        workSchedule.setWorkScheduleID(workScheduleId);
                        workSchedule.setEmployeeID(employeeID);
                        workSchedule.setShopID(shopID);
                        workSchedule.setShiftID(ShiftID);
                        workSchedule.setWorkDate(date);
                        workSchedule.setStatus(1);
                        workSchedule.setNote("Updated with monthly repeat");
                        workSchedule.setCreatedBy("ADMIN");

                        int result = workScheduleDAO.updateWorkSchedule(workSchedule);
                        if (result > 0) {
                            successCount++;
                        }
                        hasUpdatedOriginal = true;
                    } else {
                        // Tạo lịch mới cho các ngày khác nếu chưa tồn tại
                        if (!workScheduleDAO.isScheduleExist(employeeID, dateStr, ShiftID)) {
                            WorkSchedule w = new WorkSchedule(employeeID, shopID, ShiftID, date, 1, "Monthly repeat from update", "ADMIN");
                            int result = workScheduleDAO.insertWorkSchedule(w);
                            if (result > 0) {
                                successCount++;
                            }
                        }
                    }
                    totalCount++;
                }

                if (successCount > 0) {
                    request.getSession().setAttribute("message",
                            String.format("Đã cập nhật/tạo %d/%d lịch làm việc thành công cho cả tháng!", successCount, totalCount));
                    request.getSession().setAttribute("messageType", "success");
                } else {
                    request.getSession().setAttribute("message", "Không có lịch nào được cập nhật.");
                    request.getSession().setAttribute("messageType", "warning");
                }
            } else {
                // Cập nhật lịch đơn lẻ
                Date utilDate = sdf.parse(WorkDate);

                WorkSchedule workSchedule = new WorkSchedule();
                workSchedule.setWorkScheduleID(workScheduleId);
                workSchedule.setEmployeeID(employeeID);
                workSchedule.setShopID(shopID);
                workSchedule.setShiftID(ShiftID);
                workSchedule.setWorkDate(utilDate);
                workSchedule.setStatus(1);
                workSchedule.setNote("Updated single");
                workSchedule.setCreatedBy("ADMIN");

                int result = workScheduleDAO.updateWorkSchedule(workSchedule);

                if (result > 0) {
                    request.getSession().setAttribute("message", "Cập nhật lịch làm việc thành công!");
                    request.getSession().setAttribute("messageType", "success");
                } else {
                    request.getSession().setAttribute("message", "Cập nhật lịch làm việc thất bại!");
                    request.getSession().setAttribute("messageType", "error");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("message", "Lỗi: " + e.getMessage());
            request.getSession().setAttribute("messageType", "error");
        }

        response.sendRedirect("WorkSchedule");
    }

    private void removeWorkSchedule(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException {
        try {
            int WorkScheduleID = Integer.parseInt(request.getParameter("workScheduleId"));

            int result = workScheduleDAO.deleteWorkSchedule(WorkScheduleID);

            if (result > 0) {
                request.getSession().setAttribute("message", "Xóa lịch làm việc thành công!");
                request.getSession().setAttribute("messageType", "success");
            } else {
                request.getSession().setAttribute("message", "Xóa lịch làm việc thất bại!");
                request.getSession().setAttribute("messageType", "error");
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("message", "Lỗi: " + e.getMessage());
            request.getSession().setAttribute("messageType", "error");
        }

        response.sendRedirect("WorkSchedule");
    }

    private boolean checkNumberOfEmployeeInShift(WorkSchedule w) {
        try {
            // Kiểm tra shift có tồn tại không
            Shift shift = shiftDAO.searchShift(w.getShiftID());
            if (shift == null) {
                return false;
            }

            int maxNumberOfEmployee = shift.getNumberOfEmployees();

            // Format ngày đúng cách
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String workDateStr = sdf.format(w.getWorkDate());

            // Hoặc nếu DAO không hỗ trợ parameterized query, ít nhất format an toàn:
            String checkList = sqlList + " WHERE ShiftID = " + w.getShiftID()
                    + " AND ShopID = " + w.getShopID()
                    + " AND WorkDate = '" + workDateStr + "'";

            List<WorkSchedule> currentSchedules = workScheduleDAO.getAllWorkSchedule(checkList);

            return currentSchedules.size() < maxNumberOfEmployee;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void addWorkSchedule(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ParseException {
        try {

            int employeeID = Integer.parseInt(request.getParameter("employeeId"));
            String WorkDate = request.getParameter("dayOfWeek");
            int ShiftID = Integer.parseInt(request.getParameter("ShiftID"));

            // Kiểm tra xem có bật repeat weekly không
            String[] selectedDays = request.getParameterValues("selectedDays");
            boolean isRepeatWeekly = selectedDays != null && selectedDays.length > 0;

            // Tìm shopID từ employee
            int shopID = 0;
            for (Employee e : employees) {
                if (e.getId() == employeeID) {
                    shopID = e.getShopId();
                    break;
                }
            }

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            LocalDate baseDate = LocalDate.parse(WorkDate);
            int successCount = 0;
            int duplicateCount = 0;

            if (isRepeatWeekly) {
                // Thêm lịch cho nhiều ngày trong tháng
                List<Date> repeatDates = generateRepeatDates(selectedDays, baseDate);

                for (Date date : repeatDates) {
                    try {
                        String dateStr = sdf.format(date);

                        // Kiểm tra xem đã có lịch cho ngày này chưa
                        if (!workScheduleDAO.isScheduleExist(employeeID, dateStr, ShiftID)) {
                            WorkSchedule w = new WorkSchedule(employeeID, shopID, ShiftID, date, 1, "Monthly repeat", "ADMIN");
                            if (checkNumberOfEmployeeInShift(w)) {
                                int result = workScheduleDAO.insertWorkSchedule(w);
                                if (result > 0) {
                                    successCount++;
                                }
                            }
                        } else {
                            duplicateCount++;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                // Tạo thông báo chi tiết
                StringBuilder message = new StringBuilder();
                if (successCount > 0) {
                    message.append(String.format("Đã thêm %d lịch làm việc thành công cho cả tháng!", successCount));
                }
                if (duplicateCount > 0) {
                    if (message.length() > 0) {
                        message.append(" ");
                    }
                    message.append(String.format("(%d lịch đã tồn tại)", duplicateCount));
                }

                if (successCount > 0) {
                    request.getSession().setAttribute("message", message.toString());
                    request.getSession().setAttribute("messageType", "success");
                } else if (duplicateCount > 0) {
                    request.getSession().setAttribute("message", "Tất cả lịch đã tồn tại trước đó.");
                    request.getSession().setAttribute("messageType", "warning");
                } else {
                    request.getSession().setAttribute("message", "Không thể thêm lịch làm việc.");
                    request.getSession().setAttribute("messageType", "error");
                }
            } else {
                // Thêm lịch cho 1 ngày
                if (!workScheduleDAO.isScheduleExist(employeeID, WorkDate, ShiftID)) {
                    Date utilDate = sdf.parse(WorkDate);
                    WorkSchedule w = new WorkSchedule(employeeID, shopID, ShiftID, utilDate, 1, "Single day", "ADMIN");
                    int result = 0;
                    if (checkNumberOfEmployeeInShift(w)) {
                        result = workScheduleDAO.insertWorkSchedule(w);
                    }
                    if (result > 0) {
                        request.getSession().setAttribute("message", "Thêm lịch làm việc thành công!");
                        request.getSession().setAttribute("messageType", "success");
                    } else {
                        request.getSession().setAttribute("message", "Thêm lịch làm việc thất bại!");
                        request.getSession().setAttribute("messageType", "error");
                    }
                } else {
                    request.getSession().setAttribute("message", "Đã tồn tại lịch làm việc cho ngày và ca này!");
                    request.getSession().setAttribute("messageType", "warning");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("message", "Lỗi: " + e.getMessage());
            request.getSession().setAttribute("messageType", "error");
        }

        response.sendRedirect("WorkSchedule");
    }

    private void workSchedule(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Lấy view parameter, mặc định là "employee" nếu không có
        String view = request.getParameter("view");
        if (view == null || view.trim().isEmpty()) {
            view = "employee"; // Mặc định là employee view
        }

        List<Employee> ListEmployee = new ArrayList<>();

        String submit = request.getParameter("submit");
        String name = request.getParameter("search");
        if (submit != null) {
            employees = employeeDAO.searchEmployeesByName(name);
        }

        for (Employee employee : employees) {
            if (employee.getId() != 1) {
                ListEmployee.add(employee);
            }
        }

        // Lấy tuần hiện tại từ session hoặc sử dụng tuần hiện tại
        LocalDate currentWeekStart = (LocalDate) request.getSession().getAttribute("currentWeekStart");
        if (currentWeekStart == null) {
            currentWeekStart = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        }

        // Tạo thông tin tuần
        Map<String, Object> weekInfo = getWeekInfo(currentWeekStart);
        LocalDate weekEnd = currentWeekStart.plusDays(6);

        // Tạo map cho các ngày trong tuần hiện tại
        Map<String, String> DayInWeek = new HashMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE", new Locale("vi", "VN"));
        DateTimeFormatter formatterDAY = DateTimeFormatter.ofPattern("yyyy-MM-dd", new Locale("vi", "VN"));

        for (int i = 0; i < 7; i++) {
            LocalDate date = currentWeekStart.plusDays(i);
            DayInWeek.put(date.format(formatterDAY), date.format(formatter));
        }

        // Sắp xếp theo thứ tự ngày
        Map<String, String> weekDays = DayInWeek.entrySet()
                .stream()
                .sorted(Comparator.comparing(Map.Entry::getKey))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));

        // Lọc dữ liệu work schedule theo tuần hiện tại
        Vector<WorkSchedule> filteredList = new Vector<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        for (WorkSchedule ws : workScheduleDAO.getAllWorkSchedule(sqlList)) {
            String workDateStr = sdf.format(ws.getWorkDate());
            if (weekDays.containsKey(workDateStr)) {
                filteredList.add(ws);
            }
        }

        // Tính lương cho từng nhân viên trong tuần hiện tại
        Map<Integer, String> employeeSalaries = new HashMap<>();
        for (Employee employee : ListEmployee) {
            double weeklySalary = calculateWeeklySalary(employee.getId(), currentWeekStart, weekEnd);
            employeeSalaries.put(employee.getId(), formatCurrency(weeklySalary));
        }

        // Set attributes
        request.setAttribute("weekDays", weekDays);
        request.setAttribute("weekInfo", weekInfo);
        request.setAttribute("ListShift", listShift);
        request.setAttribute("data", filteredList);
        request.setAttribute("ListEmployee", ListEmployee);
        request.setAttribute("employeeSalaries", employeeSalaries);
        request.setAttribute("currentView", view); // Thêm attribute này để JSP biết view hiện tại

        // Kiểm tra message từ session
        String message = (String) request.getSession().getAttribute("message");
        String messageType = (String) request.getSession().getAttribute("messageType");
        if (message != null) {
            request.setAttribute("message", message);
            request.setAttribute("messageType", messageType);
            request.getSession().removeAttribute("message");
            request.getSession().removeAttribute("messageType");
        }

        // Điều hướng đến JSP tương ứng
        if (view.equalsIgnoreCase("employee")) {
            request.getRequestDispatcher("TransferReceiptJSP/WorkSchedule.jsp").forward(request, response);
        } else if (view.equalsIgnoreCase("shift")) {
            request.getRequestDispatcher("TransferReceiptJSP/WorkScheduleByShift.jsp").forward(request, response);
        } else {
            // Mặc định chuyển về employee view nếu view không hợp lệ
            request.getRequestDispatcher("TransferReceiptJSP/WorkSchedule.jsp").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (ParseException ex) {
            Logger.getLogger(WorkScheduleController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (ParseException ex) {
            Logger.getLogger(WorkScheduleController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String getServletInfo() {
        return "Work Schedule Controller";
    }
}
