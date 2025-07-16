package Controller;

import Context.DBContext;
import DTO.EmployeeDto;
import Dal.EmployeeDAO;
import Dal.SalarySettingDAO;
import Models.SalarySetting;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "SalarySettingController", urlPatterns = {"/SalarySettingController", "/SalarySetting"})
public class SalarySettingController extends HttpServlet {

    DBContext connection = new DBContext("Test");
    SalarySettingDAO salarySettingDAO = new SalarySettingDAO(connection.getConnection());
    EmployeeDAO employeeDAO = new EmployeeDAO(connection.getConnection());

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String service = request.getParameter("service");

        if (service == null) {
            listSalarySetting(request, response);
        } else {
            switch (service) {
                case "list":
                    listSalarySetting(request, response);
                    break;
                case "add":
                    addSalarySetting(request, response);
                    break;
                case "update":
                    updateSalarySetting(request, response);
                    break;
                case "delete":
                    deleteSalarySetting(request, response);
                    break;
                case "search":
                    searchSalarySetting(request, response);
                    break;
                default:
                    listSalarySetting(request, response);
                    break;
            }
        }
    }

    private void listSalarySetting(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Vector<SalarySetting> salarySettings = salarySettingDAO.getAllSalarySettingWithEmployeeInfo();
            List<EmployeeDto> employees = employeeDAO.listAllEmployeeDTO();
            employees.removeIf(emp -> emp.getId() == 1);

            String[] salaryTypes = {"PerShift", "PerHour", "FixedMonthly"};
            String[] salaryTypeNames = {"Theo ca làm việc", "Theo giờ", "Lương cố định tháng"};

            request.setAttribute("salarySettings", salarySettings);
            request.setAttribute("employees", employees);
            request.setAttribute("salaryTypes", salaryTypes);
            request.setAttribute("salaryTypeNames", salaryTypeNames);

            String message = (String) request.getSession().getAttribute("message");
            String messageType = (String) request.getSession().getAttribute("messageType");
            if (message != null) {
                request.setAttribute("message", message);
                request.setAttribute("messageType", messageType);
                request.getSession().removeAttribute("message");
                request.getSession().removeAttribute("messageType");
            }

            request.getRequestDispatcher("TransferReceiptJSP/SalarySetting.jsp").forward(request, response);

        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi khi tải dữ liệu: " + e.getMessage());
            request.getRequestDispatcher("TransferReceiptJSP/SalarySetting.jsp").forward(request, response);
        }
    }

    private void addSalarySetting(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int employeeId = Integer.parseInt(request.getParameter("employeeId"));
            String salaryType = request.getParameter("salaryType");
            BigDecimal amount = new BigDecimal(request.getParameter("amount"));

            SalarySetting existingSetting = salarySettingDAO.getSalarySettingByEmployee(employeeId);

            if (existingSetting != null) {
                existingSetting.setSalaryType(salaryType);
                existingSetting.setAmount(amount);
                int result = salarySettingDAO.updateSalarySetting(existingSetting);

                if (result > 0) {
                    request.getSession().setAttribute("message", "Nhân viên đã có thiết lập lương. Đã cập nhật thành công!");
                    request.getSession().setAttribute("messageType", "success");
                } else {
                    request.getSession().setAttribute("message", "Cập nhật thiết lập lương thất bại!");
                    request.getSession().setAttribute("messageType", "error");
                }
            } else {
                SalarySetting salarySetting = new SalarySetting();
                salarySetting.setEmployeeID(employeeId);
                salarySetting.setSalaryType(salaryType);
                salarySetting.setAmount(amount);

                int result = salarySettingDAO.insertSalarySetting(salarySetting);

                if (result > 0) {
                    request.getSession().setAttribute("message", "Thêm thiết lập lương thành công!");
                    request.getSession().setAttribute("messageType", "success");
                } else {
                    request.getSession().setAttribute("message", "Thêm thiết lập lương thất bại!");
                    request.getSession().setAttribute("messageType", "error");
                }
            }

        } catch (NumberFormatException e) {
            request.getSession().setAttribute("message", "Dữ liệu không hợp lệ!");
            request.getSession().setAttribute("messageType", "error");
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("message", "Lỗi: " + e.getMessage());
            request.getSession().setAttribute("messageType", "error");
        }

        response.sendRedirect("SalarySetting");
    }

    private void updateSalarySetting(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int salarySettingId = Integer.parseInt(request.getParameter("salarySettingId"));
            int employeeId = Integer.parseInt(request.getParameter("employeeId"));
            String salaryType = request.getParameter("salaryType");
            BigDecimal amount = new BigDecimal(request.getParameter("amount"));

            SalarySetting salarySetting = new SalarySetting();
            salarySetting.setSalarySettingID(salarySettingId);
            salarySetting.setEmployeeID(employeeId);
            salarySetting.setSalaryType(salaryType);
            salarySetting.setAmount(amount);

            int result = salarySettingDAO.updateSalarySetting(salarySetting);

            if (result > 0) {
                request.getSession().setAttribute("message", "Cập nhật thiết lập lương thành công!");
                request.getSession().setAttribute("messageType", "success");
            } else {
                request.getSession().setAttribute("message", "Cập nhật thiết lập lương thất bại!");
                request.getSession().setAttribute("messageType", "error");
            }

        } catch (NumberFormatException e) {
            request.getSession().setAttribute("message", "Dữ liệu không hợp lệ!");
            request.getSession().setAttribute("messageType", "error");
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("message", "Lỗi: " + e.getMessage());
            request.getSession().setAttribute("messageType", "error");
        }

        response.sendRedirect("SalarySetting");
    }

    private void deleteSalarySetting(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int salarySettingId = Integer.parseInt(request.getParameter("salarySettingId"));
            int result = salarySettingDAO.deleteSalarySetting(salarySettingId);

            if (result > 0) {
                request.getSession().setAttribute("message", "Xóa thiết lập lương thành công!");
                request.getSession().setAttribute("messageType", "success");
            } else {
                request.getSession().setAttribute("message", "Xóa thiết lập lương thất bại!");
                request.getSession().setAttribute("messageType", "error");
            }

        } catch (NumberFormatException e) {
            request.getSession().setAttribute("message", "ID không hợp lệ!");
            request.getSession().setAttribute("messageType", "error");
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("message", "Lỗi: " + e.getMessage());
            request.getSession().setAttribute("messageType", "error");
        }

        response.sendRedirect("SalarySetting");
    }

    private void searchSalarySetting(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String search = request.getParameter("search");
            Vector<SalarySetting> salarySettings = null;
            if (search!= null) {
                salarySettings = salarySettingDAO.searchByEmployeeName(search);
                
            }else{
                response.sendRedirect("SalarySettingController");
            }

            List<EmployeeDto> employees = employeeDAO.listAllEmployeeDTO();
            employees.removeIf(emp -> emp.getId() == 1);

            String[] salaryTypes = {"PerShift", "PerHour", "FixedMonthly"};
            String[] salaryTypeNames = {"Theo ca làm việc", "Theo giờ", "Lương cố định tháng"};

            request.setAttribute("salarySettings", salarySettings);
            request.setAttribute("employees", employees);
            request.setAttribute("salaryTypes", salaryTypes);
            request.setAttribute("salaryTypeNames", salaryTypeNames);

            request.getRequestDispatcher("TransferReceiptJSP/SalarySetting.jsp").forward(request, response);

        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi khi tìm kiếm: " + e.getMessage());
            listSalarySetting(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
